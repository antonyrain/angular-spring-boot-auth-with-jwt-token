// package com.antonyrain.server.service;

// import java.text.ParseException;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.stream.Collectors;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.oauth2.jwt.JwtClaimsSet;
// import org.springframework.security.oauth2.jwt.JwtEncoder;
// import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

// import com.nimbusds.jwt.SignedJWT;

// public class MyTokenService  {
	
// 	private final JwtEncoder jwtEncoder;

// 	public MyTokenService(JwtEncoder jwtEncoder) {
// 		super();
// 		this.jwtEncoder = jwtEncoder;
// 	}
	
//     public String generateAccessToken(UserDetailsImpl userDetails) {
//         Instant now = Instant.now();
//         String scope = userDetails.getAuthorities().stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .collect(Collectors.joining(" "));
    
//         JwtClaimsSet claims = JwtClaimsSet.builder()
//                 .issuer("self")
//                 .issuedAt(now)
//                 .expiresAt(now.plus(2, ChronoUnit.MINUTES))
//                 .subject(userDetails.getUsername())
//                 .claim("scope", scope)
//                 .build();
//         return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//     }

//     public String generateRefreshToken(UserDetailsImpl userDetails) {
//         Instant now = Instant.now();
//         String scope = userDetails.getAuthorities().stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .collect(Collectors.joining(" "));
        
//         JwtClaimsSet claims = JwtClaimsSet.builder()
//                 .issuer("self")
//                 .issuedAt(now)
//                 .expiresAt(now.plus(10, ChronoUnit.MINUTES))
//                 .subject(userDetails.getUsername())
//                 .claim("scope", scope)
//                 .build();
//         return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//     }
    
//     public String parseToken(String token) {
//     	try {
// 			SignedJWT decodedJWT = SignedJWT.parse(token);
// 			String subject = decodedJWT.getJWTClaimsSet().getSubject();
// 			return subject;
// 		} catch (ParseException e) {
// 			e.printStackTrace();
// 		}
//     	return null;
//     }
// }
