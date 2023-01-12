package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build()
			.apiInfo(this.apiInfo());
	}

	private ApiInfo apiInfo(){
		String version = "V0.1";
		return new ApiInfoBuilder()
			.title("Smilegate Winter-Dev Camp JIKJI Instagram-clone-project Auth Server")
			.description("설명")
			.version(version)
			.contact(new Contact("이름","홈페이지 URL","e-mail"))
			.build();
	}
}
