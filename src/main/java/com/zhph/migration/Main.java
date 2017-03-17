package com.zhph.migration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Created by Administrator on 2017/3/17.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("start migrate...");
        ApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            Assert.notNull(context);
        }catch (BeanDefinitionStoreException e){
            ThereIsNoDataSource(e);
        }

        DBMigration dbMigration = (DBMigration) context.getBean("DBMigration");
        Assert.notNull(dbMigration);

        boolean isPageQuery = true;
        int pageNum = 1;
        int pageSize = 20;

        if(null != args && args.length > 0){
            String arg1 = args[0];
            String arg2 = args[1];
            String arg3 = args[2];

            if(StringUtils.isNotEmpty(arg1)){
                isPageQuery = Boolean.valueOf(arg1);
            }

            if(StringUtils.isNotEmpty(arg2) && StringUtils.isNumeric(arg2)){
                pageNum = Integer.parseInt(arg2);
            }

            if(StringUtils.isNotEmpty(arg3) && StringUtils.isNumeric(arg3)){
                pageSize = Integer.parseInt(arg3);
            }
        }

        dbMigration.setPageNum(pageNum);
        dbMigration.setPageSize(pageSize);
        try {
            dbMigration.startMigrate(isPageQuery);
            LOGGER.info("migrate success,please check your target database.");
        } catch (Exception e) {
            LOGGER.error("migrate fail.");
            LOGGER.error(e);
        }
    }

    private static void ThereIsNoDataSource(BeanDefinitionStoreException e){
        String beanName = e.getBeanName();
        if(beanName.endsWith("DataSource")){
            LOGGER.warn("There is no configuration of source table data source or target table data source.");
            LOGGER.warn("Please check \"src/main/resources/jdbc.properties\" first.Program will shutdown");
            System.exit(0);
        }
    }
}
