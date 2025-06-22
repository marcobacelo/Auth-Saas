package com.jwt.server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    @Getter
    @RequiredArgsConstructor
    public enum Values {

        ADMIN(1L),
        BASIC(2L);

        final long roleId;
    }
}
