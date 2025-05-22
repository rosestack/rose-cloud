/*
 * Copyright © 2025 rosestack.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.security.support;

import static io.github.rose.security.CacheConstants.USER_REFRESH_TOKEN_PREFIX;
import static io.github.rose.security.CacheConstants.USER_TOKEN_PREFIX;

import io.github.rose.security.SecurityProperties;
import io.github.rose.security.exception.ExpiredTokenException;
import io.github.rose.security.util.Authority;
import io.github.rose.security.util.SecurityUser;
import io.github.rose.security.util.TokenPair;
import io.jsonwebtoken.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtTokenFactory implements TokenFactory {

    private static final String SCOPES = "scopes";

    private static final String ENABLED = "enabled";

    private final RedisTemplate<String, Object> redisTemplate;

    private final SecurityProperties securityProperties;

    public JwtTokenFactory(RedisTemplate<String, Object> redisTemplate, SecurityProperties securityProperties) {
        this.redisTemplate = redisTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    public TokenPair createTokenPair(SecurityUser securityUser) {
        String accessToken = createAccessToken(securityUser, securityProperties.getAccessTokenExpireTime());
        String refreshToken = createRefreshToken(securityUser, securityProperties.getRefreshTokenExpireTime());

        redisTemplate
                .opsForValue()
                .set(
                        USER_TOKEN_PREFIX + accessToken,
                        securityProperties.getAccessTokenExpireTime(),
                        securityProperties.getAccessTokenExpireTime());
        redisTemplate
                .opsForValue()
                .set(
                        USER_REFRESH_TOKEN_PREFIX + refreshToken,
                        securityProperties.getRefreshTokenExpireTime(),
                        securityProperties.getRefreshTokenExpireTime());

        return new TokenPair(accessToken, refreshToken, securityUser.getAuthorities());
    }

    @Override
    public SecurityUser parseAccessToken(String accessToken) {
        Object accessTokenExpireTime = redisTemplate.opsForValue().get(USER_TOKEN_PREFIX + accessToken);
        if (accessTokenExpireTime == null) {
            throw new BadCredentialsException("Access token is invalid");
        }

        Jws<Claims> jwsClaims = parseTokenClaims(accessToken);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        List<String> scopes = claims.get(SCOPES, List.class);
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("JWT Token doesn't have any scopes");
        }

        return new SecurityUser(
                subject, accessToken, AuthorityUtils.createAuthorityList(scopes.toArray(new String[0])));
    }

    @Override
    public SecurityUser parseRefreshToken(String refreshToken) {
        Object accessTokenExpireTime = redisTemplate.opsForValue().get(USER_REFRESH_TOKEN_PREFIX + refreshToken);
        if (accessTokenExpireTime == null) {
            throw new BadCredentialsException("Refresh Token is invalid");
        }

        Jws<Claims> jwsClaims = parseTokenClaims(refreshToken);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> scopes = claims.get(SCOPES, List.class);
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("Refresh Token doesn't have any scopes");
        }
        if (!scopes.get(0).equals(Authority.REFRESH_TOKEN.name())) {
            throw new IllegalArgumentException("Invalid Refresh Token scope");
        }
        return new SecurityUser(
                subject, refreshToken, AuthorityUtils.createAuthorityList(scopes.toArray(new String[0])));
    }

    @Override
    public TokenPair createPreVerificationTokenPair(SecurityUser user) {
        JwtBuilder jwtBuilder = setUpToken(
                user,
                Collections.singletonList(Authority.PRE_VERIFICATION_TOKEN.name()),
                securityProperties.getAccessTokenExpireTime());
        String accessToken = jwtBuilder.compact();
        return new TokenPair(
                accessToken, null, AuthorityUtils.createAuthorityList(Authority.PRE_VERIFICATION_TOKEN.name()));
    }

    private Jws<Claims> parseTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(securityProperties.getJwt().getTokenSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Token is Invalid", ex);
        } catch (SignatureException | ExpiredJwtException expiredEx) {
            throw new ExpiredTokenException(token, "Token has expired", expiredEx);
        }
    }

    private String createAccessToken(SecurityUser securityUser, Long accessTokenExpireTime) {
        JwtBuilder jwtBuilder = setUpToken(
                securityUser,
                securityUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                accessTokenExpireTime);
        jwtBuilder.claim(ENABLED, securityUser.isEnabled());

        return jwtBuilder.compact();
    }

    private String createRefreshToken(SecurityUser securityUser, Long refreshTokenExpireTime) {
        return setUpToken(
                        securityUser, Collections.singletonList(Authority.REFRESH_TOKEN.name()), refreshTokenExpireTime)
                .id(UUID.randomUUID().toString())
                .compact();
    }

    private JwtBuilder setUpToken(SecurityUser securityUser, List<String> scopes, long expirationTime) {
        Claims claims = Jwts.claims()
                .setSubject(securityUser.getUsername())
                .add(SCOPES, scopes)
                .build();

        ZonedDateTime currentTime = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .issuer(securityProperties.getJwt().getTokenIssuer())
                .issuedAt(Date.from(currentTime.toInstant()))
                .expiration(Date.from(currentTime.plusSeconds(expirationTime).toInstant()))
                .signWith(SignatureAlgorithm.HS512, securityProperties.getJwt().getTokenSigningKey());
    }
}
