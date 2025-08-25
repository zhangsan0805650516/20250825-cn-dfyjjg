package com.ruoyi.web.controller.biz.nav;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.biz.shengou.domain.FaNewStock;
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
import com.ruoyi.biz.nav.domain.FaNav;
import com.ruoyi.biz.nav.service.IFaNavService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 导航图标Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "导航图标")
@RestController
@RequestMapping("/biz/nav")
public class FaNavController extends BaseController
{
    @Autowired
    private IFaNavService faNavService;

    /**
     * 查询导航图标列表
     */
    @ApiOperation("查询导航图标列表")
    @PreAuthorize("@ss.hasPermi('biz:nav:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaNav faNav)
    {
        startPage();
        List<FaNav> list = faNavService.selectFaNavList(faNav);
        return getDataTable(list);
    }

    /**
     * 导出导航图标列表
     */
    @ApiOperation("导出导航图标列表")
    @PreAuthorize("@ss.hasPermi('biz:nav:export')")
    @Log(title = "导航图标", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaNav faNav)
    {
        List<FaNav> list = faNavService.selectFaNavList(faNav);
        ExcelUtil<FaNav> util = new ExcelUtil<FaNav>(FaNav.class);
        util.exportExcel(response, list, "导航图标数据");
    }

    /**
     * 获取导航图标详细信息
     */
    @ApiOperation("获取导航图标详细信息")
    @PreAuthorize("@ss.hasPermi('biz:nav:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faNavService.selectFaNavById(id));
    }

    /**
     * 新增导航图标
     */
    @ApiOperation("新增导航图标")
    @PreAuthorize("@ss.hasPermi('biz:nav:add')")
    @Log(title = "导航图标", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaNav faNav)
    {
        return toAjax(faNavService.insertFaNav(faNav));
    }

    /**
     * 修改导航图标
     */
    @ApiOperation("修改导航图标")
    @PreAuthorize("@ss.hasPermi('biz:nav:edit')")
    @Log(title = "导航图标", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaNav faNav)
    {
        return toAjax(faNavService.updateFaNav(faNav));
    }

    /**
     * 删除导航图标
     */
    @ApiOperation("删除导航图标")
    @PreAuthorize("@ss.hasPermi('biz:nav:remove')")
    @Log(title = "导航图标", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faNavService.deleteFaNavByIds(ids));
    }

    /**
     * 显示隐藏开关修改
     */
    @ApiOperation("显示隐藏开关修改")
    @Log(title = "显示隐藏开关修改", businessType = BusinessType.UPDATE)
    @PostMapping("/changeSwitch")
    public AjaxResult changeSwitch(@RequestBody FaNav faNav)
    {
        try {
            faNavService.changeSwitch(faNav);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("changeSwitch", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeSwitch", e);
            return AjaxResult.error();
        }
    }

}
