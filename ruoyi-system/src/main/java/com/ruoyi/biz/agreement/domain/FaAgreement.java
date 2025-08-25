package com.ruoyi.biz.agreement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 关闭通道协议
 *
 * @author ruoyi
 * @date 2024-01-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_agreement")
@ApiModel(value = "关闭通道协议")
public class FaAgreement extends BaseEntity {

private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @Excel(name = "内容")
    @TableField("content")
    private String content;

    /** 关于我们 */
    @ApiModelProperty(value = "关于我们")
    @Excel(name = "关于我们")
    @TableField("about_content")
    private String aboutContent;

    /** 登录协议 */
    @ApiModelProperty(value = "登录协议")
    @Excel(name = "登录协议")
    @TableField("login_content")
    private String loginContent;

    /** 关闭通道说明 */
    @ApiModelProperty(value = "关闭通道说明")
    @Excel(name = "关闭通道说明")
    @TableField("guanbi_content")
    private String guanbiContent;

    /** 免责声明 */
    @ApiModelProperty(value = "免责声明")
    @Excel(name = "免责声明")
    @TableField("mzsm_content")
    private String mzsmContent;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}