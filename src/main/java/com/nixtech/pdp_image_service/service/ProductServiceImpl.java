package com.nixtech.pdp_image_service.service;

import com.nixtech.pdp_image_service.entity.Product;
import com.nixtech.pdp_image_service.repository.ProductRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ImageService imageService;
  private final ProductRepository repository;

  @Override
  public void createThumbnail(Integer productId) {
    Product product = repository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    String thumbnailName;
    try {
      thumbnailName = imageService.createThumbnail(product.getImage());
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not create thumbnail", e);
    }

    product.setThumbnailImage(thumbnailName);
    repository.save(product);
  }
}
