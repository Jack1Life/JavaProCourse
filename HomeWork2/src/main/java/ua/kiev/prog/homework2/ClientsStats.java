package ua.kiev.prog.homework2;

import java.util.HashMap;

public class ClientsStats {
    private static HashMap<String, QuizStat> statistics = new HashMap<String, QuizStat>();

    public static HashMap<String, QuizStat> getStatistics() {
        return ClientsStats.statistics;
    }

    public static QuizStat getStatistic(String id){
        QuizStat qs = ClientsStats.statistics.get(id);
        return qs == null ? createNewStatistic(id) : qs;
    }

    public static QuizStat createNewStatistic(String id){
        QuizStat qs = new QuizStat();
        ClientsStats.statistics.put(id, qs);
        return qs;
    }
}
