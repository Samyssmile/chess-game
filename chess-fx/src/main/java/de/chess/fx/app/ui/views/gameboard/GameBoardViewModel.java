package de.chess.fx.app.ui.views.gameboard;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;
import de.chess.dto.request.MoveRequest;
import de.chess.dto.request.Request;
import de.chess.fx.app.ChessFX;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.*;
import de.chess.model.ChessColor;
import de.chess.model.ChessField;
import de.chess.model.Piece;
import de.chess.model.PieceType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.UUID;
import java.util.concurrent.Flow;

public class GameBoardViewModel implements Flow.Subscriber<String>, IChannel {

  private static final String COLOR_BLACK = "#D18B47";
  private static final String COLOR_WHITE = "#FFCE9E";
  private static final double PREFERED_FIELD_SIZE = 90;
  private static final Background BLACK_COLOR =
      new Background(new BackgroundFill(Color.web(COLOR_BLACK), CornerRadii.EMPTY, Insets.EMPTY));
  private static final Background WHITE_COLOR =
      new Background(new BackgroundFill(Color.web(COLOR_WHITE), CornerRadii.EMPTY, Insets.EMPTY));
  private SimpleObjectProperty<FieldView[][]> boardMatrix =
      new SimpleObjectProperty<>(new FieldView[8][8]);

  private Flow.Subscription subscription;
  private ChessGame chessGame;

  public GameBoardViewModel() {}

  public GameBoardViewModel(ChessGame gameDTO) {
    initFields();
    initChessFigureMatrix(gameDTO, ChessColor.WHITE);
  }

  private void initFields() {
    for (int i = 0; i < boardMatrix.get().length; i++) {
      for (int y = 0; y < boardMatrix.get()[i].length; y++) {
        FieldView pane = new FieldView(i, 7 - y);
        Flow.Subscriber<String> gameBoardSubscriber = this;
        pane.registerOnPublisher(gameBoardSubscriber);
        boardMatrix.get()[i][y] = pane;
        pane.setBorder(
            new Border(
                new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT)));
        if ((i % 2) == 0) {
          if ((y % 2) != 0) {
            pane.setBackground(BLACK_COLOR);
          } else {
            pane.setBackground(WHITE_COLOR);
          }
        } else {
          if ((y % 2) == 0) {
            pane.setBackground(BLACK_COLOR);
          } else {
            pane.setBackground(WHITE_COLOR);
          }
        }
        pane.setPrefWidth(PREFERED_FIELD_SIZE);
        pane.setPrefHeight(PREFERED_FIELD_SIZE);
      }
    }
  }

  public void resetFigures() {}

  private void initChessFigureMatrix(ChessGame gameDTO, ChessColor color) {
    ChessField a = gameDTO.getGameBoard().getBoard()[1][1];
    UUID hostUUID = gameDTO.getHostPlayer().getUuid();

    UUID myUUID = ChessFX.PLAYERS_UUID;
    if (hostUUID.equals(myUUID)){
      ChessColor hostColor = gameDTO.getHostColor();
      putPiecesOnChessboard(hostColor, gameDTO );

    }

    if (gameDTO.getClientPlayer() != null && gameDTO.getClientPlayer().getUuid().equals(myUUID)){
      ChessColor clientColor = gameDTO.getClientColor();
      putPiecesOnChessboard(clientColor, gameDTO );
    }

  }

  private void putPiecesOnChessboard(ChessColor color, ChessGame gameDTO) {
    for (int i = 0; i < gameDTO.getGameBoard().getBoard().length; i++) {
      for (int y = 0; y < gameDTO.getGameBoard().getBoard()[i].length; y++) {

        ChessField chessField = gameDTO.getGameBoard().getBoard()[i][y];

        if (chessField.isPiecePresent()){
          if (color == ChessColor.WHITE){
            boardMatrix
                    .get()[7-y][7-i]
                    .getViewModel()
                    .figureProperty()
                    .set(putFigureOf(chessField.getPiece()));
          }else{
            boardMatrix
                    .get()[y][i]
                    .getViewModel()
                    .figureProperty()
                    .set(putFigureOf(chessField.getPiece()));
          }

        }
      }
    }
  }

  private ChessFigure putFigureOf(Piece piece) {
    ChessFigure resultFigure = null;
    switch (piece.getPieceType()){
      case KING -> resultFigure =  new King((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
      case QUUEN -> resultFigure =new Queen((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
      case BISHOP -> resultFigure =new Bishop((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
      case KNIGHT ->resultFigure = new Horse((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
      case  ROOK-> resultFigure =new Tower((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
      case PAWN -> resultFigure =new Pawn((piece.getColor() == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
    }
    return resultFigure;
  }

  private void initKings(ChessColor color) {
    boardMatrix
        .get()[4][0]
        .getViewModel()
        .figureProperty()
        .set(new King((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[4][7]
        .getViewModel()
        .figureProperty()
        .set(new King((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  private void initQueens(ChessColor color) {
    boardMatrix
        .get()[3][0]
        .getViewModel()
        .figureProperty()
        .set(new Queen((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[3][7]
        .getViewModel()
        .figureProperty()
        .set(new Queen((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  private void initBishops(ChessColor color) {
    boardMatrix
        .get()[2][0]
        .getViewModel()
        .figureProperty()
        .set(new Bishop((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[5][0]
        .getViewModel()
        .figureProperty()
        .set(new Bishop((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[2][7]
        .getViewModel()
        .figureProperty()
        .set(new Bishop((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[5][7]
        .getViewModel()
        .figureProperty()
        .set(new Bishop((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  private void initHorses(ChessColor color) {
    boardMatrix
        .get()[1][0]
        .getViewModel()
        .figureProperty()
        .set(new Horse((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[6][0]
        .getViewModel()
        .figureProperty()
        .set(new Horse((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));

    boardMatrix
        .get()[1][7]
        .getViewModel()
        .figureProperty()
        .set(new Horse((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[6][7]
        .getViewModel()
        .figureProperty()
        .set(new Horse((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  private void initTowers(ChessColor color) {
    boardMatrix
        .get()[0][0]
        .getViewModel()
        .figureProperty()
        .set(new Tower((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[7][0]
        .getViewModel()
        .figureProperty()
        .set(new Tower((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));

    boardMatrix
        .get()[0][7]
        .getViewModel()
        .figureProperty()
        .set(new Tower((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[7][7]
        .getViewModel()
        .figureProperty()
        .set(new Tower((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  private void initPawns(ChessColor color) {
    boardMatrix
        .get()[0][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[1][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[2][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[3][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[4][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[5][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[6][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));
    boardMatrix
        .get()[7][1]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK));

    boardMatrix
        .get()[0][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[1][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[2][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[3][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[4][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[5][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[6][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
    boardMatrix
        .get()[7][6]
        .getViewModel()
        .figureProperty()
        .set(new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE));
  }

  public FieldView[][] getBoardMatrix() {
    return boardMatrix.get();
  }

  public SimpleObjectProperty<FieldView[][]> boardMatrixProperty() {
    return boardMatrix;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(100);
  }

  @Override
  public void onNext(String move) {
    Request request =
        new MoveRequest(UUID.randomUUID(), ChessFX.PLAYERS_UUID, RequestType.MOVE, move);
    subscription.request(100);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("Flow API: Error");
  }

  @Override
  public void onComplete() {
    System.out.println("Flow API: complete");
  }

  public void openGame() {}

  @Override
  public void update(EventType eventType, EventData eventData) {

    switch (eventType) {
      case OPEN_NEW_GAME:
        ChessGame chessGame = (ChessGame) eventData.getData();
        this.chessGame = chessGame;
        break;
    }
  }
}
