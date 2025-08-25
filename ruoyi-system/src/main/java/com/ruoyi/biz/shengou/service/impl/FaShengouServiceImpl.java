package com.ruoyi.biz.shengou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgList.service.IFaSgListService;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.shengou.mapper.FaShengouMapper;
import com.ruoyi.biz.shengou.service.IFaShengouService;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockSgSecond.service.IFaStockSgSecondService;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 新股列表Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class FaShengouServiceImpl extends ServiceImpl<FaShengouMapper, FaNewStock> implements IFaShengouService
{
    @Autowired
    private FaShengouMapper faShengouMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaSgListService iFaSgListService;

    @Autowired
    private IFaStockSgSecondService iFaStockSgSecondService;

    @Autowired
    private IFaStrategyService iFaStrategyService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaSgjiaoyiService iFaSgjiaoyiService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询新股列表
     *
     * @param id 新股列表主键
     * @return 新股列表
     */
    @Override
    public FaNewStock selectFaShengouById(Long id)
    {
        return faShengouMapper.selectFaShengouById(id);
    }

    /**
     * 查询新股列表列表
     *
     * @param faNewStock 新股列表
     * @return 新股列表
     */
    @Override
    public List<FaNewStock> selectFaShengouList(FaNewStock faNewStock)
    {
        faNewStock.setDeleteFlag(0);
        return faShengouMapper.selectFaShengouList(faNewStock);
    }

    /**
     * 新增新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaShengou(FaNewStock faNewStock)
    {
        if (StringUtils.isEmpty(faNewStock.getCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 是否已存在
        LambdaQueryWrapper<FaNewStock> newStockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        newStockLambdaQueryWrapper.eq(FaNewStock::getCode, faNewStock.getCode());
        newStockLambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        FaNewStock checkOne = this.getOne(newStockLambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(checkOne)) {
            throw new ServiceException(MessageUtils.message("new.stock.exists"), HttpStatus.ERROR);
        }

        // 股票
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faNewStock.getCode());
        FaStrategy faStrategy = iFaStrategyService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faNewStock)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }
        faNewStock.setName(faStrategy.getTitle());
        faNewStock.setCode(faStrategy.getCode());
        faNewStock.setAllCode(faStrategy.getAllCode());
        faNewStock.setSgType(faStrategy.getType());
        faNewStock.setCreateTime(DateUtils.getNowDate());
        return faShengouMapper.insertFaShengou(faNewStock);
    }

    /**
     * 修改新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaShengou(FaNewStock faNewStock)
    {
        // 更新所有申购配售的上市时间
//        if (null != faNewStock.getSsDate()) {
//            // 申购
//            LambdaUpdateWrapper<FaSgList> sgListLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//            sgListLambdaUpdateWrapper.eq(FaSgList::getShengouId, faNewStock.getId());
//            sgListLambdaUpdateWrapper.set(FaSgList::getSgSsDate, faNewStock.getSsDate());
//            sgListLambdaUpdateWrapper.set(FaSgList::getUpdateTime, new Date());
//            iFaSgListService.update(sgListLambdaUpdateWrapper);
//            // 配售
//            LambdaUpdateWrapper<FaSgjiaoyi> sgjiaoyiLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//            sgjiaoyiLambdaUpdateWrapper.eq(FaSgjiaoyi::getShengouId, faNewStock.getId());
//            sgjiaoyiLambdaUpdateWrapper.set(FaSgjiaoyi::getSgSsDate, faNewStock.getSsDate());
//            iFaSgjiaoyiService.update(sgjiaoyiLambdaUpdateWrapper);
//        }

        faNewStock.setUpdateTime(DateUtils.getNowDate());
        return faShengouMapper.updateFaShengou(faNewStock);
    }

    /**
     * 批量删除新股列表
     *
     * @param ids 需要删除的新股列表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaShengouByIds(Long[] ids)
    {
        return faShengouMapper.deleteFaShengouByIds(ids);
    }

    /**
     * 删除新股列表信息
     *
     * @param id 新股列表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaShengouById(Long id)
    {
        return faShengouMapper.deleteFaShengouById(id);
    }

    /**
     * 查询新股申购列表，按申购日期分类排序
     * @return
     * @throws Exception
     */
    @Override
    public List<FaNewStock> getShengouListByGroup(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewStock::getSgSwitch, 1);
        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        // 申购日期顺序
        lambdaQueryWrapper.orderByAsc(FaNewStock::getSgDate);
        List<FaNewStock> list = this.list(lambdaQueryWrapper);
//        if (list.size() > 0) {
//            // 申购日期集合
//            Set<Date> dateSet = new LinkedHashSet<>();
//            for (FaNewStock shengou : list) {
//                dateSet.add(shengou.getSgDate());
//            }
//            // 按日期分类
//            LinkedHashMap<String, List<FaNewStock>> map = new LinkedHashMap<>();
//            for (Date date : dateSet) {
//                List<FaNewStock> faShengouList = new ArrayList<>();
//                for (FaNewStock shengou : list) {
//                    if (shengou.getSgDate().equals(date)) {
//                        faShengouList.add(shengou);
//                    }
//                }
//                map.put(sdf1.format(date), faShengouList);
//            }
//            return map;
//        }

        // 查询是否已申购
        if (!list.isEmpty()) {
            for (FaNewStock newStock : list) {
                LambdaQueryWrapper<FaSgList> sgListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                sgListLambdaQueryWrapper.eq(FaSgList::getUserId, faNewStock.getMemberId());
                sgListLambdaQueryWrapper.eq(FaSgList::getShengouId, newStock.getId());
                long count = iFaSgListService.count(sgListLambdaQueryWrapper);
                if (count > 0) {
                    newStock.setIsSg(1);
                } else {
                    newStock.setIsSg(0);
                }
            }
        }

        return list;
    }

    /**
     * 查询配售列表(申购=配售)
     * @param faNewStock
     * @return
     * @throws Exception
     */
    @Override
    public List<FaNewStock> getSgSecondList(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewStock::getXxSwitch, 1);
        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        // 申购日期顺序
        lambdaQueryWrapper.orderByAsc(FaNewStock::getSgDate);
        List<FaNewStock> list = this.list(lambdaQueryWrapper);
//        if (list.size() > 0) {
//            // 申购日期集合
//            Set<Date> dateSet = new LinkedHashSet<>();
//            for (FaNewStock shengou : list) {
//                dateSet.add(shengou.getSgDate());
//            }
//            // 按日期分类
//            LinkedHashMap<String, List<FaNewStock>> map = new LinkedHashMap<>();
//            for (Date date : dateSet) {
//                List<FaNewStock> faShengouList = new ArrayList<>();
//                for (FaNewStock shengou : list) {
//                    if (shengou.getSgDate().equals(date)) {
//                        faShengouList.add(shengou);
//                    }
//                }
//                map.put(sdf1.format(date), faShengouList);
//            }
//            return map;
//        }

        // 查询是否已申购
        if (!list.isEmpty()) {
            for (FaNewStock newStock : list) {
                LambdaQueryWrapper<FaStockSgSecond> stockSgSecondLambdaQueryWrapper = new LambdaQueryWrapper<>();
                stockSgSecondLambdaQueryWrapper.eq(FaStockSgSecond::getUserId, faNewStock.getMemberId());
                stockSgSecondLambdaQueryWrapper.eq(FaStockSgSecond::getShengouId, newStock.getId());
                long count = iFaStockSgSecondService.count(stockSgSecondLambdaQueryWrapper);
                if (count > 0) {
                    newStock.setIsSg(1);
                } else {
                    newStock.setIsSg(0);
                }
            }
        }

        return list;
    }

    /**
     * 查询新股配售列表, 按配售日期分类排序
     * @return
     * @throws Exception
     */
    @Override
    public List<FaNewStock> getPeiShouListByGroup() throws Exception {
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaNewStock::getXxSwitch, 1);
        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        // 申购日期顺序
        lambdaQueryWrapper.orderByAsc(FaNewStock::getSgDate);
        List<FaNewStock> list = this.list(lambdaQueryWrapper);

        for (FaNewStock faNewStock : list) {
            faNewStock.setFxPrice(faNewStock.getPsPrice().toString());
        }

//        if (list.size() > 0) {
//            // 申购日期集合
//            Set<Date> dateSet = new LinkedHashSet<>();
//            for (FaNewStock shengou : list) {
//                dateSet.add(shengou.getSgDate());
//            }
//            // 按日期分类
//            LinkedHashMap<String, List<FaNewStock>> map = new LinkedHashMap<>();
//            for (Date date : dateSet) {
//                List<FaNewStock> faShengouList = new ArrayList<>();
//                for (FaNewStock shengou : list) {
//                    if (shengou.getSgDate().equals(date)) {
//                        faShengouList.add(shengou);
//                    }
//                }
//                map.put(sdf1.format(date), faShengouList);
//            }
//            return map;
//        }
        return list;
    }

    /**
     * 修改申购配售开关
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public void changeSwitch(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getSwitchStatus() || StringUtils.isEmpty(faNewStock.getSwitchType())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        switch (faNewStock.getSwitchType()) {
            case "sg":
                lambdaUpdateWrapper.set(FaNewStock::getSgSwitch, faNewStock.getSwitchStatus());
//                if (0 == faNewStock.getSwitchStatus()) {
//                    lambdaUpdateWrapper.set(FaNewStock::getSgExchange, 0);
//                }
                break;
            case "xx":
                lambdaUpdateWrapper.set(FaNewStock::getXxSwitch, faNewStock.getSwitchStatus());
//                if (0 == faNewStock.getSwitchStatus()) {
//                    lambdaUpdateWrapper.set(FaNewStock::getXxExchange, 0);
//                }
                break;
            case "show":
                lambdaUpdateWrapper.set(FaNewStock::getStatus, faNewStock.getSwitchStatus());
                break;
            default:
                break;
        }
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 查询新股详情
     * @param faNewStock
     * @return
     * @throws Exception
     */
    @Override
    public FaNewStock getShengouDetail(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faNewStock = this.getById(faNewStock.getId());
        if (ObjectUtils.isEmpty(faNewStock)) {
            throw new ServiceException(MessageUtils.message("new.stock.error"), HttpStatus.ERROR);
        }
        return faNewStock;
    }

    /**
     * 一键申购
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public void addShengou(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getId() || null == faNewStock.getMemberId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaNewStock selectOne = this.getById(faNewStock.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("new.stock.error"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (selectOne.getSgNums() < faNewStock.getSgNums()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.selectFaMemberById(faNewStock.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户申购锁定判断
        if (0 == faMember.getIsSg()) {
            throw new ServiceException(MessageUtils.message("member.subscribe.lock"), HttpStatus.ERROR);
        }

        // 新股申购开关
        if (0 == selectOne.getSgSwitch()) {
            throw new ServiceException(MessageUtils.message("new.stock.subscribe.lock"), HttpStatus.ERROR);
        }

        if (null != faNewStock.getSgNums() && faNewStock.getSgNums() > 0) {
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算，算手
            if ("0".equals(gqpeizhi)) {
                // 1手=100股
                faNewStock.setSgNum(faNewStock.getSgNums());
                faNewStock.setSgNums(faNewStock.getSgNums() * 100);
            }
            // 按签计算
            else if ("1".equals(gqpeizhi)) {
                faNewStock.setSgNum(faNewStock.getSgNums());
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == selectOne.getSgType()) {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 1000);
                } else {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 500);
                }
            }
        } else {
            faNewStock.setSgNum(0);
            faNewStock.setSgNums(0);
        }

        // 风控校验
        faNewStock.setFxPrice(selectOne.getFxPrice());
        iFaRiskConfigService.checkShengou(faNewStock);

        FaSgList faSgList = new FaSgList();
        faSgList.setUserId(faMember.getId());
        faSgList.setShengouId(selectOne.getId());
        faSgList.setCode(selectOne.getCode());
        faSgList.setAllCode(selectOne.getAllCode());
        faSgList.setName(selectOne.getName());
        faSgList.setSgFxPrice(new BigDecimal(selectOne.getFxPrice()));
        if (StringUtils.isNotEmpty(selectOne.getHyRate())) {
            faSgList.setSgHyRate(new BigDecimal(selectOne.getHyRate()));
        }
        faSgList.setSgNum(faNewStock.getSgNum());
        faSgList.setSgNums(faNewStock.getSgNums());
        faSgList.setStatus(0);
        faSgList.setSgType(selectOne.getSgType());
        if (null == faNewStock.getCreateTime()) {
            faSgList.setCreateTime(new Date());
        } else {
            faSgList.setCreateTime(faNewStock.getCreateTime());
        }
        faSgList.setSgSgDate(selectOne.getSgDate());
        faSgList.setSgZqJkDate(selectOne.getZqJkDate());
        faSgList.setSgSsDate(selectOne.getSsDate());
        faSgList.setDeleteFlag(0);
        iFaSgListService.save(faSgList);

        // 减少剩余数量
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        lambdaUpdateWrapper.set(FaNewStock::getSgNums, selectOne.getSgNums() - faNewStock.getSgNums());
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 一键配售(申购=配售)
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public void addSgSecond(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getId() || null == faNewStock.getMemberId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaNewStock selectOne = this.getById(faNewStock.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("new.stock.error"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (selectOne.getXxNums() < faNewStock.getSgNums()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.selectFaMemberById(faNewStock.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户申购锁定判断
        if (0 == faMember.getIsPs()) {
            throw new ServiceException(MessageUtils.message("member.subscribe.lock"), HttpStatus.ERROR);
        }

        // 新股申购开关
        if (0 == selectOne.getXxSwitch()) {
            throw new ServiceException(MessageUtils.message("new.stock.subscribe.lock"), HttpStatus.ERROR);
        }

        if (null != faNewStock.getSgNums() && faNewStock.getSgNums() > 0) {
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算，算手
            if ("0".equals(gqpeizhi)) {
                // 1手=100股
                faNewStock.setSgNum(faNewStock.getSgNums());
                faNewStock.setSgNums(faNewStock.getSgNums() * 100);
            }
            // 按签计算
            else if ("1".equals(gqpeizhi)) {
                faNewStock.setSgNum(faNewStock.getSgNums());
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == selectOne.getSgType()) {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 1000);
                } else {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 500);
                }
            }
        } else {
            faNewStock.setSgNum(0);
            faNewStock.setSgNums(0);
        }

        // 风控校验
        faNewStock.setPsPrice(selectOne.getPsPrice());
        iFaRiskConfigService.checkPeiShou(faNewStock);

        FaStockSgSecond faStockSgSecond = new FaStockSgSecond();
        faStockSgSecond.setUserId(faMember.getId());
        faStockSgSecond.setShengouId(selectOne.getId());
        faStockSgSecond.setCode(selectOne.getCode());
        faStockSgSecond.setAllCode(selectOne.getAllCode());
        faStockSgSecond.setName(selectOne.getName());
        faStockSgSecond.setSgFxPrice(selectOne.getPsPrice());
        if (StringUtils.isNotEmpty(selectOne.getHyRate())) {
            faStockSgSecond.setSgHyRate(new BigDecimal(selectOne.getHyRate()));
        }
        faStockSgSecond.setSgNum(faNewStock.getSgNum());
        faStockSgSecond.setSgNums(faNewStock.getSgNums());
        faStockSgSecond.setStatus(0);
        faStockSgSecond.setSgType(selectOne.getSgType());
        if (null == faNewStock.getCreateTime()) {
            faStockSgSecond.setCreateTime(new Date());
        } else {
            faStockSgSecond.setCreateTime(faNewStock.getCreateTime());
        }
        faStockSgSecond.setSgSgDate(selectOne.getSgDate());
        faStockSgSecond.setSgZqJkDate(selectOne.getZqJkDate());
        faStockSgSecond.setSgSsDate(selectOne.getSsDate());
        faStockSgSecond.setDeleteFlag(0);
        iFaStockSgSecondService.save(faStockSgSecond);

        // 减少剩余数量
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        lambdaUpdateWrapper.set(FaNewStock::getXxNums, selectOne.getXxNums() - faNewStock.getSgNums());
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 一键配售 保证金模式
     * 最大量配售，提交后冻结资金，转持仓后中签金额扣除，剩余退回可用
     *
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public void addPeiShou(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getId() || null == faNewStock.getMemberId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 新股
        FaNewStock selectOne = this.getById(faNewStock.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("new.stock.error"), HttpStatus.ERROR);
        }

        // 剩余数量是否足够
        if (selectOne.getXxNums() < faNewStock.getSgNums()) {
            throw new ServiceException(MessageUtils.message("remain.quantity.not.enough"), HttpStatus.ERROR);
        }

        // 新股配售开关
        if (0 == selectOne.getXxSwitch()) {
            throw new ServiceException(MessageUtils.message("new.stock.placing.lock"), HttpStatus.ERROR);
        }
        if (StringUtils.isNotEmpty(faNewStock.getContent()) && !faNewStock.getContent().equals(selectOne.getContent())) {
            throw new ServiceException(MessageUtils.message("placing.password.error"), HttpStatus.ERROR);
        }

        // 配售价格是否配置
        if (selectOne.getPsPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("new.stock.placing.price.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.selectFaMemberById(faNewStock.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户配售锁定判断
        if (0 == faMember.getIsPs()) {
            throw new ServiceException(MessageUtils.message("member.placing.lock"), HttpStatus.ERROR);
        }

        if (null != faNewStock.getSgNums() && faNewStock.getSgNums() > 0) {
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算，算手
            if ("0".equals(gqpeizhi)) {
                // 1手=100股
                faNewStock.setSgNum(faNewStock.getSgNums());
                faNewStock.setSgNums(faNewStock.getSgNums() * 100);
            }
            // 按签计算
            else if ("1".equals(gqpeizhi)) {
                faNewStock.setSgNum(faNewStock.getSgNums());
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == selectOne.getSgType()) {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 1000);
                } else {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 500);
                }
            }
        } else {
            faNewStock.setSgNum(0);
            faNewStock.setSgNums(0);
        }

        // 余额判断
        if (faMember.getBalance().compareTo(selectOne.getPsPrice().multiply(new BigDecimal(faNewStock.getSgNums()))) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 风控校验
        faNewStock.setPsPrice(selectOne.getPsPrice());
        iFaRiskConfigService.checkPeiShou(faNewStock);

        FaSgjiaoyi faSgjiaoyi = new FaSgjiaoyi();
        faSgjiaoyi.setUserId(faMember.getId());
        faSgjiaoyi.setShengouId(selectOne.getId());
        faSgjiaoyi.setCode(selectOne.getCode());
        faSgjiaoyi.setAllCode(selectOne.getAllCode());
        faSgjiaoyi.setName(selectOne.getName());
        faSgjiaoyi.setSgFxPrice(selectOne.getPsPrice());
        if (StringUtils.isNotEmpty(selectOne.getHyRate())) {
            faSgjiaoyi.setSgHyRate(new BigDecimal(selectOne.getHyRate()));
        }
        faSgjiaoyi.setSgNum(faNewStock.getSgNum());
        faSgjiaoyi.setSgNums(faNewStock.getSgNums());
        faSgjiaoyi.setStatus(0);
        faSgjiaoyi.setSgType(selectOne.getSgType());
        faSgjiaoyi.setCreateTime(new Date());
        faSgjiaoyi.setSgSgDate(selectOne.getSgDate());
        faSgjiaoyi.setSgZqJkDate(selectOne.getZqJkDate());
        faSgjiaoyi.setSgSsDate(selectOne.getSsDate());
        faSgjiaoyi.setDeleteFlag(0);
        // 保证金
        faSgjiaoyi.setMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getSgNums())));
        // 已认缴
        faSgjiaoyi.setRenjiao(1);
        faSgjiaoyi.setRenjiaoTime(new Date());
        iFaSgjiaoyiService.save(faSgjiaoyi);

        // 扣钱到冻结，记录流水
        iFaCapitalLogService.savePeiShou(faSgjiaoyi);

        // 减少剩余数量
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        lambdaUpdateWrapper.set(FaNewStock::getXxNums, selectOne.getXxNums() - faNewStock.getSgNums());
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 一键配售 补缴模式
     * 最大量配售，提交后冻结资金，转持仓后中签金额扣除，剩余退回可用
     *
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public void addPeiShouPayLater(FaNewStock faNewStock) throws Exception {
        if (null == faNewStock.getId() || null == faNewStock.getMemberId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 新股
        FaNewStock selectOne = this.getById(faNewStock.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("new.stock.error"), HttpStatus.ERROR);
        }
        // 新股配售开关
        if (0 == selectOne.getXxSwitch()) {
            throw new ServiceException(MessageUtils.message("new.stock.placing.lock"), HttpStatus.ERROR);
        }
        if (StringUtils.isNotEmpty(faNewStock.getContent()) && !faNewStock.getContent().equals(selectOne.getContent())) {
            throw new ServiceException(MessageUtils.message("placing.password.error"), HttpStatus.ERROR);
        }

        // 配售价格是否配置
        if (selectOne.getPsPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("new.stock.placing.price.error"), HttpStatus.ERROR);
        }


        // 用户
        FaMember faMember = iFaMemberService.selectFaMemberById(faNewStock.getMemberId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户配售锁定判断
        if (0 == faMember.getIsPs()) {
            throw new ServiceException(MessageUtils.message("member.placing.lock"), HttpStatus.ERROR);
        }

        if (null != faNewStock.getSgNums()) {
            // 手->股转换 根据风控设置来，默认股(0股1签)
            String gqpeizhi = iFaRiskConfigService.getConfigValue("gqpeizhi", "0");
            // 按股计算，算手
            if ("0".equals(gqpeizhi)) {
                // 1手=100股
                faNewStock.setSgNum(faNewStock.getSgNums());
                faNewStock.setSgNums(faNewStock.getSgNums() * 100);
            }
            // 按签计算
            else if ("1".equals(gqpeizhi)) {
                faNewStock.setSgNum(faNewStock.getSgNums());
                // 按交易所不同，一签等于不同股数 沪市=1000股 其他=500股
                if (1 == selectOne.getSgType()) {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 1000);
                } else {
                    faNewStock.setSgNums(faNewStock.getSgNum() * 500);
                }
            }
        } else {
            faNewStock.setSgNum(0);
            faNewStock.setSgNums(0);
        }

        // 风控校验
        faNewStock.setPsPrice(selectOne.getPsPrice());
        iFaRiskConfigService.checkPeiShou(faNewStock);

        FaSgjiaoyi faSgjiaoyi = new FaSgjiaoyi();
        faSgjiaoyi.setUserId(faMember.getId());
        faSgjiaoyi.setShengouId(selectOne.getId());
        faSgjiaoyi.setCode(selectOne.getCode());
        faSgjiaoyi.setAllCode(selectOne.getAllCode());
        faSgjiaoyi.setName(selectOne.getName());
        faSgjiaoyi.setSgFxPrice(selectOne.getPsPrice());
        if (StringUtils.isNotEmpty(selectOne.getHyRate())) {
            faSgjiaoyi.setSgHyRate(new BigDecimal(selectOne.getHyRate()));
        }
        faSgjiaoyi.setSgNum(faNewStock.getSgNum());
        faSgjiaoyi.setSgNums(faNewStock.getSgNums());
        faSgjiaoyi.setStatus(0);
        faSgjiaoyi.setSgType(selectOne.getSgType());
        faSgjiaoyi.setCreateTime(new Date());
        faSgjiaoyi.setSgSgDate(selectOne.getSgDate());
        faSgjiaoyi.setSgZqJkDate(selectOne.getZqJkDate());
        faSgjiaoyi.setSgSsDate(selectOne.getSsDate());
        faSgjiaoyi.setDeleteFlag(0);
        // 申购金额
        faSgjiaoyi.setMoney(faSgjiaoyi.getSgFxPrice().multiply(new BigDecimal(faSgjiaoyi.getSgNums())));
        iFaSgjiaoyiService.save(faSgjiaoyi);

        // 减少剩余数量
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        lambdaUpdateWrapper.set(FaNewStock::getXxNums, selectOne.getXxNums() - faNewStock.getSgNums());
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 搜索新股
     * @param faNewStock
     * @return
     * @throws Exception
     */
    @Override
    public List<FaNewStock> searchNewStock(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaNewStock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faNewStock.getQueryString())) {
            lambdaQueryWrapper.and(i -> i.like(FaNewStock::getCode, faNewStock.getQueryString())
                    .or().like(FaNewStock::getAllCode, faNewStock.getQueryString())
                    .or().like(FaNewStock::getName, faNewStock.getQueryString())
                    .or().like(FaNewStock::getSgCode, faNewStock.getQueryString())
            );
        }
        if (null != faNewStock.getId()) {
            lambdaQueryWrapper.eq(FaNewStock::getId, faNewStock.getId());
        }
        lambdaQueryWrapper.eq(FaNewStock::getXxSwitch, 1);
        lambdaQueryWrapper.eq(FaNewStock::getDeleteFlag, 0);
        List<FaNewStock> faShengouList = this.list(lambdaQueryWrapper);
        return faShengouList;
    }

    /**
     * 提交申购配售配置
     * @param faNewStock
     * @throws Exception
     */
    @Override
    public void submitExchange(FaNewStock faNewStock) throws Exception {
        LambdaUpdateWrapper<FaNewStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaNewStock::getId, faNewStock.getId());
        if (0 == faNewStock.getSgOrPs()) {
            lambdaUpdateWrapper.set(FaNewStock::getSgSwitch, 1);
            lambdaUpdateWrapper.set(FaNewStock::getSgExchange, faNewStock.getSgExchange());
        } else if (1 == faNewStock.getSgOrPs()) {
            lambdaUpdateWrapper.set(FaNewStock::getXxSwitch, 1);
            lambdaUpdateWrapper.set(FaNewStock::getXxExchange, faNewStock.getXxExchange());
        }
        lambdaUpdateWrapper.set(FaNewStock::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

}