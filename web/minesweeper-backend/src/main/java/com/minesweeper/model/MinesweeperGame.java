
package com.minesweeper.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;


public class MinesweeperGame {
    public int size;
    public int bombs;
    public boolean lost = false;
    public boolean won = false;
    public boolean generated = false; // bombs placed yet?
    public Cell[][] board;

    // convenience counters
    public int seenCount = 0;
    public int flagsUsed = 0;

    public MinesweeperGame() { }

    public MinesweeperGame(int size, int bombs) {
        int availableCells = size * size - 8;
        if (bombs > availableCells) {
            throw new IllegalArgumentException("Too many bombs for this board size and safe area!");
        }
        if (size <= 0) throw new IllegalArgumentException("size must be > 0");
        if (bombs < 0 || bombs >= size * size) throw new IllegalArgumentException("invalid bombs number");
        this.size = size;
        this.bombs = bombs;
        initBoard();
    }

    private void initBoard() {
        board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
        generated = false;
        lost = false;
        won = false;
        seenCount = 0;
        flagsUsed = 0;
    }

    public void generateBombs(int safeX, int safeY) {
        Random rand = new Random();
        int placed = 0;
        boolean[][] blocked = new boolean[size][size]; // keeps track of where the bombs can't be place (placed already or safe)

        
        
        // mark all 8 adjusent bombs to the first-click cell (safeX, safeY) as blocked
        if (safeX >= 0 && safeY >= 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = safeX + dx;
                    int ny = safeY + dy;
                    if (inBounds(nx, ny)) blocked[nx][ny] = true; // cannot place bombs here 
                }
            }
        }

        while (placed < bombs) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (board[x][y].isBomb()) continue;
            if (blocked[x][y]) continue;
            board[x][y].setBomb(true); // mark the bomb
            placed++;
        }

        assignNumbers();
        generated = true;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }

    private void assignNumbers() {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell c = board[i][j];
                if (c.isBomb()) {
                    c.setNumber(-1); // mark a bomb
                    continue;
                }
                int count = 0;
                for (int k = 0; k < 8; k++) {
                    int nx = i + dx[k], ny = j + dy[k];
                    if (inBounds(nx, ny) && board[nx][ny].isBomb()) count++;
                }
                c.setNumber(count);
            }
        }
    }
    
    public void reveal(int x, int y) {
        if (!inBounds(x, y) || lost || won) return;

        if (!generated) {
            // delay bomb generation until first reveal to guarantee safe first move
            generateBombs(x, y);
        }

        Cell start = board[x][y];
        if (start.isSeen() || start.isFlagged()) return;

        if (start.isBomb()) {
            // stepped on a bomb -> lose
            start.setSeen(true);
            lost = true;
            revealAllBombs();
            return;
        }

        // BFS flood-fill when number == 0
        Deque<Cell> dq = new ArrayDeque<>();
        dq.add(start);
        start.setSeen(true);
        seenCount++;
        

        // reveal adjesent non-bombs
        while (!dq.isEmpty()) {
            Cell c = dq.remove();
            if (c.getNumber() != 0) continue; // only expand 0-cells

            int cx = c.getX(), cy = c.getY();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = cx + dx, ny = cy + dy;
                    if (!inBounds(nx, ny)) continue;
                    Cell nb = board[nx][ny];
                    if (nb.isSeen() || nb.isFlagged()) continue;
                    if (!nb.isBomb()) {
                        nb.setSeen(true);
                        seenCount++;
                        if (nb.getNumber() == 0) dq.add(nb);
                    }
                }
            }
        }

        checkWin();
    }

    private void checkWin() {
        if (lost) return;

        int totalSeen = 0;
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if (board[i][j].isSeen()) totalSeen++;

        // win by revealing all non-bomb cells
        if (totalSeen == size * size - bombs) {
            won = true;
        }
    }

    private void revealAllBombs() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isBomb()) board[i][j].setSeen(true);
            }
        }
    }

    public void toggleFlag(int x, int y) {
        if (!inBounds(x, y) || lost || won) return;
        Cell c = board[x][y];
        if (c.isSeen()) return;
        c.setFlagged(!c.isFlagged());
        flagsUsed += c.isFlagged() ? 1 : -1;
        checkWin();
    }

    public Cell[][] getBoard() {
        return board;
    }
}