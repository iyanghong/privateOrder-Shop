package ${entityPackageName};

import java.io.Serializable;

<#if primaryKeyColumn??>
    <#if primaryKeyColumn.javaType == "String" || primaryKeyColumn.javaType == "Integer">
import com.baomidou.mybatisplus.annotation.IdType;
    </#if>
import com.baomidou.mybatisplus.annotation.TableId;
</#if>
<#if isHasAutoInsertColumn || isHasAutoUpdateColumn>
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
</#if>
<#if isHasNeedNotBlankValidate>
import javax.validation.constraints.NotBlank;
</#if>
<#if isHasNeedNotNullValidate>
import javax.validation.constraints.NotNull;
</#if>

<#if isHasDateTypeColumn>
import java.util.Date;
</#if>

/**
 * ${tableComment}
 *
 * @Author ${author}
 * @Date ${date}
 */
public class ${upperCamelCaseName} implements Serializable {

    <#list columns as column>
    <#if column.columnComment != "">
    /**
     * ${column.columnComment}
     */
    </#if>
    <#if !column.isAutoInsertFill && !column.isAutoUpdateFill && column.isHasNeedNotBlankValidate && column.javaType == "String">
    @NotBlank(message = "<#if column.nameText == "">${column.commentOrName}<#else>${column.nameText}</#if>不能为空")
    <#elseif !column.isAutoInsertFill && !column.isAutoUpdateFill && column.isHasNeedNotBlankValidate && column.javaType != "String">
    @NotNull(message = "<#if column.nameText == "">${column.commentOrName}<#else>${column.nameText}</#if>不能为空")
    </#if>
    <#if column.columnKey == "PRI">
        <#if column.javaType == "String">
    @TableId(type = IdType.ASSIGN_ID)
        <#elseif column.javaType == "Integer">
    @TableId(type = IdType.AUTO)
        <#else>
    @TableId
        </#if>
    </#if>
    <#if column.isAutoInsertFill>
    @TableField(value = "${column.columnName}", fill = FieldFill.INSERT)
    <#elseif column.isAutoUpdateFill>
    @TableField(value = "${column.columnName}", fill = FieldFill.UPDATE)
    </#if>
    private ${column.javaType} ${column.lowerCamelCaseName};
    </#list>



    <#list columns as column>
    <#if column.columnComment != "">
    /**
     * ${column.columnComment}
     */
    </#if>
    public ${column.javaType} get${column.upperCamelCaseName}() {
        return ${column.lowerCamelCaseName};
    }
    <#if column.columnName == "enable" && column.javaType == "Integer">

    <#if column.columnComment != "">
    /**
     * ${column.columnComment}
     */
    </#if>
    public boolean ifEnable() {
    	return enable == 1;
    }
    </#if>

    public void set${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}) {
        this.${column.lowerCamelCaseName} = ${column.lowerCamelCaseName};
    }
    </#list>
}