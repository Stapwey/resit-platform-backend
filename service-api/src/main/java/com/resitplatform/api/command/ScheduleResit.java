package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@JsonRootName("resit")
public class ScheduleResit implements Command<ScheduleResitResult> {
    @NotBlank
    private String name;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    private String description;
    private String image;
    private String currentUsername;
}
