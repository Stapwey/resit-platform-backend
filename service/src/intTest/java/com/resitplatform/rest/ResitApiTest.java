package com.resitplatform.rest;

import com.resitplatform.api.command.ScheduleResit;
import com.resitplatform.api.command.SignOffResit;
import com.resitplatform.api.command.SignOnResit;
import com.resitplatform.api.command.UpdateResit;
import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.api.operation.ResitClient;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.application.service.SlugService;
import com.resitplatform.rest.auth.AuthSupport;
import com.resitplatform.rest.support.FeignBasedRestTest;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class ResitApiTest extends FeignBasedRestTest {

    public static final String TEST_NAME = "test-name";
    public static final String TEST_SLUG = "test-name";
    public static final String TEST_IMAGE = "test-image";
    public static final String TEST_DESCRIPTION = "test-description";
    public static final Date TEST_DATE;

    public static final String ALTERED_NAME = "altered-name";
    public static final String ALTERED_DESCRIPTION = "altered-description";
    public static final String ALTERED_IMAGE = "test-image";
    public static final Date ALTERED_DATE;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, Calendar.FEBRUARY, 1, 1, 1);
        TEST_DATE = cal.getTime();
        cal.set(2022, Calendar.FEBRUARY, 1, 1, 1);
        ALTERED_DATE = cal.getTime();
    }

    @Autowired
    private AuthSupport auth;

    @Autowired
    private ResitClient resitClient;

    @Autowired
    private SlugService slugService;

    @AfterEach
    void afterEach() {
        auth.logout();
    }

    // general
    @Test
    void should_forbidWriteActionsForNonTeacher() {
        auth.register().login();

        UpdateResit updateCommand = UpdateResit.builder()
                .slug(TEST_SLUG)
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .build();

        ScheduleResit scheduleResit = getScheduleResitCommand();

        FeignException scheduleException = catchThrowableOfType(
                () -> resitClient.schedule(scheduleResit),
                FeignException.class
        );

        FeignException updateException = catchThrowableOfType(
                () -> resitClient.updateBySlug(updateCommand.getName(), updateCommand),
                FeignException.class
        );

        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.cancelBySlug(scheduleResit.getName()),
                FeignException.class
        );

        for (FeignException exception: new ArrayList<>(Arrays.asList(scheduleException, updateException, cancelException))) {
            assertThat(exception.status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            assertThat(exception).isNotNull();
        }

    }
    //
    // scheduling
    @Test
    void should_returnCorrectResitData_onSchedule() {
        AuthSupport.RegisteredUser user = auth.registerTeacher().login();

        ScheduleResit scheduleResitCommand = getScheduleResitCommand();
        ResitDto resit = resitClient.schedule(scheduleResitCommand).getResit();

        assertThat(resit.getSlug()).isEqualTo(
                slugService.makeSlug(scheduleResitCommand.getName())
        );
        assertThat(resit.getName()).isEqualTo(scheduleResitCommand.getName());
        assertThat(resit.getTeacherName()).isEqualTo(user.getUsername());
        assertThat(resit.getStartDate().getYear()).isEqualTo(scheduleResitCommand.getStartDate().getYear());
        assertThat(resit.getHasEnded()).isEqualTo(Boolean.FALSE);
        assertThat(resit.getDescription()).isEqualTo(scheduleResitCommand.getDescription());
        assertThat(resit.getImage()).isEqualTo(scheduleResitCommand.getImage());
        assertThat(resit.getParticipants()).isEmpty();
    }

    @Test
    void should_forbidResitWithExistingName_onSchedule() {
        auth.registerTeacher().login();

        ScheduleResit scheduleResitCommand = getScheduleResitCommand();
        resitClient.schedule(scheduleResitCommand);

        // try schedule the same one
        FeignException scheduleException = catchThrowableOfType(
                () -> resitClient.schedule(scheduleResitCommand),
                FeignException.class
        );
        assertThat(scheduleException.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    //
    // canceling
    @Test
    void should_returnCorrectResitData_onCancel() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        resitClient.cancelBySlug(created.getSlug());

        // assert that it is no more present
        auth.registerTeacher().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.findBySlug(created.getSlug()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_forbidNonResponsibleTeachers_onCancel() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        // login as another teacher
        auth.registerTeacher().login();
        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.cancelBySlug(created.getSlug()),
                FeignException.class
        );
        assertThat(cancelException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void should_returnCorrectNotExistingStatus_onCancel() {
        auth.registerTeacher().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.cancelBySlug("not-existing"),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    //
    // updating
    @Test
    void should_returnCorrectResitData_onUpdate() {
        AuthSupport.RegisteredUser user = auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        UpdateResit updateCommand = UpdateResit.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .startDate(ALTERED_DATE)
                .hasEnded(true)
                .build();

        ResitDto updated = resitClient.updateBySlug(created.getSlug(), updateCommand).getResit();
        assertThat(updated.getSlug()).isEqualTo(slugService.makeSlug(ALTERED_NAME));
        assertThat(updated.getTeacherName()).isEqualTo(user.getUsername());
        assertThat(updated.getName()).isEqualTo(ALTERED_NAME);
        assertThat(updated.getStartDate().getYear()).isEqualTo(ALTERED_DATE.getYear());
        assertThat(updated.getHasEnded()).isEqualTo(true);


        assertThat(updated.getDescription()).isEqualTo(ALTERED_DESCRIPTION);
        assertThat(updated.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    @Test
    void should_forbidNonresponsibleTeachers_onUpdate() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        UpdateResit updateCommand = UpdateResit.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .startDate(ALTERED_DATE)
                .hasEnded(true)
                .build();

        // login as another teacher
        auth.registerTeacher().login();
        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.updateBySlug(created.getSlug(), updateCommand),
                FeignException.class
        );
        assertThat(cancelException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }
    //
    //sign on resit
    @Test
    void should_returnCorrectData_onResitSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        AuthSupport.RegisteredUser user = auth.register().login();
        ResitDto resit = resitClient.signOn(created.getSlug(), new SignOnResit()).getResit();
        assertThat(resit.getParticipants().length).isEqualTo(1);
        assertThat(resit.getParticipants()[0].getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void should_refuseTeachers_onSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        FeignException signOnException = catchThrowableOfType(
                () -> resitClient.signOn(created.getSlug(), new SignOnResit()),
                FeignException.class
        );
        assertThat(signOnException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void should_returnCorrectNotExistingStatus_onSignOn() {
        auth.register().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOn("not-existing", new SignOnResit()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_refuseAlreadySignedOn_onSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        AuthSupport.RegisteredUser user = auth.register().login();
        resitClient.signOn(created.getSlug(), new SignOnResit());
        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOn(created.getSlug(), new SignOnResit()),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void should_refuseForEndedResits_onSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();
        resitClient.updateBySlug(created.getSlug(), UpdateResit.builder().hasEnded(true).name(created.getName()).build());

        auth.register().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOn(created.getSlug(), new SignOnResit()),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
    //
    // sign off resit
    @Test
    void should_returnCorrectData_onResitSignOff() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        resitClient.signOn(created.getSlug(), new SignOnResit());
        ResitDto resit = resitClient.signOff(created.getSlug(), new SignOffResit()).getResit();

        assertThat(resit.getParticipants()).isEmpty();
    }

    @Test
    void should_returnCorrectNotExistingStatus_onSignOff() {
        auth.register().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOff("not-existing", new SignOffResit()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_refuseNotSignedOn_onSignOff() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOff(created.getSlug(), new SignOffResit()),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
    //
    // find resit by slug test
    @Test
    void should_returnCorrectResitData_onSearchBySlug() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        ResitDto foundResit = resitClient.findBySlug(created.getSlug()).getResit();
        assertThat(foundResit.getSlug()).isEqualTo(created.getSlug());
    }

    @Test
    void should_return404forNotExisting_onSearchBySlug() {
        auth.register().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.findBySlug("not-existing"),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    //
    // find resits with filters
    @Test
    void should_returnCorrectResits_onSearchByFilters() {
        auth.registerTeacher().login();
        ResitDto firstCreated = resitClient.schedule(getScheduleResitCommand()
                .toBuilder()
                .name("...test_pattern...")
                .build()
        ).getResit();
        ResitDto secondCreated = resitClient.schedule(getScheduleResitCommand()).getResit();


        auth.registerTeacher().login();
        ResitDto thirdCreated = resitClient.schedule(getScheduleResitCommand()
                .toBuilder()
                .name("abc_test_pattern_abc")
                .build()
        ).getResit();
        ResitDto fourthCreated = resitClient.schedule(getScheduleResitCommand()).getResit();

        List<ResitDto> foundByTeacherName = resitClient.findByFilters(
                20,
                0,
                GetResits.builder()
                        .teacherName(thirdCreated.getTeacherName())
                        .build()
        ).getResits();

        assertThat(foundByTeacherName.size()).isEqualTo(2);
        assertThat(foundByTeacherName.get(0).getName()).isIn(thirdCreated.getName(), fourthCreated.getName());
        assertThat(foundByTeacherName.get(1).getName()).isIn(thirdCreated.getName(), fourthCreated.getName());


        List<ResitDto> foundByName = resitClient.findByFilters(
                20,
                0,
                GetResits.builder()
                        .name("test_pattern")
                        .build()
        ).getResits();

        assertThat(foundByName.size()).isEqualTo(2);
        assertThat(foundByName.get(0).getName()).isIn(firstCreated.getName(), thirdCreated.getName());
        assertThat(foundByName.get(1).getName()).isIn(firstCreated.getName(), thirdCreated.getName());

        AuthSupport.RegisteredUser firstStudent = auth.register().login();
        resitClient.signOn(firstCreated.getSlug(), new SignOnResit());

        AuthSupport.RegisteredUser secondStudent = auth.register().login();
        resitClient.signOn(thirdCreated.getSlug(), new SignOnResit());

        List<ResitDto> foundByParticipants = resitClient.findByFilters(
                20,
                0,
                GetResits.builder()
                        .participantNames(new String[]{firstStudent.getUsername(), secondStudent.getUsername()})
                        .build()
        ).getResits();

        assertThat(foundByParticipants.size()).isEqualTo(2);
        assertThat(foundByParticipants.get(0).getName()).isIn(thirdCreated.getName(), firstCreated.getName());
        assertThat(foundByParticipants.get(1).getName()).isIn(thirdCreated.getName(), firstCreated.getName());

        List<ResitDto> foundBySeveralFields = resitClient.findByFilters(
                20,
                0,
                GetResits.builder()
                        .teacherName(thirdCreated.getTeacherName())
                        .participantNames(new String[]{firstStudent.getUsername(), secondStudent.getUsername()})
                        .name("test_pattern")
                        .build()
        ).getResits();

        assertThat(foundBySeveralFields.size()).isEqualTo(1);
        assertThat(foundBySeveralFields.get(0).getName()).isEqualTo(thirdCreated.getName());
    }


    public static ScheduleResit getScheduleResitCommand() {
        return ScheduleResit.builder()
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .image(UUID.randomUUID().toString())
                .startDate(TEST_DATE)
                .build();
    }

}
