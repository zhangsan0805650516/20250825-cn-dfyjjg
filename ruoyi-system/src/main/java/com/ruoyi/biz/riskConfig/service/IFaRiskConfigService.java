package com.ruoyi.biz.riskConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.domain.FaRiskConfig;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;

import java.util.List;
import java.util.Map;

/**
 * 风控设置Service接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface IFaRiskConfigService extends IService<FaRiskConfig>
{
    /**
     * 查询风控设置
     *
     * @param id 风控设置主键
     * @return 风控设置
     */
    public FaRiskConfig selectFaRiskConfigById(Integer id);

    /**
     * 查询风控设置
     *
     * @param name 风控设置主键
     * @return 风控设置
     */
    public FaRiskConfig selectFaRiskConfigByName(String name);

    /**
     * 查询风控设置列表
     *
     * @param faRiskConfig 风控设置
     * @return 风控设置集合
     */
    public List<FaRiskConfig> selectFaRiskConfigList(FaRiskConfig faRiskConfig);

    /**
     * 新增风控设置
     *
     * @param faRiskConfig 风控设置
     * @return 结果
     */
    public int insertFaRiskConfig(FaRiskConfig faRiskConfig);

    /**
     * 修改风控设置
     *
     * @param faRiskConfig 风控设置
     * @return 结果
     */
    public int updateFaRiskConfig(FaRiskConfig faRiskConfig);

    /**
     * 批量删除风控设置
     *
     * @param ids 需要删除的风控设置主键集合
     * @return 结果
     */
    public int deleteFaRiskConfigByIds(Integer[] ids);

    /**
     * 删除风控设置信息
     *
     * @param id 风控设置主键
     * @return 结果
     */
    public int deleteFaRiskConfigById(Integer id);

    /**
     * 获取字典分类
     * @param
     * @return
     */
    List<FaRiskConfig> getConfiggroup() throws Exception;

    /**
     * 根据分类获取字典列表
     */
    List<FaRiskConfig> getConfigListByGroup(FaRiskConfig faRiskConfig) throws Exception;

    /**
     * 修改风控设置列表
     */
    void updateRiskConfigList(List<FaRiskConfig> faRiskConfigList) throws Exception;

    /**
     * 查询风控设置map
     */
    Map getRiskConfigMap() throws Exception;

    /**
     * 查询风控设置配置项
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    String getConfigValue(String key, String defaultValue) throws Exception;

    /**
     * 普通交易时间校验
     * @throws Exception
     */
    void checkOrdinaryTradeTime(FaExchangeConfig faExchangeConfig) throws Exception;

    /**
     * 普通交易风控校验
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    void checkOrdinaryTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception;

    /**
     * 大宗风控校验
     * @param faStockTrading
     * @throws Exception
     */
    void checkDzTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception;

    /**
     * 申购风控校验
     * @param faNewStock
     * @throws Exception
     */
    void checkShengou(FaNewStock faNewStock) throws Exception;

    /**
     * 配售风控校验
     * @param faNewStock
     * @throws Exception
     */
    void checkPeiShou(FaNewStock faNewStock) throws Exception;

    /**
     * 申购交易时间校验
     * @throws Exception
     */
    void checkShengouTime() throws Exception;

    /**
     * 配售交易时间校验
     * @throws Exception
     */
    void checkPeiShouTime() throws Exception;

    /**
     * 充值风控校验
     * @param faRecharge
     * @throws Exception
     */
    void checkRecharge(FaRecharge faRecharge) throws Exception;

    /**
     * 提现风控校验
     * @param faWithdraw
     * @throws Exception
     */
    void checkWithdraw(FaWithdraw faWithdraw) throws Exception;

    /**
     * 融券交易风控校验
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    void checkRqTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception;

    /**
     * 预约重组风控校验
     * @param faStockTrading
     * @throws Exception
     */
    void checkYyczTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception;

    /**
     * 判断今天是否节假日
     * @return
     * @throws Exception
     */
    boolean isHolidayToday() throws Exception;
}