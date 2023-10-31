package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    List<Product> listProducts;

    @BeforeEach
    public void setUp() {
        // Inicialize e popule a lista de produtos
        listProducts = new ArrayList<>();
        Product product1 = new Product(1L, "Manteiga", 20.00, "Manteiga de qualidade", "https://m.media-amazon.com/images/I/81JDBM2iR9L.__AC_SY300_SX300_QL70_ML2_.jpg");
        Product product2 = new Product(2L, "Requeijão", 15.00, "Requeijão feito de leite", "https://static.paodeacucar.com/img/uploads/1/282/24364282.jpg");
        listProducts.add(product1);
        listProducts.add(product2);

        // Configurar o comportamento do repository mock
        Mockito.when(productRepository.findAllByOrderByNameAsc()).thenReturn(listProducts);
    }

    @Test
    public void findAllProductsTest() {
        List<ProductDTO> listProductsDTOsResponse = productService.findAll();

        ProductDTO productDTO1Response = listProductsDTOsResponse.get(0);
        ProductDTO productDTO2Response = listProductsDTOsResponse.get(1);

        Assertions.assertEquals("Manteiga", productDTO1Response.getName());
        Assertions.assertEquals("Requeijão", productDTO2Response.getName());
    }
}