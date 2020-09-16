package it.sparks.postinstagram.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the post database table.
 */
@Entity
@Table(name = "post")
@NamedQuery(name = "post.findAll", query = "SELECT p FROM Post p")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long idPost;

    @Column(length = 25)
    private String titolo;

    @Column
    private String descrizionePost;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;

    @Column
    private int album;

    // bi-directional one-to-many association to image
    @OneToMany(mappedBy = "post")
    private List<Image> images;

    public Post() {
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public int getAlbum() {
        return album;
    }

    public void setAlbum(int album) {
        this.album = album;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Post{" +
                "idPost=" + idPost +
                ", titolo='" + titolo + '\'' +
                ", descrizionePost='" + descrizionePost + '\'' +
                ", dateStart=" + dateStart +
                ", album=" + album +
                ", images=" + images +
                '}';
    }
}