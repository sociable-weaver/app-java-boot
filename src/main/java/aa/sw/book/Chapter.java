package aa.sw.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Value
@Builder
@JsonDeserialize(builder = Chapter.ChapterBuilder.class)
public class Chapter {

    /* TODO: Should we have the title and description here too? */
    List<Entry> entries;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ChapterBuilder {

        private final List<Entry> entries = new ArrayList<>();

        public ChapterBuilder entry(final Entry entry) {
            requireNonNull(entry);

            entries.add(entry);
            return this;
        }

        public ChapterBuilder entries(final List<Entry> entries) {
            requireNonNull(entries);

            this.entries.clear();
            this.entries.addAll(entries);
            return this;
        }

        public Chapter build() {
            return new Chapter(List.copyOf(entries));
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = Entry.EntryBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Entry {

        String type;
        UUID id;
        String name;
        String workingDirectory;
        List<String> parameters;
        List<String> variables;
        Map<String, String> values;
        Boolean ignoreErrors;
        Boolean pushChanges;
        Boolean dryRun;
        Boolean visible;
        Boolean sensitive;
        Integer expectedExitValue;
        Duration commandTimeout;

        @JsonPOJOBuilder(withPrefix = "")
        public static class EntryBuilder { }
    }
}