package com.ruoyi.biz.shengou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.shengou.domain.FaNewStock;

import java.util.List;

/**
 * 新股列表Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface FaShengouMapper extends BaseMapper<FaNewStock>
{
    /**
     * 查询新股列表
     *
     * @param id 新股列表主键
     * @return 新股列表
     */
    public FaNewStock selectFaShengouById(Long id);

    /**
     * 查询新股列表列表
     *
     * @param faNewStock 新股列表
     * @return 新股列表集合
     */
    public List<FaNewStock> selectFaShengouList(FaNewStock faNewStock);

    /**
     * 新增新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    public int insertFaShengou(FaNewStock faNewStock);

    /**
     * 修改新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    public int updateFaShengou(FaNewStock faNewStock);

    /**
     * 删除新股列表
     *
     * @param id 新股列表主键
     * @return 结果
     */
    public int deleteFaShengouById(Long id);

    /**
     * 批量删除新股列表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaShengouByIds(Long[] ids);
}