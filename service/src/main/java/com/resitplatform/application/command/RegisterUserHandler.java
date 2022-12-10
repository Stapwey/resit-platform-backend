 
package com.resitplatform.application.command;

import com.resitplatform.api.command.RegisterUser;
import com.resitplatform.api.command.RegisterUserResult;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.service.JwtService;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.application.UserAssembler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RegisterUserHandler implements CommandHandler<RegisterUserResult, RegisterUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterUserResult handle(RegisterUser command) {
        Optional<User> userByEmailOptional = userRepository.findByEmail(command.getEmail());
        if (userByEmailOptional.isPresent()) {
            throw BadRequestException.badRequest("user [email=%s] already exists", command.getEmail());
        }
        Optional<User> userByUsernameOptional = userRepository.findByUsername(command.getUsername());
        if (userByUsernameOptional.isPresent()) {
            throw BadRequestException.badRequest("user [name=%s] already exists", command.getUsername());
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(command.getUsername())
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .is_teacher(command.getIsTeacher())
                .build();
        userRepository.save(user);

        return new RegisterUserResult(UserAssembler.assemble(user, jwtService));
    }

}
