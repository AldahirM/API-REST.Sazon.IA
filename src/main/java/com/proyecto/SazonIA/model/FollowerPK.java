package com.proyecto.SazonIA.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FollowerPK implements Serializable {

    @NotNull(message = "User ID must not be null")
    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private User user;

    @NotNull(message = "Followed ID must not be null")
    @Min(value = 1, message = "Followed ID must be greater than or equal to 1")
    private User followed;

    public FollowerPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FollowerPK followerPK))
            return false;
        return user.getUser_id() == followerPK.user.getUser_id() && Objects.equals(followed, followerPK.followed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, followed);
    }

    public User getUser() {
        return user;
    }

    public User getFollowed() {
        return followed;
    }
}
