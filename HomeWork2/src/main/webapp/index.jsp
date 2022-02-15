<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP - Hello World</title>
    </head>
    <body>
        <h1>Quiz</h1>
        <br/>
        <form action="/answers" method="POST">
            How is your mood? <br/>
            <p> <input type="radio" name="mood" value="fine"> Fine</p>
            <p> <input type="radio" name="mood" value="normal"> Normal</p>
            <p> <input type="radio" name="mood" value="bad"> Bad</p>
            <p> <input type="radio" name="mood" value="sucks"> Sucks</p>

            Is it better than yesterday? <br/>
            <p> <input type="radio" name="compare" value="yes"> Yes</p>
            <p> <input type="radio" name="compare" value="no"> No</p>
            <p> <input type="radio" name="compare" value="same"> Same</p>
            <p> <input type="radio" name="compare" value="aggressive"> Are you serious?</p>
            <input type="submit" />
        </form>
    </body>
</html>