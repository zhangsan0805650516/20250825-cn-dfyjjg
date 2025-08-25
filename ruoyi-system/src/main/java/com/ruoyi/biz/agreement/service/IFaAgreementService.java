package com.ruoyi.biz.agreement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.agreement.domain.FaAgreement;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 关闭通道协议Service接口
 *
 * @author ruoyi
 * @date 2024-01-08
 */
public interface IFaAgreementService extends IService<FaAgreement>
{
    /**
     * 查询关闭通道协议
     *
     * @param id 关闭通道协议主键
     * @return 关闭通道协议
     */
    public FaAgreement selectFaAgreementById(Integer id);

    /**
     * 查询关闭通道协议列表
     *
     * @param faAgreement 关闭通道协议
     * @return 关闭通道协议集合
     */
    public List<FaAgreement> selectFaAgreementList(FaAgreement faAgreement);

    /**
     * 新增关闭通道协议
     *
     * @param faAgreement 关闭通道协议
     * @return 结果
     */
    public int insertFaAgreement(FaAgreement faAgreement);

    /**
     * 修改关闭通道协议
     *
     * @param faAgreement 关闭通道协议
     * @return 结果
     */
    public int updateFaAgreement(FaAgreement faAgreement);

    /**
     * 批量删除关闭通道协议
     *
     * @param ids 需要删除的关闭通道协议主键集合
     * @return 结果
     */
    public int deleteFaAgreementByIds(Integer[] ids);

    /**
     * 删除关闭通道协议信息
     *
     * @param id 关闭通道协议主键
     * @return 结果
     */
    public int deleteFaAgreementById(Integer id);

    /**
     * 获取数据
     */
    AjaxResult getAgreementData() throws Exception;

}