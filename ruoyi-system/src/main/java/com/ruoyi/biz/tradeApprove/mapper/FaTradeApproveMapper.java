package com.ruoyi.biz.tradeApprove.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;

import java.util.List;

/**
 * 交易审核Mapper接口
 *
 * @author ruoyi
 * @date 2024-05-31
 */
public interface FaTradeApproveMapper extends BaseMapper<FaTradeApprove>
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
     * 删除交易审核
     *
     * @param id 交易审核主键
     * @return 结果
     */
    public int deleteFaTradeApproveById(Integer id);

    /**
     * 批量删除交易审核
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaTradeApproveByIds(Integer[] ids);
}