package cn.com.cgh.romantic.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户组表
 */
@Data
@Schema(description = "用户组")
public class TbCfgGroup extends TbBaseEntity {
    @Schema(description = "名称")
    private String name;
}