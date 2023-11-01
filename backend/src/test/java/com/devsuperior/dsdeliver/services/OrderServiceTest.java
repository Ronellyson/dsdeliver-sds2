package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.OrderStatus;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackageClasses = {OrderService.class})
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    public void testMissingAddressException() {
        OrderDTO orderDTO = new OrderDTO();
        assertThrows(OrderService.MissingAddressException.class, () -> orderService.insert(orderDTO));
    }

    @Test
    public void testMissingLatitudeException() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress("Address");
        assertThrows(OrderService.MissingLatitudeException.class, () -> orderService.insert(orderDTO));
    }

    @Test
    public void testMissingLongitudeException() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress("Address");
        orderDTO.setLatitude(10.0);
        assertThrows(OrderService.MissingLongitudeException.class, () -> orderService.insert(orderDTO));
    }

    @Test
    public void testMissingStatusException() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress("Address");
        orderDTO.setLatitude(10.0);
        orderDTO.setLongitude(20.0);
        assertThrows(OrderService.MissingStatusException.class, () -> orderService.insert(orderDTO));
    }

    @Test
    public void testMissingProductIdException() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress("Address");
        orderDTO.setLatitude(10.0);
        orderDTO.setLongitude(20.0);
        orderDTO.setStatus(OrderStatus.PENDING);

        // Adicionando um produto com ID nulo à lista de produtos do OrderDTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(null);
        productDTO.setName("Product Name");
        productDTO.setPrice(10.0);
        orderDTO.getProducts().add(productDTO);

        assertThrows(OrderService.MissingProductIdException.class, () -> orderService.insert(orderDTO));
    }

    @Test
    public void testOrderNotFoundException() {
        Long orderId = 1L;
        lenient().when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderService.OrderNotFoundException.class, () -> orderService.setDelivered(orderId));
    }
}
