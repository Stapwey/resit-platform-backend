package com.resitplatform.application.command;

import com.resitplatform.api.command.CancelResit;
import com.resitplatform.api.command.CancelResitResult;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.exception.ForbiddenException;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.ResitRepository;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelResitHandler implements CommandHandler<CancelResitResult, CancelResit> {

    private final ResitRepository resitRepository;
    private final UserRepository userRepository;

    @Override
    public CancelResitResult handle(CancelResit command) {
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

        resitRepository.cancel(resitBySlug);
        return new CancelResitResult();
    }
}
