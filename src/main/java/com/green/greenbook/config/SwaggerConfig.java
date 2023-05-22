//package com.green.greenbook.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//            .select()
//            .apis(RequestHandlerSelectors.basePackage("com.green.greenbook"))
//            .paths(PathSelectors.any())
//            .build().apiInfo(apiInfo());
//    }
//
//    private static ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//            .title("Green Book")
//            .description("도서 리뷰 아카이브")
//            .version("1.0")
//            .build();
//    }
//
//}