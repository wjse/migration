package com.zhph.migration.handler;

import com.zhph.migration.MigrationHandler;
import com.zhph.migration.po.TableMapping;

import java.util.Properties;

/**
 * Created by Administrator on 2017/3/14.
 */
public class TableNameHandler implements MigrationHandler {

    protected static final String SOURCE_TABLE_NAME_KEY = "source.table.name";
    protected static final String TARGET_TABLE_NAME_KEY = "target.table.name";

    @Override
    public void handleProperties(Properties properties, TableMapping tableMapping) {
        tableMapping.setSourceTableName(properties.getProperty(SOURCE_TABLE_NAME_KEY));
        tableMapping.setTargetTableName(properties.getProperty(TARGET_TABLE_NAME_KEY));
    }
}
