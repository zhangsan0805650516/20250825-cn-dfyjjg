package com.ruoyi.biz.sgjiaoyi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;

import java.math.BigDecimal;
import java.util.List;

/**
 * 线下配售Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface IFaSgjiaoyiService extends IService<FaSgjiaoyi>
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
    public int insertFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 修改线下配售
     *
     * @param faSgjiaoyi 线下配售
     * @return 结果
     */
    public int updateFaSgjiaoyi(FaSgjiaoyi faSgjiaoyi);

    /**
     * 批量删除线下配售
     *
     * @param ids 需要删除的线下配售主键集合
     * @return 结果
     */
    public int deleteFaSgjiaoyiByIds(Long[] ids);

    /**
     * 删除线下配售信息
     *
     * @param id 线下配售主键
     * @return 结果
     */
    public int deleteFaSgjiaoyiById(Long id);

    /**
     * 提交配售中签 保证金模式
     * @param faSgjiaoyi
     * @throws Exception
     */
    void submitAllocation(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 提交配售中签 补缴模式
     * @param faSgjiaoyi
     * @throws Exception
     */
    void submitAllocationPayLater(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 一键转持仓
     * @throws Exception
     */
    void transToHold() throws Exception;

    /**
     * 查询用户配售列表
     * @param faSgjiaoyi
     * @return
     * @throws Exception
     */
    IPage<FaSgjiaoyi> getMemberPsList(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 配售认缴
     * @param faSgjiaoyi
     * @throws Exception
     */
    void subscription(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 单个转持仓
     * @param faSgjiaoyi
     * @throws Exception
     */
    void transOneToHold(FaSgjiaoyi faSgjiaoyi) throws Exception;

    /**
     * 未中签退费
     * @param ids
     * @throws Exception
     */
    void refund(Long[] ids) throws Exception;

    /**
     * 增加的余额去补缴
     * @param id
     * @param amount
     * @throws Exception
     */
    void finishPayLater(Integer id, BigDecimal amount) throws Exception;
}