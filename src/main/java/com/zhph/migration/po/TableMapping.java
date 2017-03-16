package com.zhph.migration.po;

import java.util.Map;

/**
 * Entity of source table and target table mapping object
 */
public class TableMapping {

    /**
     * source table name
     */
    private String sourceTableName;

    /**
     * target table name
     */
    private String targetTableName;

    /**
     * columns mapping map
     * source column is key and target column is value
     */
    private Map<String,Object> columnsMapping;

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public Map<String, Object> getColumnsMapping() {
        return columnsMapping;
    }

    public void setColumnsMapping(Map<String, Object> columnsMapping) {
        this.columnsMapping = columnsMapping;
    }

    @Override
    public String toString() {
        return "TableMapping{" +
                "sourceTableName='" + sourceTableName + '\'' +
                ", targetTableName='" + targetTableName + '\'' +
                ", columnsMapping=" + columnsMapping +
                '}';
    }
}
