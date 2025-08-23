package com.minesweeper.controller;

import org.springframework.web.bind.annotation.*;
import com.minesweeper.model.MinesweeperGame;
import java.util.Map;

@RestController

@RequestMapping("/game")
public class GameController {
    private void ensureGameStarted() {
        if (game == null) throw new RuntimeException("Game not started");
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeException(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }

    private MinesweeperGame game;
    @PostMapping("/new")
    public MinesweeperGame newGame(@RequestParam int size, @RequestParam int bombs) {
        game = new MinesweeperGame(size, bombs);
        return game;
    }

    @PostMapping("/reveal")
    public MinesweeperGame reveal(@RequestParam int row, @RequestParam int col){
        ensureGameStarted();
        game.reveal(row,col);
        return game;
    }

    @PostMapping("/flag")
    public MinesweeperGame flag(@RequestParam int row, @RequestParam int col){
        ensureGameStarted();
        game.toggleFlag(row,col);
        return game;
    }

    @PostMapping("/reset")
    public MinesweeperGame resetGame(){
        ensureGameStarted();
        game = new MinesweeperGame(game.size, game.bombs);
        return game;
    }

    @PostMapping("/sreveal")
    public MinesweeperGame sReveal(@RequestParam int row, @RequestParam int col){
        ensureGameStarted();
        game.superReveal(row, col);
        return game;
    }

    @GetMapping("/status")
    public MinesweeperGame status() {
        ensureGameStarted();
        return game;
    }
}