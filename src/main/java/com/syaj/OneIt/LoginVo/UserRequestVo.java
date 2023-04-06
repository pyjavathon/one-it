package com.syaj.OneIt.LoginVo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class UserRequestVo {

	@Getter
	@Setter
	public static class Login {
		@NotNull
		@Size(min = 2, max = 30)
		private String userEmail;

		@NotNull
		@Size(min = 8, max = 16)
		private String userPwd;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Logout {
		@NotNull
		private String accesstoken;

		@NotNull
		private String refreshtoken;
		
		private Long refreshTokenExpirationTime;
		
		private Long accessTokenExpirationTime;
	}

	@Getter
	@Setter
	public static class SignUp {
		@NotNull
		@Size(min = 2, max = 10)
		private String userName;

		@NotNull
		@Size(min = 8, max = 16)
		private String userPwd;

		@NotNull
		@Size(min = 10, max = 20)
		private String userEmail;

		public String userBirth;

		private String userPhone;

		private String agreement;
	}
}
