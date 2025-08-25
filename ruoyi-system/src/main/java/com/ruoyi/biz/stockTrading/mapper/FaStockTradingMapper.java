package com.ruoyi.biz.stockTrading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成交记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface FaStockTradingMapper extends BaseMapper<FaStockTrading>
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
     * 删除成交记录
     *
     * @param id 成交记录主键
     * @return 结果
     */
    public int deleteFaStockTradingById(Integer id);

    /**
     * 批量删除成交记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaStockTradingByIds(Integer[] ids);

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
}