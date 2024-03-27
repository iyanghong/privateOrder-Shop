package ${servicePackageName}.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import ${mapperPackageName}.${upperCamelCaseName}Mapper;
import ${entityPackageName}.${upperCamelCaseName};
import ${servicePackageName}.${upperCamelCaseName}Service;

import javax.annotation.Resource;

/**
* ${commentOrUpperCamelCaseName}服务
*
* @Author ${author}
* @Date ${date}
*/
@Service
public class ${upperCamelCaseName}ServiceImpl implements ${upperCamelCaseName}Service {

private final static Logger log = LoggerFactory.getLogger(${upperCamelCaseName}ServiceImpl.class);

@Resource
private ${upperCamelCaseName}Mapper ${lowerCamelCaseName}Mapper;

/**
* 分页查询${commentOrUpperCamelCaseName}列表
*
* @param pageNumber 页码
* @param pageSize   每页记录数
<#list allowSearchColumns as column>
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
</#list>
* @return Page<${upperCamelCaseName}>
*/
@Override
public Page<${upperCamelCaseName}> selectPage(Integer pageNumber, Integer pageSize<#list allowSearchColumns as column>,${column.javaType} ${column.lowerCamelCaseName}</#list>) {
QueryWrapper<${upperCamelCaseName}> queryWrapper = new QueryWrapper<>();
<#list allowSearchColumns as column>
    <#if column.javaType == "String">
        if (StringUtils.isNotBlank(${column.lowerCamelCaseName})) {
    <#else>
        if (${column.lowerCamelCaseName} != null) {
    </#if>
    queryWrapper.eq("${column.columnName}", ${column.lowerCamelCaseName});
    }
</#list>
return ${lowerCamelCaseName}Mapper.selectPage(new Page<${upperCamelCaseName}>(pageNumber, pageSize), queryWrapper);
}
<#if primaryKeyColumn??>
    /**
    * 根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}
    *
    * @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.columnComment}
    * @return ${upperCamelCaseName} ${commentOrUpperCamelCaseName}信息
    */
    @Override
    public ${upperCamelCaseName} selectById(${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}) {
    return ${lowerCamelCaseName}Mapper.selectById(${primaryKeyColumn.lowerCamelCaseName});
    }
</#if>
<#list getByUniqueColumns as column>

    /**
    * 根据${column.columnComment}获取信息
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @return ${upperCamelCaseName} ${commentOrName}信息
    */
    @Override
    public ${upperCamelCaseName} selectBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}) {
    return ${lowerCamelCaseName}Mapper.selectOne(new QueryWrapper<${upperCamelCaseName}>().eq("${column.columnName}",${column.lowerCamelCaseName}));
    }
</#list>
<#list getListByForeignKeyList as column>
    /**
    * 根据${column.commentOrName}获取列表
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @return List<${upperCamelCaseName}> ${commentOrUpperCamelCaseName}列表
    */
    @Override
    public List<${upperCamelCaseName}> selectListBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}) {
    return ${lowerCamelCaseName}Mapper.selectList(new QueryWrapper<${upperCamelCaseName}>().eq("${column.columnName}", ${column.lowerCamelCaseName})<#if primaryKeyColumn??>.orderByAsc("${primaryKeyColumn.columnName}")</#if>);
    }
</#list>
/**
* 新建${commentOrName}
*
* @param ${lowerCamelCaseName} ${commentOrName}实体信息
* @param onlineUser   当前登录用户
* @return ${upperCamelCaseName} 新建后的${commentOrName}信息
*/
@Override
public ${upperCamelCaseName} create(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser) {
${lowerCamelCaseName}Mapper.insert(${lowerCamelCaseName});
log.info("${commentOrUpperCamelCaseName}, ${commentOrUpperCamelCaseName}信息创建成功: userId={}, ${lowerCamelCaseName}Id={}", onlineUser.getId(), ${lowerCamelCaseName}.getId());
return ${lowerCamelCaseName};
}

/**
* 修改${commentOrName}
*
* @param ${lowerCamelCaseName} ${commentOrName}实体信息
* @param onlineUser   当前登录用户
* @return ${upperCamelCaseName} 修改后的${commentOrName}信息
*/
@Override
public ${upperCamelCaseName} update(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser) {
${lowerCamelCaseName}Mapper.updateById(${lowerCamelCaseName});
log.info("${commentOrUpperCamelCaseName}, ${commentOrUpperCamelCaseName}信息修改成功: userId={}, ${lowerCamelCaseName}Id={}", onlineUser.getId(), ${lowerCamelCaseName}.getId());
return ${lowerCamelCaseName};
}
<#if primaryKeyColumn??>

    /**
    * 保存${commentOrName}
    *
    * @param ${lowerCamelCaseName} ${commentOrName}实体信息
    * @param onlineUser 当前登录用户
    * @return ${upperCamelCaseName} 保存后的${commentOrName}信息
    */
    @Override
    public ${upperCamelCaseName} save(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser) {
    <#if primaryKeyColumn.javaType == "String">
        if (StringUtils.isNotBlank(${lowerCamelCaseName}.get${primaryKeyColumn.upperCamelCaseName}())) {
    <#else>
        if (${lowerCamelCaseName}.get${primaryKeyColumn.upperCamelCaseName}() != null) {
    </#if>
    return create(${lowerCamelCaseName}, onlineUser);
    }
    return update(${lowerCamelCaseName}, onlineUser);
    }

    /**
    * 根据${primaryKeyColumn.commentOrName}删除${commentOrUpperCamelCaseName}信息
    *
    * @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.columnComment}
    * @param onlineUser 当前登录用户
    */
    @Override
    public void delete(${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}, OnlineUser onlineUser) {
    ${lowerCamelCaseName}Mapper.deleteById(${primaryKeyColumn.lowerCamelCaseName});
    log.info("${commentOrUpperCamelCaseName}, ${commentOrUpperCamelCaseName}信息删除成功: userId={}, ${lowerCamelCaseName}Id={}", onlineUser.getId(), ${primaryKeyColumn.lowerCamelCaseName});
    }

    /**
    * 根据${primaryKeyColumn.commentOrName}列表删除${commentOrUpperCamelCaseName}信息
    *
    * @param ids        ${primaryKeyColumn.commentOrName}列表
    * @param onlineUser 当前登录用户
    */
    @Override
    public void deleteBatchIds(List<${primaryKeyColumn.javaType}> ids, OnlineUser onlineUser) {
    ${lowerCamelCaseName}Mapper.deleteBatchIds(ids);
    log.info("${commentOrUpperCamelCaseName}, ${commentOrUpperCamelCaseName}信息批量删除成功: userId={}, count={}, ${lowerCamelCaseName}Ids={}", onlineUser.getId(), ids.size(), ids.toString());
    }
</#if>
<#list getListByForeignKeyList as column>
    /**
    * 根据${column.commentOrName}删除
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @param onlineUser 当前登录用户
    */
    @Override
    public void deleteBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}, OnlineUser onlineUser) {
    ${lowerCamelCaseName}Mapper.delete(new QueryWrapper<${upperCamelCaseName}>().eq("${column.columnName}", ${column.lowerCamelCaseName}));
    log.info("${commentOrUpperCamelCaseName}, ${commentOrUpperCamelCaseName}信息根据${column.lowerCamelCaseName}删除成功: userId={}, ${column.lowerCamelCaseName}={}", onlineUser.getId(), ${column.lowerCamelCaseName});
    }
</#list>
}
