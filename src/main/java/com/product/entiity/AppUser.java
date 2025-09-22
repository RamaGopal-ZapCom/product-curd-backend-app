package com.product.entiity;

import com.product.constants.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Entity
@Table(name = "app_user_schema")
@Data
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime lastPasswordUpdatedAt;

    @PrePersist
    public void onCreate() {
        this.lastPasswordUpdatedAt = LocalDateTime.now();
    }

}
