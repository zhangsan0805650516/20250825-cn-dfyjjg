package com.ruoyi.biz.contractList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.contractList.domain.FaContractList;

import java.util.List;

/**
 * 用户合同Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface FaContractListMapper extends BaseMapper<FaContractList>
{
    /**
     * 查询用户合同
     *
     * @param id 用户合同主键
     * @return 用户合同
     */
    public FaContractList selectFaContractListById(Integer id);

    /**
     * 查询用户合同列表
     *
     * @param faContractList 用户合同
     * @return 用户合同集合
     */
    public List<FaContractList> selectFaContractListList(FaContractList faContractList);

    /**
     * 新增用户合同
     *
     * @param faContractList 用户合同
     * @return 结果
     */
    public int insertFaContractList(FaContractList faContractList);

    /**
     * 修改用户合同
     *
     * @param faContractList 用户合同
     * @return 结果
     */
    public int updateFaContractList(FaContractList faContractList);

    /**
     * 删除用户合同
     *
     * @param id 用户合同主键
     * @return 结果
     */
    public int deleteFaContractListById(Integer id);

    /**
     * 批量删除用户合同
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaContractListByIds(Integer[] ids);
}