package com.dbs.app;

import com.dbs.com.bp.model.Cell;
import com.dbs.com.bp.model.CellMapper;
import com.dbs.service.CellEmitterService;
import com.dbs.service.CellSubscriptionCheckService;
import io.reactivex.Observable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CsvApp {

    public static void main(String[] args) throws Exception {
        Object[][] input = { { 1, 2,"=R1C1*R2C4",4,5 }, { 6,"=R2C1*R0C0", 8,9, 10 }, { 11,12,13,14,15 } };
        HashMap<String,Cell> cellDictionary=new HashMap<String,Cell>();
        CellEmitterService cellEmitterService=new CellEmitterService();
        CellSubscriptionCheckService cellSubscriptionCheckService=new CellSubscriptionCheckService();

        cellEmitterService.readCsvEmitCell(input)
                .forEach(cellMapper-> {
                    String key=CellMapper.constructKey(cellMapper.get_row(),cellMapper.get_col());
                    Cell cell=new Cell();

                    if(cellDictionary.containsKey(key)){
                        cell=cellDictionary.get(key);
                        Cell.setValue(cell,cellMapper.get_cellValue());
                    }else{
                        cell=Cell.create(cellMapper.get_row(),cellMapper.get_col(),cellMapper.get_cellValue());
                    }
                    cell.set_dependencies(cellSubscriptionCheckService.buildDependencies(cell));

                    if(cellSubscriptionCheckService.requiresSubscription(cell)) {

                        cell.get_dependencies()
                                .forEach(dependency -> {
                                    if (!cellDictionary.containsKey(dependency.toString()))
                                        cellDictionary.put(dependency.toString(), dependency);
                                });
                        cell.subscribeToDependencies();
                    }

                    cell.sayDone();
                    printCellInfo(cell);
                });


        System.out.println("yess...");
    }

    public static void printCellInfo(Cell cell){
        int deps=0;
        String depList="";
        if(cell.get_dependencies()!=null){
            deps=cell.get_dependencies().size();
            depList=cell.get_dependencies().stream().collect(Collectors.toList()).toString();
        }
        System.out.println(cell.toString()
                                        + " ,Value=" + cell.get_cellValue()
                                        + " ,IsExpr=" + cell.get_isExpression()
                                        + ",Expr=" + cell.get_expression()
                                        + ",Deps=" + deps
                                        + ",DepsList=" +depList);
    }
}


