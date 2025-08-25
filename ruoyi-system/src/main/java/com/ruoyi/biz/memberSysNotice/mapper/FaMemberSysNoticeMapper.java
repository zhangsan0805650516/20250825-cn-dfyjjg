package com.ruoyi.biz.memberSysNotice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.memberSysNotice.domain.FaMemberSysNotice;

import java.util.List;

/**
 * 用户公告Mapper接口
 *
 * @author ruoyi
 * @date 2024-08-20
 */
public interface FaMemberSysNoticeMapper extends BaseMapper<FaMemberSysNotice>
{
    /**
     * 查询用户公告
     *
     * @param id 用户公告主键
     * @return 用户公告
     */
    public FaMemberSysNotice selectFaMemberSysNoticeById(Integer id);

    /**
     * 查询用户公告列表
     *
     * @param faMemberSysNotice 用户公告
     * @return 用户公告集合
     */
    public List<FaMemberSysNotice> selectFaMemberSysNoticeList(FaMemberSysNotice faMemberSysNotice);

    /**
     * 新增用户公告
     *
     * @param faMemberSysNotice 用户公告
     * @return 结果
     */
    public int insertFaMemberSysNotice(FaMemberSysNotice faMemberSysNotice);

    /**
     * 修改用户公告
     *
     * @param faMemberSysNotice 用户公告
     * @return 结果
     */
    public int updateFaMemberSysNotice(FaMemberSysNotice faMemberSysNotice);

    /**
     * 删除用户公告
     *
     * @param id 用户公告主键
     * @return 结果
     */
    public int deleteFaMemberSysNoticeById(Integer id);

    /**
     * 批量删除用户公告
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaMemberSysNoticeByIds(Integer[] ids);
}