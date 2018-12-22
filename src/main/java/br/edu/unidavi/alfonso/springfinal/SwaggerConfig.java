package br.edu.unidavi.alfonso.springfinal;

import br.edu.unidavi.alfonso.springfinal.controller.PedidoRestController;
import br.edu.unidavi.alfonso.springfinal.db.repository.ClienteRepository;
import br.edu.unidavi.alfonso.springfinal.db.repository.PedidoRepository;
import br.edu.unidavi.alfonso.springfinal.db.repository.ProdutoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
//@ComponentScan(basePackageClasses = {PedidoRestController.class, ClienteRepository.class, ProdutoRepository.class})
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}