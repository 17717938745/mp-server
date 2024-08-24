package com.lead.fund.base.server.mp.config;

import static com.lead.fund.base.common.basic.cons.BasicConst.STRING_PACKAGE_PREFIX;
import static com.lead.fund.base.common.basic.cons.BasicConst.VARIABLE_LOOK_AT_ME;

import com.github.jeffreyning.mybatisplus.base.MppSqlInjector;
import com.lead.fund.base.common.database.config.DatabaseConfigModel;
import com.lead.fund.base.common.database.config.DatabaseProperties;
import com.lead.fund.base.server.mp.config.DousonDatabaseConfig.DousonDatabaseCondition;
import com.lead.fund.base.server.mp.config.DousonDatabaseConfig.DousonDatabaseProperties;
import java.io.Serializable;
import javax.sql.DataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * DousonDatabaseConfig
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-24 11:20
 */
@Configuration
@Slf4j
@MapperScan(
        basePackages = {
                "com.lead.fund.base.*.mapper.douson",
                "com.lead.fund.base.*.*.mapper.douson",
                "com.lead.*.mapper.douson",
                "com.lead.*.*.mapper.douson",
        },
        sqlSessionTemplateRef = "dousonSqlSessionTemplate"
)
@Conditional(DousonDatabaseCondition.class)
@ConditionalOnClass(MppSqlInjector.class)
@EnableConfigurationProperties(DousonDatabaseProperties.class)
public class DousonDatabaseConfig implements Serializable, Ordered {

    private static final long serialVersionUID = 7595009611299243253L;
    private static final String ID = "douson";
    private static final String PROPERTIES_PREFIX = STRING_PACKAGE_PREFIX + ".database." + ID;

    private final DatabaseConfigModel databaseConfigModel;

    public DousonDatabaseConfig(DousonDatabaseProperties databaseProperties, MppSqlInjector mppSqlInjector) {
        log.info("{} DousonDatabaseConfig.DousonDatabaseConfig", VARIABLE_LOOK_AT_ME);
        databaseProperties.setMaxCount(Math.max(16, databaseProperties.getMaxCount()));
        this.databaseConfigModel = new DatabaseConfigModel(ID, databaseProperties, mppSqlInjector);
    }

    @Bean
    public DataSourceTransactionManager dousonDataSourceTransactionManager() {
        return new DataSourceTransactionManager(databaseConfigModel.getHikariDataSource());
    }

    @Bean(name = "dousonDataSource")
    public DataSource dousonDataSource() {
        return databaseConfigModel.getHikariDataSource();
    }

    @Bean(name = "dousonSqlSessionFactory")
    public SqlSessionFactory dousonSqlSessionFactory() {
        return databaseConfigModel.getSqlSessionFactory();
    }

    @Bean(name = "dousonSqlSessionTemplate")
    public SqlSessionTemplate dousonSqlSessionTemplate() {
        return databaseConfigModel.getSqlSessionTemplate();
    }

    @Override
    public int getOrder() {
        return -512;
    }

    @EqualsAndHashCode(callSuper = true)
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    @Data
    public static class DousonDatabaseProperties extends DatabaseProperties {

        private static final long serialVersionUID = -453246680201452533L;
    }

    public static class DousonDatabaseCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String property = context.getEnvironment().getProperty(PROPERTIES_PREFIX + ".url");
            return null != property && property.length() > 0;
        }
    }
}
