package com.ruoyi.web.controller.biz.strategy;

import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
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
 * 策略Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "策略")
@RestController
@RequestMapping("/biz/strategy")
public class FaStrategyController extends BaseController
{
    @Autowired
    private IFaStrategyService faStrategyService;

    /**
     * 查询策略列表
     */
    @ApiOperation("查询策略列表")
    @PreAuthorize("@ss.hasPermi('biz:strategy:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaStrategy faStrategy)
    {
        startPage();
        List<FaStrategy> list = faStrategyService.selectFaStrategyList(faStrategy);
        return getDataTable(list);
    }

    /**
     * 导出策略列表
     */
    @ApiOperation("导出策略列表")
    @PreAuthorize("@ss.hasPermi('biz:strategy:export')")
    @Log(title = "策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaStrategy faStrategy)
    {
        List<FaStrategy> list = faStrategyService.selectFaStrategyList(faStrategy);
        ExcelUtil<FaStrategy> util = new ExcelUtil<FaStrategy>(FaStrategy.class);
        util.exportExcel(response, list, "策略数据");
    }

    /**
     * 获取策略详细信息
     */
    @ApiOperation("获取策略详细信息")
    @PreAuthorize("@ss.hasPermi('biz:strategy:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faStrategyService.selectFaStrategyById(id));
    }

    /**
     * 新增策略
     */
    @ApiOperation("新增策略")
    @PreAuthorize("@ss.hasPermi('biz:strategy:add')")
    @Log(title = "策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaStrategy faStrategy)
    {
        return toAjax(faStrategyService.insertFaStrategy(faStrategy));
    }

    /**
     * 修改策略
     */
    @ApiOperation("修改策略")
    @PreAuthorize("@ss.hasPermi('biz:strategy:edit')")
    @Log(title = "策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaStrategy faStrategy)
    {
        return toAjax(faStrategyService.updateFaStrategy(faStrategy));
    }

    /**
     * 删除策略
     */
    @ApiOperation("删除策略")
    @PreAuthorize("@ss.hasPermi('biz:strategy:remove')")
    @Log(title = "策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faStrategyService.deleteFaStrategyByIds(ids));
    }

    /**
     * 搜索股票
     */
    @ApiOperation("搜索股票")
    @Log(title = "搜索股票", businessType = BusinessType.OTHER)
    @PostMapping("/searchStock")
    public AjaxResult searchStock(@RequestBody FaStrategy faStrategy)
    {
        try {
            List<FaStrategy> faStrategyList = faStrategyService.searchStock(faStrategy);
            return AjaxResult.success(faStrategyList);
        } catch (ServiceException e) {
            logger.error("searchStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("searchStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改股票状态
     */
    @ApiOperation("修改股票状态")
    @PreAuthorize("@ss.hasPermi('biz:strategy:changeStockStatus')")
    @Log(title = "修改股票状态", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStockStatus")
    public AjaxResult changeStockStatus(@RequestBody FaStrategy faStrategy)
    {
        try {
            return faStrategyService.changeStockStatus(faStrategy);
        } catch (ServiceException e) {
            logger.error("changeStockStatus", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeStockStatus", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改预约重组
     */
    @ApiOperation("修改预约重组")
    @PreAuthorize("@ss.hasPermi('biz:strategy:changeStockStatus')")
    @Log(title = "修改预约重组", businessType = BusinessType.UPDATE)
    @PostMapping("/submitFormYycz")
    public AjaxResult submitFormYycz(@RequestBody FaStrategy faStrategy)
    {
        try {
            return faStrategyService.submitFormYycz(faStrategy);
        } catch (ServiceException e) {
            logger.error("submitFormYycz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitFormYycz", e);
            return AjaxResult.error();
        }
    }

}
