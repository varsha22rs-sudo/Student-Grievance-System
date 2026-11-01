package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/faculty")
public class FacultyDashboardServlet extends HttpServlet {
    private final GrievanceDAO grievanceDAO = new GrievanceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int facultyId = (Integer) request.getSession().getAttribute("userId");
            request.setAttribute("grievances", grievanceDAO.findAssignedTo(facultyId));
            request.getRequestDispatcher("/faculty/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load faculty dashboard.", e);
        }
    }
}
