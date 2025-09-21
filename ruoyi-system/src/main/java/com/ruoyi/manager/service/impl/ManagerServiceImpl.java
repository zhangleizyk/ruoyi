package com.ruoyi.manager.service.impl;

import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.manager.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService {

    private static final Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Map<String, String> selectManagerInfo() {
        Map<String, String> map = new HashMap<String, String>();
//        for(int i=0; i< 5; i++){
//            redisUtil.lSetObj("students", i);
//        }

        List<Object> list = redisUtil.lGet("students", 0, -1);
        try {
//            Object obj = redisUtil.lPop("students");
//            log.info("After leftPop, key: {}, result: {}", "students", obj);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }

        return map;
    }
}
