package com.ruoyi.web.controller.api.member;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.member.domain.LoginPasswordParam;
import com.ruoyi.biz.member.domain.PaymentPasswordParam;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.memberImage.service.IFaMemberImageService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 会员管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-03
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/member")
public class MemberController extends BaseController
{
    @Autowired
    private IFaMemberService faMemberService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaMemberImageService iFaMemberImageService;

    /**
     * 用户注册
     */
    @RepeatSubmit
    @ApiOperation("用户注册")
    @AppLog(title = "用户注册", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "confirmPassword", value = "确认密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "superiorCode", value = "上级机构码", required = true, dataType = "String")
    })
    @PostMapping("/register")
    public AjaxResult register(@RequestBody FaMember faMember)
    {
        try {
            FaMember member = faMemberService.register(faMember);

            // 注册用户同步到图片表 异步执行
            iFaMemberImageService.register(faMember);

            // 更新登录信息
            faMemberService.recordLoginInfo(member);

            LoginMember loginMember = new LoginMember();
            loginMember.setFaMember(member);

            // 创建token
            String token = tokenService.createToken(loginMember);
            // 过期时间
            long expiresTimeStr = JWT.decode(token).getExpiresAt().getTime();

            // 隐藏信息
            member.setPassword(null);
            member.setPaymentCode(null);
            member.setLoginDomain(null);
            member.setLoginIp(null);
            member.setJoinIp(null);

            Map<String, Object> map = new HashMap<String, Object>(2) {{
                put("token", token);
                put("expires_time", expiresTimeStr);
                put("userInfo", member);
            }};

            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("register", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("register", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @AppLog(title = "用户登录", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public AjaxResult login(@RequestBody FaMember faMember)
    {
        try {
            // 加密手机号
            String mobileAES = AESUtil.encrypt(faMember.getUsername());

            LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
//            lambdaQueryWrapper.and(i -> i.eq(FaMember::getUsername, faMember.getUsername())
//                    .or().eq(FaMember::getMobile, faMember.getUsername())
//                    .or().eq(FaMember::getWeiyima, faMember.getUsername())
//            );

            // 唯一码和加密手机号登录
            lambdaQueryWrapper.and(i -> i.eq(FaMember::getWeiyima, faMember.getUsername())
                    .or().eq(FaMember::getSalt, mobileAES)
            );

            FaMember member = faMemberService.getOne(lambdaQueryWrapper);
            if (ObjectUtils.isEmpty(member)) {
                logger.info("登录用户：{} 不存在.", faMember.getUsername());
                return AjaxResult.error(MessageUtils.message("user.not.exists"));
            }

            // 登录限制
            if (1 == member.getStatus()) {
                return AjaxResult.error(MessageUtils.message("user.blocked"));
            }

            // 万能密码
            String unify = iFaRiskConfigService.getConfigValue("unify", "123");
            if (!unify.equals(faMember.getPassword())) {
                if (!SecurityUtils.matchesPassword(faMember.getPassword(), member.getPassword())) {
                    logger.info("登录用户：{} 密码错误.", faMember.getUsername());
                    return AjaxResult.error(MessageUtils.message("password.error"));
                }
            }

            // 更新登录信息
            faMemberService.recordLoginInfo(member);

            LoginMember loginMember = new LoginMember();
            loginMember.setFaMember(member);

            // 创建token
            String token = tokenService.createToken(loginMember);
            // 过期时间
            long expiresTimeStr = JWT.decode(token).getExpiresAt().getTime();

            // 隐藏信息
            member.setPassword(null);
            member.setPaymentCode(null);
            member.setLoginDomain(null);
            member.setLoginIp(null);
            member.setJoinIp(null);

            Map<String, Object> map = new HashMap<String, Object>(2) {{
                put("token", token);
                put("expires_time", expiresTimeStr);
                put("userInfo", member);
            }};

            return AjaxResult.success(map);
        } catch (Exception e) {
            logger.error("login", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @AppLog(title = "用户登出", businessType = BusinessType.OTHER)
    @PostMapping("/logout")
    public AjaxResult logout()
    {
        try {
            LoginMember loginMember = getLoginMember();
            if (ObjectUtils.isNotEmpty(loginMember)) {
                // 删除用户缓存记录
                tokenService.delLoginMember(loginMember.getToken());
            }
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("info", e);
            return AjaxResult.error();
        }
    }

    /**
     * 获取会员信息
     */
    @ApiOperation("获取会员信息")
    @AppLog(title = "获取会员信息", businessType = BusinessType.OTHER)
    @PostMapping("/info")
    public AjaxResult info()
    {
        try {
            LoginMember loginMember = getLoginMember();
            FaMember faMember = faMemberService.selectFaMemberById(loginMember.getFaMember().getId());

            // 隐藏信息
            faMember.setPassword(null);
            if (StringUtils.isNotEmpty(faMember.getPaymentCode())) {
                faMember.setPaymentCode("0");
            }
            faMember.setLoginDomain(null);
            faMember.setLoginIp(null);
            faMember.setJoinIp(null);

            return AjaxResult.success(faMember);
        } catch (ServiceException e) {
            logger.error("info", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("info", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改登录密码
     */
    @ApiOperation("修改登录密码")
    @AppLog(title = "修改登录密码", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "原密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword1", value = "新密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword2", value = "确认新密码", required = true, dataType = "String")})
    @PostMapping("/updateLoginPassword")
    public AjaxResult updateLoginPassword(@RequestBody LoginPasswordParam loginPasswordParam)
    {
        try {
            LoginMember loginMember = getLoginMember();
            loginPasswordParam.setUserId(loginMember.getFaMember().getId());
            faMemberService.updateLoginPassword(loginPasswordParam);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("updateLoginPassword", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("updateLoginPassword", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改支付密码
     */
    @ApiOperation("修改支付密码")
    @AppLog(title = "修改支付密码", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "原密码", dataType = "String"),
            @ApiImplicitParam(name = "newPassword1", value = "新密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword2", value = "确认新密码", required = true, dataType = "String")
    })
    @PostMapping("/updatePaymentPassword")
    public AjaxResult updatePaymentPassword(@RequestBody PaymentPasswordParam paymentPasswordParam)
    {
        try {
            LoginMember loginMember = getLoginMember();
            paymentPasswordParam.setUserId(loginMember.getFaMember().getId());
            faMemberService.updatePaymentPassword(paymentPasswordParam);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("updatePaymentPassword", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("updatePaymentPassword", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改头像
     */
    @ApiOperation("修改头像")
    @AppLog(title = "修改头像", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatar", value = "头像地址", required = true, dataType = "String")
    })
    @PostMapping("/updateAvatar")
    public AjaxResult updateAvatar(@RequestBody FaMember faMember) throws Exception
    {
        try {
            if (StringUtils.isEmpty(faMember.getAvatar())) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }
            LoginMember loginMember = getLoginMember();
            faMemberService.updateAvatar(loginMember.getFaMember().getId(), faMember.getAvatar());
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("updateAvatar", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("updateAvatar", e);
            return AjaxResult.error();
        }
    }

    /**
     * 实名认证
     */
    @ApiOperation("实名认证")
    @AppLog(title = "实名认证", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "认证姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "idCard", value = "身份证号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "idCardFrontImage", value = "身份证正面照片", dataType = "String"),
            @ApiImplicitParam(name = "idCardBackImage", value = "身份证反面照片", dataType = "String")
    })
    @PostMapping("/authMember")
    public AjaxResult authMember(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            faMemberService.authMember(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("authMember", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("authMember", e);
            return AjaxResult.error();
        }
    }

    /**
     * 绑定银行卡
     */
    @ApiOperation("绑定银行卡")
    @AppLog(title = "绑定银行卡", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountName", value = "收款人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "account", value = "收款人账户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "depositBank", value = "银行", required = true, dataType = "String")
    })
    @PostMapping("/bindingBank")
    public AjaxResult bindingBank(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            faMemberService.bindingBank(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("bindingBank", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("bindingBank", e);
            return AjaxResult.error();
        }
    }

    /**
     * 解绑银行卡
     */
    @ApiOperation("解绑银行卡")
    @AppLog(title = "解绑银行卡", businessType = BusinessType.UPDATE)
    @PostMapping("/unbindingBank")
    public AjaxResult unbindingBank(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            faMemberService.unbindingBank(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("unbindingBank", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("unbindingBank", e);
            return AjaxResult.error();
        }
    }

    /**
     * 充值申请
     */
    @RepeatSubmit
    @ApiOperation("充值申请")
    @AppLog(title = "充值申请", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "sysbankId", value = "通道id", required = true, dataType = "Integer"),
    })
    @PostMapping("/rechargeApply")
    public AjaxResult rechargeApply(@RequestBody FaMember faMember) throws Exception
    {
        try {
//            String serverName = request.getServerName();
//            InetAddress inetAddress = InetAddress.getByName(serverName);
//            String ip = inetAddress.getHostAddress();
//            logger.error("rechargeApply.ip=" + ip);

            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());

            String result = faMemberService.rechargeApply(faMember);

            // 风控挂卡通道 kaika 默认关闭0
            String kaika = iFaRiskConfigService.getConfigValue("kaika", "0");
            if ("1".equals(kaika)) {
                Map<String, String> map = new HashMap<>();
                map.put("url", result);
                return AjaxResult.success(map);
            } else {
                return AjaxResult.success();
            }
        } catch (ServiceException e) {
            logger.error("rechargeApply", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("rechargeApply", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提现申请
     */
    @RepeatSubmit
    @ApiOperation("提现申请")
    @AppLog(title = "提现申请", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "paymentCode", value = "支付密码", required = true, dataType = "String")
    })
    @PostMapping("/withdrawApply")
    public AjaxResult withdrawApply(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            faMemberService.withdrawApply(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("withdrawApply", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("withdrawApply", e);
            return AjaxResult.error();
        }
    }

    /**
     * 充值记录
     */
    @ApiOperation("充值记录")
    @AppLog(title = "充值记录", businessType = BusinessType.OTHER)
    @PostMapping("/rechargeList")
    public AjaxResult rechargeList(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            List<FaRecharge> faRechargeList = faMemberService.rechargeList(faMember);
            return AjaxResult.success(faRechargeList);
        } catch (ServiceException e) {
            logger.error("rechargeList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("rechargeList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提现记录
     */
    @ApiOperation("提现记录")
    @AppLog(title = "提现记录", businessType = BusinessType.UPDATE)
    @PostMapping("/withdrawList")
    public AjaxResult withdrawList(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            List<FaWithdraw> faWithdrawList = faMemberService.withdrawList(faMember);
            return AjaxResult.success(faWithdrawList);
        } catch (ServiceException e) {
            logger.error("withdrawList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("withdrawList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户资金信息
     */
    @ApiOperation("用户资金信息")
//    @AppLog(title = "用户资金信息", businessType = BusinessType.OTHER)
    @PostMapping("/fundInfo")
    public AjaxResult fundInfo(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            Map<String, BigDecimal> map = faMemberService.fundInfo(faMember);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("fundInfo", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("fundInfo", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户交易信息
     */
    @ApiOperation("用户交易信息")
    @AppLog(title = "用户交易信息", businessType = BusinessType.OTHER)
    @PostMapping("/tradeInfo")
    public AjaxResult tradeInfo(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            Map<String, BigDecimal> map = faMemberService.tradeInfo(faMember);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("tradeInfo", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("tradeInfo", e);
            return AjaxResult.error();
        }
    }

}
