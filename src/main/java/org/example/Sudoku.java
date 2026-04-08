package org.example;

import java.util.*;

public class Sudoku {
    Group[] rows = new Group[9];
    Group[] cols = new Group[9];
    Group[] cubes = new Group[9];
    List<Cell> cells;

    public record result(int[][] board, int nResults){}

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

    public boolean populateSudokuRandomly(List<Cell> remCells) {
        if (remCells.isEmpty()) {return true;}
        Cell minDomainCell = Collections.min(remCells, Comparator.comparingInt(c -> c.getDomain().size()));
        remCells.remove(minDomainCell);
        List<Integer> domain = minDomainCell.getDomain();
        Collections.shuffle(domain);
        for (Integer val : domain){
            if (minDomainCell.setValue(val)){
                if(populateSudokuRandomly(remCells)) {
                    return true;
                }
                else{
                    minDomainCell.removeValue();
                }
            }
        }
        remCells.add(minDomainCell);
        return false;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        if (sudoku.populateSudokuRandomly(new ArrayList<>(sudoku.getCells()))){
            System.out.println("Sudoku populated");
        }
        int[][] board = new int[9][9];
        for (var cell : sudoku.cells) {
            board[cell.getxAxis()][cell.getyAxis()] = cell.getValue();
        }

        System.out.println(Arrays.deepToString(board));

    }
}
