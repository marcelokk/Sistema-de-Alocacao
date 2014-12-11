<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Excel</title>

        <script>
			function func_search() {
				xmlhttp.open("POST", "/Servlet?acao=teste", true);
				xmlhttp.send();
			}
        </script>
    </head>
    <body>
        <%--
        <jsp:useBean id="horarios" type="String" scope="session"/>
        <jsp:useBean id="cursos" type="String" scope="session"/>
        <jsp:useBean id="salas" type="String" scope="session"/>
        <jsp:useBean id="recursos" type="String" scope="session"/>
        <jsp:useBean id="disciplinas" type="String" scope="session"/>
        <jsp:useBean id="turmas" type="String" scope="session"/>
        <jsp:useBean id="preferencias" type="String" scope="session"/>
		--%>
    <center>
        <h2>Upload das Tabelas do Excel</h2>

        <form method="POST" action="Servlet?acao=upload_excel" enctype="multipart/form-data">
			<input type="file" name="excel" value="${horario}" onchange="func_search();">
            <input type="submit" value="upload">
        </form>                    

        <a href="home.jsp">Voltar</a>
    </center>
</body>
</html>
