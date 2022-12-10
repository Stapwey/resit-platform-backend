 
package com.resitplatform.bus;

public interface QueryHandler<R, Q extends Query<R>> {

    R handle(Q query);

}
