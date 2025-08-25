package com.ruoyi.biz.riskConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.riskConfig.domain.FaRiskConfig;

import java.util.List;

/**
 * 风控设置Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface FaRiskConfigMapper extends BaseMapper<FaRiskConfig>
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
     * @param name 风控设置name
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
     * 删除风控设置
     *
     * @param id 风控设置主键
     * @return 结果
     */
    public int deleteFaRiskConfigById(Integer id);

    /**
     * 批量删除风控设置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaRiskConfigByIds(Integer[] ids);
}