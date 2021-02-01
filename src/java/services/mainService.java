/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.functions;

/**
 *
 * @author paatap
 */
@WebServlet(name = "mainService", urlPatterns = {"/mainService"})
public class mainService extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String ptable = "";
        String buytable = "";

//response.getWriter().write(tools.SendEmailTLS.SendEmailTLS());
        try {

            BufferedReader br = request.getReader();
            System.out.println("----BODY-----");
            String result = "";
            String line;
            while ((line = br.readLine()) != null) {
                byte[] bytes = line.getBytes(StandardCharsets.ISO_8859_1);
                line = new String(bytes, StandardCharsets.UTF_8);
                System.out.println(line);
                result += line + "\n";
            }

            System.out.println("111111111" + result);
            JsonElement el = new JsonParser().parse(result);
            System.out.println("22222");
            JsonObject job = el.getAsJsonObject();
            System.out.println("333");
            String command = tools.functions.jsonget(job, "command");
            System.out.println("command=" + command);

            if (command.equals("smstemplate")) {
// smstemplate

                String qwr = "select id,messagetxt from messagetemplate";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"smstemplate\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"smstable\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"id\":\"" + s22[0] + "\",\n"
                                + "\"txt\":\"" + s22[1] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        System.out.println("i=" + i);
                        i = i + 1;

                    }
                    ss += "\n]\n}";
                } else {
                    ss = "{\n\"command\":\"smstemplate\",\n"
                            + "\"result\":\"noTemplates\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("addtemplate")) {
// addtemplate

                String text = tools.functions.jsonget(job, "text");
                System.out.println("text=" + text);
                String qwr = "insert into  messagetemplate (messagetxt) values ('" + text + "')returning id" ;
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"addtemplate\",\n"
                            + "\"result\":\"ok\"\n}";

                } else {
                    ss = "{\n\"command\":\"addtemplate\",\n"
                            + "\"result\":\"notsaved\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("changetemplate")) {
// changetemplate

                String id = tools.functions.jsonget(job, "id");
                System.out.println("id=" + id);
                String text = tools.functions.jsonget(job, "text");
                System.out.println("text=" + text);
                String qwr = "update  messagetemplate set messagetxt= '" + text + "' where id=" + id+"returning id" ;
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"changetemplate\",\n"
                            + "\"result\":\"ok\"\n}";
                } else {
                    ss = "{\n\"command\":\"changetemplate\",\n"
                            + "\"result\":\"templatenotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("deletetemplate")) {
// deletetemplate

               String id = tools.functions.jsonget(job, "id");
                System.out.println("id=" + id);
                String qwr = "update  messagetemplate set activem= false where id=" + id+"returning id" ;
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"deletetemplate\",\n"
                            + "\"result\":\"ok\"\n}";
                } else {
                    ss = "{\n\"command\":\"deletetemplate\",\n"
                            + "\"result\":\"templatenotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("changecustomer")) {
// changecustomer

                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String anumber = tools.functions.jsonget(job, "anumber");
                System.out.println("anumber=" + anumber);
                 String name = tools.functions.jsonget(job, "name");
                System.out.println("name=" + name);
                String qwr = "update user2subscriber set usernumber='"+usernumber+"',name='"+name+"'where subscribernumber='" + anumber+ "'returning id";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"changecustomer\",\n"
                   + "\"result\":\"ok\"\n}";

                } else {
                    ss = "{\n\"command\":\"changecustomer\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("getcustomers")) {
// getcustomers

                 String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String qwr = "select usernumber,subscribernumber,name from user2subscriber where usernumber='" + usernumber + "'";
                System.out.println("qwr=" + qwr);

  
                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"getsmstable\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"smstable\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"usernumber\":\"" + s22[0] + "\",\n"
                                + "\"bnumber\":\"" + s22[1] + "\"\n"
                                + "\"name\":\"" + s22[2] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        System.out.println("i=" + i);
                        i = i + 1;

                    }
                    ss += "\n]\n}";
                } else {
                    ss = "{\n\"command\":\"getcustomers\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("sendsms")) {
// sendsms

                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String bnumber = tools.functions.jsonget(job, "bnumber");
                System.out.println("bnumber=" + bnumber);
                String template_id = tools.functions.jsonget(job, "template_id");
                System.out.println("template_id=" + template_id);
                String smstext = tools.functions.jsonget(job, "smstext");
                System.out.println("smstext=" + smstext);

                String qwr = "insert into messages_log (usernumber,bnumber,template_id,text)values('" + usernumber + "','" + bnumber + "','" + template_id + "','" + smstext + "') returning id";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"sendsms\",\n"
                            + "\"result\":\"ok\"\n}";

                } else {
                    ss = "{\n\"command\":\"sendsms\",\n"
                            + "\"result\":\"notSaved\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("numberlist")) {
// numberlist

                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String qwr = "select subscribernumber from user2subscriber where usernumber='" + usernumber + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"numberlist\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"numberlist\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        //            ss += "{\"subscribernumber\":\"" + s22[0] + "\",\n"
                        ss += "{ \"subscribernumber\":\"" + s22[0] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        System.out.println("i=" + i);
                        i = i + 1;

                    }
                    ss += "\n]\n}";
                } else {
                    ss = "{\n\"command\":\"numberlist\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("getsmstable")) {
// getsmstable

                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String qwr = "select usernumber,bnumber,text from messages_log where usernumber='" + usernumber + "'";

                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"getsmstable\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"smstable\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"usernumber\":\"" + s22[0] + "\",\n"
                                + "\"bnumber\":\"" + s22[1] + "\"\n"
                                + "\"senddate\":\"" + s22[1] + "\"\n"
                                + "\"text\":\"" + s22[2] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        System.out.println("i=" + i);
                        i = i + 1;

                    }
                    ss += "\n]\n}";
                } else {
                    ss = "{\n\"command\":\"getsmstable\",\n"
                            + "\"result\":\"no results\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("incomingcall")) {
// icommingcall

                String anumber = tools.functions.jsonget(job, "anumber");
                System.out.println("anumber=" + anumber);
                String qwr = "select usernumber,usermobile,name from user2subscriber where subscribernumber='" + anumber + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"incomingcall\",\n"
                            + "\"result\":\"ok\",\n"
                            + "\"usernumber\":\"" + s2.get(0)[0] + "\",\n"
                            + "\"usermobile\":\"" + s2.get(0)[1] + "\",\n"
                            + "\"name\":\"" + s2.get(0)[2] + "\"\n}";

                } else {
                    ss = "{\n\"command\":\"incomingcall\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("outboundcall")) {
// outboundcall
                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String anumber = tools.functions.jsonget(job, "anumber");
                System.out.println("anumber=" + anumber);
                String name = tools.functions.jsonget(job, "name");
                System.out.println("name=" + name);
                String qwr = "insert into user2subscriber (usernumber,subscribernumber,name)values('" + usernumber + "','" + anumber + "','" + name + "') returning id";
                System.out.println("qwr=" + qwr);
                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"outboundcall\",\n"
                            + "\"result\":\"ok\",\n"
                            + "\"id\":\"" + s2.get(0)[0] + "\"\n}";
                } else {
                    ss = "{\n\"command\":\"outboundcall\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            }

        } catch (Exception e) {
            System.out.println("tryend" + e.toString());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
