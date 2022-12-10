 
package com.resitplatform.api.operation;

import com.resitplatform.api.query.GetProfileResult;
import com.resitplatform.api.query.GetProfilesResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProfileOperations {

    @GetMapping("/profiles/{username}")
    GetProfileResult findByUsername(@PathVariable("username") String username);

    @GetMapping("/profiles/")
    GetProfilesResult list();

}
