 
package com.resitplatform.api.operation;

import com.resitplatform.rest.support.LocalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user", path = "${api.version}", configuration = LocalFeignConfig.class)
public interface UserClient extends UserOperations {
}
