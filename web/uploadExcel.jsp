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

		<script>
			var contador = 1;

			function proximo() {
				if (contador === 4) {
					document.getElementById("btn").style.visibility = "hidden";
					document.getElementById("etapa" + contador).style.backgroundColor = "coral";
					alert("Compra finalizada.");
					return;
				}
				else {
					document.getElementById("carregando").style.visibility = "visible";
					document.getElementById("btn").style.visibility = "hidden";

					setTimeout(function() {
						document.getElementById("carregando").style.visibility = "hidden";
						document.getElementById("btn").style.visibility = "visible";
						document.getElementById("etapa" + contador).style.backgroundColor = "coral";
						document.getElementById("etapa" + ++contador).style.opacity = "1";
					}, 2000);
				}
			}
		</script>

    </head>
    <body>
		<jsp:useBean id="mensagem_excel" type="java.util.List" scope="session"/>

    <center>
        <h2>Upload das Tabelas do Excel</h2>

        <form method="POST" action="Servlet?acao=upload_excel" enctype="multipart/form-data">
			<input type="file" name="excel" onchange="func_search();" required="true">
            <input type="submit" value="upload" onclick="proximo()">
        </form>                    
		<img id="carregando" src="C:\Users\Marcelo\Desktop\Sistema\ajax-loader.gif" alt="Gif de Carregamento" style="visibility:hidden">
        <a href="home.jsp">Voltar</a>

		<c:forEach items="${mensagem_excel}" var="m">
			<p>${m}</p>
		</c:forEach> 			
    </center>
</body>
</html>
