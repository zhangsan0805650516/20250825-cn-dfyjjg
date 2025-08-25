package com.ruoyi.web.controller.biz.searchMember;

import com.ruoyi.biz.searchMember.domain.FaSearchMember;
import com.ruoyi.biz.searchMember.service.IFaSearchMemberService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
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
 * 搜索用户记录Controller
 * 
 * @author ruoyi
 * @date 2024-12-15
 */
@Api(tags = "搜索用户记录")
@RestController
@RequestMapping("/biz/searchMember")
public class FaSearchMemberController extends BaseController
{
    @Autowired
    private IFaSearchMemberService faSearchMemberService;

    /**
     * 查询搜索用户记录列表
     */
    @ApiOperation("查询搜索用户记录列表")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaSearchMember faSearchMember)
    {
        startPage();
        List<FaSearchMember> list = faSearchMemberService.selectFaSearchMemberList(faSearchMember);
        return getDataTable(list);
    }

    /**
     * 导出搜索用户记录列表
     */
    @ApiOperation("导出搜索用户记录列表")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:export')")
    @Log(title = "搜索用户记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaSearchMember faSearchMember)
    {
        List<FaSearchMember> list = faSearchMemberService.selectFaSearchMemberList(faSearchMember);
        ExcelUtil<FaSearchMember> util = new ExcelUtil<FaSearchMember>(FaSearchMember.class);
        util.exportExcel(response, list, "搜索用户记录数据");
    }

    /**
     * 获取搜索用户记录详细信息
     */
    @ApiOperation("获取搜索用户记录详细信息")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faSearchMemberService.selectFaSearchMemberById(id));
    }

    /**
     * 新增搜索用户记录
     */
    @ApiOperation("新增搜索用户记录")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:add')")
    @Log(title = "搜索用户记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaSearchMember faSearchMember)
    {
        return toAjax(faSearchMemberService.insertFaSearchMember(faSearchMember));
    }

    /**
     * 修改搜索用户记录
     */
    @ApiOperation("修改搜索用户记录")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:edit')")
    @Log(title = "搜索用户记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaSearchMember faSearchMember)
    {
        return toAjax(faSearchMemberService.updateFaSearchMember(faSearchMember));
    }

    /**
     * 删除搜索用户记录
     */
    @ApiOperation("删除搜索用户记录")
    @PreAuthorize("@ss.hasPermi('biz:searchMember:remove')")
    @Log(title = "搜索用户记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faSearchMemberService.deleteFaSearchMemberByIds(ids));
    }

    /**
     * 搜索会员
     */
    @ApiOperation("搜索会员")
    @Log(title = "搜索会员", businessType = BusinessType.OTHER)
    @PostMapping("/getSearchList")
    public AjaxResult getSearchList(@RequestBody FaSearchMember faSearchMember)
    {
        try {
            List<FaMember> list = faSearchMemberService.getSearchList(faSearchMember);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getSearchList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSearchList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 剩余搜索次数
     */
    @ApiOperation("剩余搜索次数")
    @PostMapping("/getSearchTimesLeft")
    public AjaxResult getSearchTimesLeft()
    {
        try {
            int count = faSearchMemberService.getSearchTimesLeft();
            return AjaxResult.success(count);
        } catch (ServiceException e) {
            logger.error("getSearchTimesLeft", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSearchTimesLeft", e);
            return AjaxResult.error();
        }
    }

}
