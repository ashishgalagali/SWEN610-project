<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="300">
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<div class="page">

    <h1>Web Checkers Register Page</h1>

    <div class="body">
        <div align="center">
            <p>Hello! Please Register Below</p>
            <#if message??>
                <div class="error">${message}</div><br>
                <script data-main="js/game/index" src="js/require.js"></script>
            </#if>
            <form action="/register" method="POST">
                <input type="text" name="username" pattern="[a-zA-Z0-9-]+" placeholder="Username"
                       title="Username can only contain letters and numbers" required><br><br>
                <input type="email" id="email" name="email" class="form-control" placeholder="Email" required><br><br>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                       required>
                <input type="submit" value="register">
            </form>
        </div>
    </div>

</div>
</body>
</html>
