package com.ruoyi.biz.exchangeConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;

import java.util.List;

/**
 * 交易所配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-03-14
 */
public interface FaExchangeConfigMapper extends BaseMapper<FaExchangeConfig>
{
    /**
     * 查询交易所配置
     *
     * @param id 交易所配置主键
     * @return 交易所配置
     */
    public FaExchangeConfig selectFaExchangeConfigById(Integer id);

    /**
     * 查询交易所配置列表
     *
     * @param faExchangeConfig 交易所配置
     * @return 交易所配置集合
     */
    public List<FaExchangeConfig> selectFaExchangeConfigList(FaExchangeConfig faExchangeConfig);

    /**
     * 新增交易所配置
     *
     * @param faExchangeConfig 交易所配置
     * @return 结果
     */
    public int insertFaExchangeConfig(FaExchangeConfig faExchangeConfig);

    /**
     * 修改交易所配置
     *
     * @param faExchangeConfig 交易所配置
     * @return 结果
     */
    public int updateFaExchangeConfig(FaExchangeConfig faExchangeConfig);

    /**
     * 删除交易所配置
     *
     * @param id 交易所配置主键
     * @return 结果
     */
    public int deleteFaExchangeConfigById(Integer id);

    /**
     * 批量删除交易所配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaExchangeConfigByIds(Integer[] ids);
}