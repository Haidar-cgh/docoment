package cn.com.cgh.romantic.pojo.resource;

import cn.com.cgh.romantic.em.GenderStatus;
import cn.com.cgh.romantic.em.UserStatus;
import cn.com.cgh.romantic.pojo.TbBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TbCfgUser extends TbBaseEntity implements UserDetails {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
    /**
     * 用户密码
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 用户状态
     */
    @Schema(description = "用户状态 0 : 正常，1锁定")
    @TableField(typeHandler = EnumTypeHandler.class)
    private UserStatus status;
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * 性别
     */
    @Schema(description = "性别：0男 1女")
    @TableField(typeHandler = EnumTypeHandler.class)
    private GenderStatus gender;
    /**
     * 角色列表
     */
    @TableField(exist = false,typeHandler = FastjsonTypeHandler.class)
    private List<TbCfgRole> tbCfgRoles;
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String clientId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.tbCfgRoles.stream().map(TbCfgRole::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(UserStatus.NORMAL);
    }
}