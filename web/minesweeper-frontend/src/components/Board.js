import React, { useState, useEffect } from "react";
import Cell from "./Cell";
import { getStatus, newGame } from "../services/api";
import { revealCell, flagCell } from "../services/api";

export default function Board() {
    const [board, setBoard] = useState([]);

    const loadBoard = async () => {
        const data = await getStatus(); 
        setBoard(data.board);           
    };

    useEffect(() => {
        newGame(10, 10).then(loadBoard); // start new 8x10 board
    }, []);

    const handleReveal = (x, y) => {
        revealCell(x, y).then(loadBoard); // tell backend to reveal, then reload
    };

    const handleFlag = (x, y) => {
        flagCell(x, y).then(loadBoard);   // toggle flag, then reload
    };

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
                />
                ))}
            </div>
            ))}
        </div>
    );

}
