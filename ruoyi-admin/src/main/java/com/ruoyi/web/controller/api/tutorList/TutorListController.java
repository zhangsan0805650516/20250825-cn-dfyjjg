package com.ruoyi.web.controller.api.tutorList;

import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.biz.tutorList.service.IFaTutorListService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 导师Controller
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Api(tags = "导师")
@RestController
@RequestMapping("/api/tutorList")
public class TutorListController extends BaseController
{
    @Autowired
    private IFaTutorListService faTutorListService;

    /**
     * 获取导师列表
     */
    @ApiOperation("获取导师列表")
    @AppLog(title = "获取导师列表", businessType = BusinessType.OTHER)
    @PostMapping("/getTutorList")
    public AjaxResult getTutorList(@RequestBody FaTutorList faTutorList) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faTutorList.setUserId(loginMember.getFaMember().getId());
            List<FaTutorList> list = faTutorListService.getTutorList(faTutorList);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getTutorList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getTutorList", e);
            return AjaxResult.error();
        }
    }

}
