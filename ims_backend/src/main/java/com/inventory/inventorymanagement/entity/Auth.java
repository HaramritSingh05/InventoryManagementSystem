package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import jakarta.servlet.http.Cookie;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "auth")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "cookie", unique = true, length = 250)
    private String cookie;

    public Auth(String username, String cookie) {
        this.username = username;
        this.cookie = cookie;
    }

}

