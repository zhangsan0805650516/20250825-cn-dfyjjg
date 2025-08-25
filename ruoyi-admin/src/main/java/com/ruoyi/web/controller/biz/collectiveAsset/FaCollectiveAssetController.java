package com.ruoyi.web.controller.biz.collectiveAsset;

import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;
import com.ruoyi.biz.collectiveAsset.service.IFaCollectiveAssetService;
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
 * 集合资产Controller
 * 
 * @author ruoyi
 * @date 2025-02-17
 */
@Api(tags = "集合资产")
@RestController
@RequestMapping("/biz/collectiveAsset")
public class FaCollectiveAssetController extends BaseController
{
    @Autowired
    private IFaCollectiveAssetService faCollectiveAssetService;

    /**
     * 查询集合资产列表
     */
    @ApiOperation("查询集合资产列表")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaCollectiveAsset faCollectiveAsset)
    {
        startPage();
        List<FaCollectiveAsset> list = faCollectiveAssetService.selectFaCollectiveAssetList(faCollectiveAsset);
        return getDataTable(list);
    }

    /**
     * 导出集合资产列表
     */
    @ApiOperation("导出集合资产列表")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:export')")
    @Log(title = "集合资产", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaCollectiveAsset faCollectiveAsset)
    {
        List<FaCollectiveAsset> list = faCollectiveAssetService.selectFaCollectiveAssetList(faCollectiveAsset);
        ExcelUtil<FaCollectiveAsset> util = new ExcelUtil<FaCollectiveAsset>(FaCollectiveAsset.class);
        util.exportExcel(response, list, "集合资产数据");
    }

    /**
     * 获取集合资产详细信息
     */
    @ApiOperation("获取集合资产详细信息")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faCollectiveAssetService.selectFaCollectiveAssetById(id));
    }

    /**
     * 新增集合资产
     */
    @ApiOperation("新增集合资产")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:add')")
    @Log(title = "集合资产", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaCollectiveAsset faCollectiveAsset)
    {
        return toAjax(faCollectiveAssetService.insertFaCollectiveAsset(faCollectiveAsset));
    }

    /**
     * 修改集合资产
     */
    @ApiOperation("修改集合资产")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:edit')")
    @Log(title = "集合资产", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaCollectiveAsset faCollectiveAsset)
    {
        return toAjax(faCollectiveAssetService.updateFaCollectiveAsset(faCollectiveAsset));
    }

    /**
     * 删除集合资产
     */
    @ApiOperation("删除集合资产")
    @PreAuthorize("@ss.hasPermi('biz:collectiveAsset:remove')")
    @Log(title = "集合资产", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faCollectiveAssetService.deleteFaCollectiveAssetByIds(ids));
    }

    /**
     * 集合资产开始
     */
    @RepeatSubmit
    @ApiOperation("集合资产开始")
    @Log(title = "集合资产开始", businessType = BusinessType.UPDATE)
    @PostMapping("/startAsset")
    public AjaxResult startAsset(@RequestBody FaCollectiveAsset faCollectiveAsset) throws Exception
    {
        try {
            faCollectiveAssetService.startAsset(faCollectiveAsset);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("startAsset", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("startAsset", e);
            return AjaxResult.error();
        }
    }

    /**
     * 集合资产结束
     */
    @RepeatSubmit
    @ApiOperation("集合资产结束")
    @Log(title = "集合资产结束", businessType = BusinessType.UPDATE)
    @PostMapping("/endAsset")
    public AjaxResult endAsset(@RequestBody FaCollectiveAsset faCollectiveAsset) throws Exception
    {
        try {
            faCollectiveAssetService.endAsset(faCollectiveAsset);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("endAsset", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("endAsset", e);
            return AjaxResult.error();
        }
    }

}
