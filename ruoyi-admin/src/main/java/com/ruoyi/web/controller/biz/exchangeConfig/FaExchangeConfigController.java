package com.ruoyi.web.controller.biz.exchangeConfig;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.exception.ServiceException;
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
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.exchangeConfig.service.IFaExchangeConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 交易所配置Controller
 * 
 * @author ruoyi
 * @date 2024-03-14
 */
@Api(tags = "交易所配置")
@RestController
@RequestMapping("/biz/exchangeConfig")
public class FaExchangeConfigController extends BaseController
{
    @Autowired
    private IFaExchangeConfigService faExchangeConfigService;

    /**
     * 查询交易所配置列表
     */
    @ApiOperation("查询交易所配置列表")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaExchangeConfig faExchangeConfig)
    {
        startPage();
        List<FaExchangeConfig> list = faExchangeConfigService.selectFaExchangeConfigList(faExchangeConfig);
        return getDataTable(list);
    }

    /**
     * 导出交易所配置列表
     */
    @ApiOperation("导出交易所配置列表")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:export')")
    @Log(title = "交易所配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaExchangeConfig faExchangeConfig)
    {
        List<FaExchangeConfig> list = faExchangeConfigService.selectFaExchangeConfigList(faExchangeConfig);
        ExcelUtil<FaExchangeConfig> util = new ExcelUtil<FaExchangeConfig>(FaExchangeConfig.class);
        util.exportExcel(response, list, "交易所配置数据");
    }

    /**
     * 获取交易所配置详细信息
     */
    @ApiOperation("获取交易所配置详细信息")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faExchangeConfigService.selectFaExchangeConfigById(id));
    }

    /**
     * 新增交易所配置
     */
    @ApiOperation("新增交易所配置")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:add')")
    @Log(title = "交易所配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaExchangeConfig faExchangeConfig)
    {
        return toAjax(faExchangeConfigService.insertFaExchangeConfig(faExchangeConfig));
    }

    /**
     * 修改交易所配置
     */
    @ApiOperation("修改交易所配置")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:edit')")
    @Log(title = "交易所配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaExchangeConfig faExchangeConfig)
    {
        return toAjax(faExchangeConfigService.updateFaExchangeConfig(faExchangeConfig));
    }

    /**
     * 删除交易所配置
     */
    @ApiOperation("删除交易所配置")
    @PreAuthorize("@ss.hasPermi('biz:exchangeConfig:remove')")
    @Log(title = "交易所配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faExchangeConfigService.deleteFaExchangeConfigByIds(ids));
    }

    /**
     * 获取交易所列表
     */
    @ApiOperation("获取交易所列表")
    @Log(title = "获取交易所列表", businessType = BusinessType.OTHER)
    @PostMapping("/getExchangeList")
    public AjaxResult getExchangeList()
    {
        try {
            List<FaExchangeConfig> list = faExchangeConfigService.getExchangeList();
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getExchangeList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getExchangeList", e);
            return AjaxResult.error();
        }
    }

}
