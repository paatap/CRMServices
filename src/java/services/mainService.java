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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

            if (command.equals("missed")) {
// missed
                String anumber = tools.functions.jsonget(job, "anumber");
                System.out.println("anumber=" + anumber);
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String qwr = "insert into missedcalls (anumber,agentname)values('" + anumber + "','" + agent + "') returning id";
                System.out.println("qwr=" + qwr);
                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"missed\",\n"
                            + "\"result\":\"ok\",\n"
                            + "\"id\":\"" + s2.get(0)[0] + "\"\n}";
                } else {
                    ss = "{\n\"command\":\"missed\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("missedcalls")) {
// missedcalllist

                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);

                String qwr = "select id,agentname,usernumber,anumber,startdate from missedcalls where agentname='" + agent + "' order by startdate";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"missedcalls\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"missedcalls\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"id\":\"" + s22[0] + "\",\n"
                                ////////////////////////////////
                                + "\"anumber\":\"" + s22[3] + "\",\n"
                                + "\"date\":\"" + s22[4] + "\"\n}";
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
                    ss = "{\n\"command\":\"missedcalls\",\n"
                            + "\"result\":\"nocalls\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("smstemplates")) {
// smstemplate

                String qwr = "select id,messagetxt from messagetemplate where activem=true order by id";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"smstemplates\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"smstemplates\": [\n";
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
                    ss = "{\n\"command\":\"smstemplates\",\n"
                            + "\"result\":\"noTemplates\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("addtemplate")) {
// addtemplate

                String text = tools.functions.jsonget(job, "text");
                System.out.println("text=" + text);
                String qwr = "insert into  messagetemplate (messagetxt) values ('" + text + "')returning id";
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
                String qwr = "update  messagetemplate set messagetxt= '" + text + "' where id=" + id + "returning id";
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
                String qwr = "update  messagetemplate set activem= false where id=" + id + "returning id";
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
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String saveusernumber = tools.functions.jsonget(job, "saveusernumber");
                System.out.println("saveusernumber=" + saveusernumber);
                String saveagent = tools.functions.jsonget(job, "saveagent");
                System.out.println("saveagent=" + saveagent);
                String saveagentmobile = tools.functions.jsonget(job, "saveagentmobile");
                System.out.println("saveagentmobile=" + saveagentmobile);
                String savecustomernumber = tools.functions.jsonget(job, "savecustomernumber");
                System.out.println("savecustomernumber=" + savecustomernumber);

                //               String qwr = "update user2subscriber set usernumber='" + saveusernumber + "',agentname='" + saveagent + "', usermobile='" +saveagentmobile+"' where subscribernumber='" + savecustomernumber + "'returning id";
                String qwr = "update user2subscriber set agentname='" + saveagent + "' where subscribernumber='" + savecustomernumber + "'returning id";
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
            } else if (command.equals("addagent")) {
// addagent

                String agentname = tools.functions.jsonget(job, "saveagent");
                System.out.println("agentname=" + agentname);
                String agentmobile = tools.functions.jsonget(job, "saveagentmobile");
                System.out.println("agentmobile=" + agentmobile);
                String qwr = "insert into  agents (agentname,agentmobile) values ('" + agentname + "','" + agentmobile + "')returning agentname";
                System.out.println("qwr=" + qwr);
                String ss;

                try {
                    ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);

                    if (s2.size() > 0) {
                        ss = "{\n\"command\":\"addagent\",\n"
                                + "\"result\":\"ok\"\n}";

                    } else {
                        ss = "{\n\"command\":\"addagent\",\n"
                                + "\"result\":\"notsaved\"\n}";
                    }
                    System.out.println("ss=" + ss);
                } catch (Exception e) {
                    ss = "{\n\"command\":\"addagent\",\n"
                            + "\"result\":\"notsaved\"\n}";

                }
                response.getWriter().write(ss);
            } else if (command.equals("changeagent")) {
// changeagent

                String agentname = tools.functions.jsonget(job, "saveagent");
                System.out.println("agentname=" + agentname);
                String agentmobile = tools.functions.jsonget(job, "saveagentmobile");
                System.out.println("agentmobile=" + agentmobile);
                String qwr = "update  agents set agentmobile= '" + agentmobile + "' where agentname='" + agentname + "' returning agentname";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"changeagent\",\n"
                            + "\"result\":\"ok\"\n}";
                } else {
                    ss = "{\n\"command\":\"changeagent\",\n"
                            + "\"result\":\"agentnotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("deleteagent")) {
// deleteagent

                String agentname = tools.functions.jsonget(job, "saveagent");
                System.out.println("agentname=" + agentname);
                String qwr = "update  agents set agentactive= false where agentname='" + agentname + "' returning agentname";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"deleteagent\",\n"
                            + "\"result\":\"ok\"\n}";
                } else {
                    ss = "{\n\"command\":\"deleteagent\",\n"
                            + "\"result\":\"agentnamenotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("getagents")) {
// getagents

                String qwr = "select agentname,agentmobile from agents where agentactive=true order by agentname";
                //  + "-- where agentname='" + agent + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"getagents\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"table\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"agentname\":\"" + s22[0] + "\",\n"
                                + "\"agentmobile\":\"" + s22[1] + "\"\n}";
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
                    ss = "{\n\"command\":\"getagents\",\n"
                            + "\"result\":\"noagents\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("findcustomer")) {
// findcustomer

                String subscribernumber = tools.functions.jsonget(job, "savecustomernumber");
                System.out.println("subscribernumber=" + subscribernumber);

                String qwr = "select subscribernumber,agentname from user2subscriber where subscribernumber='" + subscribernumber + "'";
                //  + "-- where agentname='" + agent + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"findcustomer\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + "\"subscribernumber\":\"" + s2.get(0)[0] + "\",\n"
                            + "\"agentname\":\"" + s2.get(0)[1] + "\"\n}";

                
                } else {
                    ss = "{\n\"command\":\"findcustomer\",\n"
                            + "\"result\":\"usernotfound\"\n}";
                }
                System.out.println("ss=" + ss);
                response.getWriter().write(ss);
            } else if (command.equals("getcustomers")) {
// getcustomers

                String usernumber = tools.functions.jsonget(job, "usernumber");
                System.out.println("usernumber=" + usernumber);
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String qwr = "select usernumber,subscribernumber,agentname,usermobile from user2subscriber order by subscribernumber";
                //  + "-- where agentname='" + agent + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"getcustomers\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"table\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"usernumber\":\"" + s22[0] + "\",\n"
                                + "\"bnumber\":\"" + s22[1] + "\",\n"
                                + "\"name\":\"" + s22[2] + "\",\n"
                                + "\"namemobile\":\"" + s22[3] + "\"\n}";
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
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String template_id = tools.functions.jsonget(job, "template_id");
                System.out.println("template_id=" + template_id);
                if (template_id == null || template_id.equals("")) {
                    template_id = "-1";
                }

                String smstext = tools.functions.jsonget(job, "smstext");
                System.out.println("smstext=" + smstext);

                ///// send sms
                String ob = null;
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(formatter.format(date));
//                String ddt=date.toString();
                String ddt = formatter.format(date);
                String smsbodytext = "{\n"
                        + "\"Body\":  \"" + smstext + "\",\n"
                        + "    \"SourceInfo\":  {\n"
                        + "                       \"Date\":  \"" + ddt + "\"\n"
                        + "                   },\n"
                        + "    \"MessageType\":  \"SMS\",\n"
                        + "    \"Subject\":  \"smssubject\",\n"
                        + "    \"System\":  \"CiscoMedical\",\n"
                        + "    \"Recipients\":  \"" + bnumber + "\"\n"
                        + "}";
                System.out.println("smsbodytext=" + smsbodytext);
                ob = tools.SendSMS.getResult(smsbodytext, request);

                if (ob.equals("\"Ok\"")) {

                    String qwr = "insert into messages_log (usernumber,bnumber,template_id,text,agentname)values('" + usernumber + "','" + bnumber + "','" + template_id + "','" + smstext + "','" + agent + "') returning id";
                    System.out.println("qwr=" + qwr);

                    String s2 = tools.functions.getResult2(qwr, "", "", tools.functions.isnewcompare).toString();

                    String ss = s2;

//                    if (s2.size() > 0) {
//
//                    } else {
//                        ss = "{\n\"command\":\"sendsms\",\n"
//                                + "\"result\":\"Message Do Not Sent\"\n}";
//                    }
                    System.out.println("ss=" + ss);
                    ss = "{\n\"command\":\"sendsms\",\n"
                            + "\"result\":\"ok\"\n}";
                    response.getWriter().write(ss);

                } else {
                    String ss = "{\n\"command\":\"sendsms\",\n"
                            + "\"result\":\"Message Do Not Sent\"\n}";
                    System.out.println("ss=" + ss);
                    response.getWriter().write(ss);
                }
            } else if (command.equals("numberlist")) {
// numberlist

                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String qwr = "select subscribernumber from user2subscriber where agentname='" + agent + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss = "{\n\"command\":\"numberlist\",\n";
                if (s2.size() > 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"numberlist\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        //            ss += "{\"subscribernumber\":\"" + s22[0] + "\",\n"
                        ss += "{ \"anumber\":\"" + s22[0] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        //    System.out.println("i=" + i);
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
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("usernumber=" + usernumber);
                String qwr = "select bnumber,text,date_trunc('second',senddate),agentname from messages_log where agentname='" + agent + "' order by senddate desc limit 20";

                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);

                String ss = "{\n\"command\":\"getsmstable\",\n";
                if (s2.size() >= 0) {
                    ss += "\"result\":\"ok\",\n "
                            + " \"smstable\": [\n";
                    int i = 1;
                    for (String[] s22 : s2) {

                        ss += "{\"bnumber\":\"" + s22[0] + "\",\n"
                                + "\"txt\":\"" + s22[1] + "\",\n"
                                + "\"agent\":\"" + s22[3] + "\",\n"
                                + "\"senddate\":\"" + s22[2] + "\"\n}";
                        if (i < s2.size()) {
                            ss += ",\n";
//                            System.out.println("Kuku=" + s22.length);
//                            System.out.println("i=" + i);
                        }
                        //                System.out.println("i=" + i);
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
                String qwr = "select a.agentmobile,u.agentname from user2subscriber u   left join agents a on u.agentname=a.agentname where subscribernumber='" + anumber + "'";
                System.out.println("qwr=" + qwr);

                ArrayList<String[]> s2 = tools.functions.getResult(qwr, tools.functions.isnewcompare);
                String ss;
                if (s2.size() > 0) {
                    ss = "{\n\"command\":\"incomingcall\",\n"
                            + "\"result\":\"ok\",\n"
                            + "\"usernumber\":\"" + s2.get(0)[0] + "\",\n"
                            + "\"agent\":\"" + s2.get(0)[1] + "\"\n}";

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
                String agent = tools.functions.jsonget(job, "agent");
                System.out.println("agent=" + agent);
                String qwr = "insert into user2subscriber (usernumber,subscribernumber,agentname)values('" + usernumber + "','" + anumber + "','" + agent + "') returning id";
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
