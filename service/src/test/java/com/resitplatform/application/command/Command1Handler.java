
package com.resitplatform.application.command;

import com.resitplatform.bus.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class Command1Handler implements CommandHandler<String, Command1> {

    @Override
    public String handle(Command1 command) {
        return null;
    }
}
