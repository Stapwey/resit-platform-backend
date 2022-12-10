package com.resitplatform.api.command;

import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignOnResit implements Command<SignOnResitResult> {
    private String slug;
    private String currentUsername;
}
