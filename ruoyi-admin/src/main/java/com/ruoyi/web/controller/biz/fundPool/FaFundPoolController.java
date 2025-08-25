package com.ruoyi.web.controller.biz.fundPool;

import com.ruoyi.biz.fundPool.service.FundPoolService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 资金池Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "资金池")
@RestController
@RequestMapping("/biz/fundPool")
public class FaFundPoolController extends BaseController
{

    @Autowired
    private FundPoolService fundPoolService;

    /**
     * 获取资金池信息
     */
    @ApiOperation("获取资金池信息")
    @Log(title = "获取资金池信息", businessType = BusinessType.OTHER)
    @PostMapping("/getFundPoolInfo")
    public AjaxResult getFundPoolInfo()
    {
        try {
            Map<String, BigDecimal> map = fundPoolService.getFundPoolInfo(null);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getFundPoolInfo", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getFundPoolInfo", e);
            return AjaxResult.error();
        }
    }

}
