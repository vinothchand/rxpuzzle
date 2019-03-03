package com.dbs.service;

import com.dbs.com.bp.model.Cell;
import com.dbs.com.bp.model.CellMapper;
import io.reactivex.Observable;

import java.util.Optional;


public class CellEmitterService {

    public Observable<CellMapper> readCsvEmitCell(Object[][] cellValues)
    {
        return Observable.create(emitter->{
            /*simulating a file*/
            for (int row = 0; row < cellValues.length; row++){
                for (int col = 0; col < cellValues[row].length; col++){
                    emitter.onNext(new CellMapper(row,col,cellValues[row][col]));
                }
            }
        });


    }
}
