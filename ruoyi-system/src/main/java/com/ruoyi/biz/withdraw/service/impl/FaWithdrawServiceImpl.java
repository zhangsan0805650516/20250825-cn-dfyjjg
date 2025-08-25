package com.ruoyi.biz.withdraw.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.biz.withdraw.mapper.FaWithdrawMapper;
import com.ruoyi.biz.withdraw.service.IFaWithdrawService;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 提现Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Service
public class FaWithdrawServiceImpl extends ServiceImpl<FaWithdrawMapper, FaWithdraw> implements IFaWithdrawService
{
    @Autowired
    private FaWithdrawMapper faWithdrawMapper;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaMemberService iFaMemberService;

    /**
     * 查询提现
     *
     * @param id 提现主键
     * @return 提现
     */
    @Override
    public FaWithdraw selectFaWithdrawById(Integer id)
    {
        return faWithdrawMapper.selectFaWithdrawById(id);
    }

    /**
     * 查询提现列表
     *
     * @param faWithdraw 提现
     * @return 提现
     */
    @Override
    public List<FaWithdraw> selectFaWithdrawList(FaWithdraw faWithdraw)
    {
        try {
            faWithdraw.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faWithdraw.getMobile())) {
                faWithdraw.setSalt(AESUtil.encrypt(faWithdraw.getMobile()));
                faWithdraw.setMobile(null);
            }

            return faWithdrawMapper.selectFaWithdrawList(faWithdraw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增提现
     *
     * @param faWithdraw 提现
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaWithdraw(FaWithdraw faWithdraw)
    {
        FaMember faMember = iFaMemberService.getById(faWithdraw.getUserId());
        if (ObjectUtils.isNotEmpty(faMember)) {
            faWithdraw.setName(faMember.getName());
            faWithdraw.setMobile(faMember.getMobile());
            faWithdraw.setSuperiorId(faMember.getSuperiorId());
            faWithdraw.setSuperiorCode(faMember.getSuperiorCode());
            faWithdraw.setSuperiorName(faMember.getSuperiorName());

        }
        faWithdraw.setIsQx(0);
        faWithdraw.setCreateTime(DateUtils.getNowDate());
        return faWithdrawMapper.insertFaWithdraw(faWithdraw);
    }

    /**
     * 修改提现
     *
     * @param faWithdraw 提现
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaWithdraw(FaWithdraw faWithdraw)
    {
        faWithdraw.setMobile(null);
        faWithdraw.setUpdateTime(DateUtils.getNowDate());
        return faWithdrawMapper.updateFaWithdraw(faWithdraw);
    }

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的提现主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaWithdrawByIds(Integer[] ids)
    {
        return faWithdrawMapper.deleteFaWithdrawByIds(ids);
    }

    /**
     * 删除提现信息
     *
     * @param id 提现主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaWithdrawById(Integer id)
    {
        return faWithdrawMapper.deleteFaWithdrawById(id);
    }

    /**
     * 审核提现
     * @param faWithdraw
     * @throws Exception
     */
    @Transactional
    @Override
    public void approveWithdraw(FaWithdraw faWithdraw) throws Exception {
        if (null == faWithdraw.getId() || null == faWithdraw.getIsPay()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaWithdraw withdraw = this.getById(faWithdraw.getId());
        if (ObjectUtils.isEmpty(withdraw)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 已审核
        if (1 == withdraw.getIsApprove()) {
            throw new ServiceException(MessageUtils.message("withdraw.already.approve"), HttpStatus.ERROR);
        }

        FaMember faMember = iFaMemberService.getById(withdraw.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 驳回 退回金额
        if (2 == faWithdraw.getIsPay()) {
            // 流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(faMember.getId());
            faCapitalLog.setMobile(faMember.getMobile());
            faCapitalLog.setName(faMember.getName());
            faCapitalLog.setSuperiorId(faMember.getSuperiorId());
            faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
            faCapitalLog.setSuperiorName(faMember.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.WITHDRAW_RETURN));
            faCapitalLog.setMoney(faWithdraw.getMoney());
            faCapitalLog.setBeforeMoney(faMember.getBalance());
            faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
            faCapitalLog.setType(CapitalFlowConstants.WITHDRAW_RETURN);
            faCapitalLog.setDirect(0);
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额 增加 提现退回
            iFaMemberService.updateMemberBalance(faMember.getId(), faWithdraw.getMoney(), 0);

        }

        faWithdraw.setUpdateTime(new Date());

        // 审核标记
        faWithdraw.setIsApprove(1);
        this.updateFaWithdraw(faWithdraw);
    }

    /**
     * 今天提现次数
     * @return
     * @throws Exception
     */
    @Override
    public int getWithdrawTimesToday(Integer userId) throws Exception {
        return faWithdrawMapper.getWithdrawTimesToday(userId);
    }

    /**
     * 提现统计
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    @Override
    public List<BigDecimal> getWithdrawStatistics(FaWithdraw faWithdraw) throws Exception {
        return faWithdrawMapper.getWithdrawStatistics(faWithdraw);
    }

    /**
     * 今日提现金额
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getWithdrawAlreadyToday(Integer userId) throws Exception {
        return faWithdrawMapper.getWithdrawAlreadyToday(userId);
    }

    /**
     * 已打款总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalPaid(FaWithdraw faWithdraw) throws Exception {
        return faWithdrawMapper.getTotalPaid(faWithdraw);
    }

    /**
     * 未打款总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalUnpaid(FaWithdraw faWithdraw) throws Exception {
        return faWithdrawMapper.getTotalUnpaid(faWithdraw);
    }

    /**
     * 驳回总额
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalRefuse(FaWithdraw faWithdraw) throws Exception {
        return faWithdrawMapper.getTotalRefuse(faWithdraw);
    }

    /**
     * 修改消息通知
     * @param faWithdraw
     * @throws Exception
     */
    @Override
    public void changeIsQx(FaWithdraw faWithdraw) throws Exception {
        if (null == faWithdraw.getId() || null == faWithdraw.getIsQx()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaWithdraw> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaWithdraw::getId, faWithdraw.getId());
        lambdaUpdateWrapper.set(FaWithdraw::getIsQx, faWithdraw.getIsQx());
        lambdaUpdateWrapper.set(FaWithdraw::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 检测消息
     * 待打款，未关闭的消息
     * @param faWithdraw
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkQx(FaWithdraw faWithdraw) throws Exception {
        faWithdraw.setIsPay(0);
        faWithdraw.setIsQx(1);
        faWithdraw.setDeleteFlag(0);
        List<FaWithdraw> faWithdrawList = faWithdrawMapper.selectFaWithdrawList(faWithdraw);
        if (faWithdrawList.isEmpty()) {
            return false;
        }
        return true;
    }

}