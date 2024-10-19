package com.savolodya.predictiveportfolio.models.user;

import com.savolodya.predictiveportfolio.models.team.TeamGrantedAuthority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "UUID has to be set")
    private UUID uuid;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Email has to be set")
    @Email(message = "Email is not valid")
    @Size(min = 3, max = 255, message = "Email has to be between 3 and 255 characters")
    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private transient Set<UserTeam> teams;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status has to be set")
    private UserStatus status;

    public User(String email, String password, UserStatus status) {
        this.uuid = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return teams.stream()
                .map(team -> new TeamGrantedAuthority(team.getTeam(), team.getRoles()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(UserStatus.ACTIVE);
    }
}
