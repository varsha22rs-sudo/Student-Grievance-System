package com.grievance.controller;

import com.grievance.dao.UserDAO;
import com.grievance.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("registered")))
            request.setAttribute("message", "Registration successful. Please log in.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = userDAO.login(request.getParameter("email"),
                    request.getParameter("password"));
            if (user == null) {
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("role", user.getRole());
            session.setMaxInactiveInterval(20 * 60);

            String context = request.getContextPath();
            switch (user.getRole()) {
                case "ADMIN" -> response.sendRedirect(context + "/admin");
                case "FACULTY" -> response.sendRedirect(context + "/faculty");
                default -> response.sendRedirect(context + "/student");
            }
        } catch (Exception e) {
            throw new ServletException("Login failed. Check the database connection.", e);
        }
    }
}
