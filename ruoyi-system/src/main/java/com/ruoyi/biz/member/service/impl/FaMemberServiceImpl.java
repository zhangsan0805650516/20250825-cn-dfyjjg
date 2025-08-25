package com.ruoyi.biz.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.common.ApiCommonService;
import com.ruoyi.biz.fundPool.service.FundPoolService;
import com.ruoyi.biz.member.domain.LoginPasswordParam;
import com.ruoyi.biz.member.domain.PaymentPasswordParam;
import com.ruoyi.biz.member.mapper.FaMemberMapper;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.memberImage.domain.FaMemberImage;
import com.ruoyi.biz.memberImage.service.IFaMemberImageService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.recharge.service.IFaRechargeService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.biz.withdraw.service.IFaWithdrawService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.file.ThumbnailatorUtil;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * 会员管理Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-04
 */
@Service
public class FaMemberServiceImpl extends ServiceImpl<FaMemberMapper, FaMember> implements IFaMemberService
{
    @Autowired
    private FaMemberMapper faMemberMapper;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaRechargeService iFaRechargeService;

    @Autowired
    private IFaWithdrawService iFaWithdrawService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IFaSgjiaoyiService iFaSgjiaoyiService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ApiCommonService apiCommonService;

    @Autowired
    private FundPoolService fundPoolService;

    @Autowired
    private IFaMemberImageService iFaMemberImageService;

    /**
     * 查询会员管理
     *
     * @param id 会员管理主键
     * @return 会员管理
     */
    @Override
    public FaMember selectFaMemberById(Integer id) throws Exception
    {
        FaMember faMember = faMemberMapper.selectFaMemberById(id);
        if (ObjectUtils.isNotEmpty(faMember) && null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            // 代理是客服
            if (1 == sysUser.getIsKf()) {
                faMember.setKfUrl(sysUser.getKfUrl());
            } else {
                faMember.setKfUrl("");
            }
        }
        if (1 == faMember.getStatus() || 1 == faMember.getDeleteFlag()) {
            Thread thread = new Thread(kickoutMember(faMember.getId()));
            thread.start();
        }
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, id);
        lambdaUpdateWrapper.set(FaMember::getLoginTime, new Date());
        this.update(lambdaUpdateWrapper);

        // 资金池余额
        BigDecimal zjcBalance = fundPoolService.getFundPoolBalance();

        faMember.setZjcBalance(zjcBalance);

        // 取图片表base64
        if (3 == faMember.getIsAuth()) {
            LambdaQueryWrapper<FaMemberImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMemberImage::getMemberId, faMember.getId());
            FaMemberImage faMemberImage = iFaMemberImageService.getOne(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(faMemberImage)) {
                faMember.setIdCardFrontImage(faMemberImage.getIdCardFrontImage());
                faMember.setIdCardBackImage(faMemberImage.getIdCardBackImage());
            }
        } else {
            faMember.setIdCardFrontImage(null);
            faMember.setIdCardBackImage(null);
        }

        return faMember;
    }

    /**
     * 查询会员管理列表
     *
     * @param faMember 会员管理
     * @return 会员管理
     */
    @Override
    public List<FaMember> selectFaMemberList(FaMember faMember)
    {
        try {
            faMember.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faMember.getMobile())) {
                faMember.setSalt(AESUtil.encrypt(faMember.getMobile()));
                faMember.setMobile(null);
            }

            List<FaMember> list = faMemberMapper.selectFaMemberList(faMember);
            if (!list.isEmpty()) {
                for (FaMember member : list) {
                    List<BigDecimal> amounts = faMemberMapper.getFundInfos(member.getId());

                    // 新股资金 = 新股申购资金 + 新股申购资金
                    // 新股申购资金
                    BigDecimal sg = amounts.get(0);
                    // 新股配售资金
                    BigDecimal ps = amounts.get(1);
                    BigDecimal fundInfoNew = sg.add(ps);
                    member.setNewStockFreeze(fundInfoNew);

                    // 持仓市值 = 上市股票持仓 + 未上市新股转持仓
                    BigDecimal fundInfoMarketListed = amounts.get(2);
                    BigDecimal fundInfoMarketUnlisted = amounts.get(3);
                    BigDecimal fundInfoMarket = fundInfoMarketListed.add(fundInfoMarketUnlisted);
                    member.setTakeUp(fundInfoMarket);

                    // 总盈亏 = 持仓市值 + 总提现 + 余额 + 新股申购资金 - 总充值
                    // 总充值
                    BigDecimal recharge = amounts.get(4);
                    // 总提现
                    BigDecimal withdraw = amounts.get(5);
                    member.setTotalProfit(fundInfoMarket.add(withdraw).add(member.getBalance()).add(fundInfoNew).subtract(recharge).setScale(2, RoundingMode.HALF_UP));

                    // 总资产 = 余额 + 新股 + 持仓市值
                    BigDecimal total = member.getBalance().add(fundInfoNew).add(fundInfoMarket);
                    member.setTotal(total);

                    // 交易盈亏
                    BigDecimal tradeProfit = amounts.get(6);

                    // 总手续费
                    BigDecimal totalPoundage = amounts.get(7);

                    // 总盈亏 = 交易盈亏 - 总手续费
                    member.setTotalProfit(tradeProfit.subtract(totalPoundage));

                    // 隐藏图片
                    member.setAvatar(null);
                    member.setIdCardFrontImage(null);
                    member.setIdCardBackImage(null);
                    member.setCardImage(null);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出会员管理列表
     * @param faMember
     * @return
     */
    @Override
    public List<FaMember> selectFaMemberListForExport(FaMember faMember) throws Exception
    {
        faMember.setDeleteFlag(0);
        List<FaMember> list = faMemberMapper.selectFaMemberListForExport(faMember);
        return list;
    }

    /**
     * 查询会员实名认证列表
     * @param faMember
     * @return
     */
    @Override
    public List<FaMember> authMemberList(FaMember faMember) {
        try {
            faMember.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faMember.getMobile())) {
                faMember.setSalt(AESUtil.encrypt(faMember.getMobile()));
                faMember.setMobile(null);
            }

            List<FaMember> list = faMemberMapper.authMemberList(faMember);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询会员绑卡列表
     * @param faMember
     * @return
     */
    @Override
    public List<FaMember> memberBankList(FaMember faMember) {
        try {
            faMember.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faMember.getMobile())) {
                faMember.setSalt(AESUtil.encrypt(faMember.getMobile()));
                faMember.setMobile(null);
            }

            List<FaMember> list = faMemberMapper.memberBankList(faMember);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaMember(FaMember faMember) throws Exception
    {
        faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
        faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        faMember.setCreateTime(DateUtils.getNowDate());

        // 取代理数据
        if (null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            if (ObjectUtils.isNotEmpty(sysUser)) {
                faMember.setSuperiorId(sysUser.getUserId().intValue());
                faMember.setSuperiorCode(sysUser.getInstitutionNo());
                faMember.setSuperiorName(sysUser.getNickName());
            }
        }

        // 手机号校验
        checkMobile(faMember.getMobile());
        faMember.setUsername(faMember.getMobile());
        // 校验机构码
        checkInstitutionNumber(faMember.getInstitutionNumber());
        return faMemberMapper.insertFaMember(faMember);
    }

    private void checkInstitutionNumber(String institutionNumber) throws Exception {
        if (StringUtils.isNotEmpty(institutionNumber)) {
            LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMember::getInstitutionNumber, institutionNumber);
            lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
            int count = (int) this.count(lambdaQueryWrapper);
            if (count > 0) {
                throw new ServiceException(MessageUtils.message("institution.number.exists"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 修改会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaMember(FaMember faMember)
    {

        faMember.setUsername(null);
        faMember.setMobile(null);

        // 取代理数据
        if (null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            if (ObjectUtils.isNotEmpty(sysUser)) {
                faMember.setSuperiorId(sysUser.getUserId().intValue());
                faMember.setSuperiorCode(sysUser.getInstitutionNo());
                faMember.setSuperiorName(sysUser.getNickName());
            }
        }

        if ("******".equals(faMember.getPassword())) {
            faMember.setPassword(null);
        } else {
            faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
        }
        if ("******".equals(faMember.getPaymentCode())) {
            faMember.setPaymentCode(null);
        } else {
            faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        }
        faMember.setUpdateTime(DateUtils.getNowDate());
        faMember.setNickname(faMember.getName());
        return faMemberMapper.updateFaMember(faMember);
    }

    /**
     * 批量删除会员管理
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberByIds(Integer[] ids)
    {
        return faMemberMapper.deleteFaMemberByIds(ids);
    }

    /**
     * 批量删除实名认证
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int delAuthMemberByIds(Integer[] ids)
    {
        return faMemberMapper.delAuthMemberByIds(ids);
    }

    /**
     * 批量删除绑卡
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int delMemberBankByIds(Integer[] ids)
    {
        return faMemberMapper.delMemberBankByIds(ids);
    }

    /**
     * 删除会员管理信息
     *
     * @param id 会员管理主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberById(Integer id)
    {
        return faMemberMapper.deleteFaMemberById(id);
    }

    /**
     * 修改会员状态
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public AjaxResult changeMemberStatus(FaMember faMember) throws Exception {
        if (StringUtils.isNotEmpty(faMember.getStatusType())) {
            LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
            switch (faMember.getStatusType()) {
                case "jiaoyi":
                    lambdaUpdateWrapper.set(FaMember::getJingzhijiaoyi, faMember.getStatus());
                    break;
                case "denglu":
                    lambdaUpdateWrapper.set(FaMember::getStatus, faMember.getStatus());

                    // 如果限制登录，先把用户踢出登录状态，异步执行
                    if (1 == faMember.getStatus()) {
                        Thread thread = new Thread(kickoutMember(faMember.getId()));
                        thread.start();
                    }
                    break;
                case "peizi":
                    lambdaUpdateWrapper.set(FaMember::getIsPz, faMember.getStatus());
                    break;
                case "dazong":
                    lambdaUpdateWrapper.set(FaMember::getIsDz, faMember.getStatus());
                    break;
                case "peishou":
                    lambdaUpdateWrapper.set(FaMember::getIsPs, faMember.getStatus());
                    break;
                case "shengou":
                    lambdaUpdateWrapper.set(FaMember::getIsSg, faMember.getStatus());
                    break;
                case "zhishu":
                    lambdaUpdateWrapper.set(FaMember::getIsZs, faMember.getStatus());
                    break;
                case "qiangchou":
                    lambdaUpdateWrapper.set(FaMember::getIsQc, faMember.getStatus());
                    break;
                case "qihuo":
                    lambdaUpdateWrapper.set(FaMember::getIsQh, faMember.getStatus());
                    break;
                case "withdraw":
                    lambdaUpdateWrapper.set(FaMember::getWithdrawSwitch, faMember.getStatus());
                    break;
                default:
                    break;
            }
            this.update(lambdaUpdateWrapper);
            return AjaxResult.success(MessageUtils.message("operation.success"));
        }
        return AjaxResult.error();
    }

    /**
     * 踢出用户
     * @param id
     * @return
     * @throws Exception
     */
    private Runnable kickoutMember(Integer id) throws Exception {
        return () -> {
            // 取出所有在线用户
            Collection<String> keys = redisCache.keys(CacheConstants.MEMBER_LOGIN_TOKEN_KEY + "*");
            for (String key : keys) {
                LoginMember loginMember = redisCache.getCacheObject(key);
                // 确认用户
                if (ObjectUtils.isNotEmpty(loginMember) && id.equals(loginMember.getFaMember().getId())) {
                    redisCache.deleteObject(key);
                }
            }
        };
    }

    /**
     * 提交实名认证
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public void submitAuthMember(FaMember faMember) throws Exception {
        if (null == faMember.getId() || StringUtils.isEmpty(faMember.getName()) || StringUtils.isEmpty(faMember.getIdCard()) || null == faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getName, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getIdCard, faMember.getIdCard());
        if (StringUtils.isNotEmpty(faMember.getIdCardFrontImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardFrontImage, faMember.getIdCardFrontImage());
        }
        if (StringUtils.isNotEmpty(faMember.getIdCardBackImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardBackImage, faMember.getIdCardBackImage());
        }
        lambdaUpdateWrapper.set(FaMember::getIsAuth, faMember.getIsAuth());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        // 提交审核
        if (1 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthRejectReason, null);
            lambdaUpdateWrapper.set(FaMember::getAuthRejectTime, null);
        }
        // 审核通过
        if (2 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthTime, new Date());
        }
        // 审核失败
        if (3 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthRejectReason, faMember.getAuthRejectReason());
            lambdaUpdateWrapper.set(FaMember::getAuthRejectTime, new Date());
        }

        // 提交审核时间
        if (null == selectOne.getSubmitAuthTime()) {
            lambdaUpdateWrapper.set(FaMember::getSubmitAuthTime, new Date());
        }

        // 昵称
        lambdaUpdateWrapper.set(FaMember::getNickname, faMember.getName());

        this.update(lambdaUpdateWrapper);

        // 异步保存用户认证图片
        iFaMemberImageService.updateImage(faMember);
    }

    /**
     * 提交绑定银行卡
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public void submitBindingBank(FaMember faMember) throws Exception {
        if (null == faMember.getId() || StringUtils.isEmpty(faMember.getAccountName()) || StringUtils.isEmpty(faMember.getAccount())
                || StringUtils.isEmpty(faMember.getDepositBank())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, faMember.getAccountName());
        lambdaUpdateWrapper.set(FaMember::getAccount, faMember.getAccount());
        lambdaUpdateWrapper.set(FaMember::getDepositBank, faMember.getDepositBank());
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, faMember.getKhzhihang());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());

        if (StringUtils.isNotEmpty(faMember.getCardImage())) {
            lambdaUpdateWrapper.set(FaMember::getCardImage, faMember.getCardImage());
        }

        // 审核状态
        lambdaUpdateWrapper.set(FaMember::getBankCardAuth, faMember.getBankCardAuth());

        // 提交审核时间
        if (null == selectOne.getBindingTime()) {
            lambdaUpdateWrapper.set(FaMember::getBindingTime, new Date());
        }

        this.update(lambdaUpdateWrapper);

        // 异步保存用户银行卡图片
        iFaMemberImageService.updateImage(faMember);
    }

    /**
     * 更新登录信息
     * @param faMember
     * @throws Exception
     */
    @Override
    public void recordLoginInfo(FaMember faMember) throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());

        // 连续登录天数
        int successions = faMember.getSuccessions();
        // 最大连续登录天数
        int maxSuccessions = faMember.getMaxSuccessions();
        // 计算是否超出48小时，否则连续登录天数加1
        long now = new Date().getTime();
        if (null == faMember.getLoginTime()) {
            faMember.setLoginTime(new Date());
        }
        long old = faMember.getLoginTime().getTime();
        long betweenDays = (now - old) / 24 / 60 / 60 / 1000;
        if (betweenDays < 2) {
            successions++;
            lambdaUpdateWrapper.set(FaMember::getSuccessions, successions);
            if (successions > maxSuccessions) {
                maxSuccessions = successions;
                lambdaUpdateWrapper.set(FaMember::getMaxSuccessions, maxSuccessions);
            }
        } else {
            successions = 1;
            lambdaUpdateWrapper.set(FaMember::getSuccessions, successions);
        }
        lambdaUpdateWrapper.set(FaMember::getPrevTime, faMember.getLoginTime());
        lambdaUpdateWrapper.set(FaMember::getLoginTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getLoginIp, IpUtils.getIpAddr());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户登录密码
     * @param loginPasswordParam
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateLoginPassword(LoginPasswordParam loginPasswordParam) throws Exception {
        if (StringUtils.isEmpty(loginPasswordParam.getOldPassword()) || StringUtils.isEmpty(loginPasswordParam.getNewPassword1()) ||
                StringUtils.isEmpty(loginPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember faMember = this.getById(loginPasswordParam.getUserId());
        // 用户不存在
        if (ObjectUtils.isEmpty(faMember)) {
            throw new UserNotExistsException();
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        if (!loginPasswordParam.getNewPassword1().equals(loginPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }
        if (!SecurityUtils.matchesPassword(loginPasswordParam.getOldPassword(), faMember.getPassword())) {
            throw new ServiceException(MessageUtils.message("user.old.password.error"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getPassword, SecurityUtils.encryptPassword(loginPasswordParam.getNewPassword1()));
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户支付密码
     * @param paymentPasswordParam
     * @throws Exception
     */
    @Transactional
    @Override
    public void updatePaymentPassword(PaymentPasswordParam paymentPasswordParam) throws Exception {
        if (StringUtils.isEmpty(paymentPasswordParam.getNewPassword1()) ||
                StringUtils.isEmpty(paymentPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember faMember = this.getById(paymentPasswordParam.getUserId());
        // 用户不存在
        if (ObjectUtils.isEmpty(faMember)) {
            throw new UserNotExistsException();
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 设置过支付密码,修改支付密码必须传原密码
        if (StringUtils.isNotEmpty(faMember.getPaymentCode()) && StringUtils.isEmpty(paymentPasswordParam.getOldPassword())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 判断支付密码是否正确
        if (StringUtils.isNotEmpty(faMember.getPaymentCode()) && StringUtils.isNotEmpty(paymentPasswordParam.getOldPassword())) {
            if (!SecurityUtils.matchesPassword(paymentPasswordParam.getOldPassword(), faMember.getPaymentCode())) {
                throw new ServiceException(MessageUtils.message("password.error"), HttpStatus.ERROR);
            }
        }

        // 两次新密码是否一致
        if (!paymentPasswordParam.getNewPassword1().equals(paymentPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getPaymentCode, SecurityUtils.encryptPassword(paymentPasswordParam.getNewPassword1()));
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户头像
     * @param id
     * @param avatar
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateAvatar(Integer id, String avatar) throws Exception {
        if (StringUtils.isEmpty(avatar)) {
            throw new ServiceException(MessageUtils.message("no.avatar.file"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, id);
        lambdaUpdateWrapper.set(FaMember::getAvatar, avatar);
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);

        // 异步保存用户头像
        FaMember faMember = new FaMember();
        faMember.setId(id);
        faMember.setAvatar(avatar);
        iFaMemberImageService.updateImage(faMember);
    }

    /**
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaMember> searchMember(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faMember.getQueryString())) {
            lambdaQueryWrapper.and(i -> i.like(FaMember::getName, faMember.getQueryString())
//                    .or().like(FaMember::getMobile, faMember.getQueryString())
                            .or().like(FaMember::getNickname, faMember.getQueryString())
                            .or().like(FaMember::getWeiyima, faMember.getQueryString())
            );
        }
        if (null != faMember.getId()) {
            lambdaQueryWrapper.eq(FaMember::getId, faMember.getId());
        }
        if (null != faMember.getIsAuth()) {
            lambdaQueryWrapper.eq(FaMember::getIsAuth, faMember.getIsAuth());
        }
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        List<FaMember> faMemberList = this.list(lambdaQueryWrapper);
        return faMemberList;
    }

    /**
     * T+1冻结资金清零
     * @throws Exception
     */
    @Override
    public void clearT1Amount() throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.gt(FaMember::getFreezeProfit, 0);
        lambdaUpdateWrapper.set(FaMember::getFreezeProfit, 0);
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 实名认证
     * @throws Exception
     */
    @Transactional
    @Override
    public void authMember(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getName()) || StringUtils.isEmpty(faMember.getIdCard())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = this.getById(faMember.getId());

        // 风控判断 是否需要上传身份证照片 默认需要
        String isauth_rz = iFaRiskConfigService.getConfigValue("isauth_rz", "1");
        // 需要上传身份证照片 判断参数
        if ("1".equals(isauth_rz)) {
            // 数据库里身份证照片为空的时候，必须传照片
            if (StringUtils.isEmpty(selectOne.getIdCardFrontImage()) || StringUtils.isEmpty(selectOne.getIdCardBackImage())) {
                if (StringUtils.isEmpty(faMember.getIdCardFrontImage()) || StringUtils.isEmpty(faMember.getIdCardBackImage())) {
                    throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
                }
            }
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getName, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getNickname, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getIdCard, faMember.getIdCard());
        lambdaUpdateWrapper.set(FaMember::getIsAuth, 1);
        if (StringUtils.isNotEmpty(faMember.getIdCardFrontImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardFrontImage, faMember.getIdCardFrontImage());
        }
        if (StringUtils.isNotEmpty(faMember.getIdCardBackImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardBackImage, faMember.getIdCardBackImage());
        }
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getSubmitAuthTime, new Date());
        this.update(lambdaUpdateWrapper);

        // 异步保存用户认证图片
        iFaMemberImageService.updateImage(faMember);
    }

    /**
     * 绑定银行卡
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void bindingBank(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getAccountName()) || StringUtils.isEmpty(faMember.getAccount())
                || StringUtils.isEmpty(faMember.getDepositBank())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 审核中
        if (1 == member.getBankCardAuth()) {
            throw new ServiceException("正在审核中", HttpStatus.ERROR);
        }

        // 风控判断 是否需要上传银行卡照片 默认需要
        String bankcard_need_image = iFaRiskConfigService.getConfigValue("bankcard_need_image", "1");
        // 需要上传银行卡照片 判断参数
        if ("1".equals(bankcard_need_image)) {
            // 数据库里银行卡照片为空的时候，必须传照片
            if (StringUtils.isEmpty(member.getCardImage())) {
                if (StringUtils.isEmpty(faMember.getCardImage())) {
                    throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
                }
            }
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz)) {
            // 实名程度：1审核中和审核通过 2审核通过，默认1
            String rz_progress = iFaRiskConfigService.getConfigValue("rz_progress", "1");
            if ("1".equalsIgnoreCase(rz_progress)) {
                if (0 == member.getIsAuth() || 3 == member.getIsAuth()) {
                    throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
                }
            }
            if ("2".equalsIgnoreCase(rz_progress)) {
                if (2 != member.getIsAuth()) {
                    throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
                }
            }
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, faMember.getAccountName());
        lambdaUpdateWrapper.set(FaMember::getAccount, faMember.getAccount());
        lambdaUpdateWrapper.set(FaMember::getDepositBank, faMember.getDepositBank());
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, faMember.getKhzhihang());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getBindingTime, new Date());

        // 银行卡照片
        if (StringUtils.isNotEmpty(faMember.getCardImage())) {
            lambdaUpdateWrapper.set(FaMember::getCardImage, faMember.getCardImage());
        }

        // 银行卡是否需要审核 默认需要
        String add_bankcard_approve = iFaRiskConfigService.getConfigValue("add_bankcard_approve", "1");
        if ("1".equals(add_bankcard_approve)) {
            // 审核中
            lambdaUpdateWrapper.set(FaMember::getBankCardAuth, 1);
        }
        // 不需要审核
        else if ("0".equals(add_bankcard_approve)) {
            // 审核通过
            lambdaUpdateWrapper.set(FaMember::getBankCardAuth, 2);
        }

        // 修改银行卡需要审核，默认需要
        String edit_bankcard_approve = iFaRiskConfigService.getConfigValue("edit_bankcard_approve", "1");
        if ("1".equals(edit_bankcard_approve)) {
            // 2审核通过，修改，变审核中
            if (2 == member.getBankCardAuth()) {
                lambdaUpdateWrapper.set(FaMember::getBankCardAuth, 1);
            }
        }

        this.update(lambdaUpdateWrapper);

        // 异步保存用户银行卡图片
        iFaMemberImageService.updateImage(faMember);
    }

    /**
     * 解绑银行卡
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void unbindingBank(FaMember faMember) throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, null);
        lambdaUpdateWrapper.set(FaMember::getAccount, null);
        lambdaUpdateWrapper.set(FaMember::getDepositBank, null);
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, null);
        lambdaUpdateWrapper.set(FaMember::getUnbindTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 充值
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitRecharge(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect() || null == faMember.getRechargeType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

//        // 实名认证判断 默认需要
//        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
//        // 需要认证 尚未实名
//        if ("1".equals(is_rz) && (0 == member.getIsAuth() || 3 == member.getIsAuth())) {
//            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
//        }

        // 充值，有充值记录，有流水
        if (0 == faMember.getRechargeType()) {
            // 充值记录
            FaRecharge faRecharge = new FaRecharge();
            faRecharge.setUserId(member.getId());
            faRecharge.setMobile(member.getMobile());
            faRecharge.setName(member.getName());
            faRecharge.setSuperiorId(member.getSuperiorId());
            faRecharge.setSuperiorCode(member.getSuperiorCode());
            faRecharge.setSuperiorName(member.getSuperiorName());
            if (0 == faMember.getDirect()) {
                faRecharge.setMoney(faMember.getAmount());
            } else if (1 == faMember.getDirect()) {
                faRecharge.setMoney(faMember.getAmount().multiply(new BigDecimal(-1)));
            }
            faRecharge.setCreateTime(new Date());
            faRecharge.setPayTime(new Date());
            faRecharge.setIsPay(1);

            faRecharge.setPayType(faMember.getPayType());
            faRecharge.setIsApprove(1);
            faRecharge.setRemarks(faMember.getRemark());

            // 订单号
            faRecharge.setOrderId("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());

            iFaRechargeService.insertFaRecharge(faRecharge);

            // 记录流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(member.getId());
            faCapitalLog.setMobile(member.getMobile());
            faCapitalLog.setName(member.getName());
            faCapitalLog.setSuperiorId(member.getSuperiorId());
            faCapitalLog.setSuperiorCode(member.getSuperiorCode());
            faCapitalLog.setSuperiorName(member.getSuperiorName());
            if (StringUtils.isNotEmpty(faMember.getRemark())) {
                faCapitalLog.setContent(faMember.getRemark());
            } else {
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RECHARGE));
            }
            faCapitalLog.setMoney(faMember.getAmount());
            faCapitalLog.setBeforeMoney(member.getBalance());
            if (0 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().add(faCapitalLog.getMoney()));
            } else if (1 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
            }
            faCapitalLog.setType(CapitalFlowConstants.RECHARGE);
            faCapitalLog.setDirect(faMember.getDirect());
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额
            if (0 == faMember.getDirect()) {
                // 增加余额
                updateMemberBalance(member.getId(), faMember.getAmount(), 0);

                // 增加的余额去补缴
                iFaSgjiaoyiService.finishPayLater(member.getId(), faMember.getAmount());

                // 资金池增加
//                fundPoolService.updateMoney(member.getId(), faMember.getAmount(), 0, "资金池增加", 1002);
            }
            // 减少余额
            else if (1 == faMember.getDirect()) {
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
            }
        }
        // 调整余额，无流水
        else if (1 == faMember.getRechargeType()) {
            updateMemberBalance(member.getId(), faMember.getAmount(), faMember.getDirect());

            // 记录流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(member.getId());
            faCapitalLog.setMobile(member.getMobile());
            faCapitalLog.setName(member.getName());
            faCapitalLog.setSuperiorId(member.getSuperiorId());
            faCapitalLog.setSuperiorCode(member.getSuperiorCode());
            faCapitalLog.setSuperiorName(member.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.CHANGE_BALANCE));
            faCapitalLog.setMoney(faMember.getAmount());
            faCapitalLog.setBeforeMoney(member.getBalance());
            if (0 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().add(faCapitalLog.getMoney()));
            } else if (1 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
            }
            faCapitalLog.setType(CapitalFlowConstants.CHANGE_BALANCE);
            faCapitalLog.setDirect(faMember.getDirect());
            faCapitalLog.setCreateTime(new Date());
            // 删除状态
            faCapitalLog.setDeleteFlag(1);
            iFaCapitalLogService.save(faCapitalLog);
        }
        // 调整锁定金额，无流水
        else if (2 == faMember.getRechargeType()) {
            updateFaMemberFreezeProfit(member.getId(), faMember.getAmount(), faMember.getDirect());

            // 记录流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(member.getId());
            faCapitalLog.setMobile(member.getMobile());
            faCapitalLog.setName(member.getName());
            faCapitalLog.setSuperiorId(member.getSuperiorId());
            faCapitalLog.setSuperiorCode(member.getSuperiorCode());
            faCapitalLog.setSuperiorName(member.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.CHANGE_FREEZE));
            faCapitalLog.setMoney(faMember.getAmount());
            faCapitalLog.setBeforeMoney(member.getFreezeProfit());
            if (0 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getFreezeProfit().add(faCapitalLog.getMoney()));
            } else if (1 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getFreezeProfit().subtract(faCapitalLog.getMoney()));
            }
            faCapitalLog.setType(CapitalFlowConstants.CHANGE_FREEZE);
            faCapitalLog.setDirect(faMember.getDirect());
            faCapitalLog.setCreateTime(new Date());
            // 删除状态
            faCapitalLog.setDeleteFlag(1);
            iFaCapitalLogService.save(faCapitalLog);
        }
        // 资金池划转
        else if (3 == faMember.getRechargeType()) {
            // 资金池划出，用户余额增加
            if (0 == faMember.getDirect()) {
                BigDecimal zjcBalance = fundPoolService.getFundPoolBalance();
                if (zjcBalance.compareTo(faMember.getAmount()) < 0) {
                    throw new ServiceException("资金池余额不足", HttpStatus.ERROR);
                }

                // 资金池划出
                fundPoolService.updateMoney(member.getId(), faMember.getAmount(), 1, "资金池转出", 1003, faMember.getCreateTimeFundPool());
                // 用户余额增加
                updateMemberBalance(member.getId(), faMember.getAmount(), 0);

                // 用户流水
                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(member.getId());
                faCapitalLog.setMobile(member.getMobile());
                faCapitalLog.setName(member.getName());
                faCapitalLog.setSuperiorId(member.getSuperiorId());
                faCapitalLog.setSuperiorCode(member.getSuperiorCode());
                faCapitalLog.setSuperiorName(member.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.OUT_MEMBER));
                faCapitalLog.setMoney(faMember.getAmount());
                faCapitalLog.setBeforeMoney(member.getBalance());
                faCapitalLog.setLaterMoney(member.getBalance().add(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.OUT_MEMBER);
                faCapitalLog.setDirect(0);
                if (null != faMember.getCreateTimeFundPool()) {
                    faCapitalLog.setCreateTime(faMember.getCreateTimeFundPool());
                } else {
                    faCapitalLog.setCreateTime(new Date());
                }
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);
            }
            // 划入资金池，用户余额减少
            else if (1 == faMember.getDirect()) {
                if (member.getBalance().compareTo(faMember.getAmount()) < 0) {
                    throw new ServiceException("用户余额不足", HttpStatus.ERROR);
                }

                // 用户余额减少
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
                // 划入资金池
                fundPoolService.updateMoney(member.getId(), faMember.getAmount(), 0, "转入资金池", 1004, faMember.getCreateTimeFundPool());

                // 用户流水
                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(member.getId());
                faCapitalLog.setMobile(member.getMobile());
                faCapitalLog.setName(member.getName());
                faCapitalLog.setSuperiorId(member.getSuperiorId());
                faCapitalLog.setSuperiorCode(member.getSuperiorCode());
                faCapitalLog.setSuperiorName(member.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.IN_MEMBER));
                faCapitalLog.setMoney(faMember.getAmount());
                faCapitalLog.setBeforeMoney(member.getBalance());
                faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.IN_MEMBER);
                faCapitalLog.setDirect(1);
                if (null != faMember.getCreateTimeFundPool()) {
                    faCapitalLog.setCreateTime(faMember.getCreateTimeFundPool());
                } else {
                    faCapitalLog.setCreateTime(new Date());
                }
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);
            }
        }
    }

    /**
     * 修改余额
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitUpdateBalance(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 不记录流水
        // 更新用户余额
        if (0 == faMember.getDirect()) {
            // 增加余额
            updateMemberBalance(member.getId(), faMember.getAmount(), 0);
        }
        // 减少余额
        else if (1 == faMember.getDirect()) {
            // 余额不够减，清零
            if (member.getBalance().compareTo(faMember.getAmount()) < 0) {
                updateMemberBalance(member.getId(), member.getBalance(), 1);
            }
            // 余额够减，减少
            else {
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
            }
        }
    }

    /**
     * 修改T+1锁定转入转出
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitUpdateFreezeProfit(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 调整锁定金额，无流水
        updateFaMemberFreezeProfit(member.getId(), faMember.getAmount(), faMember.getDirect());
    }

    /**
     * 充值申请
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public String rechargeApply(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getSysbankId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

//        // 实名认证判断 默认需要
//        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
//        // 需要认证 尚未实名
//        if ("1".equals(is_rz) && (0 == member.getIsAuth() || 3 == member.getIsAuth())) {
//            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
//        }

        FaRecharge faRecharge = new FaRecharge();
        faRecharge.setUserId(member.getId());
        faRecharge.setMobile(member.getMobile());
        faRecharge.setName(member.getName());
        faRecharge.setSuperiorId(member.getSuperiorId());
        faRecharge.setSuperiorCode(member.getSuperiorCode());
        faRecharge.setSuperiorName(member.getSuperiorName());
        faRecharge.setMoney(faMember.getAmount());
        faRecharge.setCreateTime(new Date());
        faRecharge.setDeleteFlag(0);
        faRecharge.setIsPay(0);

        faRecharge.setPayType(faMember.getPayType());

        // 订单号
        faRecharge.setOrderId("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());

        // 风控校验
        iFaRiskConfigService.checkRecharge(faRecharge);

        // 关联通道
        faRecharge.setSysbankid(faMember.getSysbankId());

        iFaRechargeService.save(faRecharge);

        // 风控挂卡通道 kaika 默认关闭0
        String kaika = iFaRiskConfigService.getConfigValue("kaika", "0");

        if ("1".equals(kaika)) {
            // 支付地址
            String result = apiCommonService.getPayment(faMember, faRecharge);
            if (StringUtils.isEmpty(result)) {
                throw new ServiceException(MessageUtils.message("payment.error"), HttpStatus.ERROR);
            }
            result = URLEncoder.encode(result, "utf-8");
            return result;
        }
        return null;
    }

    /**
     * 提现申请
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void withdrawApply(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || StringUtils.isEmpty(faMember.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 提现开关
        if (0 == member.getWithdrawSwitch()) {
            throw new ServiceException(DictUtils.getDictLabel("reply_type", "refuseWithdraw"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != member.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 是否绑卡审核通过
        if (2 != member.getBankCardAuth()) {
            throw new ServiceException(MessageUtils.message("user.bind.card.not.auth"), HttpStatus.ERROR);
        }

        // 支付密码校验
        if (!SecurityUtils.matchesPassword(faMember.getPaymentCode(), member.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("payment.password.error"), HttpStatus.ERROR);
        }

        FaWithdraw faWithdraw = new FaWithdraw();
        faWithdraw.setUserId(member.getId());
        faWithdraw.setMobile(member.getMobile());
        faWithdraw.setName(member.getName());
        faWithdraw.setSuperiorId(member.getSuperiorId());
        faWithdraw.setSuperiorCode(member.getSuperiorCode());
        faWithdraw.setSuperiorName(member.getSuperiorName());
        faWithdraw.setMoney(faMember.getAmount());
        faWithdraw.setCreateTime(new Date());
        faWithdraw.setDeleteFlag(0);
        faWithdraw.setIsPay(0);
        faWithdraw.setAccountName(member.getAccountName());
        faWithdraw.setAccount(member.getAccount());
        faWithdraw.setDepositBank(member.getDepositBank());

        // 余额判断
        if (member.getBalance().subtract(member.getFreezeProfit()).compareTo(faMember.getAmount()) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 风控校验
        iFaRiskConfigService.checkWithdraw(faWithdraw);

        iFaWithdrawService.save(faWithdraw);

        // 流水
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(member.getId());
        faCapitalLog.setMobile(member.getMobile());
        faCapitalLog.setName(member.getName());
        faCapitalLog.setSuperiorId(member.getSuperiorId());
        faCapitalLog.setSuperiorCode(member.getSuperiorCode());
        faCapitalLog.setSuperiorName(member.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.WITHDRAW));
        faCapitalLog.setMoney(faMember.getAmount());
        faCapitalLog.setBeforeMoney(member.getBalance());
        faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.WITHDRAW);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 更新用户余额
        updateMemberBalance(member.getId(), faMember.getAmount(), 1);
    }

    /**
     * 充值记录
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaRecharge> rechargeList(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getUserId, faMember.getId());
        lambdaQueryWrapper.eq(FaRecharge::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaRecharge::getCreateTime);
        List<FaRecharge> list = iFaRechargeService.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 提现记录
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaWithdraw> withdrawList(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaWithdraw> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaWithdraw::getUserId, faMember.getId());
        lambdaQueryWrapper.eq(FaWithdraw::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaWithdraw::getCreateTime);
        List<FaWithdraw> list = iFaWithdrawService.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 注册接口
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public FaMember register(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getMobile()) || StringUtils.isEmpty(faMember.getPassword())
                || StringUtils.isEmpty(faMember.getConfirmPassword())
                || StringUtils.isEmpty(faMember.getSuperiorCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 两次密码校验
        if (!faMember.getPassword().equals(faMember.getConfirmPassword())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }

        // 手机号校验
//        checkMobile(faMember.getMobile());

        // 手机号
        faMember.setPhoneNo(faMember.getMobile());

        // 加密手机号校验
        String mobileAES = AESUtil.encrypt(faMember.getMobile());
        checkMobileAES(mobileAES);
        faMember.setSalt(mobileAES);

        // 手机号隐藏
        if (faMember.getMobile().length() > 3) {
            faMember.setMobile(faMember.getMobile().substring(0, 3) + "****" + faMember.getMobile().substring(faMember.getMobile().length() - 4, faMember.getMobile().length()));
        } else {
            faMember.setMobile(faMember.getMobile() + "********");
        }

        // 上级机构码校验
        SysUser superior = checkSuperiorCode(faMember.getSuperiorCode());

        // 保存用户信息
        faMember.setUsername(faMember.getMobile());
        faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
//        faMember.setLevel(superior.getLevel() + 1);
        if (StringUtils.isNotEmpty(faMember.getPaymentCode())) {
            faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        }
        faMember.setJoinIp(IpUtils.getIpAddr());
        faMember.setCreateTime(new Date());
        faMember.setSuperiorId(superior.getUserId().intValue());
        faMember.setSuperiorCode(superior.getInstitutionNo());
        faMember.setSuperiorName(superior.getNickName());
//        faMember.setIsSimulation(superior.getIsSimulation());
        // 生成6位机构码
//        faMember.setInstitutionNumber(getInstitutionNumber());
        // 生成 T003574928 唯一码
        faMember.setWeiyima(getWeiyima());
        faMember.setDailiId(superior.getUserId());
        faMember.setIsAuth(0);
        // 默认头像
        String avatar = iFaRiskConfigService.getConfigValue("defaultavatar", "");
        faMember.setAvatar(avatar);

        faMember.setLoginDomain(IpUtils.getLoginDomain());

        faMemberMapper.insertFaMember(faMember);
        faMember = this.getById(faMember.getId());
        return faMember;
    }

    /**
     * 生成唯一码
     * @return
     * @throws Exception
     */
    private String getWeiyima() throws Exception {
        int num = (int) Math.floor(OrderUtil.randomNumber(1000000, 9999999));
        String weiyima = "T00" + String.valueOf(num);
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getWeiyima, weiyima);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faMember)) {
            return weiyima;
        } else {
            return getWeiyima();
        }
    }

    /**
     * 生成6位机构码
     * @return
     * @throws Exception
     */
    private String getInstitutionNumber() throws Exception {
        String institutionNumber = IdUtils.fastUUID().substring(0, 6).toUpperCase();
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getInstitutionNumber, institutionNumber);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faMember)) {
            return institutionNumber;
        } else {
            return getInstitutionNumber();
        }
    }

    /**
     * 加密手机号校验
     * @param mobileAES
     * @throws Exception
     */
    private void checkMobileAES(String mobileAES) throws Exception {
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getSalt, mobileAES);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 1 ");
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.mobile.exists"), HttpStatus.ERROR);
        }
    }

    /**
     * 手机号校验
     * @param mobile
     */
    private void checkMobile(String mobile) throws Exception {
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getMobile, mobile);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 1 ");
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.mobile.exists"), HttpStatus.ERROR);
        }
    }

    /**
     * 上级机构码校验
     * @param superiorCode
     * @throws Exception
     */
    private SysUser checkSuperiorCode(String superiorCode) throws Exception {
        SysUser sysUser = iSysUserService.selectUserByInstitutionNo(superiorCode);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new ServiceException(MessageUtils.message("superiorCode.error"), HttpStatus.ERROR);
        }
        return sysUser;
    }

    /**
     * 用户统计数据
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getMemberStatistics(FaMember faMember) throws Exception {
        return faMemberMapper.getMemberStatistics(faMember);
    }

    /**
     * 批量审核身份认证
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void batchAuthMember(FaMember faMember) throws Exception {
        if (ObjectUtils.isNotEmpty(faMember) && faMember.getIds().length > 0) {
            LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.in(FaMember::getId, faMember.getIds());
            lambdaUpdateWrapper.set(FaMember::getIsAuth, 2);
            lambdaUpdateWrapper.set(FaMember::getAuthTime, new Date());
            lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
            this.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 用户资金信息
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> fundInfo(FaMember faMember) throws Exception {
        faMember = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        Map<String, BigDecimal> map = new HashMap<>();

        // 查询集合
        List<BigDecimal> amounts = faMemberMapper.getFundInfos(faMember.getId());

        // 可用资金
        map.put("available", faMember.getBalance().setScale(2, RoundingMode.HALF_UP));

        // 融券持仓盈亏
        BigDecimal holdProfitRq = amounts.get(8);

        // 新股资金 = 新股申购资金 + 新股申购资金
        // 新股申购资金
        BigDecimal sg = amounts.get(0);
        // 新股配售资金
        BigDecimal ps = amounts.get(1);
        BigDecimal fundInfoNew = sg.add(ps).add(holdProfitRq).add(holdProfitRq);
        map.put("new", fundInfoNew.setScale(2, RoundingMode.HALF_UP));

        // 可取资金
        BigDecimal cash = faMember.getBalance().subtract(faMember.getFreezeProfit()).setScale(2, RoundingMode.HALF_UP);
        if (cash.compareTo(BigDecimal.ZERO) < 0) {
            cash = BigDecimal.ZERO;
        }
        map.put("cash", cash);

        // 冻结资金
        map.put("freeze", faMember.getFreezeProfit());

        // 持仓市值 = 上市股票持仓 + 未上市新股转持仓
        BigDecimal fundInfoMarketListed = amounts.get(2);
        BigDecimal fundInfoMarketUnlisted = amounts.get(3);
        BigDecimal fundInfoMarket = fundInfoMarketListed.add(fundInfoMarketUnlisted);
        map.put("market", fundInfoMarket.setScale(2, RoundingMode.HALF_UP));

        // 总盈亏 = 持仓市值 + 总提现 + 余额 + 新股申购资金 - 总充值
        // 总充值
        BigDecimal recharge = amounts.get(4);
        // 总提现
        BigDecimal withdraw = amounts.get(5);
        map.put("profit", fundInfoMarket.add(withdraw).add(faMember.getBalance()).add(fundInfoNew).subtract(recharge).setScale(2, RoundingMode.HALF_UP));

        // T+1 冻结资金
        map.put("T1", faMember.getFreezeProfit().setScale(2, RoundingMode.HALF_UP));

        // 总资产 = 余额 + 新股 + 持仓市值
        BigDecimal total = faMember.getBalance().add(fundInfoNew).add(fundInfoMarket);
        map.put("total", total.setScale(2, RoundingMode.HALF_UP));

        // 持仓盈亏
        BigDecimal holdProfit = amounts.get(9);
        map.put("holdProfit", holdProfit.setScale(2, RoundingMode.HALF_UP));

        // 交易盈亏
        BigDecimal tradeProfit = amounts.get(6);
        map.put("tradeProfit", tradeProfit.setScale(2, RoundingMode.HALF_UP));

        // 大宗持仓市值
        BigDecimal dzMarket = amounts.get(10);
        map.put("dzMarket", dzMarket.setScale(2, RoundingMode.HALF_UP));

        // 大宗持仓盈亏
        BigDecimal dzHoldProfit = amounts.get(11);
        map.put("dzHoldProfit", dzHoldProfit.setScale(2, RoundingMode.HALF_UP));

        // 总手续费
        BigDecimal totalPoundage = amounts.get(7);
        map.put("totalPoundage", totalPoundage.setScale(2, RoundingMode.HALF_UP));

        // 持仓当日盈亏
        BigDecimal holdProfitToday = amounts.get(12);
        // 节假日，当日盈亏为0
        if (iFaRiskConfigService.isHolidayToday()) {
            map.put("holdProfitToday", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        } else {
            map.put("holdProfitToday", holdProfitToday.setScale(2, RoundingMode.HALF_UP));
        }

        return map;
    }

    /**
     * 用户交易信息
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> tradeInfo(FaMember faMember) throws Exception {
        faMember = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        Map<String, BigDecimal> map = new HashMap<>();

        // 可用资金
        map.put("available", faMember.getBalance());

        // 持仓盈亏
        BigDecimal holdProfit = faMemberMapper.getTradeInfoHoldProfit(faMember.getId());
        map.put("holdProfit", holdProfit);

        // 可取资金
        map.put("cash", faMember.getBalance().subtract(faMember.getFreezeProfit()));

        // 持仓市值
        BigDecimal fundInfoMarket = faMemberMapper.getFundInfoMarket(faMember.getId());
        map.put("market", fundInfoMarket);

        // 交易盈亏
        BigDecimal tradeProfit = faMemberMapper.getTradeInfoTradeProfit(faMember.getId());
        map.put("tradeProfit", tradeProfit);

        return map;
    }

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct
     * @throws Exception
     */
    @Override
    public void updateFaMemberFreezeProfit(Integer memberId, BigDecimal amount, int direct) throws Exception {
        faMemberMapper.updateFaMemberFreezeProfit(memberId, amount, direct);
    }

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct
     * @param type 类型(0先扣闲置资金 1先扣冻结)
     * @throws Exception
     */
    @Override
    public void updateFaMemberFreezeProfit(Integer memberId, BigDecimal amount, int direct, int type) throws Exception {
        // type 0先扣闲置资金 1先扣冻结
        String dock_type = iFaRiskConfigService.getConfigValue("dock_type", "1");

        // 先扣冻结
        if ("1".equals(dock_type)) {
            faMemberMapper.updateFaMemberFreezeProfit(memberId, amount, direct);
        }
        // 先扣闲置
        else if ("0".equals(dock_type)) {
            if (1 == direct && 0 == type) {
                FaMember faMember = this.selectFaMemberById(memberId);

                // 先扣闲置资金，后扣冻结 | 冻结资金 <= 余额 | 如果冻结资金 > 余额，更新冻结与余额一致
                // 冻结资金
                BigDecimal freezeProfit = faMember.getFreezeProfit();
                // 扣后余额
                BigDecimal balance = faMember.getBalance();

                // 冻结资金 > 余额，减掉差额
                if (freezeProfit.compareTo(balance) > 0) {
                    faMemberMapper.updateFaMemberFreezeProfit(memberId, freezeProfit.subtract(balance), 1);
                }
            }
        }
    }

    /**
     * 更新用户余额
     * @param userId 用户id
     * @param money 变动金额
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    @Override
    public void updateMemberBalance(Integer userId, BigDecimal money, int direct) throws Exception {
        faMemberMapper.updateMemberBalance(userId, money, direct);
    }

    /**
     * 总余额
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalBalance(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalBalance(faMember);
    }

    /**
     * 总充值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalRecharge(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalRecharge(faMember);
    }

    /**
     * 总提现
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalWithdraw(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalWithdraw(faMember);
    }

    /**
     * 新股申购冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalSg(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalSg(faMember);
    }

    /**
     * 新股配售冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalPs(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalPs(faMember);
    }

    /**
     * 上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalListed(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalListed(faMember);
    }

    /**
     * 未上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalUnlisted(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalUnlisted(faMember);
    }

    /**
     * 单个用户统计
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getMemberStatisticsSingle(FaMember faMember) throws Exception {
        if (null == faMember.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faMember = this.getById(faMember.getId());
        // 余额
        BigDecimal balance = faMember.getBalance();
        // T+N锁定
        BigDecimal freezeProfit = faMember.getFreezeProfit();
        // 可取资金
        BigDecimal cash = balance.subtract(freezeProfit);
        if (cash.compareTo(BigDecimal.ZERO) < 0) {
            cash = BigDecimal.ZERO;
        }
        // 上市持仓市值
        BigDecimal listedHold = faMemberMapper.getListedHold(faMember);
        // 未上市持仓市值
        BigDecimal unlistedHold = faMemberMapper.getUnlistedHold(faMember);
        // 新股申购冻结
        BigDecimal sgFreeze = faMemberMapper.getSgFreeze(faMember);
        // 新股配售冻结
        BigDecimal psFreeze = faMemberMapper.getPsFreeze(faMember);
        // 总充值
        BigDecimal recharge = faMemberMapper.getRecharge(faMember);
        // 总提现
        BigDecimal withdraw = faMemberMapper.getWithdraw(faMember);
        // 总盈亏 = 余额 + 新股 + 持仓 + 提现 - 充值
        BigDecimal profitLose = balance.add(sgFreeze).add(psFreeze).add(listedHold).add(unlistedHold).add(withdraw).subtract(recharge);
        // 总资产 = 余额 + 新股 + 持仓市值
        BigDecimal fund = balance.add(sgFreeze).add(psFreeze).add(listedHold).add(unlistedHold);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("balance", balance);
        map.put("freezeProfit", freezeProfit);
        map.put("cash", cash);
        map.put("listedHold", listedHold);
        map.put("unlistedHold", unlistedHold);
        map.put("sgFreeze", sgFreeze);
        map.put("psFreeze", psFreeze);
        map.put("recharge", recharge);
        map.put("withdraw", withdraw);
        map.put("profitLose", profitLose);
        map.put("fund", fund);
        return map;
    }

    /**
     * 获取用户手机号
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public String getMobile(FaMember faMember) throws Exception {
        faMember = this.getById(faMember.getId());
        return faMember.getMobile();
    }

    /**
     * 用户手机号加密
     * @throws Exception
     */
    @Override
    public void aesMobile() throws Exception {
        // 取出salt为空的用户
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getSalt, "");
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        List<FaMember> list = this.list(lambdaQueryWrapper);
        for (FaMember faMember : list) {
            LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
            lambdaUpdateWrapper.set(FaMember::getSalt, AESUtil.encrypt(faMember.getMobile()));
            lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
            this.update(lambdaUpdateWrapper);
        }
    }

}