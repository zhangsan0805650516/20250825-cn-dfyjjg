package com.ruoyi.biz.strategy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.strategy.service.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.stockUtils.StockUtils;
import com.ruoyi.system.service.ISysConfigService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class FaStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements IFaStrategyService
{
    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private IndiaStrategyService indiaStrategyService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private VietnamStrategyService vietnamStrategyService;

    @Autowired
    private ChinaStrategyService chinaStrategyService;

    @Autowired
    private ThailandStrategyService thailandStrategyService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFaShengouService iFaShengouService;

    private static final String API_KEY = "55add7694cb14a9ba10296e6c1956f8a";

    /**
     * 查询策略
     *
     * @param id 策略主键
     * @return 策略
     */
    @Override
    public FaStrategy selectFaStrategyById(Integer id)
    {
        return faStrategyMapper.selectFaStrategyById(id);
    }

    /**
     * 查询策略列表
     *
     * @param faStrategy 策略
     * @return 策略
     */
    @Override
    public List<FaStrategy> selectFaStrategyList(FaStrategy faStrategy)
    {
        faStrategy.setDeleteFlag(0);
        faStrategy.setClassifyId(0);
        return faStrategyMapper.selectFaStrategyList(faStrategy);
    }

    /**
     * 新增策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaStrategy(FaStrategy faStrategy)
    {
        faStrategy.setCreateTime(DateUtils.getNowDate());
        // 校验code
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        int count = (int) this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new ServiceException(MessageUtils.message("stock.code.exists"), HttpStatus.ERROR);
        }

        return faStrategyMapper.insertFaStrategy(faStrategy);
    }

    /**
     * 修改策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaStrategy(FaStrategy faStrategy)
    {
        faStrategy.setUpdateTime(DateUtils.getNowDate());
        return faStrategyMapper.updateFaStrategy(faStrategy);
    }

    /**
     * 批量删除策略
     *
     * @param ids 需要删除的策略主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStrategyByIds(Integer[] ids)
    {
        return faStrategyMapper.deleteFaStrategyByIds(ids);
    }

    /**
     * 删除策略信息
     *
     * @param id 策略主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStrategyById(Integer id)
    {
        return faStrategyMapper.deleteFaStrategyById(id);
    }

    /**
     * 查询大宗交易
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getDZStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 大宗打开
        lambdaQueryWrapper.eq(FaStrategy::getIsDz, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 查询抢筹列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getQCStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // VIP调研打开
        lambdaQueryWrapper.eq(FaStrategy::getVipQcStatus, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);

        // 把抢筹价格放入大宗价格
        if (!faStrategyIPage.getRecords().isEmpty()) {
            for (FaStrategy strategy : faStrategyIPage.getRecords()) {
                strategy.setZfaPrice(strategy.getVipQcPrice());
                strategy.setTotalZfaNum(strategy.getTotalQcNum());
                strategy.setZfaNum(strategy.getLeftQcNum());
            }
        }
        return faStrategyIPage;
    }

    /**
     * 查询融券列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getRQStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 融券打开
        lambdaQueryWrapper.eq(FaStrategy::getIsRq, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 股票是否存在
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public FaStrategy checkStock(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getId()) {
            lambdaQueryWrapper.eq(FaStrategy::getId, faStrategy.getId());
        }
        if (StringUtils.isNotEmpty(faStrategy.getCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        }
        if (StringUtils.isNotEmpty(faStrategy.getAllCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getAllCode, faStrategy.getAllCode());
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        FaStrategy selectOne = faStrategyMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }
        return selectOne;
    }

    /**
     * 新股转入股票列表
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public FaStrategy addFromNewStock(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faNewStock.getCode());
        FaStrategy faStrategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faStrategy)) {
            faStrategy = new FaStrategy();
            faStrategy.setCode(faNewStock.getCode());
            faStrategy.setTitle(faNewStock.getName());
            faStrategy.setAllCode(allCodeFormat(faNewStock));
            faStrategy.setType(faNewStock.getSgType());
            faStrategy.setDeleteFlag(0);
            faStrategy.setCreateTime(DateUtils.getNowDate());
            this.save(faStrategy);
        }
        return faStrategy;
    }

    /**
     * 股票代码格式
     * @param faNewStock
     * @return
     * @throws Exception
     */
    private String allCodeFormat(FaNewStock faNewStock) throws Exception {
        if (1 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        } else if (2 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (3 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (4 == faNewStock.getSgType()) {
            return "bj" + faNewStock.getCode();
        } else if (5 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        }
        return faNewStock.getCode();
    }

    /**
     * 查询股票详情
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public FaStrategy getStockDetail(FaStrategy faStrategy) throws Exception {
        if (null == faStrategy.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getId()) {
            lambdaQueryWrapper.eq(FaStrategy::getId, faStrategy.getId());
        }
        if (StringUtils.isNotEmpty(faStrategy.getCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        }
        if (null != faStrategy.getType()) {
            lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        FaStrategy strategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(strategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, strategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getLastViewTime, new Date());
        this.update(lambdaUpdateWrapper);

//        // 更新实时价格
//        getCurrentPrice(strategy);
//
//        // 再从数据库取一次
//        strategy = this.getById(strategy.getId());
//
//        // 买五卖五
//        strategy = getBuySell(strategy);
        return strategy;
    }

    /**
     * 买五卖五
     * @param strategy
     * @throws Exception
     */
    private FaStrategy getBuySell(FaStrategy strategy) throws Exception {
        String currentInfo = StockUtils.getCurrentInfo(strategy.getAllCode());
        if (StringUtils.isNotEmpty(currentInfo)) {
            String[] info = currentInfo.split("~");
            if (info.length > 4) {
                strategy.setBuy1(new BigDecimal(info[9]));
                strategy.setBuy1Num(new BigDecimal(info[10]));
                strategy.setBuy2(new BigDecimal(info[11]));
                strategy.setBuy2Num(new BigDecimal(info[12]));
                strategy.setBuy3(new BigDecimal(info[13]));
                strategy.setBuy3Num(new BigDecimal(info[14]));
                strategy.setBuy4(new BigDecimal(info[15]));
                strategy.setBuy4Num(new BigDecimal(info[16]));
                strategy.setBuy5(new BigDecimal(info[17]));
                strategy.setBuy5Num(new BigDecimal(info[18]));
                strategy.setSell1(new BigDecimal(info[19]));
                strategy.setSell1Num(new BigDecimal(info[20]));
                strategy.setSell2(new BigDecimal(info[21]));
                strategy.setSell2Num(new BigDecimal(info[22]));
                strategy.setSell3(new BigDecimal(info[23]));
                strategy.setSell3Num(new BigDecimal(info[24]));
                strategy.setSell4(new BigDecimal(info[25]));
                strategy.setSell4Num(new BigDecimal(info[26]));
                strategy.setSell5(new BigDecimal(info[27]));
                strategy.setSell5Num(new BigDecimal(info[28]));

                strategy.setBuyTotalNum(strategy.getBuy1Num().add(strategy.getBuy2Num()).add(strategy.getBuy3Num()).add(strategy.getBuy4Num()).add(strategy.getBuy5Num()));
                strategy.setSellTotalNum(strategy.getSell1Num().add(strategy.getSell2Num()).add(strategy.getSell3Num()).add(strategy.getSell4Num()).add(strategy.getSell5Num()));
            }
        }
        return strategy;
    }

    /**
     * 搜素股票
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> searchStock(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faStrategy.getQueryString())) {
            // 交易所类型
//            if (null != faStrategy.getType()) {
//                lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
//            }
            lambdaQueryWrapper.and(i -> i.like(FaStrategy::getCode, faStrategy.getQueryString())
                    .or().like(FaStrategy::getAllCode, faStrategy.getQueryString())
                    .or().like(FaStrategy::getTitle, faStrategy.getQueryString())
            );
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 20 ");
        List<FaStrategy> faStrategyList = this.list(lambdaQueryWrapper);
        return faStrategyList;
    }

    /**
     * 获取股票实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public BigDecimal getCurrentPrice(FaStrategy faStrategy) throws Exception {
        BigDecimal currentPrice = BigDecimal.ZERO;

        String country = configService.selectConfigByKey("country");
        // 印度
        if("india".equalsIgnoreCase(country)) {
            currentPrice = indiaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("vietnam".equalsIgnoreCase(country)) {
            currentPrice = vietnamStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("china".equalsIgnoreCase(country)) {
            currentPrice = chinaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("thailand".equalsIgnoreCase(country)) {
            currentPrice = thailandStrategyService.getStockCurrentPrice(faStrategy);
        }
        return currentPrice;
    }

    /**
     * 修改股票状态
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public AjaxResult changeStockStatus(FaStrategy faStrategy) throws Exception {
        if (StringUtils.isNotEmpty(faStrategy.getStatusType())) {
            LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
            switch (faStrategy.getStatusType()) {
                case "zhiding":
                    lambdaUpdateWrapper.set(FaStrategy::getZhiding, faStrategy.getStatus());
                    break;
                case "status":
                    lambdaUpdateWrapper.set(FaStrategy::getStatus, faStrategy.getStatus());
                    break;
                case "currentStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getCurrentStatus, faStrategy.getStatus());
                    break;
                case "qcStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getQcStatus, faStrategy.getStatus());
                    break;
                case "isHide":
                    lambdaUpdateWrapper.set(FaStrategy::getIsHide, faStrategy.getStatus());
                    break;
                case "isPz":
                    lambdaUpdateWrapper.set(FaStrategy::getIsPz, faStrategy.getStatus());
                    break;
                case "isDz":
                    lambdaUpdateWrapper.set(FaStrategy::getIsDz, faStrategy.getStatus());
                    break;
                case "vipQcStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getVipQcStatus, faStrategy.getStatus());
                    break;
                case "isRq":
                    lambdaUpdateWrapper.set(FaStrategy::getIsRq, faStrategy.getStatus());
                    break;
                default:
                    break;
            }
            this.update(lambdaUpdateWrapper);
            return AjaxResult.success(MessageUtils.message("operation.success"));
        }
        return AjaxResult.error();
    }

    /**
     * 查询股票列表 sortBy 排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getStrategyList(FaStrategy faStrategy) throws Exception {
        if (null == faStrategy.getSortBy() || null == faStrategy.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
//        if (null != faStrategy.getType()) {
//            lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
//        }
        switch (faStrategy.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiTrade);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiTrade);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiPricechange);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiAmount);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiAmount);
                }
                break;
            // 换手率
            case 5:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangeHands);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangeHands);
                }
                break;
            // 昨收价
            case 6:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiSettlement);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiSettlement);
                }
                break;
            // 今开价
            case 7:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiOpen);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiOpen);
                }
                break;
            // 最高价
            case 8:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiHigh);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiHigh);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 查询首页5大指数
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> getIndexList(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        List<FaStrategy> faStrategyList = redisCache.getCacheObject(Constants.HOME_INDEX);
        if (null == faStrategyList || faStrategyList.isEmpty()) {
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 1);
            faStrategyList = this.list(lambdaQueryWrapper);
            for (FaStrategy strategy : faStrategyList) {
                List<Map<String, String>> list = new ArrayList<>();
                String result = HttpUtils.sendGet("https://web.ifzq.gtimg.cn/appstock/app/minute/query?_var=min_data_" + strategy.getAllCode() + "&code=" + strategy.getAllCode() + "&r=" + System.currentTimeMillis());
                if (StringUtils.isNotEmpty(result)) {
                    result = result.substring(result.indexOf("=") + 1);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.containsKey("data")) {
                        jsonObject = jsonObject.getJSONObject("data");
                        if (jsonObject.containsKey(strategy.getAllCode())) {
                            jsonObject = jsonObject.getJSONObject(strategy.getAllCode());
                            if (jsonObject.containsKey("data")) {
                                jsonObject = jsonObject.getJSONObject("data");
                                if (jsonObject.containsKey("data")) {
                                    String date = jsonObject.getString("date");
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (!jsonArray.isEmpty()) {
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd HHmmss");
                                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            String info = jsonArray.getString(i);
                                            String[] infos = info.split(" ");
                                            Map<String, String> map = new HashMap<>();
                                            map.put("close", infos[1]);
                                            String datetime = sdf2.format(sdf1.parse(date + " " + infos[0] + "00"));
                                            map.put("datetime", datetime);
                                            map.put("high", infos[1]);
                                            map.put("low", infos[1]);
                                            map.put("open", infos[1]);
                                            map.put("timestamp", String.valueOf(sdf2.parse(datetime).getTime()));
                                            map.put("volume", infos[2]);
                                            list.add(map);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                strategy.setKline(list);
            }

            // 存入redis，保留5分钟
            redisCache.setCacheObject(Constants.HOME_INDEX, faStrategyList, 2, TimeUnit.MINUTES);
        }
        return faStrategyList;
    }

    /**
     * 查询涨跌对比
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Integer> getRiseAndFall(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        Map<String, Integer> map = redisCache.getCacheObject(Constants.RISE_AND_FALL);
        if (null == map) {
            map = new HashMap<>();
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 涨数量
            lambdaQueryWrapper.gt(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long rise = this.count(lambdaQueryWrapper);
            map.put("rise", (int) rise);
            // 跌数量
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.lt(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long fall = this.count(lambdaQueryWrapper);
            map.put("fall", (int) fall);
            // 平数量
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long flat = this.count(lambdaQueryWrapper);
            map.put("flat", (int) flat);

            // 存入redis，保留5分钟
            redisCache.setCacheObject(Constants.RISE_AND_FALL, map, 5, TimeUnit.MINUTES);
        }
        return map;
    }

    /**
     * 板块涨跌幅
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getBkHotList(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        JSONArray jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType());
        // redis存在
        if (null != jsonArray && !jsonArray.isEmpty()) {
            return jsonArray;
        }

        // redis里不存在，从接口取
        jsonArray = new JSONArray();
        // 接口token
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        String result = HttpUtils.sendGet(Constants.QI_ZHANG_URL + "/bkhotlst?pz=" + faStrategy.getBkType() + "&page=1&px=1&token=" + token);
        if (StringUtils.isNotEmpty(result)) {
            jsonArray = JSONArray.parse(result);
            if (!jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (ObjectUtils.isNotEmpty(jsonObject)) {
                        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaStrategy::getCode, jsonObject.getString("stockcode"));
                        lambdaQueryWrapper.last(" limit 1 ");
                        FaStrategy fa = this.getOne(lambdaQueryWrapper);
                        if (ObjectUtils.isNotEmpty(fa)) {
                            jsonObject.put("id", fa.getId());
                            jsonObject.put("allCode", fa.getAllCode());
                        }
                    }
                }
                // 接口取成功，存入redis，保留5分钟
                redisCache.setCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType(), jsonArray, 5, TimeUnit.MINUTES);
                redisCache.setCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType() + ":old", jsonArray);
            }
        }

        if (null == jsonArray || jsonArray.isEmpty()) {
            // 从redis取最后保留的数据
            jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType() + ":old");
        }

        return jsonArray;
    }

    /**
     * 板块涨跌幅-新浪 1地区 2板块 3概念
     * @param faStrategy bkType
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getBkHotListFromSina(FaStrategy faStrategy) throws Exception {
        // 地区 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=area
        // 行业 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=industry
        // 概念 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=class
        JSONArray jsonArray = new JSONArray();
        switch (faStrategy.getBkType()) {
            case 1:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=area");
                break;
            case 2:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=industry");
                break;
            case 3:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=class");
                break;
            default:
                break;
        }
        return jsonArray;
    }

    /**
     * 板块涨跌幅
     * @param url
     * @return
     * @throws Exception
     */
    private JSONArray getBkHotListFromSina(String url) throws Exception {
        String result = HttpUtils.sendGet(url);
        // todo
        return null;
    }

    /**
     * 资金流向
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getMoneyFlow(FaStrategy faStrategy) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        // redis先查询
        jsonObject = redisCache.getCacheObject("MoneyFlow" + date);

        // redis里存在
        if (ObjectUtils.isNotEmpty(jsonObject)) {
            return jsonObject;
        }

        // redis不存在，请求接口
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        String result = HttpUtils.sendGet(Constants.QI_ZHANG_URL + "/moneyflow_hsgt?token=" + token);
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONArray.parse(result);
            if (!jsonArray.isEmpty()) {
                jsonObject = jsonArray.getJSONObject(0);

                // south_money_true 真南向资金 south_money 主力资金
                String south_money = jsonObject.getString("south_money");
                jsonObject.put("south_money_true", south_money);

                // 主力资金
                String zhuli = getMainNetInFlow();
                jsonObject.put("south_money", zhuli);

                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("trade_date") && date.equals(jsonObject.getString("trade_date"))) {
                    // 更新到redis
                    redisCache.setCacheObject("MoneyFlow" + date, jsonObject, 1, TimeUnit.HOURS);
                    redisCache.setCacheObject("MoneyFlow", jsonObject);
                }
            }
        }

        if (ObjectUtils.isEmpty(jsonObject)) {
            // 从redis取最后保留的数据
            jsonObject = redisCache.getCacheObject("MoneyFlow");
        }

        return jsonObject;
    }

    private String getMainNetInFlow() throws Exception {
        BigDecimal zhuli = BigDecimal.ZERO;
        String result = HttpUtils.sendGet("https://igp-img-upload-1251605428.cos.ap-shanghai.myqcloud.com/cos_data/main_net_inflow_pc.json");
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("data")) {
                jsonObject = jsonObject.getJSONObject("data");
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("main_net_inflow")) {
                    String main_net_inflow = jsonObject.getString("main_net_inflow");
                    if (StringUtils.isNotEmpty(main_net_inflow) && main_net_inflow.indexOf("亿") > 0) {
                        main_net_inflow = main_net_inflow.replace("亿", "");
                        zhuli = new BigDecimal(main_net_inflow).multiply(new BigDecimal(100));
                    }
                }

            }
        }
        return zhuli.toString();
    }

    /**
     * 查询推荐列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getRecommendStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getType()) {
            lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
        }
        lambdaQueryWrapper.eq(FaStrategy::getZhiding, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 上证指数k线
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> getSHKline(FaStrategy faStrategy) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();

        String country = configService.selectConfigByKey("country");
        // 印度
        if("india".equalsIgnoreCase(country)) {
//            currentPrice = indiaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("vietnam".equalsIgnoreCase(country)) {
//            currentPrice = vietnamStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("china".equalsIgnoreCase(country)) {
            list = chinaStrategyService.getSHKline(faStrategy);
        } else if ("thailand".equalsIgnoreCase(country)) {
//            currentPrice = thailandStrategyService.getStockCurrentPrice(faStrategy);
        }
        return list;
    }

    /**
     * 前瞻会议 pz=2 取第四个
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getQzhy() throws Exception {
        int pz = 2;
        // 先从redis取
        JSONArray jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + pz);

        // redis存在
        if (null != jsonArray && !jsonArray.isEmpty()) {
            return jsonArray;
        }

        // redis里不存在，从接口取
        jsonArray = new JSONArray();
        // 接口token
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        String result = HttpUtils.sendGet(Constants.QI_ZHANG_URL + "/bkhotlst?pz=" + pz + "&page=1&px=1&token=" + token);
        if (StringUtils.isNotEmpty(result)) {
            jsonArray = JSONArray.parse(result);
            if (!jsonArray.isEmpty()) {
                for (int i = 3; i < 4; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (ObjectUtils.isNotEmpty(jsonObject)) {
                        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaStrategy::getCode, jsonObject.getString("stockcode"));
                        lambdaQueryWrapper.last(" limit 1 ");
                        FaStrategy fa = this.getOne(lambdaQueryWrapper);
                        if (ObjectUtils.isNotEmpty(fa)) {
                            jsonObject.put("id", fa.getId());
                            jsonObject.put("allCode", fa.getAllCode());
                        }
                    }
                }
                // 接口取成功，存入redis，保留5分钟
                redisCache.setCacheObject(Constants.BK_HOT_LST + pz, jsonArray, 10, TimeUnit.MINUTES);
                redisCache.setCacheObject(Constants.BK_HOT_LST + pz + ":old", jsonArray);
            }
        }

        if (null == jsonArray || jsonArray.isEmpty()) {
            // 从redis取最后保留的数据
            jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + pz + ":old");
        }

        return jsonArray;
    }

    /**
     * 取涨幅前十之一，业绩略超预期，稳健增长
     * @return
     * @throws Exception
     */
    @Override
    public FaStrategy getYbjx() throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangepercent);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 10 ");
        List<FaStrategy> list = this.list(lambdaQueryWrapper);

        Random random = new Random();
        FaStrategy faStrategy = list.get(random.nextInt(list.size()));

        return faStrategy;
    }

    /**
     * 前瞻会议 pz=2 取前三个
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getRmhy() throws Exception {
        int pz = 2;
        // 先从redis取
        JSONArray jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + pz);

        // redis存在
        if (null != jsonArray && !jsonArray.isEmpty()) {
            return jsonArray;
        }

        // redis里不存在，从接口取
        jsonArray = new JSONArray();
        // 接口token
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        String result = HttpUtils.sendGet(Constants.QI_ZHANG_URL + "/bkhotlst?pz=" + pz + "&page=1&px=1&token=" + token);
        if (StringUtils.isNotEmpty(result)) {
            jsonArray = JSONArray.parse(result);
            if (!jsonArray.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (ObjectUtils.isNotEmpty(jsonObject)) {
                        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(FaStrategy::getCode, jsonObject.getString("stockcode"));
                        lambdaQueryWrapper.last(" limit 1 ");
                        FaStrategy fa = this.getOne(lambdaQueryWrapper);
                        if (ObjectUtils.isNotEmpty(fa)) {
                            jsonObject.put("id", fa.getId());
                            jsonObject.put("allCode", fa.getAllCode());
                        }
                    }
                }
                // 接口取成功，存入redis，保留5分钟
                redisCache.setCacheObject(Constants.BK_HOT_LST + pz, jsonArray, 10, TimeUnit.MINUTES);
                redisCache.setCacheObject(Constants.BK_HOT_LST + pz + ":old", jsonArray);
            }
        }

        if (null == jsonArray || jsonArray.isEmpty()) {
            // 从redis取最后保留的数据
            jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + pz + ":old");
        }

        return jsonArray;
    }

    /**
     * 取涨幅前十之二，业绩略超预期，稳健增长
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> getDxjj() throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangepercent);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 10 ");
        List<FaStrategy> list = this.list(lambdaQueryWrapper);

        Collections.shuffle(list); // 随机打乱list顺序

        // 获取前两个元素作为随机选中的数据
        List<FaStrategy> randomElements = list.subList(0, 2);

        return randomElements;
    }

    /**
     * 涨跌柱状图
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Integer> getRiseAndFallBar(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        Map<String, Integer> map = redisCache.getCacheObject(Constants.RISE_AND_FALL_BAR);
        if (null == map) {
            map = new HashMap<>();
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 涨 9.9% < a
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
//            lambdaQueryWrapper.gt(FaStrategy::getCaiChangepercent, 9.95);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            lambdaQueryWrapper.last(" and cai_trade = cai_limit_up_price ");
            long rise9 = this.count(lambdaQueryWrapper);
            map.put("rise9", (int) rise9);
            // 涨 7% < a
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.gt(FaStrategy::getCaiChangepercent, 7);
            lambdaQueryWrapper.le(FaStrategy::getCaiChangepercent, 9.95);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long rise7 = this.count(lambdaQueryWrapper);
            map.put("rise7", (int) rise7);
            // 涨 5% < a <= 7%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.gt(FaStrategy::getCaiChangepercent, 5);
            lambdaQueryWrapper.le(FaStrategy::getCaiChangepercent, 7);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long rise57 = this.count(lambdaQueryWrapper);
            map.put("rise57", (int) rise57);
            // 涨 3% < a <= 5%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.gt(FaStrategy::getCaiChangepercent, 3);
            lambdaQueryWrapper.le(FaStrategy::getCaiChangepercent, 5);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long rise35 = this.count(lambdaQueryWrapper);
            map.put("rise35", (int) rise35);
            // 涨 0% < a <= 3%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.gt(FaStrategy::getCaiChangepercent, 0);
            lambdaQueryWrapper.le(FaStrategy::getCaiChangepercent, 3);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long rise03 = this.count(lambdaQueryWrapper);
            map.put("rise03", (int) rise03);
            // 平 a == 0%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.notLike(FaStrategy::getTitle, "ST");
            lambdaQueryWrapper.eq(FaStrategy::getCaiChangepercent, 0);
            lambdaQueryWrapper.ne(FaStrategy::getCaiBuy, 0);
            lambdaQueryWrapper.ne(FaStrategy::getCaiSell, 0);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long flat = this.count(lambdaQueryWrapper);
            map.put("flat", (int) flat);
            // 跌 -3% <= a < 0%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.ge(FaStrategy::getCaiChangepercent, -3);
            lambdaQueryWrapper.lt(FaStrategy::getCaiChangepercent, 0);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long fall03 = this.count(lambdaQueryWrapper);
            map.put("fall03", (int) fall03);
            // 跌 -5% <= a < -3%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.ge(FaStrategy::getCaiChangepercent, -5);
            lambdaQueryWrapper.lt(FaStrategy::getCaiChangepercent, -3);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long fall35 = this.count(lambdaQueryWrapper);
            map.put("fall35", (int) fall35);
            // 跌 -7% <= a < -5%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.ge(FaStrategy::getCaiChangepercent, -7);
            lambdaQueryWrapper.lt(FaStrategy::getCaiChangepercent, -5);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long fall57 = this.count(lambdaQueryWrapper);
            map.put("fall57", (int) fall57);
            // 跌 a < -7%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            lambdaQueryWrapper.ge(FaStrategy::getCaiChangepercent, -9.95);
            lambdaQueryWrapper.lt(FaStrategy::getCaiChangepercent, -7);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            long fall7 = this.count(lambdaQueryWrapper);
            map.put("fall7", (int) fall7);
            // 跌 a < -9.9%
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
//            lambdaQueryWrapper.lt(FaStrategy::getCaiChangepercent, -9.95);
            lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
            lambdaQueryWrapper.last(" and cai_trade = cai_limit_down_price ");
            long fall9 = this.count(lambdaQueryWrapper);
            map.put("fall9", (int) fall9);

            // 存入redis，保留5分钟
            redisCache.setCacheObject(Constants.RISE_AND_FALL_BAR, map, 5, TimeUnit.MINUTES);
        }
        return map;
    }

    /**
     * 龙虎榜
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> getDragonTigerList(FaStrategy faStrategy) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String date = sdf.format(cal.getTime());

        if (null == faStrategy.getTradeDate()) {
            faStrategy.setTradeDate(date);
        }

        List<FaStrategy> list = new ArrayList<>();
        // 先从redis取
        JSONArray jsonArray = redisCache.getCacheObject(Constants.DRAGON_TIGER + faStrategy.getTradeDate());
        if (null != jsonArray) {
            redisCache.setCacheObject(Constants.DRAGON_TIGER + date + ":old", jsonArray);
        }

        // redis里不存在，从接口取
        if (null == jsonArray || jsonArray.isEmpty()) {
            // 接口token
            String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
            String result = HttpUtils.sendGet(Constants.QI_ZHANG_URL + "/top_list?code=&trade_date=" + faStrategy.getTradeDate() +"&token=" + token);
            if (StringUtils.isNotEmpty(result)) {
                jsonArray = JSONArray.parse(result);
                if (!jsonArray.isEmpty()) {
                    // 接口取成功，存入redis，保留1天
                    redisCache.setCacheObject(Constants.DRAGON_TIGER + faStrategy.getTradeDate(), jsonArray, 1, TimeUnit.DAYS);
                    redisCache.setCacheObject(Constants.DRAGON_TIGER + faStrategy.getTradeDate() + ":old", jsonArray);
                    redisCache.setCacheObject(Constants.DRAGON_TIGER + date + ":old", jsonArray);
                }
            }
        }

        if (null == jsonArray || jsonArray.isEmpty()) {
            // 从redis取最后保留的数据
            jsonArray = redisCache.getCacheObject(Constants.DRAGON_TIGER + faStrategy.getTradeDate() + ":old");
            if (null != jsonArray) {
                redisCache.setCacheObject(Constants.DRAGON_TIGER + date + ":old", jsonArray);
            }
        }

        if (null != jsonArray && !jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (ObjectUtils.isNotEmpty(jsonObject)) {
                    String ts_code = jsonObject.getString("ts_code");
                    String[] tsCode = ts_code.split("\\.");
                    FaStrategy strategy = new FaStrategy();
                    strategy.setTitle(jsonObject.getString("name"));
                    strategy.setTradeDate(jsonObject.getString("trade_date"));
                    strategy.setCode(tsCode[0]);
                    strategy.setAllCode(tsCode[1].toLowerCase() + tsCode[0]);
                    strategy.setId(getStrategyId(strategy.getAllCode()));
                    strategy.setCaiSettlement(jsonObject.getBigDecimal("close"));
                    strategy.setCaiAmount(jsonObject.getBigDecimal("l_buy"));
                    strategy.setCaiChangepercent(jsonObject.getBigDecimal("pct_change").setScale(2, RoundingMode.HALF_UP));
                    list.add(strategy);
                }
            }
        }
        return list;
    }

    private Integer getStrategyId(String allCode) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getAllCode, allCode);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 1 ");
        FaStrategy faStrategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faStrategy)) {
            return faStrategy.getId();
        } else {
            return 0;
        }
    }

    /**
     * 查询爱股票领涨概念板块
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getAiStockUpPlate(FaStrategy faStrategy) throws Exception {
        JSONObject jsonObject = redisCache.getCacheObject(Constants.AI_STOCK_UP_PLATE + faStrategy.getKind() + ":" + faStrategy.getThemeType());
        if (ObjectUtils.isEmpty(jsonObject)) {
            String result = HttpUtils.sendGet("https://apis.aigupiao.com/theme/up_down_concept_pc?page=1&number=8&kind=" + faStrategy.getKind() + "&md=c5aeae1346b782f8e5056145ccaa67de&theme_type=" + faStrategy.getThemeType());
            if (StringUtils.isNotEmpty(result)) {
                jsonObject = JSONObject.parseObject(result);
                // 存入redis，保留5分钟
                redisCache.setCacheObject(Constants.AI_STOCK_UP_PLATE + faStrategy.getKind() + ":" + faStrategy.getThemeType(), jsonObject, 5, TimeUnit.MINUTES);
            }
        }
        return jsonObject;
    }

    /**
     * 修改预约重组
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public AjaxResult submitFormYycz(FaStrategy faStrategy) throws Exception {
        LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
        lambdaUpdateWrapper.set(FaStrategy::getYyczSwitch, faStrategy.getYyczSwitch());
        lambdaUpdateWrapper.set(FaStrategy::getYyczNotice, faStrategy.getYyczNotice());
        lambdaUpdateWrapper.set(FaStrategy::getYyczTotalNum, faStrategy.getYyczTotalNum());
        lambdaUpdateWrapper.set(FaStrategy::getYyczLeftNum, faStrategy.getYyczLeftNum());
        lambdaUpdateWrapper.set(FaStrategy::getYyczTotalQuota, faStrategy.getYyczTotalQuota());
        lambdaUpdateWrapper.set(FaStrategy::getYyczLeftQuota, faStrategy.getYyczLeftQuota());
        lambdaUpdateWrapper.set(FaStrategy::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
        return AjaxResult.success();
    }

    /**
     * 查询预约重组列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getYyczStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 预约重组打开
        lambdaQueryWrapper.eq(FaStrategy::getYyczSwitch, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 新股大宗数量统计
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Integer> getNewCountAndDz(FaStrategy faStrategy) throws Exception {
        Map<String, Integer> map = new HashMap<>();

        // 新股申购
        LambdaQueryWrapper<FaNewStock> newStockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        newStockLambdaQueryWrapper.eq(FaNewStock::getSgSwitch, 1);
        newStockLambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        int newCount = Math.toIntExact(iFaShengouService.count(newStockLambdaQueryWrapper));
        map.put("new", newCount);

        // 新股配售
        newStockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        newStockLambdaQueryWrapper.eq(FaNewStock::getXxSwitch, 1);
        newStockLambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        int ps = Math.toIntExact(iFaShengouService.count(newStockLambdaQueryWrapper));
        map.put("ps", ps);

        // 大宗
        LambdaQueryWrapper<FaStrategy> strategyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        strategyLambdaQueryWrapper.eq(FaStrategy::getIsDz, 1);
        strategyLambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        int dzCount = Math.toIntExact(faStrategyMapper.selectCount(strategyLambdaQueryWrapper));
        map.put("dz", dzCount);
        return map;
    }

}