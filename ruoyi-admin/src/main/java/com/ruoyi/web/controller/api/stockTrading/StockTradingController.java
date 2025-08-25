package com.ruoyi.web.controller.api.stockTrading;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingBatchService;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HttpStatus;
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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成交记录Controller
 * 
 * @author ruoyi
 * @date 2024-01-23
 */
@Api(tags = "股票交易")
@RestController
@RequestMapping("/api/stockTrading")
public class StockTradingController extends BaseController
{
    @Autowired
    private IFaStockTradingService faStockTradingService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaStockTradingBatchService iFaStockTradingBatchService;

    /**
     * 买入股票
     */
    @RepeatSubmit
    @ApiOperation("买入股票(普通交易)")
    @AppLog(title = "买入股票(普通交易)", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockId", value = "股票id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "tradingNumber", value = "成交数量(股)", required = true, dataType = "Integer")
    })
    @PostMapping("/buyStock")
    public AjaxResult buyStock(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockTrading.setMemberId(loginMember.getFaMember().getId());
            faStockTradingService.buyStock(faStockTrading);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出股票（拆分持仓）
     */
    @RepeatSubmit
    @ApiOperation("卖出股票(拆分持仓)")
    @AppLog(title = "卖出股票(拆分持仓)", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sellNumber", value = "卖出数量(股)", dataType = "Integer")
    })
    @PostMapping("/sellStockBatch")
    public AjaxResult sellStockBatch(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            // 参数判断
            if (null == faStockHoldDetail.getId()) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }

            // 查询持仓
            FaStockHoldDetail selectOne = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
            if (ObjectUtils.isEmpty(selectOne)) {
                throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
            }

            // 持仓状态
            if (1 == selectOne.getStatus()) {
                throw new ServiceException(MessageUtils.message("stock.hold.already.sell"), HttpStatus.ERROR);
            }

            // 是否上市
            if (0 == selectOne.getIsList()) {
                throw new ServiceException(MessageUtils.message("unlisted.stocks"), HttpStatus.ERROR);
            }

            iFaStockTradingBatchService.sellStockBatch(faStockHoldDetail, selectOne);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellStockBatch", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellStockBatch", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出股票
     */
    @RepeatSubmit
    @ApiOperation("卖出股票")
    @AppLog(title = "卖出股票", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer")
    })
    @PostMapping("/sellStock")
    public AjaxResult sellStock(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());

            // 参数判断
            if (null == faStockHoldDetail.getId()) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }

            // 查询持仓
            faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
            if (ObjectUtils.isEmpty(faStockHoldDetail)) {
                throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
            }

            // 持仓状态
            if (1 == faStockHoldDetail.getStatus()) {
                throw new ServiceException(MessageUtils.message("stock.hold.already.sell"), HttpStatus.ERROR);
            }

            // 是否上市
            if (0 == faStockHoldDetail.getIsList()) {
                throw new ServiceException(MessageUtils.message("unlisted.stocks"), HttpStatus.ERROR);
            }

            //新股交易，走普通逻辑
            if (0 == faStockHoldDetail.getHoldType()) {
                faStockTradingService.sellStock(faStockHoldDetail);
            }
            //普通交易
            else if (1 == faStockHoldDetail.getHoldType()) {
                faStockTradingService.sellStock(faStockHoldDetail);
            }
            // 大宗交易
            else if (2 == faStockHoldDetail.getHoldType()) {
                faStockTradingService.sellStockDz(faStockHoldDetail);
            }
            // VIP调研交易
            else if (3 == faStockHoldDetail.getHoldType()) {
                faStockTradingService.sellStockVip(faStockHoldDetail);
            }
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 买入股票
     */
    @RepeatSubmit
    @ApiOperation("买入股票(大宗交易)")
    @AppLog(title = "买入股票(大宗交易)", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockId", value = "股票id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "tradingNumber", value = "成交数量(股)", required = true, dataType = "Integer")
    })
    @PostMapping("/buyStockDz")
    public AjaxResult buyStockDz(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            // 参数判断
            if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getHoldType()) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }

            LoginMember loginMember = getLoginMember();
            faStockTrading.setMemberId(loginMember.getFaMember().getId());

            // 2大宗交易
            if (2 == faStockTrading.getHoldType()) {
                // 进入审核
//                faStockTradingService.buyStockDzApprove(faStockTrading);
                // 大宗买入
                faStockTradingService.buyStockDz(faStockTrading);
            }
            // 3VIP调研
            else if (3 == faStockTrading.getHoldType()) {
                faStockTradingService.buyStockVip(faStockTrading);
            }

            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyStockDz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyStockDz", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出股票 废弃
     */
    @RepeatSubmit
    @ApiOperation("卖出股票(大宗交易)")
    @AppLog(title = "卖出股票(大宗交易)", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "大宗持仓id", required = true, dataType = "Integer")
    })
    @PostMapping("/sellStockDz")
    public AjaxResult sellStockDz(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockTradingService.sellStockDz(faStockHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellStockDz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellStockDz", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询成交记录
     */
    @ApiOperation("查询成交记录")
    @AppLog(title = "查询成交记录", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stockId", value = "股票id(可选)", dataType = "Integer"),
            @ApiImplicitParam(name = "stockSymbol", value = "股票代码(可选)", dataType = "String"),
            @ApiImplicitParam(name = "tradingType", value = "买卖(1买 2卖)(可选)", dataType = "Integer")})
    @PostMapping("/getStockTradingList")
    public AjaxResult getStockTradingList(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            if (null == faStockTrading.getPage()) {
                faStockTrading.setPage(1);
            }
            if (null == faStockTrading.getSize()) {
                faStockTrading.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockTrading.setMemberId(loginMember.getFaMember().getId());
            IPage<FaStockTrading> faStockTradingIPage = faStockTradingService.getStockTradingList(faStockTrading);
            return AjaxResult.success(faStockTradingIPage);
        } catch (ServiceException e) {
            logger.error("getStockTradingList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockTradingList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询成交记录详情
     */
    @ApiOperation("查询成交记录详情")
    @AppLog(title = "查询成交记录详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "成交记录id", required = true, dataType = "Integer")})
    @PostMapping("/getStockTradingDetail")
    public AjaxResult getStockTradingDetail(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            faStockTrading = faStockTradingService.getStockTradingDetail(faStockTrading);
            return AjaxResult.success(faStockTrading);
        } catch (ServiceException e) {
            logger.error("getStockTradingDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockTradingDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 买入融券股票
     */
    @RepeatSubmit
    @ApiOperation("买入融券股票")
    @AppLog(title = "买入融券股票", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockId", value = "股票id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "tradingNumber", value = "成交数量(股)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "tradeDirect", value = "方向(1买涨 2买跌)", required = true, dataType = "Integer"),
    })
    @PostMapping("/buySecuritiesLending")
    public AjaxResult buyFXContract(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockTrading.setMemberId(loginMember.getFaMember().getId());
            faStockTradingService.buySecuritiesLending(faStockTrading);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buySecuritiesLending", e);
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("buySecuritiesLending", e);
            return AjaxResult.error(MessageUtils.message("operation.fail"));
        }
    }

    /**
     * 平仓融券股票
     */
    @RepeatSubmit
    @ApiOperation("平仓融券股票")
    @AppLog(title = "平仓融券股票", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer")
    })
    @PostMapping("/closeSecuritiesLending")
    public AjaxResult closeFXContract(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockHoldDetail.setMemberId(loginMember.getFaMember().getId());
            faStockTradingService.closeSecuritiesLending(faStockHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("closeSecuritiesLending", e);
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("closeSecuritiesLending", e);
            return AjaxResult.error(MessageUtils.message("operation.fail"));
        }
    }

    /**
     * 买入股票
     */
    @RepeatSubmit
    @ApiOperation("买入股票(预约重组)")
    @AppLog(title = "买入股票(预约重组)", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockId", value = "股票id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "tradingNumber", value = "成交数量(股)", required = true, dataType = "Integer")
    })
    @PostMapping("/buyStockYycz")
    public AjaxResult buyStockYycz(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockTrading.setMemberId(loginMember.getFaMember().getId());
            faStockTradingService.buyStockYycz(faStockTrading);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyStockYycz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyStockYycz", e);
            return AjaxResult.error();
        }
    }

}
