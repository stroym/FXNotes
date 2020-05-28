package cz.stroym.fxnotes.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.stroym.fxnotes.model.Note;
import cz.stroym.fxnotes.model.Tag;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {

  private static final ObjectWriter WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();
  private static final ObjectReader READER = new ObjectMapper().reader();
  private static final ObjectMapper MAPPER = new ObjectMapper();

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
    //TODO check/catch if file is a valid json, doing it by extension is meh

    notes.addAll(MAPPER.convertValue(readMap.get("notes"), new TypeReference<>() {
    }));
    tags.addAll(MAPPER.convertValue(readMap.get("tags"), new TypeReference<>() {
    }));
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
    JSONObject writeJson = new JSONObject() {{
      put("notes", new ArrayList<>(notes));
      put("tags", new ArrayList<>(tags));
    }};

    WRITER.writeValue(file, writeJson.toMap());
  }

}
