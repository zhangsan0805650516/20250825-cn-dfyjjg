package com.ruoyi.biz.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.FaMember;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 会员管理Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-04
 */
public interface FaMemberMapper extends BaseMapper<FaMember>
{
    /**
     * 查询会员管理
     *
     * @param id 会员管理主键
     * @return 会员管理
     */
    public FaMember selectFaMemberById(Integer id);

    /**
     * 查询会员管理列表
     *
     * @param faMember 会员管理
     * @return 会员管理集合
     */
    public List<FaMember> selectFaMemberList(FaMember faMember);

    /**
     * 导出会员管理列表
     *
     * @param faMember 会员管理
     * @return 会员管理集合
     */
    public List<FaMember> selectFaMemberListForExport(FaMember faMember);

    /**
     * 查询会员实名认证列表
     * @param faMember
     * @return
     */
    List<FaMember> authMemberList(FaMember faMember);

    /**
     * 查询会员绑卡列表
     * @param faMember
     * @return
     */
    List<FaMember> memberBankList(FaMember faMember);

    /**
     * 新增会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    public int insertFaMember(FaMember faMember);

    /**
     * 修改会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    public int updateFaMember(FaMember faMember);

    /**
     * 删除会员管理
     *
     * @param id 会员管理主键
     * @return 结果
     */
    public int deleteFaMemberById(Integer id);

    /**
     * 批量删除会员管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaMemberByIds(Integer[] ids);

    /**
     * 批量删除实名认证
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int delAuthMemberByIds(Integer[] ids);

    /**
     * 批量删除绑卡
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int delMemberBankByIds(Integer[] ids);

    /**
     * 用户统计数据
     * @param faMember
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getMemberStatistics(FaMember faMember) throws Exception;

//    /**
//     * 新股申购资金
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    BigDecimal getFundInfoNew(@Param("id") Integer id) throws Exception;

    /**
     * 持仓市值
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoMarket(@Param("id") Integer id) throws Exception;

    BigDecimal getFundInfoMarketListed(@Param("id") Integer id) throws Exception;

    BigDecimal getFundInfoMarketUnlisted(@Param("id") Integer id) throws Exception;

    /**
     * 总盈亏
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoProfit(@Param("id") Integer id) throws Exception;

    /**
     * 总充值
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoRecharge(@Param("id") Integer id) throws Exception;

    /**
     * 总提现
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoWithdraw(@Param("id") Integer id) throws Exception;

    /**
     * 总提现
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoSg(@Param("id") Integer id) throws Exception;

    /**
     * 总提现
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getFundInfoPs(@Param("id") Integer id) throws Exception;

    /**
     * 持仓盈亏
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getTradeInfoHoldProfit(Integer id) throws Exception;

    /**
     * 融券持仓盈亏
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getTradeInfoHoldProfitRq(Integer id) throws Exception;

    /**
     * 交易盈亏
     * @param id
     * @return
     * @throws Exception
     */
    BigDecimal getTradeInfoTradeProfit(Integer id) throws Exception;

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    void updateFaMemberFreezeProfit(@Param("userId") Integer memberId, @Param("money") BigDecimal amount, @Param("direct") int direct) throws Exception;

    /**
     * 更新用户余额
     * @param userId 用户id
     * @param money 变动金额
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    void updateMemberBalance(@Param("userId") Integer userId, @Param("money") BigDecimal money, @Param("direct") int direct) throws Exception;

    /**
     * 总余额
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalBalance(FaMember faMember) throws Exception;

    /**
     * 总充值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalRecharge(FaMember faMember) throws Exception;

    /**
     * 总提现
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalWithdraw(FaMember faMember) throws Exception;

    /**
     * 新股申购冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalSg(FaMember faMember) throws Exception;

    /**
     * 新股配售冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalPs(FaMember faMember) throws Exception;

    /**
     * 上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalListed(FaMember faMember) throws Exception;

    /**
     * 未上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getTotalUnlisted(FaMember faMember) throws Exception;

    /**
     * 上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getListedHold(FaMember faMember) throws Exception;

    /**
     * 未上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getUnlistedHold(FaMember faMember) throws Exception;

    /**
     * 新股申购冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getSgFreeze(FaMember faMember) throws Exception;

    /**
     * 新股配售冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getPsFreeze(FaMember faMember) throws Exception;

    /**
     * 总充值
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getRecharge(FaMember faMember) throws Exception;

    /**
     * 总提现
     * @param faMember
     * @return
     * @throws Exception
     */
    BigDecimal getWithdraw(FaMember faMember) throws Exception;

    BigDecimal getDzFundInfoMarket(@Param("id") Integer id) throws Exception;

    BigDecimal getDzTradeInfoHoldProfit(@Param("id") Integer id) throws Exception;

    BigDecimal getTotalPoundage(@Param("id") Integer id) throws Exception;

    List<BigDecimal> getFundInfos(@Param("id") Integer id) throws Exception;
}