package it.sparks.postinstagram.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the image database table.
 */
@Entity
@Table(name = "image")
@NamedQuery(name = "image.findAll", query = "SELECT i FROM Image i")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long idImage;

    // bi-directional many-to-one association to Ente
    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    @Column(length = 255)
    private String mimeType;

    @Column(length = 255)
    private String originalNameFile;

    public Image() {
    }

    public long getIdImage() {
        return idImage;
    }

    public void setIdImage(long idImage) {
        this.idImage = idImage;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getOriginalNameFile() {
        return originalNameFile;
    }

    public void setOriginalNameFile(String originalNameFile) {
        this.originalNameFile = originalNameFile;
    }

    @Override
    public String toString() {
        return "Image{" +
                "idImage=" + idImage +
                ", post=" + post +
                ", mimeType='" + mimeType + '\'' +
                ", originalNameFile='" + originalNameFile + '\'' +
                '}';
    }
}