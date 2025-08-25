package com.ruoyi.web.controller.api.capitalLog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.fundPool.service.FundPoolService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 资金记录Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "资金记录")
@RestController
@RequestMapping("/api/capitalLog")
public class CapitalLogController extends BaseController
{
    @Autowired
    private IFaCapitalLogService faCapitalLogService;

    @Autowired
    private FundPoolService fundPoolService;

    /**
     * 查询资金记录
     */
    @ApiOperation("查询资金记录")
    @AppLog(title = "查询资金记录", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "类型(0充值 1提现 2普通下单 3手续费 4印花税)(可选)", dataType = "Integer")})
    @PostMapping("/getFundRecord")
    public AjaxResult getFundRecord(@RequestBody FaCapitalLog faCapitalLog)
    {
        try {
            if (null == faCapitalLog.getPage()) {
                faCapitalLog.setPage(1);
            }
            if (null == faCapitalLog.getSize()) {
                faCapitalLog.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faCapitalLog.setUserId(loginMember.getFaMember().getId());
            IPage<FaCapitalLog> faCapitalLogIPage = faCapitalLogService.getFundRecord(faCapitalLog);
            return AjaxResult.success(faCapitalLogIPage);
        } catch (Exception e) {
            logger.error("getFundRecord", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询资金池记录
     */
    @ApiOperation("查询资金池记录")
    @AppLog(title = "查询资金池记录", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "类型(1005资金池划入 1006划入资金池)(可选)", dataType = "Integer")})
    @PostMapping("/getFundPoolRecord")
    public AjaxResult getFundPoolRecord(@RequestBody FaCapitalLog faCapitalLog)
    {
        try {
            if (null == faCapitalLog.getPage()) {
                faCapitalLog.setPage(1);
            }

            // h5
            if (100 == faCapitalLog.getSize()) {
                faCapitalLog.setSize(30);
            }
            // PC
            else {
                faCapitalLog.setSize(10);
            }

            LoginMember loginMember = getLoginMember();
            faCapitalLog.setUserId(loginMember.getFaMember().getId());
            IPage<FaCapitalLog> faCapitalLogIPage = faCapitalLogService.getFundPoolRecord(faCapitalLog);
            if (faCapitalLogIPage.getTotal() > 30) {
                faCapitalLogIPage.setTotal(30L);
            }
            return AjaxResult.success(faCapitalLogIPage);
        } catch (Exception e) {
            logger.error("getFundPoolRecord", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询资金池余额
     */
    @ApiOperation("查询资金池余额")
    @AppLog(title = "查询资金池余额", businessType = BusinessType.OTHER)
    @PostMapping("/getFundPoolBalance")
    public AjaxResult getFundPoolBalance()
    {
        try {
            BigDecimal balance = fundPoolService.getFundPoolBalance();
            Map<String, BigDecimal> map = new HashMap<>();
            map.put("fundPoolBalance", balance);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getFundPoolBalance", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getFundPoolBalance", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户转入资金池
     */
    @RepeatSubmit
    @ApiOperation("用户转入资金池")
    @AppLog(title = "用户转入资金池", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money", value = "金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true, dataType = "String")
    })
    @PostMapping("/transferToFundPool")
    public AjaxResult transferToFundPool(@RequestBody FaMember faMember)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            fundPoolService.transferToFundPool(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("transferToFundPool", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("transferToFundPool", e);
            return AjaxResult.error();
        }
    }

}
