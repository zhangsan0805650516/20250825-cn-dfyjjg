package com.ruoyi.biz.contractList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.contractList.domain.FaContractList;
import com.ruoyi.biz.contractList.mapper.FaContractListMapper;
import com.ruoyi.biz.contractList.service.IFaContractListService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户合同Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Service
public class FaContractListServiceImpl extends ServiceImpl<FaContractListMapper, FaContractList> implements IFaContractListService
{
    @Autowired
    private FaContractListMapper faContractListMapper;

    /**
     * 查询用户合同
     *
     * @param id 用户合同主键
     * @return 用户合同
     */
    @Override
    public FaContractList selectFaContractListById(Integer id)
    {
        return faContractListMapper.selectFaContractListById(id);
    }

    /**
     * 查询用户合同列表
     *
     * @param faContractList 用户合同
     * @return 用户合同
     */
    @Override
    public List<FaContractList> selectFaContractListList(FaContractList faContractList)
    {
        faContractList.setDeleteFlag(0);
        return faContractListMapper.selectFaContractListList(faContractList);
    }

    /**
     * 新增用户合同
     *
     * @param faContractList 用户合同
     * @return 结果
     */
    @Override
    public int insertFaContractList(FaContractList faContractList)
    {
        faContractList.setCreateTime(DateUtils.getNowDate());
        return faContractListMapper.insertFaContractList(faContractList);
    }

    /**
     * 修改用户合同
     *
     * @param faContractList 用户合同
     * @return 结果
     */
    @Override
    public int updateFaContractList(FaContractList faContractList)
    {
        faContractList.setUpdateTime(DateUtils.getNowDate());
        return faContractListMapper.updateFaContractList(faContractList);
    }

    /**
     * 批量删除用户合同
     *
     * @param ids 需要删除的用户合同主键
     * @return 结果
     */
    @Override
    public int deleteFaContractListByIds(Integer[] ids)
    {
        return faContractListMapper.deleteFaContractListByIds(ids);
    }

    /**
     * 删除用户合同信息
     *
     * @param id 用户合同主键
     * @return 结果
     */
    @Override
    public int deleteFaContractListById(Integer id)
    {
        return faContractListMapper.deleteFaContractListById(id);
    }

    /**
     * 查询用户合同列表
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public List<FaContractList> getContractList(int id) throws Exception {
        LambdaQueryWrapper<FaContractList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaContractList::getUserId, id);
        lambdaQueryWrapper.eq(FaContractList::getDeleteFlag, 0);
        List<FaContractList> list = this.list(lambdaQueryWrapper);
        return list;
    }
}