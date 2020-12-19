package org.forensics.griffeyeanalyze.json.insertrelativefilepath;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class App
{
    public static void main( String[] args ) throws IOException {
//        String born = LocalDate.of(1992, 3, 2).toString();
//        JsonObject json = Json.createObjectBuilder().add("name", "Geroge Wade")
//                .add("occupation", "wallstreetgoon")
//                .add("born", born).build();
//
//        String result = json.toString();
//
//        System.out.println("Created json data:" + result);


//        read in keys and output data into new file until md5 is found,
//        and store it in variable.  continue reading keys and outputting
//        data into new file until position of relativefilepath is located,
//        output relativefilepath to output file, and continue loop.




        var url = new URL("https://jsonplaceholder.typicode.com/posts");

        try (var in = url.openStream(); var parser = Json.createParser(in)) {

            // starting array
            parser.next();

            while (parser.hasNext()) {

                // starting object
                var event1 = parser.next();

                if (event1 == JsonParser.Event.START_OBJECT) {

                    while (parser.hasNext()) {

                        var event = parser.next();

                        if (event == JsonParser.Event.KEY_NAME) {

                            switch (parser.getString()) {

                                case "userId":
                                    parser.next();

                                    System.out.printf("User Id: %d%n", parser.getInt());
                                    break;

                                case "id":
                                    parser.next();

                                    System.out.printf("Post Id: %d%n", parser.getInt());
                                    break;

                                case "title":
                                    parser.next();

                                    System.out.printf("Post title: %s%n", parser.getString());
                                    break;

                                case "body":
                                    parser.next();

                                    System.out.printf("Post body: %s%n%n", parser.getString());
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
