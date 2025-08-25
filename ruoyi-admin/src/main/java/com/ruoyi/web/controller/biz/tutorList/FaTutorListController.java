package com.ruoyi.web.controller.biz.tutorList;

import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.biz.tutorList.service.IFaTutorListService;
import com.ruoyi.common.annotation.Log;
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
 * 导师Controller
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Api(tags = "导师")
@RestController
@RequestMapping("/biz/tutorList")
public class FaTutorListController extends BaseController
{
    @Autowired
    private IFaTutorListService faTutorListService;

    /**
     * 查询导师列表
     */
    @ApiOperation("查询导师列表")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaTutorList faTutorList)
    {
        startPage();
        List<FaTutorList> list = faTutorListService.selectFaTutorListList(faTutorList);
        return getDataTable(list);
    }

    /**
     * 导出导师列表
     */
    @ApiOperation("导出导师列表")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:export')")
    @Log(title = "导出导师列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaTutorList faTutorList)
    {
        List<FaTutorList> list = faTutorListService.selectFaTutorListList(faTutorList);
        ExcelUtil<FaTutorList> util = new ExcelUtil<FaTutorList>(FaTutorList.class);
        util.exportExcel(response, list, "导师数据");
    }

    /**
     * 获取导师详细信息
     */
    @ApiOperation("获取导师详细信息")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faTutorListService.selectFaTutorListById(id));
    }

    /**
     * 新增导师
     */
    @ApiOperation("新增导师")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:add')")
    @Log(title = "新增导师", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaTutorList faTutorList)
    {
        return toAjax(faTutorListService.insertFaTutorList(faTutorList));
    }

    /**
     * 修改导师
     */
    @ApiOperation("修改导师")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:edit')")
    @Log(title = "修改导师", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaTutorList faTutorList)
    {
        return toAjax(faTutorListService.updateFaTutorList(faTutorList));
    }

    /**
     * 删除导师
     */
    @ApiOperation("删除导师")
    @PreAuthorize("@ss.hasPermi('biz:tutorList:remove')")
    @Log(title = "删除导师", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faTutorListService.deleteFaTutorListByIds(ids));
    }

    /**
     * 搜索导师
     */
    @ApiOperation("搜索导师")
    @Log(title = "搜索导师", businessType = BusinessType.OTHER)
    @PostMapping("/searchTutor")
    public AjaxResult searchTutor(@RequestBody FaTutorList faTutorList)
    {
        try {
            List<FaTutorList> list = faTutorListService.searchTutor(faTutorList);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("searchTutor", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("searchTutor", e);
            return AjaxResult.error();
        }
    }

}
