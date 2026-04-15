package sudoku;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dodoku")
public class SudokuController {

    public record PuzzleResponse(int[][] solved, int[][] puzzle) {}

    @GetMapping("/new")
    public PuzzleResponse newSudoku() {
        Sudoku sudoku = new Sudoku();
        sudoku.populateSudokuRandomly();
        ClueElimination ce = new ClueElimination(sudoku, 25);
        ce.runPipeline(3);
        return new PuzzleResponse(toGrid(ce.getSolvedSudoku()), toGrid(ce.getPuzzle()));
    }



    private int[][] toGrid(Sudoku sudoku) {
        int [][] grid = new int[9][9];
        for (Cell c : sudoku.getCells()) {grid[c.getxAxis()][c.getyAxis()] = c.getValue();}
        return grid;
    }
}
