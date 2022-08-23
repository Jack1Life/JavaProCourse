package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.List;

@Controller
public class MyController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public MyController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index(Model model){
        User user = getCurrentUser();

        String login = user.getUsername();
        CustomUser dbUser = userService.findByLogin(login);

        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin",     hasRole(user, "ROLE_ADMIN"));
        model.addAttribute("moderator", hasRole(user, "ROLE_MODERATOR"));
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());
        model.addAttribute("address", dbUser.getAddress());

        return "index";
    }

    @PostMapping(value = "/update")
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address) {
        User user = getCurrentUser();

        String login = user.getUsername();
        userService.updateUser(login, email, phone, address);

        return "redirect:/";
    }

    @PostMapping(value = "/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                         Model model) {
        String passHash = passwordEncoder.encode(password);
        if ( ! userService.addUser(login, passHash, UserRole.USER, email, phone, address)) {
            model.addAttribute("exists", true);
            model.addAttribute("login", login);
            return "register";
        }

        return "redirect:/";
    }

    @PostMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String register() {
        return "register";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MODERATOR')") // !!!
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model){
        User user = getCurrentUser();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    // ----

    private User getCurrentUser() {
        return (User)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean hasRole(User user, String role) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if (role.equals(auth.getAuthority()))
                return true;
        }

        return false;
    }
    private boolean isAdmin(User user) {
        return hasRole(user, "ROLE_ADMIN");
    }
}
