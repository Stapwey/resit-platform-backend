package com.resitplatform.api.command;

import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CancelResit implements Command<CancelResitResult> {
    private String currentUsername;
    private String slug;
}
