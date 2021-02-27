package it.sparks.postdb.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "image")
@NamedQueries({
        @NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i")
})
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

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
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

    public byte[] getImageBinary() {
        return imageBinary;
    }

    public void setImageBinary(byte[] imageBinary) {
        this.imageBinary = imageBinary;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
