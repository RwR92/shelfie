package org.example.shelfie.book;

public record OpenLibraryBookDto(
        String olKey,
        String title,
        String author,
        Integer publishYear,
        String coverUrl
) {
    private static final String WORK_PREFIX = "/works/";
    private static final String COVER_URL = "https://covers.openlibrary.org/b/id/%d-M.jpg";

    static OpenLibraryBookDto from(OpenLibraryDoc doc) {
        String olKey = doc.key().startsWith(WORK_PREFIX)
                ? doc.key().substring(WORK_PREFIX.length())
                : doc.key();

        String author = doc.authorNames() == null || doc.authorNames().isEmpty()
                ? "Unknown author"
                : String.join(", ", doc.authorNames());

        String coverUrl = doc.coverId() == null ? null : COVER_URL.formatted(doc.coverId());

        return new OpenLibraryBookDto(olKey, doc.title(), author, doc.firstPublishYear(), coverUrl);
    }
}
