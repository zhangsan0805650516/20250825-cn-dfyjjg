package com.ruoyi.biz.searchMember.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 搜索用户记录
 *
 * @author ruoyi
 * @date 2024-12-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_search_member")
@ApiModel(value = "搜索用户记录")
public class FaSearchMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String weiyima;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 搜索次数 */
    @ApiModelProperty(value = "搜索次数")
    @Excel(name = "搜索次数")
    @TableField("search_times")
    private Integer searchTimes;

    /** 使用次数 */
    @ApiModelProperty(value = "使用次数")
    @Excel(name = "使用次数")
    @TableField("use_times")
    private Integer useTimes;

    /** 搜索密码 */
    @ApiModelProperty(value = "搜索密码")
    @Excel(name = "搜索密码")
    @TableField("search_password")
    private String searchPassword;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}