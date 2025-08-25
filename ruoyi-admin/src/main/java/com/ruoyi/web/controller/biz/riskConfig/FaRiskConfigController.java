package com.ruoyi.web.controller.biz.riskConfig;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.MessageUtils;
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
import com.ruoyi.biz.riskConfig.domain.FaRiskConfig;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 风控设置Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "风控设置")
@RestController
@RequestMapping("/biz/riskConfig")
public class FaRiskConfigController extends BaseController
{
    @Autowired
    private IFaRiskConfigService faRiskConfigService;

    /**
     * 查询风控设置列表
     */
    @ApiOperation("查询风控设置列表")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaRiskConfig faRiskConfig)
    {
        startPage();
        List<FaRiskConfig> list = faRiskConfigService.selectFaRiskConfigList(faRiskConfig);
        return getDataTable(list);
    }

    /**
     * 导出风控设置列表
     */
    @ApiOperation("导出风控设置列表")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:export')")
    @Log(title = "风控设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaRiskConfig faRiskConfig)
    {
        List<FaRiskConfig> list = faRiskConfigService.selectFaRiskConfigList(faRiskConfig);
        ExcelUtil<FaRiskConfig> util = new ExcelUtil<FaRiskConfig>(FaRiskConfig.class);
        util.exportExcel(response, list, "风控设置数据");
    }

    /**
     * 获取风控设置详细信息
     */
    @ApiOperation("获取风控设置详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faRiskConfigService.selectFaRiskConfigById(id));
    }

    /**
     * 获取风控设置详细信息
     */
    @ApiOperation("获取风控设置详细信息")
    @PostMapping(value = "/getInfoByName")
    public AjaxResult getInfoByName(@RequestBody FaRiskConfig faRiskConfig)
    {

        FaRiskConfig config = faRiskConfigService.selectFaRiskConfigByName(faRiskConfig.getName());

        return success(config);
    }

    /**
     * 获取风控设置网站标题
     */
    @GetMapping(value = "/getTitle")
    public AjaxResult getTitle()
    {
        return success(faRiskConfigService.selectFaRiskConfigByName("name"));
    }

    /**
     * 获取风控设置网站logo
     */
    @GetMapping(value = "/getLogo")
    public AjaxResult getLogo()
    {
        return success(faRiskConfigService.selectFaRiskConfigByName("applogo"));
    }

    /**
     * 新增风控设置
     */
    @ApiOperation("新增风控设置")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:add')")
    @Log(title = "风控设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaRiskConfig faRiskConfig)
    {
        return toAjax(faRiskConfigService.insertFaRiskConfig(faRiskConfig));
    }

    /**
     * 修改风控设置
     */
    @ApiOperation("修改风控设置")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:edit')")
    @Log(title = "风控设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaRiskConfig faRiskConfig)
    {
        return toAjax(faRiskConfigService.updateFaRiskConfig(faRiskConfig));
    }

    /**
     * 删除风控设置
     */
    @ApiOperation("删除风控设置")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:remove')")
    @Log(title = "风控设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faRiskConfigService.deleteFaRiskConfigByIds(ids));
    }

    /**
     * 获取字典分类
     */
    @ApiOperation("获取字典分类")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:query')")
    @Log(title = "获取字典分类", businessType = BusinessType.OTHER)
    @PostMapping("/getConfiggroup")
    public AjaxResult getConfiggroup()
    {
        try {
            List<FaRiskConfig> list = faRiskConfigService.getConfiggroup();
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("getConfiggroup", e);
            return AjaxResult.error();
        }
    }

    /**
     * 根据分类获取字典列表
     */
    @ApiOperation("根据分类获取字典列表")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:query')")
    @Log(title = "根据分类获取字典列表", businessType = BusinessType.OTHER)
    @PostMapping("/getConfigListByGroup")
    public AjaxResult getConfigListByGroup(@RequestBody FaRiskConfig faRiskConfig)
    {
        try {
            List<FaRiskConfig> list = faRiskConfigService.getConfigListByGroup(faRiskConfig);
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("getConfigListByGroup", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改风控设置列表
     */
    @ApiOperation("修改风控设置列表")
    @PreAuthorize("@ss.hasPermi('biz:riskConfig:edit')")
    @Log(title = "修改风控设置列表", businessType = BusinessType.UPDATE)
    @PostMapping("/updateRiskConfigList")
    public AjaxResult updateRiskConfigList(@RequestBody List<FaRiskConfig> faRiskConfigList)
    {
        try {
            faRiskConfigService.updateRiskConfigList(faRiskConfigList);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("updateRiskConfigList", e);
            return AjaxResult.error();
        }
    }

}
