package com.zhph.migration.handler;

import com.zhph.migration.MigrationHandler;
import com.zhph.migration.po.TableMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ColumnsHandler implements MigrationHandler {

    @Override
    public void handleProperties(Properties properties, TableMapping tableMapping) {
        Set<String> names = properties.stringPropertyNames();
        Map<String,Object> map = new HashMap<>();
        for(String key : names){
            if(isIgnore(key)){
                continue;
            }

            map.put(key,properties.get(key));
        }

        tableMapping.setColumnsMapping(map);
    }

    private boolean isIgnore(String key){
        return key.equals(TableNameHandler.SOURCE_TABLE_NAME_KEY) || key.equals(TableNameHandler.TARGET_TABLE_NAME_KEY);
    }
}
