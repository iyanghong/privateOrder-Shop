package ${controllerPackageName};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import ${entityPackageName}.${upperCamelCaseName};
import ${servicePackageName}.${upperCamelCaseName}Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* ${commentOrUpperCamelCaseName}接口
*
* @Author ${author}
* @Date ${date}
*/
@RestController
@Validated
@RequestMapping("/${lowerCamelCaseName}")
@AuthGroup(value = "${appName}.${lowerCamelCaseName}", name = "${commentOrUpperCamelCaseName}模块", description = "${commentOrUpperCamelCaseName}模块权限组")
public class ${upperCamelCaseName}Controller {

@Resource
private ${upperCamelCaseName}Service ${lowerCamelCaseName}Service;


/**
* 分页查询${commentOrUpperCamelCaseName}列表
*
* @param pageNumber 页码
* @param pageSize   每页记录数
<#list allowSearchColumns as column>
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
</#list>
* @return 当前页数据
*/
@GetMapping("/page/{pageNumber}/{pageSize}")
@Auth(value = "${appName}.${lowerCamelCaseName}.page", name = "${commentOrUpperCamelCaseName}分页", description = "${commentOrUpperCamelCaseName}分页接口")
public Result
<Page<${upperCamelCaseName}>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize<#list allowSearchColumns as column>,${column.javaType} ${column.lowerCamelCaseName}</#list>) {
return new Result<>(${lowerCamelCaseName}Service.selectPage(pageNumber, pageSize<#list allowSearchColumns as column>, ${column.lowerCamelCaseName}</#list>), "分页数据查询成功");
}
<#list getListByForeignKeyList as column>
    /**
    * 根据${column.commentOrName}获取列表
    *
    * @param ${column.lowerCamelCaseName} ${column.columnComment}
    * @return List<${upperCamelCaseName}> ${commentOrUpperCamelCaseName}列表
    */
    @GetMapping("/listBy${column.upperCamelCaseName}/{${column.lowerCamelCaseName}}")
    @Auth(value = "${appName}.${lowerCamelCaseName}.listBy${column.upperCamelCaseName}", name = "根据${column.commentOrName}获取${commentOrUpperCamelCaseName}列表", description = "根据${column.commentOrName}获取${commentOrUpperCamelCaseName}列表接口")
    public Result
    <List<${upperCamelCaseName}>> selectListBy${column.upperCamelCaseName}(@PathVariable("${column.lowerCamelCaseName}") ${column.javaType} ${column.lowerCamelCaseName}) {
    return new Result<>(${lowerCamelCaseName}Service.selectListBy${column.upperCamelCaseName}(${column.lowerCamelCaseName}), "查询成功");
    }
</#list>
<#list getByUniqueColumns as column>

    /**
    * 根据${column.commentOrName}邀请码获取${commentOrUpperCamelCaseName}信息
    *
    * @param ${column.lowerCamelCaseName} ${column.commentOrName}
    * @return ${commentOrUpperCamelCaseName}信息
    */
    @GetMapping("/${column.lowerCamelCaseName}/{${column.lowerCamelCaseName}}")
    @Auth(value = "clever-system.platform.selectBy${column.upperCamelCaseName}", name = "根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}信息", description = "根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}信息接口")
    public Result<${upperCamelCaseName}> selectBy${column.upperCamelCaseName}(@PathVariable("${column.lowerCamelCaseName}") ${column.javaType} ${column.lowerCamelCaseName}) {
    return new Result<>(${lowerCamelCaseName}Service.selectBy${column.upperCamelCaseName}(${column.lowerCamelCaseName}), "查询成功");
    }
</#list>
<#if primaryKeyColumn??>

    /**
    * 根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}信息
    *
    * @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.commentOrName}
    * @return ${commentOrUpperCamelCaseName}信息
    */
    @GetMapping("/{${primaryKeyColumn.lowerCamelCaseName}}")
    @Auth(value = "clever-system.${lowerCamelCaseName}.selectById", name = "根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}信息", description = "根据${primaryKeyColumn.commentOrName}获取${commentOrUpperCamelCaseName}信息接口")
    public Result<${upperCamelCaseName}> selectById(@PathVariable("${primaryKeyColumn.lowerCamelCaseName}") ${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}) {
    return new Result<>(${lowerCamelCaseName}Service.selectById(${primaryKeyColumn.lowerCamelCaseName}), "查询成功");
    }
</#if>
/**
* 创建${commentOrUpperCamelCaseName}信息
*
* @param ${lowerCamelCaseName} ${commentOrUpperCamelCaseName}实体信息
* @return 创建后的${commentOrName}信息
*/
@PostMapping("")
@Auth(value = "${appName}.${lowerCamelCaseName}.create", name = "创建${commentOrUpperCamelCaseName}", description = "创建${commentOrUpperCamelCaseName}信息接口")
public Result<${upperCamelCaseName}> create(@Validated ${upperCamelCaseName} ${lowerCamelCaseName}) {
OnlineUser onlineUser = SpringUtil.getOnlineUser();
return new Result<>(${lowerCamelCaseName}Service.create(${lowerCamelCaseName}, onlineUser), "创建成功");
}
<#if primaryKeyColumn??>
/**
* 修改${commentOrUpperCamelCaseName}信息
*
* @param ${lowerCamelCaseName} ${commentOrUpperCamelCaseName}实体信息
* @return 修改后的${commentOrName}信息
*/
@PatchMapping("/{${primaryKeyColumn.lowerCamelCaseName}}")
@Auth(value = "${appName}.${lowerCamelCaseName}.update", name = "修改${commentOrUpperCamelCaseName}", description = "修改${commentOrUpperCamelCaseName}信息接口")
public Result<${upperCamelCaseName}> update(@Validated ${upperCamelCaseName} ${lowerCamelCaseName}, @PathVariable("${primaryKeyColumn.lowerCamelCaseName}") ${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}) {
OnlineUser onlineUser = SpringUtil.getOnlineUser();
${lowerCamelCaseName}.set${primaryKeyColumn.upperCamelCaseName}(${primaryKeyColumn.lowerCamelCaseName});
return new Result<>(${lowerCamelCaseName}Service.update(${lowerCamelCaseName}, onlineUser), "修改成功");
}

/**
* 保存${commentOrUpperCamelCaseName}信息
*
* @param ${lowerCamelCaseName} ${commentOrUpperCamelCaseName}实体信息
* @return 保存后的${commentOrName}信息
*/
@PostMapping("/save")
@Auth(value = "${appName}.${lowerCamelCaseName}.save", name = "保存${commentOrUpperCamelCaseName}", description = "保存${commentOrUpperCamelCaseName}信息接口")
public Result<${upperCamelCaseName}> save(@Validated ${upperCamelCaseName} ${lowerCamelCaseName}) {
OnlineUser onlineUser = SpringUtil.getOnlineUser();
return new Result<>(${lowerCamelCaseName}Service.save(${lowerCamelCaseName}, onlineUser), "保存成功");
}

/**
* 根据${commentOrUpperCamelCaseName}id删除${commentOrUpperCamelCaseName}信息
*
* @param ${primaryKeyColumn.lowerCamelCaseName} ${primaryKeyColumn.commentOrName}
*/
@DeleteMapping("/{${primaryKeyColumn.lowerCamelCaseName}}")
@Auth(value = "${appName}.${lowerCamelCaseName}.delete", name = "删除${commentOrUpperCamelCaseName}", description = "删除${commentOrUpperCamelCaseName}信息接口")
public Result
<String> delete(@PathVariable("${primaryKeyColumn.lowerCamelCaseName}
    ") ${primaryKeyColumn.javaType} ${primaryKeyColumn.lowerCamelCaseName}) {
    OnlineUser onlineUser = SpringUtil.getOnlineUser();
    ${lowerCamelCaseName}Service.delete(${primaryKeyColumn.lowerCamelCaseName}, onlineUser);
    return Result.ofSuccess("删除成功");
    }
    </#if>
    }
