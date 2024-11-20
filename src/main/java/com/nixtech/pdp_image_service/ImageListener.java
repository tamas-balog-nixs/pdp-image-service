package com.nixtech.pdp_image_service;

import com.nixtech.pdp_image_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageListener {

  private final ProductService productService;

  @JmsListener(destination = "${amq.queue}")
  public void createThumbnail(Integer productId) {
    productService.createThumbnail(productId);
  }
}
