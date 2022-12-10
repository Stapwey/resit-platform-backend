package com.resitplatform.api.query;

import com.resitplatform.api.dto.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GetProfilesResult {
    private List<ProfileDto> profiles;
    private Integer profilesCount;
}
