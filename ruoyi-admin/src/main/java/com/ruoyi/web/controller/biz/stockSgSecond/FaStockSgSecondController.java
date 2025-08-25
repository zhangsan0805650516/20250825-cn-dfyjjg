package com.ruoyi.web.controller.biz.stockSgSecond;

import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockSgSecond.service.IFaStockSgSecondService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
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
 * 线下配售(世纪独享)Controller
 * 
 * @author ruoyi
 * @date 2024-11-01
 */
@Api(tags = "线下配售(世纪独享)")
@RestController
@RequestMapping("/biz/stockSgSecond")
public class FaStockSgSecondController extends BaseController
{
    @Autowired
    private IFaStockSgSecondService faStockSgSecondService;

    /**
     * 查询线下配售(世纪独享)列表
     */
    @ApiOperation("查询线下配售(世纪独享)列表")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaStockSgSecond faStockSgSecond)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faStockSgSecond.getDailiId()) {
            faStockSgSecond.setParentId(Long.valueOf(faStockSgSecond.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faStockSgSecond.setParentId(1L);
            } else {
                faStockSgSecond.setParentId(loginUser.getUserId());
            }
        }

        List<FaStockSgSecond> list = faStockSgSecondService.selectFaStockSgSecondList(faStockSgSecond);
        return getDataTable(list);
    }

    /**
     * 导出线下配售(世纪独享)列表
     */
    @ApiOperation("导出线下配售(世纪独享)列表")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:export')")
    @Log(title = "线下配售(世纪独享)", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaStockSgSecond faStockSgSecond)
    {
        List<FaStockSgSecond> list = faStockSgSecondService.selectFaStockSgSecondList(faStockSgSecond);
        ExcelUtil<FaStockSgSecond> util = new ExcelUtil<FaStockSgSecond>(FaStockSgSecond.class);
        util.exportExcel(response, list, "线下配售(世纪独享)数据");
    }

    /**
     * 获取线下配售(世纪独享)详细信息
     */
    @ApiOperation("获取线下配售(世纪独享)详细信息")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faStockSgSecondService.selectFaStockSgSecondById(id));
    }

    /**
     * 新增线下配售(世纪独享)
     */
    @ApiOperation("新增线下配售(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:add')")
    @Log(title = "线下配售(世纪独享)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaStockSgSecond faStockSgSecond) throws Exception
    {
        return toAjax(faStockSgSecondService.insertFaStockSgSecond(faStockSgSecond));
    }

    /**
     * 修改线下配售(世纪独享)
     */
    @ApiOperation("修改线下配售(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:edit')")
    @Log(title = "线下配售(世纪独享)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        return toAjax(faStockSgSecondService.updateFaStockSgSecond(faStockSgSecond));
    }

    /**
     * 删除线下配售(世纪独享)
     */
    @ApiOperation("删除线下配售(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:stockSgSecond:remove')")
    @Log(title = "线下配售(世纪独享)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faStockSgSecondService.deleteFaStockSgSecondByIds(ids));
    }


    /**
     * 提交中签(世纪独享)
     */
    @ApiOperation("提交中签(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "提交打新中签(世纪独享)", businessType = BusinessType.UPDATE)
    @PostMapping("/submitAllocation")
    public AjaxResult submitAllocation(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        try {
            faStockSgSecondService.submitAllocation(faStockSgSecond);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitAllocation", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitAllocation", e);
            return AjaxResult.error();
        }
    }

    /**
     * 后台认缴(世纪独享)
     */
    @ApiOperation("后台认缴(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "后台认缴(世纪独享)", businessType = BusinessType.UPDATE)
    @PostMapping("/subscriptionBg")
    public AjaxResult subscriptionBg(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        try {
            faStockSgSecondService.subscription(faStockSgSecond);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("subscriptionBg", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("subscriptionBg", e);
            return AjaxResult.error();
        }
    }

    /**
     * 一键转持仓(世纪独享)
     */
    @ApiOperation("一键转持仓(世纪独享)")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "一键转持仓(世纪独享)", businessType = BusinessType.UPDATE)
    @PostMapping("/transToHold")
    public AjaxResult transToHold()
    {
        try {
            faStockSgSecondService.transToHold();
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("transToHold", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("transToHold", e);
            return AjaxResult.error();
        }
    }

    /**
     * 单个转持仓(世纪独享)
     */
    @ApiOperation("单个转持仓(世纪独享)")
    @Log(title = "单个转持仓(世纪独享)", businessType = BusinessType.UPDATE)
    @PostMapping("/transOneToHold")
    public AjaxResult transOneToHold(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        try {
            faStockSgSecondService.transOneToHold(faStockSgSecond);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("transOneToHold", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("transOneToHold", e);
            return AjaxResult.error();
        }
    }

}
