//package com.sit.homeloan.serviceimpl;
//
//import com.sit.homeloan.enums.Role;
//import com.sit.homeloan.model.Customer;
//import com.sit.homeloan.model.User;
//import com.sit.homeloan.repository.CustomerRepository;
//import com.sit.homeloan.repository.UserRepository;
//import com.sit.homeloan.service.AuthService;
//
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class AuthServiceImpl implements AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Override
//    public String registerUser(User user) {
//        
//        Optional<User> existing = userRepository.findByEmail(user.getEmail());
//        if (existing.isPresent()) {
//            return "User with this email already exists.";
//        }
//
//        
//        
//        if (user.getRole() == Role.LOAN_OFFICER || user.getRole() == Role.CREDIT_MANAGER || user.getRole() == Role.DISBURSEMENT_MANAGER) {
//        	
//            boolean roleTaken = userRepository.existsByRole(user.getRole());
//            if (roleTaken) {
//                return "A user with role " + user.getRole().name() + " is already registered.";
//            }
//        }
//
//        
//        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
//        user.setPassword(hashed);
//
//        
//        User savedUser = userRepository.save(user);
//
//       
//        if (user.getRole() == Role.CUSTOMER) {
//            Customer customer = new Customer();
//            customer.setUser(user);
//            customerRepository.save(customer);
//        }
//
//        return "User registered successfully as " + user.getRole().name();
//    }
//
//    @Override
//    public String loginUser(String email, String password) {
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) {
//            return "Invalid credentials!";
//        }
//
//        User user = userOpt.get();
//
//        if (!BCrypt.checkpw(password, user.getPassword())) {
//            return "Invalid credentials!";
//        }
//
//        return "Login successful for role: " + user.getRole().name();
//    }
//}

package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.enums.Role;
import com.sit.homeloan.model.Customer;
import com.sit.homeloan.model.User;
import com.sit.homeloan.repository.CustomerRepository;
import com.sit.homeloan.repository.UserRepository;
import com.sit.homeloan.service.AuthService;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public String registerUser(User user) {

		Optional<User> existing = userRepository.findByEmail(user.getEmail());
		if (existing.isPresent()) {
			return "User with this email already exists.";
		}

		if (user.getRole() == Role.LOAN_OFFICER || user.getRole() == Role.CREDIT_MANAGER
				|| user.getRole() == Role.DISBURSEMENT_MANAGER) {

			boolean roleTaken = userRepository.existsByRole(user.getRole());
			if (roleTaken) {
				return "A user with role " + user.getRole().name() + " is already registered.";
			}
		}

		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);

		User savedUser = userRepository.save(user);

		if (user.getRole() == Role.CUSTOMER) {
			Customer customer = new Customer();
			customer.setUser(user);
			customerRepository.save(customer);
		}

		return "User registered successfully as " + user.getRole().name();
	}

	@Override
	public String loginUser(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isEmpty()) {
			return "Invalid credentials!";
		}

		User user = userOpt.get();

		if (!BCrypt.checkpw(password, user.getPassword())) {
			return "Invalid credentials!";
		}

		return "Login successful for role: " + user.getRole().name();
	}
}
