package com.ruoyi.biz.shengou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.shengou.domain.FaNewStock;

import java.util.List;

/**
 * 新股列表Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface IFaShengouService extends IService<FaNewStock>
{
    /**
     * 查询新股列表
     *
     * @param id 新股列表主键
     * @return 新股列表
     */
    public FaNewStock selectFaShengouById(Long id);

    /**
     * 查询新股列表列表
     *
     * @param faNewStock 新股列表
     * @return 新股列表集合
     */
    public List<FaNewStock> selectFaShengouList(FaNewStock faNewStock);

    /**
     * 新增新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    public int insertFaShengou(FaNewStock faNewStock);

    /**
     * 修改新股列表
     *
     * @param faNewStock 新股列表
     * @return 结果
     */
    public int updateFaShengou(FaNewStock faNewStock);

    /**
     * 批量删除新股列表
     *
     * @param ids 需要删除的新股列表主键集合
     * @return 结果
     */
    public int deleteFaShengouByIds(Long[] ids);

    /**
     * 删除新股列表信息
     *
     * @param id 新股列表主键
     * @return 结果
     */
    public int deleteFaShengouById(Long id);

    /**
     * 查询新股申购列表，按申购日期分类排序
     * @return
     * @throws Exception
     */
    List<FaNewStock> getShengouListByGroup(FaNewStock faNewStock) throws Exception;

    /**
     * 查询配售列表(申购=配售)
     * @param faNewStock
     * @return
     * @throws Exception
     */
    List<FaNewStock> getSgSecondList(FaNewStock faNewStock) throws Exception;

    /**
     * 查询新股配售列表, 按配售日期分类排序
     * @return
     * @throws Exception
     */
    List<FaNewStock> getPeiShouListByGroup() throws Exception;

    /**
     * 修改申购配售开关
     * @param faNewStock
     * @throws Exception
     */
    void changeSwitch(FaNewStock faNewStock) throws Exception;

    /**
     * 查询新股详情
     * @param faNewStock
     * @return
     * @throws Exception
     */
    FaNewStock getShengouDetail(FaNewStock faNewStock) throws Exception;

    /**
     * 一键申购
     * @param faNewStock
     * @throws Exception
     */
    void addShengou(FaNewStock faNewStock) throws Exception;

    /**
     * 一键配售(申购=配售)
     * @param faNewStock
     * @throws Exception
     */
    void addSgSecond(FaNewStock faNewStock) throws Exception;

    /**
     * 一键配售 保证金模式
     * @param faNewStock
     * @throws Exception
     */
    void addPeiShou(FaNewStock faNewStock) throws Exception;

    /**
     * 一键配售 补缴模式
     * @param faNewStock
     * @throws Exception
     */
    void addPeiShouPayLater(FaNewStock faNewStock) throws Exception;

    /**
     * 搜索新股
     * @param faNewStock
     * @return
     * @throws Exception
     */
    List<FaNewStock> searchNewStock(FaNewStock faNewStock) throws Exception;

    /**
     * 提交申购配售配置
     * @param faNewStock
     * @throws Exception
     */
    void submitExchange(FaNewStock faNewStock) throws Exception;
}