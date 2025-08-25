package com.ruoyi.biz.holidayConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.holidayConfig.domain.FaHolidayConfig;
import com.ruoyi.biz.holidayConfig.mapper.FaHolidayConfigMapper;
import com.ruoyi.biz.holidayConfig.service.IFaHolidayConfigService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hutool.core.date.LocalDateTimeUtil.isWeekend;

/**
 * 节假日配置Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-12
 */
@Service
public class FaHolidayConfigServiceImpl extends ServiceImpl<FaHolidayConfigMapper, FaHolidayConfig> implements IFaHolidayConfigService
{
    @Autowired
    private FaHolidayConfigMapper faHolidayConfigMapper;

    /**
     * 查询节假日配置
     *
     * @param id 节假日配置主键
     * @return 节假日配置
     */
    @Override
    public FaHolidayConfig selectFaHolidayConfigById(Long id)
    {
        return faHolidayConfigMapper.selectFaHolidayConfigById(id);
    }

    /**
     * 查询节假日配置列表
     *
     * @param faHolidayConfig 节假日配置
     * @return 节假日配置
     */
    @Override
    public List<FaHolidayConfig> selectFaHolidayConfigList(FaHolidayConfig faHolidayConfig)
    {
        faHolidayConfig.setDeleteFlag(0);
        return faHolidayConfigMapper.selectFaHolidayConfigList(faHolidayConfig);
    }

    /**
     * 新增节假日配置
     *
     * @param faHolidayConfig 节假日配置
     * @return 结果
     */
    @Override
    public int insertFaHolidayConfig(FaHolidayConfig faHolidayConfig)
    {
        faHolidayConfig.setCreateTime(DateUtils.getNowDate());
        return faHolidayConfigMapper.insertFaHolidayConfig(faHolidayConfig);
    }

    /**
     * 修改节假日配置
     *
     * @param faHolidayConfig 节假日配置
     * @return 结果
     */
    @Override
    public int updateFaHolidayConfig(FaHolidayConfig faHolidayConfig)
    {
        faHolidayConfig.setUpdateTime(DateUtils.getNowDate());
        return faHolidayConfigMapper.updateFaHolidayConfig(faHolidayConfig);
    }

    /**
     * 批量删除节假日配置
     *
     * @param ids 需要删除的节假日配置主键
     * @return 结果
     */
    @Override
    public int deleteFaHolidayConfigByIds(Long[] ids)
    {
        return faHolidayConfigMapper.deleteFaHolidayConfigByIds(ids);
    }

    /**
     * 删除节假日配置信息
     *
     * @param id 节假日配置主键
     * @return 结果
     */
    @Override
    public int deleteFaHolidayConfigById(Long id)
    {
        return faHolidayConfigMapper.deleteFaHolidayConfigById(id);
    }

    /**
     * 判断节假日
     * @throws Exception
     */
    @Override
    public Boolean isHoliday() throws Exception {
        // 判断是否周末
        if (isWeekend(LocalDateTime.now())) {
            return true;
        }

        // 判断是否节日
        int count = faHolidayConfigMapper.isHoliday();
        if (count > 0) {
            return true;
        }
        return false;
    }

}