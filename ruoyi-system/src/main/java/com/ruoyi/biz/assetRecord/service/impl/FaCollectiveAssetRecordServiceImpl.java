package com.ruoyi.biz.assetRecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.assetRecord.mapper.FaCollectiveAssetRecordMapper;
import com.ruoyi.biz.assetRecord.service.IFaCollectiveAssetRecordService;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;
import com.ruoyi.biz.collectiveAsset.service.IFaCollectiveAssetService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 集合资产记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-17
 */
@Service
public class FaCollectiveAssetRecordServiceImpl extends ServiceImpl<FaCollectiveAssetRecordMapper, FaCollectiveAssetRecord> implements IFaCollectiveAssetRecordService
{
    @Autowired
    private FaCollectiveAssetRecordMapper faCollectiveAssetRecordMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaCollectiveAssetService iFaCollectiveAssetService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    /**
     * 查询集合资产记录
     *
     * @param id 集合资产记录主键
     * @return 集合资产记录
     */
    @Override
    public FaCollectiveAssetRecord selectFaCollectiveAssetRecordById(Integer id)
    {
        FaCollectiveAssetRecord faCollectiveAssetRecord = faCollectiveAssetRecordMapper.selectFaCollectiveAssetRecordById(id);
        FaMember faMember = iFaMemberService.getById(faCollectiveAssetRecord.getMemberId());
        FaCollectiveAsset faCollectiveAsset = iFaCollectiveAssetService.getById(faCollectiveAssetRecord.getAssetId());
        faCollectiveAssetRecord.setFaMember(faMember);
        faCollectiveAssetRecord.setFaCollectiveAsset(faCollectiveAsset);
        return faCollectiveAssetRecord;
    }

    /**
     * 查询集合资产记录列表
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 集合资产记录
     */
    @Override
    public List<FaCollectiveAssetRecord> selectFaCollectiveAssetRecordList(FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        MPJLambdaWrapper<FaCollectiveAssetRecord> recordMPJLambdaWrapper = new MPJLambdaWrapper<>();
        recordMPJLambdaWrapper.selectAll(FaCollectiveAssetRecord.class);
        recordMPJLambdaWrapper.selectAssociation(FaMember.class, FaCollectiveAssetRecord::getFaMember);
        recordMPJLambdaWrapper.selectAssociation(FaCollectiveAsset.class, FaCollectiveAssetRecord::getFaCollectiveAsset);
        recordMPJLambdaWrapper.leftJoin(FaMember.class, FaMember::getId, FaCollectiveAssetRecord::getMemberId);
        recordMPJLambdaWrapper.leftJoin(FaCollectiveAsset.class, FaCollectiveAsset::getId, FaCollectiveAssetRecord::getAssetId);
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getType, faCollectiveAssetRecord.getType());
        recordMPJLambdaWrapper.eq(FaCollectiveAssetRecord::getDeleteFlag, 0);

        // 查询条件
        if (StringUtils.isNotEmpty(faCollectiveAssetRecord.getName())) {
            recordMPJLambdaWrapper.like(FaMember::getName, faCollectiveAssetRecord.getName());
        }
        if (StringUtils.isNotEmpty(faCollectiveAssetRecord.getMobile())) {
            try {
                String salt = AESUtil.encrypt(faCollectiveAssetRecord.getMobile());
                recordMPJLambdaWrapper.eq(FaMember::getSalt, salt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotEmpty(faCollectiveAssetRecord.getWeiyima())) {
            recordMPJLambdaWrapper.eq(FaMember::getWeiyima, faCollectiveAssetRecord.getWeiyima());
        }
        if (StringUtils.isNotEmpty(faCollectiveAssetRecord.getAssetName())) {
            recordMPJLambdaWrapper.like(FaCollectiveAsset::getAssetName, faCollectiveAssetRecord.getAssetName());
        }

        List<FaCollectiveAssetRecord> list = faCollectiveAssetRecordMapper.selectJoinList(FaCollectiveAssetRecord.class, recordMPJLambdaWrapper);
        for (FaCollectiveAssetRecord record : list) {
            record.getFaMember().setAvatar(null);
            record.getFaMember().setIdCardFrontImage(null);
            record.getFaMember().setIdCardBackImage(null);
            record.getFaMember().setCardImage(null);
        }
        return list;
    }

    /**
     * 新增集合资产记录
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 结果
     */
    @Override
    public int insertFaCollectiveAssetRecord(FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        faCollectiveAssetRecord.setCreateTime(DateUtils.getNowDate());
        return faCollectiveAssetRecordMapper.insertFaCollectiveAssetRecord(faCollectiveAssetRecord);
    }

    /**
     * 修改集合资产记录
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 结果
     */
    @Override
    public int updateFaCollectiveAssetRecord(FaCollectiveAssetRecord faCollectiveAssetRecord)
    {
        faCollectiveAssetRecord.setUpdateTime(DateUtils.getNowDate());
        return faCollectiveAssetRecordMapper.updateFaCollectiveAssetRecord(faCollectiveAssetRecord);
    }

    /**
     * 批量删除集合资产记录
     *
     * @param ids 需要删除的集合资产记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCollectiveAssetRecordByIds(Integer[] ids)
    {
        return faCollectiveAssetRecordMapper.deleteFaCollectiveAssetRecordByIds(ids);
    }

    /**
     * 删除集合资产记录信息
     *
     * @param id 集合资产记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCollectiveAssetRecordById(Integer id)
    {
        return faCollectiveAssetRecordMapper.deleteFaCollectiveAssetRecordById(id);
    }

    /**
     * 审核赎回
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    @Override
    public void approveRedeem(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception {
        FaCollectiveAssetRecord selectOne = this.getById(faCollectiveAssetRecord.getId());
        // 判断状态
        if (0 != selectOne.getStatus()) {
            throw new ServiceException("状态错误", HttpStatus.ERROR);
        }

        // 更新记录状态
        LambdaUpdateWrapper<FaCollectiveAssetRecord> recordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recordLambdaUpdateWrapper.eq(FaCollectiveAssetRecord::getId, selectOne.getAssetId());
        recordLambdaUpdateWrapper.set(FaCollectiveAssetRecord::getStatus, faCollectiveAssetRecord.getStatus());
        if (StringUtils.isNotEmpty(faCollectiveAssetRecord.getRejectReason())) {
            recordLambdaUpdateWrapper.set(FaCollectiveAssetRecord::getRejectReason, faCollectiveAssetRecord.getRejectReason());
        }
        recordLambdaUpdateWrapper.set(FaCollectiveAssetRecord::getUpdateTime, new Date());
        this.update(recordLambdaUpdateWrapper);

        FaMember faMember = iFaMemberService.getById(selectOne.getMemberId());
        FaCollectiveAsset faCollectiveAsset = iFaCollectiveAssetService.getById(selectOne.getMemberId());

        // 审核通过
        if (1 == faCollectiveAssetRecord.getStatus()) {
            // 更新用户余额，记录流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(faMember.getId());
            faCapitalLog.setMobile(faMember.getMobile());
            faCapitalLog.setName(faMember.getName());
            faCapitalLog.setSuperiorId(faMember.getSuperiorId());
            faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
            faCapitalLog.setSuperiorName(faMember.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.JHZC_REDEEM));
            faCapitalLog.setMoney(faCollectiveAssetRecord.getAmount());
            faCapitalLog.setBeforeMoney(faMember.getBalance());
            faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
            faCapitalLog.setType(CapitalFlowConstants.JHZC_REDEEM);
            faCapitalLog.setDirect(0);
            faCapitalLog.setOrderId(faCollectiveAssetRecord.getId());
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额
            iFaMemberService.updateMemberBalance(faCapitalLog.getUserId(), faCapitalLog.getMoney(), faCapitalLog.getDirect());
        }
        // 驳回 赎回总额减少
        else if (2 == faCollectiveAssetRecord.getStatus()) {
            LambdaUpdateWrapper<FaCollectiveAsset> assetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            assetLambdaUpdateWrapper.eq(FaCollectiveAsset::getId, faCollectiveAsset.getId());
            assetLambdaUpdateWrapper.set(FaCollectiveAsset::getRedeemTotalAmount, faCollectiveAsset.getRedeemTotalAmount().subtract(selectOne.getAmount()));
            assetLambdaUpdateWrapper.set(FaCollectiveAsset::getUpdateTime, new Date());
            iFaCollectiveAssetService.update(assetLambdaUpdateWrapper);
        }
    }
}