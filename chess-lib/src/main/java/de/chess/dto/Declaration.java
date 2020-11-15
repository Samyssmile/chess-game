package de.chess.dto;

import java.io.Serializable;

public class Declaration implements Serializable {

    private final GameDTO gameDTO;
    private String uuid;
    private String name;

    public Declaration(String uuid, GameDTO gameDTO) {
        this.uuid = uuid;
        this.gameDTO= gameDTO;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Declaration{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
