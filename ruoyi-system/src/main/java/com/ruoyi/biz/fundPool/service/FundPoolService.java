package com.ruoyi.biz.fundPool.service;

import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.common.core.domain.entity.FaMember;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface FundPoolService {

    /**
     * 获取资金池信息
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    Map<String, BigDecimal> getFundPoolInfo(FaCapitalLog faCapitalLog) throws Exception;

    /**
     * 资金池更新
     * @param userId 关联用户
     * @param money 金额
     * @param direct 方向(0赠 1减)
     * @param content 流水内容
     * @param type 流水类型
     * @param creatTime 创建时间
     * @throws Exception
     */
    void updateMoney(Integer userId, BigDecimal money, int direct, String content, int type, Date creatTime) throws Exception;

    /**
     * 资金池余额
     * @return
     * @throws Exception
     */
    BigDecimal getFundPoolBalance() throws Exception;

    /**
     * 用户转入资金池
     * @param faMember
     * @throws Exception
     */
    void transferToFundPool(FaMember faMember) throws Exception;
}
