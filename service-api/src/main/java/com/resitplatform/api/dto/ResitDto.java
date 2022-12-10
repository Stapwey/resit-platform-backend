package com.resitplatform.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResitDto {
    private String slug;
    private String name;
    private String teacherName;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Date startDate;
    private Boolean hasEnded;
    private String image;
    private String description;
    private ProfileDto[] participants;

}
