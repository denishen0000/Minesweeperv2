import React from "react";

export default function Cell({ cell, onReveal, onFlag }) {
  const handleClick = () => onReveal();
  const handleRightClick = (e) => {
    e.preventDefault();
    onFlag();
  };

  let display = ""; // default empty
  if (cell.flagged) display = "🚩";
  else if (cell.seen && cell.bomb) display = "💣";
  else if (cell.seen && cell.number > 0) display = cell.number;

  return (
    <button
      className="cell"
      onClick={handleClick}
      onContextMenu={handleRightClick}
    >
      {display}
    </button>
  );
}