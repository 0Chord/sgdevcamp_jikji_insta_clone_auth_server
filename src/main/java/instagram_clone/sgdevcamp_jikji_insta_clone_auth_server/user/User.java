package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String email;
	private String password;
	private String name;
	private String nickname;
	private String phone;
	@Column(name="user_profile")
	private String profile;
	private Boolean status;
	@Column(name = "created_at")
	private LocalDateTime createAt;
	@Column(name = "updated_at")
	private LocalDateTime updateAt;
	@Column(name = "login_at")
	private LocalDateTime loginAt;


	public void updateUpdatedAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

	public void updateLoginAt(LocalDateTime loginAt) {
		this.loginAt = loginAt;
	}

	public void updateEmailAuth(Boolean status) {
		this.status = status;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateNickname(String nickname){ this.nickname = nickname;}

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}