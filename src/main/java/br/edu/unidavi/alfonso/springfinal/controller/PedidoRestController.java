package br.edu.unidavi.alfonso.springfinal.controller;


import br.edu.unidavi.alfonso.springfinal.db.entity.Cliente;
import br.edu.unidavi.alfonso.springfinal.db.entity.Item;
import br.edu.unidavi.alfonso.springfinal.db.entity.Pedido;
import br.edu.unidavi.alfonso.springfinal.db.entity.Produto;
import br.edu.unidavi.alfonso.springfinal.db.repository.ClienteRepository;
import br.edu.unidavi.alfonso.springfinal.db.repository.PedidoRepository;
import br.edu.unidavi.alfonso.springfinal.db.repository.ProdutoRepository;
import br.edu.unidavi.alfonso.springfinal.db.resource.PedidoResource;
import br.edu.unidavi.alfonso.springfinal.db.resource.PedidoResourceAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "Pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoRepository repoPedido;
    @Autowired
    private ClienteRepository repoCliente;
    @Autowired
    private ProdutoRepository repoProduto;

    private PedidoResourceAssembler pedidoAssembler = new PedidoResourceAssembler();

    @Secured("ROLE_MANAGER")
    @ApiOperation("Lista de pedidos")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Pedido>> getAll() {
        return new ResponseEntity(pedidoAssembler.toResources(repoPedido.findAll()), HttpStatus.OK);
    }

    @Secured({"ROLE_MANAGER","ROLE_VENDEDOR"})
    @ApiOperation("Busca de pedido pelo número")
    @GetMapping(value = "/{numero}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PedidoResource> get(
            @ApiParam("Número do Pedido") @PathVariable(name = "numero") Long numero
    ) {
        Pedido pedido = repoPedido.findByNumero(numero);
        if (pedido != null) {
            return new ResponseEntity(pedidoAssembler.toResource(pedido), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({"ROLE_MANAGER","ROLE_VENDEDOR"})
    @ApiOperation("Busca de pedidos por data de criação")
    @GetMapping(value = "/criacao", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PedidoResource>> getPedidos(
            @ApiParam("Data de Criação (yyyy-MM-dd)") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "data_criacao") Date data_criacao
    ) {
        List<Pedido> pedidos = repoPedido.findAllByDataCriacao(data_criacao);
        if (pedidos.size() > 0) {
            return new ResponseEntity(pedidoAssembler.toResources(pedidos), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({"ROLE_MANAGER","ROLE_VENDEDOR"})
    @ApiOperation("Criação do pedido")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PedidoResource> save(
            @ApiParam("Dados do pedido") @RequestBody Pedido pedido
    ) {
        if (validatePedidoCliente(pedido)) {
            if (validatePedidoProdutos(pedido)) {
                Pedido pedidoSaved = repoPedido.save(pedido);
                pedidoSaved.setCliente(repoCliente.findById(pedidoSaved.getCliente().getId()));
                Long id = 0l;
                Produto prod = null;
                for (int pos = 0; pos < pedidoSaved.getItens().size(); pos++) {
                    id = pedidoSaved.getItens().get(pos).getProduto().getId();
                    prod = repoProduto.findById(id);
                    pedidoSaved.getItens().get(pos).setProduto(prod);
                }
                return new ResponseEntity(pedidoAssembler.toResource(pedidoSaved), HttpStatus.OK);
            } else {
                return new ResponseEntity("{\"mensagem\":\"Existe um produto não cadastrado no pedido enviado\"}", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("{\"mensagem\":\"O cliente informado no pedido não está cadastrado\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @Secured({"ROLE_MANAGER","ROLE_VENDEDOR"})
    @ApiOperation("Atualização do pedido")
    @PutMapping(value = "/{numero}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PedidoResource> put(
            @ApiParam(value = "Dados do pedido") @RequestBody Pedido pedido,
            @ApiParam(value = "Número do pedido") @PathVariable Long numero
    ) {
        Optional<Pedido> ped = repoPedido.findPedidoByNumero(numero);
        if (ped.isPresent()) {
            if (validatePedidoCliente(pedido)) {
                if (validatePedidoProdutos(pedido)) {
                    Pedido pedidoSaved = repoPedido.save(pedido);
                    return new ResponseEntity(pedidoAssembler.toResource(pedidoSaved), HttpStatus.OK);
                } else {
                    return new ResponseEntity("{\"mensagem\":\"Existe um produto não cadastrado no pedido enviado\"}", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("{\"mensagem\":\"O cliente informado no pedido não está cadastrado\"}", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("{\"mensagem\":\"O pedido informado não foi encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }

    @Secured({"ROLE_MANAGER"})
    @ApiOperation("Eliminação do pedido")
    @DeleteMapping(value = "/{numero}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PedidoResource> delete(
            @ApiParam("Número do pedido") @PathVariable Long numero
    ) {
        Optional<Pedido> pedido = repoPedido.findPedidoByNumero(numero);
        if (pedido.isPresent()) {
            repoPedido.delete(numero);
            return new ResponseEntity(pedidoAssembler.toResource(pedido.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity("{\"mensagem\":\"O pedido informado não foi encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }

    private boolean validatePedidoCliente(Pedido pedido) {
        Optional<Cliente> cliente = repoCliente.findClienteById(pedido.getCliente().getId());
        return cliente.isPresent();
    }

    private boolean validatePedidoProdutos(Pedido pedido) {
        boolean isProdutoOk = true;
        for (Item item : pedido.getItens()) {
            Optional<Produto> produto = repoProduto.findProdutoById(item.getProduto().getId());
            if (!produto.isPresent()) {
                isProdutoOk = false;
                break;
            }
        }
        return isProdutoOk;
    }

}
