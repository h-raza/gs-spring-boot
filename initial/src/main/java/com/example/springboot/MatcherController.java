package com.example.springboot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;


@RestController
public class MatcherController {
	Matcher matcher=new Matcher();
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

}
