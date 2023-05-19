import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import ru.murashov.Document;
import ru.murashov.DocumentManagementSystem;
import ru.murashov.UnknownFileTypeException;

import static ru.murashov.Attributes.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class DocumentManagementSystemTest {

  private static final String RESOURCES =
      "src" + File.separator + "test" + File.separator + "resources" + File.separator;
  private static final String LETTER = RESOURCES + "patient.letter";
  private static final String REPORT = RESOURCES + "patient.report";
  private static final String XRAY = RESOURCES + "xray.jpg";
  private static final String INVOICE = RESOURCES + "patient.invoice";
  private static final String JOE_BLOGGS = "Joe Bloggs";

  private final DocumentManagementSystem system = new DocumentManagementSystem();

  @Test
  public void shouldImportFile() throws Exception {
    system.importFile(LETTER);

    final Document document = onlyDocument();

    assertAttributeEquals(document, PATH, LETTER);
  }

  @Test
  public void shouldImporterLetterAttributes() throws Exception {
    system.importFile(LETTER);

    final Document document = onlyDocument();

    assertAttributeEquals(document, PATIENT, JOE_BLOGGS);
    assertAttributeEquals(document, ADDRESS, """
        123 Fake Street
        Westminster
        London
        United Kingdom""");
    assertAttributeEquals(document, BODY, """
        We are writing to you to confirm the re-scheduling of your appointment
        with Dr. Avaj from 29th December 2016 to 5th January 2017.""");
  }

  @Test
  public void shouldImportReportAttributes() throws Exception {
    system.importFile(REPORT);
    assertIsReport(onlyDocument());
  }

  @Test
  public void shouldImportImageAttributes() throws Exception {
    system.importFile(XRAY);

    final Document document = onlyDocument();

    assertAttributeEquals(document, WIDTH, "320");
    assertAttributeEquals(document, HEIGHT, "179");
    assertTypeIs("image", document);
  }

  @Test
  public void shouldImportInvoiceAttributes() throws Exception {
    system.importFile(INVOICE);

    final Document document = onlyDocument();

    assertAttributeEquals(document, PATIENT, JOE_BLOGGS);
    assertAttributeEquals(document, AMOUNT, "$100");
    assertTypeIs("invoice", document);
  }

  @Test
  public void shouldBeAbleToSearchFilesByAttributes() throws Exception {
    system.importFile(LETTER);
    system.importFile(REPORT);
    system.importFile(XRAY);

    final List<Document> documents = system.search("patient:Joe,body:Diet Coke");
    assertThat(documents, hasSize(1));
    assertIsReport(documents.get(0));
  }

  @Test(expected = FileNotFoundException.class)
  public void shouldNotImportMissingFile() throws Exception {
    system.importFile("gobbledygook.txt");
  }

  @Test(expected = UnknownFileTypeException.class)
  public void shouldNotImportUnknownFile() throws Exception {
    system.importFile(RESOURCES + "unknown.txt");
  }

  private void assertIsReport(final Document document) {
    assertAttributeEquals(document, PATIENT, JOE_BLOGGS);
    assertAttributeEquals(document, BODY, """
        On 5th January 2017 I examined Joe's teeth.
        We discussed his switch from drinking Coke to Diet Coke.
        No new problems were noted with his teeth.""");
    assertTypeIs("report", document);
  }

  private Document onlyDocument() {
    final List<Document> documents = system.contents();
    assertThat(documents, hasSize(1));
    return documents.get(0);
  }

  private void assertTypeIs(final String type, final Document document) {
    assertAttributeEquals(document, TYPE, type);
  }

  private void assertAttributeEquals(final Document document, final String attributeName,
      final String expectedValue) {
    assertEquals("Document has the wrong value for " + attributeName, expectedValue,
        document.getAttribute(attributeName));
  }
}
