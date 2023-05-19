package ru.murashov;

import static ru.murashov.Attributes.ADDRESS;
import static ru.murashov.Attributes.BODY;
import static ru.murashov.Attributes.PATIENT;
import static ru.murashov.Attributes.TYPE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LetterImporter implements Importer {

  private static final String NAME_PREFIX = "Dear ";

  @Override
  public Document importFile(File file) throws IOException {
    final TextFile textFile = new TextFile(file);

    textFile.addLineSuffix(NAME_PREFIX, PATIENT);

    final int lineNumber = textFile.addLines(2, String::isEmpty, ADDRESS);
    textFile.addLines(lineNumber + 1, (line) -> line.startsWith("regards,"), BODY);

    final Map<String, String> attributes = textFile.getAttributes();
    attributes.put(TYPE, "letter");
    return new Document(attributes);
  }
}
