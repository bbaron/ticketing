package ticketing.auth;

//@Configuration
//@EnableAuthorizationServer
//@RequiredArgsConstructor
public class AuthServerConfigSave {// extends AuthorizationServerConfigurerAdapter {
//    private final TicketingProperties properties;
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//               .withClient(properties.security.clientId)
//               .secret(properties.security.clientSecret)
//               .authorizedGrantTypes("password", "refresh_token")
//               .scopes("read");
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        var tokenEnhancerChain = new TokenEnhancerChain();
//        var tokenEnhancers = List.of(new CustomTokenEnhancer(), jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//        endpoints.authenticationManager(authenticationManager)
//                 .tokenStore(tokenStore())
//                 .tokenEnhancer(tokenEnhancerChain);
//    }
//
//    @Bean
//    TokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    JwtAccessTokenConverter jwtAccessTokenConverter() {
//        var converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(properties.security.jwtKey);
//        return converter;
//    }
//
//
}
