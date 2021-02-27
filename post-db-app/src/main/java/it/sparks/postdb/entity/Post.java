package it.sparks.postdb.entity;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images;

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizionePost() {
        return descrizionePost;
    }

    public void setDescrizionePost(String descrizionePost) {
        this.descrizionePost = descrizionePost;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public Integer getAlbum() {
        return album;
    }

    public void setAlbum(Integer album) {
        this.album = album;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
