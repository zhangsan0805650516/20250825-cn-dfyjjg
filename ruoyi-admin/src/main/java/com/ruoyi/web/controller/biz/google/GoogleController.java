package com.ruoyi.web.controller.biz.google;

import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.google.GoogleUtil;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "谷歌验证器")
@RestController
@RequestMapping("/biz/google")
public class GoogleController extends BaseController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 生成二维码
     */
    @ApiOperation("生成二维码扫描绑定")
    @Log(title = "生成二维码扫描绑定", businessType = BusinessType.UPDATE)
    @PostMapping("/getQrcode")
    public AjaxResult getQrcode()
    {
        try {
            Map<String, Object> map = new HashMap<>();
            LoginUser loginUser = getLoginUser();

            // 谷歌验证器开关，默认关
            int isGoogleAuth = Integer.parseInt(iFaRiskConfigService.getConfigValue("is_google_auth", "0"));
            // 管理员谷歌验证器开关，默认关
            int isGoogleAuthAdmin = Integer.parseInt(iFaRiskConfigService.getConfigValue("is_google_auth_admin", "0"));

            // 谷歌验证器开启
            if (1 == isGoogleAuth) {
                // 管理员关 且 为管理员
                if (0 == isGoogleAuthAdmin && 1 == loginUser.getUserId()) {
                    map.put("isBinding", 1);
                }
                // 其他情况全部进入判断
                else {
                    SysUser sysUser = iSysUserService.selectUserById(loginUser.getUserId());
                    // 已绑定
                    if (1 == sysUser.getBindingGoogleSecret()) {
                        map.put("isBinding", 1);
                    }
                    // 未绑定
                    else if (0 == sysUser.getBindingGoogleSecret()) {
                        map.put("isBinding", 0);
                        String googleSecret = sysUser.getGoogleSecret();
                        if (StringUtils.isEmpty(googleSecret)) {
                            googleSecret = GoogleUtil.generateSecretKey();
                        }
                        String qrCodeText = GoogleUtil.getQRBarcode(sysUser.getUserName(), googleSecret);
                        map.put("secret", googleSecret);
                        map.put("qrCode", qrCodeText);
                    }
                }
            } else {
                map.put("isBinding", 1);
            }
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getQrcode", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getQrcode", e);
            return AjaxResult.error();
        }
    }

    /**
     * 验证code是否正确
     */
    @ApiOperation("验证code")
    @Log(title = "验证code", businessType = BusinessType.UPDATE)
    @PostMapping("/checkCode")
    public AjaxResult checkCode(@RequestBody SysUser sysUser) {
        try {
            LoginUser loginUser = getLoginUser();
            SysUser user = iSysUserService.selectUserById(loginUser.getUserId());
            boolean success = GoogleUtil.checkCode(user.getGoogleSecret(), Integer.parseInt(sysUser.getCode()));
            if (success) {
                // 状态存入redis，保存24小时
                redisCache.setCacheObject(Constants.GOOGLE_AUTH_STATUS + ":" + user.getUserId(), "success", 1, TimeUnit.DAYS);
                return AjaxResult.success();
            }
            // 清掉token
            return AjaxResult.error("验证码错误");
        } catch (ServiceException e) {
            logger.error("checkCode", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("checkCode", e);
            return AjaxResult.error();
        }
    }

    /**
     * 检查Google校验状态
     */
    @ApiOperation("检查Google校验状态")
    @PostMapping("/checkAuthStatus")
    public AjaxResult checkAuthStatus(@RequestBody SysUser sysUser) {
        try {
            Map<String, Object> map = new HashMap<>();
            LoginUser loginUser = getLoginUser();

            // 谷歌验证器开关，默认关
            int isGoogleAuth = Integer.parseInt(iFaRiskConfigService.getConfigValue("is_google_auth", "0"));
            // 管理员谷歌验证器开关，默认关
            int isGoogleAuthAdmin = Integer.parseInt(iFaRiskConfigService.getConfigValue("is_google_auth_admin", "0"));

            // 谷歌验证器开启
            map.put("notBinding", false);
            if (1 == isGoogleAuth) {
                // 管理员关 且 为管理员
                if (0 == isGoogleAuthAdmin && 1 == loginUser.getUserId()) {
                    map.put("googleAuth", true);
                }
                // 其他情况全部验证码
                else {
                    SysUser user = iSysUserService.selectUserById(loginUser.getUserId());
                    if (0 == user.getBindingGoogleSecret()) {
                        map.put("notBinding", true);
                    }

                    String success = redisCache.getCacheObject(Constants.GOOGLE_AUTH_STATUS + ":" + loginUser.getUserId());
                    if (StringUtils.isNotEmpty(success) && "success".equals(success)) {
                        map.put("googleAuth", true);
                    } else {
                        map.put("googleAuth", false);
                    }
                }
            } else {
                map.put("googleAuth", true);
            }
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("checkAuthStatus", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("checkAuthStatus", e);
            return AjaxResult.error();
        }
    }

    /**
     * 绑定Secret
     */
    @ApiOperation("绑定Secret")
    @Log(title = "绑定Secret", businessType = BusinessType.UPDATE)
    @PostMapping("/bindingSecret")
    public AjaxResult bindingSecret(@RequestBody SysUser sysUser) {
        try {
            LoginUser loginUser = getLoginUser();
            SysUser user = new SysUser();
            user.setUserId(loginUser.getUser().getUserId());
            user.setBindingGoogleSecret(1);
            user.setUpdateTime(new Date());
            iSysUserService.bindingGoogleSecret(user);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("bindingSecret", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("bindingSecret", e);
            return AjaxResult.error();
        }
    }

}
