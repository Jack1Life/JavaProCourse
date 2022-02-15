package ua.kiev.prog.homework2;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "quizServlet", value = "/answers")
public class Quiz extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        QuizStat qs = ClientsStats.getStatistic(session.getId());
        for(String name : QuizStat.QUIZ_NAMES) {
            qs.incrementAnswerNum(name, request.getParameter(name));
        }
        response.sendRedirect("results.jsp");
    }

    public void destroy() {
    }
}