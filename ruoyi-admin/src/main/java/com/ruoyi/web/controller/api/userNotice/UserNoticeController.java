package com.ruoyi.web.controller.api.userNotice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.userNotice.domain.FaUserNotice;
import com.ruoyi.biz.userNotice.service.IFaUserNoticeService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户消息Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户消息")
@RestController
@RequestMapping("/api/userNotice")
public class UserNoticeController extends BaseController
{
    @Autowired
    private IFaUserNoticeService faUserNoticeService;

    /**
     * 查询用户消息列表
     */
    @ApiOperation("查询用户消息列表")
    @AppLog(title = "查询用户消息列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")})
    @PostMapping("/getUserNotice")
    public AjaxResult getUserNotice(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            if (null == faUserNotice.getPage()) {
                faUserNotice.setPage(1);
            }
            if (null == faUserNotice.getSize()) {
                faUserNotice.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faUserNotice.setUserId(loginMember.getFaMember().getId());
            IPage<FaUserNotice> faUserNoticeIPage = faUserNoticeService.getMemberSgPage(faUserNotice);
            return AjaxResult.success(faUserNoticeIPage);
        } catch (ServiceException e) {
            logger.error("getUserNotice", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getUserNotice", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户消息详情
     */
    @ApiOperation("用户消息详情")
    @AppLog(title = "用户消息详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息id", required = true, dataType = "Integer")})
    @PostMapping("/userNoticeDetail")
    public AjaxResult userNoticeDetail(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            faUserNotice = faUserNoticeService.userNoticeDetail(faUserNotice);
            return AjaxResult.success(faUserNotice);
        } catch (ServiceException e) {
            logger.error("userNoticeDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("userNoticeDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 阅读用户消息(单条)
     */
    @ApiOperation("阅读用户消息(单条)")
    @AppLog(title = "阅读用户消息(单条)", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息id", required = true, dataType = "Integer")})
    @PostMapping("/readUserNotice")
    public AjaxResult readUserNotice(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            faUserNoticeService.readUserNotice(faUserNotice);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("readUserNotice", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("readUserNotice", e);
            return AjaxResult.error();
        }
    }

    /**
     * 阅读用户消息(所有)
     */
    @ApiOperation("阅读用户消息(所有)")
    @AppLog(title = "阅读用户消息(所有)", businessType = BusinessType.OTHER)
    @PostMapping("/readUserNoticeAll")
    public AjaxResult readUserNoticeAll(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faUserNotice.setUserId(loginMember.getFaMember().getId());
            faUserNoticeService.readUserNoticeAll(faUserNotice);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("readUserNoticeAll", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("readUserNoticeAll", e);
            return AjaxResult.error();
        }
    }

    /**
     * 未读消息条数
     */
    @ApiOperation("未读消息条数")
    @AppLog(title = "未读消息条数", businessType = BusinessType.OTHER)
    @PostMapping("/unreadNoticeNum")
    public AjaxResult unreadNoticeNum(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faUserNotice.setUserId(loginMember.getFaMember().getId());
            long count = faUserNoticeService.unreadNoticeNum(faUserNotice);
            return AjaxResult.success(count);
        } catch (ServiceException e) {
            logger.error("unreadNoticeNum", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("unreadNoticeNum", e);
            return AjaxResult.error();
        }
    }

}
