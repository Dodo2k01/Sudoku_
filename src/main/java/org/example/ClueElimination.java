package org.example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ClueElimination {
    Sudoku solvedSudoku;
    int maxClues;
    Sudoku almostMinimalSudoku;


    public Sudoku getAlmostMinimalSudoku() {
        return almostMinimalSudoku;
    }

    public Sudoku getSolvedSudoku() {
        return solvedSudoku;
    }

    public ClueElimination(Sudoku solvedSudoku, int maxClues) {
        if (solvedSudoku.getCells().stream().anyMatch(c -> c.getValue()==0)){
            throw new IllegalArgumentException("The sudoku board passed is not solved!");
        }
        //Copy to avoid modifying the original board
        this.solvedSudoku = new Sudoku(solvedSudoku);
        if (maxClues < 17) {
            throw new IllegalArgumentException("Minimum number of clues must be at least 17");
        }
        else if (maxClues == 17){
            System.out.println("17 cues is not possible for all boards. " +
                    "Finding min number of clues is an NP-hard problem");
        }
        this.maxClues = maxClues;
    }

    /**

     */
    public void runPipeline(int timeLimitSeconds) {
        LocalDateTime tEnd = LocalDateTime.now().plusSeconds(timeLimitSeconds);
        Sudoku currentBestSudoku = new Sudoku(this.solvedSudoku);
        int minClues = 81;
        Random random = new Random();

        while (LocalDateTime.now().isBefore(tEnd)) {
            Sudoku working = new Sudoku(currentBestSudoku);
            List<Cell> workingCells = working.getCells();
            Collections.shuffle(workingCells, random);

            for (Cell cell : workingCells) {
                int val = cell.getValue();
                cell.removeValue();
                boolean unique = working.hasUnique();
//                System.out.printf("%s is leading to unique = %b\n", cell, unique);
                if (!unique){
                    cell.setValue(val);
                }
            }

            int nClues = 0;
            for (Cell cell : workingCells) {if(cell.getValue()!=0) nClues++;}
            if (nClues < minClues) {
                minClues = nClues;
                currentBestSudoku = working;
                System.out.printf("Found a board with %d clues", minClues);
            }
            if (minClues <= maxClues) {
                break;
            }
        }
        this.almostMinimalSudoku = currentBestSudoku;
    }

    public static void main(String[] args) {
        Sudoku solved = new Sudoku();
        solved.populateSudokuRandomly();
        System.out.println(solved);
        ClueElimination ce = new ClueElimination(solved, 20);
        ce.runPipeline(60);
        System.out.println(ce.getAlmostMinimalSudoku());
    }
}
