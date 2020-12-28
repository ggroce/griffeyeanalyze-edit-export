package org.forensics.griffeyeanalyze.json.insertrelativefilepath;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class Main
{
    public static void main( String[] args ) throws IOException {
        JsonParserFactory factory = Json.createParserFactory(null);
        String md5 = null;
        File inputFile = null;

        if ((args.length > 0) && (args[0] != null) && (!args[0].isEmpty())) {
            String filePath = args[0];
            inputFile = new File(filePath).getAbsoluteFile();
            if (!inputFile.exists()) {
                System.out.println("Invalid filename and/or path");
                return;
            }
        } else {
            System.out.println("Must enter input file name or full path");
            return;
        }

        String outputFileStr = inputFile.getParentFile() + "\\" +
                inputFile.getName().replaceFirst("[.][^.]+$", "") + "_output.json";

        try (FileInputStream is = new FileInputStream(inputFile);
             FileWriter outputFile = new FileWriter((outputFileStr), StandardCharsets.UTF_8);
             JsonParser parser = factory.createParser(is, StandardCharsets.UTF_8)) {

            JsonParser.Event previousEvent = null;
            String previousKey = null;
            while (parser.hasNext()) {
                final JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        if (previousEvent == JsonParser.Event.END_OBJECT) {
                            outputFile.write(",");
                            outputFile.write("{");
                            System.out.println(",");
                            System.out.println("{");
                        } else {
                            outputFile.write("{");
                            System.out.println("{");
                        }
                        previousEvent = JsonParser.Event.START_OBJECT;
                        break;
                    case END_OBJECT:
                        outputFile.write("}");
                        System.out.print("}");
                        previousEvent = JsonParser.Event.END_OBJECT;
                        break;
                    case START_ARRAY:
                        outputFile.write("[");
                        System.out.println("[");
                        previousEvent = JsonParser.Event.START_ARRAY;
                        break;
                    case END_ARRAY:
                        outputFile.write("]");
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
                                outputFile.write("," + "\"" + key + "\"" + ":");
                                System.out.print("," + "\"" + key + "\"" + ":");
                                break;
                            default:
                                outputFile.write("\"" + key + "\"" + ":");
                                System.out.print("\"" + key + "\"" + ":");
                        }
                        previousEvent = JsonParser.Event.KEY_NAME;
                        previousKey = key;
                        break;
                    case VALUE_STRING:
                        String string = parser.getString().replace("\\","\\\\");
                        outputFile.write("\"" + string + "\"");
                        System.out.print("\"" + string + "\"");
                        previousEvent = JsonParser.Event.VALUE_STRING;
                        if (previousKey != null && previousKey.equals("MD5")) {
                            md5 = string;
                        }
                        if (previousKey != null && previousKey.equals("MediaSize")) {
                            outputFile.write(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                            System.out.print(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                        }
                        break;
                    case VALUE_NUMBER:
                        BigDecimal number = parser.getBigDecimal();
                        outputFile.write(number.toString());
                        System.out.print(number);
                        if (previousKey != null && previousKey.equals("MediaSize")) {
                            outputFile.write(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                            System.out.print(",\"RelativeFilePath\":\"Files\\\\" + md5 + "\"");
                        }
                        previousEvent = JsonParser.Event.VALUE_NUMBER;
                        break;
                    case VALUE_TRUE:
                        outputFile.write("true");
                        System.out.print(true);
                        previousEvent = JsonParser.Event.VALUE_TRUE;
                        break;
                    case VALUE_FALSE:
                        outputFile.write("false");
                        System.out.print(false);
                        previousEvent = JsonParser.Event.VALUE_FALSE;
                        break;
                    case VALUE_NULL:
                        outputFile.write("");
                        System.out.print("");
                        previousEvent = JsonParser.Event.VALUE_NULL;
                        break;
                }
            }
            outputFile.close();
            parser.close();
        }
    }
}
