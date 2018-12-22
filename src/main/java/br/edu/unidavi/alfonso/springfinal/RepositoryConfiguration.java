package br.edu.unidavi.alfonso.springfinal;

import br.edu.unidavi.alfonso.springfinal.db.entity.Cliente;
import br.edu.unidavi.alfonso.springfinal.db.entity.Item;
import br.edu.unidavi.alfonso.springfinal.db.entity.Pedido;
import br.edu.unidavi.alfonso.springfinal.db.entity.Produto;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Cliente.class);
        config.exposeIdsFor(Produto.class);
        config.exposeIdsFor(Pedido.class);
        config.exposeIdsFor(Item.class);
    }
}
