package com.ruoyi.biz.capitalLog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.mapper.FaCapitalLogMapper;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.fundPool.service.FundPoolService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 资金记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Service
public class FaCapitalLogServiceImpl extends ServiceImpl<FaCapitalLogMapper, FaCapitalLog> implements IFaCapitalLogService
{
    @Autowired
    private FaCapitalLogMapper faCapitalLogMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    /**
     * 查询资金记录
     *
     * @param id 资金记录主键
     * @return 资金记录
     */
    @Override
    public FaCapitalLog selectFaCapitalLogById(Integer id)
    {
        return faCapitalLogMapper.selectFaCapitalLogById(id);
    }

    /**
     * 查询资金记录列表
     *
     * @param faCapitalLog 资金记录
     * @return 资金记录
     */
    @Override
    public List<FaCapitalLog> selectFaCapitalLogList(FaCapitalLog faCapitalLog)
    {
        try {
            faCapitalLog.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faCapitalLog.getMobile())) {
                faCapitalLog.setSalt(AESUtil.encrypt(faCapitalLog.getMobile()));
                faCapitalLog.setMobile(null);
            }

            return faCapitalLogMapper.selectFaCapitalLogList(faCapitalLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    @Override
    public int insertFaCapitalLog(FaCapitalLog faCapitalLog)
    {
        faCapitalLog.setCreateTime(DateUtils.getNowDate());
        return faCapitalLogMapper.insertFaCapitalLog(faCapitalLog);
    }

    /**
     * 修改资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    @Override
    public int updateFaCapitalLog(FaCapitalLog faCapitalLog)
    {
        faCapitalLog.setMobile(null);
        faCapitalLog.setUpdateTime(DateUtils.getNowDate());
        return faCapitalLogMapper.updateFaCapitalLog(faCapitalLog);
    }

    /**
     * 批量删除资金记录
     *
     * @param ids 需要删除的资金记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCapitalLogByIds(Integer[] ids)
    {
        return faCapitalLogMapper.deleteFaCapitalLogByIds(ids);
    }

    /**
     * 删除资金记录信息
     *
     * @param id 资金记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCapitalLogById(Integer id)
    {
        return faCapitalLogMapper.deleteFaCapitalLogById(id);
    }

    /**
     * 查询资金记录
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaCapitalLog> getFundRecord(FaCapitalLog faCapitalLog) throws Exception {
//        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, faCapitalLog.getUserId());
//        if (null != faCapitalLog.getType()) {
//            lambdaQueryWrapper.eq(FaCapitalLog::getType, faCapitalLog.getType());
//        }

//        类型(0充值 1提现 2普通下单 3普通下单手续费 4印花税 5平仓收益 6中签认缴 7提现退回 8大宗下单 " +
//                "9大宗下单手续费 10大宗平仓收益 11大宗印花税 12配售冻结 13未中签退回 14普通卖出手续费 15大宗卖出手续费" +
//                "16VIP调研下单 17VIP调研下单手续费 18VIP调研平仓收益 19VIP调研印花税 20VIP调研卖出手续费)
//        25融券下单 26融券下单手续费 27融券平仓收益 28融券印花税 29融券平仓手续费
//        "31重组下单 32重组下单手续费 33重组平仓收益 34重组印花税 35重组平仓手续费" +
//        lambdaQueryWrapper.notIn(FaCapitalLog::getType, 0, 1, 3, 4, 6, 7, 12, 13, 9, 11, 14, 15, 17, 19, 20, 26, 28, 29, 32, 34, 35);
//
//        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
//        lambdaQueryWrapper.orderByDesc(FaCapitalLog::getId);

        MPJLambdaWrapper<FaCapitalLog> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.selectAll(FaCapitalLog.class);
        mpjLambdaWrapper.selectAssociation(FaStockHoldDetail.class, FaCapitalLog::getFaStockHoldDetail);
        mpjLambdaWrapper.leftJoin(FaStockTrading.class, FaStockTrading::getId, FaCapitalLog::getOrderId);
        mpjLambdaWrapper.leftJoin(FaStockHoldDetail.class, FaStockHoldDetail::getId, FaStockTrading::getHoldId);

        mpjLambdaWrapper.eq(FaCapitalLog::getUserId, faCapitalLog.getUserId());
        if (null != faCapitalLog.getType()) {
            mpjLambdaWrapper.eq(FaCapitalLog::getType, faCapitalLog.getType());
        }

//        类型(0充值 1提现 2普通下单 3普通下单手续费 4印花税 5平仓收益 6中签认缴 7提现退回 8大宗下单 " +
//                "9大宗下单手续费 10大宗平仓收益 11大宗印花税 12配售冻结 13未中签退回 14普通卖出手续费 15大宗卖出手续费" +
//                "16VIP调研下单 17VIP调研下单手续费 18VIP调研平仓收益 19VIP调研印花税 20VIP调研卖出手续费)
//        25融券下单 26融券下单手续费 27融券平仓收益 28融券印花税 29融券平仓手续费
//        "31重组下单 32重组下单手续费 33重组平仓收益 34重组印花税 35重组平仓手续费" +
//        "40买入集合资产 41赎回集合资产 42赎回退回" +
//                "1002资金池增加 1003资金池转出 1004转入资金池 1005资金池转出 1006转入资金池)")
        mpjLambdaWrapper.notIn(FaCapitalLog::getType,
                CapitalFlowConstants.RECHARGE,
                CapitalFlowConstants.WITHDRAW,
                CapitalFlowConstants.NORMAL_BUY_FEE,
                CapitalFlowConstants.NORMAL_SELL_STAMP_DUTY,
                CapitalFlowConstants.SUBSCRIPTION,
                CapitalFlowConstants.WITHDRAW_RETURN,
                CapitalFlowConstants.PS_FREEZE,
                CapitalFlowConstants.NOT_ALLOCATION_RETURN,
                CapitalFlowConstants.DZ_BUY_FEE,
                CapitalFlowConstants.DZ_SELL_STAMP_DUTY,
                CapitalFlowConstants.NORMAL_SELL_FEE,
                CapitalFlowConstants.DZ_SELL_FEE,
                CapitalFlowConstants.VIP_BUY_FEE,
                CapitalFlowConstants.VIP_SELL_STAMP_DUTY,
                CapitalFlowConstants.VIP_SELL_FEE,
                CapitalFlowConstants.RQ_FEE,
                CapitalFlowConstants.RQ_SELL_STAMP_DUTY,
                CapitalFlowConstants.RQ_SELL_FEE,
                CapitalFlowConstants.YYCZ_BUY_FEE,
                CapitalFlowConstants.YYCZ_SELL_STAMP_DUTY,
                CapitalFlowConstants.YYCZ_SELL_FEE);

        mpjLambdaWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
        mpjLambdaWrapper.orderByDesc(FaCapitalLog::getId);

        IPage<FaCapitalLog> faCapitalLogIPage = faCapitalLogMapper.selectJoinPage(new Page<>(faCapitalLog.getPage(), faCapitalLog.getSize()), FaCapitalLog.class, mpjLambdaWrapper);
        if (!faCapitalLogIPage.getRecords().isEmpty()) {
            for (FaCapitalLog log : faCapitalLogIPage.getRecords()) {
                // 买入流水，集合买入手续费
                if (CapitalFlowConstants.NORMAL_BUY == log.getType() ||
                        CapitalFlowConstants.DZ_BUY == log.getType() ||
                        CapitalFlowConstants.VIP_BUY == log.getType() ||
                        CapitalFlowConstants.RQ_BUY == log.getType() ||
                        CapitalFlowConstants.YYCZ_BUY == log.getType())
                {
                    String description = "买入股票，" + log.getStockSymbol() + "/" + log.getStockName() + "，";
                    description += "占用本金：" + log.getMoney().setScale(2, RoundingMode.HALF_UP) + "，";
                    // 手续费
                    BigDecimal fee = BigDecimal.ZERO;
                    if (ObjectUtils.isNotEmpty(log.getFaStockHoldDetail())) {
                        fee = log.getFaStockHoldDetail().getBuyPoundage();
                    }
                    description += "总手续费：" + fee.setScale(2, RoundingMode.HALF_UP) + "，递延费：0.00，";
                    // 印花税
                    BigDecimal stampDuty = BigDecimal.ZERO;
                    description += "印花税：" + stampDuty.setScale(2, RoundingMode.HALF_UP);
                    log.setDescription(description);
                }
                // 卖出流水，集合卖出手续费，卖出印花税
                else if (CapitalFlowConstants.NORMAL_SELL == log.getType() ||
                            CapitalFlowConstants.DZ_SELL == log.getType() ||
                            CapitalFlowConstants.VIP_SELL == log.getType() ||
                            CapitalFlowConstants.RQ_SELL == log.getType() ||
                            CapitalFlowConstants.YYCZ_SELL == log.getType())
                {
                    String description = "卖出股票，" + log.getStockSymbol() + "/" + log.getStockName() + "，";
                    description += "占用本金：" + log.getMoney().setScale(2, RoundingMode.HALF_UP) + "，";
                    // 手续费
                    BigDecimal fee = BigDecimal.ZERO;
                    if (ObjectUtils.isNotEmpty(log.getFaStockHoldDetail())) {
                        fee = log.getFaStockHoldDetail().getSellPoundage();
                    }
                    description += "总手续费：" + fee.setScale(2, RoundingMode.HALF_UP) + "，递延费：0.00，";
                    // 印花税
                    BigDecimal stampDuty = BigDecimal.ZERO;
                    if (ObjectUtils.isNotEmpty(log.getFaStockHoldDetail())) {
                        stampDuty = log.getFaStockHoldDetail().getSellStampDuty();
                    }
                    description += "印花税：" + stampDuty.setScale(2, RoundingMode.HALF_UP) + "，";
                    // 盈亏
                    BigDecimal profit = BigDecimal.ZERO;
                    if (ObjectUtils.isNotEmpty(log.getFaStockHoldDetail())) {
                        profit = log.getFaStockHoldDetail().getProfitLose();
                    }
                    description += "盈亏：" + profit.setScale(2, RoundingMode.HALF_UP) + "，";
                    // 总盈亏
                    BigDecimal totalProfit = profit.subtract(fee).subtract(stampDuty);
                    description += "总盈亏：" + totalProfit.setScale(2, RoundingMode.HALF_UP);
                    log.setDescription(description);
                }
            }
        }
        return faCapitalLogIPage;
    }

    /**
     * 查询资金池记录
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaCapitalLog> getFundPoolRecord(FaCapitalLog faCapitalLog) throws Exception {
        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, faCapitalLog.getUserId());
        if (null != faCapitalLog.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, faCapitalLog.getType());
        } else {
            lambdaQueryWrapper.in(FaCapitalLog::getType, CapitalFlowConstants.OUT_MEMBER, CapitalFlowConstants.IN_MEMBER);
        }

        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaCapitalLog::getId);
        IPage<FaCapitalLog> faCapitalLogIPage = this.page(new Page<>(faCapitalLog.getPage(), faCapitalLog.getSize()), lambdaQueryWrapper);
        if (faCapitalLogIPage.getRecords().size() > 0) {
            for (FaCapitalLog capitalLog : faCapitalLogIPage.getRecords()) {
                FaMember faMember = iFaMemberService.getById(capitalLog.getUserId());
                if (ObjectUtils.isNotEmpty(faMember)) {
                    capitalLog.setWeiyima(faMember.getWeiyima().substring(0, 4) + "****" + faMember.getWeiyima().substring(8));
                }
            }
        }
        return faCapitalLogIPage;
    }

    /**
     * 关联盈亏
     * @param log
     * @return
     * @throws Exception
     */
    private BigDecimal getProfit(FaCapitalLog log) throws Exception {
        BigDecimal profit = BigDecimal.ZERO;
        // 交易记录
        FaStockTrading faStockTrading = iFaStockTradingService.getById(log.getOrderId());
        if (ObjectUtils.isNotEmpty(faStockTrading)) {
            // 持仓记录
            FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.getById(faStockTrading.getHoldId());
            if (ObjectUtils.isNotEmpty(faStockHoldDetail)) {
                profit = faStockHoldDetail.getProfitLose();
            }
        }
        return profit;
    }

    /**
     * 保存普通交易资金流水
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaStockTrading faStockTrading) throws Exception {
        // 买入流水
        if (1 == faStockTrading.getTradingType()) {
            saveBuyCapitalLog(faStockTrading);
        }
        // 卖出流水
        else if (2 == faStockTrading.getTradingType()) {
            saveSellCapitalLog(faStockTrading);
        }
    }

    /**
     * 买入流水
     * @param faStockTrading
     */
    private void saveBuyCapitalLog(FaStockTrading faStockTrading) throws Exception {
        BigDecimal beforeMoney = faStockTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faStockTrading.getFaMember().getBalance().subtract(faStockTrading.getTradingAmount());

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faStockTrading.getFaMember().getId());
        faCapitalLog.setMobile(faStockTrading.getFaMember().getMobile());
        faCapitalLog.setName(faStockTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
        if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_BUY));
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_BUY));
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VIP_BUY));
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RQ_BUY));
        } else if (9 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.YYCZ_BUY));
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faStockTrading.getTradingAmount());
        if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.NORMAL_BUY);
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.DZ_BUY);
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.VIP_BUY);
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.RQ_BUY);
        } else if (9 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.YYCZ_BUY);
        }
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faStockTrading.getId());
        faCapitalLog.setStockId(faStockTrading.getFaStrategy().getId());
        faCapitalLog.setStockName(faStockTrading.getFaStrategy().getTitle());
        faCapitalLog.setStockSymbol(faStockTrading.getFaStrategy().getCode());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();
        if (faStockTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setTradingPoundage(faStockTrading.getTradingPoundage().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faStockTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faStockTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_BUY_FEE));
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_BUY_FEE));
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VIP_BUY_FEE));
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RQ_FEE));
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.YYCZ_BUY_FEE));
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faStockTrading.getTradingPoundage());
            if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.NORMAL_BUY_FEE);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.DZ_BUY_FEE);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.VIP_BUY_FEE);
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.RQ_FEE);
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.YYCZ_BUY_FEE);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLogFee.setOrderId(faStockTrading.getId());
            faCapitalLogFee.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogFee.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogFee.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 减少 交易金额+手续费
        iFaMemberService.updateMemberBalance(faStockTrading.getMemberId(), faStockTrading.getTradingAmount().add(faStockTrading.getTradingPoundage()), 1);

        // 更新用户冻结 减少 交易金额+手续费
        iFaMemberService.updateFaMemberFreezeProfit(faStockTrading.getMemberId(), faStockTrading.getTradingAmount().add(faStockTrading.getTradingPoundage()), 1, 0);
    }

    /**
     * 卖出流水
     * @param faStockTrading
     */
    private void saveSellCapitalLog(FaStockTrading faStockTrading) throws Exception {
        BigDecimal beforeMoney = faStockTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faStockTrading.getFaMember().getBalance().add(faStockTrading.getTradingAmount());

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faStockTrading.getFaMember().getId());
        faCapitalLog.setMobile(faStockTrading.getFaMember().getMobile());
        faCapitalLog.setName(faStockTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
        if (0 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL));
        } else if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL));
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_SELL));
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VIP_SELL));
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RQ_SELL));
        } else if (9 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.YYCZ_SELL));
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faStockTrading.getTradingAmount());
        if (0 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.NORMAL_SELL);
        } else if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.NORMAL_SELL);
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.DZ_SELL);
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.VIP_SELL);
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.RQ_SELL);
        } else if (9 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(CapitalFlowConstants.YYCZ_SELL);
        }
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faStockTrading.getId());
        faCapitalLog.setStockId(faStockTrading.getFaStrategy().getId());
        faCapitalLog.setStockName(faStockTrading.getFaStrategy().getTitle());
        faCapitalLog.setStockSymbol(faStockTrading.getFaStrategy().getCode());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 印花税
        FaCapitalLog faCapitalLogDuty = new FaCapitalLog();
        if (faStockTrading.getStampDuty().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setStampDuty(faStockTrading.getStampDuty().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = faCapitalLog.getLaterMoney();
            laterMoney = faCapitalLog.getLaterMoney().subtract(faStockTrading.getStampDuty());

            faCapitalLogDuty.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogDuty.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogDuty.setName(faStockTrading.getFaMember().getName());
            faCapitalLogDuty.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogDuty.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogDuty.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL_STAMP_DUTY));
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL_STAMP_DUTY));
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_SELL_STAMP_DUTY));
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VIP_SELL_STAMP_DUTY));
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RQ_SELL_STAMP_DUTY));
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.YYCZ_SELL_STAMP_DUTY));
            }
            faCapitalLogDuty.setBeforeMoney(beforeMoney);
            faCapitalLogDuty.setLaterMoney(laterMoney);
            faCapitalLogDuty.setMoney(faStockTrading.getStampDuty());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.NORMAL_SELL_STAMP_DUTY);
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.NORMAL_SELL_STAMP_DUTY);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.DZ_SELL_STAMP_DUTY);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.VIP_SELL_STAMP_DUTY);
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.RQ_SELL_STAMP_DUTY);
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(CapitalFlowConstants.YYCZ_SELL_STAMP_DUTY);
            }
            faCapitalLogDuty.setDirect(1);
            faCapitalLogDuty.setCreateTime(new Date());
            faCapitalLogDuty.setOrderId(faStockTrading.getId());
            faCapitalLogDuty.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogDuty.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogDuty.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogDuty.setDeleteFlag(0);
            this.save(faCapitalLogDuty);
        }

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();
        if (faStockTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setTradingPoundage(faStockTrading.getTradingPoundage().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faStockTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faStockTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL_FEE));
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.NORMAL_SELL_FEE));
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_SELL_FEE));
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VIP_SELL_FEE));
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RQ_SELL_FEE));
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.YYCZ_SELL_FEE));
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faStockTrading.getTradingPoundage());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.NORMAL_SELL_FEE);
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.NORMAL_SELL_FEE);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.DZ_SELL_FEE);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.VIP_SELL_FEE);
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.RQ_SELL_FEE);
            } else if (9 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(CapitalFlowConstants.YYCZ_SELL_FEE);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLogFee.setOrderId(faStockTrading.getId());
            faCapitalLogFee.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogFee.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogFee.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 增加 交易金额-印花税-手续费
        iFaMemberService.updateMemberBalance(faStockTrading.getMemberId(),
                faStockTrading.getTradingAmount().subtract(faStockTrading.getStampDuty()).subtract(faStockTrading.getTradingPoundage()), 0);

        // 判断卖出资金T+N 冻结资金 增加 交易金额-印花税-手续费
        int tn = Integer.parseInt(iFaRiskConfigService.getConfigValue("kq_dj", "1"));
        if (tn > 0){
            iFaMemberService.updateFaMemberFreezeProfit(faStockTrading.getMemberId(),
                    faStockTrading.getTradingAmount().subtract(faStockTrading.getStampDuty()).subtract(faStockTrading.getTradingPoundage()), 0);
        }

    }

    /**
     * 保存认缴流水
     * @param sgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaSgList sgList) throws Exception {
        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(sgList.getFaMember().getId());
        faCapitalLog.setMobile(sgList.getFaMember().getMobile());
        faCapitalLog.setName(sgList.getFaMember().getName());
        faCapitalLog.setSuperiorId(sgList.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(sgList.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(sgList.getFaMember().getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.SUBSCRIPTION));
        faCapitalLog.setMoney(sgList.getZqMoney());
        faCapitalLog.setBeforeMoney(sgList.getFaMember().getBalance());
        faCapitalLog.setLaterMoney(sgList.getFaMember().getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.SUBSCRIPTION);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockSymbol(sgList.getCode());
        faCapitalLog.setStockName(sgList.getName());
        faCapitalLog.setDeleteFlag(0);
        faCapitalLog.setOrderId(sgList.getId());
        this.save(faCapitalLog);

        // 更新用户余额 减少 认缴金额
        iFaMemberService.updateMemberBalance(sgList.getUserId(), sgList.getZqMoney(), 1);

        // 更新用户冻结余额 减少 认缴金额
        iFaMemberService.updateFaMemberFreezeProfit(sgList.getUserId(), sgList.getZqMoney(), 1);
    }

    /**
     * 保存配售中签认缴流水(申购=配售)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaStockSgSecond faStockSgSecond) throws Exception {
        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faStockSgSecond.getFaMember().getId());
        faCapitalLog.setMobile(faStockSgSecond.getFaMember().getMobile());
        faCapitalLog.setName(faStockSgSecond.getFaMember().getName());
        faCapitalLog.setSuperiorId(faStockSgSecond.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faStockSgSecond.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faStockSgSecond.getFaMember().getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.SUBSCRIPTION));
        faCapitalLog.setMoney(faStockSgSecond.getZqMoney());
        faCapitalLog.setBeforeMoney(faStockSgSecond.getFaMember().getBalance());
        faCapitalLog.setLaterMoney(faStockSgSecond.getFaMember().getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.SUBSCRIPTION);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockSymbol(faStockSgSecond.getCode());
        faCapitalLog.setStockName(faStockSgSecond.getName());
        faCapitalLog.setDeleteFlag(0);
        faCapitalLog.setOrderId(faStockSgSecond.getId());
        this.save(faCapitalLog);

        // 更新用户余额 减少 认缴金额
        iFaMemberService.updateMemberBalance(faStockSgSecond.getUserId(), faStockSgSecond.getZqMoney(), 1);

        // 更新用户冻结余额 减少 认缴金额
        iFaMemberService.updateFaMemberFreezeProfit(faStockSgSecond.getUserId(), faStockSgSecond.getZqMoney(), 1);
    }

    /**
     * 保存配售中签认缴流水
     * @param sgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaSgjiaoyi sgjiaoyi) throws Exception {
        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(sgjiaoyi.getFaMember().getId());
        faCapitalLog.setMobile(sgjiaoyi.getFaMember().getMobile());
        faCapitalLog.setName(sgjiaoyi.getFaMember().getName());
        faCapitalLog.setSuperiorId(sgjiaoyi.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(sgjiaoyi.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(sgjiaoyi.getFaMember().getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.SUBSCRIPTION));
        faCapitalLog.setMoney(sgjiaoyi.getZqMoney());
        faCapitalLog.setBeforeMoney(sgjiaoyi.getFaMember().getBalance());
        faCapitalLog.setLaterMoney(sgjiaoyi.getFaMember().getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.SUBSCRIPTION);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockSymbol(sgjiaoyi.getCode());
        faCapitalLog.setDeleteFlag(0);
        faCapitalLog.setOrderId(sgjiaoyi.getId());
        this.save(faCapitalLog);

        // 更新用户余额 减少 认缴金额
        iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), sgjiaoyi.getZqMoney(), 1);
    }

    /**
     * 充值资金流水
     * @param recharge
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaRecharge recharge) throws Exception {
        FaMember faMember = iFaMemberService.getById(recharge.getUserId());

        // 充值
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.RECHARGE));
        faCapitalLog.setMoney(recharge.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.RECHARGE);
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(recharge.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 增加 充值金额
        iFaMemberService.updateMemberBalance(recharge.getUserId(), recharge.getMoney(), 0);
    }

    /**
     * 提现资金流水
     * @param withdraw
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaWithdraw withdraw) throws Exception {
        FaMember faMember = iFaMemberService.getById(withdraw.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.WITHDRAW));
        faCapitalLog.setMoney(withdraw.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.WITHDRAW);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(withdraw.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 减少 提现金额
        iFaMemberService.updateMemberBalance(withdraw.getUserId(), withdraw.getMoney(), 1);
    }

    /**
     * 提交配售，资金转冻结
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Override
    public void savePeiShou(FaSgjiaoyi faSgjiaoyi) throws Exception {
        FaMember faMember = iFaMemberService.getById(faSgjiaoyi.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.PS_FREEZE));
        faCapitalLog.setMoney(faSgjiaoyi.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.PS_FREEZE);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockName(faSgjiaoyi.getName());
        faCapitalLog.setStockSymbol(faSgjiaoyi.getCode());
        faCapitalLog.setOrderId(faSgjiaoyi.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额
        iFaMemberService.updateMemberBalance(faSgjiaoyi.getUserId(), faCapitalLog.getMoney(), 1);

        // 更新用户冻结资金 减少
        iFaMemberService.updateFaMemberFreezeProfit(faSgjiaoyi.getUserId(), faCapitalLog.getMoney(), 1);
    }

    /**
     * 保存大宗提交流水
     * @param faTradeApprove
     * @return
     * @throws Exception
     */
    @Override
    public FaCapitalLog save(FaTradeApprove faTradeApprove) throws Exception {
        FaMember faMember = iFaMemberService.getById(faTradeApprove.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_FREEZE));
        faCapitalLog.setMoney(faTradeApprove.getShouldPayAmount());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.DZ_FREEZE);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockId(faTradeApprove.getStockId());
        faCapitalLog.setStockName(faTradeApprove.getStockName());
        faCapitalLog.setStockSymbol(faTradeApprove.getStockSymbol());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 减少 应缴
        iFaMemberService.updateMemberBalance(faMember.getId(), faTradeApprove.getShouldPayAmount(), 1);

        // 更新用户冻结 减少 应缴
        iFaMemberService.updateFaMemberFreezeProfit(faMember.getId(), faTradeApprove.getShouldPayAmount(), 1);

        return faCapitalLog;
    }

}