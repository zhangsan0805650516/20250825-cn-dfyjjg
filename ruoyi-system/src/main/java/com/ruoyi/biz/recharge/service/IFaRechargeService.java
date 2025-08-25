package com.ruoyi.biz.recharge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.recharge.domain.FaRecharge;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值Service接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface IFaRechargeService extends IService<FaRecharge>
{
    /**
     * 查询充值
     *
     * @param id 充值主键
     * @return 充值
     */
    public FaRecharge selectFaRechargeById(Integer id);

    /**
     * 查询充值列表
     *
     * @param faRecharge 充值
     * @return 充值集合
     */
    public List<FaRecharge> selectFaRechargeList(FaRecharge faRecharge);

    /**
     * 新增充值
     *
     * @param faRecharge 充值
     * @return 结果
     */
    public int insertFaRecharge(FaRecharge faRecharge);

    /**
     * 修改充值
     *
     * @param faRecharge 充值
     * @return 结果
     */
    public int updateFaRecharge(FaRecharge faRecharge);

    /**
     * 批量删除充值
     *
     * @param ids 需要删除的充值主键集合
     * @return 结果
     */
    public int deleteFaRechargeByIds(Integer[] ids);

    /**
     * 删除充值信息
     *
     * @param id 充值主键
     * @return 结果
     */
    public int deleteFaRechargeById(Integer id);

    /**
     * 审核充值
     * @param faRecharge
     */
    void approveRecharge(FaRecharge faRecharge) throws Exception;

    /**
     * 充值统计
     * @param faRecharge
     * @return
     * @throws Exception
     */
    List<BigDecimal> getRechargeStatistics(FaRecharge faRecharge) throws Exception;

    /**
     * 已付款总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    BigDecimal getTotalPaid(FaRecharge faRecharge) throws Exception;

    /**
     * 未付款总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    BigDecimal getTotalUnpaid(FaRecharge faRecharge) throws Exception;

    /**
     * 驳回总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    BigDecimal getTotalRefuse(FaRecharge faRecharge) throws Exception;
}