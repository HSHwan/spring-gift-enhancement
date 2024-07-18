package gift.product.dto;

public record CategoryResponse(Long id,
                               String name,
                               String color,
                               String imageUrl,
                               String description) { }