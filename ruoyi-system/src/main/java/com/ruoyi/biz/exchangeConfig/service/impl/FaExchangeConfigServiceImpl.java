package com.ruoyi.biz.exchangeConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.exchangeConfig.mapper.FaExchangeConfigMapper;
import com.ruoyi.biz.exchangeConfig.service.IFaExchangeConfigService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易所配置Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-14
 */
@Service
public class FaExchangeConfigServiceImpl extends ServiceImpl<FaExchangeConfigMapper, FaExchangeConfig> implements IFaExchangeConfigService
{
    @Autowired
    private FaExchangeConfigMapper faExchangeConfigMapper;

    /**
     * 查询交易所配置
     *
     * @param id 交易所配置主键
     * @return 交易所配置
     */
    @Override
    public FaExchangeConfig selectFaExchangeConfigById(Integer id)
    {
        return faExchangeConfigMapper.selectFaExchangeConfigById(id);
    }

    /**
     * 查询交易所配置列表
     *
     * @param faExchangeConfig 交易所配置
     * @return 交易所配置
     */
    @Override
    public List<FaExchangeConfig> selectFaExchangeConfigList(FaExchangeConfig faExchangeConfig)
    {
        faExchangeConfig.setDeleteFlag(0);
        return faExchangeConfigMapper.selectFaExchangeConfigList(faExchangeConfig);
    }

    /**
     * 新增交易所配置
     *
     * @param faExchangeConfig 交易所配置
     * @return 结果
     */
    @Override
    public int insertFaExchangeConfig(FaExchangeConfig faExchangeConfig)
    {
        faExchangeConfig.setCreateTime(DateUtils.getNowDate());
        return faExchangeConfigMapper.insertFaExchangeConfig(faExchangeConfig);
    }

    /**
     * 修改交易所配置
     *
     * @param faExchangeConfig 交易所配置
     * @return 结果
     */
    @Override
    public int updateFaExchangeConfig(FaExchangeConfig faExchangeConfig)
    {
        faExchangeConfig.setUpdateTime(DateUtils.getNowDate());
        return faExchangeConfigMapper.updateFaExchangeConfig(faExchangeConfig);
    }

    /**
     * 批量删除交易所配置
     *
     * @param ids 需要删除的交易所配置主键
     * @return 结果
     */
    @Override
    public int deleteFaExchangeConfigByIds(Integer[] ids)
    {
        return faExchangeConfigMapper.deleteFaExchangeConfigByIds(ids);
    }

    /**
     * 删除交易所配置信息
     *
     * @param id 交易所配置主键
     * @return 结果
     */
    @Override
    public int deleteFaExchangeConfigById(Integer id)
    {
        return faExchangeConfigMapper.deleteFaExchangeConfigById(id);
    }

    /**
     * 获取交易所列表
     * @return
     * @throws Exception
     */
    @Override
    public List<FaExchangeConfig> getExchangeList() throws Exception {
        LambdaQueryWrapper<FaExchangeConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByAsc(FaExchangeConfig::getId);
        List<FaExchangeConfig> list = this.list(lambdaQueryWrapper);
        return list;
    }

}