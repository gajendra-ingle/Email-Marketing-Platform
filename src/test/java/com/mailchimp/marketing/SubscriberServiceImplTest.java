package com.mailchimp.marketing;

import com.mailchimp.marketing.config.MailchimpProperties;
import com.mailchimp.marketing.service.impl.SubscriberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImplTest {

    @Mock
    private RestTemplate mailchimpRestTemplate;

    @Mock
    private MailchimpProperties properties;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    @Test
    void md5Hash_shouldReturnCorrectHash() {
        // Mailchimp expects lowercase email MD5 hash
        String email = "test@example.com";
        String expectedHash = "55502f40dc8b7c769880b10874abc9d0";

        String actualHash = subscriberService.md5Hash(email);

        assertThat(actualHash).isEqualTo(expectedHash);
    }

    @Test
    void md5Hash_shouldNormalizeEmailToLowercase() {
        String upper = "TEST@EXAMPLE.COM";
        String lower = "test@example.com";

        assertThat(subscriberService.md5Hash(upper))
                .isEqualTo(subscriberService.md5Hash(lower));
    }

    @Test
    void md5Hash_shouldTrimWhitespace() {
        String padded = "  test@example.com  ";
        String clean  = "test@example.com";

        assertThat(subscriberService.md5Hash(padded))
                .isEqualTo(subscriberService.md5Hash(clean));
    }
}
