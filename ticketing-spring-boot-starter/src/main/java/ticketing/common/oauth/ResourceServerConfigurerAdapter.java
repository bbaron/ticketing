package ticketing.common.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import ticketing.common.autoconfigure.TicketingProperties;

import javax.crypto.spec.SecretKeySpec;

@RequiredArgsConstructor
public class ResourceServerConfigurerAdapter extends WebSecurityConfigurerAdapter implements ResourceServerCustomizer {
    private final TicketingProperties properties;

    @Override
    public final void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder())));
        customize(http);
    }

    @Override
    public void customize(HttpSecurity http) throws Exception {

    }

    public JwtClaimAdapter jwtClaimAdapter() {
        return new JwtClaimAdapter();
    }

    JwtDecoder jwtDecoder() {
        byte[] key = properties.security.jwtKey.getBytes();
        var originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        var decoder = NimbusJwtDecoder.withSecretKey(originalKey)
                                      .build();
        decoder.setClaimSetConverter(jwtClaimAdapter());
        return decoder;
    }


}
