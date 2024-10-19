package com.savolodya.predictiveportfolio.models.team;

import com.savolodya.predictiveportfolio.models.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TeamGrantedAuthority implements GrantedAuthority {

    private transient Team team;
    private Set<UserRole> roles;

    @Override
    public String getAuthority() {
        return roles.stream()
                .map(UserRole::getAuthority)
                .collect(Collectors.joining(",", "TEAM_" + team.getUuid() + ":", ""));
    }
}
