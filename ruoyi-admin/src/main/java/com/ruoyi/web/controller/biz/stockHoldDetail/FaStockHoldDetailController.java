package com.ruoyi.web.controller.biz.stockHoldDetail;

import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 持仓明细Controller
 * 
 * @author ruoyi
 * @date 2024-01-23
 */
@Api(tags = "持仓明细")
@RestController
@RequestMapping("/biz/stockHoldDetail")
public class FaStockHoldDetailController extends BaseController
{
    @Autowired
    private IFaStockHoldDetailService faStockHoldDetailService;

    /**
     * 查询持仓明细列表
     */
    @ApiOperation("查询持仓明细列表")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaStockHoldDetail faStockHoldDetail)
    {
        startPage();
        faStockHoldDetail.setDeleteFlag(1);
        LoginUser loginUser = getLoginUser();
        if (null != faStockHoldDetail.getDailiId()) {
            faStockHoldDetail.setParentId(Long.valueOf(faStockHoldDetail.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faStockHoldDetail.setParentId(1L);
            } else {
                faStockHoldDetail.setParentId(loginUser.getUserId());
            }
        }
        List<FaStockHoldDetail> list = faStockHoldDetailService.selectFaStockHoldDetailList(faStockHoldDetail);
        return getDataTable(list);
    }

    /**
     * 导出持仓明细列表
     */
    @ApiOperation("导出持仓明细列表")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:export')")
    @Log(title = "持仓明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaStockHoldDetail faStockHoldDetail)
    {
        List<FaStockHoldDetail> list = faStockHoldDetailService.selectFaStockHoldDetailList(faStockHoldDetail);
        ExcelUtil<FaStockHoldDetail> util = new ExcelUtil<FaStockHoldDetail>(FaStockHoldDetail.class);
        util.exportExcel(response, list, "持仓明细数据");
    }

    /**
     * 获取持仓明细详细信息
     */
    @ApiOperation("获取持仓明细详细信息")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faStockHoldDetailService.selectFaStockHoldDetailById(id));
    }

    /**
     * 新增持仓明细
     */
    @ApiOperation("新增持仓明细")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:add')")
    @Log(title = "持仓明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            return toAjax(faStockHoldDetailService.insertFaStockHoldDetail(faStockHoldDetail));
        } catch (ServiceException e) {
            logger.error("addStockHoldDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addStockHoldDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改持仓明细
     */
    @ApiOperation("修改持仓明细")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:edit')")
    @Log(title = "持仓明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        return toAjax(faStockHoldDetailService.updateFaStockHoldDetail(faStockHoldDetail));
    }

    /**
     * 删除持仓明细
     */
    @ApiOperation("删除持仓明细")
    @PreAuthorize("@ss.hasPermi('biz:stockHoldDetail:remove')")
    @Log(title = "持仓明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faStockHoldDetailService.deleteFaStockHoldDetailByIds(ids));
    }

    /**
     * 修改锁仓状态
     */
    @ApiOperation("修改锁仓状态")
    @Log(title = "修改锁仓状态", businessType = BusinessType.UPDATE)
    @PostMapping("/changeLockStatus")
    public AjaxResult changeLockStatus(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockHoldDetailService.changeLockStatus(faStockHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("changeLockStatus", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeLockStatus", e);
            return AjaxResult.error();
        }
    }

    /**
     * 调整T+N
     */
    @ApiOperation("调整T+N")
    @Log(title = "调整T+N", businessType = BusinessType.UPDATE)
    @PostMapping("/changeTN")
    public AjaxResult changeTN(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockHoldDetailService.changeTN(faStockHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("changeTN", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeTN", e);
            return AjaxResult.error();
        }
    }

    /**
     * 持仓统计
     */
    @ApiOperation("持仓统计")
    @PostMapping("/getHoldStatistics")
    public AjaxResult getHoldStatistics(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faStockHoldDetail.getDailiId()) {
                faStockHoldDetail.setParentId(Long.valueOf(faStockHoldDetail.getDailiId()));
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faStockHoldDetail.setParentId(1L);
                } else {
                    faStockHoldDetail.setParentId(loginUser.getUserId());
                }
            }

            List<FaStockHoldDetail> list = faStockHoldDetailService.selectFaStockHoldDetailList(faStockHoldDetail);

            // 信用金总额
            BigDecimal totalCost = list.stream().map(FaStockHoldDetail::getPrincipal).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 盈亏总额
            BigDecimal totalProfitLose = list.stream().map(FaStockHoldDetail::getProfitLose).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 买入手续费
            BigDecimal totalBuyFee = list.stream().map(FaStockHoldDetail::getBuyPoundage).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 卖出手续费
            BigDecimal totalSellFee = list.stream().map(FaStockHoldDetail::getSellPoundage).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 印花税
            BigDecimal totalStampDuty = list.stream().map(FaStockHoldDetail::getSellStampDuty).reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, BigDecimal> map = new HashMap<>();
            map.put("totalCost", totalCost);
            map.put("totalProfitLose", totalProfitLose);
            map.put("totalBuyFee", totalBuyFee);
            map.put("totalSellFee", totalSellFee);
            map.put("totalStampDuty", totalStampDuty);

            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getTradingStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getTradingStatistics", e);
            return AjaxResult.error();
        }
    }

}
