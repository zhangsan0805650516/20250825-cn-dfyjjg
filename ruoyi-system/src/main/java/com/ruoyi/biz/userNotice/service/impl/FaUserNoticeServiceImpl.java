package com.ruoyi.biz.userNotice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.userNotice.domain.FaUserNotice;
import com.ruoyi.biz.userNotice.mapper.FaUserNoticeMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户消息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Service
public class FaUserNoticeServiceImpl extends ServiceImpl<FaUserNoticeMapper, FaUserNotice> implements IFaUserNoticeService
{
    @Autowired
    private FaUserNoticeMapper faUserNoticeMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    /**
     * 查询用户消息
     *
     * @param id 用户消息主键
     * @return 用户消息
     */
    @Override
    public FaUserNotice selectFaUserNoticeById(Integer id)
    {
        return faUserNoticeMapper.selectFaUserNoticeById(id);
    }

    /**
     * 查询用户消息列表
     *
     * @param faUserNotice 用户消息
     * @return 用户消息
     */
    @Override
    public List<FaUserNotice> selectFaUserNoticeList(FaUserNotice faUserNotice)
    {
        try {
            faUserNotice.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faUserNotice.getMobile())) {
                faUserNotice.setSalt(AESUtil.encrypt(faUserNotice.getMobile()));
                faUserNotice.setMobile(null);
            }

            return faUserNoticeMapper.selectFaUserNoticeList(faUserNotice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaUserNotice(FaUserNotice faUserNotice) throws Exception
    {
        if (StringUtils.isEmpty(faUserNotice.getTitle()) || StringUtils.isEmpty(faUserNotice.getContent())
                || null == faUserNotice.getIsAll()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        if (0 == faUserNotice.getIsAll() && null == faUserNotice.getUserId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        if (0 == faUserNotice.getIsAll()) {
            faUserNotice.setNoticeType(0);
            faUserNotice.setCreateTime(new Date());
            faUserNoticeMapper.insertFaUserNotice(faUserNotice);
        } else if (1 == faUserNotice.getIsAll()) {
            // 取出所有用户
            LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
            List<FaMember> list = iFaMemberService.list(lambdaQueryWrapper);
            if (!list.isEmpty()) {
                FaUserNotice notice;
                for (FaMember member : list) {
                    notice = new FaUserNotice();
                    notice.setUserId(member.getId());
                    notice.setTitle(faUserNotice.getTitle());
                    notice.setContent(faUserNotice.getContent());
                    notice.setNoticeType(0);
                    notice.setIsAll(faUserNotice.getIsAll());
                    notice.setCreateTime(new Date());
                    faUserNoticeMapper.insertFaUserNotice(notice);
                }
            }
        }
        return 1;
    }

    /**
     * 修改用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaUserNotice(FaUserNotice faUserNotice)
    {
        faUserNotice.setUpdateTime(DateUtils.getNowDate());
        return faUserNoticeMapper.updateFaUserNotice(faUserNotice);
    }

    /**
     * 批量删除用户消息
     *
     * @param ids 需要删除的用户消息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaUserNoticeByIds(Integer[] ids)
    {
        return faUserNoticeMapper.deleteFaUserNoticeByIds(ids);
    }

    /**
     * 删除用户消息信息
     *
     * @param id 用户消息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaUserNoticeById(Integer id)
    {
        return faUserNoticeMapper.deleteFaUserNoticeById(id);
    }

    /**
     * 发申购中签通知
     * @param faSgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void addAllocation(FaSgList faSgList) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(faSgList.getUserId());
        faUserNotice.setTitle("中签通知");
        faUserNotice.setContent("您已中签股票" + faSgList.getName() + "，请缴款！");
        faUserNotice.setNoticeType(1);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 发配售中签通知(申购=配售)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Transactional
    @Override
    public void addAllocation(FaStockSgSecond faStockSgSecond) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(faStockSgSecond.getUserId());
        faUserNotice.setTitle("中签通知");
        faUserNotice.setContent("您已中签股票" + faStockSgSecond.getName() + "，请缴款！");
        faUserNotice.setNoticeType(1);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 发配售中签通知
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void addAllocation(FaSgjiaoyi faSgjiaoyi) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(faSgjiaoyi.getUserId());
        faUserNotice.setTitle("中签通知");
        faUserNotice.setContent("您已中签股票" + faSgjiaoyi.getName() + "！");
        faUserNotice.setNoticeType(1);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 发认缴通知
     * @param sgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void addSubscriptionBg(FaSgList sgList) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(sgList.getUserId());
        faUserNotice.setTitle("认缴通知");
        faUserNotice.setContent("您已认缴股票" + sgList.getName() + "！");
        faUserNotice.setNoticeType(2);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 发配售中签认缴通知(申购=配售)
     * @param faStockSgSecond
     * @throws Exception
     */
    @Transactional
    @Override
    public void addSubscriptionBg(FaStockSgSecond faStockSgSecond) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(faStockSgSecond.getUserId());
        faUserNotice.setTitle("认缴通知");
        faUserNotice.setContent("您已认缴股票" + faStockSgSecond.getName() + "！");
        faUserNotice.setNoticeType(2);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 发配售中签认缴通知
     * @param sgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void addSubscription(FaSgjiaoyi sgjiaoyi) throws Exception {
        FaUserNotice faUserNotice = new FaUserNotice();
        faUserNotice.setUserId(sgjiaoyi.getUserId());
        faUserNotice.setTitle("认缴通知");
        faUserNotice.setContent("您已认缴股票" + sgjiaoyi.getName() + "！");
        faUserNotice.setNoticeType(2);
        faUserNotice.setCreateTime(new Date());
        faUserNotice.setIsRead(0);
        faUserNotice.setIsAll(0);
        faUserNotice.setIsDialog(1);
        faUserNotice.setDeleteFlag(0);
        this.insertFaUserNotice(faUserNotice);
    }

    /**
     * 查询用户消息列表
     * @param faUserNotice
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaUserNotice> getMemberSgPage(FaUserNotice faUserNotice) throws Exception {
        LambdaQueryWrapper<FaUserNotice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaUserNotice::getUserId, faUserNotice.getUserId());
        lambdaQueryWrapper.eq(FaUserNotice::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaUserNotice::getCreateTime);
        IPage<FaUserNotice> faUserNoticeIPage = this.page(new Page<>(faUserNotice.getPage(), faUserNotice.getSize()), lambdaQueryWrapper);
        return faUserNoticeIPage;
    }

    /**
     * 阅读用户消息
     * @param faUserNotice
     * @throws Exception
     */
    @Transactional
    @Override
    public void readUserNotice(FaUserNotice faUserNotice) throws Exception {
        if (null == faUserNotice.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaUserNotice> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaUserNotice::getId, faUserNotice.getId());
        lambdaUpdateWrapper.set(FaUserNotice::getIsRead, 1);
        lambdaUpdateWrapper.set(FaUserNotice::getReadTime, new Date());
        lambdaUpdateWrapper.set(FaUserNotice::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 阅读用户消息(所有)
     * @param faUserNotice
     * @throws Exception
     */
    @Transactional
    @Override
    public void readUserNoticeAll(FaUserNotice faUserNotice) throws Exception {
        LambdaUpdateWrapper<FaUserNotice> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaUserNotice::getUserId, faUserNotice.getUserId());
        lambdaUpdateWrapper.eq(FaUserNotice::getIsRead, 0);
        lambdaUpdateWrapper.set(FaUserNotice::getIsRead, 1);
        lambdaUpdateWrapper.set(FaUserNotice::getReadTime, new Date());
        lambdaUpdateWrapper.set(FaUserNotice::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 用户消息详情
     * @param faUserNotice
     * @throws Exception
     */
    @Override
    public FaUserNotice userNoticeDetail(FaUserNotice faUserNotice) throws Exception {
        if (null == faUserNotice.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faUserNotice = this.selectFaUserNoticeById(faUserNotice.getId());
        if (ObjectUtils.isEmpty(faUserNotice)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        return faUserNotice;
    }

    /**
     * 未读消息条数
     * @param faUserNotice
     * @return
     * @throws Exception
     */
    @Override
    public long unreadNoticeNum(FaUserNotice faUserNotice) throws Exception {
        LambdaQueryWrapper<FaUserNotice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaUserNotice::getUserId, faUserNotice.getUserId());
        lambdaQueryWrapper.eq(FaUserNotice::getIsRead, 0);
        lambdaQueryWrapper.eq(FaUserNotice::getDeleteFlag, 0);
        long num = this.count(lambdaQueryWrapper);
        return num;
    }

}