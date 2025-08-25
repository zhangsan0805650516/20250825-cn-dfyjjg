package com.ruoyi.biz.nav.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.nav.domain.FaNav;

import java.util.List;

/**
 * 导航图标Service接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface IFaNavService extends IService<FaNav>
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
     * 批量删除导航图标
     *
     * @param ids 需要删除的导航图标主键集合
     * @return 结果
     */
    public int deleteFaNavByIds(Integer[] ids);

    /**
     * 删除导航图标信息
     *
     * @param id 导航图标主键
     * @return 结果
     */
    public int deleteFaNavById(Integer id);

    /**
     * 查询导航图标
     * @param faNav
     * @return
     * @throws Exception
     */
    List<FaNav> getNavList(FaNav faNav) throws Exception;

    /**
     * 显示隐藏开关修改
     * @param faNav
     * @throws Exception
     */
    void changeSwitch(FaNav faNav) throws Exception;
}