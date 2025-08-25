package com.ruoyi.web.controller.api.sysNotice;

import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户消息Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户消息")
@RestController
@RequestMapping("/api/sysNotice")
public class SystemNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService iSysNoticeService;

    /**
     * 查询系统公告列表
     */
    @ApiOperation("查询系统公告列表")
    @AppLog(title = "查询系统公告列表", businessType = BusinessType.OTHER)
    @PostMapping("/getSystemNotice")
    public AjaxResult getSystemNotice(@RequestBody SysNotice sysNotice)
    {
        try {
            LoginMember loginMember = getLoginMember();
            sysNotice.setMemberId(loginMember.getFaMember().getId());
            sysNotice.setNoticeType("2");
            List<SysNotice> sysNoticeList = iSysNoticeService.getSystemNotice(sysNotice);
            return AjaxResult.success(sysNoticeList);
        } catch (ServiceException e) {
            logger.error("getSystemNotice", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSystemNotice", e);
            return AjaxResult.error();
        }
    }

    /**
     * 阅读公告
     */
    @ApiOperation("阅读公告")
    @AppLog(title = "阅读公告", businessType = BusinessType.INSERT)
    @PostMapping("/readNotice")
    public AjaxResult readNotice(@RequestBody SysNotice sysNotice)
    {
        try {
            LoginMember loginMember = getLoginMember();
            sysNotice.setMemberId(loginMember.getFaMember().getId());
            iSysNoticeService.readNotice(sysNotice);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("readNotice", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("readNotice", e);
            return AjaxResult.error();
        }
    }

}
