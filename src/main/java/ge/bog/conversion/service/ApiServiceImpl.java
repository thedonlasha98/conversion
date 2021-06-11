package ge.bog.conversion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${api.rate.info.url}")
    private String infoUrl;

    private final RestTemplate restTemplate;

    public ApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BigDecimal getRateInfo(String ccy) {
        ResponseEntity<Double> response = restTemplate.exchange(
                infoUrl + "ccy/" + ccy,
                HttpMethod.GET,
                null,
                Double.class);
        return BigDecimal.valueOf(response.getBody());
    }
}
