package ru.murashov;

import static ru.murashov.Attributes.AMOUNT;
import static ru.murashov.Attributes.PATIENT;
import static ru.murashov.Attributes.TYPE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class InvoiceImporter implements Importer {

  private static final String NAME_PREFIX = "Dear ";
  private static final String AMOUNT_PREFIX = "Amount: ";

  @Override
  public Document importFile(File file) throws IOException {
    final TextFile textFile = new TextFile(file);

    textFile.addLineSuffix(NAME_PREFIX, PATIENT);
    textFile.addLineSuffix(AMOUNT_PREFIX, AMOUNT);

    final Map<String, String> attributes = textFile.getAttributes();
    attributes.put(TYPE, "invoice");
    return new Document(attributes);
  }
}
