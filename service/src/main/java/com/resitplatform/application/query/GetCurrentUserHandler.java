 
package com.resitplatform.application.query;

import com.resitplatform.api.query.GetCurrentUser;
import com.resitplatform.api.query.GetCurrentUserResult;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.service.JwtService;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.application.UserAssembler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class GetCurrentUserHandler implements QueryHandler<GetCurrentUserResult, GetCurrentUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    @Override
    public GetCurrentUserResult handle(GetCurrentUser query) {
        User user = userRepository.findByUsername(query.getUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", query.getUsername()));

        return new GetCurrentUserResult(UserAssembler.assemble(user, jwtService));
    }

}
