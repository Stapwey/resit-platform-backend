package com.resitplatform.application.query;

import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.api.query.GetResitsResult;
import com.resitplatform.application.ResitAssembler;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.ResitRepository;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class GetResitsHandler implements QueryHandler<GetResitsResult, GetResits> {

    private final ResitRepository resitRepository;
    private final UserRepository userRepository;

    @Override
    public GetResitsResult handle(GetResits query) {
        if (query.getName() == null)
            query = query.toBuilder().name("").build();

        if (query.getTeacherName() == null)
            query = query.toBuilder().teacherName("").build();

        Set<User> participants = new HashSet<>();
        if (query.getParticipantNames() != null)
            for (String name : query.getParticipantNames()) {
                 participants.add(
                         userRepository.findByUsername(name)
                            .orElseThrow(
                                    () -> BadRequestException.badRequest(
                                        "user [name=%s] does not exist",
                                        name
                                    )
                            )
                 );
            }

        List<Resit> resitList = resitRepository
                .findByFilters(query.getName(), query.getTeacherName(), query.getLimit(), query.getOffset());

        if (!participants.isEmpty()) {
            List<Resit> resitByParticipantsList = resitRepository.findByParticipants(
                    participants,
                    query.getLimit(),
                    query.getOffset()
            );
//            resitList = Stream.concat(resitList.stream(), resitByParticipantsList.stream())
//                    .collect(Collectors.toList());
            resitList.retainAll(resitByParticipantsList);
        }

        List<ResitDto> results = new ArrayList<>();

        resitList.forEach(resit -> results.add(ResitAssembler.assemble(resit)));

        return new GetResitsResult(results, results.size());
    }
}
