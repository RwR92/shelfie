package org.example.shelfie.shelf;

import jakarta.validation.constraints.NotNull;

public record MoveEntryRequest(@NotNull Long shelfId) {
}
