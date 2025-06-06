package de.staatsbibliothek.berlin.oxygen.excel.file;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.09.23
 */
class FileUtilsTest {

  private static final Logger logger = Logger.getLogger(FileUtilsTest.class.getName());

  public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

  @Test
  void listDirectory() throws IOException {
    String tmpdir = System.getProperty(JAVA_IO_TMPDIR);
    assertNotNull(tmpdir);
    List<String> listDirectory = FileUtils.listDirectory(tmpdir, "[0-9]+", true);
    logger.info(listDirectory.toString());

    assertFalse(listDirectory.isEmpty());

  }
}
