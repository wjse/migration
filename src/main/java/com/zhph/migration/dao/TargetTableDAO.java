package com.zhph.migration.dao;

import com.zhph.migration.po.TargetBatchInsert;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@DynamicIbatisDAO(value = "TargetTableDAO" , sqlMapClientTemplate = "sqlMapClientTemplateTarget")
public interface TargetTableDAO {

    void batchInsert(List<TargetBatchInsert> list);
}
