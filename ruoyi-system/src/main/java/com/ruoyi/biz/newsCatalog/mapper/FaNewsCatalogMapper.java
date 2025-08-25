package com.ruoyi.biz.newsCatalog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;

import java.util.List;

/**
 * 新闻栏目Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface FaNewsCatalogMapper extends BaseMapper<FaNewsCatalog>
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
     * 删除新闻栏目
     *
     * @param id 新闻栏目主键
     * @return 结果
     */
    public int deleteFaNewsCatalogById(Long id);

    /**
     * 批量删除新闻栏目
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaNewsCatalogByIds(Long[] ids);
}