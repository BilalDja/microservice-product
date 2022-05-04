package dz.bdjaghout.orderservice.controller;

import dz.bdjaghout.orderservice.dto.CreateOrder;
import dz.bdjaghout.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody CreateOrder orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }

}
