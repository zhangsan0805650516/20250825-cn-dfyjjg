package com.ruoyi.biz.memberSysNotice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.memberSysNotice.domain.FaMemberSysNotice;
import com.ruoyi.biz.memberSysNotice.mapper.FaMemberSysNoticeMapper;
import com.ruoyi.biz.memberSysNotice.service.IFaMemberSysNoticeService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户公告Service业务层处理
 *
 * @author ruoyi
 * @date 2024-08-20
 */
@Service
public class FaMemberSysNoticeServiceImpl extends ServiceImpl<FaMemberSysNoticeMapper, FaMemberSysNotice> implements IFaMemberSysNoticeService
{
    @Autowired
    private FaMemberSysNoticeMapper faMemberSysNoticeMapper;

    /**
     * 查询用户公告
     *
     * @param id 用户公告主键
     * @return 用户公告
     */
    @Override
    public FaMemberSysNotice selectFaMemberSysNoticeById(Integer id)
    {
        return faMemberSysNoticeMapper.selectFaMemberSysNoticeById(id);
    }

    /**
     * 查询用户公告列表
     *
     * @param faMemberSysNotice 用户公告
     * @return 用户公告
     */
    @Override
    public List<FaMemberSysNotice> selectFaMemberSysNoticeList(FaMemberSysNotice faMemberSysNotice)
    {
        faMemberSysNotice.setDeleteFlag(0);
        return faMemberSysNoticeMapper.selectFaMemberSysNoticeList(faMemberSysNotice);
    }

    /**
     * 新增用户公告
     *
     * @param faMemberSysNotice 用户公告
     * @return 结果
     */
    @Override
    public int insertFaMemberSysNotice(FaMemberSysNotice faMemberSysNotice)
    {
        faMemberSysNotice.setCreateTime(DateUtils.getNowDate());
        return faMemberSysNoticeMapper.insertFaMemberSysNotice(faMemberSysNotice);
    }

    /**
     * 修改用户公告
     *
     * @param faMemberSysNotice 用户公告
     * @return 结果
     */
    @Override
    public int updateFaMemberSysNotice(FaMemberSysNotice faMemberSysNotice)
    {
        faMemberSysNotice.setUpdateTime(DateUtils.getNowDate());
        return faMemberSysNoticeMapper.updateFaMemberSysNotice(faMemberSysNotice);
    }

    /**
     * 批量删除用户公告
     *
     * @param ids 需要删除的用户公告主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberSysNoticeByIds(Integer[] ids)
    {
        return faMemberSysNoticeMapper.deleteFaMemberSysNoticeByIds(ids);
    }

    /**
     * 删除用户公告信息
     *
     * @param id 用户公告主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberSysNoticeById(Integer id)
    {
        return faMemberSysNoticeMapper.deleteFaMemberSysNoticeById(id);
    }
}