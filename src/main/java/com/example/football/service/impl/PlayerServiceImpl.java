package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final ValidationUtil validationUtil;

    private final XmlParser xmlParser;

    private final ModelMapper modelMapper;

    private static final String PLAYER_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final TownService townService;

    private final StatService statService;

    private final TeamService teamService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ValidationUtil validationUtil, XmlParser xmlParser, ModelMapper modelMapper, TownService townService, StatService statService, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.statService = statService;
        this.teamService = teamService;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYER_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedRootDto playerSeedRootDto = xmlParser.fromFile(PLAYER_FILE_PATH, PlayerSeedRootDto.class);

        playerSeedRootDto.getPlayers().stream().filter(playerSeedDto -> {
            boolean isValid = validationUtil.isValid(playerSeedDto) && !playerEmailExists(playerSeedDto.getEmail());
            if (isValid){
                sb.append(String.format("Successfully imported Player %s %s - %s", playerSeedDto.getFirstName(),
                        playerSeedDto.getLastName(), playerSeedDto.getPosition())).append(System.lineSeparator());
            } else {
                sb.append("Invalid Player").append(System.lineSeparator());
            }
            return isValid;
        }).map(playerSeedDto -> {
            Player player = modelMapper.map(playerSeedDto, Player.class);
            player.setTown(townService.findByName(playerSeedDto.getTown().getName()));
            player.setStat(statService.findStatById(playerSeedDto.getStat().getId()));
            player.setTeam(teamService.findByName(playerSeedDto.getTeam().getName()));
            return player;
        }).forEach(playerRepository::save);
        return sb.toString();
    }

    private boolean playerEmailExists(String email) {
        return playerRepository.existsByEmail(email);
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();
        List<Player> players = playerRepository.findBestPlayersOrderByShootingPassingEnduranceAndLastName(LocalDate.of(1995, 1, 1), LocalDate.of(2003, 1, 1));
        players.forEach(player -> sb.append(String.format("Player - %s %s\n" +
                "\tPosition - %s\n" +
                "\tTeam - %s\n" +
                "\tStadium - %s", player.getFirstName(), player.getLastName(),
                player.getPosition(), player.getTeam().getName(), player.getTeam().getStadiumName()))
        .append(System.lineSeparator()));
        return sb.toString();
    }
}
