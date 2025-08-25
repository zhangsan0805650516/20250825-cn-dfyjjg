package com.ruoyi.web.controller.api.banner;

import com.ruoyi.biz.adv.domain.FaAdv;
import com.ruoyi.biz.banner.domain.FaBanner;
import com.ruoyi.biz.banner.service.IFaBannerService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
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
 * 轮播图Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "轮播图")
@RestController
@RequestMapping("/api/banner")
public class BannerController extends BaseController
{
    @Autowired
    private IFaBannerService faBannerService;

    /**
     * 查询轮播图
     */
    @ApiOperation("查询轮播图")
    @AppLog(title = "查询轮播图", businessType = BusinessType.OTHER)
    @PostMapping("/getBanner")
    public AjaxResult getBanner()
    {
        try {
            List<FaBanner> faBannerList = faBannerService.getBanner();
            return AjaxResult.success(faBannerList);
        } catch (Exception e) {
            logger.error("getBanner", e);
            return AjaxResult.error();
        }
    }

}
