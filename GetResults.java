import java.util.*;

public class GetResults {

    private final LinkedHashMap<String, Integer> messages;
    private final int num_top;
    private final String username;
    private final int in_position;

    public GetResults(LinkedHashMap<String, Integer> messages, int num_top, String username, int in_position) {
        this.messages = messages;
        this.num_top = num_top;
        this.username = username;
        this.in_position = in_position - 1;
    }

    public void getTop() {
        LinkedHashMap<String, Integer> topData = new LinkedHashMap<>();
        this.messages.entrySet().stream().limit(this.num_top).forEachOrdered(x -> topData.put(x.getKey(), x.getValue()));
        Set<String> keys = topData.keySet();
        int i = 1;
        for (String key: keys) {
            System.out.printf("%d. %s sent %d messages\n", i++, key, topData.get(key));
        }
        System.out.println();
    }

    public void getSpecific() {
        Set<String> keys = this.messages.keySet();
        int i = 0;
        for (String key: keys) {
            i++;
            if (Objects.equals(key, this.username)) {
                System.out.printf("%s has sent %d messages and is in position %d\n", this.username, this.messages.get(key) , i);
                System.out.println();
                return;
            }
        }
        System.out.printf("%s hasn't sent any messages\n\n", username);
    }

    public void getByPosition() {
        if (in_position >= messages.size()) {
            System.out.println("The requested position is outside the range of available data\n\n");
            return;
        }
        Set<String> keySet = this.messages.keySet();
        ArrayList<String> listKeys = new ArrayList<>(keySet);
        String key = listKeys.get(this.in_position);
        System.out.printf("%s has sent %d messages and is in position %d\n", key, this.messages.get(key), in_position + 1);
        System.out.println();
    }
}
