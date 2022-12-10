package com.resitplatform.api.query;

import com.resitplatform.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
public class GetResits implements Query<GetResitsResult> {
    private String name;
    private String teacherName;
    private Integer limit;
    private Integer offset;
    private String currentUsername;
    private String[] participantNames;

}
