package com.zhph.migration.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@Component("TargetMetaDataDAO")
public class TargetMetaDataDAO {

    private static final Logger LOGGER = Logger.getLogger(TargetMetaDataDAO.class);

    @Autowired@Qualifier("sqlMapClientTemplateTarget")
    private SqlMapClientTemplate sqlMapClientTemplate;

    public List<String> getAllTargetTableColumns (String tableName){
        DataSource dataSource = sqlMapClientTemplate.getDataSource();
        Connection conn = null;
        ResultSet resultSet = null;
        List<String> columns = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            resultSet = metaData.getColumns(null,null,tableName,null);
            columns = new ArrayList<>();
            while(resultSet.next()){
                columns.add(resultSet.getString(4));//column name , 6 is column type
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }finally {
            try {
                if(null != resultSet){
                    resultSet.close();
                }

                if(null != conn){
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error(e);
            }
        }
        return columns;
    }
}
