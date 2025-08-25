package com.ruoyi.web.controller.biz.tutorVote;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
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
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;
import com.ruoyi.biz.tutorVote.service.IFaTutorVoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 投票Controller
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Api(tags = "投票")
@RestController
@RequestMapping("/biz/tutorVote")
public class FaTutorVoteController extends BaseController
{
    @Autowired
    private IFaTutorVoteService faTutorVoteService;

    /**
     * 查询投票列表
     */
    @ApiOperation("查询投票列表")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaTutorVote faTutorVote)
    {
        startPage();
        List<FaTutorVote> list = faTutorVoteService.selectFaTutorVoteList(faTutorVote);
        return getDataTable(list);
    }

    /**
     * 导出投票列表
     */
    @ApiOperation("导出投票列表")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:export')")
    @Log(title = "导出投票列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaTutorVote faTutorVote)
    {
        List<FaTutorVote> list = faTutorVoteService.selectFaTutorVoteList(faTutorVote);
        ExcelUtil<FaTutorVote> util = new ExcelUtil<FaTutorVote>(FaTutorVote.class);
        util.exportExcel(response, list, "投票数据");
    }

    /**
     * 获取投票详细信息
     */
    @ApiOperation("获取投票详细信息")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faTutorVoteService.selectFaTutorVoteById(id));
    }

    /**
     * 新增投票
     */
    @ApiOperation("新增投票")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:add')")
    @Log(title = "新增投票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaTutorVote faTutorVote)
    {
        return toAjax(faTutorVoteService.insertFaTutorVote(faTutorVote));
    }

    /**
     * 修改投票
     */
    @ApiOperation("修改投票")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:edit')")
    @Log(title = "修改投票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaTutorVote faTutorVote)
    {
        return toAjax(faTutorVoteService.updateFaTutorVote(faTutorVote));
    }

    /**
     * 删除投票
     */
    @ApiOperation("删除投票")
    @PreAuthorize("@ss.hasPermi('biz:tutorVote:remove')")
    @Log(title = "删除投票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faTutorVoteService.deleteFaTutorVoteByIds(ids));
    }

    /**
     * 增加用户投票
     */
    @RepeatSubmit
    @ApiOperation("增加用户投票")
    @Log(title = "增加用户投票", businessType = BusinessType.INSERT)
    @PostMapping("/submitMemberVote")
    public AjaxResult submitMemberVote(@RequestBody FaTutorVote faTutorVote)
    {
        try {
            faTutorVoteService.submitMemberVote(faTutorVote);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitMemberVote", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitMemberVote", e);
            return AjaxResult.error();
        }
    }

    /**
     * 投票解冻
     */
    @RepeatSubmit
    @ApiOperation("投票解冻")
    @Log(title = "投票解冻", businessType = BusinessType.UPDATE)
    @PostMapping("/unfreeze")
    public AjaxResult unfreeze(@RequestBody FaTutorVote faTutorVote)
    {
        try {
            faTutorVoteService.unfreeze(faTutorVote);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("unfreeze", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("unfreeze", e);
            return AjaxResult.error();
        }
    }

    /**
     * 批量解冻
     */
    @RepeatSubmit
    @ApiOperation("批量解冻")
    @Log(title = "批量解冻", businessType = BusinessType.UPDATE)
    @PostMapping("/batchUnfreeze")
    public AjaxResult batchUnfreeze(@RequestBody FaTutorVote faTutorVote)
    {
        try {
            faTutorVoteService.batchUnfreeze(faTutorVote.getIds());
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("batchUnfreeze", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("batchUnfreeze", e);
            return AjaxResult.error();
        }
    }

}
