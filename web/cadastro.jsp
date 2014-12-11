<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Cadastro</title>
    </head>
    <body>

    <center>
        <h1>Cadastro de Novo Usuario</h1>

        <form action="Servlet?acao=cadastrar" method="POST">
            <table>

                <tr>
                    <td>Numero USP</td>
                    <td><input type="text" name="numerousp" required="true"></td>
                </tr>

                <tr>
                    <td>Nome completo:</td>
                    <td><input type="text" name="nome" required="true"></td>
                </tr>

                <tr>
                    <td>Email:</td>
                    <td><input type="text" name="email" placeholder="seu email aqui" required="true"></td>     
                </tr>

                <tr>
                    <td>Senha:</td>
                    <td><input type="password" name="password" required="true" maxlength="8"></td>
                </tr>

                <tr>
                    <td>Confirmacao de Senha</td>
                    <td><input type="password" name="cpassword" required="true" maxlength="8"></td>
                </tr>

            </table>
            <input type="submit" name="submit" value="Cadastrar">
        </form>

        <a href="login.jsp">voltar</a>         

    </center> 
</body>
</html>
