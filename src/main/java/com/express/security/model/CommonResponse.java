package com.express.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommonResponse<T>{
    private T data;
    private String errors;

    private String statusCode;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getData() {
        return data;
    }
    @JsonProperty("errors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getErrors() {
        return errors;
    }

    @JsonProperty("statusCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getStatusCode() {
        return statusCode;
    }
}
