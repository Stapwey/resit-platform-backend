 
package com.resitplatform.bus;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
class QueryHandlerProvider<H extends QueryHandler<?, ?>> {

    private final ApplicationContext applicationContext;
    private final Class<H> type;

    H get() {
        return applicationContext.getBean(type);
    }

}
