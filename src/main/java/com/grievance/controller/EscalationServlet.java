package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/escalate")
public class EscalationServlet extends HttpServlet {
    private final GrievanceDAO grievanceDAO = new GrievanceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer studentId = (Integer) request.getSession().getAttribute("userId");
        if (studentId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int grievanceId = Integer.parseInt(request.getParameter("grievanceId"));
            String reason = request.getParameter("reason");
            if (reason == null || reason.isBlank()) {
                request.getSession().setAttribute("flashError", "Please explain why you are escalating.");
            } else {
                boolean escalated = grievanceDAO.escalate(grievanceId, studentId, reason.trim());
                request.getSession().setAttribute("flashMessage",
                        escalated ? "Grievance escalated for review." :
                                "This grievance cannot be escalated in its current status.");
            }
            response.sendRedirect(request.getContextPath() + "/student");
        } catch (Exception e) {
            throw new ServletException("Unable to escalate grievance.", e);
        }
    }
}
