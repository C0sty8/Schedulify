package schedulify.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import schedulify.models.Calendar;
import schedulify.models.Date;
import schedulify.models.Event;
import schedulify.models.HourMinute;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Clasa pentru interactiunea cu Gemini
 */
public class GeminiService {

    /**
     * Metoda pentru obtinerea evenimentelor din JSON-ul generat de Gemini
     * @return Evenimentele
     */
    public static List<Event> getEvents() {
        List<Event> result = new ArrayList<>();

        try {
            Object obj = new JSONParser().parse(new FileReader("result.json"));
            JSONObject jo = (JSONObject) obj;

            JSONArray events = (JSONArray) jo.get("events");
            for (Object e : events) {
                Map<String,String> event = (Map) e;
                result.add(new Event(-1,new Date(event.get("eventDate")),
                        event.get("eventName"), new HourMinute(event.get("start")),
                        new HourMinute(event.get("end")), event.get("location")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Metoda pentru scrierea in result.json
     * @param text Noul continut al fisierului
     */
    public static void writeInJSON(String text) {
        try {
            Files.writeString(Path.of("result.json"), text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru generarea promptului pentru Gemini
     * @param calendar Calendarul din care se vor lua date pentru generarea promptului
     * @return Promptul generat
     */
    public static String makePrompt(Calendar calendar) {
        String prompt = "Locatia principala in care grupul cu numele: " + calendar.getName() + " isi desfasoara activitatiile/evenimentele este: " + calendar.getLocation() +
                " in intervalul calendaristic " + calendar.getStart() + " - " + calendar.getEnd() + "." +
                " Activitati pe care le desfasoara grupul: " + calendar.getSchedule() + "." +
                " Evenimente la care va participa grupul: " + calendar.getEvents() + "." +
                " Sugerează evenimente care ar putea fi de interes pentru grup, in zona si in intervalul calendaristic oferit." +
                "Returnează DOAR un array JSON cu formatul: " +
                "{\"events\":[{\"eventName\":\"nume (maxim 45 de caractere)\", \"eventDate\":\"YYYY-MM-DD\", \"start\":\"HH:mm\", \"end\":\"HH:mm\", \"location\":\"lat,long\"}]}";

        return prompt;
    }

    /**
     * Metoda pentru generarea raspunsului de la Gemini
     * @param prompt Promptul pentru care se va genera raspunsul
     * @return Raspunsul generat
     */
    public static String getResponse(String prompt) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentResponse response =
                client.models.generateContent("gemini-3-flash-preview", prompt, null);
        return response.text();
    }
}

