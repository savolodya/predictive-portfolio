package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.team.Team;
import com.savolodya.predictiveportfolio.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    public Team findOrCreateTeam(String name) {
        return teamRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Team [{}] not found, creating new", name);
                    return save(new Team(name));
                });
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

}
