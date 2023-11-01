package com.devsuperior.dsdeliver.services;

import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Método para encontrar todos os produtos ordenados por nome
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAllByOrderByNameAsc();
        return ProductDTO.fromAll(list);
    }

    // Método para encontrar produtos por nome (insensível a maiúsculas/minúsculas)
    @Transactional(readOnly = true)
    public List<ProductDTO> findByName(String name) {
        // Verificar se o nome do produto é fornecido
        if (name == null || name.trim().isEmpty()) {
            throw new MissingProductNameException("Nome do produto não pode estar vazio ou nulo.");
        }

        // Encontrar produtos por nome e ordenar por nome
        List<Product> list = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
        if (list.isEmpty()) {
            throw new ProductNotFoundException("Nenhum produto encontrado com o nome: " + name);
        }

        return ProductDTO.fromAll(list);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        // Verificar se os campos obrigatórios estão presentes no DTO
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new MissingProductNameException("Nome do produto é obrigatório.");
        }

        if (dto.getPrice() == null) {
            throw new MissingProductPriceException("Preço do produto é obrigatório.");
        }

        // Verificar se um produto com o mesmo nome já existe no banco de dados
        if (productRepository.existsByName(dto.getName())) {
            throw new ProductAlreadyExistsException("Produto com o nome " + dto.getName() + " já existe.");
        }

        // Criar e salvar o produto no banco de dados
        Product product = new Product(dto.getName(), dto.getPrice(), dto.getDescription(), dto.getImageUri());
        product = productRepository.save(product);

        // Criar e retornar o objeto ProductDTO com base no produto criado
        return new ProductDTO(product);
    }

    // Método para atualizar um produto existente
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        // Verificar se o produto com o ID especificado existe
        Optional<Product> existingProduct = productRepository.findById(id);

        // Verificar se o produto com o ID especificado foi encontrado
        Product product = existingProduct.orElseThrow(() -> new ProductNotFoundException("Produto com o ID " + id + " não encontrado."));

        // Atualizar os campos do produto com base no DTO fornecido
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageUri(dto.getImageUri());

        // Salvar o produto atualizado no banco de dados
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    // Método para excluir um produto por ID
    @Transactional
    public void delete(Long id) {
        // Verificar se o produto com o ID especificado existe
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto com o ID " + id + " não encontrado."));

        // Excluir o produto do banco de dados
        productRepository.delete(product);
    }

    // Exceção para nome do produto ausente
    class MissingProductNameException extends RuntimeException {
        public MissingProductNameException(String message) {
            super(message);
        }
    }

    // Exceção para preço do produto ausente
    class MissingProductPriceException extends RuntimeException {
        public MissingProductPriceException(String message) {
            super(message);
        }
    }

    // Exceção para produto não encontrado
    static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    // Exceção para produto com mesmo nome já existente
    static class ProductAlreadyExistsException extends RuntimeException {
        public ProductAlreadyExistsException(String message) {
            super(message);
        }
    }
}
