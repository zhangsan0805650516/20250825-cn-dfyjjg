package com.ruoyi.biz.tutorVote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;

import java.util.List;

/**
 * 分类Service接口
 *
 * @author ruoyi
 * @date 2024-10-10
 */
public interface IFaTutorVoteService extends IService<FaTutorVote>
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
     * 批量删除分类
     *
     * @param ids 需要删除的分类主键集合
     * @return 结果
     */
    public int deleteFaTutorVoteByIds(Integer[] ids);

    /**
     * 删除分类信息
     *
     * @param id 分类主键
     * @return 结果
     */
    public int deleteFaTutorVoteById(Integer id);

    /**
     * 投票
     * @param faTutorVote
     * @throws Exception
     */
    void voteTutor(FaTutorVote faTutorVote) throws Exception;

    /**
     * 投票记录
     * @param faTutorVote
     * @return
     * @throws Exception
     */
    List<FaTutorVote> getVoteList(FaTutorVote faTutorVote) throws Exception;

    /**
     * 增加用户投票
     * @param faTutorVote
     * @throws Exception
     */
    void submitMemberVote(FaTutorVote faTutorVote) throws Exception;

    /**
     * 投票解冻
     * @param faTutorVote
     * @throws Exception
     */
    void unfreeze(FaTutorVote faTutorVote) throws Exception;

    /**
     * 批量解冻
     * @param ids
     * @throws Exception
     */
    void batchUnfreeze(Integer[] ids) throws Exception;
}