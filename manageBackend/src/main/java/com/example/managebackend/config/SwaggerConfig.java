package com.example.managebackend.config;


//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                // 通过.select()方法，配置扫描哪些API接口,RequestHandlerSelectors配置如何扫描接口
//                .select()
//                //any() // 扫描所有，项目中的所有接口都会被扫描到
//                //none() // 不扫描接口
//                // 通过方法上的注解扫描，如withMethodAnnotation(GetMapping.class)只扫描get请求
//                //withMethodAnnotation(final Class<? extends Annotation> annotation)
//                // 通过类上的注解扫描，如.withClassAnnotation(Controller.class)只扫描有controller注解的类中的接口
//                //withClassAnnotation(final Class<? extends Annotation> annotation)
//                //basePackage(final String basePackage) // 根据包路径扫描接口
////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))  //添加ApiOperiation注解的被扫描
//
//                .paths(PathSelectors.any())
//                .build();
//    }
//    private ApiInfo apiInfo() {
//        Contact contact = new Contact("加洛斯", "666", "xxxxx@qq.com");
//        return new ApiInfo(
//                "文档标题",
//                "文档标题的描述",
//                "版本1.0",
//                "urn:tos",
//                contact,
//                "Apache 2.0",
//                "http://www.apache.org/licenses/LICENSE-2.0",
//                new ArrayList());
//    }
//}



import com.example.managebackend.utils.SwaggerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return SwaggerUtils.createDefaultRestApi(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("测试系统用户模块")
                .description("用户模块api")
                .version("1.0")
                .build();
    }

}
