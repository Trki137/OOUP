package hr.fer.zemris.ooup;

import java.util.Objects;
import java.util.regex.Pattern;

public class Sheet {
    private Cell[][] cells;

    private int usedFields = 0;

    private final int maxCapacity;

    private final int rows;

    private final int columns;
    private final Pattern isNumberPattern = Pattern.compile("-?\\d+(\\\\.\\\\d+)?");

    public Sheet(int rows, int columns){
        this.cells = new Cell[rows][columns];
        this.maxCapacity = rows * columns;
        this.rows = rows;
        this.columns = columns;
    }

    private Cell cell(String ref){
        for(Cell[] cells1: cells){
            for(Cell cell: cells1)
                if(!Objects.isNull(cell) && cell.getName().equals(ref)) return cell;
        }

        return null;
    }

    public void set(String ref, String content){
        if(usedFields >= maxCapacity){
            System.out.println("There are no more cells left");
            return;
        }

        Cell cell = cell(ref);

        if(Objects.isNull(cell)){
            cell = createNewCell(ref);
        }

        boolean valid = checkIsValid(content);

        if(!valid){
            System.out.println("Invalid content.");
            return;
        }


        cell.setExp(content);

        if(isCircularDependent(ref,cell))
            throw new IllegalArgumentException("Circular dependency");

        cell.setValue(evaluate(cell));
        setListeners(cell);
    }

    private void setListeners(Cell cell) {
        if(isNumberPattern.matcher(cell.getExp()).matches())
            return;

        String[] splited = cell.getExp().split("\\+");

        for(String oneCell: splited){
            if(isNumberPattern.matcher(oneCell).matches())
                continue;

            Cell foundCell = cell(oneCell);
            if(Objects.isNull(foundCell)){
                System.out.println("Invalid input "+ oneCell);
                return;
            }

            foundCell.addListener(() -> cell.setValue(evaluate(cell)));
        }
    }

    public Integer evaluate(Cell cell){
        Objects.requireNonNull(cell);

        boolean isNumber = isNumberPattern.matcher(cell.getExp()).matches();

        if(isNumber) return cell.getValue();

        String[] cells = cell.getExp().split("\\+");

        int res = 0;
        for(String cellValue : cells){
            if(isNumberPattern.matcher(cellValue).matches()) res += Integer.parseInt(cellValue);
            else res += evaluate(cell(cellValue));
        }

        return res;
    }

    public void print(){
        for(Cell[] cells1 : cells){
            for(Cell oneCell: cells1){
                if(Objects.isNull(oneCell))continue;
                System.out.printf("Cell: %s, Expression: %s, value: %d\n",oneCell.getName(),oneCell.getExp(),oneCell.getValue());
            }
        }
    }
    private Cell createNewCell(String ref) {
        for(int i = 0;  i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(Objects.isNull(cells[i][j])){
                    Cell cell = new Cell(ref);
                    cells[i][j] = cell;
                    usedFields++;
                    return cell;
                }
            }
        }

        return null;
    }

    private boolean checkIsValid(String content) {
        boolean isNumber = isNumberPattern.matcher(content).matches();

        if(isNumber) return true;

        if(!content.contains("+")) return false;

        String[] operators = content.split("\\+");

        for(String operator: operators) {
            if (isNumberPattern.matcher(operator).matches()) continue;

            Cell foundCell = cell(operator.trim());
            if (Objects.isNull(foundCell)) return false;

        }

        return true;
    }

    private boolean isCircularDependent(String currentCellName, Cell cell) {
        String cellExp = cell.getExp();

        if(isNumberPattern.matcher(cellExp).matches()) return false;

        String[] split = cellExp.split("\\+");

        for(String value: split){
            if(isNumberPattern.matcher(value).matches()) continue;
            if(value.equals(currentCellName)) return true;

            Cell nextCell = cell(value);
            boolean isDependent = isCircularDependent(currentCellName,nextCell);

            if(isDependent) return true;
        }

        return false;
    }
}
