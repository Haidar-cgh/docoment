package cn.com.cgh.romantic.pojo.resource;

import cn.com.cgh.romantic.pojo.TbBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 组织表
 * @author cgh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "组织表")
@TableName("tb_cfg_organization")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TbCfgOrganization extends TbBaseEntity {
    @Schema(description = "组织名称")
    private String name;
    @Schema(description = "组织描述")
    private String description;
    @Schema(description = "组织父节点")
    private Long parentId;

    @TableField(exist = false)
    private List<TbCfgOrganization> children;
}
