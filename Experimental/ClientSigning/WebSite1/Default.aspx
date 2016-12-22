<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <script src="/socket.io/socket.io.js"></script>
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
      <table>
	
		
	<tr>
	<td>InputData :</td>
	<td><textarea id="name" cols=80 rows=20 ></textarea></td>
	</tr>
	
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr>

	<tr>
	<td>
	<input type="button" value="FormSign" onClick='call("signData",document.getElementById("name").value)'>
	</td>
    </tr>

	<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr>
	<tr>
	<td>Output signedData :</td>
	<td><textarea id="signData" cols=80 rows=20 readonly = "readonly"></textarea></td>
	</tr>

	</table>
        
    </div>
    </form>
</body>

      <script type="text/javascript">
var connection = new WebSocket('wss://localhost.emudhra.com:8080');

connection.onopen = function () {
  console.log('Connection Opened');
};
connection.onerror = function (error) 
{
  alert('Please check the server connection: ' + error);
  document.getElementById("signData").value=error;
};
connection.onmessage = function (e) 
{
  if(e.data.indexOf("subProtocol")==-1)
    alert(e.data);
};


function call(txf1,msg)
{
connection.send(msg);
connection.onerror = function (error) 
{
  alert('Please check the server connection: ' + error);
  document.getElementById("signData").value=error;
};
connection.onmessage = function (e) 
{
  if(e.data.indexOf("subProtocol")==-1)
    document.getElementById(txf1).value=e.data;
};

}
</script>
 
</html>
