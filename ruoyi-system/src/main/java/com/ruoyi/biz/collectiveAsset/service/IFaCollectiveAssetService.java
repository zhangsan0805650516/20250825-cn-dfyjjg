package com.ruoyi.biz.collectiveAsset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.assetRecord.domain.FaCollectiveAssetRecord;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;

import java.util.List;

/**
 * 集合资产Service接口
 *
 * @author ruoyi
 * @date 2025-02-17
 */
public interface IFaCollectiveAssetService extends IService<FaCollectiveAsset>
{
    /**
     * 查询集合资产
     *
     * @param id 集合资产主键
     * @return 集合资产
     */
    public FaCollectiveAsset selectFaCollectiveAssetById(Integer id);

    /**
     * 查询集合资产列表
     *
     * @param faCollectiveAsset 集合资产
     * @return 集合资产集合
     */
    public List<FaCollectiveAsset> selectFaCollectiveAssetList(FaCollectiveAsset faCollectiveAsset);

    /**
     * 新增集合资产
     *
     * @param faCollectiveAsset 集合资产
     * @return 结果
     */
    public int insertFaCollectiveAsset(FaCollectiveAsset faCollectiveAsset);

    /**
     * 修改集合资产
     *
     * @param faCollectiveAsset 集合资产
     * @return 结果
     */
    public int updateFaCollectiveAsset(FaCollectiveAsset faCollectiveAsset);

    /**
     * 批量删除集合资产
     *
     * @param ids 需要删除的集合资产主键集合
     * @return 结果
     */
    public int deleteFaCollectiveAssetByIds(Integer[] ids);

    /**
     * 删除集合资产信息
     *
     * @param id 集合资产主键
     * @return 结果
     */
    public int deleteFaCollectiveAssetById(Integer id);

    /**
     * 集合资产列表
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    List<FaCollectiveAsset> getAssetList(FaCollectiveAsset faCollectiveAsset) throws Exception;

    /**
     * 买入集合资产详情
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    FaCollectiveAsset getBuyAssetDetail(FaCollectiveAsset faCollectiveAsset) throws Exception;

    /**
     * 赎回集合资产详情
     * @param faCollectiveAsset
     * @return
     * @throws Exception
     */
    FaCollectiveAsset getRedeemAssetDetail(FaCollectiveAsset faCollectiveAsset) throws Exception;

    /**
     * 集合资产买入
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    void buyAsset(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception;

    /**
     * 集合资产赎回
     * @param faCollectiveAssetRecord
     * @throws Exception
     */
    void redeemAsset(FaCollectiveAssetRecord faCollectiveAssetRecord) throws Exception;

    /**
     * 集合资产开始
     * @param faCollectiveAsset
     * @throws Exception
     */
    void startAsset(FaCollectiveAsset faCollectiveAsset) throws Exception;

    /**
     * 集合资产结束
     * @param faCollectiveAsset
     * @throws Exception
     */
    void endAsset(FaCollectiveAsset faCollectiveAsset) throws Exception;
}