<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
<head>
        
<meta charset="utf-8">
<title>Login</title>
		
</head>
<body>
    <jsp:useBean id="mensagem" type="String" scope="session"/>

    <center>
        <h1>Sistema de Alocacao</h1>        
        
        <form action="Servlet?acao=logar" method="POST">
        <table>
            <tr>
                <td>Login</td>
                <td><input type="text" name="login" required="true"></td>
            </tr>

            <tr>
                <td>Senha</td>
                <td><input type="password" name="password" required="true" maxlength="8"></td>
            </tr>
        </table>
            <input type="submit" name="submit" value="enviar">
        </form>
            
        <p>Ainda nao e' cadastrado? Entao cadastre-se em <a href="cadastro.jsp">Cadastro</a></p>
		
		<p>${mensagem}</p>
    </center>
</body>
</html>
