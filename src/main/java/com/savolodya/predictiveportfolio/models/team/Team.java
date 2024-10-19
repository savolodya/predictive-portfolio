package com.savolodya.predictiveportfolio.models.team;

import com.savolodya.predictiveportfolio.models.user.UserTeam;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "teams")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "UUID has to be set")
    private UUID uuid;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Name has to be set")
    @Size(min = 3, max = 255, message = "Name has to be between 3 and 255 characters")
    private String name;

    @OneToMany(mappedBy = "team")
    private Set<UserTeam> users;

    public Team(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
    }
}
