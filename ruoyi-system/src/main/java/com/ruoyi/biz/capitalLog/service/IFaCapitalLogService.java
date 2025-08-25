package com.ruoyi.biz.capitalLog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;

import java.util.List;

/**
 * 资金记录Service接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface IFaCapitalLogService extends IService<FaCapitalLog>
{
    /**
     * 查询资金记录
     *
     * @param id 资金记录主键
     * @return 资金记录
     */
    public FaCapitalLog selectFaCapitalLogById(Integer id);

    /**
     * 查询资金记录列表
     *
     * @param faCapitalLog 资金记录
     * @return 资金记录集合
     */
    public List<FaCapitalLog> selectFaCapitalLogList(FaCapitalLog faCapitalLog);

    /**
     * 新增资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    public int insertFaCapitalLog(FaCapitalLog faCapitalLog);

    /**
     * 修改资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    public int updateFaCapitalLog(FaCapitalLog faCapitalLog);

    /**
     * 批量删除资金记录
     *
     * @param ids 需要删除的资金记录主键集合
     * @return 结果
     */
    public int deleteFaCapitalLogByIds(Integer[] ids);

    /**
     * 删除资金记录信息
     *
     * @param id 资金记录主键
     * @return 结果
     */
    public int deleteFaCapitalLogById(Integer id);

    /**
     * 查询资金记录
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    IPage<FaCapitalLog> getFundRecord(FaCapitalLog faCapitalLog) throws Exception;

    /**
     * 查询资金池记录
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    IPage<FaCapitalLog> getFundPoolRecord(FaCapitalLog faCapitalLog) throws Exception;

    /**
     * 保存资金流水
     * @param faStockTrading
     * @throws Exception
     */
    void save(FaStockTrading faStockTrading) throws Exception;

    /**
     * 保存申购中签认缴流水
     * @param sgList
     * @throws Exception
     */
    void save(FaSgList sgList) throws Exception;

    /**
     * 保存配售中签认缴流水(申购=配售)
     * @param faStockSgSecond
     * @throws Exception
     */
    void save(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 保存配售中签认缴流水
     * @param sgjiaoyi
     * @throws Exception
     */
    void save(FaSgjiaoyi sgjiaoyi) throws Exception;

    /**
     * 充值资金流水
     * @param recharge
     * @throws Exception
     */
    void save(FaRecharge recharge) throws Exception;

    /**
     * 提现资金流水
     * @param withdraw
     * @throws Exception
     */
    void save(FaWithdraw withdraw) throws Exception;

    /**
     * 提交配售，资金转冻结
     * @param faSgjiaoyi
     * @throws Exception
     */
    void savePeiShou(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 保存大宗提交流水
     * @param faTradeApprove
     * @return
     * @throws Exception
     */
    FaCapitalLog save(FaTradeApprove faTradeApprove) throws Exception;
}