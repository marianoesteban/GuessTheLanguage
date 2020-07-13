package marianoesteban.guessthelanguage.wikipedia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import marianoesteban.guessthelanguage.quiz.Question;

public class Wikipedia {

    private static final String API_BASE_URL = "https://%s.wikipedia.org/api/rest_v1/";
    private static final Map<String, String> LANGUAGES_BY_CODE = new HashMap<String, String>() {{
        put("en", "English");
        put("sv", "Swedish");
        put("de", "German");
        put("fr", "French");
        put("nl", "Dutch");
        put("ru", "Russian");
        put("it", "Italian");
        put("es", "Spanish");
        put("pl", "Polish");
        put("vi", "Vietnamese");
        put("ja", "Japanese");
        put("zh", "Chinese");
        put("ar", "Arabic");
        put("pt", "Portuguese");
        put("uk", "Ukrainian");
    }};

    public static String getRandomApiBaseUrl() {
        return String.format(API_BASE_URL, getRandomLanguageCode());
    }

    private static String getRandomLanguageCode() {
        List<String> languageCodes = new ArrayList<String>(LANGUAGES_BY_CODE.keySet());
        Random r = new Random();
        return languageCodes.get(r.nextInt(languageCodes.size()));
    }

    public static Question createQuestion(PageSummary pageSummary) {
        final int NUM_OPTIONS = 4;

        String correctAnswer = LANGUAGES_BY_CODE.get(pageSummary.getLang());

        List<String> options = new ArrayList<String>();
        options.add(correctAnswer);
        while (options.size() < NUM_OPTIONS) {
            String language = LANGUAGES_BY_CODE.get(getRandomLanguageCode());
            if (options.contains(language))
                continue;
            options.add(language);
        }

        Collections.shuffle(options);

        return new Question(pageSummary.getExtract(), options, correctAnswer);
    }
}
