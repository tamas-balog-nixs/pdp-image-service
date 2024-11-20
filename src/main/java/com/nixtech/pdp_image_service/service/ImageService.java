package com.nixtech.pdp_image_service.service;

import java.io.IOException;

public interface ImageService {

  String createThumbnail(String imageName) throws IOException;
}
