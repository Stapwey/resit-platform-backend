package com.resitplatform.application.command;

import com.resitplatform.api.command.ScheduleResit;
import com.resitplatform.api.command.ScheduleResitResult;
import com.resitplatform.application.ResitAssembler;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.exception.ForbiddenException;
import com.resitplatform.application.service.SlugService;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.ResitRepository;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ScheduleResitHandler implements CommandHandler<ScheduleResitResult, ScheduleResit> {
    private final ResitRepository resitRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    @Transactional
    @Override
    public ScheduleResitResult handle(ScheduleResit command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (!currentUser.getIs_teacher()) {
            throw ForbiddenException.forbidden("you are not a teacher");
        }

        // check if such name already exists
        Optional<Resit> resitByNameOptional = resitRepository.findByName(command.getName());
        if (resitByNameOptional.isPresent()) {
            throw BadRequestException.badRequest("resit [name=%s] already exists", command.getName());
        }

        Resit.ResitBuilder resitBuilder = Resit.builder()
                .responsibleTeacher(currentUser)
                .startDate(command.getStartDate())
                .image(command.getImage())
                .id(UUID.randomUUID())
                .slug(slugService.makeSlug(command.getName()))
                .name(command.getName())
                .hasEnded(false)
                .description(command.getDescription());

        Resit resit = resitRepository.save(resitBuilder.build());

        return new ScheduleResitResult(ResitAssembler.assemble(resit));
    }

}
