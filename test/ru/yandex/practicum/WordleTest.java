package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Exception.InputFileLoaderException;
import Exception.WordNotFoundInDictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class WordleTest {
    //Проверка класса WordleDictionaryLoader
    @Test
    void testLoadDictionaryWords() throws IOException, InputFileLoaderException {
        //Загрузка корректного справочника и проверка его содержимого;
        List<String> wordsList = WordleDictionaryLoader.loadDictionaryWords("words_ru.txt");
        boolean sizeUpdatedList = !wordsList.isEmpty();
        Assertions.assertTrue(sizeUpdatedList);
    }

    @Test
    void testShouldThrowExceptionWithMessage() {
        //Вызов ошибки со справочником где нет подходящих слов для игры
        Exception exception = assertThrows(InputFileLoaderException.class, () ->
                WordleDictionaryLoader.loadDictionaryWords("only_6_words_ru.txt"));
        assertEquals("В словаре нет корректных слов из 5 букв.", exception.getMessage());
    }

    @Test
    void testShouldThrowExceptionWithMessageEmptyFile() {
        //Вызов ошибки со справочником, где справочник пуст
        Exception exception = assertThrows(InputFileLoaderException.class, () ->
                WordleDictionaryLoader.loadDictionaryWords("empty_file.txt"));
        assertEquals("Файл пуст.", exception.getMessage());
    }

    @Test
    void testShouldReplaceAllRussianEWithPointOnBasicE() throws InputFileLoaderException, IOException {
        //Проверка замены буквы "ё" в справочнике на "е"
        List<String> wordsList = WordleDictionaryLoader.loadDictionaryWords("e_with_point.txt");
        assertEquals("еееее", wordsList.getFirst());
    }

    @Test
    void testShouldReplaceAllRussianBigEWithPointOnBasicE() throws InputFileLoaderException, IOException {
        //Проверка замены буквы "Ё" в справочнике на "е"
        List<String> wordsList = WordleDictionaryLoader.loadDictionaryWords("big_e_with_point.txt");
        assertEquals("еееее", wordsList.getFirst());
    }

    //Проверка класса WordleDictionary
    @Test
    void testShouldGiveRandomWord() throws InputFileLoaderException, IOException {
        //Проверка метода который должен случайное слово и оно лежит в правильном диапазон 5 букв и верной буквой
        WordleDictionary wordleDictionary = new WordleDictionary("randomTest.txt");
        String randomWord = wordleDictionary.getRandomWord();
        assertEquals(5, randomWord.length());
        assertEquals('г', randomWord.charAt(0));
    }

    @Test
    void testShouldGiveCorrectAnswer() throws InputFileLoaderException, IOException {
        //Проверка метода, что игрок правильно отгадывает слово
        String answer = "+++++";
        WordleDictionary wordleDictionary = new WordleDictionary("only_1_word.txt");
        assertEquals(answer, wordleDictionary.wordGuessingCheck("гонец", "гонец"));
    }

    @Test
    void testShouldGiveHalfIncorrectAnswerAtUserAnswer() throws InputFileLoaderException, IOException {
        //Проверка метода, что игрок частично отгадывает слово
        String answer = "+^-^-";
        WordleDictionary wordleDictionary = new WordleDictionary("only_1_word.txt");
        assertEquals(answer, wordleDictionary.wordGuessingCheck("герой", "гонец"));
    }

    @Test
    void testShouldGiveIncorrectAnswerAtUserAnswer() throws InputFileLoaderException, IOException {
        //Проверка метода, что игрок полностью не отгадывает слово
        String answer = "-----";
        WordleDictionary wordleDictionary = new WordleDictionary("only_1_word.txt");
        assertEquals(answer, wordleDictionary.wordGuessingCheck("герой", "ааааа"));
    }

    @Test
    void testShouldGiveHelpUser() throws InputFileLoaderException, IOException, WordNotFoundInDictionary {
        //Проверка метода, что игроку дается случайная подсказка при пустом вводе
        Map<String, String> emptyMap = new LinkedHashMap<>();
        String answer = "гонец";
        WordleDictionary wordleDictionary = new WordleDictionary("only_1_word.txt");
        assertEquals(answer, wordleDictionary.getHelp(emptyMap));
    }

    @Test
    void testShouldNoGiveHelpUser() throws InputFileLoaderException, IOException{
        //Проверка метода, что игроку не дается никакая подсказка и выводится ошибка
        Map<String, String> map = new LinkedHashMap<>();
        map.put("крыса", "-----");
        WordleDictionary wordleDictionary = new WordleDictionary("only_1_word.txt");
        wordleDictionary.setLastHistorySize(1);
        wordleDictionary.setCurrentListWithHelp(new ArrayList<>());
        Exception exception = assertThrows(WordNotFoundInDictionary.class, () ->
                wordleDictionary.getHelp(map));
        assertEquals("Все подходящие слова уже были предложены.", exception.getMessage());
    }

}
