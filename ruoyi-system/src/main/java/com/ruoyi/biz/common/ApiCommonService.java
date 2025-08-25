package com.ruoyi.biz.common;

import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.common.core.domain.entity.FaMember;
import org.springframework.web.multipart.MultipartFile;

/**
 * 策略Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface ApiCommonService
{

    String upload(MultipartFile file, String ip) throws Exception;

    String getPayment(FaMember faMember, FaRecharge faRecharge) throws Exception;
}