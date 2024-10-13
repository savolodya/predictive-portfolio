package com.savolodya.predictiveportfolio.models.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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

    public UserRole(Role name) {
        this.name = name;
    }
}
