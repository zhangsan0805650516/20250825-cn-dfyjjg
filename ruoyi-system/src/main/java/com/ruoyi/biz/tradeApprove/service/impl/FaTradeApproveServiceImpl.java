package com.ruoyi.biz.tradeApprove.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.mapper.FaMemberMapper;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;
import com.ruoyi.biz.tradeApprove.mapper.FaTradeApproveMapper;
import com.ruoyi.biz.tradeApprove.service.IFaTradeApproveService;
import com.ruoyi.common.constant.CapitalFlowConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交易审核Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-31
 */
@Service
public class FaTradeApproveServiceImpl extends ServiceImpl<FaTradeApproveMapper, FaTradeApprove> implements IFaTradeApproveService
{
    @Autowired
    private FaTradeApproveMapper faTradeApproveMapper;

    @Autowired
    private FaMemberMapper faMemberMapper;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    /**
     * 查询交易审核
     *
     * @param id 交易审核主键
     * @return 交易审核
     */
    @Override
    public FaTradeApprove selectFaTradeApproveById(Integer id)
    {
        return faTradeApproveMapper.selectFaTradeApproveById(id);
    }

    /**
     * 查询交易审核列表
     *
     * @param faTradeApprove 交易审核
     * @return 交易审核
     */
    @Override
    public List<FaTradeApprove> selectFaTradeApproveList(FaTradeApprove faTradeApprove)
    {
        faTradeApprove.setDeleteFlag(0);
        return faTradeApproveMapper.selectFaTradeApproveList(faTradeApprove);
    }

    /**
     * 新增交易审核
     *
     * @param faTradeApprove 交易审核
     * @return 结果
     */
    @Override
    public int insertFaTradeApprove(FaTradeApprove faTradeApprove)
    {
        faTradeApprove.setCreateTime(DateUtils.getNowDate());
        return faTradeApproveMapper.insertFaTradeApprove(faTradeApprove);
    }

    /**
     * 修改交易审核
     *
     * @param faTradeApprove 交易审核
     * @return 结果
     */
    @Override
    public int updateFaTradeApprove(FaTradeApprove faTradeApprove)
    {
        faTradeApprove.setUpdateTime(DateUtils.getNowDate());
        return faTradeApproveMapper.updateFaTradeApprove(faTradeApprove);
    }

    /**
     * 批量删除交易审核
     *
     * @param ids 需要删除的交易审核主键
     * @return 结果
     */
    @Override
    public int deleteFaTradeApproveByIds(Integer[] ids)
    {
        return faTradeApproveMapper.deleteFaTradeApproveByIds(ids);
    }

    /**
     * 删除交易审核信息
     *
     * @param id 交易审核主键
     * @return 结果
     */
    @Override
    public int deleteFaTradeApproveById(Integer id)
    {
        return faTradeApproveMapper.deleteFaTradeApproveById(id);
    }

    /**
     * 新增大宗审核
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void addDz(FaStockTrading faStockTrading) throws Exception {
        FaMember faMember = faStockTrading.getFaMember();
        FaStrategy faStrategy = faStockTrading.getFaStrategy();
        FaTradeApprove faTradeApprove = new FaTradeApprove();

        // 用户
        faTradeApprove.setUserId(faMember.getId());
        faTradeApprove.setMobile(faMember.getMobile());
        faTradeApprove.setName(faMember.getName());
        faTradeApprove.setSuperiorId(faMember.getSuperiorId());
        faTradeApprove.setSuperiorCode(faMember.getSuperiorCode());
        faTradeApprove.setSuperiorName(faMember.getSuperiorName());

        // 交易类型(1大宗 2VIP调研 3申购 4配售 )
        faTradeApprove.setTradeType(1);

        // 股票
        faTradeApprove.setStockId(faStrategy.getId());
        faTradeApprove.setStockName(faStrategy.getTitle());
        faTradeApprove.setStockSymbol(faStrategy.getCode());

        // 交易
        faTradeApprove.setTradingHand(faStockTrading.getTradingHand());
        faTradeApprove.setTradingNumber(faStockTrading.getTradingNumber());
        faTradeApprove.setTradingPrice(faStockTrading.getTradingPrice());
        faTradeApprove.setTradingAmount(faStockTrading.getTradingAmount());
        faTradeApprove.setTradingPoundage(faStockTrading.getTradingPoundage());

        // 应缴 = 交易金额 + 手续费
        faTradeApprove.setShouldPayAmount(faTradeApprove.getTradingAmount().add(faTradeApprove.getTradingPoundage()));

        faTradeApprove.setCreateTime(new Date());

        // 余额判断 < 交易金额 + 手续费
        if (faMember.getBalance().compareTo(faTradeApprove.getTradingAmount().add(faTradeApprove.getTradingPoundage())) < 0) {
            // 需要补缴
            faTradeApprove.setRepayStatus(1);
            // 实缴金额
            if (faMember.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                faTradeApprove.setPaidAmount(BigDecimal.ZERO);
            } else {
                faTradeApprove.setPaidAmount(faMember.getBalance());
            }
        } else {
            // 无需补缴
            faTradeApprove.setRepayStatus(0);
            // 实缴金额
            faTradeApprove.setPaidAmount(faTradeApprove.getShouldPayAmount());
        }

        // 记录流水
        FaCapitalLog faCapitalLog = iFaCapitalLogService.save(faTradeApprove);
        faTradeApprove.setCapitalId(faCapitalLog.getId());

        this.save(faTradeApprove);
    }

    /**
     * 审核交易
     * @param faTradeApprove
     * @throws Exception
     */
    @Override
    public void approveTrade(FaTradeApprove faTradeApprove) throws Exception {
        // 审核状态
        int status = faTradeApprove.getStatus();

        FaTradeApprove selectOne = this.getById(faTradeApprove.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 非待审核状态
        if (0 != selectOne.getStatus()) {
            throw new ServiceException("数据已审核，请勿重复审核", HttpStatus.ERROR);
        }

        // 0无需补缴 1需要补缴 2补缴完成 3废弃
        if (1 == selectOne.getRepayStatus() || 3 == selectOne.getRepayStatus()) {
            throw new ServiceException("用户需要补缴金额", HttpStatus.ERROR);
        }

        FaMember faMember = faMemberMapper.selectById(selectOne.getUserId());
        FaStrategy faStrategy = iFaStrategyService.getById(selectOne.getStockId());

        // 审核通过，退回金额，删除流水，走大宗交易逻辑
        if (1 == status) {
            // 更新用户余额 增加 实缴
            faMemberMapper.updateMemberBalance(faMember.getId(), faTradeApprove.getPaidAmount(), 0);

            // 删除流水
            LambdaUpdateWrapper<FaCapitalLog> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaCapitalLog::getId, selectOne.getCapitalId());
            lambdaUpdateWrapper.set(FaCapitalLog::getDeleteFlag, 1);
            lambdaUpdateWrapper.set(FaCapitalLog::getUpdateTime, new Date());
            iFaCapitalLogService.update(lambdaUpdateWrapper);

            //大宗买入
            FaStockTrading faStockTrading = new FaStockTrading();
            faStockTrading.setTradingHand(selectOne.getTradingHand());
            faStockTrading.setTradingNumber(selectOne.getTradingNumber());
            faStockTrading.setTradingPrice(selectOne.getTradingPrice());
            faStockTrading.setTradingAmount(selectOne.getTradingAmount());
            faStockTrading.setStockId(selectOne.getStockId());
            faStockTrading.setStockName(selectOne.getStockName());
            faStockTrading.setStockSymbol(selectOne.getStockSymbol());
            faStockTrading.setAllCode(faStrategy.getAllCode());
            faStockTrading.setTradingType(1);
            faStockTrading.setTradingPoundage(selectOne.getTradingPoundage());
            faStockTrading.setCreateTime(new Date());
            faStockTrading.setDeleteFlag(0);
            faStockTrading.setFaMember(faMember);
            faStockTrading.setFaStrategy(faStrategy);

            // 新增持仓
            FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
            faStockTrading.setHoldId(faStockHoldDetail.getId());

            // 保存交易记录
            iFaStockTradingService.insertFaStockTrading(faStockTrading);

            // 资金流水
            iFaCapitalLogService.save(faStockTrading);

            // 减少剩余数量
            LambdaUpdateWrapper<FaStrategy> strategyLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            strategyLambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
            strategyLambdaUpdateWrapper.set(FaStrategy::getZfaNum, faStrategy.getZfaNum() - faStockTrading.getTradingNumber());
            strategyLambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
            iFaStrategyService.update(strategyLambdaUpdateWrapper);
        }

        // 审核驳回，退回金额
        if (2 == status) {
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(faMember.getId());
            faCapitalLog.setMobile(faMember.getMobile());
            faCapitalLog.setName(faMember.getName());
            faCapitalLog.setSuperiorId(faMember.getSuperiorId());
            faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
            faCapitalLog.setSuperiorName(faMember.getSuperiorName());
            faCapitalLog.setContent(DictUtils.getCapitalFlowDictLabel(CapitalFlowConstants.DZ_RETURN));
            faCapitalLog.setMoney(faTradeApprove.getShouldPayAmount());
            faCapitalLog.setBeforeMoney(faMember.getBalance());
            faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
            faCapitalLog.setType(CapitalFlowConstants.DZ_RETURN);
            faCapitalLog.setDirect(1);
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setStockId(faTradeApprove.getStockId());
            faCapitalLog.setStockName(faTradeApprove.getStockName());
            faCapitalLog.setStockSymbol(faTradeApprove.getStockSymbol());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额 增加 实缴
            faMemberMapper.updateMemberBalance(faMember.getId(), faTradeApprove.getPaidAmount(), 0);
        }

    }
}