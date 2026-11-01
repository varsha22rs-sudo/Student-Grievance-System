<%@ page import="java.util.List,com.grievance.model.Grievance,com.grievance.model.User,com.grievance.util.Html" contentType="text/html;charset=UTF-8" %>
<%
    List<Grievance> grievances = (List<Grievance>) request.getAttribute("grievances");
    List<User> facultyList = (List<User>) request.getAttribute("facultyList");
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
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css?v=1">
</head>
<body class="app">
<div class="shell">
    <%@ include file="/WEB-INF/partials/sidebar.jspf" %>
    <main class="main">
        <div class="page-head">
            <div><h1>Dashboard</h1><p class="muted"><%= Html.today() %></p></div>
        </div>

        <% String flash = (String) session.getAttribute("flashMessage");
           if (flash != null) { session.removeAttribute("flashMessage"); %><p class="success"><%= Html.esc(flash) %></p><% } %>

        <section class="stats">
            <div class="stat"><div><div class="lbl">Total complaints</div><div class="num"><%= total %></div></div><div class="ico">→</div></div>
            <div class="stat amber"><div><div class="lbl">Awaiting action</div><div class="num"><%= pending %></div></div><div class="ico">→</div></div>
            <div class="stat"><div><div class="lbl">In progress</div><div class="num"><%= progress %></div></div><div class="ico">→</div></div>
            <div class="stat green"><div><div class="lbl">Resolved</div><div class="num"><%= resolved %></div></div><div class="ico">→</div></div>
        </section>

        <%@ include file="/WEB-INF/partials/overview.jspf" %>

        <div class="panel">
            <h2>Manage Grievances</h2>
            <% if (grievances == null || grievances.isEmpty()) { %>
                <div class="empty"><div class="big">📭</div><p>No grievances have been submitted yet.</p></div>
            <% } else { %>
            <div class="table-wrap"><table>
                <thead><tr><th>ID</th><th>Student</th><th>Title</th><th>Department</th><th>Faculty</th><th>Status</th><th>Assign faculty</th></tr></thead>
                <tbody>
                <% for (Grievance g : grievances) { %>
                <tr>
                    <td class="id-tag">#<%= g.getGrievanceId() %></td>
                    <td><%= Html.esc(g.getStudentName()) %></td>
                    <td><%= Html.esc(g.getTitle()) %></td>
                    <td><%= Html.esc(g.getDepartment()) %></td>
                    <td><%= g.getAssignedFacultyName() == null ? "<span class='muted'>Unassigned</span>" : Html.esc(g.getAssignedFacultyName()) %></td>
                    <td><span class="status s-<%= Html.statusClass(g.getStatus()) %>"><%= Html.statusLabel(g.getStatus()) %></span></td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/admin" class="inline-form">
                            <input type="hidden" name="grievanceId" value="<%= g.getGrievanceId() %>">
                            <select name="facultyId" required>
                                <option value="">Select faculty</option>
                                <% if (facultyList != null) for (User f : facultyList) { %>
                                    <option value="<%= f.getUserId() %>"><%= Html.esc(f.getName()) %></option>
                                <% } %>
                            </select>
                            <button class="small-button" type="submit">Assign</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table></div>
            <% } %>
            <p class="muted small">Faculty accounts must exist with the FACULTY role before they appear here.</p>
        </div>
    </main>
</div>
</body>
</html>
