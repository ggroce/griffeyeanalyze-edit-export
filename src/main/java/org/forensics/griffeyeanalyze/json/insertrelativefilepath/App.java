package org.forensics.griffeyeanalyze.json.insertrelativefilepath;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class App
{
    public static void main( String[] args ) throws IOException {
//        URL url = new URL("https://jsonplaceholder.typicode.com/users");
        var factory = Json.createParserFactory(null);
        String md5 = null;

//        try (InputStream in = url.openStream(); JsonParser parser = Json.createParser(in)) {
        try (FileInputStream is = new FileInputStream("C:\\report\\export.json");
             JsonParser parser = factory.createParser(is, StandardCharsets.UTF_8)) {

            JsonParser.Event previousEvent = null;
            String previousKey = null;
            while (parser.hasNext()) {
                final JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        if (previousEvent == JsonParser.Event.END_OBJECT) {
                            System.out.println(",");
                            System.out.println("{");
                        } else {
                            System.out.println("{");
                        }
                        previousEvent = JsonParser.Event.START_OBJECT;
                        break;
                    case END_OBJECT:
                        System.out.print("}");
                        previousEvent = JsonParser.Event.END_OBJECT;
                        break;
                    case START_ARRAY:
                        System.out.println("[");
                        previousEvent = JsonParser.Event.START_ARRAY;
                        break;
                    case END_ARRAY:
                        System.out.println("]");
                        previousEvent = JsonParser.Event.END_ARRAY;
                        break;
                    case KEY_NAME:
                        String key = parser.getString();
                        assert previousEvent != null;
                        switch(previousEvent) {
                            case END_ARRAY:
                            case VALUE_STRING:
                            case VALUE_NUMBER:
                            case VALUE_TRUE:
                            case VALUE_FALSE:
                            case VALUE_NULL:
                                System.out.print("," + "\"" + key + "\"" + ":");
                                break;
                            default:
                                System.out.print("\"" + key + "\"" + ":");
                        }
                        previousEvent = JsonParser.Event.KEY_NAME;
                        previousKey = key;
                        break;
                    case VALUE_STRING:
                        String string = parser.getString();
                        System.out.print("\"" + string + "\"");
                        previousEvent = JsonParser.Event.VALUE_STRING;
                        if (previousKey != null && previousKey.equals("MD5")) {
                            md5 = string;
                        }
                        if (previousKey != null && previousKey.equals("MediaSize")) {
                            System.out.print(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                        }
                        break;
                    case VALUE_NUMBER:
                        BigDecimal number = parser.getBigDecimal();
                        System.out.print(number);
                        if (previousKey != null && previousKey.equals("MediaSize")) {
                            System.out.print(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                        }
                        previousEvent = JsonParser.Event.VALUE_NUMBER;
                        break;
                    case VALUE_TRUE:
                        System.out.print(true);
                        previousEvent = JsonParser.Event.VALUE_TRUE;
                        break;
                    case VALUE_FALSE:
                        System.out.print(false);
                        previousEvent = JsonParser.Event.VALUE_FALSE;
                        break;
                    case VALUE_NULL:
                        System.out.print("");
                        previousEvent = JsonParser.Event.VALUE_NULL;
                        break;
                }
            }
            parser.close();
        }
    }
}
