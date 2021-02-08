
public class Message {
    String time;
    Client client;
    String text;

    public Message(String time, Client client, String text) {
        this.time = time;
        this.client = client;
        this.text = text;
    }
}
