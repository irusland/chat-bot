package game.calculator;


import game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Calculator implements Game, Serializable {
    private String cache;
    private int result;
    private boolean isOperand;
    private String operation;
    private final HashMap<String, Consumer<Integer>> operations = new HashMap<>();
    private boolean done;

    private final ArrayList<String> history;
    private StringBuilder sb;

    public Calculator() {
        operations.put("+", (Consumer<Integer> & Serializable)(Integer a) -> result += a);
        operations.put("-", (Consumer<Integer> & Serializable)(Integer a) -> result -= a);
        operations.put("*", (Consumer<Integer> & Serializable)(Integer a) -> result *= a);
        operations.put("/", (Consumer<Integer> & Serializable)(Integer a) -> result /= a);
        operations.put("%", (Consumer<Integer> & Serializable)(Integer a) -> result %= a);
        cache = "Введите число";
        isOperand = true;
        history = new ArrayList<>();
        sb = new StringBuilder();
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
        sb = new StringBuilder();
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
                sb.append(result);
            } else {
                sb.append(operation).append(number).append(")");
                sb.insert(0,"(");
                operations.get(operation).accept(number);
            }
            isOperand = false;
            cache = "Введите операцию";
            return cache;
        } else {
            operation = query;
            if (operation.equals("=")) {
                done = true;
                sb.append("=").append(result).append("\n");
                history.add(sb.toString());
                sb = new StringBuilder();
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
        StringBuilder res = new StringBuilder();
        res.append("Operation history\n");
        for (String s : history) {
            res.append(s);
        }
        return res.toString();
    }
}
