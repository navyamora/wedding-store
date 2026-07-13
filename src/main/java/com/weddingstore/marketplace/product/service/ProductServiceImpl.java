package com.weddingstore.marketplace.product.service;

import com.weddingstore.marketplace.category.entity.Category;
import com.weddingstore.marketplace.category.repository.CategoryRepository;
import com.weddingstore.marketplace.product.dto.*;
import com.weddingstore.marketplace.product.entity.Product;
import com.weddingstore.marketplace.product.entity.ProductImage;
import com.weddingstore.marketplace.product.mapper.ProductMapper;
import com.weddingstore.marketplace.product.repository.ProductRepository;
import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse create(CreateProductRequest request) {
        String slug = generateSlug(request.getName());

        if (productRepository.existsBySlug(slug)) {
            throw new ConflictException("Product slug already exists");
        }

        if (productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Product SKU already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .sku(request.getSku())
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .quantity(request.getQuantity())
                .featured(Boolean.TRUE.equals(request.getFeatured()))
                .active(true)
                .category(category)
                .images(new ArrayList<>())
                .build();

        addImages(product, request.getImages());

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponse> getAllActive(Long categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryIdAndActiveTrueOrderByIdDesc(categoryId);
        } else {
            products = productRepository.findByActiveTrueOrderByIdDesc();
        }

        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getFeatured() {
        return productRepository.findByFeaturedTrueAndActiveTrueOrderByIdDesc()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = findProductById(id);
        String slug = generateSlug(request.getName());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(request.getName());
        product.setSlug(slug);
        product.setShortDescription(request.getShortDescription());
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setQuantity(request.getQuantity());
        product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        product.setCategory(category);

        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        product.getImages().clear();
        addImages(product, request.getImages());

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Product product = findProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private void addImages(Product product, List<ProductImageRequest> imageRequests) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            return;
        }

        for (ProductImageRequest imageRequest : imageRequests) {
            ProductImage image = ProductImage.builder()
                    .imageUrl(imageRequest.getImageUrl())
                    .displayOrder(imageRequest.getDisplayOrder())
                    .primaryImage(Boolean.TRUE.equals(imageRequest.getPrimaryImage()))
                    .product(product)
                    .build();

            product.getImages().add(image);
        }
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}