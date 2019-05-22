package mx.budgie.security.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@ComponentScan(basePackages={"mx.budgie"})
public class BudgieSecuritySSOApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgieSecuritySSOApplication.class, args);
	}
	
	@Configuration
    public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/login").setViewName("login.html");
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }
}
