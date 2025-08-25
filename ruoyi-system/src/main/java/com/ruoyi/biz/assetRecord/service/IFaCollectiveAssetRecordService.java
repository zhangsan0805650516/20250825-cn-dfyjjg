package com.ruoyi.biz.assetRecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;

import java.util.List;

/**
 * 集合资产记录Service接口
 *
 * @author ruoyi
 * @date 2025-02-17
 */
public interface IFaCollectiveAssetRecordService extends IService<FaCollectiveAssetRecord>
{
    /**
     * 查询集合资产记录
     *
     * @param id 集合资产记录主键
     * @return 集合资产记录
     */
    public FaCollectiveAssetRecord selectFaCollectiveAssetRecordById(Integer id);

    /**
     * 查询集合资产记录列表
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 集合资产记录集合
     */
    public List<FaCollectiveAssetRecord> selectFaCollectiveAssetRecordList(FaCollectiveAssetRecord faCollectiveAssetRecord);

    /**
     * 新增集合资产记录
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 结果
     */
    public int insertFaCollectiveAssetRecord(FaCollectiveAssetRecord faCollectiveAssetRecord);

    /**
     * 修改集合资产记录
     *
     * @param faCollectiveAssetRecord 集合资产记录
     * @return 结果
     */
    public int updateFaCollectiveAssetRecord(FaCollectiveAssetRecord faCollectiveAssetRecord);

    /**
     * 批量删除集合资产记录
     *
     * @param ids 需要删除的集合资产记录主键集合
     * @return 结果
     */
    public int deleteFaCollectiveAssetRecordByIds(Integer[] ids);

    /**
     * 删除集合资产记录信息
     *
     * @param id 集合资产记录主键
     * @return 结果
     */
    public int deleteFaCollectiveAssetRecordById(Integer id);

    /**
     * 审核赎回
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    void approveRedeem(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception;
}