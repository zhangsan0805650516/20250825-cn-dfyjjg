package com.ruoyi.biz.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.member.domain.LoginPasswordParam;
import com.ruoyi.biz.member.domain.PaymentPasswordParam;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.recharge.domain.RechargeNotify;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 会员管理Service接口
 *
 * @author ruoyi
 * @date 2024-01-04
 */
public interface IFaMemberService extends IService<FaMember>
{
    /**
     * 查询会员管理
     *
     * @param id 会员管理主键
     * @return 会员管理
     */
    public FaMember selectFaMemberById(Integer id) throws Exception;

    /**
     * 查询会员管理列表
     *
     * @param faMember 会员管理
     * @return 会员管理集合
     */
    public List<FaMember> selectFaMemberList(FaMember faMember);

    /**
     * 导出会员管理列表
     * @param faMember
     * @return
     */
    public List<FaMember> selectFaMemberListForExport(FaMember faMember) throws Exception;

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
    public int insertFaMember(FaMember faMember) throws Exception;

    /**
     * 修改会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    public int updateFaMember(FaMember faMember);

    /**
     * 批量删除会员管理
     *
     * @param ids 需要删除的会员管理主键集合
     * @return 结果
     */
    public int deleteFaMemberByIds(Integer[] ids);

    /**
     * 批量删除实名认证
     *
     * @param ids 需要删除的会员管理主键集合
     * @return 结果
     */
    public int delAuthMemberByIds(Integer[] ids);

    /**
     * 批量删除绑卡
     *
     * @param ids 需要删除的会员管理主键集合
     * @return 结果
     */
    public int delMemberBankByIds(Integer[] ids);

    /**
     * 删除会员管理信息
     *
     * @param id 会员管理主键
     * @return 结果
     */
    public int deleteFaMemberById(Integer id);

    /**
     * 修改会员状态
     * @param faMember
     * @return
     */
    AjaxResult changeMemberStatus(FaMember faMember) throws Exception;

    /**
     * 提交实名认证
     * @param faMember
     * @return
     */
    void submitAuthMember(FaMember faMember) throws Exception;

    /**
     * 提交绑定银行卡
     * @param faMember
     * @return
     */
    void submitBindingBank(FaMember faMember) throws Exception;

    /**
     * 更新登录信息
     * @param faMember
     * @throws Exception
     */
    void recordLoginInfo(FaMember faMember) throws Exception;

    /**
     * 修改用户登录密码
     * @param loginPasswordParam
     * @throws Exception
     */
    void updateLoginPassword(LoginPasswordParam loginPasswordParam) throws Exception;

    /**
     * 修改用户支付密码
     * @param paymentPasswordParam
     * @throws Exception
     */
    void updatePaymentPassword(PaymentPasswordParam paymentPasswordParam) throws Exception;

    /**
     * 修改用户头像
     * @param id
     * @param avatar
     * @throws Exception
     */
    void updateAvatar(Integer id, String avatar) throws Exception;

    /**
     * @param faMember
     * @return
     * @throws Exception
     */
    List<FaMember> searchMember(FaMember faMember) throws Exception;

    /**
     * T+1冻结资金清零
     * @throws Exception
     */
    void clearT1Amount() throws Exception;

    /**
     * 实名认证
     * @param faMember
     * @throws Exception
     */
    void authMember(FaMember faMember) throws Exception;

    /**
     * 绑定银行卡
     * @param faMember
     * @throws Exception
     */
    void bindingBank(FaMember faMember) throws Exception;

    /**
     * 解绑银行卡
     * @param faMember
     * @throws Exception
     */
    void unbindingBank(FaMember faMember) throws Exception;

    /**
     * 充值
     * @param faMember
     * @throws Exception
     */
    void submitRecharge(FaMember faMember) throws Exception;

    /**
     * 修改余额
     * @param faMember
     * @throws Exception
     */
    void submitUpdateBalance(FaMember faMember) throws Exception;

    /**
     * 修改T+1锁定转入转出
     * @param faMember
     * @throws Exception
     */
    void submitUpdateFreezeProfit(FaMember faMember) throws Exception;

    /**
     * 充值申请
     * @param faMember
     * @param ip
     * @throws Exception
     */
    String rechargeApply(FaMember faMember) throws Exception;

    /**
     * 提现申请
     * @param faMember
     * @throws Exception
     */
    void withdrawApply(FaMember faMember) throws Exception;

    /**
     * 充值记录
     * @param faMember
     * @return
     * @throws Exception
     */
    List<FaRecharge> rechargeList(FaMember faMember) throws Exception;

    /**
     * 提现记录
     * @param faMember
     * @return
     * @throws Exception
     */
    List<FaWithdraw> withdrawList(FaMember faMember) throws Exception;

    /**
     * 注册接口
     * @param faMember
     * @throws Exception
     */
    FaMember register(FaMember faMember) throws Exception;

    /**
     * 用户统计数据
     * @param faMember
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getMemberStatistics(FaMember faMember) throws Exception;

    /**
     * 批量审核身份认证
     * @param faMember
     * @throws Exception
     */
    void batchAuthMember(FaMember faMember) throws Exception;

    /**
     * 用户资金信息
     * @param faMember
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> fundInfo(FaMember faMember) throws Exception;

    /**
     * 用户交易信息
     * @param faMember
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> tradeInfo(FaMember faMember) throws Exception;

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    void updateFaMemberFreezeProfit(Integer memberId, BigDecimal amount, int direct) throws Exception;

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct 方向(0赠 1减)
     * @param type 类型(0先扣闲置资金 1先扣冻结)
     * @throws Exception
     */
    void updateFaMemberFreezeProfit(Integer memberId, BigDecimal amount, int direct, int type) throws Exception;

    /**
     * 更新用户余额
     * @param userId 用户id
     * @param money 变动金额
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    void updateMemberBalance(Integer userId, BigDecimal money, int direct) throws Exception;

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
     * 单个用户统计
     * @param faMember
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getMemberStatisticsSingle(FaMember faMember) throws Exception;

    /**
     * 获取用户手机号
     * @param faMember
     * @return
     * @throws Exception
     */
    String getMobile(FaMember faMember) throws Exception;

    /**
     * 用户手机号加密
     * @throws Exception
     */
    void aesMobile() throws Exception;

}