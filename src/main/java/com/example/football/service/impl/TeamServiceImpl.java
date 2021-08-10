package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final ValidationUtil validationUtil;

    private final Gson gson;

    private final ModelMapper modelMapper;

    private static final String TEAM_FILE_PATH = "src/main/resources/files/json/teams.json";

    private final TownService townService;


    public TeamServiceImpl(TeamRepository teamRepository, ValidationUtil validationUtil, Gson gson, ModelMapper modelMapper, TownService townService) {
        this.teamRepository = teamRepository;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAM_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        TeamSeedDto[] teamSeedDtos = gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        Arrays.stream(teamSeedDtos).filter(teamSeedDto -> {
            boolean isValid = validationUtil.isValid(teamSeedDto) && !teamNameExist(teamSeedDto.getName());
            if (isValid){
                sb.append(String.format("Successfully imported Team %s - %d", teamSeedDto.getName(),
                        teamSeedDto.getFanBase())).append(System.lineSeparator());
            } else {
                sb.append("Invalid Team").append(System.lineSeparator());
            }
            return isValid;
        }).map(teamSeedDto -> {
            Team team = modelMapper.map(teamSeedDto, Team.class);
            team.setTown(townService.findByName(teamSeedDto.getTownName()));
            return team;
        }).forEach(teamRepository::save);

        return sb.toString();
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findByName(name);
    }

    private boolean teamNameExist(String name) {
        return teamRepository.existsByName(name);
    }
}
