package com.validPasswordServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class ValidPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");

        // Username validation: Starts with an uppercase letter and is at least 3 characters long
        String userRegex = "^[A-Z][A-Za-z]{2,}$";
        Pattern userPattern = Pattern.compile(userRegex);
        Matcher userMatcher = userPattern.matcher(user);

        // Password validation: 8+ characters, 1 uppercase, 1 digit, exactly 1 special character
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(pwd);

        // Fetch stored credentials
        String userId = getServletConfig().getInitParameter("user");
        String password = getServletConfig().getInitParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (!userMatcher.matches()) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            out.println("<font color=red>Username must start with a capital letter and be at least 3 characters long.</font>");
            rd.include(request, response);
        } else if (!passwordMatcher.matches()) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            out.println("<font color=red>Password must be at least 8 characters long, contain at least one uppercase letter, one digit, and exactly one special character.</font>");
            rd.include(request, response);
        } else if (userId.equals(user) && password.equals(pwd)) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("LoginSuccess.jsp").forward(request, response);
        } else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            out.println("<font color=red>Either username or password is incorrect.</font>");
            rd.include(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Servlet is working!");
    }
}
