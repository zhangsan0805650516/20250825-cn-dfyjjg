package com.ruoyi.biz.holidayConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.holidayConfig.domain.FaHolidayConfig;

import java.util.List;

/**
 * 节假日配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-03-12
 */
public interface FaHolidayConfigMapper extends BaseMapper<FaHolidayConfig>
{
    /**
     * 查询节假日配置
     *
     * @param id 节假日配置主键
     * @return 节假日配置
     */
    public FaHolidayConfig selectFaHolidayConfigById(Long id);

    /**
     * 查询节假日配置列表
     *
     * @param faHolidayConfig 节假日配置
     * @return 节假日配置集合
     */
    public List<FaHolidayConfig> selectFaHolidayConfigList(FaHolidayConfig faHolidayConfig);

    /**
     * 新增节假日配置
     *
     * @param faHolidayConfig 节假日配置
     * @return 结果
     */
    public int insertFaHolidayConfig(FaHolidayConfig faHolidayConfig);

    /**
     * 修改节假日配置
     *
     * @param faHolidayConfig 节假日配置
     * @return 结果
     */
    public int updateFaHolidayConfig(FaHolidayConfig faHolidayConfig);

    /**
     * 删除节假日配置
     *
     * @param id 节假日配置主键
     * @return 结果
     */
    public int deleteFaHolidayConfigById(Long id);

    /**
     * 批量删除节假日配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaHolidayConfigByIds(Long[] ids);

    /**
     * 判断是否节日
     * @return
     * @throws Exception
     */
    int isHoliday() throws Exception;

}