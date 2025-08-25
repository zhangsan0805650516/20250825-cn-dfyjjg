package com.ruoyi.biz.memberStock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.memberStock.domain.FaMemberStock;
import com.ruoyi.biz.memberStock.mapper.FaMemberStockMapper;
import com.ruoyi.biz.memberStock.service.IFaMemberStockService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
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
 * 自选股票Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-19
 */
@Service
public class FaMemberStockServiceImpl extends ServiceImpl<FaMemberStockMapper, FaMemberStock> implements IFaMemberStockService
{
    @Autowired
    private FaMemberStockMapper faMemberStockMapper;

    @Autowired
    private IFaStrategyService faStrategyService;

    /**
     * 查询自选股票
     *
     * @param id 自选股票主键
     * @return 自选股票
     */
    @Override
    public FaMemberStock selectFaMemberStockById(Integer id)
    {
        return faMemberStockMapper.selectFaMemberStockById(id);
    }

    /**
     * 查询自选股票列表
     *
     * @param faMemberStock 自选股票
     * @return 自选股票
     */
    @Override
    public List<FaMemberStock> selectFaMemberStockList(FaMemberStock faMemberStock)
    {
        faMemberStock.setDeleteFlag(0);
        return faMemberStockMapper.selectFaMemberStockList(faMemberStock);
    }

    /**
     * 新增自选股票
     *
     * @param faMemberStock 自选股票
     * @return 结果
     */
    @Override
    public int insertFaMemberStock(FaMemberStock faMemberStock)
    {
        faMemberStock.setCreateTime(DateUtils.getNowDate());
        return faMemberStockMapper.insertFaMemberStock(faMemberStock);
    }

    /**
     * 修改自选股票
     *
     * @param faMemberStock 自选股票
     * @return 结果
     */
    @Override
    public int updateFaMemberStock(FaMemberStock faMemberStock)
    {
        faMemberStock.setUpdateTime(DateUtils.getNowDate());
        return faMemberStockMapper.updateFaMemberStock(faMemberStock);
    }

    /**
     * 批量删除自选股票
     *
     * @param ids 需要删除的自选股票主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberStockByIds(Integer[] ids)
    {
        return faMemberStockMapper.deleteFaMemberStockByIds(ids);
    }

    /**
     * 删除自选股票信息
     *
     * @param id 自选股票主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberStockById(Integer id)
    {
        return faMemberStockMapper.deleteFaMemberStockById(id);
    }

    /**
     * 查询自选股票
     * @param faMemberStock
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getMemberStock(FaMemberStock faMemberStock) throws Exception {
//        if (null == faMemberStock.getType()) {
//            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
//        }
        IPage<FaStrategy> faStrategyIPage = faMemberStockMapper.getMemberStock(new Page<>(faMemberStock.getPage(), faMemberStock.getSize()), faMemberStock);
        return faStrategyIPage;
    }

    /**
     * 查询股票是否自选
     * @param faStrategy
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public int checkMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception {
        if (null == faStrategy.getId() && StringUtils.isEmpty(faStrategy.getCode()) && StringUtils.isEmpty(faStrategy.getAllCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 股票是否存在
        FaStrategy selectOne = faStrategyService.checkStock(faStrategy);

        // 是否已自选
        if(checkMemberStock(faMember.getId(), selectOne.getId())) {
            return 1;
        }

        return 0;
    }

    /**
     * 新增自选股票
     * @param faStrategy
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void addMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception {
        if (null == faStrategy.getId() && StringUtils.isEmpty(faStrategy.getCode()) && StringUtils.isEmpty(faStrategy.getAllCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 股票是否存在
        FaStrategy selectOne = faStrategyService.checkStock(faStrategy);

        // 是否已自选
        if(checkMemberStock(faMember.getId(), selectOne.getId())) {
            throw new ServiceException(MessageUtils.message("member.stock.exists"), HttpStatus.ERROR);
        }

        FaMemberStock faMemberStock = new FaMemberStock();
        faMemberStock.setMemberId(faMember.getId());
        faMemberStock.setStockId(selectOne.getId());
        faMemberStock.setCreateTime(new Date());
        faMemberStock.setDeleteFlag(0);
        this.insertFaMemberStock(faMemberStock);
    }

    /**
     * 是否已自选
     * @param memberId
     * @param stockId
     */
    private boolean checkMemberStock(Integer memberId, Integer stockId) throws Exception {
        LambdaQueryWrapper<FaMemberStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMemberStock::getMemberId, memberId);
        lambdaQueryWrapper.eq(FaMemberStock::getStockId, stockId);
        lambdaQueryWrapper.eq(FaMemberStock::getDeleteFlag, 0);
        FaMemberStock faMemberStock = faMemberStockMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faMemberStock)) {
            return true;
        }
        return false;
    }

    /**
     * 删除自选股票
     * @param faStrategy
     * @param faMember
     * @throws Exception
     */
    @Override
    public void deleteMemberStock(FaStrategy faStrategy, FaMember faMember) throws Exception {
        if (null == faStrategy.getId() && StringUtils.isEmpty(faStrategy.getCode()) && StringUtils.isEmpty(faStrategy.getAllCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 股票是否存在
        FaStrategy selectOne = faStrategyService.checkStock(faStrategy);

        LambdaUpdateWrapper<FaMemberStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMemberStock::getMemberId, faMember.getId());
        lambdaUpdateWrapper.eq(FaMemberStock::getStockId, selectOne.getId());
        lambdaUpdateWrapper.set(FaMemberStock::getDeleteFlag, 1);
        lambdaUpdateWrapper.set(FaMemberStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

}