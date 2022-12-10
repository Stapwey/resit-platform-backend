 
package com.resitplatform.api.query;

import com.resitplatform.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProfile implements Query<GetProfileResult> {

    private String currentUsername;
    private String username;

}
