package com.ruoyi.biz.fundPool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.mapper.FaCapitalLogMapper;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.fundPool.service.FundPoolService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FundPoolServiceImpl implements FundPoolService {

    private static final Logger log = LoggerFactory.getLogger(FundPoolServiceImpl.class);

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private FaCapitalLogMapper faCapitalLogMapper;

    @Autowired
    private IFaMemberService faMemberService;

    /**
     * 获取资金池信息
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getFundPoolInfo(FaCapitalLog faCapitalLog) throws Exception {
        Map<String, BigDecimal> map = new HashMap<>();

        // 资金池总额
        BigDecimal fundPoolTotal = faCapitalLogMapper.getFundPoolTotal();
        map.put("fundPoolTotal", fundPoolTotal);

        // 划入总额
        BigDecimal fundPoolInTotal = faCapitalLogMapper.getFundPoolInTotal();
        map.put("fundPoolInTotal", fundPoolInTotal);

        // 划出总额
        BigDecimal fundPoolOutTotal = faCapitalLogMapper.getFundPoolOutTotal();
        map.put("fundPoolOutTotal", fundPoolOutTotal);

        return map;
    }

    /**
     * 资金池增加
     * @param userId 关联用户
     * @param money 金额
     * @param direct 方向(0赠 1减)
     * @param content 流水内容
     * @param type 流水类型
     * @throws Exception
     */
    @Override
    public void updateMoney(Integer userId, BigDecimal money, int direct, String content, int type, Date creatTime) throws Exception {
        // 资金池
        FaMember faMember = faMemberService.getById(1);

        if (ObjectUtils.isNotEmpty(faMember)) {
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(1);
            faCapitalLog.setMobile("00000000000");
            faCapitalLog.setName("资金池");
            faCapitalLog.setSuperiorId(1);
            faCapitalLog.setSuperiorCode("0");
//            faCapitalLog.setSuperiorName("");
            faCapitalLog.setContent(content);
            faCapitalLog.setMoney(money);
            faCapitalLog.setBeforeMoney(faMember.getBalance());

            // 资金池增加
            if (0 == direct) {
                faCapitalLog.setLaterMoney(faCapitalLog.getBeforeMoney().add(faCapitalLog.getMoney()));
            }
            // 资金池减少
            else if (1 == direct) {
                faCapitalLog.setLaterMoney(faCapitalLog.getBeforeMoney().subtract(faCapitalLog.getMoney()));
            }

            faCapitalLog.setType(type);
            faCapitalLog.setDirect(direct);
            if (null != creatTime) {
                faCapitalLog.setCreateTime(creatTime);
            } else {
                faCapitalLog.setCreateTime(new Date());
            }
            faCapitalLog.setOrderId(userId);
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 资金池余额变动
            faMemberService.updateMemberBalance(1, money, direct);
        }
    }

    /**
     * 资金池余额
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getFundPoolBalance() throws Exception {
        FaMember faMember = faMemberService.getById(1);
        if (ObjectUtils.isNotEmpty(faMember)) {
            return faMember.getBalance();
        } else {
            return BigDecimal.ZERO;
        }

//        // 取出最后一条资金池资金记录
//        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, 1);
//        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
//        lambdaQueryWrapper.orderByDesc(FaCapitalLog::getId);
//        lambdaQueryWrapper.last(" limit 1 ");
//        FaCapitalLog lastCapitalLog = iFaCapitalLogService.getOne(lambdaQueryWrapper);
//        if (ObjectUtils.isNotEmpty(lastCapitalLog)) {
//            return lastCapitalLog.getLaterMoney();
//        } else {
//            return BigDecimal.ZERO;
//        }
    }

    /**
     * 用户转入资金池
     * @param faMember
     * @throws Exception
     */
    @Override
    public void transferToFundPool(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getMoney() || StringUtils.isEmpty(faMember.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = faMemberService.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        if (member.getBalance().compareTo(faMember.getMoney()) < 0) {
            throw new ServiceException("用户余额不足", HttpStatus.ERROR);
        }

        // 支付密码校验
        if (!SecurityUtils.matchesPassword(faMember.getPaymentCode(), member.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("payment.password.error"), HttpStatus.ERROR);
        }

        // 用户余额减少
        faMemberService.updateMemberBalance(member.getId(), faMember.getMoney(), 1);
        // 划入资金池
        updateMoney(member.getId(), faMember.getMoney(), 0, "转入资金池", 1004, new Date());

        // 用户流水
        FaCapitalLog memberCapitalLog = new FaCapitalLog();
        memberCapitalLog.setUserId(member.getId());
        memberCapitalLog.setMobile(member.getMobile());
        memberCapitalLog.setName(member.getName());
        memberCapitalLog.setSuperiorId(member.getSuperiorId());
        memberCapitalLog.setSuperiorCode(member.getSuperiorCode());
        memberCapitalLog.setSuperiorName(member.getSuperiorName());
        memberCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.IN_MEMBER));
        memberCapitalLog.setMoney(faMember.getMoney());
        memberCapitalLog.setBeforeMoney(member.getBalance());
        memberCapitalLog.setLaterMoney(member.getBalance().subtract(faMember.getMoney()));
        memberCapitalLog.setType(CapitalFlowConstants.IN_MEMBER);
        memberCapitalLog.setDirect(1);
        memberCapitalLog.setCreateTime(new Date());
        memberCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(memberCapitalLog);
    }
}
