import React, { useState, useEffect, useCallback } from "react";
import Cell from "./Cell";
import { getStatus, newGame, superReveal } from "../services/api";
import { revealCell, flagCell } from "../services/api";

export default function Board() {
    const [board, setBoard] = useState([]);

    const loadBoard = useCallback(async () => {
        const data = await getStatus();
        setBoard(data.board);
    }, []);

    const size = 10

    const resetGame = useCallback(() => {
        newGame(10, 10).then(loadBoard);
    }, [loadBoard]); 

    const handleKeyPress = useCallback((event) => {
        if (event.key === 'r' || event.key === 'R') {
            resetGame();
        }
    }, [resetGame, loadBoard]);

    useEffect(() => {
        newGame(size, 10).then(loadBoard); // start new 8x10 board
    }, [loadBoard]);

    const handleReveal = useCallback((x, y) => {
        revealCell(x, y).then(loadBoard);
    }, [loadBoard]); 
    
    const handleFlag = useCallback((x, y) => {
        flagCell(x, y).then(loadBoard);
    }, [loadBoard]); 

    const handleSuperReveal = useCallback((x, y) => {
        superReveal(x, y).then(loadBoard);
    }, [loadBoard]); 

    useEffect(() => {
        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [handleKeyPress]);

    return (
        <div className="board"> 
            {board.map((row, i) => (
                <div key={i} className="row">
                    {row.map((cell, j) => (
                        <Cell
                            key={j}
                            cell={cell}
                            onReveal={() => handleReveal(i, j)}
                            onFlag={() => handleFlag(i, j)}
                            onSuperReveal={() => handleSuperReveal(i, j)}
                        />
                    ))}
                </div>
            ))}
        </div>
    );
}
