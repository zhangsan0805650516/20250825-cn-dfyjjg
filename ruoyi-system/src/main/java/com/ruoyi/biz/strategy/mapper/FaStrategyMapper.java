package com.ruoyi.biz.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface FaStrategyMapper extends BaseMapper<FaStrategy>
{
    /**
     * 查询策略
     *
     * @param id 策略主键
     * @return 策略
     */
    public FaStrategy selectFaStrategyById(Integer id);

    /**
     * 查询策略列表
     *
     * @param faStrategy 策略
     * @return 策略集合
     */
    public List<FaStrategy> selectFaStrategyList(FaStrategy faStrategy);

    /**
     * 新增策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    public int insertFaStrategy(FaStrategy faStrategy);

    /**
     * 修改策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    public int updateFaStrategy(FaStrategy faStrategy);

    /**
     * 删除策略
     *
     * @param id 策略主键
     * @return 结果
     */
    public int deleteFaStrategyById(Integer id);

    /**
     * 批量删除策略
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaStrategyByIds(Integer[] ids);

    /**
     * 获取股票代码列表
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    String[] getCodeList(@Param("start") int start, @Param("end") int end) throws Exception;

    /**
     * 从数据库取最新价
     * @param faStrategy
     * @return
     * @throws Exception
     */
    BigDecimal getStockCurrentPrice(FaStrategy faStrategy) throws Exception;

    /**
     * 刷新大宗价格
     * @throws Exception
     */
    void updateDzPrice() throws Exception;

    /**
     * 刷新抢筹价
     * @throws Exception
     */
    void updateVipQcPrice() throws Exception;

    /**
     * 取出持仓股票allCode集合
     * @return
     * @throws Exception
     */
    String[] getHoldingStockCodeList() throws Exception;

    String[] getChooseStockCodeList() throws Exception;
}