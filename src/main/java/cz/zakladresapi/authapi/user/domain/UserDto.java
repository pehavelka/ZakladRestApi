package cz.zakladresapi.authapi.user.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
	private Integer id;
	private String email;
	private String fullName;
	
	private LocalDateTime zmenaCas;
	private String zmenaUzivatel;
	private boolean platnost;
	
	public static UserDto of(final User ent) {
		return UserDto
				.builder()
				.id(ent.getId())
				.email(ent.getEmail())
				.fullName(ent.getFullName())
				.zmenaCas(ent.getZmenaCas())
				.zmenaUzivatel(ent.getZmenaUzivatel())
				.platnost(ent.isPlatnost())
				.build();
	}
}