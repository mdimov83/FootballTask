package com.example.football.models.dto;

import com.example.football.models.entity.enums.Position;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {
    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement(name = "position")
    private Position position;
    @XmlElement(name = "town")
    private PlayerSeedTownNameDto town;
    @XmlElement(name = "team")
    private PlayerSeedTeamNameDto team;
    @XmlElement(name = "stat")
    private PlayerSeedStatIdDto stat;

    public PlayerSeedDto() {
    }
    @Size(min = 3)
    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Size(min = 3)
    @NotBlank
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @NotNull
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    @NotNull
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    @NotNull
    public PlayerSeedTownNameDto getTown() {
        return town;
    }

    public void setTown(PlayerSeedTownNameDto town) {
        this.town = town;
    }
    @NotNull
    public PlayerSeedTeamNameDto getTeam() {
        return team;
    }

    public void setTeam(PlayerSeedTeamNameDto team) {
        this.team = team;
    }
    @NotNull
    public PlayerSeedStatIdDto getStat() {
        return stat;
    }

    public void setStat(PlayerSeedStatIdDto stat) {
        this.stat = stat;
    }
}
