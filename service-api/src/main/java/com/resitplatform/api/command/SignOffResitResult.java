package com.resitplatform.api.command;

import com.resitplatform.api.dto.ResitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignOffResitResult {
    private ResitDto resit;
}
