package com.resitplatform.api.operation;

import com.resitplatform.rest.support.LocalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "resit", path = "${api.version}", configuration = LocalFeignConfig.class)
public interface ResitClient extends ResitOperations {
}
