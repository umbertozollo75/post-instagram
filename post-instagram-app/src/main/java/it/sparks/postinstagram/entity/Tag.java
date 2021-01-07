package it.sparks.postinstagram.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tag")
@Data
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idTag", nullable = false)
    private Integer idTag;

    @ManyToOne
    @JoinColumn(name = "idImage")
    private Image image;

    @Column(name = "originalNameFile")
    private String originalNameFile;

    @Column(name = "tag")
    private String tag;

}
