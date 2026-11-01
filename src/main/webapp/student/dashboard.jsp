<%@ page import="java.util.List,com.grievance.model.Grievance,com.grievance.util.Html" contentType="text/html;charset=UTF-8" %>
<%
    List<Grievance> grievances = (List<Grievance>) request.getAttribute("grievances");
    int total = 0, pending = 0, progress = 0, resolved = 0;
    if (grievances != null) {
        total = grievances.size();
        for (Grievance x : grievances) {
            String s = x.getStatus();
            if ("RESOLVED".equals(s) || "CLOSED".equals(s)) resolved++;
            else if ("IN_PROGRESS".equals(s) || "ASSIGNED".equals(s)) progress++;
            else pending++;
        }
    }
    String navActive = "dashboard";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css?v=1">
</head>
<body class="app">
<div class="shell">
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <main class="main">
        <div class="page-head">
            <div><h1>Dashboard</h1><p class="muted"><%= Html.today() %></p></div>
            <a class="button-link" href="<%= request.getContextPath() %>/student/submit-grievance.jsp">+ Submit New Grievance</a>
        </div>

        <% String flash = (String) session.getAttribute("flashMessage");
           if (flash != null) { session.removeAttribute("flashMessage"); %><p class="success"><%= Html.esc(flash) %></p><% } %>
        <% String flashError = (String) session.getAttribute("flashError");
           if (flashError != null) { session.removeAttribute("flashError"); %><p class="error"><%= Html.esc(flashError) %></p><% } %>

        <section class="stats">
            <div class="stat"><div><div class="lbl">Total submitted</div><div class="num"><%= total %></div></div><div class="ico">→</div></div>
            <div class="stat amber"><div><div class="lbl">Awaiting action</div><div class="num"><%= pending %></div></div><div class="ico">→</div></div>
            <div class="stat"><div><div class="lbl">In progress</div><div class="num"><%= progress %></div></div><div class="ico">→</div></div>
            <div class="stat green"><div><div class="lbl">Resolved</div><div class="num"><%= resolved %></div></div><div class="ico">→</div></div>
        </section>

        <%@ include file="/WEB-INF/partials/overview.jspf" %>

        <div class="panel">
            <h2>My Grievances</h2>
            <% if (grievances == null || grievances.isEmpty()) { %>
                <div class="empty"><div class="big">📝</div><p>You have not submitted any grievances yet.</p></div>
            <% } else { %>
            <div class="table-wrap"><table>
                <thead><tr><th>ID</th><th>Title</th><th>Category</th><th>Department</th><th>Status</th><th>Submitted</th><th>Escalation</th></tr></thead>
                <tbody>
                <% for (Grievance g : grievances) {
                       boolean canEscalate = !("RESOLVED".equals(g.getStatus()) || "CLOSED".equals(g.getStatus()) || "ESCALATED".equals(g.getStatus())); %>
                <tr>
                    <td class="id-tag">#<%= g.getGrievanceId() %></td>
                    <td><%= Html.esc(g.getTitle()) %></td>
                    <td><%= Html.esc(g.getCategory()) %></td>
                    <td><%= Html.esc(g.getDepartment()) %></td>
                    <td><span class="status s-<%= Html.statusClass(g.getStatus()) %>"><%= Html.statusLabel(g.getStatus()) %></span></td>
                    <td><%= Html.date(g.getCreatedAt()) %></td>
                    <td>
                        <% if (canEscalate) { %>
                        <form method="post" action="<%= request.getContextPath() %>/student/escalate" class="inline-form">
                            <input type="hidden" name="grievanceId" value="<%= g.getGrievanceId() %>">
                            <input name="reason" placeholder="Reason for escalation" required>
                            <button class="small-button danger-button" type="submit">Escalate</button>
                        </form>
                        <% } else { %><span class="muted small">—</span><% } %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table></div>
            <% } %>
        </div>
    </main>
</div>
</body>
</html>
