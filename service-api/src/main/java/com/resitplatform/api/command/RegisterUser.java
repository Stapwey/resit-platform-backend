 
package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonRootName("user")
public class RegisterUser implements Command<RegisterUserResult> {

    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private Boolean isTeacher;

}
