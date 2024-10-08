package com.savolodya.predictiveportfolio.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role name;

    @Override
    public String getAuthority() {
        return name.getKey();
    }

}
