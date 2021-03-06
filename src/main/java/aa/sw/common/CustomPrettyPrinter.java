package aa.sw.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class CustomPrettyPrinter extends DefaultPrettyPrinter {

    public static ObjectWriter of(final ObjectMapper mapper) {
        requireNonNull(mapper);

        final CustomPrettyPrinter prettyPrinter = new CustomPrettyPrinter();
        prettyPrinter._spacesInObjectEntries = true;
        prettyPrinter._objectFieldValueSeparatorWithSpaces = ": ";
        prettyPrinter._arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        return mapper.writer().with(prettyPrinter);
    }

    private CustomPrettyPrinter() {
        super();
    }

    private CustomPrettyPrinter(final DefaultPrettyPrinter base) {
        super(base);
    }

    @Override
    public void writeEndObject(final JsonGenerator g, final int nrOfEntries) throws IOException {
        if (!_objectIndenter.isInline()) {
            --_nesting;
        }
        if (nrOfEntries > 0) {
            _objectIndenter.writeIndentation(g, _nesting);
        } else {
            g.writeRaw("");
        }
        g.writeRaw('}');
    }

    @Override
    public void writeEndArray(final JsonGenerator g, final int nrOfValues) throws IOException {
        if (!_arrayIndenter.isInline()) {
            --_nesting;
        }
        if (nrOfValues > 0) {
            _arrayIndenter.writeIndentation(g, _nesting);
        } else {
            g.writeRaw("");
        }
        g.writeRaw(']');
    }

    @Override
    public CustomPrettyPrinter createInstance() {
        if (getClass() != CustomPrettyPrinter.class) {
            throw new IllegalStateException("Failed `createInstance()`: " + getClass().getName()
                    + " does not override method; it has to");
        }
        return new CustomPrettyPrinter(this);
    }
}
