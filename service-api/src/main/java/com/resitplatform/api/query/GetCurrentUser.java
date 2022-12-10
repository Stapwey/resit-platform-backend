 
package com.resitplatform.api.query;

import com.resitplatform.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCurrentUser implements Query<GetCurrentUserResult> {

    private String username;

}
