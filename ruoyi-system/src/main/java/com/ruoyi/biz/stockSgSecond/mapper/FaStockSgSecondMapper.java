package com.ruoyi.biz.stockSgSecond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;

import java.util.List;

/**
 * 线下配售(世纪独享)Mapper接口
 *
 * @author ruoyi
 * @date 2024-11-01
 */
public interface FaStockSgSecondMapper extends BaseMapper<FaStockSgSecond>
{
    /**
     * 查询线下配售(世纪独享)
     *
     * @param id 线下配售(世纪独享)主键
     * @return 线下配售(世纪独享)
     */
    public FaStockSgSecond selectFaStockSgSecondById(Long id);

    /**
     * 查询线下配售(世纪独享)列表
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 线下配售(世纪独享)集合
     */
    public List<FaStockSgSecond> selectFaStockSgSecondList(FaStockSgSecond faStockSgSecond);

    /**
     * 新增线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    public int insertFaStockSgSecond(FaStockSgSecond faStockSgSecond);

    /**
     * 修改线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    public int updateFaStockSgSecond(FaStockSgSecond faStockSgSecond);

    /**
     * 删除线下配售(世纪独享)
     *
     * @param id 线下配售(世纪独享)主键
     * @return 结果
     */
    public int deleteFaStockSgSecondById(Long id);

    /**
     * 批量删除线下配售(世纪独享)
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaStockSgSecondByIds(Long[] ids);
}