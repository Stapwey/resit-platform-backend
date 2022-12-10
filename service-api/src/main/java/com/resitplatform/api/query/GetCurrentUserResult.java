 
package com.resitplatform.api.query;

import com.resitplatform.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCurrentUserResult {

    private UserDto user;

}
