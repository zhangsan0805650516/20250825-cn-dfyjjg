package com.ruoyi.web.controller.api.stockSgSecond;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockSgSecond.service.IFaStockSgSecondService;
import com.ruoyi.common.annotation.AppLog;
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

/**
 * 线下配售(世纪独享)
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "申购")
@RestController
@RequestMapping("/api/stockSgSecond")
public class StockSgSecondController extends BaseController
{

    @Autowired
    private IFaStockSgSecondService iFaStockSgSecondService;

    /**
     * 查询用户申购列表(配售 世纪独享)
     */
    @ApiOperation("查询用户申购列表(配售 世纪独享)")
    @AppLog(title = "查询用户申购列表(配售 世纪独享)", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgType", value = "1沪 2深 3创业 4北交 5科创", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "状态:0=申购中,1=中签,2=未中签,3=弃购", dataType = "Integer")
    })
    @PostMapping("/getMemberSgList")
    public AjaxResult getMemberSgList(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        try {
            if (null == faStockSgSecond.getPage()) {
                faStockSgSecond.setPage(1);
            }
            if (null == faStockSgSecond.getSize()) {
                faStockSgSecond.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faStockSgSecond.setUserId(loginMember.getFaMember().getId());
            IPage<FaStockSgSecond> faStockSgSecondIPage = iFaStockSgSecondService.getMemberStockSgSecondPage(faStockSgSecond);
            return AjaxResult.success(faStockSgSecondIPage);
        } catch (ServiceException e) {
            logger.error("getMemberSgList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getMemberSgList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 认缴
     */
    @ApiOperation("打新认缴")
    @AppLog(title = "打新认缴", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "申购id", required = true, dataType = "Integer")
    })
    @PostMapping("/subscription")
    public AjaxResult subscription(@RequestBody FaStockSgSecond faStockSgSecond)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faStockSgSecond.setUserId(loginMember.getFaMember().getId());
            iFaStockSgSecondService.subscription(faStockSgSecond);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("subscription", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getMemberSgList", e);
            return AjaxResult.error();
        }
    }

}
