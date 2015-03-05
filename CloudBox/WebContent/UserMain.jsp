<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CloudBox Login and Register</title>
<script type="text/javascript">
function insertTable()
{
    var num_rows = document.getElementById('rows').value;
    var num_cols = document.getElementById('cols').value;
    var width = document.getElementById('width').value;
    var theader = "<table id='table1' width = ' "+ width +"% '>";
    var tbody = "";

    for(var j = 0; j < num_cols; j++)
    {
      theader += "<th>header col "+ (j+1) +" </th>";
    }

    for(var i = 0; i < num_rows; i++)
    {
        tbody += "<tr>";
        for(var j = 0; j < num_cols; j++)
        {
            tbody += "<td>";
            tbody += "?";
            tbody += "</td>"
        }
        tbody += "</tr>";
    }
    var tfooter = "</table>";
    document.getElementById('wrapper').innerHTML = theader + tbody + tfooter;
}
</script>
<style>
#table1
{
    border:solid 1px;
    border-collapse:collapse;
}

#table1 th
{
    border:solid 1px;
    border-collapse:collapse;
}

#table1 td
{
    border:solid 1px;
    vertical-align:top;
}

</style>
</head>
<body>
<form name="tableForm" class="dynTblForm">
    <label>Rows: <input type="text" name="rows" id="rows"/></label><br />
    <label>Cols: <input type="text" name="cols" id="cols"/></label><br />
    <label>Width (%): <input type="text" name="width" id="width"/></label><br />
    <button type="button" onclick="insertTable()">Create Table</button>
    <div id="wrapper"></div>
</form>
</body>
</html>