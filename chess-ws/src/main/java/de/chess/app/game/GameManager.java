package de.chess.app.game;

import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.Player;
import de.chess.game.IGameManager;
import de.chess.model.Move;
import de.chess.model.Piece;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class GameManager implements IGameManager {
    public static final Logger LOGGER = Logger.getGlobal();
    private static IGameManager instance = null;
    private final List<ChessGame> activeGameList = new ArrayList<>();
    private int gameLimit = 10;


    private GameManager() {
    }

    public static IGameManager instance() {
        if (Objects.isNull(instance)) {
            instance = new GameManager();
        }
        return instance;
    }

    @Override
    public ChessGame requestGame(ChessGame gameDTO) {
        ChessGame chessGame = gameDTO;
        if (gameLimitNotReached()) {
            chessGame.setUuid(generateUUID());
            chessGame.initGameBoard();
            activeGameList.add(chessGame);
            chessGame.setGameStatus(GameStatus.WATING);
            LOGGER.log(Level.INFO, "Requested Game Granted: GameList Size: {0}", activeGameList.size());
        } else {
            chessGame = null;
            LOGGER.warning("Limit for concurent running games reached");
        }
        return chessGame;
    }


    @Override
    public void killGame(UUID uuid) {
        removeGame(activeGameList, uuid);
    }

    @Override
    public boolean isGameExists(UUID uuid) {
        return IntStream.range(0, activeGameList.size()).anyMatch(i -> Objects.equals(uuid, activeGameList.get(i).getUuid()));
    }

    @Override
    public int numberOfRunningGames() {
        return activeGameList.size();
    }

    @Override
    public void reset() {
        activeGameList.clear();
    }

    @Override
    public int getGameLimit() {
        return gameLimit;
    }

    @Override
    public void setGameLimit(int gameLimit) {
        this.gameLimit = gameLimit;
    }


    public Optional<ChessGame> findGameByUUID(UUID gameUUID) {
        Optional<ChessGame> chessGameOptional = getActiveGameList().stream().filter(chessGame -> chessGame.getUuid().equals(gameUUID)).findFirst();
        return chessGameOptional;
    }

    @Override
    public Optional<ChessGame> requestToJoinGame(UUID gameUUID, Player player) {
        Optional<ChessGame> response = Optional.empty();
        Optional<ChessGame> gameOfInteresst = getGameByUUIID(gameUUID);
        if (gameOfInteresst.isPresent()){
            ChessGame chessGame = gameOfInteresst.get();
            if (chessGame.getGameStatus() == GameStatus.WATING){
                chessGame.setClientPlayer(player);
                chessGame.setGameStatus(GameStatus.READY_TO_START);
                response = Optional.of(chessGame);
            }else {
                LOGGER.info("Game already running");
            }
        }else{
            LOGGER.info("Requested Game not found.");
        }

        return response;

    }

    @Override
    public Optional<ChessGame> getGameByUUIID(UUID gameUUID) {
        return activeGameList.stream().filter(e -> e.getUuid().equals(gameUUID)).findFirst();
    }

    public boolean move(UUID uuid, String move) {
        try {
            // Find the game
            Optional<ChessGame> gameOptional = getGameByUUIID(uuid);
            if (!gameOptional.isPresent()) {
                LOGGER.warning("Game not found for UUID: " + uuid);
                return false;
            }

            ChessGame game = gameOptional.get();
            
            // Check if game is running
            if (game.getGameStatus() != GameStatus.RUNNING) {
                LOGGER.warning("Game is not in RUNNING state: " + game.getGameStatus());
                return false;
            }

            // Parse move string (expecting format like "e2-e4" or "e2:e4")
            String[] moveParts = move.split("[-:]");
            if (moveParts.length != 2) {
                LOGGER.warning("Invalid move format: " + move + ". Expected format: 'from-to' or 'from:to'");
                return false;
            }

            String fromField = moveParts[0].trim();
            String toField = moveParts[1].trim();
            Move moveObj = new Move(fromField, toField);

            // Validate move using ChessMoveValidator
            ChessMoveValidator validator = new ChessMoveValidator();
            if (!validator.isMoveValid(game.getGameBoard(), moveObj)) {
                LOGGER.warning("Invalid move: " + move);
                return false;
            }

            // Get the piece to move and validate it belongs to the current player
            Piece movingPiece = game.getGameBoard().getPiece(fromField);
            if (movingPiece == null) {
                LOGGER.warning("No piece at source field: " + fromField);
                return false;
            }

            // Check if the piece color matches the current turn
            if (movingPiece.getColor() != game.getCurrentTurn()) {
                LOGGER.warning("Wrong player turn. Expected: " + game.getCurrentTurn() + ", but piece is: " + movingPiece.getColor());
                return false;
            }

            // Execute the move
            Piece capturedPiece = game.getGameBoard().removePiece(toField); // Remove captured piece if any
            game.getGameBoard().removePiece(fromField); // Remove piece from source
            game.getGameBoard().putPiece(toField, movingPiece); // Place piece at destination

            // Switch turn
            game.switchTurn();

            // Update game state (check for check/checkmate/stalemate)
            game.updateGameState();

            LOGGER.info("Move executed successfully: " + move + " in game " + uuid);
            return true;

        } catch (Exception e) {
            LOGGER.severe("Error executing move: " + move + " in game " + uuid + ". Error: " + e.getMessage());
            return false;
        }
    }


    private boolean gameLimitNotReached() {
        return activeGameList.size() < gameLimit;
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    @Override
    public List<ChessGame> getActiveGameList() {
        return new ArrayList<>(activeGameList);
    }

    private void removeGame(List<ChessGame> list, UUID element) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(element, list.get(i).getUuid())) {
                list.remove(i);
            }
        }
    }


}
