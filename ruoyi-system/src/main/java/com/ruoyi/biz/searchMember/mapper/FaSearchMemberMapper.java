package com.ruoyi.biz.searchMember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.searchMember.domain.FaSearchMember;

import java.util.List;

/**
 * 搜索用户记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-15
 */
public interface FaSearchMemberMapper extends BaseMapper<FaSearchMember>
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
     * 删除搜索用户记录
     *
     * @param id 搜索用户记录主键
     * @return 结果
     */
    public int deleteFaSearchMemberById(Integer id);

    /**
     * 批量删除搜索用户记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaSearchMemberByIds(Integer[] ids);
}