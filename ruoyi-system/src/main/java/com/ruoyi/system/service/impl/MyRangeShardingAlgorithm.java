package com.ruoyi.system.service.impl;

import com.google.common.collect.Range;
import com.ruoyi.common.utils.DateUtils;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;


public class MyRangeShardingAlgorithm implements RangeShardingAlgorithm<String> {

    private static final Logger log = LoggerFactory.getLogger(MyRangeShardingAlgorithm.class);

    /**
     *
     * 范围查询（适用 >, =, <, between and）
     *
     * @param tbNames
     *            数据库中所有的事实表
     * @param rangeShardingValue
     *            分片相关信息
     * @return 返回匹配的数据源
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<String> rangeShardingValue) {
        log.info("+++++===进入MyRangeShardingAlgorithm===+++++");
        Range<String> valueRange = rangeShardingValue.getValueRange();
        String upper = valueRange.upperEndpoint(); // 范围值的上限
        String lower = valueRange.lowerEndpoint(); // 范围值的下限

        // 日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(upper,dateTimeFormatter);
        LocalDateTime upperEndpoint = localDate1.atStartOfDay();

        LocalDate localDate2 = LocalDate.parse(lower,dateTimeFormatter);
        LocalDateTime lowerEndpoint = localDate2.atStartOfDay();

        // 逻辑表名
        String logicTableName = rangeShardingValue.getLogicTableName();
        List<String> tables = new ArrayList<>();

//        if (Duration.between(lowerEndpoint, upperEndpoint).toDays() > 365) {
//            throw new RuntimeException("不能查询大于一年的数据");
//        }

        while (lowerEndpoint.compareTo(upperEndpoint) <= 0) {
            // 添加到集合
            tables.add(buildTable(logicTableName, lowerEndpoint));
            // 往后加一个月
            lowerEndpoint = lowerEndpoint.plusMonths(1);
        }
        log.info("MyRangeShardingAlgorithm返回tableName：{}", tables.toString());
        return tables;
    }

    private String buildTable(String logicTableName, LocalDateTime startTime) {
        String tableName = "";
        String timeStr = DateUtils.dateTime(DateUtils.toDate(startTime));
        String year = timeStr.substring(2, 4);
        String month = timeStr.substring(5, 7);
        tableName = logicTableName + "_" + year + month;
        return tableName;
    }
}
