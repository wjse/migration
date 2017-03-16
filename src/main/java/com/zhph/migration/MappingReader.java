package com.zhph.migration;

import com.zhph.migration.po.TableMapping;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Mapping file reader
 */
public class MappingReader {

    /**
     * mapping file path
     */
    private static final String MAPPING_PATH = "com.zhph.migration.mapping";

    /**
     * mapping file name
     */
    private static final String MAPPING_FILE_NAME = "/table_mapping.properties";

    /**
     * mapping field handlers
     */
    private List<MigrationHandler> handlers;

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(MappingReader.class);

    /**
     * handlers setter for spring applicationContext
     * @param handlers
     */
    public void setHandlers(List<MigrationHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * read properties to mapping TableMapping Object
     * will map the source table name , and the target table name,
     * and any mapped column when you want
     * @return TableMapping
     * @throws IOException when the mapping file read has something wrong.
     */
    public TableMapping read() throws IOException {
        Properties properties = LoadMappingProperties();
        if(null == handlers || handlers.size() == 0){
            LOGGER.warn("There is no MigrationHandler configuration in context");
            return null;
        }

        TableMapping mapping = new TableMapping();
        for (MigrationHandler handler : handlers){
            handler.handleProperties(properties,mapping);
        }

        LOGGER.info("table mapping ended");
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(" : " + mapping);
        }
        return mapping;
    }

    /**
     * load properties file to the Properties Object
     * @return Properties
     * @throws IOException when the mapping file read has something wrong.
     */
    private Properties LoadMappingProperties() throws IOException {
        LOGGER.info("start load properties");
        String basePath = resolveBasePackage();
        Properties properties = new Properties();
        properties.load(new FileInputStream(basePath));
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(" files in: ");
            LOGGER.debug(basePath);
        }
        return properties;
    }

    /**
     * transfer MAPPING_PATH to physical real file path
     * @return physical real file path
     */
    private String resolveBasePackage() {
        final String systemPath = System.getProperty("user.dir");
        final String basePath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(MAPPING_PATH));
        final String srcPath = "/src/main/java/";
        return String.format("%s%s%s%s",systemPath,srcPath,basePath,MAPPING_FILE_NAME);
    }
}
