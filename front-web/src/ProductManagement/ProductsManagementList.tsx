import React from 'react';
import Product from './Product';
import './../Orders/ProductCard'; // Importe seu arquivo de estilo para os cards

type ProductsManagementListProps = {
    products: Product[];
    onSelectProduct: (product: Product) => void;
    selectedProduct: Product | null;
};

const ProductsManagementList: React.FC<ProductsManagementListProps> = ({
    products,
    onSelectProduct,
    selectedProduct,
}) => {
    return (
        <div className="product-cards-container"> {/* Adicione uma classe de contêiner para os cards */}
            {products.map((product) => (
                <div
                    key={product.id}
                    className={`product-card ${selectedProduct?.id === product.id ? 'selected' : ''}`} // Adicione a classe 'selected' se o produto estiver selecionado
                    onClick={() => onSelectProduct(product)}
                >
                    <h3>{product.name}</h3>
                    <p>{product.description}</p>
                    <p>Preço: R$ {product.price.toFixed(2)}</p>
                    {/* Adicione outros detalhes do produto conforme necessário */}
                </div>
            ))}
        </div>
    );
};

export default ProductsManagementList;
