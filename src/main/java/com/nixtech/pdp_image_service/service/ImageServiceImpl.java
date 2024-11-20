package com.nixtech.pdp_image_service.service;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  @Value("${thumbnail.width}")
  public int thumbnailWidth;

  @Value("${thumbnail.height}")
  public int thumbnailHeight;

  @Value("${thumbnail.corner-radius}")
  public int thumbnailCornerRadius;

  @Value("${file.upload-dir}")
  private String imageDirectory;

  @Override
  public String createThumbnail(String imageName) throws IOException {
    File imageFile = new File(imageDirectory, imageName);

    if (!imageFile.exists()) {
      throw new IOException("Image not found: " + imageName);
    }

    // Create a thumbnail of the image with the specified width and height
    BufferedImage thumbnail = Thumbnails.of(imageFile)
        .size(thumbnailWidth, thumbnailHeight)
        .asBufferedImage();

    // Calculate the aspect ratio and resized dimensions of the thumbnail
    int originalImageWidth = thumbnail.getWidth();
    int originalImageHeight = thumbnail.getHeight();

    double aspectRatio = (double) originalImageWidth / originalImageHeight;

    int targetWidth = thumbnailWidth;
    int targetHeight = (int) (thumbnailWidth / aspectRatio);

    if (targetHeight > thumbnailHeight) {
      targetHeight = thumbnailHeight;
      targetWidth = (int) (thumbnailHeight * aspectRatio);
    }

    // Create a new BufferedImage to hold the rounded corner image
    BufferedImage roundedThumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight,
        BufferedImage.TYPE_INT_ARGB);

    // Draw the rounded corners
    Graphics2D g2 = roundedThumbnail.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setComposite(AlphaComposite.Src);

    // Define the rounded rectangle shape
    RoundRectangle2D round = new RoundRectangle2D.Float(0, 0, thumbnailWidth, thumbnailHeight,
        thumbnailCornerRadius, thumbnailCornerRadius);

    // Clip the shape to the round rectangle
    g2.setClip(round);

    // Calculate the x and y positions to center the image inside the thumbnail
    int x = (thumbnailWidth - targetWidth) / 2;
    int y = (thumbnailHeight - targetHeight) / 2;

    // Draw the thumbnail image centered on the canvas
    g2.drawImage(thumbnail, x, y, targetWidth, targetHeight, null);
    g2.dispose();

    String thumbnailFileName =
        "thumb_" + imageName.substring(0, imageName.lastIndexOf('.')) + ".png";
    File thumbnailFile = new File(imageDirectory, thumbnailFileName);
    ImageIO.write(roundedThumbnail, "png", thumbnailFile);
    return thumbnailFileName;
  }

}
