package cn.com.utils;

import cn.com.utils.interfaceRun.YmlPropertySourceComponent;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Configuration
//@YmlPropertySourceComponent(value = {"classpath:auth.properties"})
@ConfigurationProperties(prefix = "data")
@Data
@Log4j
public class AuthFilterItemProperties {
    private Map<Object,Object> filterChainDefinitionMap;
    private String hashAlgorithmName;
    private int isSeparation;
    private String isSeparationDesc;
    @NotNull private String LOGIN;
    @NotNull private String INDEX;
    @NotNull private String PAGE404;
    @NotNull private String PAGE500;
    @NotNull private String PAGE302;
    private List<String> items;
    public void setIsSeparation(int isSeparation) {
        log.info(22222222);
        log.info(isSeparation);
        this.isSeparation = isSeparation;
        this.isSeparationDesc = isSeparationEnum.getDesc(isSeparation);
    }

    enum isSeparationEnum{
        isSeparation_0(0,"两者并存"),isSeparation_1(1,"Session的方式"),isSeparation_2(2,"无Session的方式");
        private int isSeparations;
        private String Desc;
        private isSeparationEnum(int isSeparation, String Desc){
            this.isSeparations = isSeparation;
            this.Desc = Desc;
        }

        public int getIsSeparation() {
            return isSeparations;
        }

        public void setIsSeparation(int isSeparation) {
            this.isSeparations = isSeparation;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public static String getDesc(int code) {
            return isSeparationEnum.valueOf("isSeparation_"+code).toString();
        }

        @Override
        public String toString() {
            return this.Desc;
        }
    }
}
