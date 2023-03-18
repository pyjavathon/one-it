package com.syaj.OneIt.LoginVo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {

	@NotNull
	@Size(min = 2, max = 30)
	private String userEmail;
	
	@NotNull
	@Size(min = 8, max = 16)
	private String userPwd;
}
