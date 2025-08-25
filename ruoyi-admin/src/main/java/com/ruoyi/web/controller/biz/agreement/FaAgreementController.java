package com.ruoyi.web.controller.biz.agreement;

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
import com.ruoyi.biz.agreement.domain.FaAgreement;
import com.ruoyi.biz.agreement.service.IFaAgreementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 关闭通道协议Controller
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
@Api(tags = "关闭通道协议")
@RestController
@RequestMapping("/biz/agreement")
public class FaAgreementController extends BaseController
{
    @Autowired
    private IFaAgreementService faAgreementService;

    /**
     * 查询关闭通道协议列表
     */
    @ApiOperation("查询关闭通道协议列表")
    @PreAuthorize("@ss.hasPermi('biz:agreement:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaAgreement faAgreement)
    {
        startPage();
        List<FaAgreement> list = faAgreementService.selectFaAgreementList(faAgreement);
        return getDataTable(list);
    }

    /**
     * 导出关闭通道协议列表
     */
    @ApiOperation("导出关闭通道协议列表")
    @PreAuthorize("@ss.hasPermi('biz:agreement:export')")
    @Log(title = "关闭通道协议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaAgreement faAgreement)
    {
        List<FaAgreement> list = faAgreementService.selectFaAgreementList(faAgreement);
        ExcelUtil<FaAgreement> util = new ExcelUtil<FaAgreement>(FaAgreement.class);
        util.exportExcel(response, list, "关闭通道协议数据");
    }

    /**
     * 获取关闭通道协议详细信息
     */
    @ApiOperation("获取关闭通道协议详细信息")
    @PreAuthorize("@ss.hasPermi('biz:agreement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faAgreementService.selectFaAgreementById(id));
    }

    /**
     * 新增关闭通道协议
     */
    @ApiOperation("新增关闭通道协议")
    @PreAuthorize("@ss.hasPermi('biz:agreement:add')")
    @Log(title = "关闭通道协议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaAgreement faAgreement)
    {
        return toAjax(faAgreementService.insertFaAgreement(faAgreement));
    }

    /**
     * 修改关闭通道协议
     */
    @ApiOperation("修改关闭通道协议")
    @PreAuthorize("@ss.hasPermi('biz:agreement:edit')")
    @Log(title = "关闭通道协议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaAgreement faAgreement)
    {
        return toAjax(faAgreementService.updateFaAgreement(faAgreement));
    }

    /**
     * 删除关闭通道协议
     */
    @ApiOperation("删除关闭通道协议")
    @PreAuthorize("@ss.hasPermi('biz:agreement:remove')")
    @Log(title = "关闭通道协议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faAgreementService.deleteFaAgreementByIds(ids));
    }

    /**
     * 获取数据
     */
    @ApiOperation("获取数据")
    @Log(title = "获取数据", businessType = BusinessType.OTHER)
    @PostMapping("/getAgreementData")
    public AjaxResult getAgreementData()
    {
        try {
            return faAgreementService.getAgreementData();
        } catch (Exception e) {
            logger.error("getAgreementData", e);
            return AjaxResult.error();
        }
    }

}
