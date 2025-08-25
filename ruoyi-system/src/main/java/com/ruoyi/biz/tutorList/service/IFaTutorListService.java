package com.ruoyi.biz.tutorList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.tutorList.domain.FaTutorList;

import java.util.List;

/**
 * 分类Service接口
 *
 * @author ruoyi
 * @date 2024-10-10
 */
public interface IFaTutorListService extends IService<FaTutorList>
{
    /**
     * 查询分类
     *
     * @param id 分类主键
     * @return 分类
     */
    public FaTutorList selectFaTutorListById(Integer id);

    /**
     * 查询分类列表
     *
     * @param faTutorList 分类
     * @return 分类集合
     */
    public List<FaTutorList> selectFaTutorListList(FaTutorList faTutorList);

    /**
     * 新增分类
     *
     * @param faTutorList 分类
     * @return 结果
     */
    public int insertFaTutorList(FaTutorList faTutorList);

    /**
     * 修改分类
     *
     * @param faTutorList 分类
     * @return 结果
     */
    public int updateFaTutorList(FaTutorList faTutorList);

    /**
     * 批量删除分类
     *
     * @param ids 需要删除的分类主键集合
     * @return 结果
     */
    public int deleteFaTutorListByIds(Integer[] ids);

    /**
     * 删除分类信息
     *
     * @param id 分类主键
     * @return 结果
     */
    public int deleteFaTutorListById(Integer id);

    /**
     * 获取导师列表
     * @param faTutorList
     * @return
     * @throws Exception
     */
    List<FaTutorList> getTutorList(FaTutorList faTutorList) throws Exception;

    /**
     * 搜索导师
     * @param faTutorList
     * @return
     * @throws Exception
     */
    List<FaTutorList> searchTutor(FaTutorList faTutorList) throws Exception;
}