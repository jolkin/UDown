<%--
  Created by IntelliJ IDEA.
  User: Jake
  Date: 9/16/16
  Time: 4:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  Hi!
  <form action="Event" method="get">
    <input type="hidden" value="Movie Night" name="eventname"/>
    <input type="hidden" value="Scobell" name="location"/>
    <input type="hidden" value="9:30" name="goTime"/>
    <input type="hidden" value="Free" name="price"/>
    <input type="hidden" value="Watch a movie!" name="description"/>
    <input type="hidden" value="1" name="uid"/>


    <button type="submit">Try me</button>
  </form>
  </body>
</html>
