package sudoku;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/dodoku")
public class SudokuController {

    public record PuzzleResponse(int[][] solved, int[][] puzzle) {}
    public record LegalRequest(int[][] board, int row, int col, int val) {}
    public record LegalResponse(boolean legal){}

    @GetMapping("/new")
    public PuzzleResponse newSudoku() {
        Sudoku sudoku = new Sudoku();
        sudoku.populateSudokuRandomly();
        ClueElimination ce = new ClueElimination(sudoku, 25);
        ce.runPipeline(1);
        return new PuzzleResponse(toGrid(ce.getSolvedSudoku()), toGrid(ce.getPuzzle()));
    }

    @PostMapping("/legality")
    public LegalResponse legality(@RequestBody LegalRequest req) {
        Sudoku sudoku = new Sudoku(req.board);
        return new LegalResponse(sudoku.getCell(req.row, req.col).setValue(req.val));
    }



    private int[][] toGrid(Sudoku sudoku) {
        int [][] grid = new int[9][9];
        for (Cell c : sudoku.getCells()) {grid[c.getxAxis()][c.getyAxis()] = c.getValue();}
        return grid;
    }
}
