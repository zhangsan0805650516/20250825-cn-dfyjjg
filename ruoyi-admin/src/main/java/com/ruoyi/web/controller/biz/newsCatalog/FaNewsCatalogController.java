package com.ruoyi.web.controller.biz.newsCatalog;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;
import com.ruoyi.biz.newsCatalog.service.IFaNewsCatalogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 新闻栏目Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "新闻栏目")
@RestController
@RequestMapping("/biz/newsCatalog")
public class FaNewsCatalogController extends BaseController
{
    @Autowired
    private IFaNewsCatalogService faNewsCatalogService;

    /**
     * 查询新闻栏目列表
     */
    @ApiOperation("查询新闻栏目列表")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaNewsCatalog faNewsCatalog)
    {
        startPage();
        List<FaNewsCatalog> list = faNewsCatalogService.selectFaNewsCatalogList(faNewsCatalog);
        return getDataTable(list);
    }

    /**
     * 导出新闻栏目列表
     */
    @ApiOperation("导出新闻栏目列表")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:export')")
    @Log(title = "新闻栏目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaNewsCatalog faNewsCatalog)
    {
        List<FaNewsCatalog> list = faNewsCatalogService.selectFaNewsCatalogList(faNewsCatalog);
        ExcelUtil<FaNewsCatalog> util = new ExcelUtil<FaNewsCatalog>(FaNewsCatalog.class);
        util.exportExcel(response, list, "新闻栏目数据");
    }

    /**
     * 获取新闻栏目详细信息
     */
    @ApiOperation("获取新闻栏目详细信息")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faNewsCatalogService.selectFaNewsCatalogById(id));
    }

    /**
     * 新增新闻栏目
     */
    @ApiOperation("新增新闻栏目")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:add')")
    @Log(title = "新闻栏目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaNewsCatalog faNewsCatalog)
    {
        return toAjax(faNewsCatalogService.insertFaNewsCatalog(faNewsCatalog));
    }

    /**
     * 修改新闻栏目
     */
    @ApiOperation("修改新闻栏目")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:edit')")
    @Log(title = "新闻栏目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaNewsCatalog faNewsCatalog)
    {
        return toAjax(faNewsCatalogService.updateFaNewsCatalog(faNewsCatalog));
    }

    /**
     * 删除新闻栏目
     */
    @ApiOperation("删除新闻栏目")
    @PreAuthorize("@ss.hasPermi('biz:newsCatalog:remove')")
    @Log(title = "新闻栏目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faNewsCatalogService.deleteFaNewsCatalogByIds(ids));
    }
}
