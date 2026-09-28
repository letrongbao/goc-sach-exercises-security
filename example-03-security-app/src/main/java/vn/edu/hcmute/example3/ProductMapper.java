package vn.edu.hcmute.example3;
import org.mapstruct.Mapper;
@Mapper(componentModel="spring") interface ProductMapper {ProductView toView(Product product);}
