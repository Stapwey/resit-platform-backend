 
package com.resitplatform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserDto {

    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

}
