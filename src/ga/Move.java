package ga;

/**
 * @author Pontus Soderlund
 */
public class Move implements Comparable<Move> {

    //***********************
    // Variables
    //***********************
    private Double moveScore;
    private String moves;

    //***********************
    // Constructor(s)
    //***********************
    public Move(){
        
    }

    public Move(String moves, double moveScore) {
        this.moves = moves;
        this.moveScore = moveScore;
    }

    //***********************
    // Support Methods
    //***********************
    public void move(char move) {
        moves += String.valueOf(move);
    }

    public void calculateScore() {

    }

    @Override
    public String toString() {
        String moveSequence = "";
        for (int i = 0; i < moves.length(); i++) {
            moveSequence += moves.charAt(i);
            if (i < moves.length() - 1) {
                moveSequence += ", ";
            }
        }
        return "Score: " + moveScore + " \t Moves: " + moveSequence;
    }

    @Override
    public int compareTo(Move t) {
        double msThis = this.getMoveScore();
        double msOther = t.getMoveScore();
        if (msThis > msOther) {
            return 1;
        } else if (msThis < msOther) {
            return -1;
        } else {
            return 0;
        }
    }

    //***********************
    // Getter Methods
    //***********************
    public int getNumberOfMoves() {
        return moves.length();
    }

    public Double getMoveScore() {
        return moveScore;
    }

    public String getMoves() {
        return moves;
    }

    //***********************
    // Setter Methods
    //***********************
    public void setMoveScore(Double moveScore) {
        this.moveScore = moveScore;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }
}
