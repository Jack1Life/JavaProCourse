package ua.kiev.prog;

import java.util.ArrayList;
import java.util.List;

public class JsonMessages {
    private final List<Message> list = new ArrayList<>();

    public JsonMessages(List<Message> sourceList, int fromIndex, String toUser) {
        for (int i = fromIndex; i < sourceList.size(); i++) {
            Message msg = sourceList.get(i);
            String msgTo = msg.getTo();
            if((msgTo == null) || msgTo.equals("all") || msgTo.equals(toUser)){
                list.add(msg);
            }
        }
    }
}
