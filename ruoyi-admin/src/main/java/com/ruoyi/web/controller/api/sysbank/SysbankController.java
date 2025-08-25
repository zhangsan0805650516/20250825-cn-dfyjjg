package com.ruoyi.web.controller.api.sysbank;

import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.biz.sysbank.service.IFaSysbankService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通道Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "支付通道")
@RestController
@RequestMapping("/api/sysbank")
public class SysbankController extends BaseController
{
    @Autowired
    private IFaSysbankService faSysbankService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 查询代理支付通道
     */
    @ApiOperation("查询代理支付通道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId", value = "代理id", required = true, dataType = "Integer")
    })
    @PostMapping("/getSysbankByDaili")
    public AjaxResult getSysbankByDaili(@RequestBody FaSysbank faSysbank)
    {
        try {
            List<FaSysbank> list = faSysbankService.getSysbankByDaili(faSysbank);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getSysbankByDaili", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSysbankByDaili", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询支付通道详情
     */
    @ApiOperation("查询支付通道详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "通道id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "ckPass", value = "查看密码", dataType = "String")
    })
    @PostMapping("/getSysbankDetail")
    public AjaxResult getSysbankDetail(@RequestBody FaSysbank faSysbank)
    {
        try {
            FaSysbank sysbank = faSysbankService.getSysbankDetail(faSysbank);
            return AjaxResult.success(sysbank);
        } catch (ServiceException e) {
            logger.error("getSysbankDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSysbankDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 根据密码查询支付通道
     */
    @ApiOperation("根据密码查询支付通道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ckPass", value = "查看密码", required = true, dataType = "String")
    })
    @PostMapping("/getSysbankByPwd")
    public AjaxResult getSysbankByPwd(@RequestBody FaSysbank faSysbank)
    {
        try {
            List<FaSysbank> list = faSysbankService.getSysbankByPwd(faSysbank);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getSysbankByPwd", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSysbankByPwd", e);
            return AjaxResult.error();
        }
    }

}
