package com.grievance.controller;

import com.grievance.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null ||
                name.isBlank() || email.isBlank() || password.length() < 8) {
            request.setAttribute("error", "Enter all details. Password must contain at least 8 characters.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userDAO.register(name.trim(), email.trim().toLowerCase(), password)) {
                response.sendRedirect(request.getContextPath() + "/login?registered=1");
            } else {
                request.setAttribute("error", "Registration could not be completed.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "This email is already registered.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Registration failed. Check the database connection.", e);
        }
    }
}
