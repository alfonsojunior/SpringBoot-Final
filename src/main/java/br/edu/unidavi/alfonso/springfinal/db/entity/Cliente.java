package br.edu.unidavi.alfonso.springfinal.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1133921080120185613L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String nome;
    private String email;
    private String cpf;

    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;

    @OneToOne(cascade= {CascadeType.ALL})
    @JoinColumn(name="cliente_endereco")
    private Endereco endereco;
}
