package dz.bdjaghout.productservice.service;

import dz.bdjaghout.productservice.dto.CreateProduct;
import dz.bdjaghout.productservice.dto.ProductResponse;
import dz.bdjaghout.productservice.model.Product;
import dz.bdjaghout.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(CreateProduct createProduct) {
        Product product = Product.builder()
                .name(createProduct.getName())
                .description(createProduct.getDescription())
                .price(createProduct.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product with ID = {} was created!", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::new).toList();
    }
}
