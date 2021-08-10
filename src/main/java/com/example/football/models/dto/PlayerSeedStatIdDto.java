package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedStatIdDto {
    @XmlElement(name = "id")
    private Long id;

    public PlayerSeedStatIdDto() {
    }
    @Positive
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
