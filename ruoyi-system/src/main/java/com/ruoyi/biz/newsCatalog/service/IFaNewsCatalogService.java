package com.ruoyi.biz.newsCatalog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;

import java.util.List;

/**
 * 新闻栏目Service接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface IFaNewsCatalogService extends IService<FaNewsCatalog>
{
    /**
     * 查询新闻栏目
     *
     * @param id 新闻栏目主键
     * @return 新闻栏目
     */
    public FaNewsCatalog selectFaNewsCatalogById(Long id);

    /**
     * 查询新闻栏目列表
     *
     * @param faNewsCatalog 新闻栏目
     * @return 新闻栏目集合
     */
    public List<FaNewsCatalog> selectFaNewsCatalogList(FaNewsCatalog faNewsCatalog);

    /**
     * 新增新闻栏目
     *
     * @param faNewsCatalog 新闻栏目
     * @return 结果
     */
    public int insertFaNewsCatalog(FaNewsCatalog faNewsCatalog);

    /**
     * 修改新闻栏目
     *
     * @param faNewsCatalog 新闻栏目
     * @return 结果
     */
    public int updateFaNewsCatalog(FaNewsCatalog faNewsCatalog);

    /**
     * 批量删除新闻栏目
     *
     * @param ids 需要删除的新闻栏目主键集合
     * @return 结果
     */
    public int deleteFaNewsCatalogByIds(Long[] ids);

    /**
     * 删除新闻栏目信息
     *
     * @param id 新闻栏目主键
     * @return 结果
     */
    public int deleteFaNewsCatalogById(Long id);

    /**
     * 查询新闻栏目
     */
    List<FaNewsCatalog> getNewsCatalog() throws Exception;
}