package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testFindAllProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product("Manteiga", 20.00, "Manteiga de qualidade", "https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg"));
        products.add(new Product("Requeijão", 18.50, "Requeijão cremoso", "https://example.com/requeijao.jpg"));

        productRepository.saveAll(products);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<ProductDTO> listProductsDTOsResponse = productService.findAll();

        // Assert
        assertEquals(2, listProductsDTOsResponse.size());
        assertEquals("Manteiga", listProductsDTOsResponse.get(0).getName());
        assertEquals("Requeijão", listProductsDTOsResponse.get(1).getName());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testCreateProductWithExistingName() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Manteiga");
        productDTO.setPrice(20.00);
        productDTO.setDescription("Manteiga de qualidade");
        productDTO.setImageUri("https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg");

        when(productRepository.existsByName("Manteiga")).thenReturn(true);

        // Act and Assert
        assertThrows(ProductService.ProductAlreadyExistsException.class, () -> productService.create(productDTO));

        verify(productRepository, times(1)).existsByName("Manteiga");
        verify(productRepository, times(0)).save(any());
    }

    @Test
    public void testCreateProductWithNewName() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Novo Produto");
        productDTO.setPrice(25.00);
        productDTO.setDescription("Descrição do novo produto");
        productDTO.setImageUri("https://example.com/image.jpg");

        when(productRepository.existsByName("Novo Produto")).thenReturn(false);

        // Act and Assert
        assertDoesNotThrow(() -> productService.create(productDTO));

        verify(productRepository, times(1)).existsByName("Novo Produto");
        verify(productRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateProductWithExistingName() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setName("Manteiga");

        when(productRepository.existsByName("Manteiga")).thenReturn(true);

        // Act and Assert
        assertThrows(ProductService.ProductAlreadyExistsException.class, () -> productService.update(1L, dto));

        verify(productRepository, times(1)).existsByName("Manteiga");
        verify(productRepository, times(0)).save(any());
    }

    @Test
    public void testUpdateProductWithNewName() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setName("Novo Produto");

        when(productRepository.existsByName("Novo Produto")).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(new Product()));

        // Act and Assert
        assertThrows(ProductService.ProductNotFoundException.class, () -> productService.update(1L, dto));

        verify(productRepository, times(1)).existsByName("Novo Produto");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).save(any());
    }
}