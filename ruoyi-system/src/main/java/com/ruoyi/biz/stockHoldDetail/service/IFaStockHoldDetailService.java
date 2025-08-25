package com.ruoyi.biz.stockHoldDetail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.strategy.domain.FaStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 持仓明细Service接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface IFaStockHoldDetailService extends IService<FaStockHoldDetail>
{
    /**
     * 查询持仓明细
     *
     * @param id 持仓明细主键
     * @return 持仓明细
     */
    public FaStockHoldDetail selectFaStockHoldDetailById(Integer id);

    /**
     * 查询持仓明细列表
     *
     * @param faStockHoldDetail 持仓明细
     * @return 持仓明细集合
     */
    public List<FaStockHoldDetail> selectFaStockHoldDetailList(FaStockHoldDetail faStockHoldDetail);

    /**
     * 新增持仓明细
     *
     * @param faStockHoldDetail 持仓明细
     * @return 结果
     */
    public int insertFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 修改持仓明细
     *
     * @param faStockHoldDetail 持仓明细
     * @return 结果
     */
    public int updateFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail);

    /**
     * 批量删除持仓明细
     *
     * @param ids 需要删除的持仓明细主键集合
     * @return 结果
     */
    public int deleteFaStockHoldDetailByIds(Integer[] ids);

    /**
     * 删除持仓明细信息
     *
     * @param id 持仓明细主键
     * @return 结果
     */
    public int deleteFaStockHoldDetailById(Integer id);

    /**
     * 持仓明细剩余冻结天数-1
     * @throws Exception
     */
    void updateFreezeDaysLeft() throws Exception;

    /**
     * 持仓明细冻结状态判断
     * @throws Exception
     */
    void updateFreezeStatus() throws Exception;

    /**
     * 扣减持仓
     * @param faStockTrading
     * @param type 0 强平 1 卖出
     * @param currentStatus 当天平仓
     * @throws Exception
     */
    FaStockHoldDetail subtractStockHoldDetail(FaStockTrading faStockTrading, Integer type, Integer currentStatus) throws Exception;

    /**
     * 新增持仓
     * @param faStockTrading
     * @throws Exception
     */
    FaStockHoldDetail addStockHoldDetail(FaStockTrading faStockTrading) throws Exception;

    /**
     * 查询持仓列表
     * @param faStockHoldDetail
     * @return
     * @throws Exception
     */
    IPage<FaStockHoldDetail> getStockHoldDetailList(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 修改锁仓状态
     * @param faStockHoldDetail
     * @throws Exception
     */
    void changeLockStatus(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 新股申购转持仓
     * @param faNewStock
     * @throws Exception
     */
    void addFromNewStock(FaNewStock faNewStock, FaStrategy faStrategy) throws Exception;

    /**
     * T+1冻结持仓清零
     * @throws Exception
     */
    void clearT1Hold() throws Exception;

    /**
     * 调整T+N
     * @param faStockHoldDetail
     * @throws Exception
     */
    void changeTN(FaStockHoldDetail faStockHoldDetail) throws Exception;

    /**
     * 查询大宗持仓统计
     * @param faStockHoldDetail
     * @return
     */
    Map<String, BigDecimal> getDzHoldStatistics(FaStockHoldDetail faStockHoldDetail) throws Exception;
}