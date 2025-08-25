package com.ruoyi.web.controller.api.riskConfig;

import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 风控设置Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "风控设置")
@RestController
@RequestMapping("/api/riskConfig")
public class RiskConfigController extends BaseController
{
    @Autowired
    private IFaRiskConfigService faRiskConfigService;

    /**
     * 查询风控设置map
     */
    @ApiOperation("查询风控设置")
    @PostMapping("/getRiskConfigMap")
    public AjaxResult getRiskConfigMap()
    {
        try {
            Map map = faRiskConfigService.getRiskConfigMap();
            return AjaxResult.success(map);
        } catch (Exception e) {
            logger.error("getRiskConfigMap", e);
            return AjaxResult.error();
        }
    }

}
