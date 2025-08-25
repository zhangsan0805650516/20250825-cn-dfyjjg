package com.ruoyi.web.controller.biz.tradeApprove;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.biz.recharge.domain.FaRecharge;
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
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;
import com.ruoyi.biz.tradeApprove.service.IFaTradeApproveService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 交易审核Controller
 * 
 * @author ruoyi
 * @date 2024-05-31
 */
@Api(tags = "交易审核")
@RestController
@RequestMapping("/biz/tradeApprove")
public class FaTradeApproveController extends BaseController
{
    @Autowired
    private IFaTradeApproveService faTradeApproveService;

    /**
     * 查询交易审核列表
     */
    @ApiOperation("查询交易审核列表")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaTradeApprove faTradeApprove)
    {
        startPage();
        List<FaTradeApprove> list = faTradeApproveService.selectFaTradeApproveList(faTradeApprove);
        return getDataTable(list);
    }

    /**
     * 导出交易审核列表
     */
    @ApiOperation("导出交易审核列表")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:export')")
    @Log(title = "交易审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaTradeApprove faTradeApprove)
    {
        List<FaTradeApprove> list = faTradeApproveService.selectFaTradeApproveList(faTradeApprove);
        ExcelUtil<FaTradeApprove> util = new ExcelUtil<FaTradeApprove>(FaTradeApprove.class);
        util.exportExcel(response, list, "交易审核数据");
    }

    /**
     * 获取交易审核详细信息
     */
    @ApiOperation("获取交易审核详细信息")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faTradeApproveService.selectFaTradeApproveById(id));
    }

    /**
     * 新增交易审核
     */
    @ApiOperation("新增交易审核")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:add')")
    @Log(title = "交易审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaTradeApprove faTradeApprove)
    {
        return toAjax(faTradeApproveService.insertFaTradeApprove(faTradeApprove));
    }

    /**
     * 修改交易审核
     */
    @ApiOperation("修改交易审核")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:edit')")
    @Log(title = "交易审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaTradeApprove faTradeApprove)
    {
        return toAjax(faTradeApproveService.updateFaTradeApprove(faTradeApprove));
    }

    /**
     * 删除交易审核
     */
    @ApiOperation("删除交易审核")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:remove')")
    @Log(title = "交易审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faTradeApproveService.deleteFaTradeApproveByIds(ids));
    }

    /**
     * 审核交易
     */
    @ApiOperation("审核交易")
    @PreAuthorize("@ss.hasPermi('biz:tradeApprove:approve')")
    @Log(title = "审核交易", businessType = BusinessType.UPDATE)
    @PostMapping("/approveTrade")
    public AjaxResult approveTrade(@RequestBody FaTradeApprove faTradeApprove)
    {
        try {
            faTradeApproveService.approveTrade(faTradeApprove);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("approveTrade", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("approveTrade", e);
            return AjaxResult.error();
        }
    }
}
