package com.ruoyi.web.controller.manager;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.manager.service.ManagerService;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/manager/user")
public class ManagerController extends BaseController
{
    @Autowired
    private ManagerService managerService;

    @GetMapping("/list")
    public AjaxResult list()
    {
        Map<String, String> map = managerService.selectManagerInfo();
        return success(map);
    }

}