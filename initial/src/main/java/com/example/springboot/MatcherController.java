package com.example.springboot;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class MatcherController {
	Matcher matcher=new Matcher();

	@Autowired
	UserService userService;


	public static HashMap<String, String> userList = new HashMap<>();

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



	@PostMapping("/createBuyOrder/")
	public ArrayList<Order> createBuyOrder(@Valid @RequestBody Order newOrder ){
		System.out.println("It got to here");
		System.out.println(newOrder);
		matcher.validityCheck(newOrder);
		System.out.println(ResponseEntity.ok("Valid"));
		return matcher.getBuyOrder();
	}
	@PostMapping("/createSellOrder/")
	public ArrayList<Order> createSellOrder(@Valid @RequestBody Order newOrder ){
		System.out.println(newOrder);
		matcher.validityCheck(newOrder);
		return matcher.getSellOrder();
	}


	@PostMapping("/user")
	public String enterUser(@RequestBody User user) {
		//@RequestParam ("user") String username, @RequestParam ("password") String password
		System.out.println("got here");
		String token = getJWTToken(user.getUsername());
//		User user = new User();
//		user.setUsername(username);
//		user.setPassword(password);
		//user.setToken(token);
		return token;
	}


//		//createUserList();
//		List<User> UserIterable= userService.findAll();
//		for (int i = 0; i< UserIterable.size(); i++){
//			if(UserIterable.get(i).getUsername().equals(username)){
//				//currentUser.setToken();
//			return true;
//			} else if(i==UserIterable.size()-1){
//				return false;
//			}
//
//		}
//		if(userList.containsKey(currentUser.getUsername()) && currentUser.getPassword().equals(userList.get(currentUser.getUsername())) ){
//			long token=Math.round(Math.random()*(100000-4500));
//			return Long.toString((token));
//		} else return "Password doesn't match";
		//return true;


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
	public String createUser(@RequestBody User newUser){
		System.out.println(newUser);
		userService.saveOrUpdate(newUser);
		return "Users have been updated";
	}

//	@GetMapping("/getUser/{username}")
//	public User getUser(@PathVariable("username") String username) {
//		System.out.println(username);
//		//System.out.println(userService.getUserByUsername(Username));
//		return userService.getUserByUsername(username);
//	}

	@GetMapping("/getUser/")
	public ArrayList<User> getUser() {
		//System.out.println(username);
		//System.out.println(userService.getUserByUsername(Username));

		ArrayList<User> usernameList=new ArrayList<>();
		Iterable<User> UserIterable= userService.findAll();
		for (User user:UserIterable){
			System.out.println(user.getUsername());
			usernameList.add(user);
		}

		return usernameList;
	}

}
