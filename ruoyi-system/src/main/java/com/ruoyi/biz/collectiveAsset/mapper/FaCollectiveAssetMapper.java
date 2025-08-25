package com.ruoyi.biz.collectiveAsset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.collectiveAsset.domain.FaCollectiveAsset;

import java.util.List;

/**
 * 集合资产Mapper接口
 *
 * @author ruoyi
 * @date 2025-02-17
 */
public interface FaCollectiveAssetMapper extends BaseMapper<FaCollectiveAsset>
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
     * 删除集合资产
     *
     * @param id 集合资产主键
     * @return 结果
     */
    public int deleteFaCollectiveAssetById(Integer id);

    /**
     * 批量删除集合资产
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaCollectiveAssetByIds(Integer[] ids);
}