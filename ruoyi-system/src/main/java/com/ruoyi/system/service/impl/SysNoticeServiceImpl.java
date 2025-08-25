package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.memberSysNotice.domain.FaMemberSysNotice;
import com.ruoyi.biz.memberSysNotice.service.IFaMemberSysNoticeService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper noticeMapper;

    @Autowired
    private IFaMemberSysNoticeService iFaMemberSysNoticeService;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }

    /**
     * 查询系统公告列表
     * @param sysNotice
     * @return
     * @throws Exception
     */
    @Override
    public List<SysNotice> getSystemNotice(SysNotice sysNotice) throws Exception {
        List<SysNotice> list = noticeMapper.selectNoticeList(sysNotice);
        for (SysNotice notice : list) {
            LambdaQueryWrapper<FaMemberSysNotice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMemberSysNotice::getNoticeId, notice.getNoticeId());
            lambdaQueryWrapper.eq(FaMemberSysNotice::getMemberId, sysNotice.getMemberId());
            lambdaQueryWrapper.eq(FaMemberSysNotice::getDeleteFlag, 0);
            long count = iFaMemberSysNoticeService.count(lambdaQueryWrapper);
            if (count > 0) {
                notice.setIsRead(1);
            } else {
                notice.setIsRead(0);
            }
        }
        return list;
    }

    /**
     * 阅读公告
     * @param sysNotice
     * @throws Exception
     */
    @Override
    public void readNotice(SysNotice sysNotice) throws Exception {
        if (null == sysNotice.getNoticeId() || null == sysNotice.getMemberId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaMemberSysNotice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMemberSysNotice::getNoticeId, sysNotice.getNoticeId());
        lambdaQueryWrapper.eq(FaMemberSysNotice::getMemberId, sysNotice.getMemberId());
        lambdaQueryWrapper.eq(FaMemberSysNotice::getDeleteFlag, 0);
        long count = iFaMemberSysNoticeService.count(lambdaQueryWrapper);
        if (count == 0) {
            FaMemberSysNotice faMemberSysNotice = new FaMemberSysNotice();
            faMemberSysNotice.setNoticeId(sysNotice.getNoticeId().intValue());
            faMemberSysNotice.setMemberId(sysNotice.getMemberId());
            faMemberSysNotice.setCreateTime(new Date());
            faMemberSysNotice.setDeleteFlag(0);
            iFaMemberSysNoticeService.save(faMemberSysNotice);
        }
    }
}
