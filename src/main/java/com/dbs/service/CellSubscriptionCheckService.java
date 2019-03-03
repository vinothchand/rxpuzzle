package com.dbs.service;

import com.dbs.com.bp.model.Cell;
import io.reactivex.Observable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;


public class CellSubscriptionCheckService {

    public Boolean requiresSubscription(Cell cell)
    {
        if(!cell.get_isExpression()|| cell.get_dependencies()==null)
            return false;
        return cell.get_dependencies()
                .stream()
                .anyMatch(dependency->{
                    return isDependentNotCreated(cell,dependency.get_rowId(),dependency.get_colId());
                });
    }

    public Boolean isDependentNotCreated(Cell cell,int rowId,int colId){
        if(rowId>cell.get_rowId()) return true;
        if(rowId==cell.get_rowId() && colId>cell.get_colId()) return true;
        return false;
    }

    public HashSet<Cell> buildDependencies(Cell cell){

        if(!cell.get_isExpression()) return null;

        String expression=cell.get_expression();
        String removeEqual=expression.replace("=","");

        return new HashSet<Cell>( Arrays.stream(removeEqual.split("\\*"))
                .map(rowCol->{
                    String removeR=rowCol.replace("R","");
                    String[] splitRowCol=removeR.split("C");
                    if(isDependentNotCreated(cell,Integer.valueOf(splitRowCol[0]),Integer.valueOf(splitRowCol[1])))
                        return Cell.create(Integer.valueOf(splitRowCol[0]),Integer.valueOf(splitRowCol[1]),0);
                    return null;
                })
                .filter(cellFilter->cellFilter!=null)
                .collect(Collectors.toList()));

    }
}
