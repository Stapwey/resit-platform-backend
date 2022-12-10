
package com.resitplatform.rest.support;

import com.resitplatform.api.operation.ResitClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.util.SocketUtils;

@EnableFeignClients(basePackageClasses = ResitClient.class)
@Import(ServerPortCustomizer.class)
@ExtendWith(FeignBasedRestTest.Before.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FeignBasedRestTest {

    public static class Before implements BeforeAllCallback {

        @Override
        public void beforeAll(ExtensionContext context) {
            if (System.getProperty("server.port") == null) {
                int port = SocketUtils.findAvailableTcpPort();
                System.setProperty("server.port", String.valueOf(port));
            }
        }

    }

}
