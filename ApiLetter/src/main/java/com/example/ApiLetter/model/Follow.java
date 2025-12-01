package com.example.ApiLetter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;

    public Follow() {}

    public Follow(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }

    public User getFollowed() { return followed; }
    public void setFollowed(User followed) { this.followed = followed; }
}
