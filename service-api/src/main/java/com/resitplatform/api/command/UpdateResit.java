package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@JsonRootName("resit")
public class UpdateResit implements Command<UpdateResitResult> {
    // this enpdpoint works both for PUT and UPDATE, but we still require the name even
    // for update
    @NotBlank
    private String name;
    private String image;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    private Boolean hasEnded;
    // these are the fields that are automatically set up on request
    private String slug;
    private String currentUsername;

}
