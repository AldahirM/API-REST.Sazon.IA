package com.proyecto.SazonIA.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(FollowerPK.class)
public class Follower {

    @Id
    @NotNull(message = "User must not be null")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @NotNull(message = "Followed user must not be null")
    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "Follower [user=" + user + ", followed=" + followed + "]";
    }
}
