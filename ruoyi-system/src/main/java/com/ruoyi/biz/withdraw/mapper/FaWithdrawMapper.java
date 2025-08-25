package com.ruoyi.biz.withdraw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface FaWithdrawMapper extends BaseMapper<FaWithdraw>
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
     * 删除提现
     *
     * @param id 提现主键
     * @return 结果
     */
    public int deleteFaWithdrawById(Integer id);

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaWithdrawByIds(Integer[] ids);

    /**
     * 今天提现次数
     * @return
     * @throws Exception
     */
    int getWithdrawTimesToday(@Param("userId") Integer userId) throws Exception;

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
    BigDecimal getWithdrawAlreadyToday(@Param("userId") Integer userId) throws Exception;

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
}