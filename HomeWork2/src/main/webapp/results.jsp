<%@ page import="ua.kiev.prog.homework2.ClientsStats" %><%--
  Created by IntelliJ IDEA.
  User: jack
  Date: 16.02.22
  Time: 00:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Statistic</title>
    </head>
    <body>
        <table>
            <tr>
                <td colspan="4"> How is your mood? </td>
            </tr>
            <tr>
                <td> Fine </td>
                <td> Normal </td>
                <td> Bad </td>
                <td> Sucks </td>
            </tr>
            <tr>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("mood", "fine") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("mood", "normal") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("mood", "bad") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("mood", "sucks") %> </td>
            </tr>
        </table>
        </br>
        <table>
            <tr>
                <td colspan="4"> Is it better than yesterday? </td>
            </tr>
            <tr>
                <td> Yes </td>
                <td> No </td>
                <td> Same </td>
                <td> Are you serious? </td>
            </tr>
            <tr>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("compare", "yes") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("compare", "no") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("compare", "same") %> </td>
                <td> <%= "" + ClientsStats.getStatistic(session.getId()).getAnwersNum("compare", "aggressive") %> </td>
            </tr>
        </table>
        </br>
        <a href="/">Try one more time!</a>
    </body>
</html>
