package com.ruoyi.biz.sgList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgList.mapper.FaSgListMapper;
import com.ruoyi.biz.sgList.service.IFaSgListService;
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
 * 申购列表Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class FaSgListServiceImpl extends ServiceImpl<FaSgListMapper, FaSgList> implements IFaSgListService
{
    @Autowired
    private FaSgListMapper faSgListMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaUserNoticeService iFaUserNoticeService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private IFaShengouService iFaShengouService;

    @Autowired
    private FaShengouMapper faShengouMapper;

    /**
     * 查询申购列表
     *
     * @param id 申购列表主键
     * @return 申购列表
     */
    @Override
    public FaSgList selectFaSgListById(Long id)
    {
        FaSgList faSgList = faSgListMapper.selectFaSgListById(id);
        FaMember faMember = iFaMemberService.getById(faSgList.getUserId());
        faSgList.setFaMember(faMember);
        return faSgListMapper.selectFaSgListById(id);
    }

    /**
     * 查询申购列表列表
     *
     * @param faSgList 申购列表
     * @return 申购列表
     */
    @Override
    public List<FaSgList> selectFaSgListList(FaSgList faSgList)
    {
        try {
            faSgList.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faSgList.getMobile())) {
                faSgList.setSalt(AESUtil.encrypt(faSgList.getMobile()));
                faSgList.setMobile(null);
            }

            return faSgListMapper.selectFaSgListList(faSgList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增申购列表
     *
     * @param faSgList 申购列表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaSgList(FaSgList faSgList) throws Exception
    {
        if (null == faSgList.getCreateTime()) {
            faSgList.setCreateTime(DateUtils.getNowDate());
        }

        FaNewStock faNewStock = new FaNewStock();
        faNewStock.setId(faSgList.getShengouId());
        faNewStock.setMemberId(faSgList.getUserId());
        faNewStock.setSgNums(faSgList.getSgNum());
        faNewStock.setCreateTime(faSgList.getCreateTime());

        // 保证金模式，先扣
        iFaShengouService.addShengou(faNewStock);
        return 1;
    }

    /**
     * 修改申购列表
     *
     * @param faSgList 申购列表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaSgList(FaSgList faSgList)
    {
        faSgList.setUpdateTime(DateUtils.getNowDate());
        return faSgListMapper.updateFaSgList(faSgList);
    }

    /**
     * 批量删除申购列表
     *
     * @param ids 需要删除的申购列表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSgListByIds(Long[] ids)
    {
        return faSgListMapper.deleteFaSgListByIds(ids);
    }

    /**
     * 删除申购列表信息
     *
     * @param id 申购列表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSgListById(Long id)
    {
        return faSgListMapper.deleteFaSgListById(id);
    }

    /**
     * 查询用户申购列表
     * @param faSgList
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaSgList> getMemberSgPage(FaSgList faSgList) throws Exception {
        LambdaQueryWrapper<FaSgList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgList::getUserId, faSgList.getUserId());
        if (null != faSgList.getStatus()) {
            lambdaQueryWrapper.eq(FaSgList::getStatus, faSgList.getStatus());
        } else {
            lambdaQueryWrapper.in(FaSgList::getStatus, 0, 1, 2);
        }

        if (null != faSgList.getSgType()) {
            lambdaQueryWrapper.eq(FaSgList::getSgType, faSgList.getSgType());
        }

        if (null != faSgList.getIsCc()) {
            lambdaQueryWrapper.eq(FaSgList::getIsCc, faSgList.getIsCc());
        }

        lambdaQueryWrapper.eq(FaSgList::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaSgList::getCreateTime);
        IPage<FaSgList> faSgListIPage = this.page(new Page<>(faSgList.getPage(), faSgList.getSize()), lambdaQueryWrapper);

        for (FaSgList sgList : faSgListIPage.getRecords()) {
            FaNewStock faNewStock = iFaShengouService.getById(sgList.getShengouId());
            if (ObjectUtils.isNotEmpty(faNewStock) && null != faNewStock.getFxRate()) {
                sgList.setFxRate(new BigDecimal(faNewStock.getFxRate()));
            }
        }

        return faSgListIPage;
    }

    /**
     * 提交中签
     * @param faSgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitAllocation(FaSgList faSgList) throws Exception {
        LambdaQueryWrapper<FaSgList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgList::getId, faSgList.getId());
        lambdaQueryWrapper.eq(FaSgList::getDeleteFlag, 0);
        // 数据库状态
        FaSgList sgList = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(sgList)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 已转未中签或弃购，禁止操作
        if (sgList.getStatus() == 2 || sgList.getStatus() == 3) {
            throw new ServiceException(MessageUtils.message("subscribe.operated"), HttpStatus.ERROR);
        }

        // 已转持仓，禁止操作
        if (sgList.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        // 重复操作
        if (sgList.getStatus().equals(faSgList.getStatus())) {
            throw new ServiceException(MessageUtils.message("repeat.operate"), HttpStatus.ERROR);
        }

        // 已认缴的 --》 申购中/未中签/弃购
        if (sgList.getRenjiao() == 1) {
            if(faSgList.getStatus() == 0 || faSgList.getStatus() == 2 || faSgList.getStatus() == 3) {
                // 查询绑定的资金流水
                LambdaQueryWrapper<FaCapitalLog> capitalLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getUserId, sgList.getUserId());
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getOrderId, sgList.getId());
                capitalLogLambdaQueryWrapper.eq(FaCapitalLog::getType, CapitalFlowConstants.SUBSCRIPTION);
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
                faSgList.setRenjiao(0);
                faSgList.setZqNum(0);
                faSgList.setZqNums(0);
                faSgList.setZqMoney(BigDecimal.ZERO);
                faSgList.setUpdateTime(new Date());
                this.updateFaSgList(faSgList);
            }
        }
        // 未认缴的
        else if (sgList.getRenjiao() == 0) {
            // 申购中转中签
            if (sgList.getStatus() == 0 && faSgList.getStatus() == 1) {
                faSgList.setUpdateTime(new Date());
                // 手->股转换 根据风控设置来，默认股(0股1签)
                String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
                // 按股计算 提交过来的是zqNum
                if ("0".equals(gqpeizhi)) {
                    faSgList.setZqNums(faSgList.getZqNum() * 100);
                    faSgList.setZqMoney(faSgList.getSgFxPrice().multiply(new BigDecimal(faSgList.getZqNums())));
                }
                // 按签计算 提交过来的是zqNum
                else if ("1".equals(gqpeizhi)) {
                    // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                    if (1 == sgList.getSgType()) {
                        faSgList.setZqNums(faSgList.getZqNum() * 1000);
                    } else {
                        faSgList.setZqNums(faSgList.getZqNum() * 500);
                    }
                    faSgList.setZqMoney(faSgList.getSgFxPrice().multiply(new BigDecimal(faSgList.getZqNums())));
                }

                // 剩余数量是否足够
                FaNewStock faNewStock = iFaShengouService.getById(sgList.getShengouId());
                if (faNewStock.getSgNums() < faSgList.getZqNums()) {
                    throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
                }

                // 减少剩余数量
                LambdaUpdateWrapper<FaNewStock> newStockLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                newStockLambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
                newStockLambdaUpdateWrapper.set(FaNewStock::getSgNums, faNewStock.getSgNums() - faSgList.getZqNums());
                newStockLambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
                iFaShengouService.update(newStockLambdaUpdateWrapper);

                this.updateFaSgList(faSgList);
                // 发中签通知
                iFaUserNoticeService.addAllocation(sgList);
                // 查询风控设置 是否自动认缴 默认关闭
                String autoSubscription = iFaRiskConfigService.getConfigValue("autorj", "0");
                if ("1".equals(autoSubscription)) {
                    // 自动认缴 == 后台认缴
                    this.subscription(sgList);
                }
            }

            // 转申购中
            if (faSgList.getStatus() == 0) {
                faSgList.setZqNum(0);
                faSgList.setZqNums(0);
                faSgList.setZqMoney(BigDecimal.ZERO);
                faSgList.setUpdateTime(new Date());
                this.updateFaSgList(faSgList);
            }

            // 转未中签
            if (faSgList.getStatus() == 2) {
                unAllocation(sgList);
            }

            // 转弃购
            if (faSgList.getStatus() == 3) {
                giveUpAllocation(sgList);
            }
        }
    }

    /**
     * 未中签
     * @param sgList
     * @throws Exception
     */
    private void unAllocation(FaSgList sgList) throws Exception {
        // 申购信息更新
        LambdaUpdateWrapper<FaSgList> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSgList::getId, sgList.getId());
        // 未中签
        lambdaUpdateWrapper.set(FaSgList::getStatus, 2);
        lambdaUpdateWrapper.set(FaSgList::getZqNum, 0);
        lambdaUpdateWrapper.set(FaSgList::getZqNums, 0);
        lambdaUpdateWrapper.set(FaSgList::getZqMoney, 0);
        lambdaUpdateWrapper.set(FaSgList::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 弃购
     * @param sgList
     * @throws Exception
     */
    private void giveUpAllocation(FaSgList sgList) throws Exception {
        // 申购信息更新
        LambdaUpdateWrapper<FaSgList> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSgList::getId, sgList.getId());
        // 弃购
        lambdaUpdateWrapper.set(FaSgList::getStatus, 3);
        lambdaUpdateWrapper.set(FaSgList::getZqNum, 0);
        lambdaUpdateWrapper.set(FaSgList::getZqNums, 0);
        lambdaUpdateWrapper.set(FaSgList::getZqMoney, 0);
        lambdaUpdateWrapper.set(FaSgList::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 打新认缴
     * @param faSgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void subscription(FaSgList faSgList) throws Exception {
        // 参数判断
        if (null == faSgList.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faSgList = this.getById(faSgList.getId());
        if (ObjectUtils.isEmpty(faSgList)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaSgList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSgList::getId, faSgList.getId());
        lambdaQueryWrapper.eq(FaSgList::getUserId, faSgList.getUserId());
        lambdaQueryWrapper.eq(FaSgList::getDeleteFlag, 0);
        FaSgList sgList = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(sgList)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 认缴状态判断
        if (1 == sgList.getRenjiao()) {
            throw new ServiceException(MessageUtils.message("member.already.subscription"), HttpStatus.ERROR);
        }
        FaMember faMember = iFaMemberService.getById(sgList.getUserId());
        // 限制认缴开关 0关闭 可以扣成负数    1开启  判断余额是否足够
        String wxzrenjiao = iFaRiskConfigService.getConfigValue("wxzrenjiao", "1");
        if ("1".equals(wxzrenjiao)) {
            if (faMember.getBalance().compareTo(sgList.getZqMoney()) < 0) {
                throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
            }
        }

        sgList.setFaMember(faMember);
        // 记录认缴流水
        iFaCapitalLogService.save(sgList);
        // 更新申购信息
        sgList.setRenjiao(1);
        sgList.setRenjiaoTime(new Date());
        sgList.setUpdateTime(new Date());
        this.updateFaSgList(sgList);

        // 发认缴通知
        iFaUserNoticeService.addSubscriptionBg(sgList);
    }

    /**
     * 一键转持仓
     * @throws Exception
     */
    @Transactional
    @Override
    public void transToHold() throws Exception {
        // 取出未转持仓申购
        LambdaQueryWrapper<FaSgList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 申购中/中签
        lambdaQueryWrapper.in(FaSgList::getStatus, 0, 1);
        // 未转持仓
        lambdaQueryWrapper.eq(FaSgList::getIsCc, 0);

        lambdaQueryWrapper.eq(FaSgList::getDeleteFlag, 0);
        List<FaSgList> faSgListList = this.list(lambdaQueryWrapper);

        for (FaSgList stockSg : faSgListList) {
            // 是否上市
            FaNewStock faNewStock = faShengouMapper.selectById(stockSg.getShengouId());
            // 上市
            if (1 == faNewStock.getIsList()) {
                // 中签且认缴 --》转持仓
                if (1 == stockSg.getStatus() && 1 == stockSg.getRenjiao()) {
                    transShenGouToHold(stockSg);
                }
                // 申购中 --》转弃购
                else if (0 == stockSg.getStatus()) {
                    stockSg.setStatus(3);
                    stockSg.setZqNum(0);
                    stockSg.setZqNums(0);
                    stockSg.setZqMoney(BigDecimal.ZERO);
                    stockSg.setUpdateTime(new Date());
                    this.updateFaSgList(stockSg);
                }
            }
        }
    }

    /**
     * 单个转持仓
     * @param faSgList
     * @throws Exception
     */
    @Override
    public void transOneToHold(FaSgList faSgList) throws Exception {
        if (null == faSgList.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faSgList = this.getById(faSgList.getId());
        if (ObjectUtils.isEmpty(faSgList)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 是否已转持仓
        if (faSgList.getIsCc() == 1) {
            throw new ServiceException(MessageUtils.message("subscribe.convert.to.position"), HttpStatus.ERROR);
        }

        transShenGouToHold(faSgList);
    }

    private void transShenGouToHold(FaSgList stockSg) throws Exception {
        // 用户信息
        FaMember faMember = iFaMemberService.getById(stockSg.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 股票
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, stockSg.getCode());
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
        faStockHoldDetail.setHoldHand(stockSg.getZqNums() / 100);
        faStockHoldDetail.setHoldNumber(stockSg.getZqNums());
        faStockHoldDetail.setAverage(stockSg.getSgFxPrice());
        // 新股转
        faStockHoldDetail.setResourceType(1);
        faStockHoldDetail.setCreateTime(new Date());
        faStockHoldDetail.setStatus(0);
        faStockHoldDetail.setDeleteFlag(0);
        faStockHoldDetail.setFreezeNumber(stockSg.getZqNums());
        faStockHoldDetail.setFreezeDaysLeft(1);
        faStockHoldDetail.setFreezeStatus(0);
        // 申购
        faStockHoldDetail.setHoldId(0);
        // 申购id
        faStockHoldDetail.setNewStockId(stockSg.getId());
        // 已上市，新股持仓
        faStockHoldDetail.setHoldType(0);
        faStockHoldDetail.setIsList(1);

        // 买入价，买入时间
        faStockHoldDetail.setBuyPrice(stockSg.getSgFxPrice());
        faStockHoldDetail.setBuyTime(faStockHoldDetail.getCreateTime());
        faStockHoldDetail.setTradingHand(faStockHoldDetail.getHoldHand());
        faStockHoldDetail.setTradingNumber(faStockHoldDetail.getHoldNumber());

        iFaStockHoldDetailService.save(faStockHoldDetail);

        // 更新申购状态
        stockSg.setIsCc(1);
        stockSg.setIsCcTime(new Date());
        stockSg.setUpdateTime(new Date());
        this.updateFaSgList(stockSg);
    }

}