# 安装

## Eclipse

mvn eclipse:eclipse

## IntelliJ idea

mvn idea:idea

# 构建

mvn clean install

# 数据源

源数据库数据源与目标数据库数据源均配置在 src/main/resources/jdbc.properties 中

source.* 为源数据库数据源配置

target.* 为目标数据库数据源配置

其他为数据源共用配置

# 映射配置文件位置

src/main/java/com/zhph/migration/mapping/table_mapping.properties

## 映射配置属性

### 源表表名键,对应的值为源表所在数据库中的表名

source.table.name='tableName'

### 目标表表名键,对应的值为目标表所在数据库中的表名

target.table.name='tableName'

### 字段映射,源表中的字段名=目标表中的字段名

sourceTableColumnName = targetTableColumnName

### 例子

source.table.name=CRF_P2P_APP_INFO

target.table.name=ZHPH_CLIENT_INFO

PRI_NUMBER=CLIENT_ID

ID_CARD=ID_CARD

LOAN_NAME=CLIENT_NAME

LOAN_NAME_PHONE=CLIENT_TELE

GENDER=CLIENT_SEX

DATE_OF_BIRTH=CLIENT_BIRTH

ID_ADDR_ADDR=CLIENT_ADDR

# 使用

## 运行maven命令

mvn test -Pmigrate -Dexec.args="true 1 10"

## 参数说明
"true 1 10"
true 是否使用分页查询,非true值会设置为全表查询
1 第几页
10 每页查询条数

## 默认不带参数
mvn test -Pmigrate

migration将默认使用分页查询第一页,每页20条数据

# TODO

后期计划增加动态设置映射关系,以及动态设置数据源配置




