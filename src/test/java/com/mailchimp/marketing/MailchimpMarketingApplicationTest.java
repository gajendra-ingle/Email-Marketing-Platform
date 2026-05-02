package com.mailchimp.marketing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "mailchimp.api.key=test-key",
        "mailchimp.api.server-prefix=us1",
        "mailchimp.api.base-url=https://us1.api.mailchimp.com/3.0"
})
class MailchimpMarketingApplicationTest {

    @Test
    void contextLoads() {
        // Verifies the Spring context loads without errors
    }
}
