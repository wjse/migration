package com.zhph.migration;

import com.zhph.migration.dao.SourceTableDAO;
import com.zhph.migration.dao.TargetMetaDataDAO;
import com.zhph.migration.dao.TargetTableDAO;
import com.zhph.migration.po.TableMapping;
import com.zhph.migration.po.TargetBatchInsert;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oproject.framework.orm.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Table data migration
 */
@Component("DBMigration")
public class DBMigration {

    private static final Logger LOGGER = Logger.getLogger(DBMigration.class);

    /**
     * mapping file reader
     */
    @Autowired
    private MappingReader reader;

    /**
     * source table DAO
     */
    @Autowired@Qualifier("SourceTableDAO")
    private SourceTableDAO sourceTableDAO;

    /**
     * target table DAO
     */
    @Autowired@Qualifier("TargetTableDAO")
    private TargetTableDAO targetTableDAO;

    /**
     * target table meta data DAO
     * for query target table all columns
     */
    @Autowired@Qualifier("TargetMetaDataDAO")
    private TargetMetaDataDAO targetMetaDataDAO;

    /**
     * is use page query
     * because the all table query will spent a large of time,
     * this switch control that use page query or all table query
     */
    private boolean isPageQuery;

    /**
     * when is page query , page num is useful
     * and default value is 1
     */
    private int pageNum = 1;

    /**
     * when is page query , page size is useful
     * and default value is 10
     */
    private int pageSize = 10;

    /**
     * page num setter for customer
     * @param pageNum
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * page size setter for customer
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * start table migrate
     * first choose is page query or not,
     * then mapping reader to start read mapping file,
     * in the end this to start migrate data
     * @param isPageQuery
     * @throws IOException when the mapping file read has something wrong
     */
    public void startMigrate(boolean isPageQuery) throws IOException {
        this.isPageQuery = isPageQuery;
        migrate(reader.read());
    }

    /**
     * table data migrate
     * @param mapping TableMapping
     */
    private void migrate(final TableMapping mapping){
        LOGGER.info("start migrate table mapping");
        long startTime = System.currentTimeMillis();

        Map<String,Object> sourceTableQueryParams = new HashMap<String,Object>(){{
            put("tableName",mapping.getSourceTableName());
            put("columns", new ArrayList<>(mapping.getColumnsMapping().keySet()));
        }};

        List<Map<String,Object>> result;
        if(this.isPageQuery){//page query
            PageResult<Map<String,Object>> page = sourceTableDAO.queryForPageResult(sourceTableQueryParams,this.pageNum,this.pageSize);
            result = page.getResultList();
        }else{//all table query
            result = sourceTableDAO.queryForList(sourceTableQueryParams);
        }

        List<String> targetAllColumns = targetMetaDataDAO.getAllTargetTableColumns(mapping.getTargetTableName());
        List<TargetBatchInsert>  targetBatchInsertPOS = resolveSourceResult(mapping.getTargetTableName(),result,mapping.getColumnsMapping(),targetAllColumns);
        targetTableDAO.batchInsert(targetBatchInsertPOS);

        LOGGER.info("table migrate ended , spent : " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * resolve the data of source table queried,
     * and sort out will insert data of target table
     * @param tableName target table name
     * @param sourceTableResult source table queried data
     * @param columnsMapping mapping of source table column and target table column
     * @param targetAllColumns all of target columns
     * @return List<TargetBatchInsert>
     */
    private List<TargetBatchInsert> resolveSourceResult(final String tableName, final List<Map<String,Object>> sourceTableResult, final Map<String,Object> columnsMapping , List<String> targetAllColumns){
        List<TargetBatchInsert> targetBatchInsertPOS = new ArrayList<>();
        for(final Map<String,Object> result : sourceTableResult){
            final List<Object> mappedList = findMappingColumn(columnsMapping, result , targetAllColumns);
            targetBatchInsertPOS.add(new TargetBatchInsert(tableName,targetAllColumns,mappedList));
        }
        return targetBatchInsertPOS;
    }

    /**
     * find source data column mapping of target table column
     * and find the position of target column
     * @param columnsMapping mapping of source table column and target table column
     * @param result source table queried data
     * @param targetAllColumns all of target columns
     * @return List<Object> will batch insert data of target table
     */
    private List<Object> findMappingColumn(Map<String,Object> columnsMapping , Map<String,Object> result,List<String> targetAllColumns){
        List<Object> mappedList = new ArrayList<>(targetAllColumns.size());
        /**
         * set all of target table column value is ""
         */
        for(int i = 0; i < targetAllColumns.size() ; i++){
            mappedList.add("");
        }

        /**
         * find target column map source column and set source data in batch insert data
         */
        for(int i = 0; i < targetAllColumns.size() ; i++){
            String targetColumn = targetAllColumns.get(i);
            for(Object v : columnsMapping.values()){
                if(targetColumn.equals(v)){
                    mappedList.set(i, findMappedValue(targetColumn,columnsMapping,result));
                }
            }
        }

        return mappedList;
    }

    /**
     * find mapped source data value
     * @param targetColumn target column
     * @param columnsMapping column mapping map
     * @param result source table queried data
     * @return Object source data value
     */
    private Object findMappedValue(String targetColumn,Map<String,Object> columnsMapping, Map<String, Object> result){
        String sourceColumn = findSourceColumn(targetColumn,columnsMapping);
        return findResultValue(sourceColumn,result);
    }

    /**
     * find source column by columns mapping map
     * @param targetColumn target column,mapping map's value
     * @param columnsMapping mapping map
     * @return source column
     */
    private String findSourceColumn(String targetColumn, Map<String, Object> columnsMapping) {
        for (Map.Entry<String, Object> entry : columnsMapping.entrySet()) {
            if (entry.getValue().equals(targetColumn)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * find source data by source column
     * @param sourceColumn source column
     * @param result source data
     * @return source data value
     */
    private String findResultValue(String sourceColumn, Map<String, Object> result) {
        if(StringUtils.isEmpty(sourceColumn)){
            return "";
        }

        for(String k : result.keySet()){
            if(k.equals(sourceColumn)){//there is a bug,only set all check off of target table ,it can work successfully.
                return (result.get(k) == null || result.get(k).toString().equals("null")) ? "" : result.get(k).toString();
            }
        }
        return "";
    }
}
