package com.ruoyi.web.controller.biz.holidayConfig;

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
import com.ruoyi.biz.holidayConfig.domain.FaHolidayConfig;
import com.ruoyi.biz.holidayConfig.service.IFaHolidayConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 节假日配置Controller
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@Api(tags = "节假日配置")
@RestController
@RequestMapping("/biz/holidayConfig")
public class FaHolidayConfigController extends BaseController
{
    @Autowired
    private IFaHolidayConfigService faHolidayConfigService;

    /**
     * 查询节假日配置列表
     */
    @ApiOperation("查询节假日配置列表")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaHolidayConfig faHolidayConfig)
    {
        startPage();
        List<FaHolidayConfig> list = faHolidayConfigService.selectFaHolidayConfigList(faHolidayConfig);
        return getDataTable(list);
    }

    /**
     * 导出节假日配置列表
     */
    @ApiOperation("导出节假日配置列表")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:export')")
    @Log(title = "节假日配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaHolidayConfig faHolidayConfig)
    {
        List<FaHolidayConfig> list = faHolidayConfigService.selectFaHolidayConfigList(faHolidayConfig);
        ExcelUtil<FaHolidayConfig> util = new ExcelUtil<FaHolidayConfig>(FaHolidayConfig.class);
        util.exportExcel(response, list, "节假日配置数据");
    }

    /**
     * 获取节假日配置详细信息
     */
    @ApiOperation("获取节假日配置详细信息")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faHolidayConfigService.selectFaHolidayConfigById(id));
    }

    /**
     * 新增节假日配置
     */
    @ApiOperation("新增节假日配置")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:add')")
    @Log(title = "节假日配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaHolidayConfig faHolidayConfig)
    {
        return toAjax(faHolidayConfigService.insertFaHolidayConfig(faHolidayConfig));
    }

    /**
     * 修改节假日配置
     */
    @ApiOperation("修改节假日配置")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:edit')")
    @Log(title = "节假日配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaHolidayConfig faHolidayConfig)
    {
        return toAjax(faHolidayConfigService.updateFaHolidayConfig(faHolidayConfig));
    }

    /**
     * 删除节假日配置
     */
    @ApiOperation("删除节假日配置")
    @PreAuthorize("@ss.hasPermi('biz:holidayConfig:remove')")
    @Log(title = "节假日配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faHolidayConfigService.deleteFaHolidayConfigByIds(ids));
    }
}
