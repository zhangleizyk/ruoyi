package com.ruoyi.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ruoyi.framework.config.properties.DruidProperties;
import com.ruoyi.system.service.impl.MyRangeShardingAlgorithm;
import com.ruoyi.system.service.impl.MyPreciseShardingAlgorithm;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.shardsource")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.shardsource", name = "enabled", havingValue = "true")
    public DataSource shardDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource(@Qualifier("shardDataSource") DataSource shardDataSource) throws SQLException
    {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("order", shardDataSource);
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        // sys_order 表规则配置
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("sys_order", "order.sys_order_$->{2503..2505}");

        // 配置分表策略--standard算法
        orderTableRuleConfig.setTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("create_time", new MyPreciseShardingAlgorithm(), new MyRangeShardingAlgorithm()));
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, getProperties());
        return dataSource;
    }

    /**
     * 打印SQL配置
     */
    private Properties getProperties() {
        Properties shardingProperties = new Properties();
        shardingProperties.put("sql.show", true);
        return shardingProperties;
    }
}
