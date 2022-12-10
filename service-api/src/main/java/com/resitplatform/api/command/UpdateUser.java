 
package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonRootName("user")
public class UpdateUser implements Command<UpdateUserResult> {

    @With
    private String currentUsername;
    @Email
    private String email;
    private String username;
    private String password;
    private String image;
    private String bio;

}
