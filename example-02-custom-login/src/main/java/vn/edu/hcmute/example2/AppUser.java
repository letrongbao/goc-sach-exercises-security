package vn.edu.hcmute.example2;
import jakarta.persistence.*;
@Entity @Table(name="users") public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false,unique=true) private String username; @Column(nullable=false,unique=true) private String email; @Column(nullable=false) private String password; @Column(nullable=false) private String fullName; private String image; @Column(nullable=false) private String role;
 protected AppUser(){} public AppUser(String username,String email,String password,String fullName,String image,String role){this.username=username;this.email=email;this.password=password;this.fullName=fullName;this.image=image;this.role=role;}
 public Long getId(){return id;} public String getUsername(){return username;} public String getEmail(){return email;} public String getPassword(){return password;} public String getFullName(){return fullName;} public String getImage(){return image;} public String getRole(){return role;}
}
