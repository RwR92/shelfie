package org.example.shelfie.shelf;

import jakarta.persistence.*;
import org.example.shelfie.auth.AppUser;

@Entity
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShelfType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    protected Shelf() {}

    public Shelf(String name, ShelfType type, AppUser owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public AppUser getOwner() {
        return owner;
    }
    public ShelfType getType() {
        return type;
    }
}
