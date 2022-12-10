package com.resitplatform.api.query;

import com.resitplatform.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetResit implements Query<GetResitResult> {
    private String currentUsername;
    private String slug;
}
