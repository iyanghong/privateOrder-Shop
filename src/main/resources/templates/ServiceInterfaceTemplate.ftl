package ${servicePackageName};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import ${entityPackageName}.${upperCamelCaseName};

/**
* ${commentOrUpperCamelCaseName}服务接口
*
* @Author ${author}
* @Date ${date}
*/
public interface ${upperCamelCaseName}Service {

/**
* 分页查询列表
*
* @param pageNumber 页码
* @param pageSize   每页记录数
<#list allowSearchColumns as column>
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
</#list>
* @return Page<${upperCamelCaseName}>
*/
Page<${upperCamelCaseName}> selectPage(Integer pageNumber, Integer pageSize<#list allowSearchColumns as column>,${column.javaType} ${column.lowerCamelCaseName}</#list>);
<#if primaryKeyColumn??>

    /**
    * 根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}
    *
    * @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.columnComment}
    * @return ${upperCamelCaseName} ${primaryKeyColumn.commentOrName}信息
    */
    ${upperCamelCaseName} selectById(${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName});
</#if>
<#list getByUniqueColumns as column>

    /**
    * 根据${column.columnComment}获取信息
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @return ${upperCamelCaseName} ${commentOrName}信息
    */
    ${upperCamelCaseName} selectBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName});
</#list>
<#list getListByForeignKeyList as column>

    /**
    * 根据${column.commentOrName}获取列表
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @return List<${upperCamelCaseName}> ${commentOrName}列表
    */
    List<${upperCamelCaseName}> selectListBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName});
</#list>

/**
* 新建${commentOrName}
*
* @param ${lowerCamelCaseName} ${commentOrName}实体信息
* @param onlineUser   当前登录用户
* @return ${upperCamelCaseName} 新建后的${commentOrName}信息
*/
${upperCamelCaseName} create(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser);

/**
* 修改${commentOrName}
*
* @param ${lowerCamelCaseName} ${commentOrName}实体信息
* @param onlineUser   当前登录用户
* @return ${upperCamelCaseName} 修改后的${commentOrName}信息
*/
${upperCamelCaseName} update(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser);

/**
* 保存${commentOrName}
*
* @param ${lowerCamelCaseName} ${commentOrName}实体信息
* @param onlineUser 当前登录用户
* @return ${upperCamelCaseName} 保存后的${commentOrName}信息
*/
${upperCamelCaseName} save(${upperCamelCaseName} ${lowerCamelCaseName}, OnlineUser onlineUser);
<#if primaryKeyColumn??>

    /**
    * 根据${primaryKeyColumn.commentOrName}删除信息
    *
    * @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.columnComment}
    * @param onlineUser 当前登录用户
    */
    void delete(${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}, OnlineUser onlineUser);

    /**
    * 根据${primaryKeyColumn.commentOrName}列表删除信息
    *
    * @param ids        ${primaryKeyColumn.commentOrName}列表
    * @param onlineUser 当前登录用户
    */
    void deleteBatchIds(List<${primaryKeyColumn.javaType}> ids, OnlineUser onlineUser);
</#if>
<#list getListByForeignKeyList as column>
    /**
    * 根据${column.commentOrName}删除
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @param onlineUser 当前登录用户
    */
    void deleteBy${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}, OnlineUser onlineUser);
</#list>

}
