package cn.com.cgh.romantic.pojo.resource;

import cn.com.cgh.romantic.em.DeleteStatus;
import cn.com.cgh.romantic.em.ResourceStatus;
import cn.com.cgh.romantic.pojo.TbBaseEntity;
import cn.com.cgh.romantic.pojo.resource.child.RouteMeta;
import cn.com.cgh.romantic.typeHandler.DefaultEnumTypeHandler;
import cn.com.cgh.romantic.typeHandler.MyArrayTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.BooleanTypeHandler;

import java.util.List;

/**
 * 菜单表
 *
 * @author cgh
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(description = "菜单表")
@TableName("tb_cfg_resource")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TbCfgResource extends TbBaseEntity {
    @Schema(description = "父菜单code")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @Schema(description = "菜单名称")
    private String name;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "path")
    private String path;
    @Schema(description = "资源类别 0 菜单 1 按钮 2 接口")
    @TableField(typeHandler = DefaultEnumTypeHandler.class)
    private ResourceStatus status;
    @Schema(description = "是否删除 1: 是，0：否")
    @TableField(typeHandler = BooleanTypeHandler.class)
    private DeleteStatus deleted;
    @Schema(description = "重定向 地址")
    private String redirect;
    @Schema(description = "路由别名")
    @TableField(typeHandler = MyArrayTypeHandler.class)
    private String[] alias;
    @Schema(description = "独享路由守卫 ")
    private String beforeEnter;
    @Schema(description = "离开该组件时被调用 ")
    private String beforeRouteLeave;
    @Schema(description = "获取跳转页面的地址")
    private String component;
    @Schema(description = "是否开启缓存页面")
    private Boolean keepAlive;
    @Schema(description = "路由元数据")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private RouteMeta meta;
    @Schema(description = "排序")
    private Integer sort;

    @TableField(exist = false)
    private List<TbCfgResource> children;
}
