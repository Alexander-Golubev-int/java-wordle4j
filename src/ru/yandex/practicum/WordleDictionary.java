package ru.yandex.practicum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Exception.InputFileLoaderException;
import Exception.WordNotFoundInDictionary;

public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();
    private List<String> currentListWithHelp;

    public void setLastHistorySize(int lastHistorySize) {
        this.lastHistorySize = lastHistorySize;
    }

    private int lastHistorySize = 0;


    public WordleDictionary(String fileName) throws IOException, InputFileLoaderException {
        words = WordleDictionaryLoader.loadDictionaryWords(fileName);
        if (words.isEmpty()) {
            throw new InputFileLoaderException("Словарь пуст.");
        }
    }

    public String getRandomWord() {
        return words.get(random.nextInt(0, words.size()));
    }

    public String wordGuessingCheck(String secretWord, String answer) {
        if (secretWord.equals(answer)) {
            return "+++++";
        } else {
            return comparisonOfWords(secretWord, answer);
        }
    }

    private String comparisonOfWords(String secretWord, String answer) {
            char[] secretWordChars = secretWord.toCharArray();
            char[] answerChars = answer.toCharArray();
            char[] result = new char[5];

            for (int i = 0; i < 5; i++) {
                if (secretWordChars[i] == answerChars[i]) {
                    result[i] = '+';
                    secretWordChars[i] = '*';
                    answerChars[i] = '#';
                }
            }
            for (int i = 0; i < 5; i++) {
                if (answerChars[i] == '#') {
                    continue;
                }
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (secretWordChars[j] == answerChars[i]) {
                        found = true;
                        result[i] = '^';
                        secretWordChars[j] = '*';
                        break;
                    }
                }
                if (!found) {
                    result[i] = '-';
                }
            }
            return new String(result);
    }

    public String getHelp(Map<String, String> helpForUser) throws WordNotFoundInDictionary {

        if (helpForUser.isEmpty()) {
            return getRandomWord();
        }

        if (currentListWithHelp == null || helpForUser.size() > lastHistorySize) {
            List<String> listWithHelps = new ArrayList<>(words);
            for (Map.Entry<String, String> entry : helpForUser.entrySet()) {
                String word = entry.getKey();
                String value = entry.getValue();

                List<String> tempList = new ArrayList<>();
                for (String wordFromListWords : listWithHelps) {
                    if (comparisonOfWords(wordFromListWords, word).equals(value)) {
                        tempList.add(wordFromListWords);
                    }
                }

                listWithHelps = tempList;

                if (listWithHelps.isEmpty()) {
                    throw new WordNotFoundInDictionary("Нет подходящих слов.");
                }
            }
            currentListWithHelp = listWithHelps;
            lastHistorySize = helpForUser.size();

        }

        if (currentListWithHelp.isEmpty()) {
            throw new WordNotFoundInDictionary("Все подходящие слова уже были предложены.");
        }
        String helpWord = currentListWithHelp.get(random.nextInt(currentListWithHelp.size()));
        currentListWithHelp.remove(helpWord);
        return helpWord;
    }

    public boolean checkWordFromUser(String userWord) {
        return userWord.length() == 5;
    }

    public void setCurrentListWithHelp(List<String> currentListWithHelp) {
        this.currentListWithHelp = currentListWithHelp;
    }
}





