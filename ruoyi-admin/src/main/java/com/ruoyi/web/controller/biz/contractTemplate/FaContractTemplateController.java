package com.ruoyi.web.controller.biz.contractTemplate;

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
import com.ruoyi.biz.contractTemplate.domain.FaContractTemplate;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同模板Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "合同模板")
@RestController
@RequestMapping("/biz/contractTemplate")
public class FaContractTemplateController extends BaseController
{
    @Autowired
    private IFaContractTemplateService faContractTemplateService;

    /**
     * 查询合同模板列表
     */
    @ApiOperation("查询合同模板列表")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaContractTemplate faContractTemplate)
    {
        startPage();
        List<FaContractTemplate> list = faContractTemplateService.selectFaContractTemplateList(faContractTemplate);
        return getDataTable(list);
    }

    /**
     * 导出合同模板列表
     */
    @ApiOperation("导出合同模板列表")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:export')")
    @Log(title = "合同模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaContractTemplate faContractTemplate)
    {
        List<FaContractTemplate> list = faContractTemplateService.selectFaContractTemplateList(faContractTemplate);
        ExcelUtil<FaContractTemplate> util = new ExcelUtil<FaContractTemplate>(FaContractTemplate.class);
        util.exportExcel(response, list, "合同模板数据");
    }

    /**
     * 获取合同模板详细信息
     */
    @ApiOperation("获取合同模板详细信息")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faContractTemplateService.selectFaContractTemplateById(id));
    }

    /**
     * 新增合同模板
     */
    @ApiOperation("新增合同模板")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:add')")
    @Log(title = "合同模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaContractTemplate faContractTemplate)
    {
        return toAjax(faContractTemplateService.insertFaContractTemplate(faContractTemplate));
    }

    /**
     * 修改合同模板
     */
    @ApiOperation("修改合同模板")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:edit')")
    @Log(title = "合同模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaContractTemplate faContractTemplate)
    {
        return toAjax(faContractTemplateService.updateFaContractTemplate(faContractTemplate));
    }

    /**
     * 删除合同模板
     */
    @ApiOperation("删除合同模板")
    @PreAuthorize("@ss.hasPermi('biz:contractTemplate:remove')")
    @Log(title = "合同模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faContractTemplateService.deleteFaContractTemplateByIds(ids));
    }
}
