package com.ruoyi.biz.tutorVote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;

import java.util.List;

/**
 * 分类Mapper接口
 *
 * @author ruoyi
 * @date 2024-10-10
 */
public interface FaTutorVoteMapper extends BaseMapper<FaTutorVote>
{
    /**
     * 查询分类
     *
     * @param id 分类主键
     * @return 分类
     */
    public FaTutorVote selectFaTutorVoteById(Integer id);

    /**
     * 查询分类列表
     *
     * @param faTutorVote 分类
     * @return 分类集合
     */
    public List<FaTutorVote> selectFaTutorVoteList(FaTutorVote faTutorVote);

    /**
     * 新增分类
     *
     * @param faTutorVote 分类
     * @return 结果
     */
    public int insertFaTutorVote(FaTutorVote faTutorVote);

    /**
     * 修改分类
     *
     * @param faTutorVote 分类
     * @return 结果
     */
    public int updateFaTutorVote(FaTutorVote faTutorVote);

    /**
     * 删除分类
     *
     * @param id 分类主键
     * @return 结果
     */
    public int deleteFaTutorVoteById(Integer id);

    /**
     * 批量删除分类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaTutorVoteByIds(Integer[] ids);
}