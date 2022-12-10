
package com.resitplatform.application.service;

import com.resitplatform.domain.model.User;

public interface JwtService {

    String getSubject(String token);

    String getToken(User user);

}
