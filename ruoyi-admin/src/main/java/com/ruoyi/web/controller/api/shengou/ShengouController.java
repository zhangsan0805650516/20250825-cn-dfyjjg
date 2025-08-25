package com.ruoyi.web.controller.api.shengou;

import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 新股列表Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "新股")
@RestController
@RequestMapping("/api/shengou")
public class ShengouController extends BaseController
{
    @Autowired
    private IFaShengouService faShengouService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询打新列表
     */
    @ApiOperation("查询打新列表")
    @AppLog(title = "查询打新列表", businessType = BusinessType.OTHER)
    @PostMapping("/getShengouList")
    public AjaxResult getShengouList()
    {
        try {
            LoginMember loginMember = getLoginMember();
            FaNewStock faNewStock = new FaNewStock();
            faNewStock.setMemberId(loginMember.getFaMember().getId());
            List<FaNewStock> list = faShengouService.getShengouListByGroup(faNewStock);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getShengouList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getShengouList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询配售列表(申购=配售)
     */
    @ApiOperation("查询配售列表")
    @AppLog(title = "查询配售列表", businessType = BusinessType.OTHER)
    @PostMapping("/getSgSecondList")
    public AjaxResult getSgSecondList()
    {
        try {
            LoginMember loginMember = getLoginMember();
            FaNewStock faNewStock = new FaNewStock();
            faNewStock.setMemberId(loginMember.getFaMember().getId());
            List<FaNewStock> list = faShengouService.getSgSecondList(faNewStock);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getSgSecondList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSgSecondList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询配售列表
     */
    @ApiOperation("查询配售列表")
    @AppLog(title = "查询配售列表", businessType = BusinessType.OTHER)
    @PostMapping("/getPeiShouList")
    public AjaxResult getPeiShouList()
    {
        try {
            List<FaNewStock> list = new ArrayList<>();

            // 配售类型(0配售1申购) 默认0
            String ps_type = iFaRiskConfigService.getConfigValue("ps_type", "0");
            if ("0".equals(ps_type)) {
                list = faShengouService.getPeiShouListByGroup();
            } else if ("1".equals(ps_type)) {
                LoginMember loginMember = getLoginMember();
                FaNewStock faNewStock = new FaNewStock();
                faNewStock.setMemberId(loginMember.getFaMember().getId());
                list = faShengouService.getSgSecondList(faNewStock);
            }

            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getPeiShouList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getPeiShouList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询新股详情
     */
    @ApiOperation("查询新股详情")
    @AppLog(title = "查询新股详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新股id", required = true, dataType = "Integer")})
    @PostMapping("/getShengouDetail")
    public AjaxResult getShengouDetail(@RequestBody FaNewStock faNewStock)
    {
        try {
            faNewStock = faShengouService.getShengouDetail(faNewStock);
            return AjaxResult.success(faNewStock);
        } catch (ServiceException e) {
            logger.error("getShengouDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getShengouDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 一键打新
     */
    @RepeatSubmit
    @ApiOperation("一键打新")
    @AppLog(title = "一键打新", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新股id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgNums", value = "申购数量", dataType = "Integer")
    })
    @PostMapping("/addShengou")
    public AjaxResult addShengou(@RequestBody FaNewStock faNewStock)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faNewStock.setMemberId(loginMember.getFaMember().getId());
            faShengouService.addShengou(faNewStock);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("addShengou", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addShengou", e);
            return AjaxResult.error();
        }
    }

    /**
     * 一键配售(申购=配售)
     */
    @RepeatSubmit
    @ApiOperation("一键配售")
    @AppLog(title = "一键配售", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新股id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgNums", value = "申购数量", dataType = "Integer")
    })
    @PostMapping("/addSgSecond")
    public AjaxResult addSgSecond(@RequestBody FaNewStock faNewStock)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faNewStock.setMemberId(loginMember.getFaMember().getId());
            faShengouService.addSgSecond(faNewStock);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("addSgSecond", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addSgSecond", e);
            return AjaxResult.error();
        }
    }

    /**
     * 一键配售
     */
    @RepeatSubmit
    @ApiOperation("一键配售")
    @AppLog(title = "一键配售", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新股id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgNums", value = "申购数量", dataType = "Integer"),
            @ApiImplicitParam(name = "content", value = "配售密钥(可选)", dataType = "String")
    })
    @PostMapping("/addPeiShou")
    public AjaxResult addPeiShou(@RequestBody FaNewStock faNewStock)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faNewStock.setMemberId(loginMember.getFaMember().getId());

            // 配售类型(0配售1申购) 默认0
            String ps_type = iFaRiskConfigService.getConfigValue("ps_type", "0");

            if ("0".equals(ps_type)) {
                faShengouService.addPeiShou(faNewStock);
            } else if ("1".equals(ps_type)) {
                faShengouService.addSgSecond(faNewStock);
            }

            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("addPeiShou", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("addPeiShou", e);
            return AjaxResult.error();
        }
    }

}
