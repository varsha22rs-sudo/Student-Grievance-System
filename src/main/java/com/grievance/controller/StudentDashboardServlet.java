
package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student")
public class StudentDashboardServlet extends HttpServlet {

    private final GrievanceDAO grievanceDAO = new GrievanceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int studentId = (Integer) session.getAttribute("userId");

            request.setAttribute(
                "grievances",
                grievanceDAO.findByStudent(studentId)
            );

            request.getRequestDispatcher("/student/dashboard.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Unable to load student dashboard.", e);
        }
    }
}
