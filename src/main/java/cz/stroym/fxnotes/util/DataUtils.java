package cz.stroym.fxnotes.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.stroym.fxnotes.model.Note;
import cz.stroym.fxnotes.model.Notebook;
import cz.stroym.fxnotes.model.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {
  
  private static final ObjectWriter WRITER = new ObjectMapper().writer()
                                                               .withDefaultPrettyPrinter();
  private static final ObjectReader READER = new ObjectMapper().reader();
  private static final ObjectMapper MAPPER = new ObjectMapper();
  
  //TODO update
  public static void readJson(File file, List<Note> notes, List<Tag> tags) throws IOException {
    Map<String, List<?>> readMap = READER.readValue(file, HashMap.class);
    //TODO check/catch if file is a valid json, doing it by extension is meh
    
    notes.addAll(MAPPER.convertValue(readMap.get("notes"), new TypeReference<>() {}));
    tags.addAll(MAPPER.convertValue(readMap.get("tags"), new TypeReference<>() {}));
  }
  
  //TODO update, possibly custom de/serializer
  public static void writeJson(File file, Notebook notebook) throws IOException {
    //    JSONObject writeJson = new JSONObject() {{
    //      put("notes", new ArrayList<>(notes));
    //      put("tags", new ArrayList<>(tags));
    //    }};
    
    //    WRITER.writeValue(file, writeJson.toMap());
  }
  
}
