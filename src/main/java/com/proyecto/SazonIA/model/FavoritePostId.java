package com.proyecto.SazonIA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;


@Embeddable
public class FavoritePostId implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull(message = "Post ID must not be null")
    @Column(name = "post_id", nullable = false)
    private String postId; // Refiriéndose al UUID de MongoDB

    public FavoritePostId() {
    }

    public FavoritePostId(Integer userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePostId that = (FavoritePostId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
