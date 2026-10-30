package org.example.shelfie.shelf;

import jakarta.validation.constraints.NotNull;

public record AddEntryRequest(@NotNull Long bookId) {
}
