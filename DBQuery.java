import java.sql.*;

public class DBQuery {
    private static Connection dbConnection;
    private static PreparedStatement preparedStatement;

    public Client checkPassword(String login, String pass) throws SQLException {

        Client authorizedClient = new Client(null, login, pass);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:chatDB.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Обращение к базе данных...");

        String queryPass = "SELECT Pass FROM Users WHERE Login = ?";
        String queryName = "SELECT Name FROM Users WHERE Login = ?";
        preparedStatement = dbConnection.prepareStatement(queryPass);
        preparedStatement.setString(1, login);
        ResultSet rsPass = preparedStatement.executeQuery();

        preparedStatement = dbConnection.prepareStatement(queryName);
        preparedStatement.setString(1, login);
        ResultSet rsName = preparedStatement.executeQuery();


        if (rsPass.getString(1).equals(pass)) {
            authorizedClient.login = login;
            authorizedClient.password = pass;
            authorizedClient.name = rsName.getString(1);
        }

        preparedStatement.close();
        rsName.close();
        dbConnection.close();
        System.out.println("Обращение к базе данных завершено...");
        return authorizedClient;
    }

    public void changeName(Client client, String newName) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:chatDB.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Обращение к базе данных...");

        String login = client.login;
        String queryChangeName = "UPDATE Users SET Name = ? WHERE Login = ?";
        preparedStatement = dbConnection.prepareStatement(queryChangeName);
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, login);
        preparedStatement.executeUpdate();

        preparedStatement.close();
        dbConnection.close();
        System.out.println("Обращение к базе данных завершено...");
    }
}
