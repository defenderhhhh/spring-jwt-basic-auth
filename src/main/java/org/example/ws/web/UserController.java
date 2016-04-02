package org.example.ws.web;

import java.security.Principal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.example.ws.model.Account;
import org.example.ws.repository.AccountRepository;
import org.example.ws.util.PropertyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {


	
	private long daysMillis = TimeUnit.MILLISECONDS.convert(90, TimeUnit.DAYS); 
	private long nowMillis = System.currentTimeMillis();
	private long expMillis = nowMillis + daysMillis;
	private Date now = new Date(nowMillis);
    Date exp = new Date(expMillis);

    @Autowired
    private AccountRepository accountRepository;
    
    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(ModelMap model, Principal principal ) {
    	String name = principal.getName();
    	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    	//We will sign our JWT with our ApiKey secret
    	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(PropertyConfig.SECRETKEY);
    	SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    	Account account = accountRepository.findByUsername(name);
    	
        return new LoginResponse(Jwts.builder().setSubject(account.getUsername())
            .claim("roles", account.getRoles()).setIssuedAt(now).setExpiration(exp)
            .signWith(signatureAlgorithm, signingKey).compact());
        
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
