package net.rusnet.sb.learningprogram.dataprovider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rusnet.sb.learningprogram.models.Lecture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LearningProgramProvider {

    private final static String LECTURES_URL = "http://landsovet.ru/learning_program.json";
    private List<Lecture> mLectures;

    public LearningProgramProvider() {
        mLectures = new ArrayList<>();
    }

    public List<Lecture> downloadLectures() {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(LECTURES_URL);
            connection =
                    (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.connect();// Start the query
            int response = connection.getResponseCode();
            inputStream = connection.getInputStream();

            String contentAsString = convertInputToString(inputStream);

            Gson gson = new GsonBuilder().create();
            Lecture[] lecture = gson.fromJson(contentAsString, Lecture[].class);
            mLectures = Arrays.asList(lecture);
            return Arrays.asList(lecture);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String convertInputToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder result = new StringBuilder();
        for (String line; (line = bufferedReader.readLine()) != null; ) {
            result.append(line).append('\n');
        }
        return result.toString();
    }

    public List<Lecture> provideLectures() {
        return new ArrayList<>(mLectures);
    }

    public List<String> provideLectors() {
        Set<String> lectorSet = new HashSet<>();
        for (Lecture lecture : mLectures) {
            lectorSet.add(lecture.getLector());

        }
        return new ArrayList<>(lectorSet);
    }

    public List<Lecture> filterBy(String lectorName) {
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : mLectures) {
            if (lecture.getLector().equals(lectorName)) {
                result.add(lecture);
            }
        }
        return result;
    }
}
