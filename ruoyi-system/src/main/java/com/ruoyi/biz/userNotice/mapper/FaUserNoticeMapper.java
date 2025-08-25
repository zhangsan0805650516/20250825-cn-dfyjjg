package com.ruoyi.biz.userNotice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.userNotice.domain.FaUserNotice;

import java.util.List;

/**
 * 用户消息Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface FaUserNoticeMapper extends BaseMapper<FaUserNotice>
{
    /**
     * 查询用户消息
     *
     * @param id 用户消息主键
     * @return 用户消息
     */
    public FaUserNotice selectFaUserNoticeById(Integer id);

    /**
     * 查询用户消息列表
     *
     * @param faUserNotice 用户消息
     * @return 用户消息集合
     */
    public List<FaUserNotice> selectFaUserNoticeList(FaUserNotice faUserNotice);

    /**
     * 新增用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    public int insertFaUserNotice(FaUserNotice faUserNotice);

    /**
     * 修改用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    public int updateFaUserNotice(FaUserNotice faUserNotice);

    /**
     * 删除用户消息
     *
     * @param id 用户消息主键
     * @return 结果
     */
    public int deleteFaUserNoticeById(Integer id);

    /**
     * 批量删除用户消息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaUserNoticeByIds(Integer[] ids);
}