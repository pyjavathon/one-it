package com.syaj.OneIt.LoginVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenVo {
	
	private String accesstoken;
	
	private String refreshtoken;

}
