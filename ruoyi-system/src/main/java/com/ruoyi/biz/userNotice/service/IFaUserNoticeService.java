package com.ruoyi.biz.userNotice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;
import com.ruoyi.biz.userNotice.domain.FaUserNotice;

import java.util.List;

/**
 * 用户消息Service接口
 *
 * @author ruoyi
 * @date 2024-01-10
 */
public interface IFaUserNoticeService extends IService<FaUserNotice>
{
    /**
     * 查询用户消息
     *
     * @param id 用户消息主键
     * @return 用户消息
     */
    public FaUserNotice selectFaUserNoticeById(Integer id);

    /**
     * 查询用户消息列表
     *
     * @param faUserNotice 用户消息
     * @return 用户消息集合
     */
    public List<FaUserNotice> selectFaUserNoticeList(FaUserNotice faUserNotice);

    /**
     * 新增用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    public int insertFaUserNotice(FaUserNotice faUserNotice) throws Exception;

    /**
     * 修改用户消息
     *
     * @param faUserNotice 用户消息
     * @return 结果
     */
    public int updateFaUserNotice(FaUserNotice faUserNotice);

    /**
     * 批量删除用户消息
     *
     * @param ids 需要删除的用户消息主键集合
     * @return 结果
     */
    public int deleteFaUserNoticeByIds(Integer[] ids);

    /**
     * 删除用户消息信息
     *
     * @param id 用户消息主键
     * @return 结果
     */
    public int deleteFaUserNoticeById(Integer id);

    /**
     * 发申购中签通知
     * @param faSgList
     * @throws Exception
     */
    void addAllocation(FaSgList faSgList) throws Exception;

    /**
     * 发配售中签通知(申购=配售)
     * @param stockSgSecond
     * @throws Exception
     */
    void addAllocation(FaStockSgSecond stockSgSecond) throws Exception;

    /**
     * 发配售中签通知
     * @param faSgjiaoyi
     * @throws Exception
     */
    void addAllocation(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 发申购中签认缴通知
     * @param sgList
     * @throws Exception
     */
    void addSubscriptionBg(FaSgList sgList) throws Exception;

    /**
     * 发配售中签认缴通知(申购=配售)
     * @param faStockSgSecond
     * @throws Exception
     */
    void addSubscriptionBg(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 发配售中签认缴通知
     * @param sgjiaoyi
     * @throws Exception
     */
    void addSubscription(FaSgjiaoyi sgjiaoyi) throws Exception;

    /**
     * 查询用户消息列表
     * @param faUserNotice
     * @return
     * @throws Exception
     */
    IPage<FaUserNotice> getMemberSgPage(FaUserNotice faUserNotice) throws Exception;

    /**
     * 阅读用户消息
     * @param faUserNotice
     * @throws Exception
     */
    void readUserNotice(FaUserNotice faUserNotice) throws Exception;

    /**
     * 阅读用户消息(所有)
     * @param faUserNotice
     * @throws Exception
     */
    void readUserNoticeAll(FaUserNotice faUserNotice) throws Exception;

    /**
     * 用户消息详情
     * @param faUserNotice
     * @throws Exception
     */
    FaUserNotice userNoticeDetail(FaUserNotice faUserNotice) throws Exception;

    /**
     * 未读消息条数
     * @param faUserNotice
     * @return
     * @throws Exception
     */
    long unreadNoticeNum(FaUserNotice faUserNotice) throws Exception;
}