package com.ruoyi.biz.nav.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.nav.domain.FaNav;

import java.util.List;

/**
 * 导航图标Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface FaNavMapper extends BaseMapper<FaNav>
{
    /**
     * 查询导航图标
     *
     * @param id 导航图标主键
     * @return 导航图标
     */
    public FaNav selectFaNavById(Integer id);

    /**
     * 查询导航图标列表
     *
     * @param faNav 导航图标
     * @return 导航图标集合
     */
    public List<FaNav> selectFaNavList(FaNav faNav);

    /**
     * 新增导航图标
     *
     * @param faNav 导航图标
     * @return 结果
     */
    public int insertFaNav(FaNav faNav);

    /**
     * 修改导航图标
     *
     * @param faNav 导航图标
     * @return 结果
     */
    public int updateFaNav(FaNav faNav);

    /**
     * 删除导航图标
     *
     * @param id 导航图标主键
     * @return 结果
     */
    public int deleteFaNavById(Integer id);

    /**
     * 批量删除导航图标
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaNavByIds(Integer[] ids);
}