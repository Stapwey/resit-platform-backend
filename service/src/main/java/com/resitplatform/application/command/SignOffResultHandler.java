package com.resitplatform.application.command;

import com.resitplatform.api.command.SignOffResit;
import com.resitplatform.api.command.SignOffResitResult;
import com.resitplatform.api.command.SignOnResit;
import com.resitplatform.api.command.SignOnResitResult;
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
public class SignOffResultHandler implements CommandHandler<SignOffResitResult, SignOffResit> {

    private final ResitRepository resitRepository;
    private final UserRepository userRepository;

    @Override
    public SignOffResitResult handle(SignOffResit command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        Resit resit = resitRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("resit [slug=%s] does not exist", command.getSlug()));

        Set<User> currentParticipants = resit.getParticipants();

        if (!currentParticipants.contains(currentUser)) {
            throw BadRequestException.badRequest("you are not signed on to resit [slug=%%s]");
        }

        currentParticipants.remove(currentUser);
        resitRepository.save(resit);

        return new SignOffResitResult(ResitAssembler.assemble(resit));
    }
}
