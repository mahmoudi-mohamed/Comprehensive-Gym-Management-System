package com.project.bodify.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.google.api.client.util.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Explicitly specify the join column to avoid repeated column issue
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private List<Files> media;
private LocalDateTime publish_date=LocalDateTime.now();
    private String description;
    private String status = "ACTIVE";
    private String urlvideo;
    private Long authorId;
    private String post_type="TEXT";
    private String urlimage;
    @ElementCollection
    @CollectionTable(name = "media_views", joinColumns = @JoinColumn(name = "media_id"))
    @Column(name = "view_id")
    private List<Long> viewsliste = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "media_likes", joinColumns = @JoinColumn(name = "media_id"))
    @Column(name = "like_id")
    private List<Long> liksliste = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "media_tags", joinColumns = @JoinColumn(name = "media_id"))
    @Column(name = "tag_id")
    private List<Long> TagsList = new ArrayList<>();

    public Media(@NonNull List<Files> media, String description) {
        this.media = media;
        this.description = description;
    }

    public Media(List<Files> media, String description, @NonNull String urlvideo) {
        this.media = media;
        this.description = description;
        this.urlvideo = urlvideo;
    }

    public Media(List<Files> media, String description, Long authourId, List<Long> tagsList) {
        this.media = media;
        this.description = description;
        this.authorId = authourId;
        this.TagsList = tagsList;
        
    }

	public Media(String description, Long authorId) {
		super();
		this.description = description;
		this.authorId = authorId;
	}

	public Media(String description, Long authorId, String post_type, String urlimage) {
		super();
		this.description = description;
		this.authorId = authorId;
		this.post_type = post_type;
		this.urlimage = urlimage;
	}
}
