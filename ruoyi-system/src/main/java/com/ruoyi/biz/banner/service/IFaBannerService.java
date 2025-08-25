package com.ruoyi.biz.banner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.banner.domain.FaBanner;

import java.util.List;

/**
 * 轮播图Service接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface IFaBannerService extends IService<FaBanner>
{
    /**
     * 查询轮播图
     *
     * @param id 轮播图主键
     * @return 轮播图
     */
    public FaBanner selectFaBannerById(Integer id);

    /**
     * 查询轮播图列表
     *
     * @param faBanner 轮播图
     * @return 轮播图集合
     */
    public List<FaBanner> selectFaBannerList(FaBanner faBanner);

    /**
     * 新增轮播图
     *
     * @param faBanner 轮播图
     * @return 结果
     */
    public int insertFaBanner(FaBanner faBanner);

    /**
     * 修改轮播图
     *
     * @param faBanner 轮播图
     * @return 结果
     */
    public int updateFaBanner(FaBanner faBanner);

    /**
     * 批量删除轮播图
     *
     * @param ids 需要删除的轮播图主键集合
     * @return 结果
     */
    public int deleteFaBannerByIds(Integer[] ids);

    /**
     * 删除轮播图信息
     *
     * @param id 轮播图主键
     * @return 结果
     */
    public int deleteFaBannerById(Integer id);

    /**
     * 查询轮播图
     * @return
     * @throws Exception
     */
    List<FaBanner> getBanner() throws Exception;
}