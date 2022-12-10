 
package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@JsonRootName("user")
public class LoginUser implements Command<LoginUserResult> {

    @Email
    private String email;
    @NotBlank
    private String password;

}
