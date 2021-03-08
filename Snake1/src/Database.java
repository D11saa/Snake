import data.PostgresDB;

import java.sql.*;

public class Database {
    private PostgresDB db = new PostgresDB();

    public int getMax(){
        try {
            Connection con = db.getConnection();
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(DISTINCT score) FROM score");
                while (rs.next()){
                    Integer max = rs.getInt("max");
                    return max;
                }
                st.close();
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertScore(int num) {
        try {
            Connection con = db.getConnection();
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO score(score) VALUES (?)");
                st.setInt(1, num);
                int bool = st.executeUpdate();

                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
