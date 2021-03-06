<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>

        <script>
			function myFunction(id) {
				var person = prompt("Please enter your name", "Harry Potter");

				if (person != null) {
					document.getElementById(id).innerHTML = person;
					document.getElementById(id).value = person;
				}
			}
        </script>       

        <script src="http://yui.yahooapis.com/3.18.1/build/yui/yui-min.js"></script>
        <script type="text/javascript">
			//http://yuilibrary.com/yui/docs/autocomplete/
			// http://www.programming-free.com/2013/03/ajax-fetch-data-from-database-in-jsp.html
			YUI().use('autocomplete', 'autocomplete-filters', 'autocomplete-highlighters', function(Y) {

				var Salas = ${salas};

				Y.one('#ac-input').plug(Y.Plugin.AutoComplete, {
					resultFilters: 'startsWith',
					resultHighlighter: 'startsWith',
					source: Salas
				});
			});
        </script>

    </head>

    <body>        
        <jsp:useBean id="currentUser" type="model.Usuario" scope="session"/>
<%--        <jsp:useBean id="horario_sala" type="java.util.List" scope="session"/>        --%>
        <jsp:useBean id="salas" type="String" scope="session"/>
        <jsp:useBean id="horarios" type="java.util.List" scope="session"/>
		<jsp:useBean id="titulo_busca" type="String" scope="session"/>
		<jsp:useBean id="dias" type="String[]" scope="session"/>

		<jsp:useBean id="segunda" type="java.util.List" scope="session"/>
		<jsp:useBean id="terca" type="java.util.List" scope="session"/>
		<jsp:useBean id="quarta" type="java.util.List" scope="session"/>
		<jsp:useBean id="quinta" type="java.util.List" scope="session"/>
		<jsp:useBean id="sexta" type="java.util.List" scope="session"/>
		<jsp:useBean id="sabado" type="java.util.List" scope="session"/>

    <center>
        <h1>Ola, ${currentUser.nome}</h1>

        <a href="Servlet?acao=uploadExcel">Upload do Excel</a> 
        <a href="editarDados.jsp">Editar dados pessoais</a>  
        <a href="Servlet?acao=usuarios">Usarios Cadastrados</a>          
        <a href="Servlet?acao=logout">Logout</a>

		<c:if test="${horarios_size == 0}">
			<h2>Faca o upload do excel para utilizar o sistema</h2>
		</c:if>

		<c:if test="${horarios_size > 0}">		
			<div id="demo" class="yui3-skin-sam"> <!-- You need this skin class -->

				<label for="ac-input">Buscar por:</label><br>
				<form method="POST" action="Servlet?acao=search">
					<input type="text" name="sala" id="ac-input">
					<input type="submit" name="submit" value="buscar">
				</form>  
			</div>

			<h3>${titulo_busca} ${currentSala}</h3>

			<table border="1px">
				<tr>
					<th></th>
						<c:forEach items="${horarios}" var="h" varStatus="i">
						<th>${h}</th>
						</c:forEach> 				
				</tr>

				<tr>
					<td>Segunda</td>
					<c:forEach items="${segunda}" var="s">
						<td colspan="${s.tamanho}">${s.conteudo}</td>
					</c:forEach>				
				</tr>

				<tr>
					<td>Terca</td>
					<c:forEach items="${terca}" var="s">
						<td colspan="${s.tamanho}"> ${s.conteudo}</td>
					</c:forEach>				
				</tr>

				<tr>
					<td>Quarta</td>
					<c:forEach items="${quarta}" var="s">
						<td colspan="${s.tamanho}"> ${s.conteudo}</td>
					</c:forEach>				
				</tr>

				<tr>
					<td>Quinta</td>
					<c:forEach items="${quinta}" var="s">
						<td colspan="${s.tamanho}"> ${s.conteudo}</td>
					</c:forEach>				
				</tr>

				<tr>
					<td>Sexta</td>
					<c:forEach items="${sexta}" var="s">
						<td colspan="${s.tamanho}"> ${s.conteudo}</td>
					</c:forEach>				
				</tr>

				<tr>
					<td>Sabado</td>
					<c:forEach items="${sabado}" var="s">
						<td colspan="${s.tamanho}"> ${s.conteudo}</td>
					</c:forEach>				
				</tr>			
			</table>
			<%--
			<table border="1px">
				<tr>
					<th></th>
					<c:forEach items="${horarios}" var="h" varStatus="i">
						<th>${h}</th>
					</c:forEach> 							
				</tr>

				<c:forEach items="${dias}" var="d" varStatus="i">
					<tr>
						<td>${d}</td>
						<c:forEach items="${horario_sala}" begin="${horarios_size * i.index}" end="${horarios_size * (i.index + 1) -1}" var="s">
							<td colspan="${s.tamanho}">${s.conteudo}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
			--%>
			<form action="Servlet?acao=gerar" method="POST">
				<input type="submit" value="Gerar Excel">
			</form>
		</c:if>		
    </center>    
</body>
</html>
