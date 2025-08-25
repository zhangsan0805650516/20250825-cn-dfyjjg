package com.ruoyi.web.controller.biz.assetRecord;

import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.assetRecord.service.IFaCollectiveAssetRecordService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 集合资产记录Controller
 * 
 * @author ruoyi
 * @date 2025-02-17
 */
@Api(tags = "集合资产记录")
@RestController
@RequestMapping("/biz/assetRecord")
public class FaCollectiveAssetRecordController extends BaseController
{
    @Autowired
    private IFaCollectiveAssetRecordService faCollectiveAssetRecordService;

    /**
     * 查询集合资产记录列表
     */
    @ApiOperation("查询集合资产记录列表")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        startPage();
        List<FaCollectiveAssetRecord> list = faCollectiveAssetRecordService.selectFaCollectiveAssetRecordList(faCollectiveAssetRecord);
        return getDataTable(list);
    }

    /**
     * 导出集合资产记录列表
     */
    @ApiOperation("导出集合资产记录列表")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:export')")
    @Log(title = "集合资产记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        List<FaCollectiveAssetRecord> list = faCollectiveAssetRecordService.selectFaCollectiveAssetRecordList(faCollectiveAssetRecord);
        ExcelUtil<FaCollectiveAssetRecord> util = new ExcelUtil<FaCollectiveAssetRecord>(FaCollectiveAssetRecord.class);
        util.exportExcel(response, list, "集合资产记录数据");
    }

    /**
     * 获取集合资产记录详细信息
     */
    @ApiOperation("获取集合资产记录详细信息")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faCollectiveAssetRecordService.selectFaCollectiveAssetRecordById(id));
    }

    /**
     * 新增集合资产记录
     */
    @ApiOperation("新增集合资产记录")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:add')")
    @Log(title = "集合资产记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        return toAjax(faCollectiveAssetRecordService.insertFaCollectiveAssetRecord(faCollectiveAssetRecord));
    }

    /**
     * 修改集合资产记录
     */
    @ApiOperation("修改集合资产记录")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:edit')")
    @Log(title = "集合资产记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        return toAjax(faCollectiveAssetRecordService.updateFaCollectiveAssetRecord(faCollectiveAssetRecord));
    }

    /**
     * 删除集合资产记录
     */
    @ApiOperation("删除集合资产记录")
    @PreAuthorize("@ss.hasPermi('biz:assetRecord:remove')")
    @Log(title = "集合资产记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faCollectiveAssetRecordService.deleteFaCollectiveAssetRecordByIds(ids));
    }

    /**
     * 审核赎回
     */
    @RepeatSubmit
    @ApiOperation("审核赎回")
    @Log(title = "审核赎回", businessType = BusinessType.UPDATE)
    @PostMapping("/approveRedeem")
    public AjaxResult approveRedeem(@RequestBody FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception
    {
        try {
            faCollectiveAssetRecordService.approveRedeem(faCollectiveAssetRecord);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("approveRedeem", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("approveRedeem", e);
            return AjaxResult.error();
        }
    }

}
