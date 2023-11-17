//package com.express.security.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.security.core.GrantedAuthority;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "m_roles")
//public class Role implements GrantedAuthority {
//    @Id
//    @GenericGenerator(strategy = "uuid2", name="system-uuid")
//    @GeneratedValue(generator = "system-uuid")
//    @Column(name = "role_id")
//    private String id;
//
//    private String authority;
//
//    @Override
//    public String getAuthority() {
//        return this.authority;
//    }
//}
