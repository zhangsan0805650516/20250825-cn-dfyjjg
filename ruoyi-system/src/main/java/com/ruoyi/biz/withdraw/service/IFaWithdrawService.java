package com.ruoyi.biz.withdraw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现Service接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface IFaWithdrawService extends IService<FaWithdraw>
{
    /**
     * 查询提现
     *
     * @param id 提现主键
     * @return 提现
     */
    public FaWithdraw selectFaWithdrawById(Integer id);

    /**
     * 查询提现列表
     *
     * @param faWithdraw 提现
     * @return 提现集合
     */
    public List<FaWithdraw> selectFaWithdrawList(FaWithdraw faWithdraw);

    /**
     * 新增提现
     *
     * @param faWithdraw 提现
     * @return 结果
     */
    public int insertFaWithdraw(FaWithdraw faWithdraw);

    /**
     * 修改提现
     *
     * @param faWithdraw 提现
     * @return 结果
     */
    public int updateFaWithdraw(FaWithdraw faWithdraw);

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的提现主键集合
     * @return 结果
     */
    public int deleteFaWithdrawByIds(Integer[] ids);

    /**
     * 删除提现信息
     *
     * @param id 提现主键
     * @return 结果
     */
    public int deleteFaWithdrawById(Integer id);

    /**
     * 审核提现
     * @param faWithdraw
     * @throws Exception
     */
    void approveWithdraw(FaWithdraw faWithdraw) throws Exception;

    /**
     * 今天提现次数
     * @return
     * @throws Exception
     */
    int getWithdrawTimesToday(Integer userId) throws Exception;

    /**
     * 提现统计
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    List<BigDecimal> getWithdrawStatistics(FaWithdraw faWithdraw) throws Exception;

    /**
     * 今日提现金额
     * @return
     * @throws Exception
     */
    BigDecimal getWithdrawAlreadyToday(Integer userId) throws Exception;

    /**
     * 已打款总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    BigDecimal getTotalPaid(FaWithdraw faWithdraw) throws Exception;

    /**
     * 未打款总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    BigDecimal getTotalUnpaid(FaWithdraw faWithdraw) throws Exception;

    /**
     * 驳回总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    BigDecimal getTotalRefuse(FaWithdraw faWithdraw) throws Exception;

    /**
     * 修改消息通知
     * @param faWithdraw
     * @throws Exception
     */
    void changeIsQx(FaWithdraw faWithdraw) throws Exception;

    /**
     * 检测消息
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    boolean checkQx(FaWithdraw faWithdraw) throws Exception;
}