package com.ruoyi.biz.banner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.banner.domain.FaBanner;
import com.ruoyi.biz.banner.mapper.FaBannerMapper;
import com.ruoyi.biz.banner.service.IFaBannerService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Service
public class FaBannerServiceImpl extends ServiceImpl<FaBannerMapper, FaBanner> implements IFaBannerService
{
    @Autowired
    private FaBannerMapper faBannerMapper;

    /**
     * 查询轮播图
     *
     * @param id 轮播图主键
     * @return 轮播图
     */
    @Override
    public FaBanner selectFaBannerById(Integer id)
    {
        return faBannerMapper.selectFaBannerById(id);
    }

    /**
     * 查询轮播图列表
     *
     * @param faBanner 轮播图
     * @return 轮播图
     */
    @Override
    public List<FaBanner> selectFaBannerList(FaBanner faBanner)
    {
        faBanner.setDeleteFlag(0);
        return faBannerMapper.selectFaBannerList(faBanner);
    }

    /**
     * 新增轮播图
     *
     * @param faBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertFaBanner(FaBanner faBanner)
    {
        faBanner.setCreateTime(DateUtils.getNowDate());
        return faBannerMapper.insertFaBanner(faBanner);
    }

    /**
     * 修改轮播图
     *
     * @param faBanner 轮播图
     * @return 结果
     */
    @Override
    public int updateFaBanner(FaBanner faBanner)
    {
        faBanner.setUpdateTime(DateUtils.getNowDate());
        return faBannerMapper.updateFaBanner(faBanner);
    }

    /**
     * 批量删除轮播图
     *
     * @param ids 需要删除的轮播图主键
     * @return 结果
     */
    @Override
    public int deleteFaBannerByIds(Integer[] ids)
    {
        return faBannerMapper.deleteFaBannerByIds(ids);
    }

    /**
     * 删除轮播图信息
     *
     * @param id 轮播图主键
     * @return 结果
     */
    @Override
    public int deleteFaBannerById(Integer id)
    {
        return faBannerMapper.deleteFaBannerById(id);
    }

    /**
     * 查询轮播图
     * @return
     * @throws Exception
     */
    @Override
    public List<FaBanner> getBanner() throws Exception {
        LambdaQueryWrapper<FaBanner> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBanner::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByAsc(FaBanner::getSort);
        lambdaQueryWrapper.orderByDesc(FaBanner::getCreateTime);
        List<FaBanner> faBannerList = this.list(lambdaQueryWrapper);
        return faBannerList;
    }

}