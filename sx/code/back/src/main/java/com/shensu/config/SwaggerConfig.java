package com.shensu.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName SwaggerConfig.java
 * @Description Swagger配置
 * @createTime 2023年03月15日 13:38
 */
@EnableKnife4j
@EnableOpenApi
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.OAS_30)  // 3.0要选择OAS_30
                .apiInfo(apiInfo())
                //.enable(enable)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                .paths(PathSelectors.any())
                .build();
//        docket.alternateTypeRules(AlternateTypeRules.newMapRule(String.class, List.class));
//        docket.alternateTypeRules(AlternateTypeRules.newMapRule(List.class, Model.class));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .version("1.0")
                .build();
    }

}
