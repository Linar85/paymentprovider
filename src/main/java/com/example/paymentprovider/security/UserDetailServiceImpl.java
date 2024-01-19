package com.example.paymentprovider.security;

import com.example.paymentprovider.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements ReactiveUserDetailsService {

    private final MerchantRepository merchantRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return merchantRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found"))))
                .map(merchant -> User.builder()
                        .username(merchant.getUsername())
                        .password(merchant.getSecretKey())
                        .build());
    }
}
