package com.ruoyi.web.controller.biz.banner;

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
import com.ruoyi.biz.banner.domain.FaBanner;
import com.ruoyi.biz.banner.service.IFaBannerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 轮播图Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "轮播图")
@RestController
@RequestMapping("/biz/banner")
public class FaBannerController extends BaseController
{
    @Autowired
    private IFaBannerService faBannerService;

    /**
     * 查询轮播图列表
     */
    @ApiOperation("查询轮播图列表")
    @PreAuthorize("@ss.hasPermi('biz:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBanner faBanner)
    {
        startPage();
        List<FaBanner> list = faBannerService.selectFaBannerList(faBanner);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @ApiOperation("导出轮播图列表")
    @PreAuthorize("@ss.hasPermi('biz:banner:export')")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBanner faBanner)
    {
        List<FaBanner> list = faBannerService.selectFaBannerList(faBanner);
        ExcelUtil<FaBanner> util = new ExcelUtil<FaBanner>(FaBanner.class);
        util.exportExcel(response, list, "轮播图数据");
    }

    /**
     * 获取轮播图详细信息
     */
    @ApiOperation("获取轮播图详细信息")
    @PreAuthorize("@ss.hasPermi('biz:banner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBannerService.selectFaBannerById(id));
    }

    /**
     * 新增轮播图
     */
    @ApiOperation("新增轮播图")
    @PreAuthorize("@ss.hasPermi('biz:banner:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBanner faBanner)
    {
        return toAjax(faBannerService.insertFaBanner(faBanner));
    }

    /**
     * 修改轮播图
     */
    @ApiOperation("修改轮播图")
    @PreAuthorize("@ss.hasPermi('biz:banner:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBanner faBanner)
    {
        return toAjax(faBannerService.updateFaBanner(faBanner));
    }

    /**
     * 删除轮播图
     */
    @ApiOperation("删除轮播图")
    @PreAuthorize("@ss.hasPermi('biz:banner:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBannerService.deleteFaBannerByIds(ids));
    }
}
