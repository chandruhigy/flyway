
package com.example.order.controller;

import com.example.order.entity.Order;
import com.example.order.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository repository;


    @GetMapping("/order")
    public String getOrderNameById(@RequestParam("id") Long id){

        Optional<Order> order = repository.findById(id);
        return order.get().getName();

    }

    @PostMapping("/order")
    public Boolean saveOrders(){
       Order order = new Order();
       order.setName(Math.random()+"");
       repository.save(order);
       return true;
    }
}
