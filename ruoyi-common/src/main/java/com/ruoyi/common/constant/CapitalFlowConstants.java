package com.ruoyi.common.constant;

/**
 * 缓存的key 常量
 * 
 * @author ruoyi
 */
public class CapitalFlowConstants
{

    public static final String CAPITAL_FLOW = "capital_flow_type";

//    0充值 1提现
//    2普通下单 3普通下单手续费 4印花税 5平仓收益  
//    6中签认缴 7提现退回  
//    8大宗下单 9大宗下单手续费 10大宗平仓收益 11大宗印花税  
//    12配售冻结 13未中签退回 14普通平仓手续费 15大宗卖出手续费  
//    16VIP调研下单 17VIP调研下单手续费 18VIP调研平仓收益 19VIP调研印花税 20VIP调研卖出手续费  
//    21货币兑换出 22货币兑换入  
//    23大宗冻结 24大宗退回  
//    25融券下单 26融券下单手续费 27融券平仓收益 28融券印花税 29融券平仓手续费 
//    30投票冻结 301投票解冻 
//    31重组下单 32重组下单手续费 33重组平仓收益 34重组印花税 35重组平仓手续费 
//    40买入集合资产 41赎回集合资产 42赎回退回
//    1101调整余额 1102调整冻结
//    1002资金池增加 1003资金池转出 1004转入资金池 1005资金池转出 1006转入资金池

    public static final Integer RECHARGE = 0;
    public static final Integer WITHDRAW = 1;
    public static final Integer NORMAL_BUY = 2;
    public static final Integer NORMAL_BUY_FEE = 3;
    public static final Integer NORMAL_SELL_STAMP_DUTY = 4;
    public static final Integer NORMAL_SELL = 5;
    public static final Integer SUBSCRIPTION = 6;
    public static final Integer WITHDRAW_RETURN = 7;
    public static final Integer DZ_BUY = 8;
    public static final Integer DZ_BUY_FEE = 9;
    public static final Integer DZ_SELL = 10;
    public static final Integer DZ_SELL_STAMP_DUTY = 11;
    public static final Integer PS_FREEZE = 12;
    public static final Integer NOT_ALLOCATION_RETURN = 13;
    public static final Integer NORMAL_SELL_FEE = 14;
    public static final Integer DZ_SELL_FEE = 15;
    public static final Integer VIP_BUY = 16;
    public static final Integer VIP_BUY_FEE = 17;
    public static final Integer VIP_SELL = 18;
    public static final Integer VIP_SELL_STAMP_DUTY = 19;
    public static final Integer VIP_SELL_FEE = 20;
    public static final Integer CURRENCY_EXCHANGE_OUT = 21;
    public static final Integer CURRENCY_EXCHANGE_IN = 22;
    public static final Integer DZ_FREEZE = 23;
    public static final Integer DZ_RETURN = 24;
    public static final Integer RQ_BUY = 25;
    public static final Integer RQ_FEE = 26;
    public static final Integer RQ_SELL = 27;
    public static final Integer RQ_SELL_STAMP_DUTY = 28;
    public static final Integer RQ_SELL_FEE = 29;
    public static final Integer VOTE_FREEZE = 30;
    public static final Integer VOTE_UNFREEZE = 301;
    public static final Integer YYCZ_BUY = 31;
    public static final Integer YYCZ_BUY_FEE = 32;
    public static final Integer YYCZ_SELL = 33;
    public static final Integer YYCZ_SELL_STAMP_DUTY = 34;
    public static final Integer YYCZ_SELL_FEE = 35;
    public static final Integer JHZC_BUY = 40;
    public static final Integer JHZC_REDEEM = 41;
    public static final Integer JHZC_REDEEM_RETURN = 42;
    public static final Integer OUT_FUND_POOL = 1003;
    public static final Integer IN_FUND_POOL = 1004;
    public static final Integer OUT_MEMBER = 1005;
    public static final Integer IN_MEMBER = 1006;
    public static final Integer CHANGE_BALANCE = 1101;
    public static final Integer CHANGE_FREEZE = 1102;

}
