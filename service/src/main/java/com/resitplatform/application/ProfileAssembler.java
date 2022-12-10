 
package com.resitplatform.application;

import com.resitplatform.api.dto.ProfileDto;
import com.resitplatform.domain.model.User;

public class ProfileAssembler {

    public static ProfileDto assemble(User user) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImage());
    }

}
