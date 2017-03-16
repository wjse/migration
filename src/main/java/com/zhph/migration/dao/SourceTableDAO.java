package com.zhph.migration.dao;

import org.oproject.framework.orm.PageResult;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */
@DynamicIbatisDAO(value = "SourceTableDAO" , sqlMapClientTemplate = "sqlMapClientTemplateSource")
public interface SourceTableDAO {

    List<Map<String,Object>> queryForList(Map<String,Object> map);

    PageResult<Map<String,Object>> queryForPageResult(Map<String,Object> map , int pageNum , int pageSize);
}
