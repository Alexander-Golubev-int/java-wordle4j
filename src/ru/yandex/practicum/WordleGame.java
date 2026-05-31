package ru.yandex.practicum;

import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;

import Exception.InputFileLoaderException;
import Exception.WordNotFoundInDictionary;

public class WordleGame {

    private String secretWord;
    private String answer;
    private Map<String, String> allAnswerFromUser;
    private int steps = 0;
    private WordleDictionary dictionary;


    public WordleGame(WordleDictionary dictionary) throws IOException, InputFileLoaderException {
        this.dictionary = dictionary;
        allAnswerFromUser = new LinkedHashMap<>();
    }

    public String checkAnswer(String answer) throws InputFileLoaderException, WordNotFoundInDictionary {
        if (answer.isEmpty()) {
            String helpWord = dictionary.getHelp(allAnswerFromUser);
            return "Подсказка: " + helpWord;
        } else {
            this.answer = WordleDictionaryLoader.replacer(answer);
            allAnswerFromUser.put(answer,dictionary.wordGuessingCheck(secretWord, answer));
            return dictionary.wordGuessingCheck(secretWord, answer);
        }
    }

    public int getSteps() {
        return steps;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void startGame() {
        secretWord = dictionary.getRandomWord();
    }

    public boolean checkWord(String word) {
        return dictionary.checkWordFromUser(word);
    }

    public Map<String, String> getAllAnswerFromUser() {
        return allAnswerFromUser;
    }

    public void setSteps() {
        steps++;
    }
}
