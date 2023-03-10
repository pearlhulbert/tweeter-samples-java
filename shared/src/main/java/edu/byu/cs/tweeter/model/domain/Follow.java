package edu.byu.cs.tweeter.model.domain;


import java.io.Serializable;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * Represents a follow relationship.
 */
public class Follow implements Serializable {
    /**
     * The user doing the following.
     */
    public edu.byu.cs.tweeter.model.domain.User follower;
    /**
     * The user being followed.
     */
    public edu.byu.cs.tweeter.model.domain.User followee;

    public Follow() {
    }

    public Follow(edu.byu.cs.tweeter.model.domain.User follower, edu.byu.cs.tweeter.model.domain.User followee) {
        this.follower = follower;
        this.followee = followee;
    }

    public edu.byu.cs.tweeter.model.domain.User getFollower() {
        return follower;
    }

    public User getFollowee() {
        return followee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow that = (Follow) o;
        return follower.equals(that.follower) &&
                followee.equals(that.followee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followee);
    }

    @Override
    public String toString() {
        return "Follow{" +
                "follower=" + follower.getAlias() +
                ", followee=" + followee.getAlias() +
                '}';
    }
}
