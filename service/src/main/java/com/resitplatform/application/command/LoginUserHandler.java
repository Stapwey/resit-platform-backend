 
package com.resitplatform.application.command;

import com.resitplatform.api.command.LoginUser;
import com.resitplatform.api.command.LoginUserResult;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.exception.UnauthorizedException;
import com.resitplatform.application.service.JwtService;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.application.UserAssembler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class LoginUserHandler implements CommandHandler<LoginUserResult, LoginUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public LoginUserResult handle(LoginUser command) {
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> BadRequestException.badRequest("user [email=%s] does not exist", command.getEmail()));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw UnauthorizedException.unauthorized("user [email=%s] password is incorrect", command.getEmail());
        }

        return new LoginUserResult(UserAssembler.assemble(user, jwtService));
    }

}
