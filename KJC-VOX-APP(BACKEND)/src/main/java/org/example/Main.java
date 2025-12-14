package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.controller.*;
import org.example.servlet.*;
import org.example.servlet.reset.CheckRegisteredServlet;
import org.example.servlet.reset.ResetPasswordServlet;
import org.example.servlet.reset.ResetSendCodeServlet;
import org.example.servlet.reset.ResetVerifyRollNoServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setResourceBase("frontend");  // Serve static pages from /frontend folder

        // ✅ Serve static files
        handler.addServlet(new ServletHolder(new DefaultServlet()), "/");

        // ✅ IMPROVED CORS Filter (Properly handling Angular Frontend requests)
        handler.addFilter(new FilterHolder(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) { }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {

                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;

                // Set CORS headers
                res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
                res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
                res.setHeader("Access-Control-Allow-Credentials", "true");
                res.setHeader("Access-Control-Max-Age", "3600");

                // Handle preflight OPTIONS requests
                if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
                    res.setStatus(HttpServletResponse.SC_OK);
                    return;
                }

                chain.doFilter(request, response);
            }

            @Override
            public void destroy() { }
        }), "/*", null);

        // ✅ Existing Auth Routes
        handler.addServlet(new ServletHolder(new LoginServlet()), "/api/login");
        handler.addServlet(new ServletHolder(new RegisterServlet()), "/api/register");
        handler.addServlet(new ServletHolder(new SendCodeServlet()), "/api/send-code");

        // ✅ First-Time Verification Routes
        handler.addServlet(new ServletHolder(new VerifyDetailsServlet()), "/api/verify-details");
        handler.addServlet(new ServletHolder(new FinalizeVerificationServlet()), "/api/finalize-verification");

        // ✅ Reset Password Routes
        handler.addServlet(new ServletHolder(new ResetVerifyRollNoServlet()), "/api/reset/verify-rollno");
        handler.addServlet(new ServletHolder(new ResetPasswordServlet()), "/api/reset/password");
        handler.addServlet(new ServletHolder(new CheckRegisteredServlet()), "/api/reset/check-registered");
        handler.addServlet(new ServletHolder(new ResetSendCodeServlet()), "/api/reset/send-code");
        // ✅ Dashboard Route
        handler.addServlet(new ServletHolder(new DashboardController()), "/api/dashboard/*");
        handler.addServlet(new ServletHolder(new FeedbackHistoryController()), "/api/feedback-history/*");



        // ✅ Faculty Management Routes
        handler.addServlet(new ServletHolder("facultyController", new FacultyController()), "/api/faculty/*");
        // ✅ Department Management Route
        handler.addServlet(new ServletHolder("addDepartmentController", new AddDepartmentController()), "/api/departments");

        handler.addServlet(new ServletHolder("departmentByClassServlet", new DepartmentByClassServlet()), "/api/departments/class");

        // Register your Notice servlet
        handler.addServlet(new ServletHolder("noticeServlet", new NoticeController()), "/api/notices");


        handler.addServlet(new ServletHolder("assignSubjectServlet", new AssignSubjectController()), "/api/assign-subjects");


        handler.addServlet(new ServletHolder("mySubjectsController", new MySubjectsController()), "/api/my-subjects");

        handler.addServlet(new ServletHolder("studentDetailsController", new StudentDetailsController()), "/api/student-details");

        handler.addServlet(new ServletHolder("submitFeedback", new SubmitFeedbackController()), "/api/submit-feedback");

        handler.addServlet(new ServletHolder(new FeedbackSummaryController()), "/api/feedback-summary");


        handler.addServlet(new ServletHolder(new TestSubController()), "/api/test-sub-my-subjects");


        handler.addServlet(new ServletHolder(new FeedbackController()), "/api/*");
        // ✅ Other Controllers
        AuthController.routes();

        server.setHandler(handler);


        server.start();

        // ✅ Console URL
        String url = "http://localhost:" + port + "/Home.html";
        System.out.println("\n==========================================");
        System.out.println("✅ Server started successfully!");
        System.out.println("🌐 Open the app in your browser:");
        System.out.println("\u001B]8;;" + url + "\u001B\\" + url + "\u001B]8;;\u001B\\");
        System.out.println("🚀 Angular Frontend: http://localhost:4200");
        System.out.println("📚 Faculty API: http://localhost:" + port + "/api/faculty");
        System.out.println("==========================================\n");

        server.join();
    }
}