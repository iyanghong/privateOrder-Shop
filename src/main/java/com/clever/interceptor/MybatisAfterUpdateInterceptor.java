package com.clever.interceptor;

import com.clever.annotation.AfterUpdate;
import com.clever.annotation.AfterUpdateHandler;
import com.clever.util.MybatisUtil;
import com.clever.util.SpringUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;

/**
 * @Author xixi
 * @Date 2023-12-27 08:48
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MybatisAfterUpdateInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //获取mapper对象
        String statementId = mappedStatement.getId();
        Class<?> mapperClass = Class.forName(statementId.substring(0, statementId.lastIndexOf(".")));
        AfterUpdate annotation = mapperClass.getAnnotation(AfterUpdate.class);
        //是否需要拦截修改方法
        if (annotation == null) {
            return invocation.proceed();
        }
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        AfterUpdateHandler updateHandler = SpringUtil.getBean(annotation.value());
        //执行sql
        Object returnValue = invocation.proceed();
        int updateCount = Integer.parseInt(returnValue.toString());
        if (updateCount > 0) {
            //解析sql
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();
            String sql = MybatisUtil.parseCompleteSql(configuration, boundSql);
            //当修改条数大于0时才有意义,去触发更新
            updateHandler.handle(sql);
        }
        return returnValue;
    }
}
