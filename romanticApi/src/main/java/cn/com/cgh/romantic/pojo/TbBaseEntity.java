package cn.com.cgh.romantic.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbBaseEntity implements Serializable {
    @TableId
    @Schema(description = "主键")
    private Long id;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建着")
    private String createBy;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新着")
    private String updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;
}