package com.ruoyi.biz.memberStock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.memberStock.domain.FaMemberStock;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.common.core.domain.entity.FaMember;

import java.util.List;

/**
 * 自选股票Service接口
 *
 * @author ruoyi
 * @date 2024-01-19
 */
public interface IFaMemberStockService extends IService<FaMemberStock>
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
     * 批量删除自选股票
     *
     * @param ids 需要删除的自选股票主键集合
     * @return 结果
     */
    public int deleteFaMemberStockByIds(Integer[] ids);

    /**
     * 删除自选股票信息
     *
     * @param id 自选股票主键
     * @return 结果
     */
    public int deleteFaMemberStockById(Integer id);

    /**
     * 查询自选股票
     * @param faMemberStock
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getMemberStock(FaMemberStock faMemberStock) throws Exception;

    /**
     * 查询股票是否自选
     * @param faStrategy
     * @param faMember
     * @return
     * @throws Exception
     */
    int checkMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception;

    /**
     * 新增自选股票
     * @param faStrategy
     * @param faMember
     * @throws Exception
     */
    void addMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception;

    /**
     * 删除自选股票
     * @param faStrategy
     * @param faMember
     * @throws Exception
     */
    void deleteMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception;

}