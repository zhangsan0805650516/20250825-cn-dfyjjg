package com.ruoyi.biz.stockHoldDetail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgList.service.IFaSgListService;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.mapper.FaStockHoldDetailMapper;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 持仓明细Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Service
public class FaStockHoldDetailServiceImpl extends ServiceImpl<FaStockHoldDetailMapper, FaStockHoldDetail> implements IFaStockHoldDetailService
{
    @Autowired
    private FaStockHoldDetailMapper faStockHoldDetailMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaShengouService iFaShengouService;

    @Autowired
    private IFaSgListService iFaSgListService;

    @Autowired
    private IFaSgjiaoyiService iFaSgjiaoyiService;

    /**
     * 查询持仓明细
     *
     * @param id 持仓明细主键
     * @return 持仓明细
     */
    @Override
    public FaStockHoldDetail selectFaStockHoldDetailById(Integer id)
    {
        return faStockHoldDetailMapper.selectFaStockHoldDetailById(id);
    }

    /**
     * 查询持仓明细列表
     *
     * @param faStockHoldDetail 持仓明细
     * @return 持仓明细
     */
    @Override
    public List<FaStockHoldDetail> selectFaStockHoldDetailList(FaStockHoldDetail faStockHoldDetail)
    {
        try {
            faStockHoldDetail.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faStockHoldDetail.getMobile())) {
                faStockHoldDetail.setSalt(AESUtil.encrypt(faStockHoldDetail.getMobile()));
                faStockHoldDetail.setMobile(null);
            }

            List<FaStockHoldDetail> list = faStockHoldDetailMapper.selectFaStockHoldDetailList(faStockHoldDetail);
            if (!list.isEmpty()) {
                for (FaStockHoldDetail holdDetail : list) {
                    if (null != holdDetail.getStockId()) {
                        FaStrategy faStrategy = iFaStrategyService.selectFaStrategyById(holdDetail.getStockId());
                        if (ObjectUtils.isNotEmpty(faStrategy)) {
                            holdDetail.setCurrentPrice(faStrategy.getCaiTrade());
                        }
                    } else if (null != holdDetail.getNewStockId()) {
                        FaNewStock faNewStock = iFaShengouService.getById(holdDetail.getNewStockId());
                        if (ObjectUtils.isNotEmpty(faNewStock)) {
                            holdDetail.setCurrentPrice(holdDetail.getBuyPrice());
                        }
                    }

                    // 持仓中，取股票数据，计算持仓信息
                    if (holdDetail.getStatus() == 0) {
                        holdDetail.setProfitLose(holdDetail.getCurrentPrice().subtract(holdDetail.getAverage()).multiply(new BigDecimal(holdDetail.getHoldNumber())));
                        holdDetail.setProfitRate(holdDetail.getCurrentPrice().subtract(holdDetail.getAverage()).divide(holdDetail.getAverage(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    }
                    // 历史持仓，计算持仓信息
                    else if (holdDetail.getStatus() == 1) {
                        holdDetail.setAverage(holdDetail.getBuyPrice());
                        holdDetail.setHoldNumber(holdDetail.getTradingNumber());

                        holdDetail.setBuyPrice(holdDetail.getBuyPrice());
                        holdDetail.setBuyFee(holdDetail.getBuyPoundage());
                        holdDetail.setBuyTime(holdDetail.getBuyTime());

                        holdDetail.setSellPrice(holdDetail.getSellPrice());
                        holdDetail.setSellFee(holdDetail.getSellPoundage());
                        holdDetail.setStampDuty(holdDetail.getSellStampDuty());
                        holdDetail.setSellTime(holdDetail.getSellTime());

                        holdDetail.setFee(holdDetail.getBuyPoundage().add(holdDetail.getSellPoundage()).add(holdDetail.getSellStampDuty()));

                        // 计算涨跌幅
                        holdDetail.setProfitRate(holdDetail.getSellPrice().subtract(holdDetail.getBuyPrice()).divide(holdDetail.getBuyPrice(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    }

                    //本金
                    holdDetail.setPrincipal(holdDetail.getBuyPrice().multiply(new BigDecimal(holdDetail.getTradingNumber())));

                    // 融券方向相反
                    if (8 == holdDetail.getHoldType()) {
                        holdDetail.setProfitLose(holdDetail.getProfitLose().negate());
                        holdDetail.setProfitRate(holdDetail.getProfitRate().negate());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增持仓明细
     *
     * @param faStockHoldDetail 持仓明细
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail) throws Exception
    {
        if (null == faStockHoldDetail.getCreateTime()) {
            faStockHoldDetail.setCreateTime(new Date());
        }
        // 参数判断
        if (null == faStockHoldDetail.getMemberId() || null == faStockHoldDetail.getStockId() ||
                null == faStockHoldDetail.getHoldNumber() || null == faStockHoldDetail.getAverage() ||
                null == faStockHoldDetail.getHoldType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 手数
        faStockHoldDetail.setHoldHand(faStockHoldDetail.getHoldNumber() / 100);

        // 用户
        FaMember faMember = iFaMemberService.selectFaMemberById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }
        // 股票
        FaStrategy faStrategy = iFaStrategyService.selectFaStrategyById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setMemberId(faStockHoldDetail.getMemberId());
        faStockTrading.setStockId(faStockHoldDetail.getStockId());
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());
        // 普通交易
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交价格
        faStockTrading.setTradingPrice(faStockHoldDetail.getAverage());
        // 成交金额
        faStockTrading.setTradingAmount(faStockTrading.getTradingPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())));

        String maiFee = "";
        // 大宗买入手续费
        if (2 == faStockHoldDetail.getHoldType()) {
            maiFee = iFaRiskConfigService.getConfigValue("zfssfee", "0.0005");
        }
        // 其他走普通买入手续费
        else {
            maiFee = iFaRiskConfigService.getConfigValue("mai_fee", "0.0001");
        }

        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(faStockHoldDetail.getCreateTime());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 融券方向2
        if (8 == faStockHoldDetail.getHoldType()) {
            faStockHoldDetail.setTradeDirect(2);
            faStockTrading.setTradeDirect(2);
        }

        // 新增持仓
        faStockHoldDetail = addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        iFaStockTradingService.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
        return 1;
    }

    /**
     * 修改持仓明细
     *
     * @param faStockHoldDetail 持仓明细
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaStockHoldDetail(FaStockHoldDetail faStockHoldDetail)
    {
        faStockHoldDetail.setUpdateTime(DateUtils.getNowDate());
        return faStockHoldDetailMapper.updateFaStockHoldDetail(faStockHoldDetail);
    }

    /**
     * 批量删除持仓明细
     *
     * @param ids 需要删除的持仓明细主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStockHoldDetailByIds(Integer[] ids)
    {
        return faStockHoldDetailMapper.deleteFaStockHoldDetailByIds(ids);
    }

    /**
     * 删除持仓明细信息
     *
     * @param id 持仓明细主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStockHoldDetailById(Integer id)
    {
        return faStockHoldDetailMapper.deleteFaStockHoldDetailById(id);
    }

    /**
     * 持仓明细剩余冻结天数-1
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateFreezeDaysLeft() throws Exception {
        faStockHoldDetailMapper.updateFreezeDaysLeft();
    }

    /**
     * 持仓明细冻结状态判断
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateFreezeStatus() throws Exception {
        faStockHoldDetailMapper.updateFreezeStatus();
    }

    /**
     * 扣减持仓
     * @param faStockTrading
     * @param type 0 强平 1 卖出
     * @param currentStatus 当天平仓
     * @throws Exception
     */
    @Transactional
    @Override
    public FaStockHoldDetail subtractStockHoldDetail(FaStockTrading faStockTrading, Integer type, Integer currentStatus) throws Exception {
        FaStockHoldDetail faStockHoldDetail = this.getById(faStockTrading.getHoldId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 强平
        if (type == 0) {
            faStockHoldDetail.setFreezeNumber(0);
            faStockHoldDetail.setFreezeDaysLeft(0);
            faStockHoldDetail.setFreezeStatus(1);
        }
        // 普通卖出
        else if (type == 1) {
            // 可以当日平仓
            if (1 == currentStatus) {
                // 强平
                faStockHoldDetail.setFreezeNumber(0);
                faStockHoldDetail.setFreezeDaysLeft(0);
                faStockHoldDetail.setFreezeStatus(1);
            }
            // 不能当日平仓
            else if (0 == currentStatus) {
                // 是否锁仓
                if (1 == faStockHoldDetail.getIsLock()) {
                    throw new ServiceException(MessageUtils.message("member.hold.lock"), HttpStatus.ERROR);
                }

                // 扣减持仓
                if (faStockHoldDetail.getHoldNumber() - faStockHoldDetail.getFreezeNumber() < faStockTrading.getTradingNumber()) {
                    throw new ServiceException("用户持仓T+" + faStockHoldDetail.getFreezeDaysLeft() + "锁定", HttpStatus.ERROR);
                }
            }
        }

        // 循环扣除持仓，持仓扣完计算盈亏，没扣完计算均价
        int totalSellNum = faStockTrading.getTradingNumber();
        // 初始持仓
        int oldHoldDetail = faStockHoldDetail.getHoldNumber();
        // 交易数量
        int tradeHoldNum = 0;
        // 不够扣除，扣完计算盈亏，更新状态，继续扣下一个
        if (totalSellNum > faStockHoldDetail.getHoldNumber() - faStockHoldDetail.getFreezeNumber()) {
            tradeHoldNum = faStockHoldDetail.getHoldNumber() - faStockHoldDetail.getFreezeNumber();
            faStockHoldDetail.setHoldNumber(faStockHoldDetail.getFreezeNumber());
        }
        // 够扣，计算盈亏，更新状态，循环结束
        else {
            tradeHoldNum = totalSellNum;
            faStockHoldDetail.setHoldNumber(faStockHoldDetail.getHoldNumber() - tradeHoldNum);
        }

        // 清空，计算盈亏
        if (0 == faStockHoldDetail.getHoldNumber()) {
            faStockHoldDetail.setStatus(1);

            // 计算盈亏 不包含手续费
            if (null == faStockTrading.getProfitLose()) {
                BigDecimal profitLose = faStockTrading.getTradingPrice().subtract(faStockHoldDetail.getAverage()).multiply(new BigDecimal(oldHoldDetail));
                faStockHoldDetail.setProfitLose(profitLose);
            } else {
                // 盈亏
                faStockHoldDetail.setProfitLose(faStockTrading.getProfitLose());
            }

            faStockHoldDetail.setAverage(BigDecimal.ZERO);
        }
        // 未清空，计算均价
        else {
            // 交易金额
            BigDecimal tradeAmount = new BigDecimal(tradeHoldNum).multiply(faStockTrading.getTradingPrice());
            // 计算均价
            faStockHoldDetail.setAverage(faStockHoldDetail.getAverage()
                    .multiply(new BigDecimal(oldHoldDetail))
                    .subtract(tradeAmount)
                    .divide(new BigDecimal(faStockHoldDetail.getHoldNumber()), 2, RoundingMode.HALF_UP));
        }
        faStockHoldDetail.setUpdateTime(new Date());

        // 卖出价，卖出时间
        faStockHoldDetail.setSellPrice(faStockTrading.getTradingPrice());
        faStockHoldDetail.setSellPoundage(faStockTrading.getTradingPoundage());
        faStockHoldDetail.setSellStampDuty(faStockTrading.getStampDuty());
        faStockHoldDetail.setSellTime(faStockTrading.getCreateTime());

        this.updateFaStockHoldDetail(faStockHoldDetail);
        return faStockHoldDetail;
    }

    /**
     * 新增持仓
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public FaStockHoldDetail addStockHoldDetail(FaStockTrading faStockTrading) throws Exception {
        // 新增持仓明细
        FaStockHoldDetail faStockHoldDetail = new FaStockHoldDetail();
//        faStockHoldDetail.setHoldId(faStockHold.getId());
        faStockHoldDetail.setMemberId(faStockTrading.getFaMember().getId());
        faStockHoldDetail.setPid(faStockTrading.getFaMember().getSuperiorId());
        faStockHoldDetail.setPidCode(faStockTrading.getFaMember().getSuperiorCode());
        faStockHoldDetail.setPidName(faStockTrading.getFaMember().getSuperiorName());
        faStockHoldDetail.setStockId(faStockTrading.getFaStrategy().getId());
        faStockHoldDetail.setStockName(faStockTrading.getFaStrategy().getTitle());
        faStockHoldDetail.setStockSymbol(faStockTrading.getFaStrategy().getCode());
        faStockHoldDetail.setAllCode(faStockTrading.getFaStrategy().getAllCode());
        faStockHoldDetail.setStockType(faStockTrading.getFaStrategy().getType());
        faStockHoldDetail.setHoldHand(faStockTrading.getTradingHand());
        faStockHoldDetail.setHoldNumber(faStockTrading.getTradingNumber());
        // 持仓类型(1普通交易 2大宗交易 3VIP调研 4指数交易 5期货交易 6基金 7增发 8融券 9预约重组)
        faStockHoldDetail.setHoldType(faStockTrading.getHoldType());
        // 买入价
        faStockHoldDetail.setAverage(faStockTrading.getTradingPrice());
        faStockHoldDetail.setResourceType(0);

        if (null != faStockTrading.getCreateTime()) {
            faStockHoldDetail.setCreateTime(faStockTrading.getCreateTime());
        } else {
            faStockHoldDetail.setCreateTime(new Date());
        }
        faStockHoldDetail.setStatus(0);
        faStockHoldDetail.setDeleteFlag(0);

        // 融券T+N天数
        if (8 == faStockTrading.getHoldType()) {
            if (faStockTrading.getFaStrategy().getBorrowDays() > 0) {
                faStockHoldDetail.setFreezeNumber(faStockTrading.getTradingNumber());
                faStockHoldDetail.setFreezeDaysLeft(faStockTrading.getFaStrategy().getBorrowDays());
                faStockHoldDetail.setFreezeStatus(0);
            } else {
                faStockHoldDetail.setFreezeNumber(0);
                faStockHoldDetail.setFreezeDaysLeft(0);
                faStockHoldDetail.setFreezeStatus(1);
            }
        } else {
            //根据股票T+N判断持仓冻结时间
            if (faStockTrading.getFaStrategy().getPingDay() > 0) {
                faStockHoldDetail.setFreezeNumber(faStockTrading.getTradingNumber());
                faStockHoldDetail.setFreezeDaysLeft(faStockTrading.getFaStrategy().getPingDay());
                faStockHoldDetail.setFreezeStatus(0);
            } else {
                faStockHoldDetail.setFreezeNumber(0);
                faStockHoldDetail.setFreezeDaysLeft(0);
                faStockHoldDetail.setFreezeStatus(1);
            }
        }

        // 买入价，买入时间
        faStockHoldDetail.setBuyPrice(faStockTrading.getTradingPrice());
        faStockHoldDetail.setBuyPoundage(faStockTrading.getTradingPoundage());
        faStockHoldDetail.setBuyTime(faStockTrading.getCreateTime());
        faStockHoldDetail.setTradingHand(faStockTrading.getTradingHand());
        faStockHoldDetail.setTradingNumber(faStockTrading.getTradingNumber());

        // 方向(1买涨 2买跌)
        faStockHoldDetail.setTradeDirect(faStockTrading.getTradeDirect());

        this.save(faStockHoldDetail);
        return faStockHoldDetail;
    }

    /**
     * 查询持仓列表
     * @param faStockHoldDetail
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStockHoldDetail> getStockHoldDetailList(FaStockHoldDetail faStockHoldDetail) throws Exception {
        LambdaQueryWrapper<FaStockHoldDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStockHoldDetail::getMemberId, faStockHoldDetail.getMemberId());
        if (null != faStockHoldDetail.getStockId()) {
            lambdaQueryWrapper.eq(FaStockHoldDetail::getStockId, faStockHoldDetail.getStockId());
        }
        if (StringUtils.isNotEmpty(faStockHoldDetail.getStockSymbol())) {
            lambdaQueryWrapper.eq(FaStockHoldDetail::getStockSymbol, faStockHoldDetail.getStockSymbol());
        }
        if (null != faStockHoldDetail.getHoldType()) {
            lambdaQueryWrapper.eq(FaStockHoldDetail::getHoldType, faStockHoldDetail.getHoldType());
        }

        // 当前持仓不包含融券,融券持仓包含融券,历史持仓包含融券
        if (0 == faStockHoldDetail.getStatus() && (null == faStockHoldDetail.getHoldType() || 8 != faStockHoldDetail.getHoldType())) {
            lambdaQueryWrapper.ne(FaStockHoldDetail::getHoldType, 8);
        }

        lambdaQueryWrapper.eq(FaStockHoldDetail::getStatus, faStockHoldDetail.getStatus());
        lambdaQueryWrapper.eq(FaStockHoldDetail::getDeleteFlag, 0);

        //起始时间
        if (StringUtils.isNotEmpty(faStockHoldDetail.getStartDate())) {
            lambdaQueryWrapper.ge(FaStockHoldDetail::getSellTime, faStockHoldDetail.getStartDate() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(faStockHoldDetail.getEndDate())) {
            lambdaQueryWrapper.le(FaStockHoldDetail::getSellTime, faStockHoldDetail.getEndDate() + " 23:59:59");
        }

        lambdaQueryWrapper.orderByDesc(FaStockHoldDetail::getSellTime);
        lambdaQueryWrapper.orderByDesc(FaStockHoldDetail::getCreateTime);
        IPage<FaStockHoldDetail> faStockHoldDetailPage = this.page(new Page<>(faStockHoldDetail.getPage(), faStockHoldDetail.getSize()), lambdaQueryWrapper);

        if (!faStockHoldDetailPage.getRecords().isEmpty()) {
            for (FaStockHoldDetail holdDetail : faStockHoldDetailPage.getRecords()) {
                holdDetail.setBuyPrice(holdDetail.getBuyPrice());
                holdDetail.setBuyTime(holdDetail.getBuyTime());
                holdDetail.setHoldNumber(holdDetail.getTradingNumber());
                holdDetail.setBuyFee(holdDetail.getBuyPoundage());

                // 持仓中 计算盈亏
                if (holdDetail.getStatus() == 0) {
                    // 上市股票
                    if (1 == holdDetail.getIsList()) {
                        FaStrategy faStrategy = iFaStrategyService.selectFaStrategyById(holdDetail.getStockId());
                        if (ObjectUtils.isNotEmpty(faStrategy)) {
                            holdDetail.setCurrentPrice(faStrategy.getCaiTrade());

                            // 计算盈亏 不包含手续费
                            BigDecimal profitLose = holdDetail.getCurrentPrice().subtract(holdDetail.getBuyPrice()).multiply(new BigDecimal(holdDetail.getHoldNumber()));
                            // 买涨,涨了增加
                            if (1 == holdDetail.getTradeDirect()) {
                                profitLose = profitLose;
                            }
                            // 买跌，跌了增加
                            else if (2 == holdDetail.getTradeDirect()) {
                                profitLose = profitLose.multiply(new BigDecimal(-1));
                            }
                            // 普通股票
                            else {
                                profitLose = holdDetail.getCurrentPrice().subtract(holdDetail.getBuyPrice()).multiply(new BigDecimal(holdDetail.getHoldNumber()));
                            }
                            holdDetail.setProfitLose(profitLose);
                            holdDetail.setProfitRate(profitLose.divide(holdDetail.getBuyPrice().multiply(new BigDecimal(holdDetail.getTradingNumber())), 4, RoundingMode.HALF_UP));
                        }
                    }
                    // 未上市
                    else if (0 == holdDetail.getIsList()) {
                        FaNewStock faNewStock = iFaShengouService.getById(holdDetail.getNewStockId());
                        if (ObjectUtils.isNotEmpty(faNewStock)) {
                            holdDetail.setCurrentPrice(new BigDecimal(faNewStock.getFxPrice()));
                        }
                    }
                }
                // 历史记录 查询卖出 交易价格和交易数量
                else if (holdDetail.getStatus() == 1) {
                    holdDetail.setSellPrice(holdDetail.getSellPrice());
                    holdDetail.setSellTime(holdDetail.getSellTime());

                    // 卖出手续费
                    holdDetail.setSellFee(holdDetail.getSellPoundage());
                    // 印花税
                    holdDetail.setStampDuty(holdDetail.getSellStampDuty());
                    // 手续费汇总
                    holdDetail.setFee(holdDetail.getBuyFee().add(holdDetail.getSellFee()).add(holdDetail.getStampDuty()));
                    // 计算涨跌幅
                    holdDetail.setProfitRate(holdDetail.getProfitLose().divide(holdDetail.getBuyPrice().multiply(new BigDecimal(holdDetail.getTradingNumber())), 4, RoundingMode.HALF_UP));
                }
            }
        }

        return faStockHoldDetailPage;
    }

    /**
     * 修改锁仓状态
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Override
    public void changeLockStatus(FaStockHoldDetail faStockHoldDetail) throws Exception {
        if (null == faStockHoldDetail.getId() || null == faStockHoldDetail.getIsLock()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaStockHoldDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockHoldDetail::getId, faStockHoldDetail.getId());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getIsLock, faStockHoldDetail.getIsLock());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 新股申购转持仓
     * @param faNewStock 新股
     * @param faStrategy 正股
     * @throws Exception
     */
    @Override
    public void addFromNewStock(FaNewStock faNewStock, FaStrategy faStrategy) throws Exception {
        // 申购转持仓
        addFromNewStockSg(faNewStock, faStrategy);
        // 配售转持仓
        addFromNewStockPs(faStrategy, faNewStock);
        // 未上市转持仓的状态更新
        updateHoldDetail(faNewStock, faStrategy);
    }

    /**
     * 申购转持仓
     * @param faNewStock
     * @throws Exception
     */
    private void addFromNewStockSg(FaNewStock faNewStock, FaStrategy faStrategy) throws Exception {
        // 申购列表
        LambdaQueryWrapper<FaSgList> faSgListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        faSgListLambdaQueryWrapper.eq(FaSgList::getShengouId, faNewStock.getId());
        faSgListLambdaQueryWrapper.eq(FaSgList::getIsCc, 0);
        faSgListLambdaQueryWrapper.eq(FaSgList::getDeleteFlag, 0);
        List<FaSgList> faSgListList = iFaSgListService.list(faSgListLambdaQueryWrapper);
        transToHold(faSgListList, faStrategy);
    }

    /**
     * 申购列表转持仓
     * @param faSgListList
     * @throws Exception
     */
    private void transToHold(List<FaSgList> faSgListList, FaStrategy faStrategy) throws Exception {
        if (!faSgListList.isEmpty()) {
            for (FaSgList faSgList : faSgListList) {
                // 用户信息
                FaMember faMember = iFaMemberService.getById(faSgList.getUserId());
                if (ObjectUtils.isEmpty(faMember)) {
                    throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
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
                faStockHoldDetail.setHoldNumber(faSgList.getZqNums());
                faStockHoldDetail.setAverage(faSgList.getSgFxPrice());
                // 新股转
                faStockHoldDetail.setResourceType(1);
                faStockHoldDetail.setCreateTime(new Date());
                faStockHoldDetail.setStatus(0);
                faStockHoldDetail.setDeleteFlag(0);
                faStockHoldDetail.setFreezeNumber(0);
                faStockHoldDetail.setFreezeDaysLeft(0);
                faStockHoldDetail.setFreezeStatus(1);
                this.save(faStockHoldDetail);

                // 更新申购状态
                faSgList.setIsCc(1);
                faSgList.setIsCcTime(new Date());
                faSgList.setUpdateTime(new Date());
                iFaSgListService.updateFaSgList(faSgList);
            }
        }
    }

    /**
     * 配售转持仓
     * @param faNewStock
     * @throws Exception
     */
    private void addFromNewStockPs(FaStrategy faStrategy, FaNewStock faNewStock) throws Exception {
        // 申购列表
        LambdaQueryWrapper<FaSgjiaoyi> faSgjiaoyiLambdaQueryWrapper = new LambdaQueryWrapper<>();
        faSgjiaoyiLambdaQueryWrapper.eq(FaSgjiaoyi::getShengouId, faNewStock.getId());
        faSgjiaoyiLambdaQueryWrapper.eq(FaSgjiaoyi::getIsCc, 0);
        faSgjiaoyiLambdaQueryWrapper.eq(FaSgjiaoyi::getDeleteFlag, 0);
        List<FaSgjiaoyi> faSgjiaoyiList = iFaSgjiaoyiService.list(faSgjiaoyiLambdaQueryWrapper);
        transToHold(faStrategy, faSgjiaoyiList);
    }

    /**
     * 配售列表转持仓
     * @param faSgjiaoyiList
     * @throws Exception
     */
    private void transToHold(FaStrategy faStrategy, List<FaSgjiaoyi> faSgjiaoyiList) throws Exception {
        if (!faSgjiaoyiList.isEmpty()) {
            for (FaSgjiaoyi faSgjiaoyi : faSgjiaoyiList) {
                // 用户信息
                FaMember faMember = iFaMemberService.getById(faSgjiaoyi.getUserId());
                if (ObjectUtils.isEmpty(faMember)) {
                    throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
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
                faStockHoldDetail.setHoldNumber(faSgjiaoyi.getZqNums());
                faStockHoldDetail.setAverage(faSgjiaoyi.getSgFxPrice());
                // 新股转
                faStockHoldDetail.setResourceType(1);
                faStockHoldDetail.setCreateTime(new Date());
                faStockHoldDetail.setStatus(0);
                faStockHoldDetail.setDeleteFlag(0);
                faStockHoldDetail.setFreezeNumber(0);
                faStockHoldDetail.setFreezeDaysLeft(0);
                faStockHoldDetail.setFreezeStatus(1);
                this.save(faStockHoldDetail);

                // 更新申购状态
                faSgjiaoyi.setIsCc(1);
                faSgjiaoyi.setIsCcTime(new Date());
                faSgjiaoyi.setUpdateTime(new Date());
                iFaSgjiaoyiService.updateFaSgjiaoyi(faSgjiaoyi);
            }
        }
    }

    /**
     * 未上市转持仓的状态更新
     * @param faNewStock
     * @throws Exception
     */
    private void updateHoldDetail(FaNewStock faNewStock, FaStrategy faStrategy) throws Exception {
        LambdaUpdateWrapper<FaStockHoldDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockHoldDetail::getNewStockId, faNewStock.getId());
        lambdaUpdateWrapper.eq(FaStockHoldDetail::getIsList, 0);
        lambdaUpdateWrapper.set(FaStockHoldDetail::getStockId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getStockName, faStrategy.getTitle());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getStockSymbol, faStrategy.getCode());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getAllCode, faStrategy.getAllCode());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getStockType, faStrategy.getType());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getIsList, 1);
        lambdaUpdateWrapper.set(FaStockHoldDetail::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * T+1冻结持仓清零
     * @throws Exception
     */
    @Override
    public void clearT1Hold() throws Exception {
        // 持仓明细剩余冻结天数-1
        updateFreezeDaysLeft();
        // 持仓明细冻结状态判断
        updateFreezeStatus();
    }

    /**
     * 调整T+N
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Override
    public void changeTN(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 取出数据，判断是否持仓状态
        FaStockHoldDetail selectOne = this.getById(faStockHoldDetail.getId());
        if (1 == selectOne.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.hold.already.sell"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaStockHoldDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockHoldDetail::getId, faStockHoldDetail.getId());
        lambdaUpdateWrapper.set(FaStockHoldDetail::getFreezeDaysLeft, faStockHoldDetail.getFreezeDaysLeft());
        // 调整为0，解冻
        if (0 == faStockHoldDetail.getFreezeDaysLeft()) {
            // 解冻
            lambdaUpdateWrapper.set(FaStockHoldDetail::getFreezeStatus, 1);
            lambdaUpdateWrapper.set(FaStockHoldDetail::getFreezeNumber, 0);
        }
        // 大于0，冻结
        else if (faStockHoldDetail.getFreezeDaysLeft() > 0) {
            // 冻结
            lambdaUpdateWrapper.set(FaStockHoldDetail::getFreezeStatus, 0);
            lambdaUpdateWrapper.set(FaStockHoldDetail::getFreezeNumber, selectOne.getHoldNumber());
        }
        lambdaUpdateWrapper.set(FaStockHoldDetail::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 查询大宗持仓统计
     * @param faStockHoldDetail
     * @return
     */
    @Override
    public Map<String, BigDecimal> getDzHoldStatistics(FaStockHoldDetail faStockHoldDetail) throws Exception {
        Map<String, BigDecimal> map = new HashMap<>();

        faStockHoldDetail = faStockHoldDetailMapper.getDzHoldStatistics(faStockHoldDetail);

        map.put("dzMarketValue", faStockHoldDetail.getDzMarketValue());
        map.put("dzProfitLose", faStockHoldDetail.getProfitLose());
        return map;
    }

}