package bot;

import org.checkerframework.checker.units.qual.A;
import bot.Pair;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Database {
    private Connection db;
    private String table;
    public Database(String table, ArrayList<Pair<String, String>> typedNames) throws Exception {

        this.table = table;
        db = connect();
        if (db == null)
            throw new Exception("Cannot connect to db");

        ArrayList<String> columns = getTypedColumns(typedNames);

        String cols = String.join(",", columns);
        Statement st = null;
        st = db.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                " (id INT AUTO_INCREMENT PRIMARY KEY, person VARCHAR, " + cols + ")";
        ResultSet cont = st.executeQuery("show columns from "+ table);
//        while (cont.next()) {
//            System.out.println(cont.getString(1));
//        }
//        System.out.println(sql);
        st.execute(sql);
    }

    private ArrayList<String> getTypedColumns(ArrayList<Pair<String, String>> typedNames) {
        ArrayList<String> typed_columns = new ArrayList<>();
        for (Pair<String, String> name : typedNames) {
            typed_columns.add(name.getFirst() + " " + name.getSecond());
        }
        return typed_columns;
    }

    private ArrayList<String> getNames(HashMap<String, String> typedNames) {
        return new ArrayList<>(typedNames.keySet());
    }

    private Connection connect() throws Exception {
        Class.forName("org.h2.Driver").newInstance();
        return DriverManager.getConnection("jdbc:h2:./file",
                "sa", "");
    }

    public void insert(ArrayList<Pair<String, String>> vars, String person) throws Exception {
        String questions = "?" + ", ?".repeat(vars.size() - 1);
        String query = "INSERT INTO " + table + " VALUES (NULL, ?, " + questions + ");";
        PreparedStatement st1 = null;
        st1 = db.prepareStatement(query);
        st1.setString(1, person);
        for (var i = 1; i <= vars.size(); i++) {
            st1.setString(i + 1, vars.get(i - 1).getSecond());
        }
        st1.executeUpdate();
    }

    public void insertPositional(ArrayList<Pair<String, String>> vars, String person) throws Exception {
        String questions = "?" + ", ?".repeat(vars.size() - 1);

        StringBuilder names = new StringBuilder();
        names.append(vars.get(0).getFirst());
        for (var i = 1; i < vars.size(); i++) {
            names.append(", ").append(vars.get(i).getFirst());
        }
        String query = "INSERT INTO " + table + " " +
                "(ID, PERSON, " + names.toString() + ") " +
                "VALUES (NULL, ?, " + questions + ");";
        PreparedStatement st1 = null;
        System.out.println(query);
        st1 = db.prepareStatement(query);
        st1.setString(1, person);
        for (var i = 1; i <= vars.size(); i++) {
            st1.setString(i + 1, vars.get(i - 1).getSecond());
        }
        st1.executeUpdate();
    }

    public HashMap<String, String> selectLast(ArrayList<Pair<String, String>> types, String person) throws Exception {
        Statement st = db.createStatement();
        String sql = "SELECT * FROM " + table + " WHERE PERSON = '" + person + "' ORDER BY ID DESC LIMIT 1";
        try (ResultSet result = st.executeQuery(sql)) {
            result.next();
            HashMap<String, String> map = new HashMap<>();
            for (Pair<String, String> col : types) {
                String value = result.getString(col.getFirst());
                map.put(col.getFirst(), value);
            }
            return map;
        }
    }

    public void drop(String table) {
        try {
            String sql = "drop table " + table;
            Statement st = null;
            st = db.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HashMap<String, String>> selectList(ArrayList<Pair<String, String>> types, String person) throws Exception {
        Statement st = db.createStatement();
        try (ResultSet result=st.executeQuery("SELECT * FROM " + table + " where person = '" + person + "'")) {
            ArrayList<HashMap<String, String>> res = new ArrayList<>();
            while (result.next()) {
                HashMap<String, String> map = new HashMap<>();
                for (Pair<String, String> col : types) {
                    String value = result.getString(col.getFirst());
                    map.put(col.getFirst(), value);
                }
                res.add(map);
            }
            return res;
        }
    }

    public static void maain(String[] args) {
        try {
            ArrayList<Pair<String, String>> types = new ArrayList<>();
            types.add(new Pair<>("NAME", "VARCHAR"));
            types.add(new Pair<>("SUR", "VARCHAR"));
            Database database = new Database("TEST3", types);

            ArrayList<Pair<String, String>> vals = new ArrayList<>();
            types.add(new Pair<>("NAME", "RUSLAN"));
            types.add(new Pair<>("SUR", "Sirazhetdinov"));
            database.insert(vals, "r");
            HashMap<String, String> res = database.selectLast(types, "r");
            database.selectList(types, "r");
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void maian(String[] args) {
        try {
            ArrayList<Pair<String, String>> types = new ArrayList<>();
            types.add(new Pair<>("NAME", "VARCHAR"));
            types.add(new Pair<>("SUR", "VARCHAR"));
            Database database = new Database("TEST3", types);

            database.drop("CALCSTAT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}