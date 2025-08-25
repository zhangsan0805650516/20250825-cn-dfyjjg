package com.ruoyi.web.controller.biz.shengou;

import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 新股列表Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "新股列表")
@RestController
@RequestMapping("/biz/shengou")
public class FaShengouController extends BaseController
{
    @Autowired
    private IFaShengouService faShengouService;

    /**
     * 查询新股列表列表
     */
    @ApiOperation("查询新股列表列表")
    @PreAuthorize("@ss.hasPermi('biz:shengou:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaNewStock faNewStock)
    {
        startPage();
        List<FaNewStock> list = faShengouService.selectFaShengouList(faNewStock);
        return getDataTable(list);
    }

    /**
     * 导出新股列表列表
     */
    @ApiOperation("导出新股列表列表")
    @PreAuthorize("@ss.hasPermi('biz:shengou:export')")
    @Log(title = "新股列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaNewStock faNewStock)
    {
        List<FaNewStock> list = faShengouService.selectFaShengouList(faNewStock);
        ExcelUtil<FaNewStock> util = new ExcelUtil<FaNewStock>(FaNewStock.class);
        util.exportExcel(response, list, "新股列表数据");
    }

    /**
     * 获取新股列表详细信息
     */
    @ApiOperation("获取新股列表详细信息")
    @PreAuthorize("@ss.hasPermi('biz:shengou:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faShengouService.selectFaShengouById(id));
    }

    /**
     * 新增新股列表
     */
    @ApiOperation("新增新股列表")
    @PreAuthorize("@ss.hasPermi('biz:shengou:add')")
    @Log(title = "新股列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaNewStock faNewStock)
    {
        return toAjax(faShengouService.insertFaShengou(faNewStock));
    }

    /**
     * 修改新股列表
     */
    @ApiOperation("修改新股列表")
    @PreAuthorize("@ss.hasPermi('biz:shengou:edit')")
    @Log(title = "新股列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaNewStock faNewStock)
    {
        return toAjax(faShengouService.updateFaShengou(faNewStock));
    }

    /**
     * 删除新股列表
     */
    @ApiOperation("删除新股列表")
    @PreAuthorize("@ss.hasPermi('biz:shengou:remove')")
    @Log(title = "新股列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faShengouService.deleteFaShengouByIds(ids));
    }

    /**
     * 修改申购配售开关
     */
    @ApiOperation("修改申购配售开关")
    @Log(title = "修改申购配售开关", businessType = BusinessType.UPDATE)
    @PostMapping("/changeSwitch")
    public AjaxResult changeSwitch(@RequestBody FaNewStock faNewStock)
    {
        try {
            faShengouService.changeSwitch(faNewStock);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("changeSwitch", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeSwitch", e);
            return AjaxResult.error();
        }
    }

    /**
     * 搜索新股
     */
    @ApiOperation("搜索新股")
    @Log(title = "搜索新股", businessType = BusinessType.OTHER)
    @PostMapping("/searchNewStock")
    public AjaxResult searchNewStock(@RequestBody FaNewStock faNewStock)
    {
        try {
            List<FaNewStock> faShengouList = faShengouService.searchNewStock(faNewStock);
            return AjaxResult.success(faShengouList);
        } catch (ServiceException e) {
            logger.error("searchNewStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("searchNewStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提交申购配售配置
     */
    @ApiOperation("提交申购配售配置")
    @Log(title = "提交申购配售配置", businessType = BusinessType.OTHER)
    @PostMapping("/submitExchange")
    public AjaxResult submitExchange(@RequestBody FaNewStock faNewStock)
    {
        try {
            faShengouService.submitExchange(faNewStock);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitExchange", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitExchange", e);
            return AjaxResult.error();
        }
    }

}
