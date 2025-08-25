package com.ruoyi.web.controller.biz.sgjiaoyi;

import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
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
 * 线下配售Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "线下配售")
@RestController
@RequestMapping("/biz/sgjiaoyi")
public class FaSgjiaoyiController extends BaseController
{
    @Autowired
    private IFaSgjiaoyiService faSgjiaoyiService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询线下配售列表
     */
    @ApiOperation("查询线下配售列表")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaSgjiaoyi faSgjiaoyi)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faSgjiaoyi.getDailiId()) {
            faSgjiaoyi.setParentId(Long.valueOf(faSgjiaoyi.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faSgjiaoyi.setParentId(1L);
            } else {
                faSgjiaoyi.setParentId(loginUser.getUserId());
            }
        }

        List<FaSgjiaoyi> list = faSgjiaoyiService.selectFaSgjiaoyiList(faSgjiaoyi);
        return getDataTable(list);
    }

    /**
     * 导出线下配售列表
     */
    @ApiOperation("导出线下配售列表")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:export')")
    @Log(title = "线下配售", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaSgjiaoyi faSgjiaoyi)
    {
        List<FaSgjiaoyi> list = faSgjiaoyiService.selectFaSgjiaoyiList(faSgjiaoyi);
        ExcelUtil<FaSgjiaoyi> util = new ExcelUtil<FaSgjiaoyi>(FaSgjiaoyi.class);
        util.exportExcel(response, list, "线下配售数据");
    }

    /**
     * 获取线下配售详细信息
     */
    @ApiOperation("获取线下配售详细信息")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faSgjiaoyiService.selectFaSgjiaoyiById(id));
    }

    /**
     * 新增线下配售
     */
    @ApiOperation("新增线下配售")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:add')")
    @Log(title = "线下配售", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaSgjiaoyi faSgjiaoyi) throws Exception
    {
        return toAjax(faSgjiaoyiService.insertFaSgjiaoyi(faSgjiaoyi));
    }

    /**
     * 修改线下配售
     */
    @ApiOperation("修改线下配售")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:edit')")
    @Log(title = "线下配售", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        return toAjax(faSgjiaoyiService.updateFaSgjiaoyi(faSgjiaoyi));
    }

    /**
     * 删除线下配售
     */
    @ApiOperation("删除线下配售")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:remove')")
    @Log(title = "线下配售", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faSgjiaoyiService.deleteFaSgjiaoyiByIds(ids));
    }

    /**
     * 提交中签
     */
    @ApiOperation("提交中签")
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:edit')")
    @Log(title = "提交配售中签", businessType = BusinessType.UPDATE)
    @PostMapping("/submitAllocation")
    public AjaxResult submitAllocation(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        try {
//
//            // 自动认缴开关 0关闭 1开启 默认关闭
//            String psxzrj = iFaRiskConfigService.getConfigValue("pszdrj", "0");
//
//            // 自动认缴开关 0下单认缴 1中签认缴 默认下单认缴
//            String psRjMoment = iFaRiskConfigService.getConfigValue("ps_rj_moment", "0");
//
//            // 自动认缴
//            if ("1".equals(psxzrj)) {
//                // 下单认缴
//                if ("0".equals(psRjMoment)) {
                    // 保证金模式
                    faSgjiaoyiService.submitAllocation(faSgjiaoyi);
//                }
//                // 中签认缴
//                else if ("1".equals(psRjMoment)) {
//                    // 补缴模式
//                    faSgjiaoyiService.submitAllocationPayLater(faSgjiaoyi);
//                }
//            }
//            // 手动认缴
//            else if ("0".equals(psxzrj)){
//                // 只中签不自动认缴
//                faSgjiaoyiService.justSubmitAllocation(faSgjiaoyi);
//            }
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
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:edit')")
    @Log(title = "后台认缴", businessType = BusinessType.UPDATE)
    @PostMapping("/subscriptionBg")
    public AjaxResult subscriptionBg(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        try {
            faSgjiaoyiService.subscription(faSgjiaoyi);
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
    @PreAuthorize("@ss.hasPermi('biz:sgjiaoyi:edit')")
    @Log(title = "一键转持仓", businessType = BusinessType.UPDATE)
    @PostMapping("/transToHold")
    public AjaxResult transToHold()
    {
        try {
            faSgjiaoyiService.transToHold();
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
    public AjaxResult transOneToHold(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        try {
            faSgjiaoyiService.transOneToHold(faSgjiaoyi);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("transOneToHold", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("transOneToHold", e);
            return AjaxResult.error();
        }
    }

    /**
     * 未中签退费
     */
    @ApiOperation("未中签退费")
    @Log(title = "未中签退费", businessType = BusinessType.UPDATE)
    @PostMapping("/refund/{ids}")
    public AjaxResult refund(@PathVariable Long[] ids)
    {
        try {
            faSgjiaoyiService.refund(ids);
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
