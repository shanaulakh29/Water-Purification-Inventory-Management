package Modal;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateAdapter implements the generic JsonSerializer and JsonDeserializer interface and overwrites the
 * deserialize and serialize methods. This class enables Gson to work with the LocalDate objects.
 */

//Got idea about how to implement generic jsonSerializer and json Deserializer interfaces and have these interfaces
// work with the Gson library from Dr. Brian Fraser posted video for this assignment
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {


    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
