package br.edu.unidavi.alfonso.springfinal.db.repository;

import br.edu.unidavi.alfonso.springfinal.db.entity.Produto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "Produtos")
@RepositoryRestResource(exported = true, path = "produtos")
public interface ProdutoRepository extends CrudRepository<Produto, Long> {

    @Secured("ROLE_MANAGER")
    @ApiOperation("Retorna todos os produtos")
    @Override
    List<Produto> findAll();

    @Secured("ROLE_MANAGER")
    @ApiOperation("Retorna todos os produtos com uma data de criação")
    List<Produto> findAllByDataCriacao(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam("Data de Criação") @Param("criacao") Date data_criacao);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Retorna todos os produtos com a data de criação entre uma data inicial e uma data final")
    List<Produto> findAllByDataCriacaoAfterAndDataCriacaoBefore(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam("Data Inicial") @Param("data_inicio") Date data_inicio,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam("Data Final") @Param("data_fim")Date data_fim);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Busca de produto pelo id")
    Produto findById(
            @ApiParam("ID do Produto") @Param("id") Long id);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Busca de produto pelo nome")
    Produto findFirstByNome(
            @ApiParam("Nome do Produto") @Param("nome") String nome);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Busca de produtos pela marca")
    List<Produto> findByMarca(
            @ApiParam("Marca do Produto") @Param("marca") String marca);

    @RestResource(exported = false)
    public Optional<Produto> findProdutoById(@Param("id") Long id);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Salvar o produto")
    @Override
    Produto save(
            @ApiParam("Conteúdo do Produto") @Param("produto") Produto produto);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Eliminação do produto pelo id")
    @Override
    void delete(
            @ApiParam("ID do Produto") @Param("id") Long id);

}
