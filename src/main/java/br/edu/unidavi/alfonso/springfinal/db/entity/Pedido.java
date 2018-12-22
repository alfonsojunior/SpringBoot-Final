package br.edu.unidavi.alfonso.springfinal.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
@RepositoryRestResource(exported = false)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 5962709645406006692L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long numero;

    @Valid
    @RestResource(exported = false)
    @OneToOne(cascade= {CascadeType.DETACH})
    @JoinColumn(name="pedido_cliente")
    private Cliente cliente;

    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCriacao;
    private double total;

    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name="pedido_item")
    private List<Item> itens;
}
