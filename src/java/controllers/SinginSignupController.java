/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import api.UserAPI;
import api.UserStore;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import models.User;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Suguru
 */
@Controller
public class SinginSignupController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-@#$%^&+=])(?=\\S+$).{8,}$");
    public static final Pattern VALID_USERNAME_REGEX = Pattern.compile("^(?=\\S+$).{6,}$");

    @Autowired
    private UserAPI userAPI;
    @Autowired
    private UserStore userStore;
    private User tempUser;
    private List<User> tempUserList;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showSingup() {
        return "signupSignin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String showSingin() {
        return "signupSignin";
    }

    @RequestMapping(value = "/processsignup", method = RequestMethod.POST)
    public String processSignup(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("signupPassword") String password, @RequestParam("confPassword") String confPassword, Model model) throws ParseException, IOException {
        username = username.trim();
        email = email.trim();

        String signupErrorMsg = "";
        boolean isFilled = !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confPassword.isEmpty();

        if (!isFilled) {
            signupErrorMsg = "Fill the blanks";
            model.addAttribute("signupErrorMsg", signupErrorMsg);
            return "signupSignin";
        }

        boolean isGoodEmail = this.isGoodEmail(email);
        boolean isGoodUsername = this.isGoodUsername(username);
        boolean isGoodPassword = this.isGoodPassword(password);
        boolean passConfPassMatch = password.equals(confPassword);

        if (isGoodEmail && isGoodUsername && isGoodPassword && passConfPassMatch) {
            tempUser = new User(username.toLowerCase(), email.toLowerCase(), password);
            userAPI.postUser(tempUser);
            return this.showSignupSuccess(tempUser, model);
        } else {
            if (!isGoodEmail) {
                signupErrorMsg += "Invalid email or already in use<br>";
            }
            if (!isGoodUsername) {
                signupErrorMsg += "Username already in use<br>";
            }
            if (!isGoodPassword) {
                signupErrorMsg += "Invalid password<br>";
            }
            if (!passConfPassMatch) {
                signupErrorMsg += "Password and confirm password don't match";
            }
        }
        model.addAttribute("signupErrorMsg", signupErrorMsg);
        return "signupSignin";
    }

    private String showSignupSuccess(User user, Model model) {
        model.addAttribute("user", user);
        return "signupSuccess";
    }

    @RequestMapping(value = "/processsignin", method = RequestMethod.POST)
    public String processSignin(@RequestParam("userid") String userid, @RequestParam("signinPassword") String password, Model model, HttpSession session) throws ParseException, IOException {
        if (session == null) {
            System.out.println("session is null");
        } else {
            System.out.println("session is not null");
        }
        String signinErrorMsg = "";
        boolean isFilled = !userid.isEmpty() && !password.isEmpty();
        if (!isFilled) {
            signinErrorMsg = "Fill blanks";
            model.addAttribute("signinErrorMsg", signinErrorMsg);
            return "signupSignin";
        } else {
            boolean isLoggedin = isLoggedin(userid, password);

            if (isLoggedin) {
                tempUser = this.getUser(userid, password);
                // put the user id into session. This value will be used everywhere.
                session.setAttribute("username", tempUser.getUsername());
                // goes to index
                return "redirect:/";
            } else {
                signinErrorMsg = "User name and password don't match. Try Again.";
                model.addAttribute("signinErrorMsg", signinErrorMsg);
                return "signupSignin";
            }
        }
    }

    private boolean isGoodPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }

    private boolean isGoodEmail(String email) throws ParseException, IOException {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        // also needs to check if the email is in use already.
        tempUserList = userStore.getAllUsers();
        boolean retVal = true;
        Iterator<User> it = tempUserList.iterator();
        while (it.hasNext()) {
            tempUser = it.next();
            if (tempUser.getEmail().equals(email)) {
                retVal = false;
                break;
            }
        }
        return matcher.find() && retVal;
    }

    private boolean isGoodUsername(String username) {
        Matcher matcher = VALID_USERNAME_REGEX.matcher(username);
        // also needs to check if the username is in use already.
        boolean retVal = true;
        Iterator<User> it = tempUserList.iterator();
        while (it.hasNext()) {
            tempUser = it.next();
            if (tempUser.getUsername().toLowerCase().equals(username.toLowerCase())) {
                retVal = false;
                break;
            }
        }
        return matcher.find() && retVal;
    }

    private boolean isLoggedin(String userid, String password) throws ParseException, IOException {
        tempUserList = userStore.getAllUsers();
        Iterator<User> it = tempUserList.iterator();
        boolean emailMatches = false;
        boolean usernameMatches = false;
        boolean passwordMatches = false;
        boolean loggedin = false;
        while (it.hasNext()) {
            tempUser = it.next();
            emailMatches = tempUser.getEmail().equalsIgnoreCase(userid);
            usernameMatches = tempUser.getUsername().equalsIgnoreCase(userid);
            passwordMatches = tempUser.getPassword().equals(password);
            if ((emailMatches || usernameMatches) && passwordMatches) {
                loggedin = (emailMatches || usernameMatches) && passwordMatches;
                break;
            }
        }
        return loggedin;
    }

    private User getUser(String email, String password) {
        Iterator<User> it = tempUserList.iterator();
        while (it.hasNext()) {
            tempUser = it.next();
            if (tempUser.getUsername().equalsIgnoreCase(email) && tempUser.getPassword().equals(password)) {
                return tempUser;
            }
        }
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    private String logout(HttpSession session) {
        session.setAttribute("username", null);
        return "redirect:/";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    private String getProfile(Model model, HttpSession session) throws ParseException, IOException {
        if (session.getAttribute("username") == null) {
            return "redirect:/";
        } else {
            String username = (String) session.getAttribute("username");
            tempUser = userStore.getUserForUsername(username);
            model.addAttribute("user", tempUser);
            return "profile";
        }
    }

    @RequestMapping(value = "/changeuserinfo", method = RequestMethod.POST)
    public String changeUserInfo(@RequestParam("username") String username, @RequestParam("email") String email, Model model, HttpSession session) throws ParseException, IOException {
        if (session.getAttribute("username") == null) {
            return "redirect:/";
        } else {
            if (username.isEmpty() && email.isEmpty()) {
                model.addAttribute("errorMsg", "Fill either username or email");
            } else {
                boolean isGoodEmail = this.isGoodEmail(email);
                if (!isGoodEmail) {
                    model.addAttribute("userInfoMsg", "Email is invalid");
                    return this.getProfile(model, session);
                }
                tempUser = userStore.getUserForUsername(username);
                if (tempUser.getUsername().equalsIgnoreCase(username) && tempUser.getEmail().equalsIgnoreCase(email)) {
                    model.addAttribute("userInfoMsg", "No change in both fields.");
                    return this.getProfile(model, session);
                }
                if (username.isEmpty()) {
                    tempUser.setEmail(email.toLowerCase());
                } else if (email.isEmpty()) {
                    tempUser.setUsername(username.toLowerCase());
                } else if (!email.isEmpty() && !username.isEmpty()) {
                    tempUser.setEmail(email.toLowerCase());
                    tempUser.setUsername(username.toLowerCase());
                }
                userAPI.putUser(tempUser, tempUser.getId());
                model.addAttribute("userInfoMsg", "User info updated");
            }
        }
        model.addAttribute("username", username);
        return this.getProfile(model, session);
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("newConfPassword") String newConfPassword, Model model, HttpSession session) throws ParseException, IOException {
        String username = null;
        if (session.getAttribute("username") == null) {
            return "redirect:/";
        } else {
            if (oldPassword.isEmpty() || newPassword.isEmpty() || newConfPassword.isEmpty()) {
                model.addAttribute("passMsg", "Fill password blanks");
            } else {
                username = (String) session.getAttribute("username");
                tempUser = userStore.getUserForUsername(username);
                if (!tempUser.getPassword().equals(oldPassword)) {
                    model.addAttribute("passMsg", "Old password doesn't match");
                } else {
                    if (!newPassword.equals(newConfPassword)) {
                        model.addAttribute("passMsg", "New password and confirmation password do not match");
                    } else {
                        tempUser.setPassword(newPassword);
                        userAPI.putUser(tempUser, tempUser.getId());
                        model.addAttribute("passMsg", "Updated password");
                    }
                }
            }
            model.addAttribute("username", username);
            return this.getProfile(model, session);
        }
    }
    
}
