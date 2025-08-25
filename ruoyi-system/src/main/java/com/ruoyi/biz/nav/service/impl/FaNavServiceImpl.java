package com.ruoyi.biz.nav.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.nav.domain.FaNav;
import com.ruoyi.biz.nav.mapper.FaNavMapper;
import com.ruoyi.biz.nav.service.IFaNavService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 导航图标Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-09
 */
@Service
public class FaNavServiceImpl extends ServiceImpl<FaNavMapper, FaNav> implements IFaNavService
{
    @Autowired
    private FaNavMapper faNavMapper;

    /**
     * 查询导航图标
     *
     * @param id 导航图标主键
     * @return 导航图标
     */
    @Override
    public FaNav selectFaNavById(Integer id)
    {
        return faNavMapper.selectFaNavById(id);
    }

    /**
     * 查询导航图标列表
     *
     * @param faNav 导航图标
     * @return 导航图标
     */
    @Override
    public List<FaNav> selectFaNavList(FaNav faNav)
    {
        faNav.setDeleteFlag(0);
        return faNavMapper.selectFaNavList(faNav);
    }

    /**
     * 新增导航图标
     *
     * @param faNav 导航图标
     * @return 结果
     */
    @Override
    public int insertFaNav(FaNav faNav)
    {
        faNav.setCreateTime(DateUtils.getNowDate());
        return faNavMapper.insertFaNav(faNav);
    }

    /**
     * 修改导航图标
     *
     * @param faNav 导航图标
     * @return 结果
     */
    @Override
    public int updateFaNav(FaNav faNav)
    {
        faNav.setUpdateTime(DateUtils.getNowDate());
        return faNavMapper.updateFaNav(faNav);
    }

    /**
     * 批量删除导航图标
     *
     * @param ids 需要删除的导航图标主键
     * @return 结果
     */
    @Override
    public int deleteFaNavByIds(Integer[] ids)
    {
        return faNavMapper.deleteFaNavByIds(ids);
    }

    /**
     * 删除导航图标信息
     *
     * @param id 导航图标主键
     * @return 结果
     */
    @Override
    public int deleteFaNavById(Integer id)
    {
        return faNavMapper.deleteFaNavById(id);
    }

    /**
     * 查询导航图标
     * @param faNav
     * @return
     * @throws Exception
     */
    @Override
    public List<FaNav> getNavList(FaNav faNav) throws Exception {
        if (null == faNav.getType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaNav> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNav::getDeleteFlag, 0);
        lambdaQueryWrapper.eq(FaNav::getStatus, 0);
        lambdaQueryWrapper.eq(FaNav::getType, faNav.getType());
        lambdaQueryWrapper.orderByAsc(FaNav::getSort);
        List<FaNav> faNavList = this.list(lambdaQueryWrapper);
        return faNavList;
    }

    /**
     * 显示隐藏开关修改
     * @param faNav
     * @throws Exception
     */
    @Override
    public void changeSwitch(FaNav faNav) throws Exception {
        if (null == faNav.getSwitchStatus() || StringUtils.isEmpty(faNav.getSwitchType())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaNav> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNav::getId, faNav.getId());
        switch (faNav.getSwitchType()) {
            case "show":
                lambdaUpdateWrapper.set(FaNav::getStatus, faNav.getSwitchStatus());
                break;
            default:
                break;
        }
        lambdaUpdateWrapper.set(FaNav::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }
}