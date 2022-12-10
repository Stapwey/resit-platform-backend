 
package com.resitplatform.application.query;

import com.resitplatform.api.query.GetProfile;
import com.resitplatform.api.query.GetProfileResult;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.application.ProfileAssembler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetProfileHandler implements QueryHandler<GetProfileResult, GetProfile> {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public GetProfileResult handle(GetProfile query) {

        User user = userRepository.findByUsername(query.getUsername())
                .orElseThrow(() -> NotFoundException.notFound("user [name=%s] does not exist", query.getUsername()));

        return new GetProfileResult(ProfileAssembler.assemble(user));
    }

}
