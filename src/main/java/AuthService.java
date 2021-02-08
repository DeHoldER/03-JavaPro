import java.sql.SQLException;

public class AuthService {
//    List<Client> clients = new ArrayList();

    AuthService() {


//        clients.add(new Client("Павел", "pavel1", "123"));
//        clients.add(new Client("Олег", "oleg1", "123"));
//        clients.add(new Client("Ник", "nick1", "123"));
    }

//    synchronized Client authorize(String login, String password) {
//        for (int i = 0; i < clients.size(); i++) {
//            Client client = clients.get(i);
//            if (client.login.equals(login) && client.password.equals(password)) {
//                return client;
//            }
//        }
//        return null;
//    }

    synchronized Client authorize(String login, String password) {
        DBQuery dbQuery = new DBQuery();
        try {
            Client client = dbQuery.checkPassword(login,password);
            if (client.name != null) {
                return client;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        for (int i = 0; i < clients.size(); i++) {
//            Client client = clients.get(i);
//            if (client.login.equals(login) && client.password.equals(password)) {
//                return client;
//            }
//        }
        return null;
    }

}
