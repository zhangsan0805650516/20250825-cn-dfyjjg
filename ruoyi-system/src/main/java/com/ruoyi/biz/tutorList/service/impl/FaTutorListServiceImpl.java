package com.ruoyi.biz.tutorList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.tutorList.domain.FaTutorList;
import com.ruoyi.biz.tutorList.mapper.FaTutorListMapper;
import com.ruoyi.biz.tutorList.service.IFaTutorListService;
import com.ruoyi.biz.tutorVote.domain.FaTutorVote;
import com.ruoyi.biz.tutorVote.service.IFaTutorVoteService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class FaTutorListServiceImpl extends ServiceImpl<FaTutorListMapper, FaTutorList> implements IFaTutorListService
{
    @Autowired
    private FaTutorListMapper faTutorListMapper;

    @Autowired
    private IFaTutorVoteService iFaTutorVoteService;

    /**
     * 查询分类
     *
     * @param id 分类主键
     * @return 分类
     */
    @Override
    public FaTutorList selectFaTutorListById(Integer id)
    {
        return faTutorListMapper.selectFaTutorListById(id);
    }

    /**
     * 查询分类列表
     *
     * @param faTutorList 分类
     * @return 分类
     */
    @Override
    public List<FaTutorList> selectFaTutorListList(FaTutorList faTutorList)
    {
        faTutorList.setDeleteFlag(0);
        return faTutorListMapper.selectFaTutorListList(faTutorList);
    }

    /**
     * 新增分类
     *
     * @param faTutorList 分类
     * @return 结果
     */
    @Override
    public int insertFaTutorList(FaTutorList faTutorList)
    {
        faTutorList.setCreateTime(DateUtils.getNowDate());
        return faTutorListMapper.insertFaTutorList(faTutorList);
    }

    /**
     * 修改分类
     *
     * @param faTutorList 分类
     * @return 结果
     */
    @Override
    public int updateFaTutorList(FaTutorList faTutorList)
    {
        faTutorList.setUpdateTime(DateUtils.getNowDate());
        return faTutorListMapper.updateFaTutorList(faTutorList);
    }

    /**
     * 批量删除分类
     *
     * @param ids 需要删除的分类主键
     * @return 结果
     */
    @Override
    public int deleteFaTutorListByIds(Integer[] ids)
    {
        return faTutorListMapper.deleteFaTutorListByIds(ids);
    }

    /**
     * 删除分类信息
     *
     * @param id 分类主键
     * @return 结果
     */
    @Override
    public int deleteFaTutorListById(Integer id)
    {
        return faTutorListMapper.deleteFaTutorListById(id);
    }

    /**
     * 获取导师列表
     * @param faTutorList
     * @return
     * @throws Exception
     */
    @Override
    public List<FaTutorList> getTutorList(FaTutorList faTutorList) throws Exception {
        if (null == faTutorList.getUserId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 导师列表
        LambdaQueryWrapper<FaTutorList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaTutorList::getStatus, 0);
        lambdaQueryWrapper.eq(FaTutorList::getDeleteFlag, 0);
        List<FaTutorList> list = this.list(lambdaQueryWrapper);

        // 遍历是否已投票
        for (FaTutorList tutor : list) {
            LambdaQueryWrapper<FaTutorVote> tutorVoteLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tutorVoteLambdaQueryWrapper.eq(FaTutorVote::getUserId, faTutorList.getUserId());
            tutorVoteLambdaQueryWrapper.eq(FaTutorVote::getTutorId, tutor.getId());
            tutorVoteLambdaQueryWrapper.eq(FaTutorVote::getDeleteFlag, 0);
            long count = iFaTutorVoteService.count(tutorVoteLambdaQueryWrapper);
            if (count > 0) {
                tutor.setIsVote(1);
            } else {
                tutor.setIsVote(0);
            }
        }
        return list;
    }

    /**
     * 搜索导师
     * @param faTutorList
     * @return
     * @throws Exception
     */
    @Override
    public List<FaTutorList> searchTutor(FaTutorList faTutorList) throws Exception {
        LambdaQueryWrapper<FaTutorList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faTutorList.getQueryString())) {
            lambdaQueryWrapper.and(i -> i.like(FaTutorList::getTutorIdNum, faTutorList.getQueryString())
                    .or().like(FaTutorList::getTutorName, faTutorList.getQueryString()));
        }
        lambdaQueryWrapper.eq(FaTutorList::getDeleteFlag, 0);
        List<FaTutorList> list = this.list(lambdaQueryWrapper);
        return list;
    }
}