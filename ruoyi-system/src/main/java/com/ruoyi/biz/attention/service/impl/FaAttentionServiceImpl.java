package com.ruoyi.biz.attention.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.attention.domain.FaAttention;
import com.ruoyi.biz.attention.mapper.FaAttentionMapper;
import com.ruoyi.biz.attention.service.IFaAttentionService;
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
public class FaAttentionServiceImpl extends ServiceImpl<FaAttentionMapper, FaAttention> implements IFaAttentionService
{
    @Autowired
    private FaAttentionMapper faAttentionMapper;

    /**
     * 查询广告图
     *
     * @param id 广告图主键
     * @return 广告图
     */
    @Override
    public FaAttention selectFaAttentionById(Integer id)
    {
        return faAttentionMapper.selectFaAttentionById(id);
    }

    /**
     * 查询广告图列表
     *
     * @param faAttention 广告图
     * @return 广告图
     */
    @Override
    public List<FaAttention> selectFaAttentionList(FaAttention faAttention)
    {
        faAttention.setDeleteFlag(0);
        return faAttentionMapper.selectFaAttentionList(faAttention);
    }

    /**
     * 新增广告图
     *
     * @param faAttention 广告图
     * @return 结果
     */
    @Override
    public int insertFaAttention(FaAttention faAttention)
    {
        faAttention.setCreateTime(DateUtils.getNowDate());
        return faAttentionMapper.insertFaAttention(faAttention);
    }

    /**
     * 修改广告图
     *
     * @param faAttention 广告图
     * @return 结果
     */
    @Override
    public int updateFaAttention(FaAttention faAttention)
    {
        faAttention.setUpdateTime(DateUtils.getNowDate());
        return faAttentionMapper.updateFaAttention(faAttention);
    }

    /**
     * 批量删除广告图
     *
     * @param ids 需要删除的广告图主键
     * @return 结果
     */
    @Override
    public int deleteFaAttentionByIds(Integer[] ids)
    {
        return faAttentionMapper.deleteFaAttentionByIds(ids);
    }

    /**
     * 删除广告图信息
     *
     * @param id 广告图主键
     * @return 结果
     */
    @Override
    public int deleteFaAttentionById(Integer id)
    {
        return faAttentionMapper.deleteFaAttentionById(id);
    }
}