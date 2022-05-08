package dz.bdjaghout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Bilal Djaghout on 5/8/2022 12:17 PM
 */

@SpringBootApplication
@EnableEurekaClient
public class APIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIGatewayApplication.class, args);
    }
}
