package com.ruoyi.biz.newsCatalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;
import com.ruoyi.biz.newsCatalog.mapper.FaNewsCatalogMapper;
import com.ruoyi.biz.newsCatalog.service.IFaNewsCatalogService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻栏目Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Service
public class FaNewsCatalogServiceImpl extends ServiceImpl<FaNewsCatalogMapper, FaNewsCatalog> implements IFaNewsCatalogService
{
    @Autowired
    private FaNewsCatalogMapper faNewsCatalogMapper;

    /**
     * 查询新闻栏目
     *
     * @param id 新闻栏目主键
     * @return 新闻栏目
     */
    @Override
    public FaNewsCatalog selectFaNewsCatalogById(Long id)
    {
        return faNewsCatalogMapper.selectFaNewsCatalogById(id);
    }

    /**
     * 查询新闻栏目列表
     *
     * @param faNewsCatalog 新闻栏目
     * @return 新闻栏目
     */
    @Override
    public List<FaNewsCatalog> selectFaNewsCatalogList(FaNewsCatalog faNewsCatalog)
    {
        faNewsCatalog.setDeleteFlag(0);
        return faNewsCatalogMapper.selectFaNewsCatalogList(faNewsCatalog);
    }

    /**
     * 新增新闻栏目
     *
     * @param faNewsCatalog 新闻栏目
     * @return 结果
     */
    @Override
    public int insertFaNewsCatalog(FaNewsCatalog faNewsCatalog)
    {
        faNewsCatalog.setCreateTime(DateUtils.getNowDate());
        return faNewsCatalogMapper.insertFaNewsCatalog(faNewsCatalog);
    }

    /**
     * 修改新闻栏目
     *
     * @param faNewsCatalog 新闻栏目
     * @return 结果
     */
    @Override
    public int updateFaNewsCatalog(FaNewsCatalog faNewsCatalog)
    {
        faNewsCatalog.setUpdateTime(DateUtils.getNowDate());
        return faNewsCatalogMapper.updateFaNewsCatalog(faNewsCatalog);
    }

    /**
     * 批量删除新闻栏目
     *
     * @param ids 需要删除的新闻栏目主键
     * @return 结果
     */
    @Override
    public int deleteFaNewsCatalogByIds(Long[] ids)
    {
        return faNewsCatalogMapper.deleteFaNewsCatalogByIds(ids);
    }

    /**
     * 删除新闻栏目信息
     *
     * @param id 新闻栏目主键
     * @return 结果
     */
    @Override
    public int deleteFaNewsCatalogById(Long id)
    {
        return faNewsCatalogMapper.deleteFaNewsCatalogById(id);
    }

    /**
     * 查询新闻栏目
     */
    @Override
    public List<FaNewsCatalog> getNewsCatalog() throws Exception {
        LambdaQueryWrapper<FaNewsCatalog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewsCatalog::getDeleteFlag, 0);
        lambdaQueryWrapper.eq(FaNewsCatalog::getStatus, 0);
        List<FaNewsCatalog> list = this.list(lambdaQueryWrapper);
        return list;
    }
}