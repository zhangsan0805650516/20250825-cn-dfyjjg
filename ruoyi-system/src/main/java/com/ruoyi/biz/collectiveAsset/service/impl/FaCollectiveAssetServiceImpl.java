package com.ruoyi.biz.collectiveAsset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.assetRecord.mapper.FaCollectiveAssetRecordMapper;
import com.ruoyi.biz.assetRecord.service.IFaCollectiveAssetRecordService;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;
import com.ruoyi.biz.collectiveAsset.mapper.FaCollectiveAssetMapper;
import com.ruoyi.biz.collectiveAsset.service.IFaCollectiveAssetService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 集合资产Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-17
 */
@Service
public class FaCollectiveAssetServiceImpl extends ServiceImpl<FaCollectiveAssetMapper, FaCollectiveAsset> implements IFaCollectiveAssetService
{
    @Autowired
    private FaCollectiveAssetMapper faCollectiveAssetMapper;

    @Autowired
    private FaCollectiveAssetRecordMapper faCollectiveAssetRecordMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    /**
     * 查询集合资产
     *
     * @param id 集合资产主键
     * @return 集合资产
     */
    @Override
    public FaCollectiveAsset selectFaCollectiveAssetById(Integer id)
    {
        return faCollectiveAssetMapper.selectFaCollectiveAssetById(id);
    }

    /**
     * 查询集合资产列表
     *
     * @param faCollectiveAsset 集合资产
     * @return 集合资产
     */
    @Override
    public List<FaCollectiveAsset> selectFaCollectiveAssetList(FaCollectiveAsset faCollectiveAsset)
    {
        faCollectiveAsset.setDeleteFlag(0);
        return faCollectiveAssetMapper.selectFaCollectiveAssetList(faCollectiveAsset);
    }

    /**
     * 新增集合资产
     *
     * @param faCollectiveAsset 集合资产
     * @return 结果
     */
    @Override
    public int insertFaCollectiveAsset(FaCollectiveAsset faCollectiveAsset)
    {
        faCollectiveAsset.setCreateTime(DateUtils.getNowDate());
        return faCollectiveAssetMapper.insertFaCollectiveAsset(faCollectiveAsset);
    }

    /**
     * 修改集合资产
     *
     * @param faCollectiveAsset 集合资产
     * @return 结果
     */
    @Override
    public int updateFaCollectiveAsset(FaCollectiveAsset faCollectiveAsset)
    {
        faCollectiveAsset.setUpdateTime(DateUtils.getNowDate());
        return faCollectiveAssetMapper.updateFaCollectiveAsset(faCollectiveAsset);
    }

    /**
     * 批量删除集合资产
     *
     * @param ids 需要删除的集合资产主键
     * @return 结果
     */
    @Override
    public int deleteFaCollectiveAssetByIds(Integer[] ids)
    {
        return faCollectiveAssetMapper.deleteFaCollectiveAssetByIds(ids);
    }

    /**
     * 删除集合资产信息
     *
     * @param id 集合资产主键
     * @return 结果
     */
    @Override
    public int deleteFaCollectiveAssetById(Integer id)
    {
        return faCollectiveAssetMapper.deleteFaCollectiveAssetById(id);
    }

    /**
     * 集合资产列表
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    @Override
    public List<FaCollectiveAsset> getAssetList(FaCollectiveAsset faCollectiveAsset) throws Exception {
        LambdaQueryWrapper<FaCollectiveAsset> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faCollectiveAsset.getAssetName())) {
            lambdaQueryWrapper.like(FaCollectiveAsset::getAssetName, faCollectiveAsset.getAssetName());
        }
        lambdaQueryWrapper.eq(FaCollectiveAsset::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaCollectiveAsset::getCreateTime);
        List<FaCollectiveAsset> list = this.list(lambdaQueryWrapper);
        for (FaCollectiveAsset collectiveAsset : list) {
            if (StringUtils.isNotEmpty(collectiveAsset.getAssetSecret())) {
                collectiveAsset.setNeedSecret(1);
                collectiveAsset.setAssetSecret(null);
            }
        }
        return list;
    }

    /**
     * 买入集合资产详情
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    @Override
    public FaCollectiveAsset getBuyAssetDetail(FaCollectiveAsset faCollectiveAsset) throws Exception {
        // 资产密钥
        String assetSecret = faCollectiveAsset.getAssetSecret();
        // 排序
        int orderBy = faCollectiveAsset.getOrderBy();

        if (null == faCollectiveAsset.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faCollectiveAsset = this.getById(faCollectiveAsset.getId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 资产密钥判断
        if (StringUtils.isNotEmpty(faCollectiveAsset.getAssetSecret()) && !faCollectiveAsset.getAssetSecret().equals(assetSecret)) {
            throw new ServiceException("邀请码错误", HttpStatus.ERROR);
        }

        // 查询资产关联买入记录
        MPJLambdaWrapper<FaCollectiveAssetRecord> recordMPJLambdaWrapper = new MPJLambdaWrapper<>();
        recordMPJLambdaWrapper.selectAll(FaCollectiveAssetRecord.class);
        recordMPJLambdaWrapper.selectFunc("CONCAT(SUBSTRING(%s, 1, 1), '*', SUBSTRING(%s, 3))",
                arg -> arg.accept(FaMember::getName, FaMember::getName)
                , FaCollectiveAssetRecord::getName);
        recordMPJLambdaWrapper.selectAs(FaMember::getMobile, FaCollectiveAssetRecord::getMobile);
        recordMPJLambdaWrapper.leftJoin(FaMember.class, FaMember::getId, FaCollectiveAssetRecord::getMemberId);
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, faCollectiveAsset.getId());
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 1);
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        // 买入时间倒序
        if (0 == orderBy) {
            recordMPJLambdaWrapper.orderByDesc(FaCollectiveAssetRecord::getCreateTime);
        }
        // 买入金额倒序
        else if (1 == orderBy) {
            recordMPJLambdaWrapper.orderByDesc(FaCollectiveAssetRecord::getAmount);
        }
        recordMPJLambdaWrapper.last(" limit  100 ");

        List<FaCollectiveAssetRecord> recordList= faCollectiveAssetRecordMapper.selectJoinList(FaCollectiveAssetRecord.class, recordMPJLambdaWrapper);
        faCollectiveAsset.setCollectiveAssetRecords(recordList);

        faCollectiveAsset.setAssetSecret(null);
        return faCollectiveAsset;
    }

    /**
     * 赎回集合资产详情
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    @Override
    public FaCollectiveAsset getRedeemAssetDetail(FaCollectiveAsset faCollectiveAsset) throws Exception {
        if (null == faCollectiveAsset.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaCollectiveAsset selectOne = this.getById(faCollectiveAsset.getId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 个人资产可赎回总额 = 个人买入总额 - 个人已赎回 - 个人正在赎回
        // 个人买入总额
        MPJLambdaWrapper<FaCollectiveAssetRecord> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, selectOne.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faCollectiveAsset.getMemberId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 1);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> buyList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal buyTotal = buyList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 个人已赎回
        mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, selectOne.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faCollectiveAsset.getMemberId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 2);
        // 审核通过
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getStatus, 1);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> assetList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal assetTotal = assetList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 个人正在赎回
        mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, selectOne.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faCollectiveAsset.getMemberId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 2);
        // 审核通过
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getStatus, 0);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> assetingList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal assetingTotal = assetingList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 查询资产关联记录 (1持有 2赎回)
        MPJLambdaWrapper<FaCollectiveAssetRecord> recordMPJLambdaWrapper = new MPJLambdaWrapper<>();
        recordMPJLambdaWrapper.selectAll(FaCollectiveAssetRecord.class);
        recordMPJLambdaWrapper.selectFunc("CONCAT(SUBSTRING(%s, 1, 1), '*', SUBSTRING(%s, 3))",
                arg -> arg.accept(FaMember::getName, FaMember::getName)
                , FaCollectiveAssetRecord::getName);
        recordMPJLambdaWrapper.selectAs(FaMember::getMobile, FaCollectiveAssetRecord::getMobile);
        recordMPJLambdaWrapper.leftJoin(FaMember.class, FaMember::getId, FaCollectiveAssetRecord::getMemberId);
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, faCollectiveAsset.getId());
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        recordMPJLambdaWrapper.orderByDesc(FaCollectiveAssetRecord::getCreateTime);
        recordMPJLambdaWrapper.last(" limit  100 ");

        List<FaCollectiveAssetRecord> recordList= faCollectiveAssetRecordMapper.selectJoinList(FaCollectiveAssetRecord.class, recordMPJLambdaWrapper);
        selectOne.setCollectiveAssetRecords(recordList);

        selectOne.setAssetSecret(null);

        selectOne.setAssetRedeemAmountPerson(buyTotal.subtract(assetTotal).subtract(assetingTotal));
        selectOne.setRedeemTotalAmountPerson(assetingTotal);
        return selectOne;
    }

    /**
     * 集合资产买入
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    @Override
    public void buyAsset(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception {
        if (null == faCollectiveAssetRecord.getMemberId() || null == faCollectiveAssetRecord.getAssetId() ||
                null == faCollectiveAssetRecord.getAmount() || StringUtils.isEmpty(faCollectiveAssetRecord.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户信息
        FaMember faMember = iFaMemberService.getById(faCollectiveAssetRecord.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 支付密码校验
        if (!SecurityUtils.matchesPassword(faCollectiveAssetRecord.getPaymentCode(), faMember.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("payment.password.error"), HttpStatus.ERROR);
        }

        // 集合资产
        FaCollectiveAsset faCollectiveAsset = this.getById(faCollectiveAssetRecord.getAssetId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 余额是否足够
        if (faMember.getBalance().compareTo(faCollectiveAssetRecord.getAmount()) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 资产余额是否足够
        if (faCollectiveAsset.getAssetTotalAmount().subtract(faCollectiveAsset.getSellTotalAmount()).compareTo(faCollectiveAssetRecord.getAmount()) < 0) {
            throw new ServiceException("集合资产余额不足", HttpStatus.ERROR);
        }

        // 保存买入记录
        faCollectiveAssetRecord.setType(1);
        faCollectiveAssetRecord.setStatus(1);
        faCollectiveAssetRecord.setCreateTime(new Date());
        faCollectiveAssetRecord.setDeleteFlag(0);
        faCollectiveAssetRecordMapper.insert(faCollectiveAssetRecord);

        // 更新集合资产信息
        LambdaUpdateWrapper<FaCollectiveAsset> assetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        assetLambdaUpdateWrapper.eq(FaCollectiveAsset::getId, faCollectiveAsset.getId());
        assetLambdaUpdateWrapper.set(FaCollectiveAsset::getSellTotalAmount, faCollectiveAsset.getSellTotalAmount().add(faCollectiveAssetRecord.getAmount()));
        assetLambdaUpdateWrapper.set(FaCollectiveAsset::getUpdateTime, new Date());
        this.update(assetLambdaUpdateWrapper);

        // 更新用户余额，记录流水
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.JHZC_BUY));
        faCapitalLog.setMoney(faCollectiveAssetRecord.getAmount());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(CapitalFlowConstants.JHZC_BUY);
        faCapitalLog.setDirect(1);
        faCapitalLog.setOrderId(faCollectiveAssetRecord.getId());
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 更新用户余额
        iFaMemberService.updateMemberBalance(faCapitalLog.getUserId(), faCapitalLog.getMoney(), faCapitalLog.getDirect());

        // 更新用户冻结 减少
        iFaMemberService.updateFaMemberFreezeProfit(faCapitalLog.getUserId(), faCapitalLog.getMoney(), faCapitalLog.getDirect());
    }

    /**
     * 集合资产赎回
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    @Override
    public void redeemAsset(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception {
        if (null == faCollectiveAssetRecord.getMemberId() || null == faCollectiveAssetRecord.getAssetId() ||
                null == faCollectiveAssetRecord.getAmount() || StringUtils.isEmpty(faCollectiveAssetRecord.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户信息
        FaMember faMember = iFaMemberService.getById(faCollectiveAssetRecord.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 支付密码校验
        if (!SecurityUtils.matchesPassword(faCollectiveAssetRecord.getPaymentCode(), faMember.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("payment.password.error"), HttpStatus.ERROR);
        }

        // 集合资产
        FaCollectiveAsset faCollectiveAsset = this.getById(faCollectiveAssetRecord.getAssetId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 资产可赎回余额是否足够
        // 个人资产可赎回总额 = 个人买入总额 - 个人已赎回 - 个人正在赎回
        // 个人买入总额
        MPJLambdaWrapper<FaCollectiveAssetRecord> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, faCollectiveAsset.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faMember.getId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 1);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> buyList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal buyTotal = buyList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 个人已赎回
        mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, faCollectiveAsset.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faMember.getId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 2);
        // 审核通过
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getStatus, 1);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> assetList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal assetTotal = assetList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 个人正在赎回
        mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getAssetId, faCollectiveAsset.getId());
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getMemberId, faMember.getId());
        // 1持有 2赎回
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getType, 2);
        // 审核通过
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getStatus, 0);
        mpjLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);
        List<FaCollectiveAssetRecord> assetingList = faCollectiveAssetRecordMapper.selectList(mpjLambdaWrapper);
        BigDecimal assetingTotal = assetingList.stream().map(FaCollectiveAssetRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (buyTotal.subtract(assetTotal).subtract(assetingTotal).compareTo(faCollectiveAssetRecord.getAmount()) < 0) {
            throw new ServiceException("可赎回余额不足", HttpStatus.ERROR);
        }

        // 保存赎回记录
        faCollectiveAssetRecord.setType(2);
        faCollectiveAssetRecord.setStatus(0);
        faCollectiveAssetRecord.setCreateTime(new Date());
        faCollectiveAssetRecord.setDeleteFlag(0);
        faCollectiveAssetRecordMapper.insert(faCollectiveAssetRecord);

        // 更新集合资产信息
//        LambdaUpdateWrapper<FaCollectiveAsset> assetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        assetLambdaUpdateWrapper.eq(FaCollectiveAsset::getId, faCollectiveAsset.getId());
//        assetLambdaUpdateWrapper.set(FaCollectiveAsset::getRedeemTotalAmount, faCollectiveAsset.getRedeemTotalAmount().add(faCollectiveAssetRecord.getAmount()));
//        assetLambdaUpdateWrapper.set(FaCollectiveAsset::getUpdateTime, new Date());
//        this.update(assetLambdaUpdateWrapper);
    }

    /**
     * 集合资产开始
     * @param faCollectiveAsset
     * @throws Exception
     */
    @Override
    public void startAsset(FaCollectiveAsset faCollectiveAsset) throws Exception {
        faCollectiveAsset = this.getById(faCollectiveAsset.getId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 判断状态
        if (0 != faCollectiveAsset.getStatus()) {
            throw new ServiceException("状态错误", HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaCollectiveAsset> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaCollectiveAsset::getId, faCollectiveAsset.getId());
        lambdaUpdateWrapper.set(FaCollectiveAsset::getStatus, 1);
        lambdaUpdateWrapper.set(FaCollectiveAsset::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 集合资产结束
     * @param faCollectiveAsset
     * @throws Exception
     */
    @Override
    public void endAsset(FaCollectiveAsset faCollectiveAsset) throws Exception {
        faCollectiveAsset = this.getById(faCollectiveAsset.getId());
        if (ObjectUtils.isEmpty(faCollectiveAsset)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 判断状态
        if (1 != faCollectiveAsset.getStatus()) {
            throw new ServiceException("状态错误", HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaCollectiveAsset> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaCollectiveAsset::getId, faCollectiveAsset.getId());
        lambdaUpdateWrapper.set(FaCollectiveAsset::getStatus, 2);
        lambdaUpdateWrapper.set(FaCollectiveAsset::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

}