package com.ruoyi.biz.stockTrading.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成交记录Service接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface IFaStockTradingService extends IService<FaStockTrading>
{
    /**
     * 查询成交记录
     *
     * @param id 成交记录主键
     * @return 成交记录
     */
    public FaStockTrading selectFaStockTradingById(Integer id);

    /**
     * 查询成交记录列表
     *
     * @param faStockTrading 成交记录
     * @return 成交记录集合
     */
    public List<FaStockTrading> selectFaStockTradingList(FaStockTrading faStockTrading);

    /**
     * 新增成交记录
     *
     * @param faStockTrading 成交记录
     * @return 结果
     */
    public int insertFaStockTrading(FaStockTrading faStockTrading);

    /**
     * 修改成交记录
     *
     * @param faStockTrading 成交记录
     * @return 结果
     */
    public int updateFaStockTrading(FaStockTrading faStockTrading);

    /**
     * 批量删除成交记录
     *
     * @param ids 需要删除的成交记录主键集合
     * @return 结果
     */
    public int deleteFaStockTradingByIds(Integer[] ids);

    /**
     * 删除成交记录信息
     *
     * @param id 成交记录主键
     * @return 结果
     */
    public int deleteFaStockTradingById(Integer id);

    /**
     * 查询成交记录
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    IPage<FaStockTrading> getStockTradingList(FaStockTrading faStockTrading) throws Exception;

    /**
     * 查询成交记录详情
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    FaStockTrading getStockTradingDetail(FaStockTrading faStockTrading) throws Exception;

    /**
     * 买入股票(普通交易)
     * @param faStockTrading
     * @throws Exception
     */
    void buyStock(FaStockTrading faStockTrading) throws Exception;

    /**
     * 卖出股票(普通交易)
     * @param faStockTrading
     * @throws Exception
     */
    void sellStock(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 买入股票(大宗交易)
     * @param faStockTrading
     * @throws Exception
     */
    void buyStockDz(FaStockTrading faStockTrading) throws Exception;

    /**
     * 大宗交易 进入审核
     * @param faStockTrading
     * @throws Exception
     */
    void buyStockDzApprove(FaStockTrading faStockTrading) throws Exception;

    /**
     * 买入股票(VIP调研)
     * @param faStockTrading
     * @throws Exception
     */
    void buyStockVip(FaStockTrading faStockTrading) throws Exception;

    /**
     * 卖出股票(大宗交易)
     * @param faStockHoldDetail
     * @throws Exception
     */
    void sellStockDz(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 卖出股票(VIP调研)
     * @param faStockHoldDetail
     * @throws Exception
     */
    void sellStockVip(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 平仓
     * @param faStockHoldDetail
     * @throws Exception
     */
    void closingPositionDetail(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 交易统计
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getTradingStatistics(FaStockTrading faStockTrading) throws Exception;

    /**
     * 买入手续费
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    BigDecimal getTotalBuyFee(FaStockTrading faStockTrading) throws Exception;

    /**
     * 卖出手续费
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    BigDecimal getTotalSellFee(FaStockTrading faStockTrading) throws Exception;

    /**
     * 印花税
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    BigDecimal getTotalStampDuty(FaStockTrading faStockTrading) throws Exception;

    /**
     * 买入融券股票
     * @param faStockTrading
     * @throws Exception
     */
    void buySecuritiesLending(FaStockTrading faStockTrading) throws Exception;

    /**
     * 平仓融券股票
     * @param faStockHoldDetail
     * @throws Exception
     */
    void closeSecuritiesLending(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 买入股票(预约重组)
     * @param faStockTrading
     * @throws Exception
     */
    void buyStockYycz(FaStockTrading faStockTrading) throws Exception;

    /**
     * 卖出股票(预约重组)
     * @param sellHold
     * @throws Exception
     */
    void sellStockYycz(FaStockHoldDetail sellHold) throws Exception;
}