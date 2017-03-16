package com.zhph.migration;

import com.zhph.migration.po.TableMapping;

import java.util.Properties;

/**
 * Created by Administrator on 2017/3/14.
 */
public interface MigrationHandler {

    void handleProperties(Properties properties , TableMapping tableMapping);
}
