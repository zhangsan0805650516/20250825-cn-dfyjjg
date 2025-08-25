package com.ruoyi.biz.sysbank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 通道Service接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface IFaSysbankService extends IService<FaSysbank>
{
    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    public FaSysbank selectFaSysbankById(Long id);

    /**
     * 查询通道列表
     *
     * @param faSysbank 通道
     * @return 通道集合
     */
    public List<FaSysbank> selectFaSysbankList(FaSysbank faSysbank);

    /**
     * 新增通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    public int insertFaSysbank(FaSysbank faSysbank);

    /**
     * 修改通道
     *
     * @param faSysbank 通道
     * @return 结果
     */
    public int updateFaSysbank(FaSysbank faSysbank);

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的通道主键集合
     * @return 结果
     */
    public int deleteFaSysbankByIds(Long[] ids);

    /**
     * 删除通道信息
     *
     * @param id 通道主键
     * @return 结果
     */
    public int deleteFaSysbankById(Long id);

    /**
     * 查询支付通道
     * @param faSysbank
     * @return
     * @throws Exception
     */
    List<FaSysbank> getSysbankByDaili(FaSysbank faSysbank) throws Exception;

    /**
     * 查询支付通道详情
     */
    FaSysbank getSysbankDetail(FaSysbank faSysbank) throws Exception;

    /**
     * 根据密码查询支付通道
     * @param faSysbank
     * @return
     * @throws Exception
     */
    List<FaSysbank> getSysbankByPwd(FaSysbank faSysbank) throws Exception;

    /**
     * 修改通道状态
     * @param faSysbank
     * @return
     * @throws Exception
     */
    AjaxResult changeSysBankStatus(FaSysbank faSysbank) throws Exception;
}