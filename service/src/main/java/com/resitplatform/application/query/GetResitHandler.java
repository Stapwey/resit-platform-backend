package com.resitplatform.application.query;

import com.resitplatform.api.query.GetResit;
import com.resitplatform.api.query.GetResitResult;
import com.resitplatform.application.ResitAssembler;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.repository.ResitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetResitHandler implements QueryHandler<GetResitResult, GetResit> {

    private final ResitRepository resitRepository;

    @Override
    public GetResitResult handle(GetResit query) {
        Resit resitBySlug = resitRepository.findBySlug(query.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No resit with slug \"[slug=%s]\" found", query.getSlug()));

        return new GetResitResult(ResitAssembler.assemble(resitBySlug));
    }
}
