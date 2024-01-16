package com.express.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "OpenApi Documentation for Spring Security", description = "OpenApi Documentation", contact = @Contact(email = "jsdevprofes@gmail.com", url = "http://localhost:8080/swagger-ui/index.html#/"), termsOfService = "Terms of Service", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), version = "1.0"), servers = {
        @Server(description = "Development ENV", url = "http://localhost:8080/"),
        @Server(description = "Production ENV", url = "http://localhost:8080/"),
})

@SecurityScheme(name = "bearerAuthentication", type = SecuritySchemeType.HTTP, description = "JWT Token for Escape the Authentication", in = SecuritySchemeIn.HEADER, scheme = "bearer", bearerFormat = "JWT")
public class OpenApi {

}
