package br.edu.unidavi.alfonso.springfinal.db.repository;

import br.edu.unidavi.alfonso.springfinal.db.entity.Pedido;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "Pedidos")
@RepositoryRestResource(exported = false, path = "pedidos")
public interface PedidoRepository extends CrudRepository<Pedido, Long> {

    @ApiOperation("Retorna todos os pedidos")
    @Override
    List<Pedido> findAll();

    @ApiOperation("Busca dos pedidos de uma data de criação")
    List<Pedido> findAllByDataCriacao(
            @ApiParam("Data de Criação") @Param("data_criacao")Date data_criacao);

    @ApiOperation(value = "Busca do pedido pelo número")
    Pedido findByNumero(
                    @ApiParam("Número do Pedido") @Param("numero") Long numero);

    @RestResource(exported = false)
    Optional<Pedido> findPedidoByNumero(@Param("numero") Long numero);

    @ApiOperation("Busca os pedidos de um cliente pelo id ou pelo nome")
    List<Pedido> findAllByCliente_IdOrCliente_Nome(
            @ApiParam("ID do Cliente") @Param("id") Long id,
            @ApiParam("Nome do Cliente") @Param("nome") String nome);

    @ApiOperation("Salvar o pedido")
    @Override
    Pedido save(
            @ApiParam("Conteúdo do Pedido") @Param("pedido") Pedido pedido);

    @ApiOperation("Eliminação do pedido pelo número")
    @Override
    void delete(
            @ApiParam("Número do Pedido") @Param("numero") Long id);

}
