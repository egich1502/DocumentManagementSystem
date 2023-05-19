package ru.murashov;

import static ru.murashov.Attributes.BODY;
import static ru.murashov.Attributes.PATIENT;
import static ru.murashov.Attributes.TYPE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ReportImporter implements Importer {

  private static final String NAME_PREFIX = "Patient: ";

  @Override
  public Document importFile(File file) throws IOException {
    final TextFile textFile = new TextFile(file);
    textFile.addLineSuffix(NAME_PREFIX, PATIENT);
    textFile.addLines(2, line -> false, BODY);

    textFile.addLineSuffix(NAME_PREFIX, PATIENT);

    final Map<String, String> attributes = textFile.getAttributes();
    attributes.put(TYPE, "report");
    return new Document(attributes);
  }
}
