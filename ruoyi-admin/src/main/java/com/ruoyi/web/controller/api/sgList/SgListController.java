package com.ruoyi.web.controller.api.sgList;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgList.service.IFaSgListService;
import com.ruoyi.common.annotation.AppLog;
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

/**
 * 申购列表Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "申购")
@RestController
@RequestMapping("/api/sgList")
public class SgListController extends BaseController
{
    @Autowired
    private IFaSgListService faSgListService;

    /**
     * 查询用户申购列表
     */
    @ApiOperation("查询用户打新列表")
    @AppLog(title = "查询用户打新列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgType", value = "1沪 2深 3创业 4北交 5科创", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "状态:0=申购中,1=中签,2=未中签,3=弃购", dataType = "Integer")
    })
    @PostMapping("/getMemberSgList")
    public AjaxResult getMemberSgList(@RequestBody FaSgList faSgList)
    {
        try {
            if (null == faSgList.getPage()) {
                faSgList.setPage(1);
            }
            if (null == faSgList.getSize()) {
                faSgList.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faSgList.setUserId(loginMember.getFaMember().getId());
            IPage<FaSgList> faSgListIPage = faSgListService.getMemberSgPage(faSgList);
            return AjaxResult.success(faSgListIPage);
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
    public AjaxResult subscription(@RequestBody FaSgList faSgList)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faSgList.setUserId(loginMember.getFaMember().getId());
            faSgListService.subscription(faSgList);
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
