package com.ruoyi.web.controller.biz.adv;

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
import com.ruoyi.biz.adv.domain.FaAdv;
import com.ruoyi.biz.adv.service.IFaAdvService;
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
@RequestMapping("/biz/adv")
public class FaAdvController extends BaseController
{
    @Autowired
    private IFaAdvService faAdvService;

    /**
     * 查询广告图列表
     */
    @ApiOperation("查询广告图列表")
    @PreAuthorize("@ss.hasPermi('biz:adv:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaAdv faAdv)
    {
        startPage();
        List<FaAdv> list = faAdvService.selectFaAdvList(faAdv);
        return getDataTable(list);
    }

    /**
     * 导出广告图列表
     */
    @ApiOperation("导出广告图列表")
    @PreAuthorize("@ss.hasPermi('biz:adv:export')")
    @Log(title = "广告图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaAdv faAdv)
    {
        List<FaAdv> list = faAdvService.selectFaAdvList(faAdv);
        ExcelUtil<FaAdv> util = new ExcelUtil<FaAdv>(FaAdv.class);
        util.exportExcel(response, list, "广告图数据");
    }

    /**
     * 获取广告图详细信息
     */
    @ApiOperation("获取广告图详细信息")
    @PreAuthorize("@ss.hasPermi('biz:adv:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faAdvService.selectFaAdvById(id));
    }

    /**
     * 新增广告图
     */
    @ApiOperation("新增广告图")
    @PreAuthorize("@ss.hasPermi('biz:adv:add')")
    @Log(title = "广告图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaAdv faAdv)
    {
        return toAjax(faAdvService.insertFaAdv(faAdv));
    }

    /**
     * 修改广告图
     */
    @ApiOperation("修改广告图")
    @PreAuthorize("@ss.hasPermi('biz:adv:edit')")
    @Log(title = "广告图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaAdv faAdv)
    {
        return toAjax(faAdvService.updateFaAdv(faAdv));
    }

    /**
     * 删除广告图
     */
    @ApiOperation("删除广告图")
    @PreAuthorize("@ss.hasPermi('biz:adv:remove')")
    @Log(title = "广告图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faAdvService.deleteFaAdvByIds(ids));
    }
}
