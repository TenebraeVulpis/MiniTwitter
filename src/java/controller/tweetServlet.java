/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import business.Tweet;
import business.User;
import business.UserTweetInfo;
import business.UserMention;
import dataaccess.TweetDB;
import dataaccess.UserDB;
import dataaccess.UserMentionDB;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Nic
 */
public class tweetServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet tweetServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet tweetServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
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
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        // Test if there is a user signed in.
        User user = (User) session.getAttribute("user");
        // TO DO GET USER NUMBER OF TWEETS
        int numberOfTweets = 0;
        OutputStream o;
        
        
        String url = "/login.jsp";
        
        // If no user go to login
        if (user == null){
            url = "/login.jsp";
            action = "no_user";
        } else if (action == null) {
            // If user but no action go home
            url = "/home.jsp";
        } else if(action.equals("get_tweets")) {            
            ArrayList<UserTweetInfo> tweets;
            String email = user.getemail();
            tweets = TweetDB.selectTweetsByUser(email);
            numberOfTweets = TweetDB.numberOfUserTweets(user);
            
            session.setAttribute("tweets", tweets);
            session.setAttribute("numberOfTweets", numberOfTweets);
            
            url = "/home.jsp";
        }                
        //get user's profile picture from User object
        else if(action.equals("get_image")) {
            // Note this is inefficient!
            String email = request.getParameter("email");
            ArrayList<User> users = (ArrayList<User>) session.getAttribute("users");
            
            for(User u : users) {
                if(email.equals(u.getemail())) {
                    if(u.getphoto() != null) {
                        response.setContentType("image/*");
                        InputStream iStream = u.getphoto();
                        byte[] bPhoto = IOUtils.toByteArray(iStream); // need byte type for outstream
                        o = response.getOutputStream();
                        o.write(bPhoto);
                        o.flush();
                        o.close();
                        iStream.reset(); //important. Inputstream values don't reset and will
                                        //not pass the values to bPhoto after the 1st photo.
                        break;
                    }
                }   
            }
        }
        
        else if(action.equals("get_images")) {
            String email = (String)session.getAttribute("user_email");
            
            ArrayList<User> users = (ArrayList<User>) session.getAttribute("users");
            InputStream iStream;
            
            
            for(int i = 0; i <users.size(); i++){
                if(users.get(i).getemail().equals(email)) {
                    iStream = users.get(i).getphoto();
                    response.setContentType("image/*");
                    byte[] bPhoto = IOUtils.toByteArray(iStream);
                    o = response.getOutputStream();
                    o.write(bPhoto);
                    o.flush();
                    o.close();
                    iStream.reset();
                    session.removeAttribute("user_email");
                }
            }
            

            
        }
                
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
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
        HttpSession session = request.getSession();
        
        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");

        String url = "/home.jsp";
        
        if(action.equals("post_tweet")) {
            String composerEmail = user.getemail();
            String message = request.getParameter("user_tweet");
            ArrayList<String> mentions = null; //ArrayList used if more than one user is mentioned. 
            UserMention userMention = new UserMention();
            
            //find usernames that are mentioned. Returned values are the email addresses of usernames.
            mentions = findMentions(message);
            //build tweet object
            Tweet tweet = new Tweet();
            tweet.setcomposerEmail(composerEmail);
            tweet.setMessage(message);
            
            //set boolean value mentions
            if(!mentions.isEmpty())
                tweet.setMentions(true);
            
            try {
                TweetDB.insert(tweet);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<UserTweetInfo> tweets;
            user = (User) session.getAttribute("user");
            String email = user.getemail();
            tweets = TweetDB.selectTweetsByUser(email);
            
            //If mentions, insert userMention with user tweetID
            if(!mentions.isEmpty()){
                int tweetid = tweets.get(0).gettweetid();
                userMention.setemailAddress(email);
                userMention.settweetid(tweetid);
                for(int i = 0; i < mentions.size(); i++)
                {
                    userMention.setuserMentionedEmail(mentions.get(i));
                    
                    try {
                        UserMentionDB.insert(userMention);
                    }catch (ClassNotFoundException ex) {
                        Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE,null,ex);
                    }catch (Exception ex) {
                        Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE,null,ex);
                    }
                }
            }
            session.setAttribute("tweets", tweets);
        }
        if(action.equals("delete_tweet")){
            // Get the id of this tweet from the request
            String tweetID = request.getParameter("tweetID");
            ArrayList<UserTweetInfo> tweets;
            user = (User) session.getAttribute("user");
            String email = user.getemail();
            boolean mentions = false;
            tweets = TweetDB.selectTweetsByUser(email);
            
            //check for user mentions
            for(int i = 0; i < tweets.size(); i++) {
                if(tweets.get(i).gettweetid() == Integer.parseInt(tweetID)) {
                    if(tweets.get(i).getmentions() != null) {
                        mentions = true;
                    }
                }
            }
            
            //if there are user mentions, delete mentions
            if(mentions){
                try {
                        UserMentionDB.delete(tweetID);
                    }catch (ClassNotFoundException ex) {
                        Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE,null,ex);
                    }catch (Exception ex) {
                        Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE,null,ex);
                    }
            }

            try {
                TweetDB.delete(tweetID);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(tweetServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            tweets.clear(); //clear tweets array to update
            tweets = TweetDB.selectTweetsByUser(email);
            session.setAttribute("tweets", tweets);
        }
        
        int numberOfTweets = TweetDB.numberOfUserTweets(user);
        session.setAttribute("numberOfTweets", numberOfTweets);
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
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

    // <editor-fold defaultstate="collapsed" desc="User methods. Click on the + sign on the left to edit the code.">
    /*****************************************************************
     *                     findMentions()                            *
     *****************************************************************
     * Helper function for the post tweet action. A tweet is checked *
     * for user mentions. If a user mention is found, the username is* 
     * checked against the user database. If the username is valid,  * 
     * the user mention is added to the userNames array list. Returns*
     * an ArrayList of valid usernames that are mentioned in a tweet.*                                     *
     *                                                               *
     * @param String text a tweet                                    *
     * @return ArrayList usernames mentioned                         *
     *****************************************************************/
    private ArrayList<String> findMentions(String text){
       int i = 0;
       ArrayList<String> userNames = new ArrayList<String>();
       ArrayList<User> users = new ArrayList<User>();
       String temp = null;
       text += " "; //adds a space to the end of text for function below.
       
       users = UserDB.selectUsers(); //all users in the database
       while(true)
       {
           int found = text.indexOf("@", i);
           if (found == -1) break;
           int start = found + 1;
           int end = text.indexOf(" ", start);
           temp = text.substring(start, end);
           for (int count=0; count < users.size(); count++) {
               if (users.get(count).getusername().equals(temp)){
                   userNames.add(users.get(count).getemail()); //add email address instead of username
               }
           }
           i= end +1;
       }
        
        return userNames;
    }

}
