package com.example.springboot;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.validation.constraints.*;

@Validated
@RestController
public class MatcherController {
	Matcher matcher = new Matcher();

	@Autowired
	UserService userService;

	@Autowired
	FirebaseService firebaseService;



	public static ArrayList<User> userList = new ArrayList<>();

//	public static void createUserList() {
//		User user1=new User("Tom", "England123");
//		User user2=new User("James", "TableChair45");
//		User user3=new User("Alex", "SkyIsBlue");
//		User user4=new User("Matt", "Barcelona12");
//		userList.put(user1.getUsername(),user1.getPassword());
//		System.out.println(userList);
//		userList.put(user2.getUsername(),user2.getPassword());
//		System.out.println(userList);
//		userList.put(user3.getUsername(),user3.getPassword());
//		System.out.println(userList);
//		userList.put(user4.getUsername(),user4.getPassword());
//		System.out.println(userList);
//	}


	@GetMapping("/getBuyOrder/")
	public ArrayList<Order> getBuyOrder() {
		return matcher.getBuyOrder();
	}

	@GetMapping("/getSellOrder/")
	public ArrayList<Order> getSellOrder() {
		return matcher.getSellOrder();
	}


	@PostMapping("/createOrder/")
	public ArrayList<Order> createBuyOrder(@RequestParam("price") @Min(5) Integer price, @RequestParam("quantity") @Min(1) Integer quantity, @RequestParam("action") @Action String action, @RequestHeader String Authorization) {

		System.out.println(price);
		System.out.println();
		for (User user : userService.findAll()) {
			System.out.println(user.getUsername());
			System.out.println(user.getToken());
			System.out.println(Authorization);
			if (user.getToken().equals(Authorization)) {
				System.out.println("it has reached this stage");
				matcher.validityCheck(new Order(user.getUsername(), price, quantity, action));
			}
		}
		return matcher.getBuyOrder();
	}


	@PostMapping("user")
	public String enterUser(@RequestParam("user") String username, @RequestParam("password") String password) {
		System.out.println("got here");
		for (User user : userService.findAll()) {
			if (user.getUsername().equals(username)) {
				String token = getJWTToken(username);
				if (user.getPassword().equals(password)) {
					System.out.println(user.getUsername() + "This is the username for the account");
					user.setToken(token);
					userService.saveOrUpdate(user);
					System.out.println(user.getToken() + "THIS IS THE TOKEN FOR THE USER");
					return token;
				} else {
					return "Incorrect username or password";
				}
			}
		}

		return "Please create an account first";
	}


	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}


	@PostMapping("/createUser")
	public String createUser(@RequestParam("username") String username, @RequestParam("password") String password) {
		System.out.println(userService.findAll());
		if (userService.findAll().size() == 0) {
			userService.saveOrUpdate(new User(username, password));
			return "New User has been created";
		} else {
			for (User user : userService.findAll()) {
				//System.out.println(user.getUsername()+"This is the user");

				if (username.equalsIgnoreCase(user.getUsername())) {
					System.out.println("Username of the loop" + ":" + user.getUsername());

					return "Username already taken";
				} else {
					System.out.println("Got here");
					userService.saveOrUpdate(new User(username, password));

					return "New User has been created";
				}
			}
		}

		return "Error something has gone wrong";
	}


	@GetMapping("/getUser/")
	public ArrayList<User> getUser() {
		ArrayList<User> usernameList = new ArrayList<>();
		Iterable<User> UserIterable = userService.findAll();
		for (User user : UserIterable) {
			System.out.println(user.getUsername());
			usernameList.add(user);
		}

		return usernameList;
	}

	@PostMapping("/privateOrders/")
	public ArrayList<PrivateOrder> privateOrders(@RequestHeader String Authorization) {
		for (User user : userService.findAll()) {
			if (user.getToken().equals(Authorization)) {
				System.out.println("it has reached this stage");
				matcher.privateOrders(user.getUsername());
			}
		}
		return matcher.getPrivateOrders();
	}


	@PostMapping("/firebaseCreateOrder/")
	public void createOrderFirebase(@RequestBody  Order order) throws ExecutionException, InterruptedException {
		//System.out.println("recieved in the controller");
		matcher.validityCheck(order);
		System.out.println(matcher.getBuyOrder()+"This is a list of buy orders");
		firebaseService.postArray(matcher.getBuyOrder());
		//return firebaseService.postArray(order);
	}

	@GetMapping("/getOrderFirebase/")
	public void getOrderFirebase(@RequestParam("name") String name) throws ExecutionException, InterruptedException {
		//return firebaseService.getOrder(name);
		firebaseService.loop(name);

	}
}



