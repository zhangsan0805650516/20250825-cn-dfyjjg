package com.ruoyi.biz.sysbank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.biz.sysbank.mapper.FaSysbankMapper;
import com.ruoyi.biz.sysbank.service.IFaSysbankService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 通道Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Service
public class FaSysbankServiceImpl extends ServiceImpl<FaSysbankMapper, FaSysbank> implements IFaSysbankService
{
    @Autowired
    private FaSysbankMapper faSysbankMapper;

    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    @Override
    public FaSysbank selectFaSysbankById(Long id)
    {
        return faSysbankMapper.selectFaSysbankById(id);
    }

    /**
     * 查询通道列表
     *
     * @param faSysbank 通道
     * @return 通道
     */
    @Override
    public List<FaSysbank> selectFaSysbankList(FaSysbank faSysbank)
    {
        faSysbank.setDeleteFlag(0);
        return faSysbankMapper.selectFaSysbankList(faSysbank);
    }

    /**
     * 新增通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaSysbank(FaSysbank faSysbank)
    {
        faSysbank.setCreateTime(DateUtils.getNowDate());
        return faSysbankMapper.insertFaSysbank(faSysbank);
    }

    /**
     * 修改通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaSysbank(FaSysbank faSysbank)
    {
        faSysbank.setUpdateTime(DateUtils.getNowDate());
        return faSysbankMapper.updateFaSysbank(faSysbank);
    }

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的通道主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSysbankByIds(Long[] ids)
    {
        return faSysbankMapper.deleteFaSysbankByIds(ids);
    }

    /**
     * 删除通道信息
     *
     * @param id 通道主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaSysbankById(Long id)
    {
        return faSysbankMapper.deleteFaSysbankById(id);
    }

    /**
     * 查询支付通道
     * @param faSysbank
     * @return
     * @throws Exception
     */
    @Override
    public List<FaSysbank> getSysbankByDaili(FaSysbank faSysbank) throws Exception {
        if (null == faSysbank.getAdminId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaSysbank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSysbank::getAdminId, faSysbank.getAdminId());
        List<FaSysbank> list = this.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 查询支付通道详情
     */
    @Override
    public FaSysbank getSysbankDetail(FaSysbank faSysbank) throws Exception {
        if (null == faSysbank.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaSysbank sysbank = this.selectFaSysbankById(Long.valueOf(faSysbank.getId()));
        if (ObjectUtils.isNotEmpty(sysbank) && StringUtils.isNotEmpty(sysbank.getCkPass())) {
            if (StringUtils.isEmpty(faSysbank.getCkPass())) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }
            if (!sysbank.getCkPass().equals(faSysbank.getCkPass())) {
                throw new ServiceException(MessageUtils.message("check.password.error"), HttpStatus.ERROR);
            }
        }
        return sysbank;
    }

    /**
     * 根据密码查询支付通道
     * @param faSysbank
     * @return
     * @throws Exception
     */
    @Override
    public List<FaSysbank> getSysbankByPwd(FaSysbank faSysbank) throws Exception {
        if (null == faSysbank.getCkPass()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaSysbank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaSysbank::getCkPass, faSysbank.getCkPass());
        lambdaQueryWrapper.eq(FaSysbank::getStatus, 1);
        lambdaQueryWrapper.eq(FaSysbank::getDeleteFlag, 0);
//        lambdaQueryWrapper.orderByDesc(FaSysbank::getUpdateTime);
        List<FaSysbank> list = this.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 修改通道状态
     * @param faSysbank
     * @return
     * @throws Exception
     */
    @Override
    public AjaxResult changeSysBankStatus(FaSysbank faSysbank) throws Exception {
        LambdaUpdateWrapper<FaSysbank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaSysbank::getId, faSysbank.getId());
        lambdaUpdateWrapper.set(FaSysbank::getStatus, faSysbank.getStatus());
        lambdaUpdateWrapper.set(FaSysbank::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
        return AjaxResult.success(MessageUtils.message("operation.success"));
    }

}