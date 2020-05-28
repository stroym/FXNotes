package cz.papp.fxnotes.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.papp.fxnotes.model.Note;
import cz.papp.fxnotes.model.Tag;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataUtils {

  private static final String[] TEXT_EXTENSIONS   = new String[]{".json", ".txt"};
  private static final String[] BINARY_EXTENSIONS = new String[]{".bin", ".dat"};

  private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();
  private static final ObjectReader READER = new ObjectMapper().reader();
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Checks if a filename contains an extension.
   *
   * @param file file to check
   * @return true if <code>file</code> has an extension
   */
  public static boolean doesFileHaveAnExtension(File file) {
    return !Objects.equals(FilenameUtils.getExtension(file.getName()), "");
  }

  /**
   * Loads application data from a json file.
   *
   * @param file  file to read
   * @param notes parsed notes target
   * @param tags  parsed tags target
   * @throws IOException thrown when something goes wrong when reading the <code>file</code>
   */
  public static void readJson(File file, List<Note> notes, List<Tag> tags) throws IOException {
    Map<String, List<?>> readMap = READER.readValue(file, HashMap.class);

    notes.addAll(MAPPER.convertValue(readMap.get("notes"), new TypeReference<>() {
    }));
    tags.addAll(MAPPER.convertValue(readMap.get("tags"), new TypeReference<>() {
    }));
  }

  /**
   * Loads application data from a binary file.
   *
   * @param file  file to read
   * @param notes parsed notes target
   * @param tags  parsed tags target
   * @throws IOException thrown when something goes wrong when reading the <code>file</code>
   */
  public static void readBinary(File file, List<Note> notes, List<Tag> tags) throws IOException {
    byte[] readBytes = READER.readValue(file, byte[].class);

    Map<String, List<?>> jsonObject = new HashMap(READER.readValue(readBytes, JSONObject.class).toMap());

    notes.addAll(MAPPER.convertValue(jsonObject.get("notes"), new TypeReference<>() {
    }));
    tags.addAll(MAPPER.convertValue(jsonObject.get("tags"), new TypeReference<>() {
    }));
  }

  /**
   * Crossroads for {@link cz.papp.fxnotes.controller.MainController#saveToCurrentFile() saveToCurrentFile}.
   * Automatically chooses which file save method to use depending on which file was last loaded.
   *
   * @param file  file to write to
   * @param notes parsed notes source
   * @param tags  parsed tags source
   * @throws IOException thrown when something goes wrong when writing to the <code>file</code>
   */
  public static void writeFile(File file, List<Note> notes, List<Tag> tags) throws IOException {
    String ext = FilenameUtils.getExtension(file.getName());

    if (Arrays.asList(TEXT_EXTENSIONS).contains(ext)) {
      writeJson(file, notes, tags);
    } else if (Arrays.asList(BINARY_EXTENSIONS).contains(ext)) {
      writeBinary(file, notes, tags);
    }
  }

  /**
   * Saves application data to a json file.
   *
   * @param file  file to write to
   * @param notes parsed notes source
   * @param tags  parsed tags source
   * @throws IOException thrown when something goes wrong when writing to the <code>file</code>
   */
  public static void writeJson(File file, List<Note> notes, List<Tag> tags) throws IOException {
    if (!doesFileHaveAnExtension(file)) {
      file = new File(file.getName() + ".json");
    }

    JSONObject writeJson = new JSONObject() {{
      put("notes", new ArrayList<>(notes));
      put("tags", new ArrayList<>(tags));
    }};

    WRITER.writeValue(file, writeJson.toMap());
  }

  /**
   * Saves application data to a binary file.
   *
   * @param file  file to write to
   * @param notes parsed notes source
   * @param tags  parsed tags source
   * @throws IOException thrown when something goes wrong when writing to the <code>file</code>
   */
  public static void writeBinary(File file, List<Note> notes, List<Tag> tags) throws IOException {
    if (!doesFileHaveAnExtension(file)) {
      file = new File(file.getName() + ".bin");
    }

    JSONObject writeJson = new JSONObject() {{
      put("notes", new ArrayList<>(notes));
      put("tags", new ArrayList<>(tags));
    }};

    WRITER.writeValue(file, WRITER.writeValueAsBytes(writeJson.toString()));
  }

}
