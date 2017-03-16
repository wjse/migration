package com.zhph.migration.po;

import java.util.List;

/**
 * For target table batch insert object
 */
public class TargetBatchInsert {
    /**
     * target table name
     */
    private String tableName;

    /**
     * all of target table columns
     */
    private List<String> columns;

    /**
     * source table result of mapped columns queried
     */
    private List<Object> values;

    public TargetBatchInsert(String tableName, List<String> columns, List<Object> values) {
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
