package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import com.grievance.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final GrievanceDAO grievanceDAO = new GrievanceDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("grievances", grievanceDAO.findAll());
            request.setAttribute("facultyList", userDAO.findFaculty());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load admin dashboard.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int grievanceId = Integer.parseInt(request.getParameter("grievanceId"));
            int facultyId = Integer.parseInt(request.getParameter("facultyId"));
            int adminId = (Integer) request.getSession().getAttribute("userId");
            boolean assigned = grievanceDAO.assign(grievanceId, facultyId, adminId);
            request.getSession().setAttribute("flashMessage",
                    assigned ? "Complaint assigned successfully." : "Assignment was not completed.");
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (Exception e) {
            throw new ServletException("Unable to assign grievance.", e);
        }
    }
}
