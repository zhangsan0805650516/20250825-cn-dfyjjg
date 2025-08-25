package com.ruoyi.biz.memberImage.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.memberImage.domain.FaMemberImage;

/**
 * 会员图片Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface FaMemberImageMapper extends BaseMapper<FaMemberImage>
{
    /**
     * 查询会员图片
     *
     * @param id 会员图片主键
     * @return 会员图片
     */
    public FaMemberImage selectFaMemberImageById(Integer id);

    /**
     * 查询会员图片列表
     *
     * @param faMemberImage 会员图片
     * @return 会员图片集合
     */
    public List<FaMemberImage> selectFaMemberImageList(FaMemberImage faMemberImage);

    /**
     * 新增会员图片
     *
     * @param faMemberImage 会员图片
     * @return 结果
     */
    public int insertFaMemberImage(FaMemberImage faMemberImage);

    /**
     * 修改会员图片
     *
     * @param faMemberImage 会员图片
     * @return 结果
     */
    public int updateFaMemberImage(FaMemberImage faMemberImage);

    /**
     * 删除会员图片
     *
     * @param id 会员图片主键
     * @return 结果
     */
    public int deleteFaMemberImageById(Integer id);

    /**
     * 批量删除会员图片
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaMemberImageByIds(Integer[] ids);
}