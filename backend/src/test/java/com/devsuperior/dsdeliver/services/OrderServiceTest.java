package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.OrderStatus;
import com.devsuperior.dsdeliver.entities.Product;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackageClasses = {OrderService.class})
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

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
        // Criar um objeto OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress("Address");
        orderDTO.setLatitude(10.0);
        orderDTO.setLongitude(20.0);
        orderDTO.setStatus(OrderStatus.PENDING);
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO(1L, "Product 1", 10.0, "Description", "image_uri"));
        products.add(new ProductDTO(2L, "Product 2", 15.0, "Description", "image_uri"));
        orderDTO.setProducts(products);

        // Stubbing the findById method call for products with different IDs
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product("Product 1", 10.0, "Description", "image_uri")));
        when(productRepository.findById(2L)).thenThrow(new ProductService.ProductNotFoundException("Produto não encontrado com ID: 2"));

        // Chamar o método insert do service para salvar o objeto Order
        assertThrows(ProductService.ProductNotFoundException.class, () -> orderService.insert(orderDTO));

        // Verificar se o objeto Order não foi salvo no banco de dados
        verify(orderRepository, never()).save(any());

        // Verificar se o método findById foi chamado com os IDs corretos
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    public void testOrderNotFoundException() {
        Long orderId = 1L;
        lenient().when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderService.OrderNotFoundException.class, () -> orderService.setDelivered(orderId));
    }
}
