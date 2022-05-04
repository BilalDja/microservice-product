package dz.bdjaghout.orderservice.service;

import dz.bdjaghout.orderservice.dto.CreateOrder;
import dz.bdjaghout.orderservice.dto.InventoryResponse;
import dz.bdjaghout.orderservice.dto.OrderLineItemsDto;
import dz.bdjaghout.orderservice.model.Order;
import dz.bdjaghout.orderservice.model.OrderLineItem;
import dz.bdjaghout.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(CreateOrder orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLines().stream().map(this::mapFromDtoToEntity).toList();
        order.setOrderLines(orderLineItems);

        List<String> skuCodes = order.getOrderLines().stream().map(OrderLineItem::getSkuCode).toList();
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if (inventoryResponses == null) {
            throw new IllegalArgumentException("Product is not in stock please try again later");
        }
        boolean allProductsAvailable = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allProductsAvailable) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock please try again later");
        }
    }

    private OrderLineItem mapFromDtoToEntity(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }
}
