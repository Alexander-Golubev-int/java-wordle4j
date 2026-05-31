package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import Exception.InputFileLoaderException;

public class WordleDictionaryLoader {

    private WordleDictionaryLoader() {
    }

    public static List<String> loadDictionaryWords(String nameOfFile) throws IOException, InputFileLoaderException {
        String word;
        final List<String> wordsList = new ArrayList<>();
        try (Reader reader = new FileReader(nameOfFile, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            word = bufferedReader.readLine();
            if (word != null) {
                do {
                    if (word.length() == 5) {
                        wordsList.add(replacer(word));
                    }
                } while ((word = bufferedReader.readLine()) != null);

                if (wordsList.isEmpty()) {
                    throw new InputFileLoaderException("В словаре нет корректных слов из 5 букв.");
                }
                return wordsList;

            } else {
                throw new InputFileLoaderException("Файл пуст.");
            }
        } catch (IOException | InputFileLoaderException e) {
            try (PrintWriter printWriter = new PrintWriter(new FileWriter("logLoader.txt", true))) {
                printWriter.write("================Ошибка====================\n");
                e.printStackTrace(printWriter);
                printWriter.write("================Конец======================\n");
                System.out.println(e.getMessage());

            } catch (IOException ex) {
                System.out.println("Не удалось записать лог:" + ex.getMessage());
            }
            throw e;
        }
    }

    public static String replacer(String word) {
        StringBuilder newWord = new StringBuilder(word);
        for (int i = 0; i < newWord.length(); ) {
            if (newWord.charAt(i) == 'Ё' || newWord.charAt(i) == 'ё') {
                newWord.replace(i, i + 1, "е");
            } else {
                i++;
            }
        }
        return newWord.toString().toLowerCase();
    }
}
