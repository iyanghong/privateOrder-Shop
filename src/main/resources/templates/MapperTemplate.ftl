package ${mapperPackageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ${entityPackageName}.${upperCamelCaseName};


/**
* ${tableComment}Mapper
*
* @Author ${author}
* @Date ${date}
*/
@Mapper
public interface ${upperCamelCaseName}Mapper extends BaseMapper<${upperCamelCaseName}> {

}
