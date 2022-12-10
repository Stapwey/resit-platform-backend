package com.resitplatform.application.query;

import com.resitplatform.api.dto.ProfileDto;
import com.resitplatform.api.query.GetProfiles;
import com.resitplatform.api.query.GetProfilesResult;
import com.resitplatform.application.ProfileAssembler;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetProfilesHandler implements QueryHandler<GetProfilesResult, GetProfiles> {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public GetProfilesResult handle(GetProfiles command) {
        Iterable<User> users = userRepository.findAll();
        List<ProfileDto> results = new ArrayList<>();

        users.forEach(user -> results.add(ProfileAssembler.assemble(user)));
        return new GetProfilesResult(results, results.size());
    }
}
