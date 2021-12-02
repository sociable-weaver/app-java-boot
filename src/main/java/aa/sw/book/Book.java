package aa.sw.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Book.BookBuilder.class)
public class Book {

    String title;
    String description;
    List<Chapter> chapters;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookBuilder {
        private final List<Chapter> chapters = new ArrayList<>();

        public BookBuilder chapter(final String title, final String description, final String path) {
            requireNonNull(title);
            requireNonNull(description);
            requireNonNull(path);

            return chapter(Chapter.builder().title(title).description(description).path(path).build());
        }

        public BookBuilder chapter(final Chapter chapter) {
            requireNonNull(chapter);

            chapters.add(chapter);
            return this;
        }

        public BookBuilder chapters(final List<Chapter> chapters) {
            requireNonNull(chapters);

            this.chapters.clear();
            this.chapters.addAll(chapters);
            return this;
        }

        public Book build() {
            /* TODO: Add null checks for the rest */
            return new Book(title, description, List.copyOf(chapters));
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = Chapter.ChapterBuilder.class)
    public static class Chapter {
        String title;
        String description;
        String path;

        @JsonPOJOBuilder(withPrefix = "")
        public static class ChapterBuilder {
        }
    }
}