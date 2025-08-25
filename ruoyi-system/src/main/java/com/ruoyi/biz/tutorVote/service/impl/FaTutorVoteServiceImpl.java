package com.ruoyi.biz.tutorVote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.biz.tutorList.service.IFaTutorListService;
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;
import com.ruoyi.biz.tutorVote.mapper.FaTutorVoteMapper;
import com.ruoyi.biz.tutorVote.service.IFaTutorVoteService;
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

import java.util.Date;
import java.util.List;

/**
 * 分类Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class FaTutorVoteServiceImpl extends ServiceImpl<FaTutorVoteMapper, FaTutorVote> implements IFaTutorVoteService
{
    @Autowired
    private FaTutorVoteMapper faTutorVoteMapper;

    @Autowired
    private IFaTutorListService iFaTutorListService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    /**
     * 查询分类
     *
     * @param id 分类主键
     * @return 分类
     */
    @Override
    public FaTutorVote selectFaTutorVoteById(Integer id)
    {
        return faTutorVoteMapper.selectFaTutorVoteById(id);
    }

    /**
     * 查询分类列表
     *
     * @param faTutorVote 分类
     * @return 分类
     */
    @Override
    public List<FaTutorVote> selectFaTutorVoteList(FaTutorVote faTutorVote)
    {
        try {
            faTutorVote.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faTutorVote.getMobile())) {
                faTutorVote.setSalt(AESUtil.encrypt(faTutorVote.getMobile()));
                faTutorVote.setMobile(null);
            }

            return faTutorVoteMapper.selectFaTutorVoteList(faTutorVote);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增分类
     *
     * @param faTutorVote 分类
     * @return 结果
     */
    @Override
    public int insertFaTutorVote(FaTutorVote faTutorVote)
    {
        faTutorVote.setCreateTime(DateUtils.getNowDate());
        return faTutorVoteMapper.insertFaTutorVote(faTutorVote);
    }

    /**
     * 修改分类
     *
     * @param faTutorVote 分类
     * @return 结果
     */
    @Override
    public int updateFaTutorVote(FaTutorVote faTutorVote)
    {
        faTutorVote.setUpdateTime(DateUtils.getNowDate());
        return faTutorVoteMapper.updateFaTutorVote(faTutorVote);
    }

    /**
     * 批量删除分类
     *
     * @param ids 需要删除的分类主键
     * @return 结果
     */
    @Override
    public int deleteFaTutorVoteByIds(Integer[] ids)
    {
        return faTutorVoteMapper.deleteFaTutorVoteByIds(ids);
    }

    /**
     * 删除分类信息
     *
     * @param id 分类主键
     * @return 结果
     */
    @Override
    public int deleteFaTutorVoteById(Integer id)
    {
        return faTutorVoteMapper.deleteFaTutorVoteById(id);
    }

    /**
     * 投票
     * @param faTutorVote
     * @throws Exception
     */
    @Override
    public void voteTutor(FaTutorVote faTutorVote) throws Exception {
        if (null == faTutorVote.getUserId() || null == faTutorVote.getTutorId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 取出用户
        FaMember faMember = iFaMemberService.getById(faTutorVote.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 取出导师
        FaTutorList faTutorList = iFaTutorListService.getById(faTutorVote.getTutorId());
        if (ObjectUtils.isEmpty(faTutorList)) {
            throw new ServiceException(MessageUtils.message("tutor.not.exist"), HttpStatus.ERROR);
        }

        // 判断余额
        if (faMember.getBalance().compareTo(faTutorList.getMoney()) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 是否已投票
        LambdaQueryWrapper<FaTutorVote> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaTutorVote::getUserId, faTutorVote.getUserId());
        lambdaQueryWrapper.eq(FaTutorVote::getTutorId, faTutorVote.getTutorId());
        lambdaQueryWrapper.eq(FaTutorVote::getDeleteFlag, 0);
        long count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new ServiceException(MessageUtils.message("already.voted"), HttpStatus.ERROR);
        }

        // 导师得票次数增加
        LambdaUpdateWrapper<FaTutorList> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaTutorList::getId, faTutorList.getId());
        lambdaUpdateWrapper.set(FaTutorList::getVoteNums, faTutorList.getVoteNums() + 1);
        lambdaUpdateWrapper.set(FaTutorList::getUpdateTime, new Date());
        iFaTutorListService.update(lambdaUpdateWrapper);

        // 保存投票
        faTutorVote.setMoney(faTutorList.getMoney());
        faTutorVote.setWeigh(faTutorList.getWeigh());
        faTutorVote.setStatus(0);
        faTutorVote.setCreateTime(new Date());
        // 用户投票
        faTutorVote.setVoteType(0);
        this.save(faTutorVote);

        // 资金流水
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VOTE_FREEZE));
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faTutorList.getMoney()));
        faCapitalLog.setMoney(faTutorList.getMoney());
        faCapitalLog.setType(CapitalFlowConstants.VOTE_FREEZE);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 更新用户余额 减少
        iFaMemberService.updateMemberBalance(faMember.getId(), faTutorList.getMoney(), 1);

        // 更新用户冻结 减少
        iFaMemberService.updateFaMemberFreezeProfit(faMember.getId(), faTutorList.getMoney(), 1, 0);
    }

    /**
     * 投票记录
     * @param faTutorVote
     * @return
     * @throws Exception
     */
    @Override
    public List<FaTutorVote> getVoteList(FaTutorVote faTutorVote) throws Exception {
        if (null == faTutorVote.getUserId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

//        LambdaQueryWrapper<FaTutorVote> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(FaTutorVote::getUserId, faTutorVote.getUserId());
//        lambdaQueryWrapper.eq(FaTutorVote::getDeleteFlag, 0);
//        lambdaQueryWrapper.orderByDesc(FaTutorVote::getCreateTime);
        List<FaTutorVote> list = this.selectFaTutorVoteList(faTutorVote);
//        if (!list.isEmpty()) {
//            // 取出用户
//            FaMember faMember = iFaMemberService.getById(faTutorVote.getUserId());
//            if (ObjectUtils.isEmpty(faMember)) {
//                throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
//            }
//
//            // 遍历获取导师
//            for (FaTutorVote vote : list) {
//                FaTutorList faTutorList = iFaTutorListService.getById(vote.getTutorId());
//                if (ObjectUtils.isNotEmpty(faTutorList)) {
//                    vote.setFaTutorList(faTutorList);
//                }
//            }
//
//        }
        return list;
    }

    /**
     * 增加用户投票
     * @param faTutorVote
     * @throws Exception
     */
    @Override
    public void submitMemberVote(FaTutorVote faTutorVote) throws Exception {
        if (null == faTutorVote.getUserId() || null == faTutorVote.getTutorId() || null == faTutorVote.getMoney() ||
                null == faTutorVote.getVoteNums() || null == faTutorVote.getStatus()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 取出用户
        FaMember faMember = iFaMemberService.getById(faTutorVote.getUserId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 取出导师
        FaTutorList faTutorList = iFaTutorListService.getById(faTutorVote.getTutorId());
        if (ObjectUtils.isEmpty(faTutorList)) {
            throw new ServiceException(MessageUtils.message("tutor.not.exist"), HttpStatus.ERROR);
        }

        // 遍历新增投票记录
        for (int i = 0; i < faTutorVote.getVoteNums(); i++) {
            FaTutorVote vote = new FaTutorVote();
            vote.setUserId(faTutorVote.getUserId());
            vote.setTutorId(faTutorVote.getTutorId());
            vote.setMoney(faTutorVote.getMoney());
            vote.setStatus(faTutorVote.getStatus());
            // 后台投票 不扣钱不退钱
            vote.setVoteType(1);
            vote.setCreateTime(new Date());
            vote.setDeleteFlag(0);
            this.save(vote);
        }

        // 增加老师票数
        LambdaUpdateWrapper<FaTutorList> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaTutorList::getId, faTutorList.getId());
        lambdaUpdateWrapper.set(FaTutorList::getVoteNums, faTutorList.getVoteNums() + faTutorVote.getVoteNums());
        lambdaUpdateWrapper.set(FaTutorList::getUpdateTime, new Date());
        iFaTutorListService.update(lambdaUpdateWrapper);
    }

    /**
     * 投票解冻
     * @param faTutorVote
     * @throws Exception
     */
    @Override
    public void unfreeze(FaTutorVote faTutorVote) throws Exception {
        if (null == faTutorVote.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faTutorVote = this.getById(faTutorVote.getId());
        if (ObjectUtils.isEmpty(faTutorVote)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 是否已解冻
        if (1 == faTutorVote.getStatus()) {
            throw new ServiceException(MessageUtils.message("vote.unfreeze.already"), HttpStatus.ERROR);
        }

        // 用户真票 解冻退钱
        if (0 == faTutorVote.getVoteType()) {
            // 取出用户
            FaMember faMember = iFaMemberService.getById(faTutorVote.getUserId());
            if (ObjectUtils.isEmpty(faMember)) {
                throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
            }

            // 资金流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(faMember.getId());
            faCapitalLog.setMobile(faMember.getMobile());
            faCapitalLog.setName(faMember.getName());
            faCapitalLog.setSuperiorId(faMember.getSuperiorId());
            faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
            faCapitalLog.setSuperiorName(faMember.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.VOTE_UNFREEZE));
            faCapitalLog.setBeforeMoney(faMember.getBalance());
            faCapitalLog.setLaterMoney(faMember.getBalance().add(faTutorVote.getMoney()));
            faCapitalLog.setMoney(faTutorVote.getMoney());
            faCapitalLog.setType(CapitalFlowConstants.VOTE_UNFREEZE);
            faCapitalLog.setDirect(0);
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额 增加
            iFaMemberService.updateMemberBalance(faMember.getId(), faTutorVote.getMoney(), 0);

        }

        // 更新状态
        LambdaUpdateWrapper<FaTutorVote> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaTutorVote::getId, faTutorVote.getId());
        lambdaUpdateWrapper.set(FaTutorVote::getStatus, 1);
        lambdaUpdateWrapper.set(FaTutorVote::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 批量解冻
     * @param ids
     * @throws Exception
     */
    @Override
    public void batchUnfreeze(Integer[] ids) throws Exception {
        if (ids.length > 0) {
            for (Integer id : ids) {
                FaTutorVote faTutorVote = new FaTutorVote();
                faTutorVote.setId(id);
                this.unfreeze(faTutorVote);
            }
        }
    }

}