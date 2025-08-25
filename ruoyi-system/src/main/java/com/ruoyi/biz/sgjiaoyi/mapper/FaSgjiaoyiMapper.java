package com.ruoyi.biz.sgjiaoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;

import java.util.List;

/**
 * 线下配售Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface FaSgjiaoyiMapper extends BaseMapper<FaSgjiaoyi>
{
    /**
     * 查询线下配售
     *
     * @param id 线下配售主键
     * @return 线下配售
     */
    public FaSgjiaoyi selectFaSgjiaoyiById(Long id);

    /**
     * 查询线下配售列表
     *
     * @param faSgjiaoyi 线下配售
     * @return 线下配售集合
     */
    public List<FaSgjiaoyi> selectFaSgjiaoyiList(FaSgjiaoyi faSgjiaoyi);

    /**
     * 新增线下配售
     *
     * @param faSgjiaoyi 线下配售
     * @return 结果
     */
    public int insertFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi);

    /**
     * 修改线下配售
     *
     * @param faSgjiaoyi 线下配售
     * @return 结果
     */
    public int updateFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi);

    /**
     * 删除线下配售
     *
     * @param id 线下配售主键
     * @return 结果
     */
    public int deleteFaSgjiaoyiById(Long id);

    /**
     * 批量删除线下配售
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaSgjiaoyiByIds(Long[] ids);
}