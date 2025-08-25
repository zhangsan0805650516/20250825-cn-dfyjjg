package com.ruoyi.web.controller.biz.capitalLog;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.biz.capitalLog.domain.ExportCapitalLog;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资金记录Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "资金记录")
@RestController
@RequestMapping("/biz/capitalLog")
public class FaCapitalLogController extends BaseController
{
    @Autowired
    private IFaCapitalLogService faCapitalLogService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询资金记录列表
     */
    @ApiOperation("查询资金记录列表")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaCapitalLog faCapitalLog)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
            faCapitalLog.setParentId(1L);
        } else {
            faCapitalLog.setParentId(loginUser.getUserId());
        }

        List<FaCapitalLog> list = faCapitalLogService.selectFaCapitalLogList(faCapitalLog);
        return getDataTable(list);
    }

    /**
     * 导出资金记录列表
     */
    @ApiOperation("导出资金记录列表")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:export')")
    @Log(title = "资金记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaCapitalLog faCapitalLog)
    {
        LoginUser loginUser = getLoginUser();
        if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
            faCapitalLog.setParentId(1L);
        } else {
            faCapitalLog.setParentId(loginUser.getUserId());
        }
        List<FaCapitalLog> list = faCapitalLogService.selectFaCapitalLogList(faCapitalLog);

        String mobile_decrypt = "0";
        // 手机号解密开关，默认关
        try {
            mobile_decrypt = iFaRiskConfigService.getConfigValue("mobile_decrypt", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("1".equalsIgnoreCase(mobile_decrypt)) {
            for (FaCapitalLog capitalLog : list) {
                String mobile = "";

                try {
                    mobile = AESUtil.decrypt(capitalLog.getFaMember().getSalt());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                capitalLog.getFaMember().setUsername(mobile);
                capitalLog.getFaMember().setMobile(mobile);

                if (1 == capitalLog.getDirect()) {
                    capitalLog.setMoney(capitalLog.getMoney().negate());
                }
            }
        }

        JSONArray capitalLogArray = JSONArray.parseArray(JSON.toJSONString(list));
        List<ExportCapitalLog> rechargeList = JSON.parseArray(capitalLogArray.toJSONString(), ExportCapitalLog.class);
        ExcelUtil<ExportCapitalLog> util = new ExcelUtil<>(ExportCapitalLog.class);
        util.exportExcel(response, rechargeList, "资金记录数据");
    }

    /**
     * 获取资金记录详细信息
     */
    @ApiOperation("获取资金记录详细信息")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faCapitalLogService.selectFaCapitalLogById(id));
    }

    /**
     * 新增资金记录
     */
    @ApiOperation("新增资金记录")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:add')")
    @Log(title = "资金记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaCapitalLog faCapitalLog)
    {
        return toAjax(faCapitalLogService.insertFaCapitalLog(faCapitalLog));
    }

    /**
     * 修改资金记录
     */
    @ApiOperation("修改资金记录")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:edit')")
    @Log(title = "资金记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaCapitalLog faCapitalLog)
    {
        return toAjax(faCapitalLogService.updateFaCapitalLog(faCapitalLog));
    }

    /**
     * 删除资金记录
     */
    @ApiOperation("删除资金记录")
    @PreAuthorize("@ss.hasPermi('biz:capitalLog:remove')")
    @Log(title = "资金记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faCapitalLogService.deleteFaCapitalLogByIds(ids));
    }
}
