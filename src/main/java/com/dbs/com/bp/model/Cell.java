package com.dbs.com.bp.model;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Cell {
    private int _cellValue;
    private Boolean _isExpression;
    private int _rowId;
    private int _colId;
    private String _expression;
    private HashSet<Cell> _dependencies;
    private Subject<Cell>  _finished=  PublishSubject.create();

    public int get_cellValue() {
        return _cellValue;
    }

    public void set_cellValue(int _cellValue) {
        this._cellValue = _cellValue;
    }

    public Boolean get_isExpression() {
        return _isExpression;
    }

    public void set_isExpression(Boolean _isExpression) {
        this._isExpression = _isExpression;
    }

    public String get_expression() {
        return _expression;
    }

    public void set_expression(String _expression) {
        this._expression = _expression;
    }

    public int get_rowId() {
        return _rowId;
    }

    public void set_rowId(int _rowId) {
        this._rowId = _rowId;
    }

    public int get_colId() {
        return _colId;
    }

    public void set_colId(int _colId) {
        this._colId = _colId;
    }

    public HashSet<Cell> get_dependencies() {
        return _dependencies;
    }

    public void set_dependencies(HashSet<Cell> _dependencies) {
        this._dependencies = _dependencies;
    }

    public Observable<Cell> subscribe(){
        return  _finished;
    }

    public void subscribeToDependencies(){

        List<Observable<Cell>>  lstCellObs=_dependencies
                .stream()
                .map(cell1->cell1.subscribe()).collect(Collectors.toList());
        System.out.println(toString() + " subscribed to " + lstCellObs.size() + " cell(s)");
        Observable.merge(lstCellObs)
                .doOnNext(cell-> {System.out.println("Published " + cell.toString()
                            + " receiving in " + this.toString());
                    sayDone();
                })
                .toList()
                .subscribe((bln)->{
                    System.out.println("all done" + this.toString());

                },(throwable -> {}));
    }

    public void sayDone(){
        _finished.onNext(this);
        //_finished.onComplete();
    }

    @Override
    public String toString() {
        return CellMapper.constructKey(_rowId,_colId);
    }


    public static Cell create(int row, int col, Object element){
        Cell cell = new Cell();
        cell.set_rowId(row);
        cell.set_colId(col);

        setValue(cell,element);
        return cell;
    }

    public static void setValue(Cell cell, Object element){
        Boolean isExpr=isExpression(element);
        cell.set_isExpression(isExpr);
        if (isExpr) {
            cell.set_expression(element.toString());
        } else {
            cell.set_cellValue(Integer.valueOf(element.toString()));
        }
    }
    public static Boolean isExpression(Object element){
        return element.toString().startsWith("=");
    }


}
