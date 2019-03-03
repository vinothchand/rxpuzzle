package com.dbs.com.bp.model;

public class CellMapper {
    private int _row;
    private int _col;
    private Object _cellValue;


    public CellMapper(int row,int col,Object cellValue){
        this._row=row;
        this._col=col;
        this._cellValue=cellValue;
    }
    public int get_row() {
        return _row;
    }

    public void set_row(int _row) {
        this._row = _row;
    }

    public int get_col() {
        return _col;
    }

    public void set_col(int _col) {
        this._col = _col;
    }

    public Object get_cellValue() {
        return _cellValue;
    }

    public void set_cellValue(Object _cellValue) {
        this._cellValue = _cellValue;
    }

    public static String constructKey(int row,int col){
        return "R"+row+"C"+col;
    }
}
