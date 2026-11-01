package com.grievance.controller;

import com.grievance.dao.GrievanceDAO;
import com.grievance.model.Grievance;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/submit")
public class SubmitGrievanceServlet extends HttpServlet {
    private final GrievanceDAO grievanceDAO = new GrievanceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer studentId = (Integer) request.getSession().getAttribute("userId");
        if (studentId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Grievance g = new Grievance();
        g.setStudentId(studentId);
        g.setTitle(request.getParameter("title"));
        g.setCategory(request.getParameter("category"));
        g.setDescription(request.getParameter("description"));
        g.setDepartment(request.getParameter("department"));

        if (blank(g.getTitle()) || blank(g.getCategory()) ||
                blank(g.getDescription()) || blank(g.getDepartment())) {
            request.getSession().setAttribute("flashError", "Please complete every field.");
            response.sendRedirect(request.getContextPath() + "/student/submit-grievance.jsp");
            return;
        }

        try {
            grievanceDAO.submit(g);
            request.getSession().setAttribute("flashMessage",
                    "Grievance submitted successfully. Your complaint ID is " + g.getGrievanceId());
            response.sendRedirect(request.getContextPath() + "/student");
        } catch (Exception e) {
            throw new ServletException("Could not submit grievance.", e);
        }
    }

    private boolean blank(String s) { return s == null || s.isBlank(); }
}
