package com.resitplatform.infrastructure.web;

import com.resitplatform.api.operation.UserOperations;
import com.resitplatform.api.query.GetCurrentUser;
import com.resitplatform.api.query.GetCurrentUserResult;
import com.resitplatform.bus.Bus;
import com.resitplatform.api.command.LoginUser;
import com.resitplatform.api.command.LoginUserResult;
import com.resitplatform.api.command.RegisterUser;
import com.resitplatform.api.command.RegisterUserResult;
import com.resitplatform.api.command.UpdateUser;
import com.resitplatform.api.command.UpdateUserResult;
import com.resitplatform.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class UserController implements UserOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public LoginUserResult login(@Valid LoginUser cmd) {
        return bus.executeCommand(cmd);
    }

    @Override
    public RegisterUserResult register(@Valid RegisterUser cmd) {
        return bus.executeCommand(cmd);
    }

    @Override
    public GetCurrentUserResult current() {
        return bus.executeQuery(new GetCurrentUser(auth.currentUsername()));
    }

    @Override
    public UpdateUserResult update(@Valid UpdateUser cmd) {
        return bus.executeCommand(cmd.withCurrentUsername(auth.currentUsername()));
    }

}
