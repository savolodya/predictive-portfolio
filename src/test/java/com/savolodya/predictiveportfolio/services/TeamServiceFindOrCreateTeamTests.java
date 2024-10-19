package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.team.Team;
import com.savolodya.predictiveportfolio.repositories.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class TeamServiceFindOrCreateTeamTests {

    @InjectMocks
    private TeamService teamService;
    @Mock
    private TeamRepository teamRepository;
    @Captor
    private ArgumentCaptor<Team> teamCaptor;

    @Test
    void should_FindTeam_When_TeamExists() {
        // given
        Team foundTeam = new Team("test");

        given(teamRepository.findByName("test"))
                .willReturn(Optional.of(foundTeam));

        // when
        teamService.findOrCreateTeam("test");

        // then
        then(teamRepository).should(never())
                .save(foundTeam);
    }

    @Test
    void should_CreateTeam_When_TeamDoesNotExists() {
        // given
        Team expectedTeam = new Team("test");

        given(teamRepository.findByName("test"))
                .willReturn(Optional.empty());

        // when
        teamService.findOrCreateTeam("test");

        // then
        then(teamRepository).should(times(1))
                .save(teamCaptor.capture());
        Team capturedTeam = teamCaptor.getValue();

        assertAll(
                () -> assertEquals(expectedTeam.getName(), capturedTeam.getName())
        );
    }

}