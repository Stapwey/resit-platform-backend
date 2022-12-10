 
package com.resitplatform.api.operation;

import com.resitplatform.api.command.LoginUser;
import com.resitplatform.api.command.LoginUserResult;
import com.resitplatform.api.command.RegisterUser;
import com.resitplatform.api.command.RegisterUserResult;
import com.resitplatform.api.command.UpdateUser;
import com.resitplatform.api.command.UpdateUserResult;
import com.resitplatform.api.query.GetCurrentUserResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface UserOperations {

    @PostMapping("/users/login")
    LoginUserResult login(@Valid @RequestBody LoginUser command);

    @PostMapping("/users")
    RegisterUserResult register(@Valid @RequestBody RegisterUser command);

    @GetMapping("/user")
    GetCurrentUserResult current();

    @PutMapping("/user")
    UpdateUserResult update(@Valid @RequestBody UpdateUser command);

}
