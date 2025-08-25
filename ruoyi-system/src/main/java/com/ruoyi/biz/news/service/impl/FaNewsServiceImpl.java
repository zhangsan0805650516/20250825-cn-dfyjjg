package com.ruoyi.biz.news.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.news.domain.FaNews;
import com.ruoyi.biz.news.mapper.FaNewsMapper;
import com.ruoyi.biz.news.service.IFaNewsService;
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;
import com.ruoyi.biz.newsCatalog.service.IFaNewsCatalogService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 新闻Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-16
 */
@Service
public class FaNewsServiceImpl extends ServiceImpl<FaNewsMapper, FaNews> implements IFaNewsService
{

    private static final Logger log = LoggerFactory.getLogger(FaNewsServiceImpl.class);
    
    @Autowired
    private FaNewsMapper faNewsMapper;

    @Autowired
    private IFaNewsCatalogService iFaNewsCatalogService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    private static String NEWS_LIST_URL = Constants.QI_ZHANG_URL + "/news_list?types={0}&page={1}&pagesize=10&token={2}";

    private static String NEWS_CONTENT_URL = Constants.QI_ZHANG_URL + "/news_content?news_id={0}&token={1}";

    private static String INDIA_NEWS_URL = "https://apibs.business-standard.com/search/?type=news&limit=10&page={0}&keyword=news";

    /**
     * 查询新闻
     *
     * @param id 新闻主键
     * @return 新闻
     */
    @Override
    public FaNews selectFaNewsById(Long id)
    {
        return faNewsMapper.selectFaNewsById(id);
    }

    /**
     * 查询新闻列表
     *
     * @param faNews 新闻
     * @return 新闻
     */
    @Override
    public List<FaNews> selectFaNewsList(FaNews faNews)
    {
        faNews.setDeleteFlag(0);
        return faNewsMapper.selectFaNewsList(faNews);
    }

    /**
     * 新增新闻
     *
     * @param faNews 新闻
     * @return 结果
     */
    @Override
    public int insertFaNews(FaNews faNews)
    {
        faNews.setCreateTime(DateUtils.getNowDate());
        return faNewsMapper.insertFaNews(faNews);
    }

    /**
     * 修改新闻
     *
     * @param faNews 新闻
     * @return 结果
     */
    @Override
    public int updateFaNews(FaNews faNews)
    {
        faNews.setUpdateTime(DateUtils.getNowDate());
        return faNewsMapper.updateFaNews(faNews);
    }

    /**
     * 批量删除新闻
     *
     * @param ids 需要删除的新闻主键
     * @return 结果
     */
    @Override
    public int deleteFaNewsByIds(Long[] ids)
    {
        return faNewsMapper.deleteFaNewsByIds(ids);
    }

    /**
     * 删除新闻信息
     *
     * @param id 新闻主键
     * @return 结果
     */
    @Override
    public int deleteFaNewsById(Long id)
    {
        return faNewsMapper.deleteFaNewsById(id);
    }

    /**
     * 刷新A股新闻
     * @throws Exception
     */
    @Override
    public void updateNews() throws Exception {
        // 接口token
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        // 刷新新闻栏目 获取新闻id
        updateNewsByCatalog(token);
        // 根据新闻id刷新新闻内容
        updateNewsByNewsId(token);
        // 删除5天以上的新闻
        faNewsMapper.deleteOldNews();
        // 删除定时任务日志
        faNewsMapper.deleteOldJobLog();
    }

    /**
     * 刷新新闻栏目 获取新闻id 一个栏目最多刷新100条
     * @throws Exception
     */
    private void updateNewsByCatalog(String token) throws Exception {
        List<FaNewsCatalog> catalogList = iFaNewsCatalogService.list();
        if (!catalogList.isEmpty()) {
            for (FaNewsCatalog faNewsCatalog : catalogList) {
                int page = 1;
                int count = 0;
                // 按栏目刷新
                updateNews(faNewsCatalog.getId(), page, count, token);
            }
        }
    }

    /**
     * 按栏目刷新新闻
     * @param type
     * @param page
     * @param count
     * @throws Exception
     */
    private void updateNews(int type, int page, int count, String token) throws Exception {
        if (count > 100) {
            return;
        }
        boolean done = false;
        // 按栏目获取新闻列表
        String result = HttpUtils.sendGet(MessageFormat.format(NEWS_LIST_URL, type, page, token));
        JSONArray jsonArray = JSON.parseArray(result);
        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("news_id") &&
                        StringUtils.isNotEmpty(jsonObject.getString("news_id"))) {
                    // 判断新闻是否已存入数据库
                    String newsId = jsonObject.getString("news_id");
                    LambdaQueryWrapper<FaNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(FaNews::getNewsId, newsId);
                    FaNews faNews = this.getOne(lambdaQueryWrapper);
                    // 没有存入数据库 保存
                    if (ObjectUtils.isEmpty(faNews)) {
                        faNews = new FaNews();
                        faNews.setCatalogId(type);
                        faNews.setNewsId(jsonObject.getString("news_id"));
                        faNews.setNewsTitle(jsonObject.getString("news_title"));
                        faNews.setNewsImage(jsonObject.getString("news_image"));
                        // 有图无图，展示方式不一样
                        if (StringUtils.isNotEmpty(faNews.getNewsImage())) {
                            faNews.setShowMode(4);
                        } else {
                            faNews.setShowMode(6);
                        }
                        faNews.setNewsTime(jsonObject.getDate("news_time"));
                        faNews.setCreateTime(new Date());
                        faNews.setDeleteFlag(0);
                        faNews.setIsDone(0);
                        faNewsMapper.insertFaNews(faNews);
                        // 计数
                        count++;
                    }
                    // 新闻id已存在 没有新的新闻 跳出循环
                    else {
                        done = true;
                        break;
                    }
                }
            }
            // 继续刷新 获取下一页
            if (!done) {
                page++;
                updateNews(type, page, count, token);
            }
        }
    }

    /**
     * 根据新闻id刷新新闻内容
     * @throws Exception
     */
    private void updateNewsByNewsId(String token) throws Exception {
        // 取出未刷新的新闻id
        LambdaQueryWrapper<FaNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNews::getIsDone, 0);
        lambdaQueryWrapper.orderByDesc(FaNews::getId);
        lambdaQueryWrapper.last(" limit 100 ");
        List<FaNews> faNewsList = this.list(lambdaQueryWrapper);
        if (!faNewsList.isEmpty()) {
            for (FaNews faNews : faNewsList) {
                // 根据新闻id获取新闻内容
                String result = HttpUtils.sendGet(MessageFormat.format(NEWS_CONTENT_URL, faNews.getNewsId(), token));
                JSONArray jsonArray = JSON.parseArray(result);
                if (!jsonArray.isEmpty()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("news_abstract") &&
                            jsonObject.containsKey("news_content")) {
                        faNews.setNewsAbstract(jsonObject.getString("news_abstract"));
                        faNews.setNewsContent(cleanContent(jsonObject.getString("news_content")));
                        faNews.setIsDone(1);
                        faNews.setUpdateTime(new Date());
                        faNewsMapper.updateFaNews(faNews);
                    }
                }
            }
        }
    }

    private String cleanContent(String content) {
        try {
            String imgSrc = content.substring(0, content.indexOf("em_handle_adv_close") + 23);
            imgSrc = imgSrc.substring(imgSrc.lastIndexOf("img") - 1);
            content = content.replace(imgSrc, "");
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
    }

    /**
     * 查询新闻列表
     * @param faNews
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaNews> getNews(FaNews faNews) throws Exception {
        IPage<FaNews> faNewsIPage = faNewsMapper.getNews(new Page<>(faNews.getPage(), faNews.getSize()), faNews.getCatalogId());
        return faNewsIPage;
    }

    /**
     * 查询新闻详情
     * @param faNews
     * @return
     * @throws Exception
     */
    @Override
    public FaNews getNewsDetail(FaNews faNews) throws Exception {
        if (null == faNews.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faNews = this.getById(faNews.getId());
        if (ObjectUtils.isEmpty(faNews)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        return faNews;
    }

//    public static void main(String[] args) {
//        Date date = new Date("Wed, 06 Mar 2024 18:35:00 +0530");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        log.info(sdf1.format(date));
//    }

}