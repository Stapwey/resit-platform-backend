package com.resitplatform.application.command;

import com.resitplatform.api.command.UpdateResit;
import com.resitplatform.api.command.UpdateResitResult;
import com.resitplatform.application.ResitAssembler;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.exception.ForbiddenException;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.application.service.SlugService;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.ResitRepository;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateResitHandler implements CommandHandler<UpdateResitResult, UpdateResit> {

    private final ResitRepository resitRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;


    @Override
    public UpdateResitResult handle(UpdateResit command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (!currentUser.getIs_teacher()) {
            throw ForbiddenException.forbidden("you are not a teacher");
        }

        Resit resitBySlug = resitRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No resit with slug \"[slug=%s]\" found", command.getSlug()));

        if (resitBySlug.getResponsibleTeacher().getId().compareTo(currentUser.getId()) != 0) {
            throw ForbiddenException.forbidden("you are not responsible for this resit");
        }

        Resit updatedResit = resitBySlug.toBuilder()
                .image(command.getImage() != null ? command.getImage() : resitBySlug.getImage())
                .startDate(command.getStartDate() != null ? command.getStartDate() : resitBySlug.getStartDate())
                .hasEnded(command.getHasEnded() != null ? command.getHasEnded() : resitBySlug.getHasEnded())
                .description(command.getDescription() != null ? command.getDescription() : resitBySlug.getDescription())
                .name(command.getName())
                .slug(slugService.makeSlug(command.getName()))
                .build();

        resitRepository.save(updatedResit);
        return new UpdateResitResult(ResitAssembler.assemble(updatedResit));
    }
}
