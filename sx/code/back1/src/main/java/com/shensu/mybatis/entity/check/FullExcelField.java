package com.shensu.mybatis.entity.check;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName FullExcelField.java
 * @Description 查询补种信息详细的表
 * @createTime 2023年04月13日 15:00
 */
public class FullExcelField {

    public FullExcelField() {
    };

    public FullExcelField(String filedName, int rowId, short colId, Class<?> filedclazz) {
        super();
        this.filedName = filedName;//对应学生的字段名
        this.rowId = rowId;
        this.colId = colId;
        this.filedclazz = filedclazz;//对应学生的字段名的值
    }

    private String   filedName;
    private int      rowId;
    private short    colId;
    private Class<?> filedclazz;

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public short getColId() {
        return colId;
    }

    public void setColId(short colId) {
        this.colId = colId;
    }

    public Class<?> getFiledclazz() {
        return filedclazz;
    }

    public void setFiledclazz(Class<?> filedclazz) {
        this.filedclazz = filedclazz;
    }

}
