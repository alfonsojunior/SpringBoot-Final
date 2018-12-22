package br.edu.unidavi.alfonso.springfinal.db.repository;

import br.edu.unidavi.alfonso.springfinal.db.entity.Cliente;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Optional;

@Api(tags = "Clientes")
@RepositoryRestResource(exported = true, path = "clientes")
public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    @Secured("ROLE_MANAGER")
    @ApiOperation("Retorna todos os clientes")
    @Override
    List<Cliente> findAll();

    @Secured("ROLE_MANAGER")
    @ApiOperation("Retorna todos os clientes de uma rua ou cidade ou estado")
    @Query("select cliente from Cliente cliente "
            + "where (:rua is null or cliente.endereco.rua like :rua) "
            + "and (:cidade is null or cliente.endereco.cidade = :cidade) "
            + "and (:estado is null or cliente.endereco.estado = :estado)")
    public List<Cliente> findByOptionalRuaOrOptionalCidadeOrOptionalEstado(
            @ApiParam("Rua") @Param("rua") String rua,
            @ApiParam("Cidade") @Param("cidade") String cidade,
            @ApiParam("Estado") @Param("estado") String estado
    );

    @Secured("ROLE_MANAGER")
    @ApiOperation("Busca de cliente pelo id")
    Cliente findById(
            @ApiParam("ID do Cliente") @Param("id") Long id);

    @RestResource(exported = false)
    Optional<Cliente> findClienteById(@Param("id") Long id);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Salvar o cliente")
    @Override
    Cliente save(
            @ApiParam("Conteúdo do Cliente") @Param("cliente") Cliente cliente);

    @Secured("ROLE_MANAGER")
    @ApiOperation("Eliminação do cliente pelo id")
    @Override
    void delete(
            @ApiParam("ID do Cliente") @Param("id") Long id);

}
