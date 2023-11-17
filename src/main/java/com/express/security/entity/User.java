package com.express.security.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstName;
    private String lastName;

    @Column(length = 60)
    private String password;

    @Column(unique = true)
    private String email;

    private String role;
    @Column(columnDefinition = "boolean default false")
    private Boolean enable = false;

    @JsonProperty("enable")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getEnable() {
        return enable;
    }

}
