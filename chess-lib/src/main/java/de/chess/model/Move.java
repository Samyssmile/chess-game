package de.chess.model;

public class Move {

    private String fromField;
    private String toField;
    private PieceType promotionPiece; // For pawn promotion

    public Move(String fromField, String toField) {
        this.fromField = fromField;
        this.toField = toField;
        this.promotionPiece = null;
    }
    
    public Move(String fromField, String toField, PieceType promotionPiece) {
        this.fromField = fromField;
        this.toField = toField;
        this.promotionPiece = promotionPiece;
    }

    public String getFromField() {
        return fromField;
    }

    public String getToField() {
        return toField;
    }
    
    public PieceType getPromotionPiece() {
        return promotionPiece;
    }
    
    public void setPromotionPiece(PieceType promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
    
    public boolean isPromotion() {
        return promotionPiece != null;
    }
}
