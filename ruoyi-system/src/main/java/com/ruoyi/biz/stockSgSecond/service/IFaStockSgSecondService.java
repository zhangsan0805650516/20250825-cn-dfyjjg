package com.ruoyi.biz.stockSgSecond.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.stockSgSecond.domain.FaStockSgSecond;

import java.util.List;

/**
 * 线下配售(世纪独享)Service接口
 *
 * @author ruoyi
 * @date 2024-11-01
 */
public interface IFaStockSgSecondService extends IService<FaStockSgSecond>
{
    /**
     * 查询线下配售(世纪独享)
     *
     * @param id 线下配售(世纪独享)主键
     * @return 线下配售(世纪独享)
     */
    public FaStockSgSecond selectFaStockSgSecondById(Long id);

    /**
     * 查询线下配售(世纪独享)列表
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 线下配售(世纪独享)集合
     */
    public List<FaStockSgSecond> selectFaStockSgSecondList(FaStockSgSecond faStockSgSecond);

    /**
     * 新增线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    public int insertFaStockSgSecond(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 修改线下配售(世纪独享)
     *
     * @param faStockSgSecond 线下配售(世纪独享)
     * @return 结果
     */
    public int updateFaStockSgSecond(FaStockSgSecond faStockSgSecond);

    /**
     * 批量删除线下配售(世纪独享)
     *
     * @param ids 需要删除的线下配售(世纪独享)主键集合
     * @return 结果
     */
    public int deleteFaStockSgSecondByIds(Long[] ids);

    /**
     * 删除线下配售(世纪独享)信息
     *
     * @param id 线下配售(世纪独享)主键
     * @return 结果
     */
    public int deleteFaStockSgSecondById(Long id);

    /**
     * 查询用户配售列表
     * @param faStockSgSecond
     * @return
     * @throws Exception
     */
    IPage<FaStockSgSecond> getMemberStockSgSecondPage(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 提交中签(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    void submitAllocation(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 后台认缴(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    void subscription(FaStockSgSecond faStockSgSecond) throws Exception;

    /**
     * 一键转持仓(世纪独享)
     * @throws Exception
     */
    void transToHold() throws Exception;

    /**
     * 单个转持仓(世纪独享)
     * @param faStockSgSecond
     * @throws Exception
     */
    void transOneToHold(FaStockSgSecond faStockSgSecond) throws Exception;
}