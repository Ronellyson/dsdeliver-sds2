import React, { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import axios from 'axios';
import Product from './Product';
import ProductsManagementList from './ProductsManagementList';

const API_URL = "http://localhost:8090";

function ProductManagement() {
    const [products, setProducts] = useState<Product[]>([]);
    const [newProduct, setNewProduct] = useState({
        name: '',
        description: '',
        price: 0,
        imageUri: ''
    });

    const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = () => {
        axios.get<Product[]>(`${API_URL}/products`)
            .then(response => {
                setProducts(response.data);
            })
            .catch(error => {
                console.error('Erro ao buscar produtos:', error);
            });
    };

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewProduct(prevProduct => ({
            ...prevProduct,
            [name]: value
        }));
    };

    const handleSelectProduct = (product: Product) => {
        setSelectedProduct(product);
        setNewProduct(product);
    };

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        if (selectedProduct) {
            // Atualiza o produto existente
            axios.put<Product>(`${API_URL}/products/${selectedProduct.id}`, newProduct)
                .then(response => {
                    console.log('Produto atualizado com sucesso:', response.data);
                    fetchProducts();
                    setSelectedProduct(null);
                })
                .catch(error => {
                    console.error('Erro ao atualizar produto:', error);
                });
        } else {
            // Cria um novo produto
            axios.post<Product>(`${API_URL}/products`, newProduct)
                .then(response => {
                    console.log('Novo produto criado com sucesso:', response.data);
                    fetchProducts();
                })
                .catch(error => {
                    console.error('Erro ao criar produto:', error);
                });
        }

        // Limpa o formulário após o envio
        setNewProduct({
            name: '',
            description: '',
            price: 0,
            imageUri: ''
        });
    };

    const handleDeleteProduct = () => {
        if (selectedProduct) {
            axios.delete(`${API_URL}/products/${selectedProduct.id}`)
                .then(response => {
                    console.log('Produto excluído com sucesso');
                    fetchProducts();
                    setSelectedProduct(null);
                })
                .catch(error => {
                    console.error('Erro ao excluir produto:', error);
                });
        }
    };

    return (
        <div>
            <h1>Gerenciamento de Produtos</h1>

            {/* Lista de Produtos usando o novo componente */}
            <ProductsManagementList
                products={products}
                onSelectProduct={handleSelectProduct}
                selectedProduct={selectedProduct}
            />

            {/* Formulário para criar/atualizar um produto */}
            <div>
                <h2>{selectedProduct ? 'Editar Produto' : 'Novo Produto'}</h2>
                <form onSubmit={handleSubmit}>
                    {/* Campos do formulário */}
                    <button type="submit">{selectedProduct ? 'Atualizar Produto' : 'Criar Produto'}</button>
                    {selectedProduct && <button type="button" onClick={handleDeleteProduct}>Excluir Produto</button>}
                </form>
            </div>
        </div>
    );
}

export default ProductManagement;
