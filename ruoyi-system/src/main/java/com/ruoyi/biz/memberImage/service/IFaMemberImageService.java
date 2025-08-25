package com.ruoyi.biz.memberImage.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.memberImage.domain.FaMemberImage;
import com.ruoyi.common.core.domain.entity.FaMember;

/**
 * 会员图片Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IFaMemberImageService extends IService<FaMemberImage>
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
     * 批量删除会员图片
     *
     * @param ids 需要删除的会员图片主键集合
     * @return 结果
     */
    public int deleteFaMemberImageByIds(Integer[] ids);

    /**
     * 删除会员图片信息
     *
     * @param id 会员图片主键
     * @return 结果
     */
    public int deleteFaMemberImageById(Integer id);

    /**
     * 注册用户同步到图片表
     * @param faMember
     * @throws Exception
     */
    void register(FaMember faMember) throws Exception;

    /**
     * 异步更新用户图片
     * @param faMember
     * @throws Exception
     */
    void updateImage(FaMember faMember) throws Exception;
}