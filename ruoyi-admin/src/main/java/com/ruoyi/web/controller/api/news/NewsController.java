package com.ruoyi.web.controller.api.news;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.biz.news.domain.FaNews;
import com.ruoyi.biz.news.service.IFaNewsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻Controller
 * 
 * @author ruoyi
 * @date 2024-01-16
 */
@Api(tags = "新闻")
@RestController
@RequestMapping("/api/news")
public class NewsController extends BaseController
{

    @Autowired
    private IFaNewsService faNewsService;

    /**
     * 查询新闻列表
     */
    @ApiOperation("查询新闻列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "catalogId", value = "栏目类型(1国内经济 2国际经济 3证券要闻 4公司资讯 5产经资讯 6纵深调查)", dataType = "Integer")
            })
    @PostMapping("/getNews")
    public AjaxResult getNews(@RequestBody FaNews faNews)
    {
        try {
            if (null == faNews.getPage()) {
                faNews.setPage(1);
            }
            if (null == faNews.getSize()) {
                faNews.setSize(10);
            }
            IPage<FaNews> newsIPage = faNewsService.getNews(faNews);
            return AjaxResult.success(newsIPage);
        } catch (ServiceException e) {
            logger.error("getNews", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getNews", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询新闻详情
     */
    @ApiOperation("查询新闻详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新闻id", required = true, dataType = "Integer")
    })
    @PostMapping("/getNewsDetail")
    public AjaxResult getNewsDetail(@RequestBody FaNews faNews)
    {
        try {
            faNews = faNewsService.getNewsDetail(faNews);
            if (StringUtils.isNotEmpty(faNews.getNewsContent())) {
                // 替换图片地址
                Document doc = Jsoup.parse(faNews.getNewsContent());
                Elements imgs = doc.select("img");
                if (!imgs.isEmpty()) {
                    for (Element img : imgs) {
                        String src = img.attr("src");
                        if (!src.contains(".aspx?")) {
                            src = "https://images.weserv.nl/?url=" + src;
                        }
                        img.attr("src", src);
                    }
                }
                faNews.setNewsContent(doc.toString());
            }
            return AjaxResult.success(faNews);
        } catch (ServiceException e) {
            logger.error("getNewsDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getNewsDetail", e);
            return AjaxResult.error();
        }
    }

}
