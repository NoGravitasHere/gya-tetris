package Tetris;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pontus Soderlund
 */
public class Board implements Cloneable {

    //***********************
    // Global Variables
    //***********************
    //The width and height of the board
    public static final int NOROWS = 20, NOCOLLUMNS = 10;
    private int[][] board;

    //***********************
    // Constructor(s)
    //***********************
    public Board() {
        int[][] b = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        this.board = b;
    }

    public Board(int[][] board) {
        this.board = board;
    }

    //***********************
    // Can Methods
    //***********************
    /**
     * Checks if a Tile with the specified Id can be generated
     *
     * @param pieceId the id of the tile to be generated
     * @return whether the tile can be created
     */
    private boolean canGenerateNewPiece(int pieceId) {
        if (pieceId == 1) {         //I-Piece
            if (board[1][3] != 0 || board[1][4] != 0 || board[1][5] != 0 || board[1][6] != 0) {
                return false;
            }
        } else if (pieceId == 2) {  //J-Piece
            if (board[0][5] != 0 || board[1][5] != 0 || board[2][5] != 0 || board[2][4] != 0) {
                return false;
            }
        } else if (pieceId == 3) {  //L-Piece
            if (board[0][4] != 0 || board[1][4] != 0 || board[2][4] != 0 || board[2][5] != 0) {
                return false;
            }
        } else if (pieceId == 4) {  //O-Piece
            if (board[0][4] != 0 || board[0][5] != 0 || board[1][4] != 0 || board[1][5] != 0) {
                return false;
            }
        } else if (pieceId == 5) {  //S-Piece
            if (board[0][4] != 0 || board[1][4] != 0 || board[1][5] != 0 || board[2][4] != 0) {
                return false;
            }
        } else if (pieceId == 6) {  //Z-Piece
            if (board[0][5] != 0 || board[1][5] != 0 || board[1][4] != 0 || board[2][4] != 0) {
                return false;
            }
        } else if (pieceId == 7) {  //T-Piece
            if (board[0][4] != 0 || board[1][3] != 0 || board[1][4] != 0 || board[1][5] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the tile with the coordinates newX, newY is occupied.
     *
     * @param newX the x-coordinate to move to
     * @param newY the y-coordinate to mote to
     * @return false if it is occupied, else true.
     */
    private boolean canMoveTo(int newX, int newY) {
        return getTile(newX, newY) <= 0;
    }

    /**
     * Checks if it is possible to move the current piece in right, left or down.
     *
     * @param direction the direction in which to move.
     * @return false if it is not possible. True if it is.
     */
    public boolean canMove(String direction) {
        switch (direction) {
            case "right":
                for (int i = 0; i < NOCOLLUMNS; i++) {
                    for (int j = 0; j < NOROWS; j++) {
                        if (getTile(i, j) < 0) {
                            if (i < NOCOLLUMNS - 1) {
                                if (getTile(i + 1, j) > 0) {
                                    return false;
                                }
                            } else if (i == 9) {
                                return false;
                            }
                        }
                    }
                }
                break;
            case "left":
                for (int i = 0; i < NOCOLLUMNS; i++) {
                    for (int j = 0; j < NOROWS; j++) {
                        if (getTile(i, j) < 0) {
                            if (i > 0) {
                                if (getTile(i - 1, j) > 0) {
                                    return false;
                                }
                            } else if (i == 0) {
                                return false;
                            }
                        }
                    }
                }
                break;
            case "down":
                for (int i = NOROWS - 1; i >= 0; i--) {
                    for (int j = 0; j < NOCOLLUMNS; j++) {
                        if (getTile(j, i) < 0) {
                            if (i != NOROWS - 1) {
                                if (getTile(j, i + 1) > 0) {
                                    return false;
                                }
                            } else if (i == NOROWS - 1) {
                                return false;
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }

    //***********************
    // Move Methods
    //***********************
    public boolean move(String direction) {
        switch (direction) {
            case "rotate":
                return rotate();
            case "right":
                if (canMove(direction)) {
                    moveRight();
                    return true;
                }
                break;
            case "left":
                if (canMove(direction)) {
                    moveLeft();
                    return true;
                }
                break;
            case "down":
                if (canMove(direction)) {
                    moveDown();
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Rotates the current piece 90 degrees to the right
     *
     * @return false if the tile could not be rotated.
     */
    private boolean rotate() {
        int[][] matrix;
        int dim;
        switch (getPieceId()) {
            case -1:
                dim = 4;
                break;
            case -4:
                dim = 2;
                break;
            default:
                dim = 3;
                break;
        }

        matrix = new int[dim][dim];
        int xMin = NOCOLLUMNS;
        int yMin = NOROWS;

        //Gets the coordinates of the topleftmost corner of the matrix.
        for (int x = 0; x < NOCOLLUMNS; x++) {
            for (int y = 0; y < NOROWS; y++) {
                if (getTile(x, y) < 0) {
                    if (xMin > x) {
                        xMin = x;
                    }
                    if (yMin > y) {
                        yMin = y;
                    }
                }
            }
        }

        //Assigns values to the matrix
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                try {
                    matrix[y][x] = getTile(x + xMin, y + yMin);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
        }

        //Creates a matrix with only the piece and removes it from the base matrix
        int[][] rotatedPiece = new int[dim][dim];
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                if (matrix[y][x] < 0) {
                    rotatedPiece[y][x] = matrix[y][x];
                    matrix[y][x] = 0;
                } else {
                    rotatedPiece[y][x] = 0;
                }
            }
        }

        //Rotates the rotatedPiece array 90 degrees right.
        rotatedPiece = rotateRight(rotatedPiece);

        //Removes empty columns
        rotatedPiece = removeEmptyRowsAndCols(rotatedPiece);

        for (int y = 0; y < rotatedPiece.length; y++) {
            for (int x = 0; x < rotatedPiece[0].length; x++) {
                if (matrix[y][x] > 0 && rotatedPiece[y][x] < 0) {
                    return false;
                } else {
                    matrix[y][x] = rotatedPiece[y][x];
                }
            }
        }

        //Removes the active pieces from the board.
        for (int x = 0; x < NOCOLLUMNS; x++) {
            for (int y = 0; y < NOROWS; y++) {
                if (getTile(x, y) < 0) {
                    setTile(x, y, 0);
                }
            }
        }

        //Applies the matrix to the board
        for (int y = 0; y < rotatedPiece.length; y++) {
            for (int x = 0; x < rotatedPiece[0].length; x++) {
                setTile(xMin + x, yMin + y, rotatedPiece[y][x]);
            }
        }

        return true;
    }

    /**
     * Moves the current piece one step to the right
     */
    private void moveRight() {
        for (int i = NOCOLLUMNS - 1; i >= 0; i--) {
            for (int j = 0; j < NOROWS; j++) {
                if (getTile(i, j) < 0) {
                    setTile(i + 1, j, getTile(i, j));
                    setTile(i, j, 0);
                }
            }
        }
    }

    /**
     * Moves the current piece one step to the left
     */
    private void moveLeft() {
        for (int i = 0; i < NOCOLLUMNS; i++) {
            for (int j = 0; j < NOROWS; j++) {
                if (getTile(i, j) < 0) {
                    setTile(i - 1, j, getTile(i, j));
                    setTile(i, j, 0);
                }
            }
        }
    }

    /**
     * Moves the current piece one step down
     */
    private void moveDown() {
        for (int i = NOROWS - 1; i >= 0; i--) {         //Y
            for (int j = NOCOLLUMNS - 1; j >= 0; j--) { //X
                if (getTile(j, i) < 0) {
                    setTile(j, i + 1, getTile(j, i));
                    setTile(j, i, 0);
                }
            }
        }
    }

    //***********************
    // Support Methods
    //***********************
    /**
     * Deselects all active pieces by setting their value, if negative to a positive
     * counterpart
     */
    public void deselectPiece() {
        for (int i = 0; i < NOCOLLUMNS; i++) {
            for (int j = 0; j < NOROWS; j++) {
                if (getTile(i, j) < 0) {
                    int v = getTile(i, j);
                    setTile(i, j, Math.abs(v));
                }
            }
        }
    }

    /**
     * Clears the rows that are full
     *
     * @return Number of rows cleare
     */
    public int clearRows() {
        int rowsCleared = 0;
        for (int i = 0; i < NOROWS; i++) {
            boolean canClearLine = true;
            for (int j = 0; j < NOCOLLUMNS; j++) {
                if (getTile(j, i) == 0) {
                    canClearLine = false;
                    break;
                }
            }
            if (canClearLine) {
                for (int j = 0; j < NOCOLLUMNS; j++) {
                    board[i][j] = 0;
                }

                for (int j = i; j > 0; j--) {
                    for (int k = 0; k < NOCOLLUMNS; k++) {
                        board[j][k] = getTile(k, j - 1);
                    }
                }
                rowsCleared++;
            }
        }
        return rowsCleared;
    }

    /**
     * Generates a new Piece with the id n.
     *
     * @param n is the number of the piece that is to be generated
     * @return true if the piece can was generated. Else false.
     */
    public boolean generatePiece(int n) {
        if (canGenerateNewPiece(n)) {
            switch (n) {
                case 1:
                    //I Piece
                    board[1][3] = -1;
                    board[1][4] = -1;
                    board[1][5] = -1;
                    board[1][6] = -1;
                    break;
                case 2:
                    //J-Piece
                    board[0][5] = -2;
                    board[1][5] = -2;
                    board[2][4] = -2;
                    board[2][5] = -2;
                    break;
                case 3:
                    //L-Piece
                    board[0][4] = -3;
                    board[1][4] = -3;
                    board[2][4] = -3;
                    board[2][5] = -3;
                    break;
                case 4:
                    //O-Piece
                    board[0][4] = -4;
                    board[0][5] = -4;
                    board[1][4] = -4;
                    board[1][5] = -4;
                    break;
                case 5:
                    //S-Piece
                    board[0][4] = -5;
                    board[1][4] = -5;
                    board[1][5] = -5;
                    board[2][5] = -5;
                    break;
                case 6:
                    //Z-Piece
                    board[0][5] = -6;
                    board[1][5] = -6;
                    board[1][4] = -6;
                    board[2][4] = -6;
                    break;
                case 7:
                    //T-Piece
                    board[0][4] = -7;
                    board[1][3] = -7;
                    board[1][4] = -7;
                    board[1][5] = -7;
                    break;
                default:
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets all values in the board to 0.
     */
    public void resetBoard() {
        for (int i = 0; i < NOCOLLUMNS; i++) {
            for (int j = 0; j < NOROWS; j++) {
                setTile(i, j, 0);
            }
        }
    }

    /**
     * Checks to see if it would create a conflict when adding two matrices togheter.
     *
     * @param matrix1 the matrix on which to add matrix2
     * @param matrix2 the matrix which is added.
     * @return false if it creates conflict else not.
     */
    private boolean createdConflict(int[][] matrix1, int[][] matrix2) {
        for (int y = 0; y < matrix1.length; y++) {
            for (int x = 0; x < matrix1[0].length; x++) {
                if (matrix1[y][x] > 0 && matrix2[y][x] < 0) {
                    return true;
                } else {
                    matrix1[y][x] = matrix2[y][x];
                }
            }
        }
        return false;
    }

    /**
     * Removes all collumns and rows where all elements are zero.
     *
     * @param matrix the matrix from which to remove.
     * @return the matrix without empty collumns and rows.
     */
    private int[][] removeEmptyRowsAndCols(int[][] matrix) {
        //Removes all empty rows from rotatedPiece
        ArrayList<int[]> rp = new ArrayList<>(Arrays.asList(matrix));
        for (int i = 0; i < 3; i++) {
            for (int y = 0; y < rp.size(); y++) {
                boolean rowIsEmpty = true;
                for (int x = 0; x < rp.get(0).length; x++) {
                    if (rp.get(y)[x] != 0) {
                        rowIsEmpty = false;
                    }
                }
                if (rowIsEmpty) {
                    rp.remove(y);
                }
            }
        }

        //Removes all empty collumns from the matrix
        for (int n = 0; n < 3; n++) {
            for (int x = 0; x < rp.get(0).length; x++) {
                boolean colIsEmpty = true;
                for (int y = 0; y < rp.size(); y++) {
                    if (rp.get(y)[x] < 0) {
                        colIsEmpty = false;
                        break;
                    }
                }

                if (colIsEmpty) {
                    int[][] k = new int[rp.size()][rp.get(0).length - 1];

                    for (int i = 0; i < rp.size(); i++) {
                        for (int j = 0; j < rp.get(i).length - 1; j++) {
                            if (j >= x) {
                                k[i][j] = rp.get(i)[j + 1];
                            }
                        }
                    }
                    rp = new ArrayList<>(Arrays.asList(k));
                    break;
                }
            }
        }

        int[][] retur = new int[rp.size()][rp.get(0).length];
        for (int i = 0; i < rp.size(); i++) {
            System.arraycopy(rp.get(i), 0, retur[i], 0, rp.get(i).length);
        }

        return retur;
    }

    /**
     * Rotates a square matrix 90 degrees to the right.
     *
     * @param matrix the matrix which to rotate.
     * @return the rotated matrix.
     */
    private int[][] rotateRight(int[][] matrix) {
        matrix = transpose(matrix);
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length / 2; x++) {
                int temp = matrix[y][matrix.length - x - 1];
                matrix[y][matrix.length - x - 1] = matrix[y][x];
                matrix[y][x] = temp;
            }
        }
        return matrix;
    }

    /**
     * Transposes a matrix
     *
     * @param matrix the matrix to be transposed.
     * @return the transposed matrix.
     */
    private int[][] transpose(int[][] matrix) {
        int[][] transpose = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                transpose[i][j] = matrix[j][i];
            }
        }
        return transpose;
    }

    /**
     * Creates a deep copy of the array.
     *
     * @param original the array to be copied
     * @return the copied array.
     */
    private int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    @Override
    public String toString() {
        String s = "   |   1    2    3    4    5    6    7    8    9   10 \n";
        s += "------------------------------------------------------ \n";
        for (int i = 0; i < NOROWS; i++) {
            if (i < 10) {
                s += "0";
            }
            s += i;
            s += " | ";
            for (int j = 0; j < NOCOLLUMNS; j++) {
                if (board[i][j] >= 0) {
                    s += "  ";
                }
                s += board[i][j];
                s += ", ";
            }
            s += "\n";
        }
        return s;
    }

    @Override
    public Board clone() {
        Board board = new Board();
        board.setBoard(deepCopy(this.board));
        return board;
    }

    //***********************
    // Getter Methods
    //***********************
    public int[][] getBoard() {
        return board;
    }

    /**
     * Returns the tile at position (x, y).
     *
     * @param x The Tiles X-cooridnate (Collumn id) (0-9 by default)
     * @param y The Tiles Y-coordinate (Row id) (0-19 by default)
     * @return the id of the tile 1 to 7 if it is set, 0 if there is no tile and -1 to -7
     * if it it is the current tile.
     */
    public int getTile(int x, int y) {
        return board[y][x];
    }

    /**
     * Returns the id of the currently active piece (-1 to -7).
     *
     * @return
     */
    public int getPieceId() {
        for (int i = 0; i < NOCOLLUMNS; i++) {
            for (int j = 0; j < NOROWS; j++) {
                if (getTile(i, j) < 0) {
                    return getTile(i, j);
                }
            }
        }
        return 0;
    }

    /**
     * Returns the height of the highest collumn currently on the board.
     *
     * @return the height of the highest collumn.
     */
    public int getHeightOfHighestCollumn() {
        int height = 0;
        for (int i = 0; i < NOCOLLUMNS; i++) {
            if (getHeightOfColumn(i) > height) {
                height = getHeightOfColumn(i);
            }
        }

        return height;
    }

    /**
     * A Hole is when there's one or more tiles above an empty space in a column.
     *
     * @return the number of holes-
     */
    public int getNumberOfHoles() {
        int n = 0;
        for (int i = 0; i < NOCOLLUMNS; i++) {
            for (int j = NOROWS - 1; j >= 0; j--) {
                if (0 == getTile(i, j)) {
                    for (int k = j; k >= 0; k--) {
                        int o = getTile(i, k);
                        if (o != 0 && o > 0) {
                            n++;
                            break;
                        }
                    }
                }
            }
        }
        return n;
    }

    /**
     * The sum of the height of all the collumns on the board
     *
     * @return
     */
    public int getTotalHeight() {
        int n = 0;
        for (int i = 0; i < NOCOLLUMNS; i++) {
            n += getHeightOfColumn(i);
        }
        return n;
    }

    /**
     * Number of lines that can be cleared on the board
     *
     * @return
     */
    public int getNumberOfLinesCleared() {
        int noRowsCleared = 0;
        for (int i = 0; i < NOROWS; i++) {
            boolean canClearLine = true;
            for (int j = 0; j < NOCOLLUMNS; j++) {
                int t = getTile(j, i);
                if (t == 0) {
                    canClearLine = false;
                    break;
                }
            }
            if (canClearLine) {
                noRowsCleared++;
            }
        }
        return noRowsCleared;
    }

    /**
     * How bumby the board is, i.e. the difference between all the adjacent collumns
     * heights summed.
     *
     * @return
     */
    public int getBumpiness() {
        int n = 0;
        for (int i = 0; i < NOCOLLUMNS - 1; i++) {
            n += Math.abs(getHeightOfColumn(i) - getHeightOfColumn(i + 1));
        }
        return n;
    }

    /**
     * Height of the collumn at the given index
     *
     * @param index of the collumn (0-9 by default)
     * @return
     */
    public int getHeightOfColumn(int index) {
        for (int i = 0; i < NOROWS; i++) {
            if (getTile(index, i) > 0) {
                return 20 - i;
            }
        }
        return 0;
    }

    //***********************
    // Setter Methods
    //***********************
    /**
     * Sets the specified tile to the value specified.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param value the new value
     * @return false if the operation was unsucessfull.
     */
    private boolean setTile(int x, int y, int value) {
        if (x > 9 || x < 0 || y > 19 || y < 0 || getTile(x, y) > 0) {
            return false;
        }
        board[y][x] = value;
        return true;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

}
