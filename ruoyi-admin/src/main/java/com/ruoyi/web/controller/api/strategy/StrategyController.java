package com.ruoyi.web.controller.api.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.domain.StrategyString;
import com.ruoyi.biz.strategy.service.IFaStrategyService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 股票Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "股票")
@RestController
@RequestMapping("/api/stock")
public class StrategyController extends BaseController
{

    @Autowired
    private IFaStrategyService faStrategyService;

    /**
     * 搜索股票
     */
    @ApiOperation("搜索股票")
    @AppLog(title = "搜索股票", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryString", value = "关键字", required = true, dataType = "String")
    })
    @PostMapping("/searchStock")
    public AjaxResult searchStock(@RequestBody FaStrategy faStrategy)
    {
        try {
            List<FaStrategy> faStrategyList = faStrategyService.searchStock(faStrategy);

            List<StrategyString> strategyStringList = new ArrayList<>();
            if (!faStrategyList.isEmpty()) {
                for (FaStrategy strategy : faStrategyList) {
                    StrategyString strategyString = JSON.parseObject(JSON.toJSONString(strategy), StrategyString.class);
                    strategyStringList.add(strategyString);
                }
                return AjaxResult.success(strategyStringList);
            }

            return AjaxResult.success(faStrategyList);
        } catch (ServiceException e) {
            logger.error("searchStock", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("searchStock", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询股票详情
     */
    @ApiOperation("查询股票详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "股票id", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "股票代码", dataType = "String")
    })
    @PostMapping("/getStockDetail")
    public AjaxResult getStockDetail(@RequestBody FaStrategy faStrategy)
    {
        try {
            FaStrategy strategy = faStrategyService.getStockDetail(faStrategy);
            return AjaxResult.success(strategy);
        } catch (ServiceException e) {
            logger.error("getStockDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStockDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询大宗交易
     */
    @ApiOperation("查询大宗交易")
    @AppLog(title = "查询大宗交易", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")})
    @PostMapping("/getDZStrategy")
    public AjaxResult getDZStrategy(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getDZStrategy(faStrategy);
            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getDZStrategy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getDZStrategy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询抢筹列表(VIP调研)
     */
    @ApiOperation("查询抢筹列表(VIP调研)")
    @AppLog(title = "查询抢筹列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")})
    @PostMapping("/getQCStrategy")
    public AjaxResult getQCStrategy(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getQCStrategy(faStrategy);
            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getQCStrategy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getQCStrategy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询融券列表
     */
    @ApiOperation("查询融券列表")
    @AppLog(title = "查询融券列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")})
    @PostMapping("/getRQStrategy")
    public AjaxResult getRQStrategy(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getRQStrategy(faStrategy);
            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getRQStrategy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRQStrategy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询首页5大指数
     */
    @ApiOperation("查询首页5大指数")
//    @AppLog(title = "查询首页5大指数", businessType = BusinessType.OTHER)
    @PostMapping("/getIndexList")
    public AjaxResult getIndexList(@RequestBody FaStrategy faStrategy)
    {
        try {
            List<FaStrategy> list = faStrategyService.getIndexList(faStrategy);

            List<StrategyString> strategyStringList = new ArrayList<>();
            if (!list.isEmpty()) {
                for (FaStrategy strategy : list) {
                    StrategyString strategyString = JSON.parseObject(JSON.toJSONString(strategy), StrategyString.class);
                    strategyStringList.add(strategyString);
                }
                return AjaxResult.success(strategyStringList);
            }

            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getIndexList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getIndexList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询股票列表
     */
    @ApiOperation("查询股票列表")
    @AppLog(title = "查询股票列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "交易所(1沪 2深 3创业 4北交 5科创)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "顺序(1正序 2倒序)", required = true, dataType = "Integer")
    })
    @PostMapping("/getStrategyList")
    public AjaxResult getStrategyList(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getStrategyList(faStrategy);

            faStrategyIPage.convert(u ->{
                StrategyString strategyString = new StrategyString();
                strategyString = JSON.parseObject(JSON.toJSONString(u), StrategyString.class);
                return strategyString;
            });

            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getStrategyList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getStrategyList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询涨跌对比
     */
    @ApiOperation("查询涨跌对比")
    @AppLog(title = "查询涨跌对比", businessType = BusinessType.OTHER)
    @PostMapping("/getRiseAndFall")
    public AjaxResult getRiseAndFall(@RequestBody FaStrategy faStrategy)
    {
        try {
            Map<String, Integer> map = faStrategyService.getRiseAndFall(faStrategy);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getRiseAndFall", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRiseAndFall", e);
            return AjaxResult.error();
        }
    }

    /**
     * 板块涨跌幅
     */
    @ApiOperation("板块涨跌幅")
    @AppLog(title = "板块涨跌幅", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bkType", value = "板块类型(1地区 2板块 3概念)", required = true, dataType = "Integer")
    })
    @PostMapping("/getBkHotList")
    public AjaxResult getBkHotList(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getBkType()) {
                // 默认取1
                faStrategy.setBkType(1);
            }
            // 启涨大数据
            JSONArray jsonArray = faStrategyService.getBkHotList(faStrategy);
            // 新浪
//            JSONArray jsonArray = faStrategyService.getBkHotListFromSina(faStrategy);
            return AjaxResult.success(jsonArray);
        } catch (ServiceException e) {
            logger.error("getBkHotList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBkHotList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 龙虎榜
     */
    @ApiOperation("龙虎榜")
    @AppLog(title = "龙虎榜", businessType = BusinessType.OTHER)
    @PostMapping("/getDragonTigerList")
    public AjaxResult getDragonTigerList(@RequestBody FaStrategy faStrategy)
    {
        try {
            // 启涨大数据
            List<FaStrategy> list = faStrategyService.getDragonTigerList(faStrategy);

            List<StrategyString> strategyStringList = new ArrayList<>();
            if (!list.isEmpty()) {
                for (FaStrategy strategy : list) {
                    StrategyString strategyString = JSON.parseObject(JSON.toJSONString(strategy), StrategyString.class);
                    strategyStringList.add(strategyString);
                }
                return AjaxResult.success(strategyStringList);
            }

            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getDragonTigerList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getDragonTigerList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 资金流向
     */
    @ApiOperation("资金流向")
    @AppLog(title = "资金流向", businessType = BusinessType.OTHER)
    @PostMapping("/getMoneyFlow")
    public AjaxResult getMoneyFlow(@RequestBody FaStrategy faStrategy)
    {
        try {
            JSONObject jsonObject = faStrategyService.getMoneyFlow(faStrategy);
            return AjaxResult.success(jsonObject);
        } catch (ServiceException e) {
            logger.error("getMoneyFlow", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getMoneyFlow", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询推荐列表
     */
    @ApiOperation("查询推荐列表")
//    @AppLog(title = "查询推荐列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "交易所", dataType = "Integer")
    })
    @PostMapping("/getRecommendStrategy")
    public AjaxResult getRecommendStrategy(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getRecommendStrategy(faStrategy);

            faStrategyIPage.convert(u ->{
                StrategyString strategyString = new StrategyString();
                strategyString = JSON.parseObject(JSON.toJSONString(u), StrategyString.class);
                return strategyString;
            });

            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getRecommendStrategy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRecommendStrategy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 上证指数k线
     */
    @ApiOperation("上证指数k线")
    @AppLog(title = "上证指数k线", businessType = BusinessType.OTHER)
    @PostMapping("/getSHKline")
    public AjaxResult getSHKline(@RequestBody FaStrategy faStrategy)
    {
        try {
            List<Map<String, String>> list = faStrategyService.getSHKline(faStrategy);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getSHKline", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getSHKline", e);
            return AjaxResult.error();
        }
    }

    /**
     * 前瞻会议
     */
    @ApiOperation("前瞻会议")
//    @AppLog(title = "前瞻会议", businessType = BusinessType.OTHER)
    @PostMapping("/getQzhy")
    public AjaxResult getQzhy(@RequestBody FaStrategy faStrategy)
    {
        try {
            // 启涨大数据 pz=2 取第四个
            JSONArray jsonArray = faStrategyService.getQzhy();
            return AjaxResult.success(jsonArray);
        } catch (ServiceException e) {
            logger.error("getQzhy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getQzhy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 研报精选
     */
    @ApiOperation("研报精选")
//    @AppLog(title = "研报精选", businessType = BusinessType.OTHER)
    @PostMapping("/getYbjx")
    public AjaxResult getYbjx(@RequestBody FaStrategy faStrategy)
    {
        try {
            // 取涨幅前十之一，业绩略超预期，稳健增长
            faStrategy = faStrategyService.getYbjx();
            return AjaxResult.success(faStrategy);
        } catch (ServiceException e) {
            logger.error("getYbjx", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getYbjx", e);
            return AjaxResult.error();
        }
    }

    /**
     * 热门行业
     */
    @ApiOperation("热门行业")
//    @AppLog(title = "热门行业", businessType = BusinessType.OTHER)
    @PostMapping("/getRmhy")
    public AjaxResult getRmhy(@RequestBody FaStrategy faStrategy)
    {
        try {
            // 启涨大数据 pz=2 取前三个
            JSONArray jsonArray = faStrategyService.getRmhy();
            return AjaxResult.success(jsonArray);
        } catch (ServiceException e) {
            logger.error("getRmhy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRmhy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 短线掘金
     */
    @ApiOperation("短线掘金")
//    @AppLog(title = "短线掘金", businessType = BusinessType.OTHER)
    @PostMapping("/getDxjj")
    public AjaxResult getDxjj(@RequestBody FaStrategy faStrategy)
    {
        try {
            // 取涨幅前十之二，业绩略超预期，稳健增长
            List<FaStrategy> list = faStrategyService.getDxjj();
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getDxjj", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getDxjj", e);
            return AjaxResult.error();
        }
    }

    /**
     * 涨跌柱状图
     */
    @ApiOperation("涨跌柱状图")
    @AppLog(title = "涨跌柱状图", businessType = BusinessType.OTHER)
    @PostMapping("/getRiseAndFallBar")
    public AjaxResult getRiseAndFallBar(@RequestBody FaStrategy faStrategy)
    {
        try {
            Map<String, Integer> map = faStrategyService.getRiseAndFallBar(faStrategy);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getRiseAndFallBar", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getRiseAndFallBar", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询爱股票领涨概念板块
     * 领涨概念
     * https://apis.aigupiao.com/theme/up_down_concept_pc?page=1&number=8&kind=up&md=c5aeae1346b782f8e5056145ccaa67de&theme_type=0
     * 领跌概念
     * https://apis.aigupiao.com/theme/up_down_concept_pc?page=1&number=8&kind=down&md=c5aeae1346b782f8e5056145ccaa67de&theme_type=0
     * 领涨行业
     * https://apis.aigupiao.com/theme/up_down_concept_pc?page=1&number=8&kind=up&md=c5aeae1346b782f8e5056145ccaa67de&theme_type=1
     * 领跌行业
     * https://apis.aigupiao.com/theme/up_down_concept_pc?page=1&number=8&kind=down&md=c5aeae1346b782f8e5056145ccaa67de&theme_type=1
     */
    @ApiOperation("查询爱股票领涨概念板块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "themeType", value = "主题", required = true, dataType = "String"),
    })
    @PostMapping("/getAiStockUpPlate")
    public AjaxResult getAiStockUpPlate(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (StringUtils.isEmpty(faStrategy.getKind())) {
                faStrategy.setKind("up");
            }
            if (StringUtils.isEmpty(faStrategy.getThemeType())) {
                faStrategy.setThemeType("0");
            }
            JSONObject jsonObject = faStrategyService.getAiStockUpPlate(faStrategy);
            return AjaxResult.success(jsonObject);
        } catch (ServiceException e) {
            logger.error("getAiStockUpPlate", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getAiStockUpPlate", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询预约重组列表
     */
    @ApiOperation("查询预约重组列表")
    @AppLog(title = "查询预约重组列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer")})
    @PostMapping("/getYyczStrategy")
    public AjaxResult getYyczStrategy(@RequestBody FaStrategy faStrategy)
    {
        try {
            if (null == faStrategy.getPage()) {
                faStrategy.setPage(1);
            }
            if (null == faStrategy.getSize()) {
                faStrategy.setSize(10);
            }
            IPage<FaStrategy> faStrategyIPage = faStrategyService.getYyczStrategy(faStrategy);
            return AjaxResult.success(faStrategyIPage);
        } catch (ServiceException e) {
            logger.error("getYyczStrategy", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getYyczStrategy", e);
            return AjaxResult.error();
        }
    }

    /**
     * 新股大宗数量统计
     */
    @ApiOperation("新股大宗数量统计")
    @AppLog(title = "新股大宗数量统计", businessType = BusinessType.OTHER)
    @PostMapping("/getNewCountAndDz")
    public AjaxResult getNewCountAndDz(@RequestBody FaStrategy faStrategy)
    {
        try {
            Map<String, Integer> map = faStrategyService.getNewCountAndDz(faStrategy);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("getNewCountAndDz", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getNewCountAndDz", e);
            return AjaxResult.error();
        }
    }

}
