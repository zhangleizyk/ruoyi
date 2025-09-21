package com.ruoyi.system.service.impl;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class MyPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    private static final Logger log = LoggerFactory.getLogger(MyPreciseShardingAlgorithm.class);

    /**
     * 精确匹配查询（适用 in，=）
     *
     * @param tbNames
     *            数据库中所有的事实表
     * @param shardingValue
     *            分片相关信息
     * @return 返回匹配的数据源
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<String> shardingValue) {

        log.info("+++++===进入MyPreciseShardingAlgorithm===+++++");

        String timeStr = shardingValue.getValue();
        String year = timeStr.substring(2, 4);
        String month = timeStr.substring(5, 7);
        String tableSuffix = year + month;
        for (String table : tbNames) {
            if (table.contains(tableSuffix)){
                log.info("MyPreciseShardingAlgorithm返回tableName：{}", table);
                return table;
            }
        }
        return null;
    }
}
