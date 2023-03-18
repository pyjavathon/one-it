package com.syaj.OneIt.LoginVo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

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
