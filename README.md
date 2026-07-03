# HangmanGame

A console-based Hangman game written in Java with a balloon-popping twist, ANSI-colored output, and a live alphabet tracker box — all in a single, self-contained class.

## Features

- **Balloon-based lives system** — you start with 6 balloons; each wrong guess pops one
- **Live alphabet tracker** — a bordered A–Z box shows every letter: green if guessed correctly, a red dash if guessed wrong, plain white if still unused
- **Colorized terminal UI** — ANSI escape codes for balloons, correct/wrong feedback, and win/lose banners
- **Duplicate-letter handling** — guessing a letter that appears multiple times (e.g. both O's in "LOOP") reveals all occurrences at once
- **Input validation** — rejects anything that isn't a single letter, and won't penalize you for repeating a guess
- **Replayable** — play again immediately after a round ends, no restart needed
- **Programming-themed word bank** — words like `POLYMORPHISM`, `ENCAPSULATION`, `INHERITANCE`, `CONSTRUCTOR`, etc.

## Controls

Type a single letter and press Enter when prompted. After a round ends, type `y` to play again or anything else to quit.

## Tech Stack

- **Language:** Java
- **No external dependencies** — pure Java standard library (`java.util.ArrayList`, `java.util.Scanner`)

## Project Structure

```
HangmanGame/
└── HangmanGame.java   # Entire game — fields, main loop, and all display/logic methods
```

Everything lives in one class (`HangmanGame`) with static fields and methods, since the game only ever needs a single instance running at a time. Key methods:

| Method | Responsibility |
|---|---|
| `main` | Outer "play again?" loop |
| `initGame` | Resets state for a new round (random word, fresh balloons, empty guess lists) |
| `playGame` | Core round loop — reads input, validates, updates state |
| `printGameState` | Orchestrates the balloons, alphabet box, word, and wrong-guess display |
| `printBalloons` / `printAlphabetBox` / `printLetter` | Rendering helpers |
| `revealLetter` | Updates all matching positions when a correct letter is guessed |
| `checkWin` / `alreadyGuessed` | Game-state checks |
| `getDisplayWord` / `getWrongGuesses` | Build formatted strings via `StringBuilder` |

> **Note:** The repo also includes a `Main.java` that appears to be IntelliJ's default scratch/tutorial file (an unrelated "Hello and welcome!" demo using Java's newer implicit `main` syntax), not part of the actual game — `HangmanGame.java` already has its own `main` method. You'll likely want to remove `Main.java` before committing, or keep it out of the build.

## Getting Started

### Prerequisites

- Java JDK 8+
- A terminal that supports ANSI colors and Unicode box-drawing characters (most modern terminals do)

### Compile and Run

```bash
git clone https://github.com/<your-username>/HangmanGame.git
cd HangmanGame
javac HangmanGame.java
java HangmanGame
```

## License

Add a license of your choice (e.g. MIT) — see [choosealicense.com](https://choosealicense.com/) for guidance.
