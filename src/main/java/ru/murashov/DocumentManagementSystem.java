package ru.murashov;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DocumentManagementSystem {

  private final Map<String, Importer> extensionToImporter = new HashMap<>();
  private final List<Document> documents = new LinkedList<>();
  private final List<Document> documentView = Collections.unmodifiableList(documents);


  public DocumentManagementSystem() {
    extensionToImporter.put("letter", new LetterImporter());
    extensionToImporter.put("report", new ReportImporter());
    extensionToImporter.put("image", new ImageImporter());
    extensionToImporter.put("invoice", new InvoiceImporter());
  }

  public void importFile(String path) throws IOException {
    final File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException(path);
    }

    final int separatorIndex = path.lastIndexOf('.');
    if (separatorIndex != -1) {
      if (separatorIndex == path.length()) {
        throw new UnknownFileTypeException("No extension found for file: " + path);
      }
      final String extension = path.substring(separatorIndex + 1);
      final Importer importer = extensionToImporter.get(extension);
      if (importer == null) {
        throw new UnknownFileTypeException("For file: " + path);
      }
      final Document document = importer.importFile(file);
      documents.add(document);
    } else {
      throw new UnknownFileTypeException("No extension found for file: " + path);
    }
  }

  public List<Document> contents() {
    return documentView;
  }

  public List<Document> search(final String query) {
    return documents.stream().filter(Query.parse(query)).toList();
  }

}
