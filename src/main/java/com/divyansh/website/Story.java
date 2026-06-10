package com.divyansh.website;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private Long authorId;

    @Column(columnDefinition = "TEXT")
    private String mediaUrl; // Image URL ya text

    @Column(length = 500)
    private String text; // Story text

    private String bgColor; // Background color
    private String type; // "IMAGE" ya "TEXT"
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    // Getters Setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getAuthor(){return author;}
    public void setAuthor(String author){this.author=author;}
    public Long getAuthorId(){return authorId;}
    public void setAuthorId(Long authorId){this.authorId=authorId;}
    public String getMediaUrl(){return mediaUrl;}
    public void setMediaUrl(String mediaUrl){this.mediaUrl=mediaUrl;}
    public String getText(){return text;}
    public void setText(String text){this.text=text;}
    public String getBgColor(){return bgColor;}
    public void setBgColor(String bgColor){this.bgColor=bgColor;}
    public String getType(){return type;}
    public void setType(String type){this.type=type;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
    public LocalDateTime getExpiresAt(){return expiresAt;}
    public void setExpiresAt(LocalDateTime expiresAt){this.expiresAt=expiresAt;}
}
