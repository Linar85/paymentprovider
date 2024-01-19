package com.example.paymentprovider.security;

import com.example.paymentprovider.entity.Merchant;
import com.example.paymentprovider.repository.MerchantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class UserDetailServiceImplTest {

    @Mock
    MerchantRepository merchantRepository;
    @InjectMocks
    UserDetailServiceImpl userDetailService;

    @Test
    void findByUsername() {
        Merchant merchant = Merchant.builder()
                .username("user_name")
                .secretKey("user_password")
                .build();

        String existMerchanderName = "user_name";

        Mockito.when(merchantRepository.findByUsername(any(String.class))).thenReturn(Mono.just(merchant));

        StepVerifier.create(userDetailService.findByUsername(existMerchanderName))
                .consumeNextWith(userDetails -> {
                    Assertions.assertEquals(userDetails.getUsername(), merchant.getUsername());
                })
                .verifyComplete();
    }

    @Test
    void findByUsernameGetException() {

        String doesntExistMerchanderName = "user_name";

        Mockito.when(merchantRepository.findByUsername(any(String.class))).thenReturn(Mono.empty());

        StepVerifier.create(userDetailService.findByUsername(doesntExistMerchanderName))
                .expectErrorMatches(throwable -> throwable instanceof UsernameNotFoundException &&
                        throwable.getMessage().equals("User not found"))
                .verify();
    }
}