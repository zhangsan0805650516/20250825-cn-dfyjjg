package com.ruoyi.biz.stockTrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.exchangeConfig.service.IFaExchangeConfigService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.mapper.FaStockTradingMapper;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.biz.tradeApprove.service.IFaTradeApproveService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成交记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Service
public class FaStockTradingServiceImpl extends ServiceImpl<FaStockTradingMapper, FaStockTrading> implements IFaStockTradingService
{
    @Autowired
    private FaStockTradingMapper faStockTradingMapper;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    @Autowired
    private IFaExchangeConfigService iFaExchangeConfigService;

    @Autowired
    private IFaTradeApproveService iFaTradeApproveService;

    /**
     * 查询成交记录
     *
     * @param id 成交记录主键
     * @return 成交记录
     */
    @Override
    public FaStockTrading selectFaStockTradingById(Integer id)
    {
        return faStockTradingMapper.selectFaStockTradingById(id);
    }

    /**
     * 查询成交记录列表
     *
     * @param faStockTrading 成交记录
     * @return 成交记录
     */
    @Override
    public List<FaStockTrading> selectFaStockTradingList(FaStockTrading faStockTrading)
    {
        try {
            faStockTrading.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faStockTrading.getMobile())) {
                faStockTrading.setSalt(AESUtil.encrypt(faStockTrading.getMobile()));
                faStockTrading.setMobile(null);
            }

            return faStockTradingMapper.selectFaStockTradingList(faStockTrading);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增成交记录
     *
     * @param faStockTrading 成交记录
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaStockTrading(FaStockTrading faStockTrading)
    {
        if (null == faStockTrading.getCreateTime()) {
            faStockTrading.setCreateTime(DateUtils.getNowDate());
        }
        return faStockTradingMapper.insertFaStockTrading(faStockTrading);
    }

    /**
     * 修改成交记录
     *
     * @param faStockTrading 成交记录
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaStockTrading(FaStockTrading faStockTrading)
    {
        faStockTrading.setUpdateTime(DateUtils.getNowDate());
        return faStockTradingMapper.updateFaStockTrading(faStockTrading);
    }

    /**
     * 批量删除成交记录
     *
     * @param ids 需要删除的成交记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStockTradingByIds(Integer[] ids)
    {
        return faStockTradingMapper.deleteFaStockTradingByIds(ids);
    }

    /**
     * 删除成交记录信息
     *
     * @param id 成交记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStockTradingById(Integer id)
    {
        return faStockTradingMapper.deleteFaStockTradingById(id);
    }

    /**
     * 查询成交记录
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStockTrading> getStockTradingList(FaStockTrading faStockTrading) throws Exception {
        LambdaQueryWrapper<FaStockTrading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStockTrading::getMemberId, faStockTrading.getMemberId());
        if (null != faStockTrading.getStockId()) {
            lambdaQueryWrapper.eq(FaStockTrading::getStockId, faStockTrading.getStockId());
        }
        if (StringUtils.isNotEmpty(faStockTrading.getStockSymbol())) {
            lambdaQueryWrapper.eq(FaStockTrading::getStockSymbol, faStockTrading.getStockSymbol());
        }
        if (null!= faStockTrading.getTradingType()) {
            lambdaQueryWrapper.eq(FaStockTrading::getTradingType, faStockTrading.getTradingType());
        }
        lambdaQueryWrapper.eq(FaStockTrading::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStockTrading::getCreateTime);
        IPage<FaStockTrading> faStockTradingIPage = this.page(new Page<>(faStockTrading.getPage(), faStockTrading.getSize()), lambdaQueryWrapper);
        return faStockTradingIPage;
    }

    /**
     * 查询成交记录详情
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public FaStockTrading getStockTradingDetail(FaStockTrading faStockTrading) throws Exception {
        if (null == faStockTrading.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faStockTrading = this.getById(faStockTrading.getId());
        return faStockTrading;
    }

    /**
     * 买入股票
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public void buyStock(FaStockTrading faStockTrading) throws Exception {
        // 普通交易
        faStockTrading.setHoldType(1);
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 手->股转换
        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票锁定判断
        if (1 == faStrategy.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 抢筹判断
        checkQc(faMember, faStrategy, faExchangeConfig);

        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkOrdinaryTrade(faStockTrading, faExchangeConfig);
        // 买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("mai_fee", "0.0001");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 新增持仓
//        FaStockHold faStockHold = iFaStockHoldService.addStockHold(faStockTrading);
        FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 抢筹校验
     * @param faMember
     * @param faStrategy
     * @throws Exception
     */
    private void checkQc(FaMember faMember, FaStrategy faStrategy, FaExchangeConfig faExchangeConfig) throws Exception {
        // 取最新股票数据
        faStrategy = iFaStrategyService.getById(faStrategy.getId());
        // 涨跌幅
        BigDecimal caiChangepercent = faStrategy.getCaiChangepercent();

        // 抢筹风控
        BigDecimal limitUp = faExchangeConfig.getLimitUp();
//        BigDecimal limitUp = new BigDecimal(iFaRiskConfigService.getConfigValue("Agu_zhang", "9.9"));

        // 股票是否开启抢筹
        int stockQc = faStrategy.getQcStatus();
        // 用户是否开始抢筹 0否1是
        int memberQc = faMember.getIsQc();

        // 达到抢筹涨跌幅 进入抢筹判断
        if (caiChangepercent.compareTo(limitUp) >= 0) {
            // 股票未打开抢筹 涨停
            if (0 == stockQc) {
                throw new ServiceException(MessageUtils.message("stock.limit.up"), HttpStatus.ERROR);
            }
            // 股票打开抢筹 判断用户抢筹资格
            else if (1 == stockQc) {
                // 用户无抢筹权限
                if (0 == memberQc) {
                    throw new ServiceException(MessageUtils.message("member.no.subscribe.authority"), HttpStatus.ERROR);
                }
            }
        }
    }

    /**
     * 卖出股票
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Transactional
    @Override
    public void sellStock(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 参数判断
        if (null == faStockHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询持仓
        faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票锁定判断
        if (1 == faStrategy.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(faStockHoldDetail.getId());
        // 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发)
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交手数
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkOrdinaryTrade(faStockTrading, faExchangeConfig);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(faMember.getId());
        faStockTrading.setStockId(faStrategy.getId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 1, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 买入股票(大宗交易)
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public void buyStockDz(FaStockTrading faStockTrading) throws Exception {
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getHoldType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户大宗交易开启判断
        if (0 == faMember.getIsDz()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票大宗交易开启判断
        if (0 == faStrategy.getIsDz()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (faStrategy.getZfaNum() < faStockTrading.getTradingNumber()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 判断成交价格
        if (faStrategy.getZfaPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 成交价格=大宗价格
        faStockTrading.setTradingPrice(faStrategy.getZfaPrice());

        // 成交金额
        faStockTrading.setTradingAmount(faStockTrading.getTradingPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())));

        // 风控校验
        iFaRiskConfigService.checkDzTrade(faStockTrading, faExchangeConfig);

        // 大宗买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("zfssfee", "0.0005");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 新增持仓
        FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);

        // 减少剩余数量
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getZfaNum, faStrategy.getZfaNum() - faStockTrading.getTradingNumber());
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        iFaStrategyService.update(lambdaUpdateWrapper);
    }

    /**
     * 大宗交易 进入审核
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public void buyStockDzApprove(FaStockTrading faStockTrading) throws Exception {
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getHoldType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户大宗交易开启判断
        if (0 == faMember.getIsDz()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票大宗交易开启判断
        if (0 == faStrategy.getIsDz()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 判断成交价格
        if (faStrategy.getZfaPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 成交价格=大宗价格
        faStockTrading.setTradingPrice(faStrategy.getZfaPrice());

        // 成交金额
        faStockTrading.setTradingAmount(faStockTrading.getTradingPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())));

        // 风控校验
        iFaRiskConfigService.checkDzTrade(faStockTrading, faExchangeConfig);

        // 大宗买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("zfssfee", "0.0005");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 新增大宗审核
        iFaTradeApproveService.addDz(faStockTrading);
    }

    /**
     * 买入股票(VIP调研)
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void buyStockVip(FaStockTrading faStockTrading) throws Exception {
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getHoldType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户VIP调研交易开启判断
        if (0 == faMember.getIsVip()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票VIP调研交易开启判断
        if (0 == faStrategy.getVipQcStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (faStrategy.getLeftQcNum() < faStockTrading.getTradingNumber()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 判断成交价格
        if (faStrategy.getVipQcPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 成交价格=VIP调研价格
        faStockTrading.setTradingPrice(faStrategy.getVipQcPrice());

        // 成交金额
        faStockTrading.setTradingAmount(faStockTrading.getTradingPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())));

        // 风控校验
        iFaRiskConfigService.checkOrdinaryTrade(faStockTrading, faExchangeConfig);

        // VIP调研买入手续费率=普通买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("mai_fee", "0.0001");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 新增持仓
        FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);

        // 减少剩余数量
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getLeftQcNum, faStrategy.getLeftQcNum() - faStockTrading.getTradingNumber());
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        iFaStrategyService.update(lambdaUpdateWrapper);
    }

    /**
     * 卖出股票(大宗交易)
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Transactional
    @Override
    public void sellStockDz(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 参数判断
        if (null == faStockHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询大宗持仓
        faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户大宗交易锁定判断
        if (0 == faMember.getIsDz()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票大宗交易开启判断
//        if (0 == faStrategy.getIsDz()) {
//            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
//        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(faStockHoldDetail.getId());
        // 持仓类型(1普通交易 2大宗交易 3VIP调研 4指数交易 5期货交易 6基金 7增发)
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交手数
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkDzTrade(faStockTrading, faExchangeConfig);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(faMember.getId());
        faStockTrading.setStockId(faStrategy.getId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 1, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 卖出股票(VIP调研)
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Override
    public void sellStockVip(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 参数判断
        if (null == faStockHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询大宗持仓
        faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户VIP调研交易锁定判断
        if (0 == faMember.getIsVip()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票VIP调研交易开启判断
//        if (0 == faStrategy.getQcStatus()) {
//            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
//        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(faStockHoldDetail.getId());
        // 持仓类型(1普通交易 2大宗交易 3VIP调研 4指数交易 5期货交易 6基金 7增发)
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交手数
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkOrdinaryTrade(faStockTrading, faExchangeConfig);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(faMember.getId());
        faStockTrading.setStockId(faStrategy.getId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 1, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 平仓
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Transactional
    @Override
    public void closingPositionDetail(FaStockHoldDetail faStockHoldDetail) throws Exception {
        if (null == faStockHoldDetail.getId() || StringUtils.isEmpty(faStockHoldDetail.getForceClosePassword())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 持仓明细
        FaStockHoldDetail detail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(detail)) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 强平密码
        String forceClosePassword = iFaRiskConfigService.getConfigValue("force_close_password", "");
        if (StringUtils.isNotEmpty(forceClosePassword) && !faStockHoldDetail.getForceClosePassword().equals(forceClosePassword)) {
            throw new ServiceException(MessageUtils.message("password.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(detail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 是否上市
        if (0 == detail.getIsList()) {
            throw new ServiceException(MessageUtils.message("unlisted.stocks"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(detail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(detail.getId());
        // 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发 8融券)
        faStockTrading.setHoldType(detail.getHoldType());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交手数
        faStockTrading.setTradingHand(detail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(detail.getHoldNumber());

        // 计算盈亏 不包含手续费
        BigDecimal profitLose = faStockTrading.getTradingPrice().subtract(detail.getAverage()).multiply(new BigDecimal(detail.getHoldNumber()));
        // 买涨,涨了增加
        if (1 == detail.getTradeDirect()) {
            profitLose = profitLose;
        }
        // 买跌，跌了增加
        else if (2 == detail.getTradeDirect()) {
            profitLose = profitLose.multiply(new BigDecimal(-1));
        }
        // 交易盈亏
        faStockTrading.setProfitLose(profitLose);

        // 成交金额
        faStockTrading.setTradingAmount(detail.getBuyPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())).add(profitLose));

        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(detail.getMemberId());
        faStockTrading.setStockId(detail.getStockId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 方向
        faStockTrading.setTradeDirect(detail.getTradeDirect());

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 0, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 交易统计
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getTradingStatistics(FaStockTrading faStockTrading) throws Exception {
        return faStockTradingMapper.getTradingStatistics(faStockTrading);
    }

    /**
     * 买入手续费
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalBuyFee(FaStockTrading faStockTrading) throws Exception {
        return faStockTradingMapper.getTotalBuyFee(faStockTrading);
    }

    /**
     * 卖出手续费
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalSellFee(FaStockTrading faStockTrading) throws Exception {
        return faStockTradingMapper.getTotalSellFee(faStockTrading);
    }

    /**
     * 印花税
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalStampDuty(FaStockTrading faStockTrading) throws Exception {
        return faStockTradingMapper.getTotalStampDuty(faStockTrading);
    }

    /**
     * 买入融券股票
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void buySecuritiesLending(FaStockTrading faStockTrading) throws Exception {
        // 融券交易
        faStockTrading.setHoldType(8);
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getTradeDirect()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 手->股转换
        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票融券开关
        if (0 == faStrategy.getIsRq()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (faStrategy.getBorrowNumLeft() < faStockTrading.getTradingNumber()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkRqTrade(faStockTrading, faExchangeConfig);
        // 买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("rq_buy_fee", "0.0001");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 交易方向
        faStockTrading.setTradeDirect(faStockTrading.getTradeDirect());

        // 新增持仓
        FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);

        // 减少剩余数量
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getBorrowNumLeft, faStrategy.getBorrowNumLeft() - faStockTrading.getTradingNumber());
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        iFaStrategyService.update(lambdaUpdateWrapper);
    }

    /**
     * 平仓融券股票
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Override
    public void closeSecuritiesLending(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 参数判断
        if (null == faStockHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询持仓
        faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 持仓状态
        if (1 == faStockHoldDetail.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.hold.already.sell"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票融券开关
//        if (0 == faStrategy.getIsRq()) {
//            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
//        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(faStockHoldDetail.getId());
        // 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发 8融券 9预约重组)
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交手数
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());

        // 计算盈亏 不包含手续费
        BigDecimal profitLose = faStockTrading.getTradingPrice().subtract(faStockHoldDetail.getAverage()).multiply(new BigDecimal(faStockHoldDetail.getHoldNumber()));
        // 买涨,涨了增加
        if (1 == faStockHoldDetail.getTradeDirect()) {
            profitLose = profitLose;
        }
        // 买跌，跌了增加
        else if (2 == faStockHoldDetail.getTradeDirect()) {
            profitLose = profitLose.multiply(new BigDecimal(-1));
        }
        // 交易盈亏
        faStockTrading.setProfitLose(profitLose);
        // 成交金额
        faStockTrading.setTradingAmount(faStockHoldDetail.getBuyPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())).add(profitLose));
        // 风控校验
        iFaRiskConfigService.checkRqTrade(faStockTrading, faExchangeConfig);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(faMember.getId());
        faStockTrading.setStockId(faStrategy.getId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 交易方向
        faStockTrading.setTradeDirect(faStockHoldDetail.getTradeDirect());

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 1, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

    /**
     * 买入股票(预约重组)
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void buyStockYycz(FaStockTrading faStockTrading) throws Exception {
        // 预约重组
        faStockTrading.setHoldType(9);
        // 参数判断
        if (null == faStockTrading.getStockId() || null == faStockTrading.getTradingNumber() || null == faStockTrading.getHoldType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faStockTrading.setTradingHand(faStockTrading.getTradingNumber() / 100);
        faStockTrading.setTradingNumber(faStockTrading.getTradingNumber());

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockTrading.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易开启判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockTrading.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票预约重组交易开启判断
        if (0 == faStrategy.getYyczSwitch()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 剩余名额是否足够
        if (faStrategy.getYyczLeftQuota() < faStockTrading.getTradingNumber()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);

        // 成交金额
        faStockTrading.setTradingAmount(faStockTrading.getTradingPrice().multiply(new BigDecimal(faStockTrading.getTradingNumber())));

        // 风控校验
        iFaRiskConfigService.checkYyczTrade(faStockTrading, faExchangeConfig);

        // 预约重组买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("yycz_fee", "0.0005");
        // 手续费
        BigDecimal fee = faStockTrading.getTradingAmount().multiply(new BigDecimal(maiFee));
        // 总金额
        BigDecimal totalAmount = faStockTrading.getTradingAmount().add(fee);
        // 余额判断
        if (faMember.getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 成交记录
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(1);
        faStockTrading.setTradingPoundage(fee);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 新增持仓
        FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.addStockHoldDetail(faStockTrading);
        faStockTrading.setHoldId(faStockHoldDetail.getId());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);

        // 减少剩余数量
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getYyczLeftQuota, faStrategy.getYyczLeftQuota() - faStockTrading.getTradingNumber());
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        iFaStrategyService.update(lambdaUpdateWrapper);
    }

    /**
     * 卖出股票(预约重组)
     * @param faStockHoldDetail
     * @throws Exception
     */
    @Transactional
    @Override
    public void sellStockYycz(FaStockHoldDetail faStockHoldDetail) throws Exception {
        // 参数判断
        if (null == faStockHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询持仓
        faStockHoldDetail = iFaStockHoldDetailService.getById(faStockHoldDetail.getId());
        if (ObjectUtils.isEmpty(faStockHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faStockHoldDetail.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易开启判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // 股票
        FaStrategy faStrategy = iFaStrategyService.getById(faStockHoldDetail.getStockId());
        if (ObjectUtils.isEmpty(faStrategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 取交易所配置
        LambdaQueryWrapper<FaExchangeConfig> exchangeConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getExchangeType, faStrategy.getType());
        exchangeConfigLambdaQueryWrapper.eq(FaExchangeConfig::getDeleteFlag, 0);
        FaExchangeConfig faExchangeConfig = iFaExchangeConfigService.getOne(exchangeConfigLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faExchangeConfig)) {
            throw new ServiceException(MessageUtils.message("exchange.config.error"), HttpStatus.ERROR);
        }

        // 实时价格
        BigDecimal currentPrice = iFaStrategyService.getCurrentPrice(faStrategy);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException(MessageUtils.message("stock.price.error"), HttpStatus.ERROR);
        }

        FaStockTrading faStockTrading = new FaStockTrading();
        faStockTrading.setHoldId(faStockHoldDetail.getId());
        // 持仓类型(1普通交易 2大宗交易 3VIP调研 4指数交易 5期货交易 6基金 7增发)
        faStockTrading.setHoldType(faStockHoldDetail.getHoldType());
        // 成交手数
        faStockTrading.setTradingHand(faStockHoldDetail.getHoldHand());
        // 成交数量
        faStockTrading.setTradingNumber(faStockHoldDetail.getHoldNumber());
        // 成交价格
        faStockTrading.setTradingPrice(currentPrice);
        // 成交金额
        faStockTrading.setTradingAmount(currentPrice.multiply(new BigDecimal(faStockTrading.getTradingNumber())));
        // 风控校验
        iFaRiskConfigService.checkYyczTrade(faStockTrading, faExchangeConfig);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = faStockTrading.getTradingAmount().multiply(new BigDecimal(yhFee));

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = faStockTrading.getTradingAmount().multiply(new BigDecimal(sellFee));

        // 成交记录
        faStockTrading.setMemberId(faMember.getId());
        faStockTrading.setStockId(faStrategy.getId());
        faStockTrading.setStockName(faStrategy.getTitle());
        faStockTrading.setStockSymbol(faStrategy.getCode());
        faStockTrading.setAllCode(faStrategy.getAllCode());
        faStockTrading.setTradingType(2);
        faStockTrading.setStampDuty(duty);
        faStockTrading.setTradingPoundage(tradingPoundage);
        faStockTrading.setCreateTime(new Date());
        faStockTrading.setDeleteFlag(0);
        faStockTrading.setFaMember(faMember);
        faStockTrading.setFaStrategy(faStrategy);

        // 扣减持仓
        iFaStockHoldDetailService.subtractStockHoldDetail(faStockTrading, 1, faStrategy.getCurrentStatus());

        // 保存交易记录
        this.insertFaStockTrading(faStockTrading);

        // 资金流水
        iFaCapitalLogService.save(faStockTrading);
    }

}