 
package com.resitplatform.infrastructure.web;

import com.resitplatform.api.operation.ProfileOperations;
import com.resitplatform.api.query.*;
import com.resitplatform.bus.Bus;
import com.resitplatform.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class ProfileController implements ProfileOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetProfileResult findByUsername(String username) {
        return bus.executeQuery(new GetProfile(auth.currentUsername(), username));
    }

    @Override
    public GetProfilesResult list() {
        return bus.executeQuery(new GetProfiles());
    }

}
