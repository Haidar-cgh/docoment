package cn.com.cgh.romantic.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Integer status = 1;
    private String clientId;
    private List<String> roles;
}