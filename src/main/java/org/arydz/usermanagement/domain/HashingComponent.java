package org.arydz.usermanagement.domain;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class HashingComponent {

    /**
     * FYI: Only for this project purpose MD5 hashing function is used
     * Normally I'm avoiding deprecated methods.
     */
    public String guavaMd5(String data) {
        return Hashing.md5().hashString(data, UTF_8).toString();
    }
}
