package com.ruoyi.biz.sysbank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.sysbank.domain.FaSysbank;

import java.util.List;

/**
 * 通道Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface FaSysbankMapper extends BaseMapper<FaSysbank>
{
    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    public FaSysbank selectFaSysbankById(Long id);

    /**
     * 查询通道列表
     *
     * @param faSysbank 通道
     * @return 通道集合
     */
    public List<FaSysbank> selectFaSysbankList(FaSysbank faSysbank);

    /**
     * 新增通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    public int insertFaSysbank(FaSysbank faSysbank);

    /**
     * 修改通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    public int updateFaSysbank(FaSysbank faSysbank);

    /**
     * 删除通道
     *
     * @param id 通道主键
     * @return 结果
     */
    public int deleteFaSysbankById(Long id);

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaSysbankByIds(Long[] ids);
}