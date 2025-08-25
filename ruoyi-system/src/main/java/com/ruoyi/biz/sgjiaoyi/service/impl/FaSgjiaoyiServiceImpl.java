package com.ruoyi.biz.sgjiaoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.sgjiaoyi.mapper.FaSgjiaoyiMapper;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.mapper.FaShengouMapper;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.userNotice.service.IFaUserNoticeService;
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
 * 线下配售Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class FaSgjiaoyiServiceImpl extends ServiceImpl<FaSgjiaoyiMapper, FaSgjiaoyi> implements IFaSgjiaoyiService
{
    @Autowired
    private FaSgjiaoyiMapper faSgjiaoyiMapper;

    @Autowired
    private IFaUserNoticeService iFaUserNoticeService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private IFaShengouService iFaShengouService;

    @Autowired
    private FaShengouMapper faShengouMapper;

    /**
     * 查询线下配售
     *
     * @param id 线下配售主键
     * @return 线下配售
     */
    @Override
    public FaSgjiaoyi selectFaSgjiaoyiById(Long id)
    {
        FaSgjiaoyi faSgjiaoyi = faSgjiaoyiMapper.selectFaSgjiaoyiById(id);
        if (ObjectUtils.isNotEmpty(faSgjiaoyi) && ObjectUtils.isNotEmpty(faSgjiaoyi.getFaMember())) {
            faSgjiaoyi.setMemberName(faSgjiaoyi.getFaMember().getName());
            faSgjiaoyi.setMobile(faSgjiaoyi.getFaMember().getMobile());
        }
        return faSgjiaoyi;
    }

    /**
     * 查询线下配售列表
     *
     * @param faSgjiaoyi 线下配售
     * @return 线下配售
     */
    @Override
    public List<FaSgjiaoyi> selectFaSgjiaoyiList(FaSgjiaoyi faSgjiaoyi)
    {
        try {
            faSgjiaoyi.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faSgjiaoyi.getMobile())) {
                faSgjiaoyi.setSalt(AESUtil.encrypt(faSgjiaoyi.getMobile()));
                faSgjiaoyi.setMobile(null);
            }

            return faSgjiaoyiMapper.selectFaSgjiaoyiList(faSgjiaoyi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增线下配售
     *
     * @param faSgjiaoyi 线下配售
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi) throws Exception
    {
        faSgjiaoyi.setCreateTime(DateUtils.getNowDate());

        FaNewStock faNewStock = new FaNewStock();
        faNewStock.setId(faSgjiaoyi.getShengouId());
        faNewStock.setMemberId(faSgjiaoyi.getUserId());
        faNewStock.setSgNums(faSgjiaoyi.getSgNum());

        // 保证金模式，先扣
        iFaShengouService.addPeiShou(faNewStock);

        return 1;
    }

    /**
     * 修改线下配售
     *
     * @param faSgjiaoyi 线下配售
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi)
    {
        faSgjiaoyi.setUpdateTime(DateUtils.getNowDate());
        return faSgjiaoyiMapper.updateFaSgjiaoyi(faSgjiaoyi);
    }

    /**
     * 批量删除线下配售
     *
     * @param ids 需要删除的线下配售主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSgjiaoyiByIds(Long[] ids)
    {
        return faSgjiaoyiMapper.deleteFaSgjiaoyiByIds(ids);
    }

    /**
     * 删除线下配售信息
     *
     * @param id 线下配售主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSgjiaoyiById(Long id)
    {
        return faSgjiaoyiMapper.deleteFaSgjiaoyiById(id);
    }

    /**
     * 提交配售中签 保证金模式
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitAllocation(FaSgjiaoyi faSgjiaoyi) throws Exception {
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgjiaoyi::getId, faSgjiaoyi.getId());
        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        // 数据库状态
        FaSgjiaoyi sgjiaoyi = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(sgjiaoyi)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 已转未中签或弃购，禁止操作
        if (sgjiaoyi.getStatus() == 2 || sgjiaoyi.getStatus() == 3) {
            throw new ServiceException(MessageUtils.message("subscribe.operated"), HttpStatus.ERROR);
        }

        // 已转持仓，禁止操作
        if (sgjiaoyi.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        // 重复操作
        if (sgjiaoyi.getStatus().equals(faSgjiaoyi.getStatus())) {
            throw new ServiceException(MessageUtils.message("repeat.operate"), HttpStatus.ERROR);
        }

        // 申购中转中签 记录中签信息
        if (sgjiaoyi.getStatus() == 0 && faSgjiaoyi.getStatus() == 1) {
            faSgjiaoyi.setUpdateTime(new Date());
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算 提交过来的是zqNum
            if ("0".equals(gqpeizhi)) {
                faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 100);
                faSgjiaoyi.setZqMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getZqNums())));
            }
            // 按签计算 提交过来的是zqNum
            else if ("1".equals(gqpeizhi)) {
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == faSgjiaoyi.getSgType()) {
                    faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 1000);
                } else {
                    faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 500);
                }
                faSgjiaoyi.setZqMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getZqNums())));
            }

            // 剩余数量是否足够
            FaNewStock faNewStock = iFaShengouService.getById(sgjiaoyi.getShengouId());
            if (faNewStock.getXxNums() < faSgjiaoyi.getZqNums()) {
                throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
            }

            // 减少剩余数量
            LambdaUpdateWrapper<FaNewStock> newStockLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            newStockLambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
            newStockLambdaUpdateWrapper.set(FaNewStock::getXxNums, faNewStock.getXxNums() - faSgjiaoyi.getZqNums());
            newStockLambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
            iFaShengouService.update(newStockLambdaUpdateWrapper);

            this.updateFaSgjiaoyi(faSgjiaoyi);
            // 发中签通知
            iFaUserNoticeService.addAllocation(sgjiaoyi);
        }

        // 中签转申购中 清空中签信息
        if (sgjiaoyi.getStatus() == 1 && faSgjiaoyi.getStatus() == 0) {
            faSgjiaoyi.setUpdateTime(new Date());
            faSgjiaoyi.setZqNum(0);
            faSgjiaoyi.setZqNums(0);
            faSgjiaoyi.setZqMoney(BigDecimal.ZERO);
            this.updateFaSgjiaoyi(faSgjiaoyi);
        }

        // 转未中签 中签信息清空 认缴状态 资金解冻
        if (faSgjiaoyi.getStatus() == 2) {
            unAllocation(sgjiaoyi);
        }

        // 转弃购 中签信息清空 认缴状态 删除状态 资金解冻
        if (faSgjiaoyi.getStatus() == 3) {
            giveUpAllocation(sgjiaoyi);
        }
    }

    /**
     * 未中签
     * @param sgjiaoyi
     * @throws Exception
     */
    private void unAllocation(FaSgjiaoyi sgjiaoyi) throws Exception {
        // 未中签流水
        FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));

        // 已退过，退中签部分
        if (1 == sgjiaoyi.getIsTz()) {
            faCapitalLog.setMoney(sgjiaoyi.getZqMoney());
        }
        // 未退过，全退
        else if (0 == sgjiaoyi.getIsTz()) {
            faCapitalLog.setMoney(sgjiaoyi.getMoney());
        }

        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(sgjiaoyi.getId());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 余额退回
        iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);

        // 配售信息更新
        LambdaUpdateWrapper<FaSgjiaoyi> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSgjiaoyi::getId, sgjiaoyi.getId());
        // 未中签
        lambdaUpdateWrapper.set(FaSgjiaoyi::getStatus, 2);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqNum, 0);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqNums, 0);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqMoney, 0);
        // 未认缴
        lambdaUpdateWrapper.set(FaSgjiaoyi::getRenjiao, 0);
        // 已退回
        lambdaUpdateWrapper.set(FaSgjiaoyi::getIsTz, 1);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getIsTzTime, new Date());

        lambdaUpdateWrapper.set(FaSgjiaoyi::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 弃购
     * @param sgjiaoyi
     * @throws Exception
     */
    private void giveUpAllocation(FaSgjiaoyi sgjiaoyi) throws Exception {
        // 弃购流水
        FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));

        // 已退过，退中签部分
        if (1 == sgjiaoyi.getIsTz()) {
            faCapitalLog.setMoney(sgjiaoyi.getZqMoney());
        }
        // 未退过，全退
        else if (0 == sgjiaoyi.getIsTz()) {
            faCapitalLog.setMoney(sgjiaoyi.getMoney());
        }

        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(sgjiaoyi.getId());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 余额退回
        iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);

        // 配售信息更新
        LambdaUpdateWrapper<FaSgjiaoyi> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSgjiaoyi::getId, sgjiaoyi.getId());
        // 弃购
        lambdaUpdateWrapper.set(FaSgjiaoyi::getStatus, 3);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqNum, 0);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqNums, 0);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getZqMoney, 0);
        // 未认缴
        lambdaUpdateWrapper.set(FaSgjiaoyi::getRenjiao, 0);
        // 已退回
        lambdaUpdateWrapper.set(FaSgjiaoyi::getIsTz, 1);
        lambdaUpdateWrapper.set(FaSgjiaoyi::getIsTzTime, new Date());

        lambdaUpdateWrapper.set(FaSgjiaoyi::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 提交配售中签 补缴模式
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitAllocationPayLater(FaSgjiaoyi faSgjiaoyi) throws Exception {
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgjiaoyi::getId, faSgjiaoyi.getId());
        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        // 数据库状态
        FaSgjiaoyi sgjiaoyi = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(sgjiaoyi)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 已转未中签或弃购，禁止操作
        if (sgjiaoyi.getStatus() == 2 || sgjiaoyi.getStatus() == 3) {
            throw new ServiceException(MessageUtils.message("subscribe.operated"), HttpStatus.ERROR);
        }

        // 已转持仓，禁止操作
        if (sgjiaoyi.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        // 重复操作
        if (sgjiaoyi.getStatus().equals(faSgjiaoyi.getStatus())) {
            throw new ServiceException(MessageUtils.message("repeat.operate"), HttpStatus.ERROR);
        }

        // 申购中转中签 记录中签信息
        if (sgjiaoyi.getStatus() == 0 && faSgjiaoyi.getStatus() == 1) {
            faSgjiaoyi.setUpdateTime(new Date());
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算 提交过来的是zqNum
            if ("0".equals(gqpeizhi)) {
                faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 100);
                faSgjiaoyi.setZqMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getZqNums())));
            }
            // 按签计算 提交过来的是zqNum
            else if ("1".equals(gqpeizhi)) {
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == faSgjiaoyi.getSgType()) {
                    faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 1000);
                } else {
                    faSgjiaoyi.setZqNums(faSgjiaoyi.getZqNum() * 500);
                }
                faSgjiaoyi.setZqMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getZqNums())));
            }
            this.updateFaSgjiaoyi(faSgjiaoyi);
            // 发中签通知
            iFaUserNoticeService.addAllocation(sgjiaoyi);

            // 扣款
            FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
            // 余额不够
            if (faMember.getBalance().compareTo(faSgjiaoyi.getZqMoney()) < 0) {
                // 实缴金额
                faSgjiaoyi.setDjMoney(faMember.getBalance());
                if (faSgjiaoyi.getDjMoney().compareTo(BigDecimal.ZERO) < 0) {
                    faSgjiaoyi.setDjMoney(BigDecimal.ZERO);
                }
                // 需要补缴
                faSgjiaoyi.setPayLater(1);
            }
            // 余额足够
            else {
                // 实缴金额
                faSgjiaoyi.setDjMoney(faSgjiaoyi.getZqMoney());
                // 无需补缴
                faSgjiaoyi.setPayLater(0);
            }

            faSgjiaoyi.setUpdateTime(new Date());

            if (0 == faSgjiaoyi.getPayLater()) {
                // 转持仓
//                transOneToHold(faSgjiaoyi);
                // 认缴状态
                faSgjiaoyi.setRenjiao(1);
                faSgjiaoyi.setRenjiaoTime(new Date());
            }

            this.updateFaSgjiaoyi(faSgjiaoyi);

            // 扣钱，记录流水
            iFaCapitalLogService.savePeiShou(faSgjiaoyi);
        }

        // 中签转申购中 清空中签信息
        if (sgjiaoyi.getStatus() == 1 && faSgjiaoyi.getStatus() == 0) {
            if (sgjiaoyi.getDjMoney().compareTo(BigDecimal.ZERO) > 0) {
                // 未中签流水
                FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(faMember.getId());
                faCapitalLog.setMobile(faMember.getMobile());
                faCapitalLog.setName(faMember.getName());
                faCapitalLog.setSuperiorId(faMember.getSuperiorId());
                faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
                faCapitalLog.setSuperiorName(faMember.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));

                // 退回实缴
                faCapitalLog.setMoney(sgjiaoyi.getDjMoney());

                faCapitalLog.setBeforeMoney(faMember.getBalance());
                faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
                faCapitalLog.setDirect(0);
                faCapitalLog.setCreateTime(new Date());
                faCapitalLog.setOrderId(sgjiaoyi.getId());
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);

                // 余额退回
                iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);
            }

            faSgjiaoyi.setUpdateTime(new Date());
            faSgjiaoyi.setZqNum(0);
            faSgjiaoyi.setZqNums(0);
            faSgjiaoyi.setZqMoney(BigDecimal.ZERO);
            faSgjiaoyi.setRenjiao(0);
            faSgjiaoyi.setDjMoney(BigDecimal.ZERO);
            this.updateFaSgjiaoyi(faSgjiaoyi);
        }

        // 转未中签 中签信息清空 认缴状态 资金解冻
        if (faSgjiaoyi.getStatus() == 2) {
            if (sgjiaoyi.getDjMoney().compareTo(BigDecimal.ZERO) > 0) {
                // 未中签流水
                FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(faMember.getId());
                faCapitalLog.setMobile(faMember.getMobile());
                faCapitalLog.setName(faMember.getName());
                faCapitalLog.setSuperiorId(faMember.getSuperiorId());
                faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
                faCapitalLog.setSuperiorName(faMember.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));

                // 退回实缴
                faCapitalLog.setMoney(sgjiaoyi.getDjMoney());

                faCapitalLog.setBeforeMoney(faMember.getBalance());
                faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
                faCapitalLog.setDirect(0);
                faCapitalLog.setCreateTime(new Date());
                faCapitalLog.setOrderId(sgjiaoyi.getId());
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);

                // 余额退回
                iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);
            }

            faSgjiaoyi.setUpdateTime(new Date());
            faSgjiaoyi.setZqNum(0);
            faSgjiaoyi.setZqNums(0);
            faSgjiaoyi.setZqMoney(BigDecimal.ZERO);
            faSgjiaoyi.setRenjiao(0);
            faSgjiaoyi.setDjMoney(BigDecimal.ZERO);
            this.updateFaSgjiaoyi(faSgjiaoyi);
        }

        // 转弃购 中签信息清空 认缴状态 删除状态 资金解冻
        if (faSgjiaoyi.getStatus() == 3) {
            if (sgjiaoyi.getDjMoney().compareTo(BigDecimal.ZERO) > 0) {
                // 未中签流水
                FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(faMember.getId());
                faCapitalLog.setMobile(faMember.getMobile());
                faCapitalLog.setName(faMember.getName());
                faCapitalLog.setSuperiorId(faMember.getSuperiorId());
                faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
                faCapitalLog.setSuperiorName(faMember.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));

                // 退回实缴
                faCapitalLog.setMoney(sgjiaoyi.getDjMoney());

                faCapitalLog.setBeforeMoney(faMember.getBalance());
                faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
                faCapitalLog.setDirect(0);
                faCapitalLog.setCreateTime(new Date());
                faCapitalLog.setOrderId(sgjiaoyi.getId());
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);

                // 余额退回
                iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);
            }

            faSgjiaoyi.setUpdateTime(new Date());
            faSgjiaoyi.setZqNum(0);
            faSgjiaoyi.setZqNums(0);
            faSgjiaoyi.setZqMoney(BigDecimal.ZERO);
            faSgjiaoyi.setRenjiao(0);
            faSgjiaoyi.setDjMoney(BigDecimal.ZERO);
            this.updateFaSgjiaoyi(faSgjiaoyi);
        }
    }

    /**
     * 查询用户配售列表
     * @param faSgjiaoyi
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaSgjiaoyi> getMemberPsList(FaSgjiaoyi faSgjiaoyi) throws Exception {
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgjiaoyi::getUserId, faSgjiaoyi.getUserId());
        if (null != faSgjiaoyi.getStatus()) {
            lambdaQueryWrapper.eq(FaSgjiaoyi::getStatus, faSgjiaoyi.getStatus());
        } else {
            lambdaQueryWrapper.in(FaSgjiaoyi::getStatus, 0, 1, 2);
        }

        if (null != faSgjiaoyi.getSgType()) {
            lambdaQueryWrapper.eq(FaSgjiaoyi::getSgType, faSgjiaoyi.getSgType());
        }

        if (null != faSgjiaoyi.getIsCc()) {
            lambdaQueryWrapper.eq(FaSgjiaoyi::getIsCc, faSgjiaoyi.getIsCc());
        }

        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaSgjiaoyi::getCreateTime);
        IPage<FaSgjiaoyi> faSgjiaoyiIPage = this.page(new Page<>(faSgjiaoyi.getPage(), faSgjiaoyi.getSize()), lambdaQueryWrapper);

        for (FaSgjiaoyi sgjiaoyi : faSgjiaoyiIPage.getRecords()) {
            FaNewStock faNewStock = iFaShengouService.getById(sgjiaoyi.getShengouId());
            if (ObjectUtils.isNotEmpty(faNewStock) && null != faNewStock.getFxRate()) {
                sgjiaoyi.setFxRate(new BigDecimal(faNewStock.getFxRate()));
            }
        }

        return faSgjiaoyiIPage;
    }

    /**
     * 配售认缴
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void subscription(FaSgjiaoyi faSgjiaoyi) throws Exception {
        // 参数判断
        if (null == faSgjiaoyi.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgjiaoyi::getId, faSgjiaoyi.getId());
        lambdaQueryWrapper.eq(FaSgjiaoyi::getUserId, faSgjiaoyi.getUserId());
        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        FaSgjiaoyi sgjiaoyi = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(sgjiaoyi)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 认缴状态判断
        if (1 == sgjiaoyi.getRenjiao()) {
            throw new ServiceException(MessageUtils.message("member.already.subscription"), HttpStatus.ERROR);
        }
        FaMember faMember = iFaMemberService.getById(sgjiaoyi.getUserId());
        // 限制认缴开关 0关闭 可以扣成负数    1开启  判断余额是否足够
        String psxzrj = iFaRiskConfigService.getConfigValue("psxzrj", "1");
        if ("1".equals(psxzrj)) {
            if (faMember.getBalance().compareTo(sgjiaoyi.getZqMoney()) < 0) {
                throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
            }
        }

        sgjiaoyi.setFaMember(faMember);
        // 记录认缴流水
        iFaCapitalLogService.save(sgjiaoyi);
        // 更新申购信息
        sgjiaoyi.setRenjiao(1);
        sgjiaoyi.setRenjiaoTime(new Date());
        sgjiaoyi.setUpdateTime(new Date());
        this.updateFaSgjiaoyi(sgjiaoyi);

        // 发认缴通知
        iFaUserNoticeService.addSubscription(sgjiaoyi);
    }


    /**
     * 一键转持仓
     * @throws Exception
     */
    @Transactional
    @Override
    public void transToHold() throws Exception {
        // 取出未转持仓配售
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 申购中/中签
        lambdaQueryWrapper.in(FaSgjiaoyi::getStatus, 0, 1);
        // 未转持仓
        lambdaQueryWrapper.eq(FaSgjiaoyi::getIsCc, 0);
        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        List<FaSgjiaoyi> faSgjiaoyiList = this.list(lambdaQueryWrapper);

        for (FaSgjiaoyi stockPs : faSgjiaoyiList) {
            // 是否上市
            FaNewStock faNewStock = faShengouMapper.selectById(stockPs.getShengouId());
            // 上市
            if (1 == faNewStock.getIsList()) {
                // 中签且认缴 --》转持仓
                if (1 == stockPs.getStatus() && 1 == stockPs.getRenjiao()) {
                    transPeiShouToHold(stockPs);
                }
                // 申购中 --》转弃购
                else if (0 == stockPs.getStatus()) {
                    giveUpAllocation(stockPs);
                }
            }
        }
    }


    /**
     * 单个转持仓
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Override
    public void transOneToHold(FaSgjiaoyi faSgjiaoyi) throws Exception {
        if (null == faSgjiaoyi.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faSgjiaoyi = this.getById(faSgjiaoyi.getId());
        if (ObjectUtils.isEmpty(faSgjiaoyi)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 是否已转持仓
        if (faSgjiaoyi.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        transPeiShouToHold(faSgjiaoyi);
    }

    private void transPeiShouToHold(FaSgjiaoyi stockPs) throws Exception {
        // 用户信息
        FaMember faMember = iFaMemberService.getById(stockPs.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 股票
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, stockPs.getCode());
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        FaStrategy faStrategy = faStrategyMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 保存持仓明细
        FaStockHoldDetail faStockHoldDetail = new FaStockHoldDetail();
        faStockHoldDetail.setMemberId(faMember.getId());
        faStockHoldDetail.setPid(faMember.getSuperiorId());
        faStockHoldDetail.setPidCode(faMember.getSuperiorCode());
        faStockHoldDetail.setPidName(faMember.getSuperiorName());
        faStockHoldDetail.setStockId(faStrategy.getId());
        faStockHoldDetail.setStockName(faStrategy.getTitle());
        faStockHoldDetail.setStockSymbol(faStrategy.getCode());
        faStockHoldDetail.setAllCode(faStrategy.getAllCode());
        faStockHoldDetail.setStockType(faStrategy.getType());
        // 持仓手数用股数计算
        faStockHoldDetail.setHoldHand(stockPs.getZqNums() / 100);
        faStockHoldDetail.setHoldNumber(stockPs.getZqNums());
        faStockHoldDetail.setAverage(stockPs.getSgFxPrice());
        // 新股转
        faStockHoldDetail.setResourceType(1);
        faStockHoldDetail.setCreateTime(new Date());
        faStockHoldDetail.setStatus(0);
        faStockHoldDetail.setDeleteFlag(0);
        faStockHoldDetail.setFreezeNumber(stockPs.getZqNums());
        faStockHoldDetail.setFreezeDaysLeft(1);
        faStockHoldDetail.setFreezeStatus(0);
        // 配售
        faStockHoldDetail.setHoldId(1);
        // 配售id
        faStockHoldDetail.setNewStockId(stockPs.getId());
        // 已上市，新股持仓
        faStockHoldDetail.setHoldType(0);
        faStockHoldDetail.setIsList(1);

        // 买入价，买入时间
        faStockHoldDetail.setBuyPrice(stockPs.getSgFxPrice());
        faStockHoldDetail.setBuyTime(faStockHoldDetail.getCreateTime());
        faStockHoldDetail.setTradingHand(faStockHoldDetail.getHoldHand());
        faStockHoldDetail.setTradingNumber(faStockHoldDetail.getHoldNumber());

        iFaStockHoldDetailService.save(faStockHoldDetail);

        // 更新申购状态
        stockPs.setIsCc(1);
        stockPs.setIsCcTime(new Date());
        stockPs.setUpdateTime(new Date());
        this.updateFaSgjiaoyi(stockPs);

        // 未退过金额
        if (0 == stockPs.getIsTz()) {
            // 配售解冻流水，是否有未中签的金额
            if (stockPs.getMoney().compareTo(stockPs.getZqMoney()) > 0) {
                stockPs.setIsTz(1);
                stockPs.setIsTzTime(new Date());
                this.updateFaSgjiaoyi(stockPs);

                FaCapitalLog faCapitalLog = new FaCapitalLog();
                faCapitalLog.setUserId(faMember.getId());
                faCapitalLog.setMobile(faMember.getMobile());
                faCapitalLog.setName(faMember.getName());
                faCapitalLog.setSuperiorId(faMember.getSuperiorId());
                faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
                faCapitalLog.setSuperiorName(faMember.getSuperiorName());
                faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));
                faCapitalLog.setMoney(stockPs.getMoney().subtract(stockPs.getZqMoney()));
                faCapitalLog.setBeforeMoney(faMember.getBalance());
                faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
                faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
                faCapitalLog.setDirect(0);
                faCapitalLog.setCreateTime(new Date());
                faCapitalLog.setOrderId(stockPs.getId());
                faCapitalLog.setDeleteFlag(0);
                iFaCapitalLogService.save(faCapitalLog);

                // 更新余额
                iFaMemberService.updateMemberBalance(stockPs.getUserId(), faCapitalLog.getMoney(), 0);
            }
        }
    }

    /**
     * 未中签退费
     * @param ids
     * @throws Exception
     */
    @Override
    public void refund(Long[] ids) throws Exception {
        if (ids.length > 0) {
            for (Long id : ids) {
                FaSgjiaoyi faSgjiaoyi = this.getById(id);
                if (ObjectUtils.isNotEmpty(faSgjiaoyi) && 0 == faSgjiaoyi.getIsTz()) {
                    FaMember faMember = iFaMemberService.getById(faSgjiaoyi.getUserId());

                    FaCapitalLog faCapitalLog = new FaCapitalLog();
                    faCapitalLog.setUserId(faMember.getId());
                    faCapitalLog.setMobile(faMember.getMobile());
                    faCapitalLog.setName(faMember.getName());
                    faCapitalLog.setSuperiorId(faMember.getSuperiorId());
                    faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
                    faCapitalLog.setSuperiorName(faMember.getSuperiorName());
                    faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NOT_ALLOCATION_RETURN));
                    faCapitalLog.setMoney(faSgjiaoyi.getMoney().subtract(faSgjiaoyi.getZqMoney()));
                    faCapitalLog.setBeforeMoney(faMember.getBalance());
                    faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
                    faCapitalLog.setType(CapitalFlowConstants.NOT_ALLOCATION_RETURN);
                    faCapitalLog.setDirect(0);
                    faCapitalLog.setCreateTime(new Date());
                    faCapitalLog.setOrderId(faSgjiaoyi.getId());
                    faCapitalLog.setDeleteFlag(0);
                    iFaCapitalLogService.save(faCapitalLog);

                    // 更新余额
                    iFaMemberService.updateMemberBalance(faSgjiaoyi.getUserId(), faCapitalLog.getMoney(), 0);

                    faSgjiaoyi.setIsTz(1);
                    faSgjiaoyi.setIsTzTime(new Date());
                    this.updateFaSgjiaoyi(faSgjiaoyi);
                }
            }
        }
    }

    /**
     * 增加的余额去补缴
     * @param id
     * @param amount
     * @throws Exception
     */
    @Override
    public void finishPayLater(Integer id, BigDecimal amount) throws Exception {
        BigDecimal leftAmount = amount;
        // 取出待补缴的配售
        LambdaQueryWrapper<FaSgjiaoyi> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgjiaoyi::getUserId, id);
        lambdaQueryWrapper.eq(FaSgjiaoyi::getPayLater, 1);
        lambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByAsc(FaSgjiaoyi::getCreateTime);
        List<FaSgjiaoyi> list = this.list(lambdaQueryWrapper);
        for (FaSgjiaoyi faSgjiaoyi : list) {
            // 补缴 = 应缴 - 实缴
            if (leftAmount.compareTo(faSgjiaoyi.getZqMoney().subtract(faSgjiaoyi.getDjMoney())) < 0) {
                faSgjiaoyi.setDjMoney(faSgjiaoyi.getDjMoney().add(leftAmount));
                faSgjiaoyi.setUpdateTime(new Date());
                this.updateFaSgjiaoyi(faSgjiaoyi);
                break;
            } else {
                faSgjiaoyi.setDjMoney(faSgjiaoyi.getZqMoney());
                faSgjiaoyi.setPayLater(2);
                faSgjiaoyi.setUpdateTime(new Date());
                this.updateFaSgjiaoyi(faSgjiaoyi);

                // 转持仓
//                transOneToHold(faSgjiaoyi);

                leftAmount = leftAmount.subtract(faSgjiaoyi.getZqMoney().add(faSgjiaoyi.getDjMoney()));
            }
        }
    }

}