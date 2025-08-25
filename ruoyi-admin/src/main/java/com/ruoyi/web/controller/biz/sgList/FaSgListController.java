package com.ruoyi.web.controller.biz.sgList;

import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgList.service.IFaSgListService;
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
import java.util.List;

/**
 * 申购列表Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "申购列表")
@RestController
@RequestMapping("/biz/sgList")
public class FaSgListController extends BaseController
{
    @Autowired
    private IFaSgListService faSgListService;

    /**
     * 查询申购列表列表
     */
    @ApiOperation("查询申购列表列表")
    @PreAuthorize("@ss.hasPermi('biz:sgList:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaSgList faSgList)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faSgList.getDailiId()) {
            faSgList.setParentId(Long.valueOf(faSgList.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faSgList.setParentId(1L);
            } else {
                faSgList.setParentId(loginUser.getUserId());
            }
        }

        List<FaSgList> list = faSgListService.selectFaSgListList(faSgList);
        return getDataTable(list);
    }

    /**
     * 导出申购列表列表
     */
    @ApiOperation("导出申购列表列表")
    @PreAuthorize("@ss.hasPermi('biz:sgList:export')")
    @Log(title = "申购列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaSgList faSgList)
    {
        List<FaSgList> list = faSgListService.selectFaSgListList(faSgList);
        ExcelUtil<FaSgList> util = new ExcelUtil<FaSgList>(FaSgList.class);
        util.exportExcel(response, list, "申购列表数据");
    }

    /**
     * 获取申购列表详细信息
     */
    @ApiOperation("获取申购列表详细信息")
    @PreAuthorize("@ss.hasPermi('biz:sgList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faSgListService.selectFaSgListById(id));
    }

    /**
     * 新增申购列表
     */
    @ApiOperation("新增申购列表")
    @PreAuthorize("@ss.hasPermi('biz:sgList:add')")
    @Log(title = "申购列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaSgList faSgList) throws Exception
    {
        return toAjax(faSgListService.insertFaSgList(faSgList));
    }

    /**
     * 修改申购列表
     */
    @ApiOperation("修改申购列表")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "申购列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaSgList faSgList)
    {
        return toAjax(faSgListService.updateFaSgList(faSgList));
    }

    /**
     * 删除申购列表
     */
    @ApiOperation("删除申购列表")
    @PreAuthorize("@ss.hasPermi('biz:sgList:remove')")
    @Log(title = "申购列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faSgListService.deleteFaSgListByIds(ids));
    }

    /**
     * 提交中签
     */
    @ApiOperation("提交中签")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "提交打新中签", businessType = BusinessType.UPDATE)
    @PostMapping("/submitAllocation")
    public AjaxResult submitAllocation(@RequestBody FaSgList faSgList)
    {
        try {
            faSgListService.submitAllocation(faSgList);
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
     * 后台认缴
     */
    @ApiOperation("后台认缴")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "后台认缴", businessType = BusinessType.UPDATE)
    @PostMapping("/subscriptionBg")
    public AjaxResult subscriptionBg(@RequestBody FaSgList faSgList)
    {
        try {
            faSgListService.subscription(faSgList);
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
     * 一键转持仓
     */
    @ApiOperation("一键转持仓")
    @PreAuthorize("@ss.hasPermi('biz:sgList:edit')")
    @Log(title = "一键转持仓", businessType = BusinessType.UPDATE)
    @PostMapping("/transToHold")
    public AjaxResult transToHold()
    {
        try {
            faSgListService.transToHold();
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
     * 单个转持仓
     */
    @ApiOperation("单个转持仓")
    @Log(title = "单个转持仓", businessType = BusinessType.UPDATE)
    @PostMapping("/transOneToHold")
    public AjaxResult transOneToHold(@RequestBody FaSgList faSgList)
    {
        try {
            faSgListService.transOneToHold(faSgList);
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
