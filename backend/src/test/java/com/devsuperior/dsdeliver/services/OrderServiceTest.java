package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.entities.Order;
import com.devsuperior.dsdeliver.entities.OrderStatus;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    OrderService orderService = new OrderService(orderRepository, productRepository);

    List<Product> listProducts;
    List<Order> listOrders;

    Product product1 = new Product(1L, "Manteiga", 20.00, "Manteiga de qualidade", "https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg");
    Product product2 = new Product(2L, "Requeijão", 15.00, "Requeijão feito de leite", "https://static.paodeacucar.com/img/uploads/1/282/24364282.jpg");

    OrderDTO orderDTO = new OrderDTO();

    Order order = new Order(null, orderDTO.getAddress(), orderDTO.getLatitude(), orderDTO.getLongitude(),
            Instant.now(), OrderStatus.PENDING);

    Order orderWithoutAddress = new Order(null, null, orderDTO.getLatitude(), orderDTO.getLongitude(),
            Instant.now(), OrderStatus.PENDING);

    Optional<OrderDTO> optionalOrder = Optional.of(new OrderDTO(orderWithoutAddress));

    @BeforeEach
    public void setUp() {
        listProducts.add(product1);
        listProducts.add(product2);
        order.getProducts().addAll(Arrays.asList(product1, product2));

        // Configurar o comportamento do repository mock
        Mockito.when(productRepository.findAllByOrderByNameAsc()).thenReturn(listProducts);
        Mockito.when(orderRepository.findOrdersWithProducts()).thenReturn(listOrders);
    }

    @Test
    public void insertOrderWithoutAddressTest(){
        Mockito.lenient().when(orderService.insert(new OrderDTO(orderWithoutAddress))).thenReturn(optionalOrderDTO.get());

    }
}
