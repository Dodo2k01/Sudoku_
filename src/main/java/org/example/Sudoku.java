package org.example;

import java.util.*;

public class Sudoku {
    Group[] rows = new Group[9];
    Group[] cols = new Group[9];
    Group[] cubes = new Group[9];
    List<Cell> cells;


    public Sudoku() {
        List<Cell> cells = new ArrayList<>(81);
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < cols.length; j++) {
                cells.add(new Cell(i, j));
            }
        }
        this.cells = cells;
        for (int i = 0; i < 9; i++) {
            rows[i] = new Group(i);
            cols[i] = new Group(i);
            cubes[i] = new Group(i);
        }

        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            int xAxis = cell.getxAxis();
            int yAxis = cell.getyAxis();
            rows[xAxis].addCell(cell);
            cell.setRow(rows[xAxis]);
            cols[yAxis].addCell(cell);
            cell.setCol(cols[yAxis]);
            cubes[(xAxis/3) * 3 + yAxis/3].addCell(cell);
            cell.setCube(cubes[(xAxis/3) * 3 + yAxis/3]);
        }
    }

    public Sudoku(Sudoku sudoku) {
        this.cells = new ArrayList<>(sudoku.cells.size());
        for (Cell c : sudoku.cells) {
            Cell cellCloned = new Cell(c.getxAxis(), c.getyAxis());
            int cVal = c.getValue();
            if (cVal != 0) {
                cellCloned.setValue(cVal);
            }
            this.cells.add(cellCloned);
        }
        for (int i = 0; i < 9; i++) {
            rows[i] = new Group(i);
            cols[i] = new Group(i);
            cubes[i] = new Group(i);
        }
        for (Cell c : this.cells) {
            int x = c.getxAxis();
            int y = c.getyAxis();
            rows[x].addCell(c);
            c.setRow(rows[x]);
            cols[y].addCell(c);
            c.setCol(cols[y]);
            cubes[(x/3) * 3 + y/3].addCell(c);
            c.setCube(cubes[(x/3) * 3 + y/3]);
        }
    }

    public boolean populateSudokuRandomly(){
        return populateSudokuRandomly(new ArrayList<>(this.cells));
    }

    public boolean populateSudokuRandomly(List<Cell> remCells) {
        if (remCells.isEmpty()) {return true;}
        Cell minDomainCell = Collections.min(remCells, Comparator.comparingInt(c -> c.getDomain().size()));
        remCells.remove(minDomainCell);
        List<Integer> domain = minDomainCell.getDomain();
        Collections.shuffle(domain, new Random(42));
        for (Integer val : domain){
            minDomainCell.setValue(val);
            if(populateSudokuRandomly(remCells)) {
                return true;
            }
            else{
                minDomainCell.removeValue();
            }
        }
        remCells.add(minDomainCell);
        return false;
    }


    public boolean solveSudoku(List<Cell> remCells){
        if (remCells.isEmpty()) {return true;}
        Cell best = Collections.min(remCells, Comparator.comparingInt(c -> c.getDomain().size()));
        remCells.remove(best);
        for (Integer val : best.getDomain()){
            best.setValue(val);
            if(solveSudoku(remCells)) {
                return true;
            }
            else{
                best.removeValue();
            }
        }
        remCells.add(best);
        return false;
    }

    public int countSolutions(List<Cell> remCells, int maxSolutions) {
        if (remCells.isEmpty()) {return 1;}
        Cell best = Collections.min(remCells, Comparator.comparingInt(c -> c.getDomain().size()));
        remCells.remove(best);
        int solutions = 0;
        var domain = best.getDomain();
        for (Integer val : domain){
            best.setValueNoDomainCheck(val); // (no overhead)
            solutions += countSolutions(remCells, maxSolutions - solutions);
            best.removeValue();
            if (solutions >= maxSolutions) break;
        }
        remCells.add(best);
        return solutions;
    }

    public boolean hasUnique(){
        List<Cell> zeroCells = new ArrayList<>(this.cells);
        zeroCells.removeIf(c -> c.getValue() != 0);
        return countSolutions(zeroCells, 2) == 1;
    }


    public List<Cell> getCells() {
        return cells;
    }

    public boolean equals(Sudoku sudoku) {
        for (int i = 0; i < sudoku.cells.size(); i++) {
            if (!this.cells.equals(sudoku.cells.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        int[][] board = new int[9][9];
        for (var cell : this.cells) {
            board[cell.getxAxis()][cell.getyAxis()] = cell.getValue();
        }
        return Arrays.deepToString(board);
    }


    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.populateSudokuRandomly();
        System.out.println(sudoku);
    }
}
