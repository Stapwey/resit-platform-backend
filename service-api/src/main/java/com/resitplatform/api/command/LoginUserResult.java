 
package com.resitplatform.api.command;

import com.resitplatform.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginUserResult {

    private UserDto user;

}
