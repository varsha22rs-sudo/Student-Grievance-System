package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/faculty/update-status")
public class UpdateStatusServlet extends HttpServlet {
    private final GrievanceDAO grievanceDAO = new GrievanceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer facultyId = (Integer) request.getSession().getAttribute("userId");
        if (facultyId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int grievanceId = Integer.parseInt(request.getParameter("grievanceId"));
            String status = request.getParameter("status");
            String remarks = request.getParameter("remarks");
            boolean updated = grievanceDAO.updateStatus(grievanceId, facultyId, status, remarks);
            request.getSession().setAttribute("flashMessage",
                    updated ? "Complaint status updated." : "Status could not be updated.");
            response.sendRedirect(request.getContextPath() + "/faculty");
        } catch (Exception e) {
            throw new ServletException("Unable to update status.", e);
        }
    }
}
