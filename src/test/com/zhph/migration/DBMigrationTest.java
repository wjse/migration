package com.zhph.migration;

import com.zhph.migration.dao.TargetMetaDataDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class DBMigrationTest {

    @Autowired
    DBMigration  dbMigration;

    @Autowired@Qualifier("TargetMetaDataDAO")
    TargetMetaDataDAO targetMetaDataDAO;


    @Test
    public void test_migrate() throws IOException, SQLException {
        dbMigration.startMigrate(true);
    }

//    @Test
    public void test_meta_data() throws SQLException {
//        targetMetaDataDAO.getAllTargetTableColumns();
    }
}
