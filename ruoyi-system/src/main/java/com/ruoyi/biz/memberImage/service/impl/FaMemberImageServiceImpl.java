package com.ruoyi.biz.memberImage.service.impl;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.strategy.service.impl.ChinaStrategyServiceImpl;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.ThumbnailatorUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.stockUtils.StockUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.biz.memberImage.mapper.FaMemberImageMapper;
import com.ruoyi.biz.memberImage.domain.FaMemberImage;
import com.ruoyi.biz.memberImage.service.IFaMemberImageService;

/**
 * 会员图片Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class FaMemberImageServiceImpl extends ServiceImpl<FaMemberImageMapper, FaMemberImage> implements IFaMemberImageService
{
    @Autowired
    private FaMemberImageMapper faMemberImageMapper;

    /**
     * 查询会员图片
     *
     * @param id 会员图片主键
     * @return 会员图片
     */
    @Override
    public FaMemberImage selectFaMemberImageById(Integer id)
    {
        return faMemberImageMapper.selectFaMemberImageById(id);
    }

    /**
     * 查询会员图片列表
     *
     * @param faMemberImage 会员图片
     * @return 会员图片
     */
    @Override
    public List<FaMemberImage> selectFaMemberImageList(FaMemberImage faMemberImage)
    {
        faMemberImage.setDeleteFlag(0);
        return faMemberImageMapper.selectFaMemberImageList(faMemberImage);
    }

    /**
     * 新增会员图片
     *
     * @param faMemberImage 会员图片
     * @return 结果
     */
    @Override
    public int insertFaMemberImage(FaMemberImage faMemberImage)
    {
        faMemberImage.setCreateTime(DateUtils.getNowDate());
        return faMemberImageMapper.insertFaMemberImage(faMemberImage);
    }

    /**
     * 修改会员图片
     *
     * @param faMemberImage 会员图片
     * @return 结果
     */
    @Override
    public int updateFaMemberImage(FaMemberImage faMemberImage)
    {
        faMemberImage.setUpdateTime(DateUtils.getNowDate());
        return faMemberImageMapper.updateFaMemberImage(faMemberImage);
    }

    /**
     * 批量删除会员图片
     *
     * @param ids 需要删除的会员图片主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberImageByIds(Integer[] ids)
    {
        return faMemberImageMapper.deleteFaMemberImageByIds(ids);
    }

    /**
     * 删除会员图片信息
     *
     * @param id 会员图片主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberImageById(Integer id)
    {
        return faMemberImageMapper.deleteFaMemberImageById(id);
    }

    /**
     * 注册用户同步到图片表
     * @param faMember
     * @throws Exception
     */
    @Override
    public void register(FaMember faMember) throws Exception {
        if (null != faMember.getId()) {
            Thread thread = new Thread(createMemberImage(faMember.getId()));
            thread.start();
        }
    }

    /**
     * 注册用户同步到图片表
     * @param id
     * @return
     */
    public static TimerTask createMemberImage(Integer id) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                FaMemberImage faMemberImage = new FaMemberImage();
                faMemberImage.setMemberId(id);
                faMemberImage.setCreateTime(new Date());
                SpringUtils.getBean(FaMemberImageServiceImpl.class).save(faMemberImage);
            }
        };
    }

    /**
     * 异步更新用户图片
     * @param faMember
     * @throws Exception
     */
    @Override
    public void updateImage(FaMember faMember) throws Exception {
        if (null != faMember.getId()) {
            Thread thread = new Thread(updateMemberImage(faMember));
            thread.start();
        }
    }

    /**
     * 异步更新用户图片
     * @param faMember
     * @return
     */
    public static TimerTask updateMemberImage(FaMember faMember) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                LambdaUpdateWrapper<FaMemberImage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaMemberImage::getMemberId, faMember.getId());

                // 头像
                if (StringUtils.isNotEmpty(faMember.getAvatar())) {
                    if (faMember.getAvatar().contains("http")) {
                        // 转base64
                        String avatar64 = ThumbnailatorUtil.compressToBase64(faMember.getAvatar());
                        lambdaUpdateWrapper.set(FaMemberImage::getAvatar, avatar64);
                        lambdaUpdateWrapper.set(FaMemberImage::getAvatarUrl, faMember.getAvatar());
                    }
                }
                // 身份证正面
                if (StringUtils.isNotEmpty(faMember.getIdCardFrontImage())) {
                    if (faMember.getIdCardFrontImage().contains("http")) {
                        // 转base64
                        String idCardFrontImage64 = ThumbnailatorUtil.compressToBase64(faMember.getIdCardFrontImage());
                        lambdaUpdateWrapper.set(FaMemberImage::getIdCardFrontImage, idCardFrontImage64);
                        lambdaUpdateWrapper.set(FaMemberImage::getIdCardFrontImageUrl, faMember.getIdCardFrontImage());
                    }
                }
                // 身份证反面
                if (StringUtils.isNotEmpty(faMember.getIdCardBackImage())) {
                    if (faMember.getIdCardBackImage().contains("http")) {
                        // 转base64
                        String idCardBackImage64 = ThumbnailatorUtil.compressToBase64(faMember.getIdCardBackImage());
                        lambdaUpdateWrapper.set(FaMemberImage::getIdCardBackImage, idCardBackImage64);
                        lambdaUpdateWrapper.set(FaMemberImage::getIdCardBackImageUrl, faMember.getIdCardBackImage());
                    }
                }
                // 银行卡照片
                if (StringUtils.isNotEmpty(faMember.getCardImage())) {
                    if (faMember.getCardImage().contains("http")) {
                        // 转base64
                        String cardImage64 = ThumbnailatorUtil.compressToBase64(faMember.getCardImage());
                        lambdaUpdateWrapper.set(FaMemberImage::getCardImage, cardImage64);
                        lambdaUpdateWrapper.set(FaMemberImage::getCardImageUrl, faMember.getCardImage());
                    }
                }

                lambdaUpdateWrapper.set(FaMemberImage::getUpdateTime, new Date());
                SpringUtils.getBean(FaMemberImageServiceImpl.class).update(lambdaUpdateWrapper);
            }
        };
    }

}