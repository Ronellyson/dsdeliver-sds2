package com.devsuperior.dsdeliver;

import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


/**
 TESTE DE INTEGRAÇÃO
 **/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TestRestTemplate testRestTemplate;


    // SALVA PRODUTO E VERIFICA SE A QUANTIDADE DE PRODUTO SALVO AUMENTOU:
    @Test
    public void salvarProdutoCorreto(){

        // Salvar um novo produto no banco de dados:

        ProductDTO p = new ProductDTO(9L,"Produto Teste", 200.0,
                "Descricao do produto", "image.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        productService.create(p);

        // Checar se a quantidade de produtos no banco aumentou:
        int size = productService.findAll().size();
        Assertions.assertEquals(10, size);
    }

    // SALVA PRODUTO E VERIFICA SE A QUANTIDADE DE PRODUTO SALVO AUMENTOU NOVAMENTE:
    @Test
    public void salvarProdutoCorretoDois(){

        // Salvar um novo produto no banco de dados:

        ProductDTO p = new ProductDTO(10L,"Produto Teste2", 200.0,
                "Descricao do produto", "image.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        productService.create(p);

        // Checar se a quantidade de produtos no banco aumentou:
        int size = productService.findAll().size();
        Assertions.assertEquals(9, size);
    }

    // TENTA SALVAR UM PRODUTO COM O PREÇO NULO:
    @Test
    public void salvarProdutoComPrecoNull(){


        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p2 = new ProductDTO(11L,"Produto Teste 3", null,
                "Descricao do produto 3", "image3.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p2);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductPriceException.class, () -> productService.create(p2));

    }

    // TENTA SALVAR OUTRO PRODUTO COM O PREÇO NULO:
    @Test
    public void salvarProdutoComPrecoNullDois(){


        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p2 = new ProductDTO(12L,"Produto Teste 4", null,
                "Descricao do produto 4", "image4.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p2);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductPriceException.class, () -> productService.create(p2));

    }

    // TENTA SALVAR UM COM NOME NULL:
    @Test
    public void salvarProdutoComNomeNull(){
        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p3 = new ProductDTO(11L,null, 100.0,
                "Descricao do produto 3", "image3.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p3);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductNameException.class, () -> productService.create(p3));
    }

    // TENTA SALVAR UM COM NOME NULL NOVAMENTE:
    @Test
    public void salvarProdutoComNomeNullDois(){
        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p3 = new ProductDTO(12L,null, 100.0,
                "Descricao do produto 6", "image3.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p3);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductNameException.class, () -> productService.create(p3));
    }

    // TENTA SALVAR UM PRODUTO COM PREÇO NULO E NOME NULO:
    @Test
    public void salvarProdutoNomeEmBranco(){
        // Salvar um novo produto no banco de dados com o nome e preço nulo:
        ProductDTO p3 = new ProductDTO(12L,"", 120.2,
                "Descricao do produto 3", "image3.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p3);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductNameException.class , () -> productService.create(p3));
    }

    @Test
    public void salvarProdutoComPrecoMenorQueZero(){


        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p2 = new ProductDTO(13L,"Produto Teste 2", -2.0,
                "Descricao do produto 2", "image2.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p2);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductPriceException.class, () -> productService.create(p2));

    }

    // TENTA SALVAR UM PRODUTO COM PREÇO NULO E NOME NULO:
    @Test
    public void salvarProdutoNomeEmBrancoDois(){
        // Salvar um novo produto no banco de dados com o nome e preço nulo:
        ProductDTO p3 = new ProductDTO(20L,"", 120.2,
                "Descricao do produto 7", "image3.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p3);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductNameException.class , () -> productService.create(p3));
    }

    // TENTANTO SALVAR PRODUTO COM PREÇO NEGATIVO NOVAMENTE
    @Test
    public void salvarProdutoComPrecoMenorQueZeroDois(){


        // Salvar um novo produto no banco de dados com o preço nulo:
        ProductDTO p2 = new ProductDTO(19L,"Produto Teste 233", -2.0,
                "Descricao do produto 233", "image2.com");
        HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(p2);

        ResponseEntity<ProductDTO> response = this.testRestTemplate.exchange("/product", HttpMethod.POST, httpEntity, ProductDTO.class);

        Assertions.assertThrows(ProductService.MissingProductPriceException.class, () -> productService.create(p2));

    }

}
