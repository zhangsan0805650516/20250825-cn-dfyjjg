package com.ruoyi.biz.sgList.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.sgList.domain.FaSgList;

import java.util.List;

/**
 * 申购列表Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface IFaSgListService extends IService<FaSgList>
{
    /**
     * 查询申购列表
     *
     * @param id 申购列表主键
     * @return 申购列表
     */
    public FaSgList selectFaSgListById(Long id);

    /**
     * 查询申购列表列表
     *
     * @param faSgList 申购列表
     * @return 申购列表集合
     */
    public List<FaSgList> selectFaSgListList(FaSgList faSgList);

    /**
     * 新增申购列表
     *
     * @param faSgList 申购列表
     * @return 结果
     */
    public int insertFaSgList(FaSgList faSgList) throws Exception;

    /**
     * 修改申购列表
     *
     * @param faSgList 申购列表
     * @return 结果
     */
    public int updateFaSgList(FaSgList faSgList);

    /**
     * 批量删除申购列表
     *
     * @param ids 需要删除的申购列表主键集合
     * @return 结果
     */
    public int deleteFaSgListByIds(Long[] ids);

    /**
     * 删除申购列表信息
     *
     * @param id 申购列表主键
     * @return 结果
     */
    public int deleteFaSgListById(Long id);

    /**
     * 查询用户申购列表
     * @param faSgList
     * @return
     * @throws Exception
     */
    IPage<FaSgList> getMemberSgPage(FaSgList faSgList) throws Exception;

    /**
     * 提交中签
     * @param faSgList
     * @throws Exception
     */
    void submitAllocation(FaSgList faSgList) throws Exception;

    /**
     * 申购认缴
     * @param faSgList
     * @throws Exception
     */
    void subscription(FaSgList faSgList) throws Exception;

    /**
     * 一键转持仓
     * @throws Exception
     */
    void transToHold() throws Exception;

    /**
     * 单个转持仓
     * @param faSgList
     * @throws Exception
     */
    void transOneToHold(FaSgList faSgList) throws Exception;

}