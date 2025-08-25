package com.ruoyi.biz.contractTemplate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.contractTemplate.domain.FaContractTemplate;
import com.ruoyi.biz.contractTemplate.mapper.FaContractTemplateMapper;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同模板Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Service
public class FaContractTemplateServiceImpl extends ServiceImpl<FaContractTemplateMapper, FaContractTemplate> implements IFaContractTemplateService
{
    @Autowired
    private FaContractTemplateMapper faContractTemplateMapper;

    /**
     * 查询合同模板
     *
     * @param id 合同模板主键
     * @return 合同模板
     */
    @Override
    public FaContractTemplate selectFaContractTemplateById(Integer id)
    {
        return faContractTemplateMapper.selectFaContractTemplateById(id);
    }

    /**
     * 查询合同模板列表
     *
     * @param faContractTemplate 合同模板
     * @return 合同模板
     */
    @Override
    public List<FaContractTemplate> selectFaContractTemplateList(FaContractTemplate faContractTemplate)
    {
        faContractTemplate.setDeleteFlag(0);
        return faContractTemplateMapper.selectFaContractTemplateList(faContractTemplate);
    }

    /**
     * 新增合同模板
     *
     * @param faContractTemplate 合同模板
     * @return 结果
     */
    @Override
    public int insertFaContractTemplate(FaContractTemplate faContractTemplate)
    {
        faContractTemplate.setCreateTime(DateUtils.getNowDate());
        return faContractTemplateMapper.insertFaContractTemplate(faContractTemplate);
    }

    /**
     * 修改合同模板
     *
     * @param faContractTemplate 合同模板
     * @return 结果
     */
    @Override
    public int updateFaContractTemplate(FaContractTemplate faContractTemplate)
    {
        faContractTemplate.setUpdateTime(DateUtils.getNowDate());
        return faContractTemplateMapper.updateFaContractTemplate(faContractTemplate);
    }

    /**
     * 批量删除合同模板
     *
     * @param ids 需要删除的合同模板主键
     * @return 结果
     */
    @Override
    public int deleteFaContractTemplateByIds(Integer[] ids)
    {
        return faContractTemplateMapper.deleteFaContractTemplateByIds(ids);
    }

    /**
     * 删除合同模板信息
     *
     * @param id 合同模板主键
     * @return 结果
     */
    @Override
    public int deleteFaContractTemplateById(Integer id)
    {
        return faContractTemplateMapper.deleteFaContractTemplateById(id);
    }
}