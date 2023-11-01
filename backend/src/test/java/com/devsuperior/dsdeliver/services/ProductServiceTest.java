package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração do mock para o método findAll
        List<ProductDTO> productsDTOs = new ArrayList<>();
        productsDTOs.add(new ProductDTO(1L, "Manteiga", 20.00, "Manteiga de qualidade", "https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg"));
        productsDTOs.add(new ProductDTO(2L, "Requeijão", 18.50, "Requeijão cremoso", "https://example.com/requeijao.jpg"));

        List<Product> products = productsDTOs.stream().map(Product::new).collect(Collectors.toList());
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(products);

        // Configuração do mock para o método existsByName
        when(productRepository.existsByName("Manteiga")).thenReturn(true);
        when(productRepository.existsByName("Novo Produto")).thenReturn(false);

        // Configuração do mock para o método findById
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        // Configuração do mock para o método save
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(1L); // Define um ID para o produto salvo
            return product;
        });
    }

    @Test
    public void testFindAllProducts() {
        // Act
        List<ProductDTO> listProductsDTOsResponse = productService.findAll();

        // Assert
        assertEquals(2, listProductsDTOsResponse.size());
        assertEquals("Manteiga", listProductsDTOsResponse.get(0).getName());
        assertEquals("Requeijão", listProductsDTOsResponse.get(1).getName());
    }

    @Test
    public void testCreateProductWithExistingName() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Manteiga");
        productDTO.setPrice(20.00);
        productDTO.setDescription("Manteiga de qualidade");
        productDTO.setImageUri("https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg");

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

        // Act
        ProductDTO createdProductDTO = productService.create(productDTO);

        // Assert
        assertNotNull(createdProductDTO.getId()); // Verifica se o ID foi atribuído ao produto salvo
        assertEquals("Novo Produto", createdProductDTO.getName());

        verify(productRepository, times(1)).existsByName("Novo Produto");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateNonExistentProduct() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Novo Nome");
        dto.setPrice(50.0);
        dto.setDescription("Nova Descrição");
        dto.setImageUri("nova-imagem.jpg");

        // Configurar o comportamento do mock para o método findById
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ProductService.ProductNotFoundException.class, () -> productService.update(1L, dto));

        // Verificar se o método foi chamado corretamente
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
