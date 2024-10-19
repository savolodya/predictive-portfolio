package com.savolodya.predictiveportfolio.models.user;

import com.savolodya.predictiveportfolio.models.team.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users_teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "team_id" })
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User has to be set")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @NotNull(message = "Team has to be set")
    private Team team;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_tams_roles",
            joinColumns = @JoinColumn(name = "user_team_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles;

    public UserTeam(User user, Team team, Set<UserRole> roles) {
        this.user = user;
        this.team = team;
        this.roles = roles;
    }
}
