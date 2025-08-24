import React from "react";

export default function Cell({ cell, onReveal, onFlag, onSuperReveal}) {
    const handleClick = () => {
        if (!cell.seen){
            onReveal();
        } 
    }
    const handleRightClick = (e) => {
        e.preventDefault();
        onFlag();
    };
    const handleDoubleClick = () => {
        if (cell.seen && cell.number > 0) {
        onSuperReveal();
        }
    };
    let display = "";
    let className = "cell";
  
    if (cell.flagged) {
    display = "🚩";
        className += " flagged";
    } else if (cell.seen) {
    if (cell.bomb) {
        display = "💣";
        className += " bomb";
    } else if (cell.number > 0) {
        display = cell.number;
        className += " revealed number"; 
        className += ` number-${cell.number}`;
    } else {
        className += " revealed";
    }
    }

  return (
    <button
      className={className} 
      onClick={handleClick}
      onContextMenu={handleRightClick}
      onDoubleClick={handleDoubleClick}
    >
      {display}
    </button>
  );
}