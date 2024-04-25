package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do filme: ");
        String movieTitle = scanner.nextLine();
        movieTitle = movieTitle.replace(" ", "%20");


        try {
            URL url = new URL("https://api.themoviedb.org/3/search/movie?query="+ movieTitle + "&language=pt-BR");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwNTFiMzc1MzEwMDIyNWZmYzI5N2Y2NGQxZjliNGZhYiIsInN1YiI6IjY1OGFmODM4ZTAzOWYxNTY0YWJlMTAwYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.MhXm046eUTXrCmeX6lwjc_NAkg74uyvX_KgIo7bbm8M");

            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while((line = bf.readLine()) != null){
                response.append(line);
            }

            bf.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.toString());

            JSONArray results = (JSONArray) json.get("results");

            for (Object result : results){
                JSONObject filme = (JSONObject) result;
                System.out.println("\n");
                System.out.println(filme.get("title"));
                System.out.println("\n");
                System.out.println(formatOverview((String) filme.get("overview")));
                System.out.println("\n");
            }
        } catch (Exception e){
            System.out.println("Erro: " + e);
        }
    }

    public static String formatOverview(String overview) {
        int maxLength = 150;
        StringBuilder formattedOverview = new StringBuilder();
        String[] words = overview.split(" ");

        int currentLength = 0;
        for (String word : words) {
            if (currentLength + word.length() > maxLength) {
                formattedOverview.append("\n");
                currentLength = 0;
            }
            formattedOverview.append(word).append(" ");
            currentLength += word.length() + 1;
        }
        return formattedOverview.toString();
    }
}
