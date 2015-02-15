<!DOCTYPE html>
<html>
<head></head>
<body>
<#list people as item>
	<#assign color = .vars[item.name + "_color"] />
	<p style="color:#${color}">${item.name}</p>
</#list>
</body> 
</html>