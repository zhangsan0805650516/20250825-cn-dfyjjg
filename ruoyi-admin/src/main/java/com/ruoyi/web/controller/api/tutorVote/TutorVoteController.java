package com.ruoyi.web.controller.api.tutorVote;

import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;
import com.ruoyi.biz.tutorVote.service.IFaTutorVoteService;
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
 * 分类Controller
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Api(tags = "投票")
@RestController
@RequestMapping("/api/tutorVote")
public class TutorVoteController extends BaseController
{
    @Autowired
    private IFaTutorVoteService faTutorVoteService;

    /**
     * 投票
     */
    @RepeatSubmit
    @ApiOperation("投票")
    @AppLog(title = "投票", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tutorId", value = "导师id", required = true, dataType = "Integer"),
    })
    @PostMapping("/voteTutor")
    public AjaxResult voteTutor(@RequestBody FaTutorVote faTutorVote) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faTutorVote.setUserId(loginMember.getFaMember().getId());
            faTutorVoteService.voteTutor(faTutorVote);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("voteTutor", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("voteTutor", e);
            return AjaxResult.error();
        }
    }

    /**
     * 投票记录
     */
    @ApiOperation("投票记录")
    @AppLog(title = "投票记录", businessType = BusinessType.OTHER)
    @PostMapping("/getVoteList")
    public AjaxResult getVoteList(@RequestBody FaTutorVote faTutorVote) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faTutorVote.setUserId(loginMember.getFaMember().getId());
            List<FaTutorVote> list = faTutorVoteService.getVoteList(faTutorVote);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getVoteList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getVoteList", e);
            return AjaxResult.error();
        }
    }

}
