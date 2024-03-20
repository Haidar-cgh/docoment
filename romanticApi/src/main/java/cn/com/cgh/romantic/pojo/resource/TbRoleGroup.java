package cn.com.cgh.romantic.pojo.resource;

import cn.com.cgh.romantic.pojo.TbBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 职位表
 * @author cgh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色-用户组关系表")
@TableName("tb_role_group")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TbRoleGroup extends TbBaseEntity {
    @Schema(description = "用户角色id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
    @Schema(description = "用户组id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;
}
