package br.edu.unidavi.alfonso.springfinal.db.resource;

import br.edu.unidavi.alfonso.springfinal.db.entity.Pedido;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Pedido.class})
public class PedidoResource extends Resource<Pedido> {

    public PedidoResource() {
        this(new Pedido());
    }

    public PedidoResource(Pedido pedido, Link... links) {
        super(pedido, links);
    }

}
