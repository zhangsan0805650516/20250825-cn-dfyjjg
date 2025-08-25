package com.ruoyi.web.controller.biz.attention;

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
import com.ruoyi.biz.attention.domain.FaAttention;
import com.ruoyi.biz.attention.service.IFaAttentionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告图Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "广告图")
@RestController
@RequestMapping("/biz/attention")
public class FaAttentionController extends BaseController
{
    @Autowired
    private IFaAttentionService faAttentionService;

    /**
     * 查询广告图列表
     */
    @ApiOperation("查询广告图列表")
    @PreAuthorize("@ss.hasPermi('biz:attention:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaAttention faAttention)
    {
        startPage();
        List<FaAttention> list = faAttentionService.selectFaAttentionList(faAttention);
        return getDataTable(list);
    }

    /**
     * 导出广告图列表
     */
    @ApiOperation("导出广告图列表")
    @PreAuthorize("@ss.hasPermi('biz:attention:export')")
    @Log(title = "广告图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaAttention faAttention)
    {
        List<FaAttention> list = faAttentionService.selectFaAttentionList(faAttention);
        ExcelUtil<FaAttention> util = new ExcelUtil<FaAttention>(FaAttention.class);
        util.exportExcel(response, list, "广告图数据");
    }

    /**
     * 获取广告图详细信息
     */
    @ApiOperation("获取广告图详细信息")
    @PreAuthorize("@ss.hasPermi('biz:attention:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faAttentionService.selectFaAttentionById(id));
    }

    /**
     * 新增广告图
     */
    @ApiOperation("新增广告图")
    @PreAuthorize("@ss.hasPermi('biz:attention:add')")
    @Log(title = "广告图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaAttention faAttention)
    {
        return toAjax(faAttentionService.insertFaAttention(faAttention));
    }

    /**
     * 修改广告图
     */
    @ApiOperation("修改广告图")
    @PreAuthorize("@ss.hasPermi('biz:attention:edit')")
    @Log(title = "广告图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaAttention faAttention)
    {
        return toAjax(faAttentionService.updateFaAttention(faAttention));
    }

    /**
     * 删除广告图
     */
    @ApiOperation("删除广告图")
    @PreAuthorize("@ss.hasPermi('biz:attention:remove')")
    @Log(title = "广告图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faAttentionService.deleteFaAttentionByIds(ids));
    }
}
