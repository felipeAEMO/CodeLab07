<% @ page language="java" contentType="text/html; charset=ISO-8859-1"pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Titulo</title>

<jsp:useBean id="tarefas" class="java.util.ArrayList" scope="application" />
</head>
<body>
<table class="table table-striped">
<tr>
<th>Titulo</th>
<th>Descrição</th>
<th>Concluida</th>
<th></th>
</tr>
<c:forEach var="task" items="${tarefas}">
<tr>
<td>${task.titulo}</td>
<td>${task.descricao}</td>
<td>${task.concluida ? 'SIM' : 'NÃO'}</td>
<td><a href="/excluir?index=${task.id}">EXCLUIR</a></td>
<td><a href="/alterar?index=${task.id}">ALTERAR</a></td>
</tr>
</c:forEach>
</table>
</body>
</html>