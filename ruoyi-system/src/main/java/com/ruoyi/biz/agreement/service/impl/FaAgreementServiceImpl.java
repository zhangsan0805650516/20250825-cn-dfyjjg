package com.ruoyi.biz.agreement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.agreement.domain.FaAgreement;
import com.ruoyi.biz.agreement.mapper.FaAgreementMapper;
import com.ruoyi.biz.agreement.service.IFaAgreementService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 关闭通道协议Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-08
 */
@Service
public class FaAgreementServiceImpl extends ServiceImpl<FaAgreementMapper, FaAgreement> implements IFaAgreementService
{
    @Autowired
    private FaAgreementMapper faAgreementMapper;

    /**
     * 查询关闭通道协议
     *
     * @param id 关闭通道协议主键
     * @return 关闭通道协议
     */
    @Override
    public FaAgreement selectFaAgreementById(Integer id)
    {
        return faAgreementMapper.selectFaAgreementById(id);
    }

    /**
     * 查询关闭通道协议列表
     *
     * @param faAgreement 关闭通道协议
     * @return 关闭通道协议
     */
    @Override
    public List<FaAgreement> selectFaAgreementList(FaAgreement faAgreement)
    {
        faAgreement.setDeleteFlag(0);
        return faAgreementMapper.selectFaAgreementList(faAgreement);
    }

    /**
     * 新增关闭通道协议
     *
     * @param faAgreement 关闭通道协议
     * @return 结果
     */
    @Override
    public int insertFaAgreement(FaAgreement faAgreement)
    {
        faAgreement.setCreateTime(DateUtils.getNowDate());
        return faAgreementMapper.insertFaAgreement(faAgreement);
    }

    /**
     * 修改关闭通道协议
     *
     * @param faAgreement 关闭通道协议
     * @return 结果
     */
    @Override
    public int updateFaAgreement(FaAgreement faAgreement)
    {
        faAgreement.setUpdateTime(DateUtils.getNowDate());
        return faAgreementMapper.updateFaAgreement(faAgreement);
    }

    /**
     * 批量删除关闭通道协议
     *
     * @param ids 需要删除的关闭通道协议主键
     * @return 结果
     */
    @Override
    public int deleteFaAgreementByIds(Integer[] ids)
    {
        return faAgreementMapper.deleteFaAgreementByIds(ids);
    }

    /**
     * 删除关闭通道协议信息
     *
     * @param id 关闭通道协议主键
     * @return 结果
     */
    @Override
    public int deleteFaAgreementById(Integer id)
    {
        return faAgreementMapper.deleteFaAgreementById(id);
    }

    /**
     * 获取数据
     */
    @Override
    public AjaxResult getAgreementData() throws Exception {
        FaAgreement faAgreement = this.getById(2);
        if (ObjectUtils.isEmpty(faAgreement)) {
            return AjaxResult.error("数据id错误");
        } else {
            return AjaxResult.success(faAgreement);
        }
    }

}