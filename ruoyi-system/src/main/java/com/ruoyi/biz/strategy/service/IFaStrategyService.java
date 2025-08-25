package com.ruoyi.biz.strategy.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.common.core.domain.AjaxResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 策略Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface IFaStrategyService extends IService<FaStrategy>
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
     * 批量删除策略
     *
     * @param ids 需要删除的策略主键集合
     * @return 结果
     */
    public int deleteFaStrategyByIds(Integer[] ids);

    /**
     * 删除策略信息
     *
     * @param id 策略主键
     * @return 结果
     */
    public int deleteFaStrategyById(Integer id);

    /**
     * 查询大宗交易
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getDZStrategy(FaStrategy faStrategy) throws Exception;

    /**
     * 查询抢筹列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getQCStrategy(FaStrategy faStrategy) throws Exception;

    /**
     * 查询融券列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getRQStrategy(FaStrategy faStrategy) throws Exception;

    /**
     * 股票是否存在
     * @param faStrategy
     * @return
     * @throws Exception
     */
    FaStrategy checkStock(FaStrategy faStrategy) throws Exception;

    /**
     * 新股转入股票列表
     * @param faNewStock
     * @throws Exception
     */
    FaStrategy addFromNewStock(FaNewStock faNewStock) throws Exception;

    /**
     * 查询股票详情
     * @param faStrategy
     * @return
     * @throws Exception
     */
    FaStrategy getStockDetail(FaStrategy faStrategy) throws Exception;

    /**
     * 搜素股票
     * @param faStrategy
     * @return
     * @throws Exception
     */
    List<FaStrategy> searchStock(FaStrategy faStrategy) throws Exception;

    /**
     * 获取股票实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    BigDecimal getCurrentPrice(FaStrategy faStrategy) throws Exception;

    /**
     * 修改股票状态
     * @param faStrategy
     * @return
     * @throws Exception
     */
    AjaxResult changeStockStatus(FaStrategy faStrategy) throws Exception;

    /**
     * 查询股票列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getStrategyList(FaStrategy faStrategy) throws Exception;

    /**
     * 查询首页5大指数
     * @param faStrategy
     * @return
     * @throws Exception
     */
    List<FaStrategy> getIndexList(FaStrategy faStrategy) throws Exception;

    /**
     * 查询涨跌对比
     * @param faStrategy
     * @return
     * @throws Exception
     */
    Map<String, Integer> getRiseAndFall(FaStrategy faStrategy) throws Exception;

    /**
     * 板块涨跌幅
     * @param faStrategy
     * @return
     * @throws Exception
     */
    JSONArray getBkHotList(FaStrategy faStrategy) throws Exception;

    /**
     * 板块涨跌幅-新浪
     * @param faStrategy
     * @return
     * @throws Exception
     */
    JSONArray getBkHotListFromSina(FaStrategy faStrategy) throws Exception;

    /**
     * 资金流向
     * @param faStrategy
     * @return
     * @throws Exception
     */
    JSONObject getMoneyFlow(FaStrategy faStrategy) throws Exception;

    /**
     * 查询推荐列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getRecommendStrategy(FaStrategy faStrategy) throws Exception;

    /**
     * 上证指数k线
     * @param faStrategy
     * @return
     * @throws Exception
     */
    List<Map<String, String>> getSHKline(FaStrategy faStrategy) throws Exception;

    /**
     * 前瞻会议 pz=2 取第四个
     * @return
     * @throws Exception
     */
    JSONArray getQzhy() throws Exception;

    /**
     * 取涨幅前十之一，业绩略超预期，稳健增长
     * @return
     * @throws Exception
     */
    FaStrategy getYbjx() throws Exception;

    /**
     * 前瞻会议 pz=2 取前三个
     * @return
     * @throws Exception
     */
    JSONArray getRmhy() throws Exception;

    /**
     * 取涨幅前十之二，业绩略超预期，稳健增长
     * @return
     * @throws Exception
     */
    List<FaStrategy> getDxjj() throws Exception;

    /**
     * 涨跌柱状图
     * @param faStrategy
     * @return
     * @throws Exception
     */
    Map<String, Integer> getRiseAndFallBar(FaStrategy faStrategy) throws Exception;

    /**
     * 龙虎榜
     * @param faStrategy
     * @return
     * @throws Exception
     */
    List<FaStrategy> getDragonTigerList(FaStrategy faStrategy) throws Exception;

    /**
     * 查询爱股票领涨概念板块
     * @return
     * @throws Exception
     */
    JSONObject getAiStockUpPlate(FaStrategy faStrategy) throws Exception;

    /**
     * 修改预约重组
     * @param faStrategy
     * @return
     * @throws Exception
     */
    AjaxResult submitFormYycz(FaStrategy faStrategy) throws Exception;

    /**
     * 查询预约重组列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    IPage<FaStrategy> getYyczStrategy(FaStrategy faStrategy) throws Exception;

    /**
     * 新股大宗数量统计
     * @param faStrategy
     * @return
     * @throws Exception
     */
    Map<String, Integer> getNewCountAndDz(FaStrategy faStrategy) throws Exception;
}