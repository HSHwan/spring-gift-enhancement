package gift.product.util;

import gift.category.entity.Category;
import gift.product.entity.Product;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;

public class ProductMapper {

    public static ProductResponse toResponseDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
                       .getName()
        );
    }

    public static Product toEntity(ProductRequest request, Category category) {
        return new Product(
                request.name(),
                request.price(),
                request.imageUrl(),
                category
        );
    }

}