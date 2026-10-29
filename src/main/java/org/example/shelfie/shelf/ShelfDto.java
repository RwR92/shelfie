package org.example.shelfie.shelf;

public record ShelfDto(Long id, String name, ShelfType type, int entryCount) {

    static ShelfDto from(Shelf shelf) {
        return new ShelfDto(shelf.getId(), shelf.getName(), shelf.getType(), shelf.getEntries().size());
    }
}
