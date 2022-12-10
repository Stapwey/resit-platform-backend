package com.resitplatform.infrastructure.web;

import com.resitplatform.api.command.*;
import com.resitplatform.api.operation.ResitOperations;
import com.resitplatform.api.query.GetResit;
import com.resitplatform.api.query.GetResitResult;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.api.query.GetResitsResult;
import com.resitplatform.application.service.AuthenticationService;
import com.resitplatform.bus.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class ResitController implements ResitOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetResitsResult findByFilters(Integer limit,
                                         Integer offset,
                                         GetResits command) {
        return bus.executeQuery(command.toBuilder()
                .currentUsername(auth.currentUsername())
                .limit(limit)
                .offset(offset)
                .build());
    }

    @Override
    public ScheduleResitResult schedule(@Valid ScheduleResit command) {
        return bus.executeCommand(command.toBuilder().currentUsername(auth.currentUsername()).build());
    }

    @Override
    public GetResitResult findBySlug(String slug) {
        return bus.executeQuery(new GetResit(auth.currentUsername(), slug));
    }

    @Override
    public UpdateResitResult updateBySlug(String slug, @Valid UpdateResit command) {
        return bus.executeCommand(command.toBuilder().slug(slug).currentUsername(auth.currentUsername()).build());
    }

    @Override
    public void cancelBySlug(String slug) {
        bus.executeCommand(new CancelResit(auth.currentUsername(), slug));
    }

    @Override
    public SignOnResitResult signOn(String slug, @Valid SignOnResit command) {
        return bus.executeCommand(command.toBuilder().slug(slug).currentUsername(auth.currentUsername()).build());
    }

    @Override
    public SignOffResitResult signOff(String slug, SignOffResit command) {
        return bus.executeCommand(command.toBuilder().slug(slug).currentUsername(auth.currentUsername()).build());
    }

}
