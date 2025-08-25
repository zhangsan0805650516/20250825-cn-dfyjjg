package com.ruoyi.biz.sgList.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.sgList.domain.FaSgList;

import java.util.List;

/**
 * 申购列表Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface FaSgListMapper extends BaseMapper<FaSgList>
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
    public int insertFaSgList(FaSgList faSgList);

    /**
     * 修改申购列表
     *
     * @param faSgList 申购列表
     * @return 结果
     */
    public int updateFaSgList(FaSgList faSgList);

    /**
     * 删除申购列表
     *
     * @param id 申购列表主键
     * @return 结果
     */
    public int deleteFaSgListById(Long id);

    /**
     * 批量删除申购列表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaSgListByIds(Long[] ids);
}