package com.example.springboot.model;
import com.example.springboot.Matcher;
import com.example.springboot.MatcherController;
import com.example.springboot.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MatcherController.class)

public class MatcherTest {

    Matcher matcher= new Matcher();

    @Test
    public void validityCheck() {
        Order order=matcher.createOrder("Hassan",5245,545,"Buy");
        assertEquals(true,matcher.validityCheck(order));

        Order order1=matcher.createOrder("Hassan",-5245,545,"Buy");
        assertEquals(false,matcher.validityCheck(order1));

        Order order2=matcher.createOrder("Hassan",-5245,545,"Hello");
        assertEquals(false,matcher.validityCheck(order2));
    }

    @Test
    public void orderPush() {
        Order order=matcher.createOrder("Hassan",5245,545,"Buy");
        matcher.validityCheck(order);
        assertEquals("Hassan",matcher.getBuyOrder().get(0).account);
        assertEquals(5245,matcher.getBuyOrder().get(0).price);
        assertEquals(545,matcher.getBuyOrder().get(0).quantity);
        assertEquals("Buy",matcher.getBuyOrder().get(0).action);
    }

    @Test
    public void orderMatch() {
        Order order=matcher.createOrder("Hassan",5245,545,"Buy");
        matcher.validityCheck(order);
        Order order1=matcher.createOrder("Raza",5245,545,"Sell");
        matcher.validityCheck(order1);
        assertEquals(0,matcher.getBuyOrder().size());

    }

    @Test
    public void sortMatch() {
        Order order=matcher.createOrder("Hassan",5245,545,"Buy");
        matcher.validityCheck(order);
        Order order1=matcher.createOrder("Raza",20,545,"Buy");
        matcher.validityCheck(order1);
        matcher.sortBuy();
        assertEquals(20,matcher.getBuyOrder().get(0).price);

    }
    @Test
    public void aggBuyTest() {
        Order order=matcher.createOrder("Hassan",20,500,"Buy");
        matcher.validityCheck(order);
        Order order1=matcher.createOrder("Raza",20,500,"Buy");
        matcher.validityCheck(order1);
        Order order3=matcher.createOrder("Hassan",50,500,"Buy");
        matcher.validityCheck(order3);
        Order order4=matcher.createOrder("Raza",50,500,"Buy");
        matcher.validityCheck(order4);
        matcher.sortBuy();
        matcher.aggBuy();

         assertEquals(1000,matcher.getAggBuyList().get(20),1);

    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Order order;

    @Test
    public void validAccount() throws Exception{
        Order newOrder=new Order("Hassan",250,250,"Buy");
        mockMvc.perform(post("/createBuyOrders/")
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());
    }



}