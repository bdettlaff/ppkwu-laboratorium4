<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Szukaj pracownika</title>
</head>
<body>
    <form:form method="POST" action="/search" modelAttribute="employee" acceptCharset="ISO-8859-1 UTF-8">
        <table>
            <tr>
                <td><form:label path="name">Imie</form:label></td>
                <td><form:input path="name"/></td>
            </tr>
            <tr>
                <td><form:label path="surname">Nazwisko</form:label></td>
                <td><form:input path="surname"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="Wyszukaj"/></td>
            </tr>
        </table>
        <br><br>
    </form:form>
</body>
</html>