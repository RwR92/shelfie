package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findByOwner(AppUser owner);
}