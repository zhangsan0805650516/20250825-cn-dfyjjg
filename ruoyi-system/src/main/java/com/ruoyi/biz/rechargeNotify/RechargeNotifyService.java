package com.ruoyi.biz.rechargeNotify;

import com.ruoyi.biz.recharge.domain.*;

import javax.servlet.http.HttpServletRequest;

public interface RechargeNotifyService {

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void nineBrotherRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 仁德充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void rendeRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 火箭充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void huojianRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 四方充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void sifangRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 充值回调接口json代付
     * @param rechargeNotifyJson
     * @throws Exception
     */
    void daiFuRechargeNotify(RechargeNotify rechargeNotifyJson) throws Exception;

    /**
     * FML充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void FMLRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 亨通充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void hengTongRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 衫德宝充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void shanDeBaoRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * XL充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void xlRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 鲨鱼充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void shaYuRechargeNotify(ShaYuRechargeNotify rechargeNotify) throws Exception;

    /**
     * AX充值回调接口
     * @param rechargeNotifyForm
     * @throws Exception
     */
    void axRechargeNotify(RechargeNotify rechargeNotifyForm) throws Exception;

    /**
     * 海贼充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void haiZeiRechargeNotify(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 鸿运充值回调接口
     * @param hongYunRechargeNotify
     * @throws Exception
     */
    void hongYunRechargeNotify(HongYunRechargeNotify hongYunRechargeNotify) throws Exception;

    /**
     * 中信充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void zhongXinRechargeNotify(ZhongXinRechargeNotify rechargeNotify) throws Exception;

    /**
     * 伟翰充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    void rechargeNotifyFormWeiHan(RechargeNotify rechargeNotify) throws Exception;

    /**
     * 充值回调接口form飞云
     * @param feiYunRechargeNotify
     * @throws Exception
     */
    void feiYunRechargeNotify(FeiYunRechargeNotify feiYunRechargeNotify) throws Exception;

    /**
     * 回调接口
     * @param json
     * @param id
     * @throws Exception
     */
    String rechargeNotify(String json, Integer id) throws Exception;

    /**
     * 回调接口
     * @param request
     * @param id
     * @throws Exception
     */
    String rechargeNotify(HttpServletRequest request, Integer id) throws Exception;
}
