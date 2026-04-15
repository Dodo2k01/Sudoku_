package sudoku;

import java.util.*;
import java.util.stream.Collectors;

public class Group implements Cloneable{
    List<Cell> cells = new ArrayList<>(9);
    int groupIndex;

    public Group(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Set<Integer> getGroupValues() {
        return cells.stream().map(Cell::getValue).collect(Collectors.toSet());
    }

    public Set<Integer> getGroupDomain(){
        Set<Integer> domain = Utils.getFullDomain();
        domain.removeAll(getGroupValues());
        return domain;
    }
}
