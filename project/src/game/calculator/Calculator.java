package game.calculator;


import game.Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Consumer;

public class Calculator implements Game {
    private String cache;
    private int result;
    private boolean isOperand;
    private String operation;
    private final HashMap<String, Consumer<Integer>> operations = new HashMap<>();
    private boolean done;

    private Stat stat;

    public Calculator() {
        operations.put("+", (Consumer<Integer> & Serializable)(Integer a) -> result += a);
        operations.put("-", (Consumer<Integer> & Serializable)(Integer a) -> result -= a);
        operations.put("*", (Consumer<Integer> & Serializable)(Integer a) -> result *= a);
        operations.put("/", (Consumer<Integer> & Serializable)(Integer a) -> result /= a);
        operations.put("%", (Consumer<Integer> & Serializable)(Integer a) -> result %= a);
        cache = "Введите число";
        stat = new Stat();
        isOperand = true;
    }

    @Override
    public String load() {
        return cache;
    }

    @Override
    public String reset() {
        cache = "Введите число";
        operation = null;
        isOperand = true;
        done = false;
        result = 0;
        stat.saveToHistory();
        return "Game reset";
    }

    @Override
    public String request(String query) throws Exception {
        if (isOperand) {
            var number = 0;
            try {
                number = Integer.parseInt(query);
            } catch (Exception e) {
                cache = "Неправильный ввод";
                return cache;
            }
            if (operation == null) {
                result = number;
                stat.startAppend(result);
            } else {
                stat.appendWithBrackets(operation, number);
                operations.get(operation).accept(number);
            }
            isOperand = false;
            cache = "Введите операцию";
            return cache;
        } else {
            operation = query;
            if (operation.equals("=")) {
                done = true;
                stat.endAppend(result);
                stat.saveToHistory();
                cache = "➡ " + result;
                return cache;
            }
            if (!operations.containsKey(operation)) {
                cache = "Неправильная операция " + operation;
                return cache;
            }
            isOperand = true;
            cache = "Введите число";
            return cache;
        }
    }

    @Override
    public Boolean isFinished() {
        return done;
    }

    @Override
    public String getStatistics() {
        return stat.toStringStat();
    }
}
