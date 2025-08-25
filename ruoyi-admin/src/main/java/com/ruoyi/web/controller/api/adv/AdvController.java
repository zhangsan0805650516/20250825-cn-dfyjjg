package com.ruoyi.web.controller.api.adv;

import com.ruoyi.biz.adv.domain.FaAdv;
import com.ruoyi.biz.adv.service.IFaAdvService;
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

import java.util.List;

/**
 * 广告图Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "广告图")
@RestController
@RequestMapping("/api/adv")
public class AdvController extends BaseController
{
    @Autowired
    private IFaAdvService faAdvService;

    /**
     * 查询广告图
     */
    @ApiOperation("查询广告图")
    @AppLog(title = "查询广告图", businessType = BusinessType.OTHER)
    @PostMapping("/getAdv")
    public AjaxResult getAdv()
    {
        try {
            List<FaAdv> faAdvList = faAdvService.getAdv();
            return AjaxResult.success(faAdvList);
        } catch (Exception e) {
            logger.error("getAdv", e);
            return AjaxResult.error();
        }
    }

}
