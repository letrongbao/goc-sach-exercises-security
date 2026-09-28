package vn.edu.hcmute.example1;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false) private String password;
    @Column(nullable=false) private String fullName;
    @Column(nullable=false) private String role;
    protected User() {}
    public User(String email, String password, String fullName, String role) { this.email=email; this.password=password; this.fullName=fullName; this.role=role; }
    public Long getId(){return id;} public String getEmail(){return email;} public String getPassword(){return password;} public String getFullName(){return fullName;} public String getRole(){return role;}
}
