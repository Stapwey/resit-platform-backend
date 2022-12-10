
package com.resitplatform.rest;

import com.resitplatform.api.command.RegisterUser;
import com.resitplatform.api.dto.ProfileDto;
import com.resitplatform.api.dto.UserDto;
import com.resitplatform.api.query.GetProfilesResult;
import com.resitplatform.rest.auth.AuthSupport;
import com.resitplatform.rest.support.FeignBasedRestTest;
import com.resitplatform.api.operation.ProfileClient;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class ProfileApiTest extends FeignBasedRestTest {

    @Autowired
    private AuthSupport auth;

    @Autowired
    private ProfileClient profileClient;

    @AfterEach
    void afterEach() {
        auth.logout();
    }

    @Test
    void should_returnNull_when_userIsNotRegistered() {

        FeignException exception = catchThrowableOfType(
                () -> profileClient.findByUsername("u3"),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_returnCorrectData_when_userIsRegistered() {
        auth.register("u1", "u1@example.com", "1234");

        ProfileDto profile = profileClient.findByUsername("u1").getProfile();

        assertThat(profile.getUsername()).isEqualTo("u1");
    }


    @Test
    void should_returnCorrectData_onList() {
        auth.register("u1", "u1@example.com", "1234");

        GetProfilesResult profiles = profileClient.list();

        assertThat(profiles.getProfilesCount()).isEqualTo(1);
        assertThat(profiles.getProfiles().get(0).getUsername()).isEqualTo("u1");

    }

}
