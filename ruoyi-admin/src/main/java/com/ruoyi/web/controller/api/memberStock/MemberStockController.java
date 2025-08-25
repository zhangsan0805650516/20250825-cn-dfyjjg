package com.ruoyi.web.controller.api.memberStock;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.memberStock.domain.FaMemberStock;
import com.ruoyi.biz.memberStock.service.IFaMemberStockService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.domain.StrategyString;
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
 * 自选股票Controller
 * 
 * @author ruoyi
 * @date 2024-01-19
 */
@Api(tags = "自选股票")
@RestController
@RequestMapping("/api/memberStock")
public class MemberStockController extends BaseController
{
    @Autowired
    private IFaMemberStockService faMemberStockService;

    /**
     * 查询自选股票
     */
    @ApiOperation("查询自选股票")
    @AppLog(title = "查询自选股票", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")
    })
    @PostMapping("/getMemberStock")
    public AjaxResult getMemberStock(@RequestBody FaMemberStock faMemberStock)
    {
        try {
            if (null == faMemberStock.getPage()) {
                faMemberStock.setPage(1);
            }
            if (null == faMemberStock.getSize()) {
                faMemberStock.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faMemberStock.setMemberId(loginMember.getFaMember().getId());
            IPage<FaStrategy> faStrategyIPage = faMemberStockService.getMemberStock(faMemberStock);

            faStrategyIPage.convert(u ->{
                StrategyString strategyString = new StrategyString();
                strategyString = JSON.parseObject(JSON.toJSONString(u), StrategyString.class);
                return strategyString;
            });

            return AjaxResult.success(faStrategyIPage);
        } catch (Exception e) {
            logger.error("getMemberStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询股票是否自选
     */
    @ApiOperation("查询股票是否自选(参数三选一)")
    @AppLog(title = "查询股票是否自选", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "股票id", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "股票代码", dataType = "String"),
            @ApiImplicitParam(name = "allCode", value = "股票全代码", dataType = "String")})
    @PostMapping("/checkMemberStock")
    public AjaxResult checkMemberStock(@RequestBody FaStrategy faStrategy)
    {
        try {
            LoginMember loginMember = getLoginMember();
            int exist = faMemberStockService.checkMemberStock(faStrategy, loginMember.getFaMember());
            return AjaxResult.success(exist);
        } catch (ServiceException e) {
            logger.error("checkMemberStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("checkMemberStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 新增自选股票
     */
    @ApiOperation("新增自选股票(参数三选一)")
    @AppLog(title = "新增自选股票", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "股票id", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "股票代码", dataType = "String"),
            @ApiImplicitParam(name = "allCode", value = "股票全代码", dataType = "String")})
    @PostMapping("/addMemberStock")
    public AjaxResult addMemberStock(@RequestBody FaStrategy faStrategy)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMemberStockService.addMemberStock(faStrategy, loginMember.getFaMember());
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("addMemberStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addMemberStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 删除自选股票
     */
    @ApiOperation("删除自选股票(参数三选一)")
    @AppLog(title = "删除自选股票", businessType = BusinessType.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "股票id", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "股票代码", dataType = "String"),
            @ApiImplicitParam(name = "allCode", value = "股票全代码", dataType = "String")})
    @PostMapping("/deleteMemberStock")
    public AjaxResult deleteMemberStock(@RequestBody FaStrategy faStrategy)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMemberStockService.deleteMemberStock(faStrategy, loginMember.getFaMember());
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("deleteMemberStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("deleteMemberStock", e);
            return AjaxResult.error();
        }
    }

}
