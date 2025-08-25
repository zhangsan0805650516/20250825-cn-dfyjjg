package com.ruoyi.web.controller.biz.news;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.ruoyi.biz.news.domain.FaNews;
import com.ruoyi.biz.news.service.IFaNewsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 新闻Controller
 * 
 * @author ruoyi
 * @date 2024-01-16
 */
@Api(tags = "新闻")
@RestController
@RequestMapping("/biz/news")
public class FaNewsController extends BaseController
{
    @Autowired
    private IFaNewsService faNewsService;

    /**
     * 查询新闻列表
     */
    @ApiOperation("查询新闻列表")
    @PreAuthorize("@ss.hasPermi('biz:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaNews faNews)
    {
        startPage();
        List<FaNews> list = faNewsService.selectFaNewsList(faNews);
        if (!list.isEmpty()) {
            for (FaNews news : list) {
                news.setNewsImage("https://images.weserv.nl/?url=" + news.getNewsImage());
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出新闻列表
     */
    @ApiOperation("导出新闻列表")
    @PreAuthorize("@ss.hasPermi('biz:news:export')")
    @Log(title = "新闻", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaNews faNews)
    {
        List<FaNews> list = faNewsService.selectFaNewsList(faNews);
        ExcelUtil<FaNews> util = new ExcelUtil<FaNews>(FaNews.class);
        util.exportExcel(response, list, "新闻数据");
    }

    /**
     * 获取新闻详细信息
     */
    @ApiOperation("获取新闻详细信息")
    @PreAuthorize("@ss.hasPermi('biz:news:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        FaNews faNews = faNewsService.selectFaNewsById(id);
        if (StringUtils.isNotEmpty(faNews.getNewsContent())) {
            faNews.setNewsContent(faNews.getNewsContent().replace("<img src=\"", "<img src=\"https://images.weserv.nl/?url="));
        }
        return success(faNews);
    }

    /**
     * 新增新闻
     */
    @ApiOperation("新增新闻")
    @PreAuthorize("@ss.hasPermi('biz:news:add')")
    @Log(title = "新闻", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaNews faNews)
    {
        return toAjax(faNewsService.insertFaNews(faNews));
    }

    /**
     * 修改新闻
     */
    @ApiOperation("修改新闻")
    @PreAuthorize("@ss.hasPermi('biz:news:edit')")
    @Log(title = "新闻", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaNews faNews)
    {
        return toAjax(faNewsService.updateFaNews(faNews));
    }

    /**
     * 删除新闻
     */
    @ApiOperation("删除新闻")
    @PreAuthorize("@ss.hasPermi('biz:news:remove')")
    @Log(title = "新闻", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faNewsService.deleteFaNewsByIds(ids));
    }
}
