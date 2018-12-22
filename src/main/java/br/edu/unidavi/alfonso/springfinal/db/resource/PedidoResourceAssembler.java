package br.edu.unidavi.alfonso.springfinal.db.resource;

import br.edu.unidavi.alfonso.springfinal.controller.PedidoRestController;
import br.edu.unidavi.alfonso.springfinal.db.entity.Pedido;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PedidoResourceAssembler extends ResourceAssemblerSupport<Pedido, PedidoResource> {

    public PedidoResourceAssembler() {
        super(Pedido.class, PedidoResource.class);
    }

    public PedidoResource toResource(Pedido pedido) {
        return new PedidoResource(pedido,
                linkTo(methodOn(PedidoRestController.class).get(pedido.getNumero())).withSelfRel());
    }

}
