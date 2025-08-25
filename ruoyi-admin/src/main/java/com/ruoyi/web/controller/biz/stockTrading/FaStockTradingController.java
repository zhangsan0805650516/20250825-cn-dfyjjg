package com.ruoyi.web.controller.biz.stockTrading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.common.core.domain.model.LoginUser;
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
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成交记录Controller
 * 
 * @author ruoyi
 * @date 2024-01-23
 */
@Api(tags = "成交记录")
@RestController
@RequestMapping("/biz/stockTrading")
public class FaStockTradingController extends BaseController
{
    @Autowired
    private IFaStockTradingService faStockTradingService;

    /**
     * 查询成交记录列表
     */
    @ApiOperation("查询成交记录列表")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaStockTrading faStockTrading)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faStockTrading.getDailiId()) {
            faStockTrading.setParentId(Long.valueOf(faStockTrading.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faStockTrading.setParentId(1L);
            } else {
                faStockTrading.setParentId(loginUser.getUserId());
            }
        }

        List<FaStockTrading> list = faStockTradingService.selectFaStockTradingList(faStockTrading);
        return getDataTable(list);
    }

    /**
     * 导出成交记录列表
     */
    @ApiOperation("导出成交记录列表")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:export')")
    @Log(title = "成交记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaStockTrading faStockTrading)
    {
        List<FaStockTrading> list = faStockTradingService.selectFaStockTradingList(faStockTrading);
        ExcelUtil<FaStockTrading> util = new ExcelUtil<FaStockTrading>(FaStockTrading.class);
        util.exportExcel(response, list, "成交记录数据");
    }

    /**
     * 获取成交记录详细信息
     */
    @ApiOperation("获取成交记录详细信息")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faStockTradingService.selectFaStockTradingById(id));
    }

    /**
     * 新增成交记录
     */
    @ApiOperation("新增成交记录")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:add')")
    @Log(title = "成交记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaStockTrading faStockTrading)
    {
        return toAjax(faStockTradingService.insertFaStockTrading(faStockTrading));
    }

    /**
     * 修改成交记录
     */
    @ApiOperation("修改成交记录")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:edit')")
    @Log(title = "成交记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaStockTrading faStockTrading)
    {
        return toAjax(faStockTradingService.updateFaStockTrading(faStockTrading));
    }

    /**
     * 删除成交记录
     */
    @ApiOperation("删除成交记录")
    @PreAuthorize("@ss.hasPermi('biz:stockTrading:remove')")
    @Log(title = "成交记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faStockTradingService.deleteFaStockTradingByIds(ids));
    }

    /**
     * 平仓 明细
     */
    @ApiOperation("平仓 明细")
    @Log(title = "平仓 明细", businessType = BusinessType.UPDATE)
    @PostMapping("/closingPositionDetail")
    public AjaxResult closingPositionDetail(@RequestBody FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockTradingService.closingPositionDetail(faStockHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("closingPositionDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("closingPositionDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 交易统计
     */
    @ApiOperation("交易统计")
    @PostMapping("/getTradingStatistics")
    public AjaxResult getTradingStatistics(@RequestBody FaStockTrading faStockTrading)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faStockTrading.getDailiId()) {
                faStockTrading.setParentId(Long.valueOf(faStockTrading.getDailiId()));
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faStockTrading.setParentId(1L);
                } else {
                    faStockTrading.setParentId(loginUser.getUserId());
                }
            }

            faStockTrading.setDeleteFlag(0);

            // 买入手续费
            BigDecimal totalBuyFee = faStockTradingService.getTotalBuyFee(faStockTrading);
            // 卖出手续费
            BigDecimal totalSellFee = faStockTradingService.getTotalSellFee(faStockTrading);
            // 印花税
            BigDecimal totalStampDuty = faStockTradingService.getTotalStampDuty(faStockTrading);

            Map<String, BigDecimal> map = new HashMap<>();
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
