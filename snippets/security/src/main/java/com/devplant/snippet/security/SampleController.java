package com.devplant.snippet.security;


import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private String logout = "<br><form method=POST action=/logout><button>Logout</button></form>";

    @GetMapping
    public String root(Principal principal, HttpServletResponse response) throws IOException {
        if (principal == null) {
            response.sendRedirect("/login");
            return null;
        }
        return "Check out the links: "
                + " <br><a href=/user>User Only</a>"
                + " <br><a href=/both>Both User And Admin</a>"
                + " <br><a href=/admin>Admin Only</a>"
                + " <br><a href=/who>Who Are you?</a>"
                + logout;

    }

    @GetMapping("/public")
    public String publicPath() {
        return "Anyone can access this, wanna see more?  go to <a href=/login>login</a> ";
    }

    @GetMapping("/user")
    public String userOnly() {

        return "Only a User can access this  --- go back <a href=/>back</a> or " + logout;
    }

    @GetMapping("/both")
    public String both() {
        return "User And Admin can access this  ---  go back <a href=/>back</a> or " + logout;
    }

    @GetMapping("/admin")
    public String admin() {
        return "Only admin can access this ---  go back <a href=/>back</a> or " + logout;
    }

    @GetMapping("/who")
    public String whoAmI(Principal principal) {
        return principal.getName() + " --- go back <a href=/>back</a> or " + logout;
    }


}
