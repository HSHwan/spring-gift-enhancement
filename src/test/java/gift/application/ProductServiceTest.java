package gift.application;

import gift.category.dao.CategoryRepository;
import gift.category.entity.Category;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.product.application.ProductService;
import gift.product.dao.ProductRepository;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.util.ProductMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private final Category category = new Category.CategoryBuilder()
            .setName("상품권")
            .setColor("#ffffff")
            .setImageUrl("https://product-shop.com")
            .setDescription("")
            .build();

    @Test
    @DisplayName("상품 전체 조회 서비스 테스트")
    void getPagedProducts() {
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product.ProductBuilder()
                .setName("product1")
                .setPrice(1000)
                .setImageUrl("https://testshop.com")
                .setCategory(category)
                .build();
        Product product2 = new Product.ProductBuilder()
                .setName("product2")
                .setPrice(2000)
                .setImageUrl("https://testshop.io")
                .setCategory(category)
                .build();
        productList.add(product1);
        productList.add(product2);
        Page<Product> productPage = new PageImpl<>(productList);
        given(productRepository.findAll(productPage.getPageable())).willReturn(productPage);

        Page<ProductResponse> responsePage = productService.getPagedProducts(productPage.getPageable());

        Assertions.assertThat(responsePage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세 조회 서비스 테스트")
    void getProductById() {
        Product product = new Product.ProductBuilder()
                .setName("product1")
                .setPrice(1000)
                .setImageUrl("https://testshop.com")
                .setCategory(category)
                .build();
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        ProductResponse resultProduct = productService.getProductByIdOrThrow(1L);

        Assertions.assertThat(resultProduct.name()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 서비스 상세 조회 실패 테스트")
    void getProductByIdFailed() {
        Long productId = 1L;
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.getProductByIdOrThrow(productId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND
                                     .getMessage());
    }

    @Test
    @DisplayName("상품 추가 서비스 테스트")
    void createProduct() {
        ProductRequest request = new ProductRequest(
                "product1",
                1000,
                "https://testshop.com",
                category.getName());
        Product product = ProductMapper.toEntity(request, category);
        given(productRepository.save(any())).willReturn(product);
        given(categoryRepository.findByName(any())).willReturn(Optional.of(category));

        ProductResponse response = productService.createProduct(request);

        Assertions.assertThat(response.name()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("단일 상품 삭제 서비스 테스트")
    void deleteProductById() {
        Product product = new Product.ProductBuilder()
                .setName("product1")
                .setPrice(1000)
                .setImageUrl("https://testshop.com")
                .setCategory(category)
                .build();

        productService.deleteProductById(product.getId());

        verify(productRepository).deleteById(product.getId());
    }

    @Test
    @DisplayName("상품 전체 삭제 서비스 테스트")
    void deleteAllProducts() {
        productService.deleteAllProducts();

        verify(productRepository).deleteAll();
    }

    @Test
    @DisplayName("상품 수정 서비스 테스트")
    void updateProduct() {
        Product product = new Product.ProductBuilder()
                .setName("product1")
                .setPrice(1000)
                .setImageUrl("https://testshop.com")
                .setCategory(category)
                .build();
        ProductRequest request = new ProductRequest(
                "product",
                3000,
                "https://testshop.io",
                category.getName());
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(categoryRepository.findByName(any())).willReturn(Optional.of(category));
        product.update(request, category);
        given(productRepository.save(any())).willReturn(product);

        Long productId = productService.updateProduct(product.getId(), request);

        Assertions.assertThat(productId).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("상품 서비스 수정 실패 테스트")
    void updateProductFailed() {
        Long productId = 1L;
        ProductRequest request = new ProductRequest(
                "product",
                3000,
                "https://testshop.io",
                category.getName());
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.updateProduct(productId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND
                                     .getMessage());
    }

}