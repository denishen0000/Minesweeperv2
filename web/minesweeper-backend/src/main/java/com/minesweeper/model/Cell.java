package com.minesweeper.model;

public class Cell {
    private int x;
    private int y;
    private boolean seen = false;
    private boolean bomb = false;
    private boolean flagged = false;
    private int number = 0; // adjacent bombs

    public Cell() { } 

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isSeen() { return seen; }
    public boolean isBomb() { return bomb; }
    public boolean isFlagged() { return flagged; }
    public int getNumber() { return number; }

 
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSeen(boolean seen) { this.seen = seen; }
    public void setBomb(boolean bomb) { this.bomb = bomb; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }
    public void setNumber(int number) { this.number = number; }
}
