package com.ruoyi.biz.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.biz.news.domain.FaNews;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-16
 */
public interface FaNewsMapper extends BaseMapper<FaNews>
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
     * 删除新闻
     *
     * @param id 新闻主键
     * @return 结果
     */
    public int deleteFaNewsById(Long id);

    /**
     * 批量删除新闻
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaNewsByIds(Long[] ids);

    /**
     * 查询新闻列表
     * @param catalogId
     * @return
     * @throws Exception
     */
    IPage<FaNews> getNews(Page<FaNews> page, @Param("catalogId") Integer catalogId) throws Exception;

    /**
     * 删除5天以上的新闻
     * @throws Exception
     */
    void deleteOldNews() throws Exception;

    /**
     * 删除定时任务日志
     * @throws Exception
     */
    void deleteOldJobLog() throws Exception;
}