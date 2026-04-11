package org.example;

public class ClueElimination {
    Sudoku solvedSudoku;
    Sudoku minimalSudoku;
    int minClues;
    int bestMinClues;

    public ClueElimination(Sudoku solvedSudoku, int minClues) {
        //Copy to avoid modifying the original board
        this.solvedSudoku = new Sudoku(solvedSudoku);
        if (minClues < 17) {
            throw new IllegalArgumentException("Minimum number of clues must be at least 17");
        }
        else if (minClues == 17){
            System.out.println("17 cues is not possible for all boards. " +
                    "Finding min number of clues is an NP-hard problem");
        }
        this.minClues = minClues;
    }

    /**

     */
    public void runPipeline(int timeLimitSeconds) {

    }
}
