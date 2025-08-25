package com.ruoyi.biz.searchMember.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.member.mapper.FaMemberMapper;
import com.ruoyi.biz.searchMember.domain.FaSearchMember;
import com.ruoyi.biz.searchMember.mapper.FaSearchMemberMapper;
import com.ruoyi.biz.searchMember.service.IFaSearchMemberService;
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

import java.util.Date;
import java.util.List;

/**
 * 搜索用户记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-15
 */
@Service
public class FaSearchMemberServiceImpl extends ServiceImpl<FaSearchMemberMapper, FaSearchMember> implements IFaSearchMemberService
{
    @Autowired
    private FaSearchMemberMapper faSearchMemberMapper;

    @Autowired
    private FaMemberMapper faMemberMapper;

    /**
     * 查询搜索用户记录
     *
     * @param id 搜索用户记录主键
     * @return 搜索用户记录
     */
    @Override
    public FaSearchMember selectFaSearchMemberById(Integer id)
    {
        return faSearchMemberMapper.selectFaSearchMemberById(id);
    }

    /**
     * 查询搜索用户记录列表
     *
     * @param faSearchMember 搜索用户记录
     * @return 搜索用户记录
     */
    @Override
    public List<FaSearchMember> selectFaSearchMemberList(FaSearchMember faSearchMember)
    {
        faSearchMember.setDeleteFlag(0);
        return faSearchMemberMapper.selectFaSearchMemberList(faSearchMember);
    }

    /**
     * 新增搜索用户记录
     *
     * @param faSearchMember 搜索用户记录
     * @return 结果
     */
    @Override
    public int insertFaSearchMember(FaSearchMember faSearchMember)
    {
        faSearchMember.setCreateTime(DateUtils.getNowDate());
        return faSearchMemberMapper.insertFaSearchMember(faSearchMember);
    }

    /**
     * 修改搜索用户记录
     *
     * @param faSearchMember 搜索用户记录
     * @return 结果
     */
    @Override
    public int updateFaSearchMember(FaSearchMember faSearchMember)
    {
        faSearchMember.setUpdateTime(DateUtils.getNowDate());
        return faSearchMemberMapper.updateFaSearchMember(faSearchMember);
    }

    /**
     * 批量删除搜索用户记录
     *
     * @param ids 需要删除的搜索用户记录主键
     * @return 结果
     */
    @Override
    public int deleteFaSearchMemberByIds(Integer[] ids)
    {
        return faSearchMemberMapper.deleteFaSearchMemberByIds(ids);
    }

    /**
     * 删除搜索用户记录信息
     *
     * @param id 搜索用户记录主键
     * @return 结果
     */
    @Override
    public int deleteFaSearchMemberById(Integer id)
    {
        return faSearchMemberMapper.deleteFaSearchMemberById(id);
    }

    /**
     * 搜索用户
     * @param faSearchMember
     * @return
     */
    @Override
    public List<FaMember> getSearchList(FaSearchMember faSearchMember) throws Exception {
        // 搜索密码
        if (StringUtils.isEmpty(faSearchMember.getSearchPassword())) {
            throw new ServiceException(MessageUtils.message("password.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaSearchMember> searchMemberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        searchMemberLambdaQueryWrapper.eq(FaSearchMember::getSearchPassword, faSearchMember.getSearchPassword());
        searchMemberLambdaQueryWrapper.eq(FaSearchMember::getDeleteFlag, 0);
        FaSearchMember selectOne = this.getOne(searchMemberLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("password.error"), HttpStatus.ERROR);
        }

        // 使用次数 > 搜索次数
        if (selectOne.getUseTimes() >= selectOne.getSearchTimes()) {
            throw new ServiceException("搜索次数用完", HttpStatus.ERROR);
        }

        if (StringUtils.isEmpty(faSearchMember.getName()) && StringUtils.isEmpty(faSearchMember.getMobile()) && StringUtils.isEmpty(faSearchMember.getWeiyima())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户列表
        LambdaQueryWrapper<FaMember> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faSearchMember.getName())) {
            memberLambdaQueryWrapper.eq(FaMember::getName, faSearchMember.getName());
        }
        if (StringUtils.isNotEmpty(faSearchMember.getMobile())) {
            memberLambdaQueryWrapper.eq(FaMember::getSalt, AESUtil.encrypt(faSearchMember.getMobile()));
        }
        if (StringUtils.isNotEmpty(faSearchMember.getWeiyima())) {
            memberLambdaQueryWrapper.eq(FaMember::getWeiyima, faSearchMember.getWeiyima());
        }
        memberLambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        List<FaMember> memberList = faMemberMapper.selectList(memberLambdaQueryWrapper);
        if (!memberList.isEmpty()) {
            for (FaMember member : memberList) {
                member.setMobile(AESUtil.decrypt(member.getSalt()));
            }

            // 搜索次数加一
            LambdaUpdateWrapper<FaSearchMember> searchMemberLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            searchMemberLambdaUpdateWrapper.eq(FaSearchMember::getId, selectOne.getId());
            searchMemberLambdaUpdateWrapper.set(FaSearchMember::getUseTimes, selectOne.getUseTimes() + 1);
            searchMemberLambdaUpdateWrapper.set(FaSearchMember::getUpdateTime, new Date());
            this.update(searchMemberLambdaUpdateWrapper);
        } else {
            throw new ServiceException("查无数据", HttpStatus.ERROR);
        }

        return memberList;
    }

    /**
     * 剩余搜索次数
     * @return
     * @throws Exception
     */
    @Override
    public int getSearchTimesLeft() throws Exception {
        LambdaQueryWrapper<FaSearchMember> searchMemberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        searchMemberLambdaQueryWrapper.eq(FaSearchMember::getDeleteFlag, 0);
        FaSearchMember faSearchMember = this.getOne(searchMemberLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faSearchMember)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        return faSearchMember.getSearchTimes() - faSearchMember.getUseTimes();
    }

    /**
     * 会员搜索次数归零
     * @throws Exception
     */
    @Override
    public void updateSearchTimes() throws Exception {
        LambdaUpdateWrapper<FaSearchMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSearchMember::getId, 1);
        lambdaUpdateWrapper.set(FaSearchMember::getUseTimes, 0);
        lambdaUpdateWrapper.set(FaSearchMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

}