# Treasure Hunt Game

## Overview

Welcome to the "Treasure Hunt" game! This is a simple Java-based board game where players roll dice to advance through a grid-based map filled with hidden passages and pitfalls. The goal is to reach the treasure first.

## Features

- **Player Movement:** Players take turns rolling dice to move across the board.
- **Hidden Passages:** Discover hidden passages that advance you forward.
- **Pits:** Fall into pits that send you back to previous positions.
- **Multiplayer:** Supports up to 4 players.

## Requirements

- Java Development Kit (JDK) 8 or higher
- `algs4` library from Princeton University

## Files

- `Treasure_hunt.java`: Main game logic and user interface.
- `spacesSTART.txt`: Digraph file for the board layout.
- `diceRoll.wav`, `ladder_1.wav`, `chute_1.wav`, `gameWin.wav`: Sound files for the game.
- `quicksand.png`, `Treasure.png`, `cave2.png`, `dragon.png`: Image files for the game board.

## Installation

1. **Clone the Repository:**
   ```sh
   git clone https://github.com/yourusername/treasure-hunt.git
2. **Navigate to the Project Directory:**
   ```sh
   cd treasure-hunt
4. **Compile the Code:**
   ```sh
   javac -cp .:algs4.jar Treasure_hunt.java

5. **Run the Game:**
   ```sh
   java -cp .:algs4.jar Treasure_hunt <numPlayers>

## How to Play

1. **Start the Game:** Run the program with the number of players.
2. **Roll Dice:** Each player rolls the dice on their turn to advance.
3. **Discover Passages or Pits:** Move through the board to find hidden passages or fall into pits.
4. **Reach the Treasure:** The first player to reach the final space wins the game.

## Contributing

Feel free to fork the repository, make changes, and submit a pull request if you'd like to contribute. For major changes or feature requests, please open an issue to discuss them first.

