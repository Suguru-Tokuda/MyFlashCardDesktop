<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyFlashCard</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <style>
            footer {
                background-color: black;
                bottom: -50px;
                height: 60px;
                position: absolute;
                width: 100%;
            }
        </style>
    </head>
    <body>
    <nav class="navbar navbar-inverse bg-primary">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">MyFlashCard</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/addDeck">Add Decks</a></li>
            </ul>
            <form class="navbar-form navbar-left">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </div>
    </nav>
    <main>
        <form method="post">
            <div class="container">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-grup">
                            <h3>Sing in:</h3>
                            <div class="form-group">
                                <label class="control-label">User name or Email:</label>
                                <input type="text" class="form-control" name="userid" />
                                <!--<div class="form-group">-->
                                <label class="control-label">Password:</label>
                                <!--</div>-->
                                <input type="password" class="form-control" name="signinPassword" />
                                <div style="margin-top: 20px;"></div>
                                <input type="submit" class="btn btn-primary" formaction="${pageContext.request.contextPath}/processsignin" value="Sign in" />
                            </div>
                            <div style="margin-top: 35px;"></div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-grup">
                            <h3>Sing up:</h3>
                            <div class="form-group">
                                <label class="control-label">User name (No space & 6 characters or more):</label>
                                <input type="text" class="form-control" name="username" />
                                <label class="control-label">Email:</label>
                                <input type="text" class="form-control" name="email" />
                                <label class="control-label">Password:</label><br>
                                <label class="control-label">- At least 8 characters long</label><br>
                                <label class="control-label">- Include one or more numbers and capital letters</label>
                                <label class="control-label">- Include at least one special character: "-", "@", "#", and "%"</label>
                                <input type="password" class="form-control" name="signupPassword" />
                                <label class="control-label">Confirm Password</label>
                                <input type="password" class="form-control" name="confPassword" />
                                <div style="margin-top: 20px;"></div>
                                <input type="submit" class="btn btn-primary" formaction="${pageContext.request.contextPath}/processsignup" value="Sing Up" /><br>
                                <div style="margin-top: 20px;"></div>
                                <label class="control-label" style="color: red;">${signupErrorMsg}</label>
                            </div>
                        </div>
                    </div>
                </div>
        </form>
    </main>
    <footer class="container-fluid bg-4 text-center">
        <p>MyFlashCard.com <a href="https://www.w3schools.com">MyFlashCard.com</a></p> 
    </footer>
</body>
</html>
