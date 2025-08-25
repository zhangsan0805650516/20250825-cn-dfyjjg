package com.ruoyi.web.controller.api.stockHoldDetail;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.domain.StockHoldDetailString;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
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

import java.math.BigDecimal;
import java.util.Map;

/**
 * 持仓汇总Controller
 * 
 * @author ruoyi
 * @date 2024-01-23
 */
@Api(tags = "持仓明细")
@RestController
@RequestMapping("/api/stockHoldDetail")
public class StockHoldDetailController extends BaseController
{

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    /**
     * 查询持仓列表
     */
    @ApiOperation("查询普通持仓列表")
    @AppLog(title = "查询普通持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String"),
            @ApiImplicitParam(name = "holdType", value = "持仓类型(1普通交易 2大宗交易 8融券)(可选)", dataType = "Integer")
    })
    @PostMapping("/getStockHoldDetailList")
    public AjaxResult getStockHoldDetailList(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 1普通交易 2大宗交易
            if (null != faStockHoldDetail.getHoldType()) {
                faStockHoldDetail.setHoldType(faStockHoldDetail.getHoldType());
            }
            // 0持有
            faStockHoldDetail.setStatus(0);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);

            faStockHoldDetailIPage.convert(u ->{
                StockHoldDetailString stockHoldDetailString = new StockHoldDetailString();
                stockHoldDetailString = JSON.parseObject(JSON.toJSONString(u), StockHoldDetailString.class);
                return stockHoldDetailString;
            });

            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询持仓列表
     */
    @ApiOperation("查询普通历史持仓列表")
    @AppLog(title = "查询普通历史持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String"),
            @ApiImplicitParam(name = "holdType", value = "持仓类型(1普通交易 2大宗交易)(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "String"),
    })
    @PostMapping("/getStockHoldDetailListHistory")
    public AjaxResult getStockHoldDetailListHistory(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 1普通交易 2大宗交易
            if (null != faStockHoldDetail.getHoldType()) {
                faStockHoldDetail.setHoldType(faStockHoldDetail.getHoldType());
            }
            // 1清空
            faStockHoldDetail.setStatus(1);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);

            faStockHoldDetailIPage.convert(u ->{
                StockHoldDetailString stockHoldDetailString = new StockHoldDetailString();
                stockHoldDetailString = JSON.parseObject(JSON.toJSONString(u), StockHoldDetailString.class);
                return stockHoldDetailString;
            });

            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailListHistory", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailListHistory", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询持仓列表
     */
    @ApiOperation("查询大宗持仓列表")
    @AppLog(title = "查询大宗持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String")
    })
    @PostMapping("/getStockHoldDetailListDz")
    public AjaxResult getStockHoldDetailListDz(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 2大宗交易
            faStockHoldDetail.setHoldType(2);
            // 0持有
            faStockHoldDetail.setStatus(0);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);
            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailListDz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailListDz", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询大宗历史持仓列表
     */
    @ApiOperation("查询大宗历史持仓列表")
    @AppLog(title = "查询大宗历史持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "String"),
    })
    @PostMapping("/getStockHoldDetailListDzHistory")
    public AjaxResult getStockHoldDetailListDzHistory(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 2大宗交易
            faStockHoldDetail.setHoldType(2);
            // 1清空
            faStockHoldDetail.setStatus(1);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);
            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailListDzHistory", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailListDzHistory", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询VIP调研持仓列表
     */
    @ApiOperation("查询VIP调研持仓列表")
    @AppLog(title = "查询VIP调研持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String")
    })
    @PostMapping("/getStockHoldDetailListVip")
    public AjaxResult getStockHoldDetailListVip(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 3VIP调研
            faStockHoldDetail.setHoldType(3);
            // 0持有
            faStockHoldDetail.setStatus(0);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);
            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailListVip", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailListVip", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询VIP历史持仓列表
     */
    @ApiOperation("查询VIP历史持仓列表")
    @AppLog(title = "查询VIP历史持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "截至时间", dataType = "String"),
    })
    @PostMapping("/getStockHoldDetailListVipHistory")
    public AjaxResult getStockHoldDetailListVipHistory(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            if (null == faStockHoldDetail.getPage()) {
                faStockHoldDetail.setPage(1);
            }
            if (null == faStockHoldDetail.getSize()) {
                faStockHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            // 3VIP调研
            faStockHoldDetail.setHoldType(3);
            // 1清空
            faStockHoldDetail.setStatus(1);
            IPage<FaStockHoldDetail> faStockHoldDetailIPage = iFaStockHoldDetailService.getStockHoldDetailList(faStockHoldDetail);
            return AjaxResult.success(faStockHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetailListVipHistory", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetailListVipHistory", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询持仓明细
     */
    @ApiOperation("查询持仓明细")
    @AppLog(title = "查询持仓明细", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer")})
    @PostMapping("/getStockHoldDetail")
    public AjaxResult getStockHoldDetail(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
            return AjaxResult.success(faStockHoldDetail);
        } catch (ServiceException e) {
            logger.error("getStockHoldDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockHoldDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询大宗持仓统计
     */
    @ApiOperation("查询大宗持仓统计")
    @AppLog(title = "查询大宗持仓统计", businessType = BusinessType.OTHER)
    @PostMapping("/getDzHoldStatistics")
    public AjaxResult getDzHoldStatistics(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            Map<String, BigDecimal> map = iFaStockHoldDetailService.getDzHoldStatistics(faStockHoldDetail);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getDzHoldStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getDzHoldStatistics", e);
            return AjaxResult.error();
        }
    }

}
