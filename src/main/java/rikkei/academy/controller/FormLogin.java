package rikkei.academy.controller;

import rikkei.academy.model.Role;
import rikkei.academy.model.RoleName;
import rikkei.academy.model.User;
import rikkei.academy.service.role.IRoleService;
import rikkei.academy.service.role.RoleServiceIMPL;
import rikkei.academy.service.user.IUserService;
import rikkei.academy.service.user.UserServiceIMPL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = {"/", "/users"})
public class FormLogin extends HttpServlet {
    private IUserService userService = new UserServiceIMPL();
    private IRoleService roleService = new RoleServiceIMPL();




    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action){
            case "register":
                showFormRegister(request,response);
                break;
            case "login":
                showFormLogin(request,response);
                break;

        }


    }

    private void showFormLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("form-login/login.jsp");
        dispatcher.forward(request,response);
    }

    private void showFormRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("form-login/register.jsp");
        dispatcher.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action){
            case "register":
                actionRegister(request,response);
                break;
            case "login":
                actionFormLogin(request,response);
                break;

        }
    }

    private void actionFormLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

       User user  = userService.findByUserNameAndPassWord(username,password);
        String destPage = "form-login/login.jsp";
        if (user != null){
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            destPage = "form-login/login.jsp";

        }else {
            String message = "Login failed! Please check username/password!!";
            request.setAttribute("message",message);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request,response);
    }

    private void actionRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = "user";
        Set<String> strRole = new HashSet<>();
        Set<Role> roles = new HashSet<>();
        strRole.add(role);
        strRole.forEach(role1->{
            switch (role1){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN);
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM);
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER);
                    roles.add(userRole);


            }
        });
        System.out.println("role set");
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        if (userService.exitedByUserName(username)){
            request.setAttribute("message","the username exited!! Try again!!!");

            RequestDispatcher dispatcher = request.getRequestDispatcher("form-login/register.jsp");
            dispatcher.forward(request,response);
            return;
        }
        String email = request.getParameter("email");
        if (userService.exitedByEmail(email)){
            request.setAttribute("message","the email exited!! Try again!!!");


            RequestDispatcher dispatcher = request.getRequestDispatcher("form-login/register.jsp");
            dispatcher.forward(request,response);
            return;
        }

        String passWord = request.getParameter("password");
        User user= new User(name,username,passWord,email,roles);
        userService.save(user);
        request.setAttribute("success","Success!!!");

        RequestDispatcher dispatcher = request.getRequestDispatcher("form-login/register.jsp");
        dispatcher.forward(request,response);




    }

    public void destroy() {
    }
}