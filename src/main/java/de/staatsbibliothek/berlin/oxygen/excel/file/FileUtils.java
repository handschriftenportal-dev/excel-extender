package de.staatsbibliothek.berlin.oxygen.excel.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.09.23
 */
public class FileUtils {

  private static final Logger logger = Logger.getLogger(FileUtils.class.getName());

  public static List<String> listDirectory(String path, String regEx, boolean recursive) throws IOException {
    List<String> result = new ArrayList<>();
    Path dirToList = Paths.get(path);
    Pattern filePattern = createPatternFromString(regEx);

    DirectoryListingFileVisitor directoryListingFileVisitor = new DirectoryListingFileVisitor(recursive, filePattern,
        result);
    Files.walkFileTree(dirToList, directoryListingFileVisitor);

    return result;

  }

  private static Pattern createPatternFromString(String regEx) {
    Pattern filePattern = null;
    if (regEx != null && !regEx.isEmpty()) {
      try {
        filePattern = Pattern.compile(regEx);
      } catch (PatternSyntaxException psx) {
        logger.log(Level.SEVERE, psx.getMessage(), psx);
      }
    }
    return filePattern;
  }

  private static class DirectoryListingFileVisitor extends SimpleFileVisitor<Path> {

    private final boolean recursive;
    private final Pattern filePattern;
    private final List<String> result;

    DirectoryListingFileVisitor(boolean recursive, Pattern filePattern, List<String> result) {
      this.recursive = recursive;
      this.filePattern = filePattern;
      this.result = result;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      if (recursive) {
        return FileVisitResult.CONTINUE;
      }
      return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException io) {
      logger.info(MessageFormat.format("{0} : {1}", io.getClass().getSimpleName(), io.getMessage()));
      return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      if (filePattern == null || filePattern.matcher(file.getFileName().toString()).find()) {
        result.add(file.toAbsolutePath().toString());
      }
      return FileVisitResult.CONTINUE;
    }


    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if ((o == null) || (!getClass().equals(o.getClass()))) {
        return false;
      }
      DirectoryListingFileVisitor that = (DirectoryListingFileVisitor) o;
      return (recursive == that.recursive) && Objects.equals(this.filePattern, that.filePattern)
          && Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.recursive, this.filePattern, this.result);
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("DirectoryListingFileVisitor{");
      sb.append("recursive=").append(recursive);
      sb.append(", filePattern=").append(filePattern);
      sb.append(", result=").append(result);
      sb.append('}');
      return sb.toString();
    }
  }
}
