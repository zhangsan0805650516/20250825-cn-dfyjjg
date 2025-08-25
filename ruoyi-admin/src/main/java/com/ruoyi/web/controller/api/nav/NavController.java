package com.ruoyi.web.controller.api.nav;

import com.ruoyi.biz.nav.domain.FaNav;
import com.ruoyi.biz.nav.service.IFaNavService;
import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导航图标Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "导航图标")
@RestController
@RequestMapping("/api/nav")
public class NavController extends BaseController
{
    @Autowired
    private IFaNavService faNavService;

    /**
     * 查询导航图标
     */
    @ApiOperation("查询导航图标")
    @AppLog(title = "查询导航图标", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1首页菜单 2个人中心 3查询记录", required = true, dataType = "Integer")})
    @PostMapping("/getNavList")
    public AjaxResult getNavList(@RequestBody FaNav faNav)
    {
        try {
            List<FaNav> list = faNavService.getNavList(faNav);
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("getNavList", e);
            return AjaxResult.error();
        }
    }

}
