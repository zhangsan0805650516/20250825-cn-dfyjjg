package com.ruoyi.biz.riskConfig.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.holidayConfig.service.IFaHolidayConfigService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.domain.FaRiskConfig;
import com.ruoyi.biz.riskConfig.mapper.FaRiskConfigMapper;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.biz.withdraw.service.IFaWithdrawService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.HolidayUtil;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 风控设置Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Service
public class FaRiskConfigServiceImpl extends ServiceImpl<FaRiskConfigMapper, FaRiskConfig> implements IFaRiskConfigService
{
    @Autowired
    private FaRiskConfigMapper faRiskConfigMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFaWithdrawService iFaWithdrawService;

    @Autowired
    private IFaHolidayConfigService iFaHolidayConfigService;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询风控设置
     *
     * @param id 风控设置主键
     * @return 风控设置
     */
    @Override
    public FaRiskConfig selectFaRiskConfigById(Integer id)
    {
        return faRiskConfigMapper.selectFaRiskConfigById(id);
    }

    /**
     * 查询风控设置
     *
     * @param name 风控设置name
     * @return 风控设置
     */
    @Override
    public FaRiskConfig selectFaRiskConfigByName(String name)
    {
        return faRiskConfigMapper.selectFaRiskConfigByName(name);
    }

    /**
     * 查询风控设置列表
     *
     * @param faRiskConfig 风控设置
     * @return 风控设置
     */
    @Override
    public List<FaRiskConfig> selectFaRiskConfigList(FaRiskConfig faRiskConfig)
    {
        faRiskConfig.setDeleteFlag(0);
        return faRiskConfigMapper.selectFaRiskConfigList(faRiskConfig);
    }

    /**
     * 新增风控设置
     *
     * @param faRiskConfig 风控设置
     * @return 结果
     */
    @Override
    public int insertFaRiskConfig(FaRiskConfig faRiskConfig)
    {
        faRiskConfig.setCreateTime(DateUtils.getNowDate());
        return faRiskConfigMapper.insertFaRiskConfig(faRiskConfig);
    }

    /**
     * 修改风控设置
     *
     * @param faRiskConfig 风控设置
     * @return 结果
     */
    @Override
    public int updateFaRiskConfig(FaRiskConfig faRiskConfig)
    {
        faRiskConfig.setUpdateTime(DateUtils.getNowDate());
        return faRiskConfigMapper.updateFaRiskConfig(faRiskConfig);
    }

    /**
     * 批量删除风控设置
     *
     * @param ids 需要删除的风控设置主键
     * @return 结果
     */
    @Override
    public int deleteFaRiskConfigByIds(Integer[] ids)
    {
        return faRiskConfigMapper.deleteFaRiskConfigByIds(ids);
    }

    /**
     * 删除风控设置信息
     *
     * @param id 风控设置主键
     * @return 结果
     */
    @Override
    public int deleteFaRiskConfigById(Integer id)
    {
        return faRiskConfigMapper.deleteFaRiskConfigById(id);
    }

    /**
     * 获取字典分类
     * @return
     */
    @Override
    public List<FaRiskConfig> getConfiggroup() throws Exception {
        LambdaQueryWrapper<FaRiskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRiskConfig::getName, "configgroup");
        return this.list(lambdaQueryWrapper);
    }

    /**
     * 根据分类获取字典列表
     */
    @Override
    public List<FaRiskConfig> getConfigListByGroup(FaRiskConfig faRiskConfig) throws Exception {
        LambdaQueryWrapper<FaRiskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRiskConfig::getConfigGroup, faRiskConfig.getConfigGroup());
        List<FaRiskConfig> list = this.list(lambdaQueryWrapper);
        // array类型value 转成list数组 方便展示修改
        for (FaRiskConfig config : list) {
            if ("array".equals(config.getType())) {
                JSONObject jsonObject = JSONObject.parseObject(config.getValue());
                List<String[]> valueList = new ArrayList<>();
                for(String key : jsonObject.keySet()) {
                    String[] arrayString = new String[2];
                    arrayString[0] = key;
                    arrayString[1] = jsonObject.getString(key);
                    valueList.add(arrayString);
                }
                config.setValueList(valueList);
            }
        }
        return list;
    }

    /**
     * 修改风控设置列表
     */
    @Transactional
    @Override
    public void updateRiskConfigList(List<FaRiskConfig> faRiskConfigList) throws Exception {
        if (!faRiskConfigList.isEmpty()) {

            // 申购输入框，默认关闭
            int pskqsrsgs = 0;

            for (FaRiskConfig faRiskConfig : faRiskConfigList) {
                // array类型value，将list数组转为String，更新value
                if ("array".equals(faRiskConfig.getType())) {
                    String value = "{";
                    for (String[] values : faRiskConfig.getValueList()) {
                        value += "\"" + values[0] + "\":\"" + values[1] + "\",";
                    }
                    value = value.substring(0, value.length() - 1) + "}";
                    faRiskConfig.setValue(value);
                }
                LambdaUpdateWrapper<FaRiskConfig> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRiskConfig::getId, faRiskConfig.getId());
                lambdaUpdateWrapper.set(FaRiskConfig::getValue, faRiskConfig.getValue().trim());

                // 申购输入框
                if ("pskqsrsgs".equals(faRiskConfig.getName())) {
                    pskqsrsgs = Integer.parseInt(faRiskConfig.getValue().trim());
                }

                // 固定申购数
                if ("ps_nums".equals(faRiskConfig.getName())) {
                    if (pskqsrsgs == 0) {
                        lambdaUpdateWrapper.set(FaRiskConfig::getValue, 0);
                    }
                }

                lambdaUpdateWrapper.set(FaRiskConfig::getUpdateTime, new Date());
                this.update(lambdaUpdateWrapper);
            }
        }
    }

    /**
     * 查询风控设置map
     */
    @Override
    public Map getRiskConfigMap() throws Exception {
        Map<String, Object> map = new HashMap();
        List<FaRiskConfig> list = faRiskConfigMapper.selectFaRiskConfigList(null);
        if (list.size() > 0) {
            for (FaRiskConfig faRiskConfig : list) {
                if ("oss".equals(faRiskConfig.getConfigGroup())) {
                    continue;
                }
                if ("payment".equals(faRiskConfig.getConfigGroup())) {
                    continue;
                }
                if ("dictionary".equals(faRiskConfig.getConfigGroup())) {
                    continue;
                }
                if ("force_close_password".equals(faRiskConfig.getName())) {
                    continue;
                }
                if ("unify".equals(faRiskConfig.getName())) {
                    continue;
                }
                if ("applogo".equals(faRiskConfig.getName())) {
                    continue;
                }
                if ("defaultavatar".equals(faRiskConfig.getName())) {
                    continue;
                }
                if ("0".equals(faRiskConfig.getValue())) {
                    map.put(faRiskConfig.getName(), 0);
                } else {
                    map.put(faRiskConfig.getName(), faRiskConfig.getValue());
                }
            }
        }
        return map;
    }

    /**
     * 查询风控设置配置项
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    @Override
    public String getConfigValue(String key, String defaultValue) throws Exception {
        LambdaQueryWrapper<FaRiskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRiskConfig::getName, key);
        lambdaQueryWrapper.eq(FaRiskConfig::getDeleteFlag, 0);
        FaRiskConfig faRiskConfig = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRiskConfig) && StringUtils.isNotEmpty(faRiskConfig.getValue())) {
            return faRiskConfig.getValue();
        }
        return defaultValue;
    }

    /**
     * 普通交易风控校验
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public void checkOrdinaryTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception {
        // 普通交易时间校验
        this.checkOrdinaryTradeTime(faExchangeConfig);

        // 普通交易最大手数校验
        String gpjiaoyi = this.getConfigValue("gpjiaoyi", "500000000");
        if (new BigDecimal(faStockTrading.getTradingNumber()).compareTo(new BigDecimal(gpjiaoyi)) > 0) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.lots"), HttpStatus.ERROR);
        }

        // 普通交易最低金额校验
        String guzuidijiaoyijinge = this.getConfigValue("guzuidijiaoyijinge", "1000");
        if (faStockTrading.getTradingAmount().compareTo(new BigDecimal(guzuidijiaoyijinge)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
        }
    }

    /**
     * 大宗风控校验
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void checkDzTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception {
        // 大宗交易时间校验
        this.checkDzTradeTime(faExchangeConfig);

        // 大宗交易最低手数校验
        String dzjyzfa_ss = this.getConfigValue("dzjyzfa_ss", "1");
//        if (faStockTrading.getTradingNumber() < Integer.parseInt(dzjyzfa_ss) * 100) {
        if (new BigDecimal(faStockTrading.getTradingNumber()).compareTo(new BigDecimal(dzjyzfa_ss)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.lots"), HttpStatus.ERROR);
        }

        // 大宗交易最低金额校验
        String dzjyzfa_jine = this.getConfigValue("dzjyzfa_jine", "1000");
        if (faStockTrading.getTradingAmount().compareTo(new BigDecimal(dzjyzfa_jine)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
        }
    }

    /**
     * 大宗交易时间校验
     * @throws Exception
     */
    private void checkDzTradeTime(FaExchangeConfig faExchangeConfig) throws Exception {
        // 上午交易时间 swjiaoyi
        String dzjyzfa_shijian = this.getConfigValue("dzjyzfa_shijian", "09:30-11:30");
//        String dzjyzfa_shijian = faExchangeConfig.getBlockTimeMorning();
        // 下午交易时间 xwjiaoyi
        String dzjyzfaxw_shijian = this.getConfigValue("dzjyzfaxw_shijian", "13:00-15:00");
//        String dzjyzfaxw_shijian = faExchangeConfig.getBlockTimeAfternoon();
        // 判断当前时间是否在时间段内
        checkTime(dzjyzfa_shijian, dzjyzfaxw_shijian);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 普通交易时间校验
     * @throws Exception
     */
    @Override
    public void checkOrdinaryTradeTime(FaExchangeConfig faExchangeConfig) throws Exception {
        // 上午交易时间 swjiaoyi
        String swjiaoyi = this.getConfigValue("swjiaoyi", "09:30-11:30");
//        String swjiaoyi = faExchangeConfig.getTradingTimeMorning();
        // 下午交易时间 xwjiaoyi
        String xwjiaoyi = this.getConfigValue("xwjiaoyi", "13:00-15:00");
//        String xwjiaoyi = faExchangeConfig.getTradingTimeAfternoon();
        // 判断当前时间是否在时间段内
        checkTime(swjiaoyi, xwjiaoyi);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 申购风控校验
     * @param faNewStock
     * @throws Exception
     */
    @Override
    public void checkShengou(FaNewStock faNewStock) throws Exception {
        // 申购交易时间校验
        this.checkShengouTime();

        // 输入框是否开启 kqssss 默认开启
        String kqssss = this.getConfigValue("kqssss", "1");

        // 输入框开启，校验最低手数/最高手数/最低金额
        if ("1".equals(kqssss)) {
            // 申购交易最低手数校验
            String psjy0_ss = this.getConfigValue("psjy0_ss", "1");
            if (new BigDecimal(faNewStock.getSgNums()).compareTo(new BigDecimal(psjy0_ss)) < 0) {
                throw new ServiceException(MessageUtils.message("below.minimum.lots"), HttpStatus.ERROR);
            }

            // 申购交易最大手数校验
            String maxxg = this.getConfigValue("maxxg", "10000");
            if (new BigDecimal(faNewStock.getSgNums()).compareTo(new BigDecimal(maxxg)) > 0) {
                throw new ServiceException(MessageUtils.message("beyond.maximum.lots"), HttpStatus.ERROR);
            }

            // 申购交易最低金额校验
            String psjy0_jine = this.getConfigValue("psjy0_jine", "10000");
            if (new BigDecimal(faNewStock.getSgNums()).multiply(new BigDecimal(faNewStock.getFxPrice())).compareTo(new BigDecimal(psjy0_jine)) < 0) {
                throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 配售风控校验
     * @param faNewStock
     * @throws Exception
     */
    @Override
    public void checkPeiShou(FaNewStock faNewStock) throws Exception {
        // 配售交易时间校验
        this.checkPeiShouTime();

        // 输入框是否开启 pskqsrsgs 默认开启
        String pskqsrsgs = this.getConfigValue("pskqsrsgs", "1");

        // 输入框开启，校验最低手数/最高手数/最低金额
        if ("1".equals(pskqsrsgs)) {
            // 配售交易最低手数校验
            String minimumNums = this.getConfigValue("psjy_ss", "1");
            if (new BigDecimal(faNewStock.getSgNums()).compareTo(new BigDecimal(minimumNums)) < 0) {
                throw new ServiceException(MessageUtils.message("below.minimum.lots"), HttpStatus.ERROR);
            }

            // 配售交易最大手数校验
            String maximumNums = this.getConfigValue("psmax", "10000");
            if (new BigDecimal(faNewStock.getSgNums()).compareTo(new BigDecimal(maximumNums)) > 0) {
                throw new ServiceException(MessageUtils.message("beyond.maximum.lots"), HttpStatus.ERROR);
            }

            // 配售交易最低金额校验
            String minimumAmount = this.getConfigValue("psjy_jine", "10000");
            if (new BigDecimal(faNewStock.getSgNums()).multiply(faNewStock.getPsPrice()).compareTo(new BigDecimal(minimumAmount)) < 0) {
                throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 申购交易时间校验
     */
    @Override
    public void checkShengouTime() throws Exception {
        // 上午交易时间 swjiaoyi
        String swjiaoyi = this.getConfigValue("swshengou", "09:30-11:30");
        // 下午交易时间 xwjiaoyi
        String xwjiaoyi = this.getConfigValue("xwshengou", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(swjiaoyi, xwjiaoyi);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 配售交易时间校验
     * @throws Exception
     */
    @Override
    public void checkPeiShouTime() throws Exception {
        // 上午交易时间 swjiaoyi
        String swjiaoyi = this.getConfigValue("swpstime", "09:30-11:30");
        // 下午交易时间 xwjiaoyi
        String xwjiaoyi = this.getConfigValue("xwpstime", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(swjiaoyi, xwjiaoyi);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 判断节假日
     * @throws Exception
     */
    private void checkHoliday() throws Exception {
        // 节假日不能交易 limit_time
        String limitTime = this.getConfigValue("limit_time", "1");
        if (limitTime.equals("1")) {
            if (isHolidayToday()) {
                throw new ServiceException(MessageUtils.message("holiday.not.trade"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 判断当前时间是否在时间段内
     * @param morning
     * @param afternoon
     */
    private void checkTime(String morning, String afternoon) {
        // 上午时间段
        Date morningStart = new Date();
        Date morningEnd = new Date();
        morningStart.setHours(Integer.parseInt(morning.split("-")[0].split(":")[0]));
        morningStart.setMinutes(Integer.parseInt(morning.split("-")[0].split(":")[1]));
        morningStart.setSeconds(0);
        morningEnd.setHours(Integer.parseInt(morning.split("-")[1].split(":")[0]));
        morningEnd.setMinutes(Integer.parseInt(morning.split("-")[1].split(":")[1]));
        morningEnd.setSeconds(0);

        // 下午时间段
        Date afternoonStart = new Date();
        Date afternoonEnd = new Date();
        afternoonStart.setHours(Integer.parseInt(afternoon.split("-")[0].split(":")[0]));
        afternoonStart.setMinutes(Integer.parseInt(afternoon.split("-")[0].split(":")[1]));
        afternoonStart.setSeconds(0);
        afternoonEnd.setHours(Integer.parseInt(afternoon.split("-")[1].split(":")[0]));
        afternoonEnd.setMinutes(Integer.parseInt(afternoon.split("-")[1].split(":")[1]));
        afternoonEnd.setSeconds(0);

        // 当前时间
        Date now = new Date();
        if (!(now.after(morningStart) && now.before(morningEnd)) && !(now.after(afternoonStart) && now.before(afternoonEnd))) {
            throw new ServiceException(MessageUtils.message("not.trade.time"), HttpStatus.ERROR);
        }
    }

    /**
     * 充值风控校验
     * @param faRecharge
     * @throws Exception
     */
    @Override
    public void checkRecharge(FaRecharge faRecharge) throws Exception {
        // 充值时间
        checkRechargeTime();
        // 最低充值
        String chargeLow = this.getConfigValue("charge_low", "100");
        if (faRecharge.getMoney().compareTo(new BigDecimal(chargeLow)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.recharge.amount"), HttpStatus.ERROR);
        }

        // 最高充值
        String chargeHigh = this.getConfigValue("charge_high", "1000000");
        if (faRecharge.getMoney().compareTo(new BigDecimal(chargeHigh)) > 0) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.recharge.amount"), HttpStatus.ERROR);
        }
    }

    private void checkRechargeTime() throws Exception {
        // 上午交易时间 swjiaoyi
        String swchongzhi = this.getConfigValue("swchongzhi", "09:30-11:30");
        // 下午交易时间 xwjiaoyi
        String xwchongzhi = this.getConfigValue("xwchongzhi", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(swchongzhi, xwchongzhi);
    }

    /**
     * 提现风控校验
     * @param faWithdraw
     * @throws Exception
     */
    @Override
    public void checkWithdraw(FaWithdraw faWithdraw) throws Exception {
        // 提现时间
        checkWithdrawTime();
        // 最低提现
        String zdtixian = this.getConfigValue("zdtixian", "100");
        if (faWithdraw.getMoney().compareTo(new BigDecimal(zdtixian)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.withdraw.amount"), HttpStatus.ERROR);
        }
        // 最高提现
        String toptixian = this.getConfigValue("toptixian", "999999999");
        if (faWithdraw.getMoney().compareTo(new BigDecimal(toptixian)) > 0) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.withdraw.amount"), HttpStatus.ERROR);
        }
        // 一天3次提现
        int txtims = Integer.parseInt(this.getConfigValue("txtims", "3"));
        int withdrawTimes = iFaWithdrawService.getWithdrawTimesToday(faWithdraw.getUserId());
        if (withdrawTimes >= txtims) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.withdraw.times"), HttpStatus.ERROR);
        }
        // 当日提现额度
        BigDecimal withdrawToday = new BigDecimal(this.getConfigValue("withdraw_today", "50000"));
        BigDecimal withdrawAlready = iFaWithdrawService.getWithdrawAlreadyToday(faWithdraw.getUserId());
        // 已提现额度 + 本次提现 > 当日提现额度
        if (withdrawAlready.add(faWithdraw.getMoney()).compareTo(withdrawToday) > 0) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.withdraw.today"), HttpStatus.ERROR);
        }

        // 开启审核通过提现下一次 kq_tx 默认关闭
        String kq_tx = this.getConfigValue("kq_tx", "0");
        // 开启，是否有审核中的提现
        if ("1".equals(kq_tx)) {
            LambdaQueryWrapper<FaWithdraw> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaWithdraw::getUserId, faWithdraw.getUserId());
            lambdaQueryWrapper.eq(FaWithdraw::getDeleteFlag, 0);
            // 未打款
            lambdaQueryWrapper.eq(FaWithdraw::getIsPay, 0);
            long count = iFaWithdrawService.count(lambdaQueryWrapper);
            if (count > 0) {
                throw new ServiceException(MessageUtils.message("exist.withdraw.approving"), HttpStatus.ERROR);
            }
        }
    }

    private void checkWithdrawTime() throws Exception {
        // 上午交易时间 swjiaoyi
        String swtixian = this.getConfigValue("swtixian", "09:30-11:30");
        // 下午交易时间 xwjiaoyi
        String xwtixian = this.getConfigValue("xwtixian", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(swtixian, xwtixian);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 融券交易风控校验
     * @param faStockTrading
     * @return
     * @throws Exception
     */
    @Override
    public void checkRqTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception {
        // 融券交易时间校验
        this.checkRqTradeTime(faExchangeConfig);

        // 融券交易最低手数校验
        String rq_minimum_num = this.getConfigValue("rq_minimum_num", "1");
        if (new BigDecimal(faStockTrading.getTradingNumber()).compareTo(new BigDecimal(rq_minimum_num)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.lots"), HttpStatus.ERROR);
        }

        // 融券交易最低金额校验
        String rq_minimum_amount = this.getConfigValue("rq_minimum_amount", "1000");
        if (faStockTrading.getTradingAmount().compareTo(new BigDecimal(rq_minimum_amount)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
        }
    }

    /**
     * 融券交易时间校验
     * @throws Exception
     */
    private void checkRqTradeTime(FaExchangeConfig faExchangeConfig) throws Exception {
        // 上午交易时间 rq_sw_time
        String rq_sw_time = this.getConfigValue("rq_sw_time", "09:30-11:30");
        // 下午交易时间 rq_xw_time
        String rq_xw_time = this.getConfigValue("rq_xw_time", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(rq_sw_time, rq_xw_time);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 预约重组风控校验
     * @param faStockTrading
     * @throws Exception
     */
    @Override
    public void checkYyczTrade(FaStockTrading faStockTrading, FaExchangeConfig faExchangeConfig) throws Exception {
        // 预约重组交易时间校验
        this.checkYyczTradeTime(faExchangeConfig);

        // 预约重组交易最低手数校验
        String yycz_minimum_num = this.getConfigValue("yycz_minimum_num", "1");
        if (new BigDecimal(faStockTrading.getTradingNumber()).compareTo(new BigDecimal(yycz_minimum_num)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.lots"), HttpStatus.ERROR);
        }

        // 预约重组交易最低金额校验
        String yycz_minimum_amount = this.getConfigValue("yycz_minimum_amount", "1000");
        if (faStockTrading.getTradingAmount().compareTo(new BigDecimal(yycz_minimum_amount)) < 0) {
            throw new ServiceException(MessageUtils.message("below.minimum.amount"), HttpStatus.ERROR);
        }

        // 预约重组交易最高金额校验
        String yycz_maximum_amount = this.getConfigValue("yycz_maximum_amount", "1000");
        if (faStockTrading.getTradingAmount().compareTo(new BigDecimal(yycz_maximum_amount)) > 0) {
            throw new ServiceException(MessageUtils.message("beyond.maximum.amount"), HttpStatus.ERROR);
        }
    }

    private void checkYyczTradeTime(FaExchangeConfig faExchangeConfig) throws Exception {
        // 上午交易时间 swjiaoyi
        String yycz_sw_time = this.getConfigValue("yycz_sw_time", "09:30-11:30");
        // 下午交易时间 xwjiaoyi
        String yycz_xw_time = this.getConfigValue("yycz_xw_time", "13:00-15:00");
        // 判断当前时间是否在时间段内
        checkTime(yycz_sw_time, yycz_xw_time);

        // 判断节假日
        checkHoliday();
    }

    /**
     * 判断今天是否节假日
     * @return
     * @throws Exception
     */
    @Override
    public boolean isHolidayToday() throws Exception {
        return isHoliday(new Date());
    }

    private boolean isHoliday(Date datetime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(datetime);

        // 从缓存取
        Boolean isHoliday = redisCache.getCacheObject(date);
        if (null == isHoliday) {
            // 从接口取
            isHoliday = HolidayUtil.isHoliday(date);
            redisCache.setCacheObject(date, isHoliday, 1, TimeUnit.DAYS);
        }

        if (isHoliday) {
            return true;
        } else {
            return false;
        }
    }

}