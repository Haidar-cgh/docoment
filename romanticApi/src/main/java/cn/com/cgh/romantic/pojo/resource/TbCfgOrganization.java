package cn.com.cgh.romantic.pojo.resource;

import cn.com.cgh.romantic.pojo.TbBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 组织表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "组织表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TbCfgOrganization extends TbBaseEntity {
    @Schema(description = "组织名称")
    private String name;
    @Schema(description = "组织描述")
    private String description;
    @Schema(description = "组织父节点")
    private Long parentId;
}