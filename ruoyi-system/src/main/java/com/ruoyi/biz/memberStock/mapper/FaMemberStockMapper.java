package com.ruoyi.biz.memberStock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.biz.memberStock.domain.FaMemberStock;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自选股票Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-19
 */
public interface FaMemberStockMapper extends BaseMapper<FaMemberStock>
{
    /**
     * 查询自选股票
     *
     * @param id 自选股票主键
     * @return 自选股票
     */
    public FaMemberStock selectFaMemberStockById(Integer id);

    /**
     * 查询自选股票列表
     *
     * @param faMemberStock 自选股票
     * @return 自选股票集合
     */
    public List<FaMemberStock> selectFaMemberStockList(FaMemberStock faMemberStock);

    /**
     * 新增自选股票
     *
     * @param faMemberStock 自选股票
     * @return 结果
     */
    public int insertFaMemberStock(FaMemberStock faMemberStock);

    /**
     * 修改自选股票
     *
     * @param faMemberStock 自选股票
     * @return 结果
     */
    public int updateFaMemberStock(FaMemberStock faMemberStock);

    /**
     * 删除自选股票
     *
     * @param id 自选股票主键
     * @return 结果
     */
    public int deleteFaMemberStockById(Integer id);

    /**
     * 批量删除自选股票
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaMemberStockByIds(Integer[] ids);

    /**
     * 查询自选股票
     * @param faMemberStock
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getMemberStock(Page<FaStrategy> page, @Param("faMemberStock") FaMemberStock faMemberStock) throws Exception;

}