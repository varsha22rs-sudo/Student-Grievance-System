<%@ page import="com.grievance.util.Html" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login | Student Grievance System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css?v=1">
</head>
<body class="auth">
<div class="auth-box">
    <div class="auth-side">
        <div class="brand-mark">
            <svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3 2 8l10 5 10-5-10-5z"/><path d="m2 13 10 5 10-5"/></svg>
        </div>
        <h2>Your voice matters on campus.</h2>
        <p>Raise a complaint, follow its progress and get it resolved faster.</p>
        <ul>
            <li>✔ Submit grievances in under a minute</li>
            <li>✔ Track every status update live</li>
            <li>✔ Escalate when you need a faster response</li>
        </ul>
    </div>
    <div class="auth-main">
        <h1>Welcome back</h1>
        <p class="muted sub">Sign in to continue to GrievEase</p>
        <% if (request.getAttribute("message") != null) { %>
            <p class="success"><%= Html.esc(request.getAttribute("message")) %></p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= Html.esc(request.getAttribute("error")) %></p>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/login">
            <label>Email</label>
            <input type="email" name="email" placeholder="you@college.edu" required>
            <label>Password</label>
            <input type="password" name="password" placeholder="Enter your password" required>
            <button class="full" type="submit">Login</button>
        </form>
        <p>New student? <a href="<%= request.getContextPath() %>/register">Create an account</a></p>
        <div class="demo-box">Demo admin: admin@college.edu / Admin@123</div>
    </div>
</div>
</body>
</html>
