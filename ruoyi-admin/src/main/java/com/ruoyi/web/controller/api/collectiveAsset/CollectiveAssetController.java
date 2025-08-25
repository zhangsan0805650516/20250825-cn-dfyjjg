package com.ruoyi.web.controller.api.collectiveAsset;

import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;
import com.ruoyi.biz.collectiveAsset.service.IFaCollectiveAssetService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 集合资产Controller
 * 
 * @author ruoyi
 * @date 2025-02-17
 */
@Api(tags = "集合资产")
@RestController
@RequestMapping("/api/collectiveAsset")
public class CollectiveAssetController extends BaseController
{
    @Autowired
    private IFaCollectiveAssetService faCollectiveAssetService;

    /**
     * 集合资产列表
     */
    @ApiOperation("集合资产列表")
    @AppLog(title = "集合资产列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetName", value = "资产名称", required = true, dataType = "string")
    })
    @PostMapping("/getAssetList")
    public AjaxResult assetList(@RequestBody FaCollectiveAsset faCollectiveAsset) throws Exception
    {
        try {
            List<FaCollectiveAsset> list = faCollectiveAssetService.getAssetList(faCollectiveAsset);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getAssetList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getAssetList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 买入集合资产详情
     */
    @ApiOperation("买入集合资产详情")
    @AppLog(title = "买入集合资产详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资产id", required = true, dataType = "integer"),
            @ApiImplicitParam(name = "orderBy", value = "排序(0时间 1金额)", dataType = "integer"),
            @ApiImplicitParam(name = "assetSecret", value = "资产邀请码", dataType = "integer")
    })
    @PostMapping("/getBuyAssetDetail")
    public AjaxResult getBuyAssetDetail(@RequestBody FaCollectiveAsset faCollectiveAsset) throws Exception
    {
        try {
            if (null == faCollectiveAsset.getOrderBy()) {
                faCollectiveAsset.setOrderBy(0);
            }
            faCollectiveAsset = faCollectiveAssetService.getBuyAssetDetail(faCollectiveAsset);
            return AjaxResult.success(faCollectiveAsset);
        } catch (ServiceException e) {
            logger.error("getBuyAssetDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBuyAssetDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 赎回集合资产详情
     */
    @ApiOperation("赎回集合资产详情")
    @AppLog(title = "赎回集合资产详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资产id", required = true, dataType = "integer")
    })
    @PostMapping("/getRedeemAssetDetail")
    public AjaxResult getRedeemAssetDetail(@RequestBody FaCollectiveAsset faCollectiveAsset) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faCollectiveAsset.setMemberId(loginMember.getFaMember().getId());

            if (null == faCollectiveAsset.getOrderBy()) {
                faCollectiveAsset.setOrderBy(0);
            }

            faCollectiveAsset = faCollectiveAssetService.getRedeemAssetDetail(faCollectiveAsset);
            return AjaxResult.success(faCollectiveAsset);
        } catch (ServiceException e) {
            logger.error("getRedeemAssetDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRedeemAssetDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 集合资产买入
     */
    @RepeatSubmit
    @ApiOperation("集合资产买入")
    @AppLog(title = "集合资产买入", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetId", value = "资产id", required = true, dataType = "integer"),
            @ApiImplicitParam(name = "amount", value = "买入金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true, dataType = "String")
    })
    @PostMapping("/buyAsset")
    public AjaxResult buyAsset(@RequestBody FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faCollectiveAssetRecord.setMemberId(loginMember.getFaMember().getId());
            faCollectiveAssetService.buyAsset(faCollectiveAssetRecord);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyAsset", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyAsset", e);
            return AjaxResult.error();
        }
    }

    /**
     * 集合资产赎回
     */
    @RepeatSubmit
    @ApiOperation("集合资产赎回")
    @AppLog(title = "集合资产赎回", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetId", value = "资产id", required = true, dataType = "integer"),
            @ApiImplicitParam(name = "amount", value = "赎回金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true, dataType = "String")
    })
    @PostMapping("/redeemAsset")
    public AjaxResult redeemAsset(@RequestBody FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faCollectiveAssetRecord.setMemberId(loginMember.getFaMember().getId());
            faCollectiveAssetService.redeemAsset(faCollectiveAssetRecord);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("redeemAsset", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("redeemAsset", e);
            return AjaxResult.error();
        }
    }

}
