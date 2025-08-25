package com.ruoyi.web.controller.biz.recharge;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.biz.recharge.domain.ExportRecharge;
import com.ruoyi.biz.recharge.domain.ExportRechargeWithMobile;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.recharge.service.IFaRechargeService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
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
 * 充值Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "充值")
@RestController
@RequestMapping("/biz/recharge")
public class FaRechargeController extends BaseController
{
    @Autowired
    private IFaRechargeService faRechargeService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询充值列表
     */
    @ApiOperation("查询充值列表")
    @PreAuthorize("@ss.hasPermi('biz:recharge:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaRecharge faRecharge)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faRecharge.getDailiId()) {
            faRecharge.setParentId(Long.valueOf(faRecharge.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faRecharge.setParentId(1L);
            } else {
                faRecharge.setParentId(loginUser.getUserId());
            }
        }

        List<FaRecharge> list = faRechargeService.selectFaRechargeList(faRecharge);
        return getDataTable(list);
    }

    /**
     * 导出充值列表
     */
    @ApiOperation("导出充值列表")
    @PreAuthorize("@ss.hasPermi('biz:recharge:export')")
    @Log(title = "充值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaRecharge faRecharge)
    {
        LoginUser loginUser = getLoginUser();
        if (null != faRecharge.getDailiId()) {
            faRecharge.setParentId(Long.valueOf(faRecharge.getDailiId()));
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faRecharge.setParentId(1L);
            } else {
                faRecharge.setParentId(loginUser.getUserId());
            }
        }

        List<FaRecharge> list = faRechargeService.selectFaRechargeList(faRecharge);

        // 手机号解密
        try {
            // 手机号解密开关，默认关
            String mobile_decrypt = iFaRiskConfigService.getConfigValue("mobile_decrypt", "0");
            if ("1".equalsIgnoreCase(mobile_decrypt)) {
                for (FaRecharge recharge : list) {
                    String mobile = AESUtil.decrypt(recharge.getFaMember().getSalt());
                    recharge.setMobile(mobile);
                }
            }

            JSONArray rechargeArray = JSONArray.parseArray(JSON.toJSONString(list));

            // 是否显示手机号，默认不显示
            String show_mobile = iFaRiskConfigService.getConfigValue("show_mobile", "0");
            if ("0".equals(show_mobile)) {
                List<ExportRecharge> rechargeList = JSON.parseArray(rechargeArray.toJSONString(), ExportRecharge.class);
                ExcelUtil<ExportRecharge> util = new ExcelUtil<>(ExportRecharge.class);
                util.exportExcel(response, rechargeList, "充值数据");
            } else if ("1".equals(show_mobile)) {
                List<ExportRechargeWithMobile> rechargeList = JSON.parseArray(rechargeArray.toJSONString(), ExportRechargeWithMobile.class);
                ExcelUtil<ExportRechargeWithMobile> util = new ExcelUtil<>(ExportRechargeWithMobile.class);
                util.exportExcel(response, rechargeList, "充值数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取充值详细信息
     */
    @ApiOperation("获取充值详细信息")
    @PreAuthorize("@ss.hasPermi('biz:recharge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faRechargeService.selectFaRechargeById(id));
    }

    /**
     * 新增充值
     */
    @ApiOperation("新增充值")
    @PreAuthorize("@ss.hasPermi('biz:recharge:add')")
    @Log(title = "充值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaRecharge faRecharge)
    {
        return toAjax(faRechargeService.insertFaRecharge(faRecharge));
    }

    /**
     * 修改充值
     */
    @ApiOperation("修改充值")
    @PreAuthorize("@ss.hasPermi('biz:recharge:edit')")
    @Log(title = "充值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaRecharge faRecharge)
    {
        return toAjax(faRechargeService.updateFaRecharge(faRecharge));
    }

    /**
     * 删除充值
     */
    @ApiOperation("删除充值")
    @PreAuthorize("@ss.hasPermi('biz:recharge:remove')")
    @Log(title = "充值", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faRechargeService.deleteFaRechargeByIds(ids));
    }

    /**
     * 审核充值
     */
    @ApiOperation("审核充值")
    @PreAuthorize("@ss.hasPermi('biz:recharge:approve')")
    @Log(title = "审核充值", businessType = BusinessType.UPDATE)
    @PostMapping("/approveRecharge")
    public AjaxResult approveRecharge(@RequestBody FaRecharge faRecharge)
    {
        try {
            faRechargeService.approveRecharge(faRecharge);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("approveRecharge", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("approveRecharge", e);
            return AjaxResult.error();
        }
    }

    /**
     * 充值统计
     */
    @ApiOperation("充值统计")
    @PostMapping("/getRechargeStatistics")
    public AjaxResult getRechargeStatistics(@RequestBody FaRecharge faRecharge)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faRecharge.getDailiId()) {
                faRecharge.setParentId(Long.valueOf(faRecharge.getDailiId()));
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faRecharge.setParentId(1L);
                } else {
                    faRecharge.setParentId(loginUser.getUserId());
                }
            }

            // 已付款总额
            BigDecimal totalPaid = faRechargeService.getTotalPaid(faRecharge);
            // 未付款总额
            BigDecimal totalUnpaid = faRechargeService.getTotalUnpaid(faRecharge);
            // 驳回总额
            BigDecimal totalRefuse = faRechargeService.getTotalRefuse(faRecharge);

            Map<String, BigDecimal> map = new HashMap<>();
            map.put("totalPaid", totalPaid);
            map.put("totalUnpaid", totalUnpaid);
            map.put("totalRefuse", totalRefuse);

            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getRechargeStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRechargeStatistics", e);
            return AjaxResult.error();
        }
    }

}
