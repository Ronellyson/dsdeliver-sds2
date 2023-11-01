package com.devsuperior.dsdeliver.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Order;
import com.devsuperior.dsdeliver.entities.OrderStatus;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        List<Order> list = orderRepository.findOrdersWithProducts();
        return list.stream().map(x -> new OrderDTO(x)).collect(Collectors.toList());        
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        // Verificar se os campos obrigatórios estão presentes no DTO
        if (dto.getAddress() == null) {
            throw new MissingAddressException("Endereço é obrigatório.");
        }

        if (dto.getLatitude() == null) {
            throw new MissingLatitudeException("Latitude é obrigatória.");
        }

        if (dto.getLongitude() == null) {
            throw new MissingLongitudeException("Longitude é obrigatória.");
        }

        if (dto.getStatus() == null) {
            throw new MissingStatusException("Status é obrigatório.");
        }

        if (dto.getProducts() == null) {
            throw new MissingProductIdException("Lista de produtos é obrigatória.");
        }

        Order order = new Order(null, dto.getAddress(), dto.getLatitude(), dto.getLongitude(),
                Instant.now(), dto.getStatus());

        for (ProductDTO p : dto.getProducts()) {
            if (p.getId() == null) {
                throw new MissingProductIdException("ID do produto é obrigatório.");
            }
            Product product = productRepository.findById(p.getId())
                    .orElseThrow(() -> new ProductService.ProductNotFoundException("Produto não encontrado com ID: " + p.getId()));
            order.getProducts().add(product);
        }

        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO setDelivered(Long id) {
        // Verificar se o Order com o ID especificado existe
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order com o ID " + id + " não encontrado.");
        }

        Order order = orderRepository.getOne(id);
        order.setStatus(OrderStatus.DELIVERED);
        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

    // Exceção para campos de endereço ausentes
    class MissingAddressException extends RuntimeException {
        public MissingAddressException(String message) {
            super(message);
        }
    }

    // Exceção para latitude ausente
    class MissingLatitudeException extends RuntimeException {
        public MissingLatitudeException(String message) {
            super(message);
        }
    }

    // Exceção para longitude ausente
    class MissingLongitudeException extends RuntimeException {
        public MissingLongitudeException(String message) {
            super(message);
        }
    }

    // Exceção para ID do produto ausente
    class MissingProductIdException extends RuntimeException {
        public MissingProductIdException(String message) {
            super(message);
        }
    }

    // Exceção para status ausente
    class MissingStatusException extends RuntimeException {
        public MissingStatusException(String message) {
            super(message);
        }
    }

    // Exceção para Order não encontrado
    class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }
}