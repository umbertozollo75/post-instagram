package it.sparks.postdb.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tag")
@NamedQueries({
        @NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t")
})
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idTag", nullable = false)
    private Integer idTag;

    @ManyToOne
    @JoinColumn(name = "idImage")
    private Image image;

    @Column(name = "tag")
    private String tag;

    public Integer getIdTag() {
        return idTag;
    }

    public void setIdTag(Integer idTag) {
        this.idTag = idTag;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
