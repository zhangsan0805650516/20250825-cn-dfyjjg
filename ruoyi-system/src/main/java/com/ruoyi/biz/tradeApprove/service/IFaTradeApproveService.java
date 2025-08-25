package com.ruoyi.biz.tradeApprove.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;

import java.util.List;

/**
 * 交易审核Service接口
 *
 * @author ruoyi
 * @date 2024-05-31
 */
public interface IFaTradeApproveService extends IService<FaTradeApprove>
{
    /**
     * 查询交易审核
     *
     * @param id 交易审核主键
     * @return 交易审核
     */
    public FaTradeApprove selectFaTradeApproveById(Integer id);

    /**
     * 查询交易审核列表
     *
     * @param faTradeApprove 交易审核
     * @return 交易审核集合
     */
    public List<FaTradeApprove> selectFaTradeApproveList(FaTradeApprove faTradeApprove);

    /**
     * 新增交易审核
     *
     * @param faTradeApprove 交易审核
     * @return 结果
     */
    public int insertFaTradeApprove(FaTradeApprove faTradeApprove);

    /**
     * 修改交易审核
     *
     * @param faTradeApprove 交易审核
     * @return 结果
     */
    public int updateFaTradeApprove(FaTradeApprove faTradeApprove);

    /**
     * 批量删除交易审核
     *
     * @param ids 需要删除的交易审核主键集合
     * @return 结果
     */
    public int deleteFaTradeApproveByIds(Integer[] ids);

    /**
     * 删除交易审核信息
     *
     * @param id 交易审核主键
     * @return 结果
     */
    public int deleteFaTradeApproveById(Integer id);

    /**
     * 新增大宗审核
     * @param faStockTrading
     * @throws Exception
     */
    void addDz(FaStockTrading faStockTrading) throws Exception;

    /**
     * 审核交易
     * @param faTradeApprove
     * @throws Exception
     */
    void approveTrade(FaTradeApprove faTradeApprove) throws Exception;
}