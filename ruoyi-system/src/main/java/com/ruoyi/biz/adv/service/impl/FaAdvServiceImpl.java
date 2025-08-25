package com.ruoyi.biz.adv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.adv.domain.FaAdv;
import com.ruoyi.biz.adv.mapper.FaAdvMapper;
import com.ruoyi.biz.adv.service.IFaAdvService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广告图Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Service
public class FaAdvServiceImpl extends ServiceImpl<FaAdvMapper, FaAdv> implements IFaAdvService
{
    @Autowired
    private FaAdvMapper faAdvMapper;

    /**
     * 查询广告图
     *
     * @param id 广告图主键
     * @return 广告图
     */
    @Override
    public FaAdv selectFaAdvById(Integer id)
    {
        return faAdvMapper.selectFaAdvById(id);
    }

    /**
     * 查询广告图列表
     *
     * @param faAdv 广告图
     * @return 广告图
     */
    @Override
    public List<FaAdv> selectFaAdvList(FaAdv faAdv)
    {
        faAdv.setDeleteFlag(0);
        return faAdvMapper.selectFaAdvList(faAdv);
    }

    /**
     * 新增广告图
     *
     * @param faAdv 广告图
     * @return 结果
     */
    @Override
    public int insertFaAdv(FaAdv faAdv)
    {
        faAdv.setCreateTime(DateUtils.getNowDate());
        return faAdvMapper.insertFaAdv(faAdv);
    }

    /**
     * 修改广告图
     *
     * @param faAdv 广告图
     * @return 结果
     */
    @Override
    public int updateFaAdv(FaAdv faAdv)
    {
        faAdv.setUpdateTime(DateUtils.getNowDate());
        return faAdvMapper.updateFaAdv(faAdv);
    }

    /**
     * 批量删除广告图
     *
     * @param ids 需要删除的广告图主键
     * @return 结果
     */
    @Override
    public int deleteFaAdvByIds(Integer[] ids)
    {
        return faAdvMapper.deleteFaAdvByIds(ids);
    }

    /**
     * 删除广告图信息
     *
     * @param id 广告图主键
     * @return 结果
     */
    @Override
    public int deleteFaAdvById(Integer id)
    {
        return faAdvMapper.deleteFaAdvById(id);
    }

    /**
     * 查询广告图
     * @return
     * @throws Exception
     */
    @Override
    public List<FaAdv> getAdv() throws Exception {
        LambdaQueryWrapper<FaAdv> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaAdv::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByAsc(FaAdv::getSort);
        lambdaQueryWrapper.orderByDesc(FaAdv::getCreateTime);
        List<FaAdv> faAdvList = this.list(lambdaQueryWrapper);
        return faAdvList;
    }
}