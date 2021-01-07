package it.sparks.postinstagram.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "image")
@Data
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idImage", nullable = false)
    private Integer idImage;

    @ManyToOne
    @JoinColumn(name = "idPost")
    private Post post;

    @Column(name = "mimeType")
    private String mimeType;

    @Column(name = "originalNameFile")
    private String originalNameFile;

    @Column(name = "imageBinary")
    private byte[] imageBinary;

    @OneToMany(mappedBy = "image")
    private List<Tag> tags;

}
