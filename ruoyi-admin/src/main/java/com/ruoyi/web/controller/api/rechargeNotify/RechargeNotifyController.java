package com.ruoyi.web.controller.api.rechargeNotify;

import com.ruoyi.biz.recharge.domain.*;
import com.ruoyi.biz.rechargeNotify.RechargeNotifyService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;


/**
 * 会员管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-03
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/rechargeNotify")
public class RechargeNotifyController extends BaseController
{
    @Autowired
    private RechargeNotifyService rechargeNotifyService;

    /**
     * 充值回调接口json火箭
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口json火箭")
    @AppLog(title = "充值回调接口json火箭", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyHuojian")
    public void rechargeNotifyHuojian(HttpServletResponse response, @RequestBody RechargeNotify rechargeNotifyJson) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotifyJson);

            rechargeNotifyService.huojianRechargeNotify(rechargeNotifyJson);
            response.getWriter().write("SUCCESS");
        } catch (Exception e) {
            logger.error("rechargeNotifyHuojian", e);
        }
    }

    /**
     * 充值回调接口json九哥
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口json九哥")
    @AppLog(title = "充值回调接口json九哥", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyJsonNineBrother")
    public void rechargeNotifyJsonNineBrother(HttpServletResponse response, @RequestBody RechargeNotify rechargeNotifyJson) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotifyJson);

            rechargeNotifyService.nineBrotherRechargeNotify(rechargeNotifyJson);
            response.getWriter().write("SUCCESS");
        } catch (Exception e) {
            logger.error("rechargeNotifyJsonNineBrother", e);
        }
    }

    /**
     * 充值回调接口json仁德
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口json仁德")
    @AppLog(title = "充值回调接口json仁德", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyJsonRende")
    public void rechargeNotifyJsonRende(HttpServletResponse response, @RequestBody RechargeNotify rechargeNotifyJson) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotifyJson);

            rechargeNotifyService.rendeRechargeNotify(rechargeNotifyJson);
            response.getWriter().write("true");
        } catch (Exception e) {
            logger.error("rechargeNotifyJsonRende", e);
        }
    }

    /**
     * 充值回调接口form四方
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form四方")
    @AppLog(title = "充值回调接口form四方", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormSifang")
    public void rechargeNotifyFormSifang(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.sifangRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("OK");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormSifang", e);
        }
    }

    /**
     * 充值回调接口json代付
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口json代付")
    @AppLog(title = "充值回调接口json代付", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyJsonDaiFu")
    public void rechargeNotifyJsonDaiFu(HttpServletResponse response, @RequestBody RechargeNotify rechargeNotifyJson) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotifyJson);

            rechargeNotifyService.daiFuRechargeNotify(rechargeNotifyJson);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyJsonDaiFu", e);
        }
    }

    /**
     * 充值回调接口form FML
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form FML")
    @AppLog(title = "充值回调接口form FML", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormFML")
    public void rechargeNotifyFormFML(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.FMLRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("OK");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormFML", e);
        }
    }

    /**
     * 充值回调接口form亨通
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form亨通")
    @AppLog(title = "充值回调接口form亨通", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormHengTong")
    public void rechargeNotifyFormHengTong(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.hengTongRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormHengTong", e);
        }
    }

    /**
     * 充值回调接口form杉德宝
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form杉德宝")
    @AppLog(title = "充值回调接口form杉德宝", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyShanDeBao")
    public void rechargeNotifyShanDeBao(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotifyForm);

            rechargeNotifyService.shanDeBaoRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyShanDeBao", e);
        }
    }

    /**
     * 充值回调接口form XL
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form XL")
    @AppLog(title = "充值回调接口form XL", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormXl")
    public void rechargeNotifyFormXl(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.xlRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormXl", e);
        }
    }

    /**
     * 充值回调接口form 鲨鱼
     */
    @ApiOperation("充值回调接口form 鲨鱼")
    @AppLog(title = "充值回调接口form 鲨鱼", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormShaYu")
    public void rechargeNotifyFormShaYu(HttpServletResponse response, ShaYuRechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.shaYuRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("ok");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormFML", e);
        }
    }

    /**
     * 充值回调接口form AX
     */
    @ApiOperation("充值回调接口json AX")
    @AppLog(title = "充值回调接口json AX", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyAXpay")
    public void rechargeNotifyAXpay(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.axRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyAXpay", e);
        }
    }

    /**
     * 充值回调接口form 海贼
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form 海贼")
    @AppLog(title = "充值回调接口form 海贼", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormHaiZei")
    public void rechargeNotifyFormHaiZei(HttpServletResponse response, RechargeNotify rechargeNotifyForm) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotifyForm);

            rechargeNotifyService.haiZeiRechargeNotify(rechargeNotifyForm);
            response.getWriter().write("ok");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormHaiZei", e);
        }
    }

    /**
     * 充值回调接口form 鸿运
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form 鸿运")
    @AppLog(title = "充值回调接口form 鸿运", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormHongYun")
    public void rechargeNotifyFormHongYun(HttpServletResponse response, HongYunRechargeNotify hongYunRechargeNotify) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + hongYunRechargeNotify);

            rechargeNotifyService.hongYunRechargeNotify(hongYunRechargeNotify);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormHongYun", e);
        }
    }

    /**
     * 充值回调接口json中信
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口json中信")
    @AppLog(title = "充值回调接口json中信", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyJsonZhongXin")
    public void rechargeNotifyJsonZhongXin(HttpServletResponse response, @RequestBody ZhongXinRechargeNotify rechargeNotify) throws Exception
    {
        try {
            logger.error("rechargeNotify1=" + response);
            logger.error("rechargeNotifyJSON=" + rechargeNotify);

            rechargeNotifyService.zhongXinRechargeNotify(rechargeNotify);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyJsonZhongXin", e);
        }
    }

    /**
     * 充值回调接口form 伟翰
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form 伟翰")
    @AppLog(title = "充值回调接口form 伟翰", businessType = BusinessType.UPDATE)
    @PostMapping("/rechargeNotifyFormWeiHan")
    public void rechargeNotifyFormWeiHan(HttpServletResponse response, RechargeNotify rechargeNotify) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + rechargeNotify);

            rechargeNotifyService.rechargeNotifyFormWeiHan(rechargeNotify);
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormWeiHan", e);
        }
    }

    /**
     * 充值回调接口form飞云
     */
    @RepeatSubmit
    @ApiOperation("充值回调接口form飞云")
    @AppLog(title = "充值回调接口form飞云", businessType = BusinessType.UPDATE)
    @GetMapping("/rechargeNotifyFormFeiYun")
    public void rechargeNotifyFormFeiYun(HttpServletResponse response, FeiYunRechargeNotify feiYunRechargeNotify) throws Exception
    {
        try {
            logger.error("rechargeNotify2=" + response);
            logger.error("rechargeNotifyForm=" + feiYunRechargeNotify);

            rechargeNotifyService.feiYunRechargeNotify(feiYunRechargeNotify);
            response.getWriter().write("ok");
        } catch (Exception e) {
            logger.error("rechargeNotifyFormFeiYun", e);
        }
    }

    @RepeatSubmit
    @ApiOperation("充值回调")
    @AppLog(title = "充值回调", businessType = BusinessType.UPDATE)
    @RequestMapping(value = "/rechargeNotify/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public void rechargeNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer id) throws Exception
    {
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String json = jsonBuilder.toString();
            if (StringUtils.isNotEmpty(json)) {
                logger.error("rechargeNotify.json=" + json);
                String responseWord = rechargeNotifyService.rechargeNotify(json, id);
                response.getWriter().write(responseWord);
            } else {
                logger.error("rechargeNotify.request=" + request);
                String responseWord = rechargeNotifyService.rechargeNotify(request, id);
                response.getWriter().write(responseWord);
            }
        } catch (Exception e) {
            logger.error("rechargeNotify", e);
        }
    }

}
