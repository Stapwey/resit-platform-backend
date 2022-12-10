package com.resitplatform.api.operation;

import com.resitplatform.api.command.*;
import com.resitplatform.api.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ResitOperations {
    @PostMapping("/resits/search{?limit,offset}")
    GetResitsResult findByFilters(@RequestParam(defaultValue = "20") Integer limit,
                                  @RequestParam(defaultValue = "0") Integer offset,
                                  @RequestBody GetResits command);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resits")
    ScheduleResitResult schedule(@Valid @RequestBody ScheduleResit command);

    @GetMapping("/resits/{slug}")
    GetResitResult findBySlug(@PathVariable("slug") String slug);

    @PutMapping("/resits/{slug}")
    UpdateResitResult updateBySlug(@PathVariable("slug") String slug, @Valid @RequestBody UpdateResit command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/resits/{slug}")
    void cancelBySlug(@PathVariable("slug") String slug);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resits/{slug}/sign-on")
    SignOnResitResult signOn(@PathVariable("slug") String slug, SignOnResit command); // we don't expect anything in request body

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resits/{slug}/sign-off")
    SignOffResitResult signOff(@PathVariable("slug") String slug, SignOffResit command);
}
