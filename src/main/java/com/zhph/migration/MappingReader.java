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
     * mappingFileName file path
     */
    private String mappingPackage;

    /**
     * mappingFileName file name
     */
    private String mappingFileName = "/table_mapping.properties";

    /**
     * mappingFileName field handlers
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

    public void setMappingPackage(String mappingPackage) {
        this.mappingPackage = mappingPackage;
    }

    public void setMappingFileName(String mappingFileName) {
        this.mappingFileName = mappingFileName;
    }

    /**
     * read properties to mappingFileName TableMapping Object
     * will map the source table name , and the target table name,
     * and any mapped column when you want
     * @return TableMapping
     * @throws IOException when the mappingFileName file read has something wrong.
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

        LOGGER.info("table mappingFileName ended");
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(" : " + mapping);
        }
        return mapping;
    }

    /**
     * load properties file to the Properties Object
     * @return Properties
     * @throws IOException when the mappingFileName file read has something wrong.
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
     * transfer mappingPackage to physical real file path
     * @return physical real file path
     */
    private String resolveBasePackage() {
        final String systemPath = System.getProperty("user.dir");
        final String basePath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(mappingPackage));
        final String srcPath = "/src/main/java/";
        final String fileName = mappingFileName.startsWith("/") ? mappingFileName : String.format("/%s",mappingFileName);
        return String.format("%s%s%s%s",systemPath,srcPath,basePath, fileName);
    }
}
