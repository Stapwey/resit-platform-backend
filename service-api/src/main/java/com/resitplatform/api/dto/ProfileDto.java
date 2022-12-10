 
package com.resitplatform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileDto {

    private String username;
    private String bio;
    private String image;

}
