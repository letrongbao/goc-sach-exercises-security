package vn.edu.hcmute.example3;
import java.math.BigDecimal;
public record ProductView(Long id,String name,String description,BigDecimal price,String imageUrl,AppUser owner) {}
