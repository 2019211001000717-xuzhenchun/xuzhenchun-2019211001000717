package com.xuzhenchun.week3.demo;
import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

@WebServlet(
        urlPatterns = {"/Register"}
)

public class RegisterServlet extends HttpServlet {
    Connection con=null;
    @Override
    public void init() throws ServletException {
        String driver=getServletConfig().getServletContext().getInitParameter("driver");
        String url=getServletConfig().getServletContext().getInitParameter("url");
        String username=getServletConfig().getServletContext().getInitParameter("username");
        String password=getServletConfig().getServletContext().getInitParameter("password");
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,username,password);
            System.out.println("init()-->"+con);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ID=request.getParameter("id");
        String Username=request.getParameter("Username");
        String password=request.getParameter("password");
        String Email=request.getParameter("Email");
        String Gender=request.getParameter("Gender");
        String Date=request.getParameter("Date");

        PrintWriter writer= response.getWriter();
        /*PrintWriter writer= response.getWriter();
        writer.println("<br>Username :"+Username);
        writer.println("<br>password :"+password);
        writer.println("<br>Email :"+Email);
        writer.println("<br>Gender :"+Gender);
        writer.println("<br>Date :"+Date);
        writer.close();
        writer.close();*/
        String sql1="insert into Usertable values(?,?,?,?,?,?)";
        PreparedStatement pstmt= null;
        try {
            pstmt = con.prepareStatement(sql1);
            pstmt.setString(1,ID);
            pstmt.setString(2,Username);
            pstmt.setString(3,password);
            pstmt.setString(4,Email);
            pstmt.setString(5,Gender);
            pstmt.setString(6,Date);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<html>");
        out.println("<head><title>Register</title></head>");
        out.println("<body>");
        out.println("<table>");
        out.println("<tr><td>ID</td><td>username</td><td>password</td><td>Email</td><td>Gender</td><td>Birthdate</td></tr>");
        String sql2="select * from Usertable";
        ResultSet rs= null;
        try {
            rs = con.createStatement().executeQuery(sql2);
            while(rs.next()){
                String id=rs.getString("id");
                String username=rs.getString("username");
                String password1=rs.getString("password");
                String email=rs.getString("email");
                String gender=rs.getString("gender");
                Date birthdate=rs.getDate("birthdate");
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                out.println("<tr><td>"+id+"</td><td>"+username+"</td><td>"+password1+"</td><td>"+email+"</td><td>"+gender+"</td><td>"+sdf.format(birthdate)+"</td></tr>");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}