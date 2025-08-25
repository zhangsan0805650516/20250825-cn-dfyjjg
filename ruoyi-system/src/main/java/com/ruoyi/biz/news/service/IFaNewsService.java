package com.ruoyi.biz.news.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.news.domain.FaNews;

import java.util.List;

/**
 * 新闻Service接口
 *
 * @author ruoyi
 * @date 2024-01-16
 */
public interface IFaNewsService extends IService<FaNews>
{
    /**
     * 查询新闻
     *
     * @param id 新闻主键
     * @return 新闻
     */
    public FaNews selectFaNewsById(Long id);

    /**
     * 查询新闻列表
     *
     * @param faNews 新闻
     * @return 新闻集合
     */
    public List<FaNews> selectFaNewsList(FaNews faNews);

    /**
     * 新增新闻
     *
     * @param faNews 新闻
     * @return 结果
     */
    public int insertFaNews(FaNews faNews);

    /**
     * 修改新闻
     *
     * @param faNews 新闻
     * @return 结果
     */
    public int updateFaNews(FaNews faNews);

    /**
     * 批量删除新闻
     *
     * @param ids 需要删除的新闻主键集合
     * @return 结果
     */
    public int deleteFaNewsByIds(Long[] ids);

    /**
     * 删除新闻信息
     *
     * @param id 新闻主键
     * @return 结果
     */
    public int deleteFaNewsById(Long id);

    /**
     * 刷新A股新闻
     * @throws Exception
     */
    void updateNews() throws Exception;

    /**
     * 查询新闻列表
     * @param faNews
     * @return
     * @throws Exception
     */
    IPage<FaNews> getNews(FaNews faNews) throws Exception;

    /**
     * 查询新闻详情
     * @param faNews
     * @return
     * @throws Exception
     */
    FaNews getNewsDetail(FaNews faNews) throws Exception;
}