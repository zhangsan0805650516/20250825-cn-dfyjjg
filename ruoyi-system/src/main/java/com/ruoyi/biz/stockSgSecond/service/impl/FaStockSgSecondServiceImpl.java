package com.ruoyi.biz.stockSgSecond.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.mapper.FaShengouMapper;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockSgSecond.mapper.FaStockSgSecondMapper;
import com.ruoyi.biz.stockSgSecond.service.IFaStockSgSecondService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.userNotice.service.IFaUserNoticeService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 线下配售(世纪独享)Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-01
 */
@Service
public class FaStockSgSecondServiceImpl extends ServiceImpl<FaStockSgSecondMapper, FaStockSgSecond> implements IFaStockSgSecondService
{
    @Autowired
    private FaStockSgSecondMapper faStockSgSecondMapper;

    @Autowired
    private IFaUserNoticeService iFaUserNoticeService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private FaShengouMapper faShengouMapper;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaShengouService iFaShengouService;

    /**
     * 查询线下配售(世纪独享)
     *
     * @param id 线下配售(世纪独享)主键
     * @return 线下配售(世纪独享)
     */
    @Override
    public FaStockSgSecond selectFaStockSgSecondById(Long id)
    {
        return faStockSgSecondMapper.selectFaStockSgSecondById(id);
    }

    /**
     * 查询线下配售(世纪独享)列表
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 线下配售(世纪独享)
     */
    @Override
    public List<FaStockSgSecond> selectFaStockSgSecondList(FaStockSgSecond faStockSgSecond)
    {
        try {
            faStockSgSecond.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faStockSgSecond.getMobile())) {
                faStockSgSecond.setSalt(AESUtil.encrypt(faStockSgSecond.getMobile()));
                faStockSgSecond.setMobile(null);
            }

            return faStockSgSecondMapper.selectFaStockSgSecondList(faStockSgSecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    @Override
    public int insertFaStockSgSecond(FaStockSgSecond faStockSgSecond) throws Exception
    {
        if (null == faStockSgSecond.getCreateTime()) {
            faStockSgSecond.setCreateTime(DateUtils.getNowDate());
        }

        FaNewStock faNewStock = new FaNewStock();
        faNewStock.setId(faStockSgSecond.getShengouId());
        faNewStock.setMemberId(faStockSgSecond.getUserId());
        faNewStock.setSgNums(faStockSgSecond.getSgNum());
        faNewStock.setCreateTime(faStockSgSecond.getCreateTime());

        // 保证金模式，先扣
        iFaShengouService.addSgSecond(faNewStock);
        return 1;
    }

    /**
     * 修改线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    @Override
    public int updateFaStockSgSecond(FaStockSgSecond faStockSgSecond)
    {
        faStockSgSecond.setUpdateTime(DateUtils.getNowDate());
        return faStockSgSecondMapper.updateFaStockSgSecond(faStockSgSecond);
    }

    /**
     * 批量删除线下配售(世纪独享)
     *
     * @param ids 需要删除的线下配售(世纪独享)主键
     * @return 结果
     */
    @Override
    public int deleteFaStockSgSecondByIds(Long[] ids)
    {
        return faStockSgSecondMapper.deleteFaStockSgSecondByIds(ids);
    }

    /**
     * 删除线下配售(世纪独享)信息
     *
     * @param id 线下配售(世纪独享)主键
     * @return 结果
     */
    @Override
    public int deleteFaStockSgSecondById(Long id)
    {
        return faStockSgSecondMapper.deleteFaStockSgSecondById(id);
    }

    /**
     * 查询用户申购列表
     * @param faStockSgSecond
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStockSgSecond> getMemberStockSgSecondPage(FaStockSgSecond faStockSgSecond) throws Exception {
        LambdaQueryWrapper<FaStockSgSecond> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStockSgSecond::getUserId, faStockSgSecond.getUserId());
        if (null != faStockSgSecond.getStatus()) {
            lambdaQueryWrapper.eq(FaStockSgSecond::getStatus, faStockSgSecond.getStatus());
        } else {
            lambdaQueryWrapper.in(FaStockSgSecond::getStatus, 0, 1, 2);
        }

        if (null != faStockSgSecond.getSgType()) {
            lambdaQueryWrapper.eq(FaStockSgSecond::getSgType, faStockSgSecond.getSgType());
        }

        if (null != faStockSgSecond.getIsCc()) {
            lambdaQueryWrapper.eq(FaStockSgSecond::getIsCc, faStockSgSecond.getIsCc());
        }

        lambdaQueryWrapper.eq(FaStockSgSecond::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStockSgSecond::getCreateTime);
        IPage<FaStockSgSecond> faStockSgSecondIPage = this.page(new Page<>(faStockSgSecond.getPage(), faStockSgSecond.getSize()), lambdaQueryWrapper);

        for (FaStockSgSecond stockSgSecond : faStockSgSecondIPage.getRecords()) {
            FaNewStock faNewStock = iFaShengouService.getById(stockSgSecond.getShengouId());
            if (ObjectUtils.isNotEmpty(faNewStock) && null != faNewStock.getFxRate()) {
                stockSgSecond.setFxRate(new BigDecimal(faNewStock.getFxRate()));
            }
        }

        return faStockSgSecondIPage;
    }

    /**
     * 提交中签(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Override
    public void submitAllocation(FaStockSgSecond faStockSgSecond) throws Exception {
        LambdaQueryWrapper<FaStockSgSecond> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStockSgSecond::getId, faStockSgSecond.getId());
        lambdaQueryWrapper.eq(FaStockSgSecond::getDeleteFlag, 0);
        // 数据库状态
        FaStockSgSecond stockSgSecond = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(stockSgSecond)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 已转未中签或弃购，禁止操作
        if (stockSgSecond.getStatus() == 2 || stockSgSecond.getStatus() == 3) {
            throw new ServiceException(MessageUtils.message("subscribe.operated"), HttpStatus.ERROR);
        }

        // 已转持仓，禁止操作
        if (stockSgSecond.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        // 重复操作
        if (stockSgSecond.getStatus().equals(faStockSgSecond.getStatus())) {
            throw new ServiceException(MessageUtils.message("repeat.operate"), HttpStatus.ERROR);
        }

        // 已认缴的 --》 申购中/未中签/弃购
        if (stockSgSecond.getRenjiao() == 1) {
            if(faStockSgSecond.getStatus() == 0 || faStockSgSecond.getStatus() == 2 || faStockSgSecond.getStatus() == 3) {
                // 查询绑定的资金流水
                LambdaQueryWrapper<FaCapitalLog> capitalLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getUserId, stockSgSecond.getUserId());
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getOrderId, stockSgSecond.getId());
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getType, 6);
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
                FaCapitalLog faCapitalLog = iFaCapitalLogService.getOne(capitalLogLambdaQueryWrapper);
                if (ObjectUtils.isEmpty(faCapitalLog)) {
                    throw new ServiceException(MessageUtils.message("capital.log.error"), HttpStatus.ERROR);
                }

                // 更新流水删除标记
                LambdaUpdateWrapper<FaCapitalLog> capitalLogLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                capitalLogLambdaUpdateWrapper.eq(FaCapitalLog::getId, faCapitalLog.getId());
                capitalLogLambdaUpdateWrapper.set(FaCapitalLog::getDeleteFlag, 1);
                capitalLogLambdaUpdateWrapper.set(FaCapitalLog::getUpdateTime, new Date());
                iFaCapitalLogService.update(capitalLogLambdaUpdateWrapper);

                // 更新用户余额
                iFaMemberService.updateMemberBalance(faCapitalLog.getUserId(), faCapitalLog.getMoney(), 0);

                // 冻结退回
                iFaMemberService.updateFaMemberFreezeProfit(faCapitalLog.getUserId(), faCapitalLog.getMoney(), 1);

                // 更新申购状态
                faStockSgSecond.setRenjiao(0);
                faStockSgSecond.setZqNum(0);
                faStockSgSecond.setZqNums(0);
                faStockSgSecond.setZqMoney(BigDecimal.ZERO);
                faStockSgSecond.setUpdateTime(new Date());
                this.updateFaStockSgSecond(faStockSgSecond);
            }
        }
        // 未认缴的
        else if (stockSgSecond.getRenjiao() == 0) {
            // 申购中转中签
            if (stockSgSecond.getStatus() == 0 && faStockSgSecond.getStatus() == 1) {
                faStockSgSecond.setUpdateTime(new Date());
                // 手->股转换 根据风控设置来，默认股(0股1签)
                String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
                // 按股计算 提交过来的是zqNum
                if ("0".equals(gqpeizhi)) {
                    faStockSgSecond.setZqNums(faStockSgSecond.getZqNum() * 100);
                    faStockSgSecond.setZqMoney(faStockSgSecond.getSgFxPrice().multiply(new BigDecimal(faStockSgSecond.getZqNums())));
                }
                // 按签计算 提交过来的是zqNum
                else if ("1".equals(gqpeizhi)) {
                    // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                    if (1 == stockSgSecond.getSgType()) {
                        faStockSgSecond.setZqNums(faStockSgSecond.getZqNum() * 1000);
                    } else {
                        faStockSgSecond.setZqNums(faStockSgSecond.getZqNum() * 500);
                    }
                    faStockSgSecond.setZqMoney(faStockSgSecond.getSgFxPrice().multiply(new BigDecimal(faStockSgSecond.getZqNums())));
                }

                // 剩余数量是否足够
                FaNewStock faNewStock = iFaShengouService.getById(stockSgSecond.getShengouId());
                if (faNewStock.getSgNums() < faStockSgSecond.getZqNums()) {
                    throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
                }

                // 减少剩余数量
                LambdaUpdateWrapper<FaNewStock> newStockLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                newStockLambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
                newStockLambdaUpdateWrapper.set(FaNewStock::getSgNums, faNewStock.getSgNums() - faStockSgSecond.getZqNums());
                newStockLambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                iFaShengouService.update(newStockLambdaUpdateWrapper);

                this.updateFaStockSgSecond(faStockSgSecond);
                // 发中签通知
                iFaUserNoticeService.addAllocation(stockSgSecond);
                // 查询风控设置 是否自动认缴 默认关闭
                String autoSubscription = iFaRiskConfigService.getConfigValue("pszdrj", "0");
                if ("1".equals(autoSubscription)) {
                    // 自动认缴 == 后台认缴
                    this.subscription(stockSgSecond);
                }
            }

            // 转申购中
            if (faStockSgSecond.getStatus() == 0) {
                faStockSgSecond.setZqNum(0);
                faStockSgSecond.setZqNums(0);
                faStockSgSecond.setZqMoney(BigDecimal.ZERO);
                faStockSgSecond.setUpdateTime(new Date());
                this.updateFaStockSgSecond(faStockSgSecond);
            }

            // 转未中签
            if (faStockSgSecond.getStatus() == 2) {
                unAllocation(stockSgSecond);
            }

            // 转弃购
            if (faStockSgSecond.getStatus() == 3) {
                giveUpAllocation(stockSgSecond);
            }
        }
    }

    /**
     * 未中签
     * @param faStockSgSecond
     * @throws Exception
     */
    private void unAllocation(FaStockSgSecond faStockSgSecond) throws Exception {
        // 申购信息更新
        LambdaUpdateWrapper<FaStockSgSecond> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockSgSecond::getId, faStockSgSecond.getId());
        // 未中签
        lambdaUpdateWrapper.set(FaStockSgSecond::getStatus, 2);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqNum, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqNums, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqMoney, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 弃购
     * @param faStockSgSecond
     * @throws Exception
     */
    private void giveUpAllocation(FaStockSgSecond faStockSgSecond) throws Exception {
        // 申购信息更新
        LambdaUpdateWrapper<FaStockSgSecond> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStockSgSecond::getId, faStockSgSecond.getId());
        // 弃购
        lambdaUpdateWrapper.set(FaStockSgSecond::getStatus, 3);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqNum, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqNums, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getZqMoney, 0);
        lambdaUpdateWrapper.set(FaStockSgSecond::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 后台认缴(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Override
    public void subscription(FaStockSgSecond faStockSgSecond) throws Exception {
        // 参数判断
        if (null == faStockSgSecond.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faStockSgSecond = this.getById(faStockSgSecond.getId());
        if (ObjectUtils.isEmpty(faStockSgSecond)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaStockSgSecond> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStockSgSecond::getId, faStockSgSecond.getId());
        lambdaQueryWrapper.eq(FaStockSgSecond::getUserId, faStockSgSecond.getUserId());
        lambdaQueryWrapper.eq(FaStockSgSecond::getDeleteFlag, 0);
        FaStockSgSecond stockSgSecond = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(stockSgSecond)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 认缴状态判断
        if (1 == stockSgSecond.getRenjiao()) {
            throw new ServiceException(MessageUtils.message("member.already.subscription"), HttpStatus.ERROR);
        }
        FaMember faMember = iFaMemberService.getById(stockSgSecond.getUserId());
        // 限制认缴开关 0关闭 可以扣成负数    1开启  判断余额是否足够
        String wxzrenjiao = iFaRiskConfigService.getConfigValue("psxzrj", "1");
        if ("1".equals(wxzrenjiao)) {
            if (faMember.getBalance().compareTo(stockSgSecond.getZqMoney()) < 0) {
                throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
            }
        }

        stockSgSecond.setFaMember(faMember);
        // 记录认缴流水
        iFaCapitalLogService.save(stockSgSecond);
        // 更新申购信息
        stockSgSecond.setRenjiao(1);
        stockSgSecond.setRenjiaoTime(new Date());
        stockSgSecond.setUpdateTime(new Date());
        this.updateFaStockSgSecond(stockSgSecond);

        // 发认缴通知
        iFaUserNoticeService.addSubscriptionBg(stockSgSecond);
    }

    /**
     * 一键转持仓(世纪独享)
     * @throws Exception
     */
    @Override
    public void transToHold() throws Exception {
        // 取出未转持仓申购
        LambdaQueryWrapper<FaStockSgSecond> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 申购中/中签
        lambdaQueryWrapper.in(FaStockSgSecond::getStatus, 0, 1);
        // 未转持仓
        lambdaQueryWrapper.eq(FaStockSgSecond::getIsCc, 0);

        lambdaQueryWrapper.eq(FaStockSgSecond::getDeleteFlag, 0);
        List<FaStockSgSecond> faStockSgSecondList = this.list(lambdaQueryWrapper);

        for (FaStockSgSecond stockSgSecond : faStockSgSecondList) {
            // 是否上市
            FaNewStock faNewStock = faShengouMapper.selectById(stockSgSecond.getShengouId());
            // 上市
            if (1 == faNewStock.getIsList()) {
                // 中签且认缴 --》转持仓
                if (1 == stockSgSecond.getStatus() && 1 == stockSgSecond.getRenjiao()) {
                    transShenGouToHold(stockSgSecond);
                }
                // 申购中 --》转弃购
                else if (0 == stockSgSecond.getStatus()) {
                    stockSgSecond.setStatus(3);
                    stockSgSecond.setZqNum(0);
                    stockSgSecond.setZqNums(0);
                    stockSgSecond.setZqMoney(BigDecimal.ZERO);
                    stockSgSecond.setUpdateTime(new Date());
                    this.updateFaStockSgSecond(stockSgSecond);
                }
            }
        }
    }

    /**
     * 单个转持仓(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Override
    public void transOneToHold(FaStockSgSecond faStockSgSecond) throws Exception {
        if (null == faStockSgSecond.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faStockSgSecond = this.getById(faStockSgSecond.getId());
        if (ObjectUtils.isEmpty(faStockSgSecond)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 是否已转持仓
        if (faStockSgSecond.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        transShenGouToHold(faStockSgSecond);
    }

    private void transShenGouToHold(FaStockSgSecond stockSgSecond) throws Exception {
        // 用户信息
        FaMember faMember = iFaMemberService.getById(stockSgSecond.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 股票
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, stockSgSecond.getCode());
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
        faStockHoldDetail.setHoldHand(stockSgSecond.getZqNums() / 100);
        faStockHoldDetail.setHoldNumber(stockSgSecond.getZqNums());
        faStockHoldDetail.setAverage(stockSgSecond.getSgFxPrice());
        // 新股转
        faStockHoldDetail.setResourceType(1);
        faStockHoldDetail.setCreateTime(new Date());
        faStockHoldDetail.setStatus(0);
        faStockHoldDetail.setDeleteFlag(0);
        faStockHoldDetail.setFreezeNumber(stockSgSecond.getZqNums());
        faStockHoldDetail.setFreezeDaysLeft(1);
        faStockHoldDetail.setFreezeStatus(0);
        // 申购
        faStockHoldDetail.setHoldId(0);
        // 申购id
        faStockHoldDetail.setNewStockId(stockSgSecond.getId());
        // 已上市，新股持仓
        faStockHoldDetail.setHoldType(0);
        faStockHoldDetail.setIsList(1);

        // 买入价，买入时间
        faStockHoldDetail.setBuyPrice(stockSgSecond.getSgFxPrice());
        faStockHoldDetail.setBuyTime(faStockHoldDetail.getCreateTime());
        faStockHoldDetail.setTradingHand(faStockHoldDetail.getHoldHand());
        faStockHoldDetail.setTradingNumber(faStockHoldDetail.getHoldNumber());

        iFaStockHoldDetailService.save(faStockHoldDetail);

        // 更新申购状态
        stockSgSecond.setIsCc(1);
        stockSgSecond.setIsCcTime(new Date());
        stockSgSecond.setUpdateTime(new Date());
        this.updateFaStockSgSecond(stockSgSecond);
    }
}