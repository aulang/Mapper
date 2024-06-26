package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class BatchUpdateProvider extends MapperTemplate {

    public BatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpdate(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        return "<foreach collection=\"list\" item=\"record\" separator=\";\" >" +
                SqlHelper.updateTable(entityClass, tableName(entityClass)) +
                SqlHelper.updateSetColumns(entityClass, "record", false, false) +
                SqlHelper.wherePKColumns(entityClass, "record", true) +
                "</foreach>";
    }

    public String batchUpdateSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        return "<foreach collection=\"list\" item=\"record\" separator=\";\" >" +
                SqlHelper.updateTable(entityClass, tableName(entityClass)) +
                SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()) +
                SqlHelper.wherePKColumns(entityClass, "record", true) +
                "</foreach>";
    }
}
