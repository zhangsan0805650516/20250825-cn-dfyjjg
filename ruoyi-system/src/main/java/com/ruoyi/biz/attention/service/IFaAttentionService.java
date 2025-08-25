package com.ruoyi.biz.attention.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.attention.domain.FaAttention;

import java.util.List;

/**
 * 广告图Service接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface IFaAttentionService extends IService<FaAttention>
{
    /**
     * 查询广告图
     *
     * @param id 广告图主键
     * @return 广告图
     */
    public FaAttention selectFaAttentionById(Integer id);

    /**
     * 查询广告图列表
     *
     * @param faAttention 广告图
     * @return 广告图集合
     */
    public List<FaAttention> selectFaAttentionList(FaAttention faAttention);

    /**
     * 新增广告图
     *
     * @param faAttention 广告图
     * @return 结果
     */
    public int insertFaAttention(FaAttention faAttention);

    /**
     * 修改广告图
     *
     * @param faAttention 广告图
     * @return 结果
     */
    public int updateFaAttention(FaAttention faAttention);

    /**
     * 批量删除广告图
     *
     * @param ids 需要删除的广告图主键集合
     * @return 结果
     */
    public int deleteFaAttentionByIds(Integer[] ids);

    /**
     * 删除广告图信息
     *
     * @param id 广告图主键
     * @return 结果
     */
    public int deleteFaAttentionById(Integer id);
}