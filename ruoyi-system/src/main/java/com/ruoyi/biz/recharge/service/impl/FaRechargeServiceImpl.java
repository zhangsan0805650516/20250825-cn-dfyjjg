package com.ruoyi.biz.recharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.recharge.mapper.FaRechargeMapper;
import com.ruoyi.biz.recharge.service.IFaRechargeService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
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
 * 充值Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Service
public class FaRechargeServiceImpl extends ServiceImpl<FaRechargeMapper, FaRecharge> implements IFaRechargeService
{
    @Autowired
    private FaRechargeMapper faRechargeMapper;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    /**
     * 查询充值
     *
     * @param id 充值主键
     * @return 充值
     */
    @Override
    public FaRecharge selectFaRechargeById(Integer id)
    {
        return faRechargeMapper.selectFaRechargeById(id);
    }

    /**
     * 查询充值列表
     *
     * @param faRecharge 充值
     * @return 充值
     */
    @Override
    public List<FaRecharge> selectFaRechargeList(FaRecharge faRecharge)
    {
        try {
            faRecharge.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faRecharge.getMobile())) {
                faRecharge.setSalt(AESUtil.encrypt(faRecharge.getMobile()));
                faRecharge.setMobile(null);
            }

            return faRechargeMapper.selectFaRechargeList(faRecharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增充值
     *
     * @param faRecharge 充值
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaRecharge(FaRecharge faRecharge)
    {
        faRecharge.setCreateTime(DateUtils.getNowDate());
        return faRechargeMapper.insertFaRecharge(faRecharge);
    }

    /**
     * 修改充值
     *
     * @param faRecharge 充值
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaRecharge(FaRecharge faRecharge)
    {
        faRecharge.setMobile(null);
        faRecharge.setUpdateTime(DateUtils.getNowDate());
        return faRechargeMapper.updateFaRecharge(faRecharge);
    }

    /**
     * 批量删除充值
     *
     * @param ids 需要删除的充值主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaRechargeByIds(Integer[] ids)
    {
        return faRechargeMapper.deleteFaRechargeByIds(ids);
    }

    /**
     * 删除充值信息
     *
     * @param id 充值主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaRechargeById(Integer id)
    {
        return faRechargeMapper.deleteFaRechargeById(id);
    }

    /**
     * 审核充值
     * @param faRecharge
     */
    @Transactional
    @Override
    public void approveRecharge(FaRecharge faRecharge) throws Exception {
        if (null == faRecharge.getId() || null == faRecharge.getIsPay()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaRecharge recharge = this.getById(faRecharge.getId());
        if (ObjectUtils.isEmpty(recharge)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 已审核
        if (1 == recharge.getIsPay()) {
            throw new ServiceException(MessageUtils.message("recharge.already.approve"), HttpStatus.ERROR);
        }

        // 审核标记
        faRecharge.setIsApprove(1);

        faRecharge.setUpdateTime(new Date());

        // 已付款
        if (1 == faRecharge.getIsPay()) {
            faRecharge.setPayTime(new Date());
            // 资金流水
            iFaCapitalLogService.save(recharge);
        }

        this.updateFaRecharge(faRecharge);
    }

    /**
     * 充值统计
     * @param faRecharge
     * @return
     * @throws Exception
     */
    @Override
    public List<BigDecimal> getRechargeStatistics(FaRecharge faRecharge) throws Exception {
        return faRechargeMapper.getRechargeStatistics(faRecharge);
    }

    /**
     * 已付款总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalPaid(FaRecharge faRecharge) throws Exception {
        return faRechargeMapper.getTotalPaid(faRecharge);
    }

    /**
     * 未付款总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalUnpaid(FaRecharge faRecharge) throws Exception {
        return faRechargeMapper.getTotalUnpaid(faRecharge);
    }

    /**
     * 驳回总额
     * @param faRecharge
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalRefuse(FaRecharge faRecharge) throws Exception {
        return faRechargeMapper.getTotalRefuse(faRecharge);
    }

}