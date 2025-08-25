package com.ruoyi.web.controller.biz.withdraw;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.withdraw.domain.ExportWithdraw;
import com.ruoyi.biz.withdraw.domain.ExportWithdrawWithMobile;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.biz.withdraw.service.IFaWithdrawService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "提现")
@RestController
@RequestMapping("/biz/withdraw")
public class FaWithdrawController extends BaseController
{
    @Autowired
    private IFaWithdrawService faWithdrawService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询提现列表
     */
    @ApiOperation("查询提现列表")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaWithdraw faWithdraw)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faWithdraw.getDailiId()) {
            faWithdraw.setParentId(Long.valueOf(faWithdraw.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faWithdraw.setParentId(1L);
            } else {
                faWithdraw.setParentId(loginUser.getUserId());
            }
        }

        List<FaWithdraw> list = faWithdrawService.selectFaWithdrawList(faWithdraw);
        return getDataTable(list);
    }

    /**
     * 导出提现列表
     */
    @ApiOperation("导出提现列表")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:export')")
    @Log(title = "提现", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaWithdraw faWithdraw)
    {
        LoginUser loginUser = getLoginUser();
        if (null != faWithdraw.getDailiId()) {
            faWithdraw.setParentId(Long.valueOf(faWithdraw.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faWithdraw.setParentId(1L);
            } else {
                faWithdraw.setParentId(loginUser.getUserId());
            }
        }

        List<FaWithdraw> list = faWithdrawService.selectFaWithdrawList(faWithdraw);

        // 手机号解密
        try {
            // 手机号解密开关，默认关
            String mobile_decrypt = iFaRiskConfigService.getConfigValue("mobile_decrypt", "0");
            if ("1".equalsIgnoreCase(mobile_decrypt)) {
                for (FaWithdraw withdraw : list) {
                    String mobile = AESUtil.decrypt(withdraw.getFaMember().getSalt());
                    withdraw.setMobile(mobile);
                }
            }

            JSONArray withdrawArray = JSONArray.parseArray(JSON.toJSONString(list));

            // 是否显示手机号，默认不显示
            String show_mobile = iFaRiskConfigService.getConfigValue("show_mobile", "0");
            if ("0".equals(show_mobile)) {
                List<ExportWithdraw> withdrawList = JSON.parseArray(withdrawArray.toJSONString(), ExportWithdraw.class);
                ExcelUtil<ExportWithdraw> util = new ExcelUtil<>(ExportWithdraw.class);
                util.exportExcel(response, withdrawList, "提现数据");
            } else if ("1".equals(show_mobile)) {
                List<ExportWithdrawWithMobile> withdrawList = JSON.parseArray(withdrawArray.toJSONString(), ExportWithdrawWithMobile.class);
                ExcelUtil<ExportWithdrawWithMobile> util = new ExcelUtil<>(ExportWithdrawWithMobile.class);
                util.exportExcel(response, withdrawList, "提现数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取提现详细信息
     */
    @ApiOperation("获取提现详细信息")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faWithdrawService.selectFaWithdrawById(id));
    }

    /**
     * 新增提现
     */
    @ApiOperation("新增提现")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:add')")
    @Log(title = "提现", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaWithdraw faWithdraw)
    {
        return toAjax(faWithdrawService.insertFaWithdraw(faWithdraw));
    }

    /**
     * 修改提现
     */
    @ApiOperation("修改提现")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:edit')")
    @Log(title = "提现", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaWithdraw faWithdraw)
    {
        return toAjax(faWithdrawService.updateFaWithdraw(faWithdraw));
    }

    /**
     * 删除提现
     */
    @ApiOperation("删除提现")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:remove')")
    @Log(title = "提现", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faWithdrawService.deleteFaWithdrawByIds(ids));
    }

    /**
     * 审核提现
     */
    @ApiOperation("审核提现")
    @PreAuthorize("@ss.hasPermi('biz:withdraw:approve')")
    @Log(title = "审核提现", businessType = BusinessType.UPDATE)
    @PostMapping("/approveWithdraw")
    public AjaxResult approveWithdraw(@RequestBody FaWithdraw faWithdraw)
    {
        try {
            faWithdrawService.approveWithdraw(faWithdraw);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("approveWithdraw", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("approveWithdraw", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提现统计
     */
    @ApiOperation("提现统计")
    @PostMapping("/getWithdrawStatistics")
    public AjaxResult getWithdrawStatistics(@RequestBody FaWithdraw faWithdraw)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faWithdraw.getDailiId()) {
                faWithdraw.setParentId(Long.valueOf(faWithdraw.getDailiId()));
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faWithdraw.setParentId(1L);
                } else {
                    faWithdraw.setParentId(loginUser.getUserId());
                }
            }

            // 已打款总额
            BigDecimal totalPaid = faWithdrawService.getTotalPaid(faWithdraw);
            // 未打款总额
            BigDecimal totalUnpaid = faWithdrawService.getTotalUnpaid(faWithdraw);
            // 驳回总额
            BigDecimal totalRefuse = faWithdrawService.getTotalRefuse(faWithdraw);

            Map<String, BigDecimal> map = new HashMap<>();
            map.put("totalPaid", totalPaid);
            map.put("totalUnpaid", totalUnpaid);
            map.put("totalRefuse", totalRefuse);

            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getWithdrawStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getWithdrawStatistics", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改消息通知
     */
    @ApiOperation("修改消息通知")
    @Log(title = "修改消息通知", businessType = BusinessType.UPDATE)
    @PostMapping("/changeIsQx")
    public AjaxResult changeIsQx(@RequestBody FaWithdraw faWithdraw)
    {
        try {
            faWithdrawService.changeIsQx(faWithdraw);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("changeIsQx", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeIsQx", e);
            return AjaxResult.error();
        }
    }

    /**
     * 检测消息
     */
    @ApiOperation("检测消息")
    @PostMapping("/checkQx")
    public AjaxResult checkQx(@RequestBody FaWithdraw faWithdraw)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faWithdraw.getDailiId()) {
                faWithdraw.setParentId(Long.valueOf(faWithdraw.getDailiId()));
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faWithdraw.setParentId(1L);
                } else {
                    faWithdraw.setParentId(loginUser.getUserId());
                }
            }

            boolean flag = faWithdrawService.checkQx(faWithdraw);
            return AjaxResult.success(flag);
        } catch (ServiceException e) {
            logger.error("checkQx", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("checkQx", e);
            return AjaxResult.error();
        }
    }

}
