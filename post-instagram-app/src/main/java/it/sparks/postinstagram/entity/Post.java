package it.sparks.postinstagram.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
@NamedQueries({
        @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
        @NamedQuery(name = "Post.findByIdPost", query = "SELECT p FROM Post p WHERE p.idPost = :idPost")
})
@Data
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPost", nullable = false)
    private Integer idPost;

    @Column(name = "titolo")
    private String titolo;

    @Column(name = "descrizionePost")
    private String descrizionePost;

    @Column(name = "dateStart", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateStart;

    @Column(name = "album")
    private Integer album;

    @Column(name = "interval")
    private Integer interval;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

}
