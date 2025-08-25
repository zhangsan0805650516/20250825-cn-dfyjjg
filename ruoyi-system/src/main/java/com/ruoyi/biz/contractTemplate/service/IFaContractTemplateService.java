package com.ruoyi.biz.contractTemplate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.contractTemplate.domain.FaContractTemplate;

import java.util.List;

/**
 * 合同模板Service接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface IFaContractTemplateService extends IService<FaContractTemplate>
{
    /**
     * 查询合同模板
     *
     * @param id 合同模板主键
     * @return 合同模板
     */
    public FaContractTemplate selectFaContractTemplateById(Integer id);

    /**
     * 查询合同模板列表
     *
     * @param faContractTemplate 合同模板
     * @return 合同模板集合
     */
    public List<FaContractTemplate> selectFaContractTemplateList(FaContractTemplate faContractTemplate);

    /**
     * 新增合同模板
     *
     * @param faContractTemplate 合同模板
     * @return 结果
     */
    public int insertFaContractTemplate(FaContractTemplate faContractTemplate);

    /**
     * 修改合同模板
     *
     * @param faContractTemplate 合同模板
     * @return 结果
     */
    public int updateFaContractTemplate(FaContractTemplate faContractTemplate);

    /**
     * 批量删除合同模板
     *
     * @param ids 需要删除的合同模板主键集合
     * @return 结果
     */
    public int deleteFaContractTemplateByIds(Integer[] ids);

    /**
     * 删除合同模板信息
     *
     * @param id 合同模板主键
     * @return 结果
     */
    public int deleteFaContractTemplateById(Integer id);
}