 
package com.resitplatform.application;

import com.resitplatform.api.dto.UserDto;
import com.resitplatform.application.service.JwtService;
import com.resitplatform.domain.model.User;

public class UserAssembler {

    public static UserDto assemble(User user, JwtService jwtService) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(jwtService.getToken(user))
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

}
