package com.example.Smart.Controller;

import com.example.Smart.Dao.ContactRepository;
import com.example.Smart.Dao.OrderRepository;
import com.example.Smart.Dao.UserRepository;
import com.example.Smart.Entity.Contact;
import com.example.Smart.Entity.User;
import com.example.Smart.Helper.Message;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void AddCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("user name " + userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("User-> " + user);
        model.addAttribute("user", user);

    }

    @RequestMapping("/index")
    public ModelAndView dashboard(Model model, Principal principal) {
        //return "";
        return new ModelAndView("normal/user_dashboard");
    }

    @GetMapping("/add-contact")
    public ModelAndView addContact(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return new ModelAndView("normal/add-contact");
    }

    @RequestMapping(value = "/process-contact", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView processContact(@ModelAttribute Contact contact,
                                       HttpSession session,
                                       Principal principal) {
        try {

            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
//
            user.getContacts().add(contact);
            contact.setUser(user);

            this.userRepository.save(user);
            System.out.println("Added");
            System.out.println("Data " + contact);
            session.setAttribute("message", new Message("Contact added Successfully..!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong..!", "danger"));
        }
        return new ModelAndView("redirect:/user/show-contacts/0");
    }

    @GetMapping("/show-contacts/{page}")
    public ModelAndView showContact(@PathVariable("page") int page, Model model, Principal principal) {
        model.addAttribute("title", "View Contacts");
        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());


        return new ModelAndView("normal/show_contact");
    }

    @GetMapping("/delete/{c_id}")
    public ModelAndView deleteContact(@PathVariable("c_id") int c_id, HttpSession session) {
        // Optional<Contact> optionalContact= this.contactRepository.findById(cId);
        System.out.println("id= " + c_id);
        contactRepository.deleteById(c_id);
       /* Contact contact = this.contactRepository.findById(cId).orElse(null);
        if (contact != null) {
            this.contactRepository.delete(contact);
        }*/
        session.setAttribute("message", new Message("Contact Deleted Successfully..!", "success"));

        return new ModelAndView("redirect:/user/show-contacts/0");
    }

    //open update form
    @RequestMapping(value = "/update-contact/{c_id}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateContact(@PathVariable("c_id") int c_id, Model model) {
        model.addAttribute("title", "Update Contact");
        Contact contact = this.contactRepository.findById(c_id).get();
        model.addAttribute("contact", contact);
        return new ModelAndView("normal/update-form");
    }

    //update contact handler
    @RequestMapping(value = "/process-update", method = RequestMethod.POST)
    public ModelAndView updateHandler(@ModelAttribute Contact contact, Principal principal, HttpSession session) {
        try {
            // Contact old = this.contactRepository.findById(contact.getcId()).get();
            User user = this.userRepository.getUserByUserName(principal.getName());
            System.out.println("userrr" + user);
            contact.setUser(user);
            System.out.println("coontacct: " + contact.getName());
            /*Contact contact = new Contact();
            contact.setName(contact1.getName());
            contact.setNickName(contact1.getNickName());
            contact.setEmail(contact1.getEmail());
            contact.setPhone(contact1.getPhone());
            contact.setDescription(contact1.getDescription());
            contact.setImage(contact1.getImage());
            contact.setWork(contact1.getWork());*/
            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Updated Successfully..!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("id " + contact.getc_id());
        return new ModelAndView("redirect:/user/show-contacts/0");
    }

    //payment integration

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws Exception {
        //System.out.println("Order created successfully");
        System.out.println(data);
        int amt = Integer.parseInt(data.get("amount").toString());
        var razorpayClient = new RazorpayClient("rzp_test_ZuqHK4IeVlDUDQ", "DO0CwyDvIXJpT0JFyY8m5Gbr");

        JSONObject object = new JSONObject();
        object.put("amount", amt * 100);
        object.put("currency", "INR");
        object.put("receipt", "txn_12345");

        //creating order

        Order order = razorpayClient.Orders.create(object);
        System.out.println(order);

        com.example.Smart.Entity.Order ord = new com.example.Smart.Entity.Order();
        ord.setAmount(order.get("amount") + "");
        ord.setOrderId(order.get("id"));
        ord.setPayment_id(null);
        ord.setStatus("created");
        ord.setUser(this.userRepository.getUserByUserName(principal.getName()));
        ord.setReceipt(order.get("receipt"));
        this.orderRepository.save(ord);
        return order.toString();
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
        com.example.Smart.Entity.Order order = this.orderRepository.findByOrderId(data.get("order_id").toString());
        order.setPayment_id(data.get("payment_id").toString());
        order.setStatus(data.get("status").toString());
        this.orderRepository.save(order);
        System.out.println(data);
        return ResponseEntity.ok(Map.of("msg", "updated"));
    }

    @GetMapping("/setting")
    public ModelAndView setting() {
        return new ModelAndView("normal/settings");
    }

    @PostMapping("/change-password")
    public ModelAndView changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
        System.out.println("old" + oldPassword);
        System.out.println("new" + newPassword);

        String currentUser = principal.getName();
        User userByUserName = this.userRepository.getUserByUserName(currentUser);
        System.out.println(userByUserName.getPassword());

        if (this.bCryptPasswordEncoder.matches(oldPassword, userByUserName.getPassword())) {
            userByUserName.setPassword(bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(userByUserName);
            session.setAttribute("message", new Message("Password has been changed", "success"));
        } else {
            session.setAttribute("message", new Message("Enter correct old password", "danger"));
            return new ModelAndView("redirect:/user/setting");
        }
        return new ModelAndView("redirect:/user/index");
    }
}
