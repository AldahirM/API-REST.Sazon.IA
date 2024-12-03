package com.proyecto.SazonIA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "commentrecipe")
@JsonIgnoreProperties({ "replies", "recipe", "user", "comment_time_stamp" })
public class CommentRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @JsonProperty("comment_id")
    private Integer comment_id;

    @NotNull(message = "Content must not be null")
    @NotBlank(message = "Content is mandatory or at least must contain one character")
    @Size(min = 1, max = 300, message = "The content must be between 1 and 300 characters")
    @Column(name = "content", nullable = false, length = 300)
    @JsonProperty("content")
    private String content;

    @NotNull(message = "Comment time stamp must not be null")
    @Column(name = "comment_time_stamp", nullable = false)
    @JsonProperty("comment_time_stamp")
    private String comment_time_stamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @OneToMany(mappedBy = "reply_id")
    private List<ReplyCommentRecipe> replies;

    public CommentRecipe() {
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_time_stamp() {
        return comment_time_stamp;
    }

    public void setComment_time_stamp(String comment_time_stamp) {
        this.comment_time_stamp = comment_time_stamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<ReplyCommentRecipe> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyCommentRecipe> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "CommentRecipe{" +
                "comment_id=" + comment_id +
                ", content='" + content + '\'' +
                ", time_stamp=" + comment_time_stamp +
                ", user=" + user +
                ", recipe=" + recipe +
                ", replies=" + replies +
                '}';
    }

}