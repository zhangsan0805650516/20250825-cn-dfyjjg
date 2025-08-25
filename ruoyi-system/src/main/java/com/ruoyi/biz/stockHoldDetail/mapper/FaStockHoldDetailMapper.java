package com.ruoyi.biz.stockHoldDetail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;

import java.util.List;

/**
 * 持仓明细Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface FaStockHoldDetailMapper extends BaseMapper<FaStockHoldDetail>
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
    public int insertFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail);

    /**
     * 修改持仓明细
     *
     * @param faStockHoldDetail 持仓明细
     * @return 结果
     */
    public int updateFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail);

    /**
     * 删除持仓明细
     *
     * @param id 持仓明细主键
     * @return 结果
     */
    public int deleteFaStockHoldDetailById(Integer id);

    /**
     * 批量删除持仓明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaStockHoldDetailByIds(Integer[] ids);

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
     * 查询大宗持仓统计
     * @param faStockHoldDetail
     * @return
     */
    FaStockHoldDetail getDzHoldStatistics(FaStockHoldDetail faStockHoldDetail) throws Exception;
}