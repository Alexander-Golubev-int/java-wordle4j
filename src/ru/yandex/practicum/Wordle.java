package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import Exception.InputFileLoaderException;
import Exception.WordNotFoundInDictionary;

public class Wordle {
    private static final Scanner scanner = new Scanner(System.in);
    private static WordleGame wordleGame;

    public static void main(String[] args) {

        try {
            WordleDictionary wordleDictionary = new WordleDictionary("words_ru.txt");
            wordleGame = new WordleGame(wordleDictionary);
            printMenu();
            wordleGame.startGame();
            String userAnswer = scanner.nextLine();
            while (true) {
                if (wordleGame.checkWord(userAnswer) || userAnswer.isEmpty()) {
                    try {
                        if (wordleGame.checkAnswer(userAnswer).equals("+++++")) {
                            System.out.println("Вы угадали слово! Поздравляем!");
                            wordleGame.setSteps();
                            printStatistic();
                            break;
                        } else {
                            System.out.println(wordleGame.checkAnswer(userAnswer));
                            wordleGame.setSteps();
                            if (wordleGame.getSteps() == 6) {
                                System.out.println("Попытки пройти игру закончились.");
                                printStatistic();
                                break;
                            }
                            userAnswer = scanner.nextLine();
                        }
                    } catch (WordNotFoundInDictionary e) {
                        try (PrintWriter printWriter = new PrintWriter(new FileWriter("log.txt", true))) {
                            printWriter.write("================Ошибка====================\n");
                            e.printStackTrace(printWriter);
                            printWriter.write("================Конец======================\n");
                            System.out.println(e.getMessage());
                        } catch (IOException ex) {
                            System.out.println("Не удалось записать лог:" + ex.getMessage());
                        }
                        System.out.println("Введите ваше слово: ");
                        userAnswer = scanner.nextLine();
                    }
                } else {
                    System.out.println("Необходимо ввести слово длинной равной 5 буквам");
                    userAnswer = scanner.nextLine();
                }
            }

        } catch (InputFileLoaderException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printMenu() {
        System.out.println("Вы попали в демо-игру <Wordle> на языке Java. Компьютер загадал слово, теперь вам " +
                "необходимо отгадать слово.");
        System.out.println("Правила просты при вводе слова появляются символы где:\n" +
                "- — им отмечается буква, которой НЕТ в загаданном слове\n" +
                "+ — этим символом отмечается буква, которая ЕСТЬ в загаданном слове и находится на правильной позиции\n" +
                "^ — так отмечается буква, которая ЕСТЬ в загаданном слове, но находится в другом месте.\n" +
                "Ввод пустого пробела или просто нажатия ENTER выводит слово-подсказку. " +
                "Необходимо ввести строчку длинной 5 символов и компьютер покажет " +
                "правильность введенных символов.");
        System.out.println("Введите ваше слово: ");
    }

    public static void printStatistic() {
        System.out.println("Загаданное слово: " + wordleGame.getSecretWord());
        System.out.println("Количество затраченных шагов: " + wordleGame.getSteps());
        System.out.println("Ваши ответы слова: " + wordleGame.getAllAnswerFromUser().entrySet());
    }
}
