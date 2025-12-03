package com.example.ApiLetter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "watchlists")
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "watchlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchlistChange> changes;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "active")
    private Boolean active = true; // Por padrão, watchlist é ativa

    public Watchlist() {
        this.lastUpdate = LocalDateTime.now();
        this.active = true;
    }

    public Watchlist(String name, User user) {
        this.name = name;
        this.user = user;
        this.lastUpdate = LocalDateTime.now();
        this.active = true;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WatchlistChange> getChanges() { return changes; }
    public void setChanges(List<WatchlistChange> changes) { this.changes = changes; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
