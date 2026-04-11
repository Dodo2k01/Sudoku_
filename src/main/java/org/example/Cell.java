package org.example;

import java.util.*;

public class Cell {
    int value = 0;
    int xAxis;
    int yAxis;
    Group row;
    Group col;
    Group cube;

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public Cell(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }



    public boolean setValue(int value) {
        if (value > 9 || value < 1) {throw new IllegalArgumentException("Value must be between 1 and 9");}
        if (this.value != 0) {return false;}
        if (!getDomain().contains(value)) {
            return false;
        }
        else{
            this.value = value;
            return true;
        }
    }

    public void removeValue(){
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public Group getRow() {
        return row;
    }

    public void setRow(Group row) {
        this.row = row;
    }

    public Group getCube() {
        return cube;
    }

    public void setCube(Group cube) {
        this.cube = cube;
    }

    public Group getCol() {
        return col;
    }

    public void setCol(Group col) {
        this.col = col;
    }

    public boolean equals(Cell cell){
        if (this.value != cell.value) return false;
        if (this.xAxis != cell.xAxis) return false;
        if (this.yAxis != cell.yAxis) return false;
        return true;
    }

    @Override
    public String toString(){
       return String.format("x-axis: %d, y-axis: %d, val: %d", xAxis, yAxis, value);
    }

    public List<Integer> getDomain() {
        if (this.value != 0) return Collections.emptyList();
        Group[] groups = {row, col, cube};
        Set<Integer> intersection = new HashSet<>(Utils.getFullDomain());
        for (Group g : groups) {
            if(g!=null) intersection.retainAll(g.getGroupDomain());
        }
        return new ArrayList<>(intersection);
    }
}