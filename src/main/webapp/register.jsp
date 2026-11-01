<%@ page import="com.grievance.util.Html" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register | Student Grievance System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css?v=1">
</head>
<body class="auth">
<div class="auth-box">
    <div class="auth-side">
        <div class="brand-mark">
            <svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3 2 8l10 5 10-5-10-5z"/><path d="m2 13 10 5 10-5"/></svg>
        </div>
        <h2>Join GrievEase today.</h2>
        <p>One account to submit, track and escalate all your college grievances.</p>
        <ul>
            <li>✔ Free for every student</li>
            <li>✔ Clear status for each complaint</li>
            <li>✔ Direct line to faculty and admin</li>
        </ul>
    </div>
    <div class="auth-main">
        <h1>Create your account</h1>
        <p class="muted sub">Register to submit and track grievances</p>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= Html.esc(request.getAttribute("error")) %></p>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/register">
            <label>Full name</label>
            <input type="text" name="name" maxlength="100" placeholder="Your full name" required>
            <label>Email</label>
            <input type="email" name="email" maxlength="150" placeholder="you@college.edu" required>
            <label>Password (minimum 8 characters)</label>
            <input type="password" name="password" minlength="8" placeholder="Create a strong password" required>
            <button class="full" type="submit">Register</button>
        </form>
        <p>Already registered? <a href="<%= request.getContextPath() %>/login">Login</a></p>
    </div>
</div>
</body>
</html>
