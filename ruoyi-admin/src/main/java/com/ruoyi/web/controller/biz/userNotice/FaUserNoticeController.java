package com.ruoyi.web.controller.biz.userNotice;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.biz.userNotice.domain.FaUserNotice;
import com.ruoyi.biz.userNotice.service.IFaUserNoticeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户消息Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户消息")
@RestController
@RequestMapping("/biz/userNotice")
public class FaUserNoticeController extends BaseController
{
    @Autowired
    private IFaUserNoticeService faUserNoticeService;

    /**
     * 查询用户消息列表
     */
    @ApiOperation("查询用户消息列表")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaUserNotice faUserNotice)
    {
        startPage();
        List<FaUserNotice> list = faUserNoticeService.selectFaUserNoticeList(faUserNotice);
        return getDataTable(list);
    }

    /**
     * 导出用户消息列表
     */
    @ApiOperation("导出用户消息列表")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:export')")
    @Log(title = "导出用户消息列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaUserNotice faUserNotice)
    {
        List<FaUserNotice> list = faUserNoticeService.selectFaUserNoticeList(faUserNotice);
        ExcelUtil<FaUserNotice> util = new ExcelUtil<FaUserNotice>(FaUserNotice.class);
        util.exportExcel(response, list, "用户消息数据");
    }

    /**
     * 获取用户消息详细信息
     */
    @ApiOperation("获取用户消息详细信息")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faUserNoticeService.selectFaUserNoticeById(id));
    }

    /**
     * 新增用户消息
     */
    @ApiOperation("新增用户消息")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:add')")
    @Log(title = "新增用户消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaUserNotice faUserNotice)
    {
        try {
            return toAjax(faUserNoticeService.insertFaUserNotice(faUserNotice));
        } catch (ServiceException e) {
            logger.error("addUserNotice", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addUserNotice", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改用户消息
     */
    @ApiOperation("修改用户消息")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:edit')")
    @Log(title = "修改用户消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaUserNotice faUserNotice)
    {
        return toAjax(faUserNoticeService.updateFaUserNotice(faUserNotice));
    }

    /**
     * 删除用户消息
     */
    @ApiOperation("删除用户消息")
    @PreAuthorize("@ss.hasPermi('biz:userNotice:remove')")
    @Log(title = "删除用户消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faUserNoticeService.deleteFaUserNoticeByIds(ids));
    }
}
