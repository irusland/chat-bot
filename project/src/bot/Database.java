package bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Database {
    private Connection db;
    private String table;
    public Database(String table, HashMap<String, String> typedNames) throws Exception {

        this.table = table;
        db = connect();
        if (db == null)
            throw new Exception("Cannot connect to db");

        ArrayList<String> columns = getTypedColumns(typedNames);

        String cols = String.join(",", columns);
        Statement st = null;
        st = db.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                " (id INT AUTO_INCREMENT PRIMARY KEY, " + cols + ")";
        System.out.println(sql);
        st.execute(sql);
    }

    private ArrayList<String> getTypedColumns(HashMap<String, String> typedNames) {
        ArrayList<String> typed_columns = new ArrayList<>();
        for (String name : typedNames.keySet()) {
            typed_columns.add(name + " " + typedNames.get(name));
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

    public void insert(ArrayList<String> vars) throws Exception {
        String questions = "?" + " ?".repeat(vars.size() - 1);
        String query = "insert into " + table + " values(" + questions + ")";
        PreparedStatement st1 = null;
        st1 = db.prepareStatement(query);
        for (var i = 1; i <= vars.size(); i++) {
            st1.setString(i, vars.get(i));
        }
        st1.execute();
    }

    public HashMap<String, String> selectLast(ArrayList<String> names) throws Exception {
        Statement st = db.createStatement();
        ResultSet result;
        result = st.executeQuery("SELECT * FROM " + table + " ORDER BY ID DESC LIMIT 1");
        HashMap<String, String> map = new HashMap<>();
        for (String column : names) {
            String value = result.getString(column);
            map.put(column, value);
        }
        return map;
    }

    public static void main(String[] args) {
        try {
            HashMap<String, String> types = new HashMap<>();
            types.put("NAME", "VARCHAR");
            Database database = new Database("TEST", types);
            database.insert(new ArrayList<>(types.keySet()));
            HashMap<String, String> res = database.selectLast(new ArrayList<>(types.keySet()));
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}