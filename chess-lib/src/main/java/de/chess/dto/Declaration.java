package de.chess.dto;

import java.io.Serializable;

public class Declaration implements Serializable {

    private final GameDTO gameDTO;
    private String uuid;
    private String name;
    private boolean isMoveDeclaration;
    private String move;

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

    public void moveDeclaration(String item) {
        this.isMoveDeclaration = true;
        this.move = item;
    }

    public String getMove() {
        return move;
    }

    public boolean isMoveDeclaration() {
        return isMoveDeclaration;
    }

    public void setMoveDeclaration(boolean moveDeclaration) {
        isMoveDeclaration = moveDeclaration;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
