package com.resitplatform.api.query;

import com.resitplatform.api.dto.ResitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GetResitsResult {

    private List<ResitDto> resits;
    private Integer resitsCount;

}
