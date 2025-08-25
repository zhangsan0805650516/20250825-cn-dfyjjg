package com.ruoyi.biz.searchMember.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.searchMember.domain.FaSearchMember;
import com.ruoyi.common.core.domain.entity.FaMember;

/**
 * 搜索用户记录Service接口
 *
 * @author ruoyi
 * @date 2024-12-15
 */
public interface IFaSearchMemberService extends IService<FaSearchMember>
{
    /**
     * 查询搜索用户记录
     *
     * @param id 搜索用户记录主键
     * @return 搜索用户记录
     */
    public FaSearchMember selectFaSearchMemberById(Integer id);

    /**
     * 查询搜索用户记录列表
     *
     * @param faSearchMember 搜索用户记录
     * @return 搜索用户记录集合
     */
    public List<FaSearchMember> selectFaSearchMemberList(FaSearchMember faSearchMember);

    /**
     * 新增搜索用户记录
     *
     * @param faSearchMember 搜索用户记录
     * @return 结果
     */
    public int insertFaSearchMember(FaSearchMember faSearchMember);

    /**
     * 修改搜索用户记录
     *
     * @param faSearchMember 搜索用户记录
     * @return 结果
     */
    public int updateFaSearchMember(FaSearchMember faSearchMember);

    /**
     * 批量删除搜索用户记录
     *
     * @param ids 需要删除的搜索用户记录主键集合
     * @return 结果
     */
    public int deleteFaSearchMemberByIds(Integer[] ids);

    /**
     * 删除搜索用户记录信息
     *
     * @param id 搜索用户记录主键
     * @return 结果
     */
    public int deleteFaSearchMemberById(Integer id);

    /**
     * 搜索用户
     * @param faSearchMember
     * @return
     */
    List<FaMember> getSearchList(FaSearchMember faSearchMember) throws Exception;

    /**
     * 剩余搜索次数
     * @return
     * @throws Exception
     */
    int getSearchTimesLeft() throws Exception;

    /**
     * 会员搜索次数归零
     * @throws Exception
     */
    void updateSearchTimes() throws Exception;
}