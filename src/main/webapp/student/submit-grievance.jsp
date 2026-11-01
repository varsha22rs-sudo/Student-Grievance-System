<%@ page import="com.grievance.util.Html" contentType="text/html;charset=UTF-8" %>
<%
    String navActive = "submit";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Submit a Grievance</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css?v=1">
</head>
<body class="app">
<div class="shell">
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <main class="main">
        <div class="page-head">
            <div><h1>Submit a Grievance</h1><p class="muted">Describe your issue clearly so it can be resolved quickly.</p></div>
        </div>

        <% String flashError = (String) session.getAttribute("flashError");
           if (flashError != null) { session.removeAttribute("flashError"); %><p class="error"><%= Html.esc(flashError) %></p><% } %>

        <div class="panel narrow">
            <form method="post" action="<%= request.getContextPath() %>/student/submit">
                <label>Complaint title</label>
                <input type="text" name="title" maxlength="150" placeholder="Short summary of the problem" required>

                <label>Category</label>
                <select name="category" required>
                    <option value="">Choose category</option>
                    <option>Academic</option>
                    <option>Examination</option>
                    <option>Hostel</option>
                    <option>Library</option>
                    <option>Fees</option>
                    <option>Infrastructure</option>
                    <option>Other</option>
                </select>

                <label>Concerned department</label>
                <select name="department" required>
                    <option value="">Choose department</option>
                    <option>Computer Science</option>
                    <option>Information Technology</option>
                    <option>Electronics</option>
                    <option>Mechanical</option>
                    <option>Civil</option>
                    <option>Administration</option>
                    <option>Accounts</option>
                </select>

                <label>Description</label>
                <textarea name="description" placeholder="Explain what happened, when, and what you expect..." required></textarea>

                <button class="full" type="submit">Submit Grievance</button>
            </form>
        </div>
    </main>
</div>
</body>
</html>
