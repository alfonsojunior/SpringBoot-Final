package br.edu.unidavi.alfonso.springfinal.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
public class Item implements Serializable {

    private static final long serialVersionUID = -2079469574874924468L;

    @Id
    @Valid
    @OneToOne(cascade= {CascadeType.DETACH})
    @JoinColumn(name="item_produto")
    private Produto produto;

    private int quantidade;
    private double total;
}
