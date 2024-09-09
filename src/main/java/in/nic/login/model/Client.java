package in.nic.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Data
@Builder
@Document(collection = "client")
public class Client implements UserDetails {

    @Id
    @Field("client_id")
    private String clientId;

    @Field("client_secret")
    private String clientSecret;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Field("created_on")
    private Date createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Field("expiry_on")
    private Date expiryOn;

    @Field("mobile_no")
    private long mobileNo;

    @Field("email_id")
    private String emailId;
    private String name;
    private String gender;
    private String dob;
    private String address;

    private Role role;

    @JsonIgnore
    public String getUsername() { return clientId; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public void setUsername(String username) { this.clientId = username; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority(role.name())); }

    @JsonIgnore
    public String getPassword() { return clientSecret; }

    public void setPassword(String password) { this.clientSecret = password; }
}
