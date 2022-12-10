package com.resitplatform.application.command;

import com.resitplatform.api.command.SignOnResit;
import com.resitplatform.api.command.SignOnResitResult;
import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.application.ResitAssembler;
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

import java.util.Set;

@RequiredArgsConstructor
@Service
public class SignOnResitHandler implements CommandHandler<SignOnResitResult, SignOnResit> {

    private final ResitRepository resitRepository;
    private final UserRepository userRepository;

    @Override
    public SignOnResitResult handle(SignOnResit command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (currentUser.getIs_teacher()) {
            throw ForbiddenException.forbidden("teachers are not allowed to sign on to a resit");
        }

        Resit resit = resitRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("resit [slug=%s] does not exist", command.getSlug()));

        if (resit.getHasEnded()) {
            throw BadRequestException.badRequest("the resit has ended");
        }

        Set<User> currentParticipants = resit.getParticipants();

        if (currentParticipants.contains(currentUser)) {
            throw BadRequestException.badRequest("you are already signed on to resit [slug=%%s]");
        }

        currentParticipants.add(currentUser);
        resitRepository.save(resit);

        return new SignOnResitResult(ResitAssembler.assemble(resit));
    }
}
