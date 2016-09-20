package demo.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class HelloClient {

    /*
     * @Autowired HelloService helloService;
     */

    /*
     * @Autowired private DiscoveryClient client;
     */

    @Autowired
    private RestTemplate restTemplate;

    /**
     * LoadBalanced 注解表明restTemplate使用LoadBalancerClient执行请求
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) template.getRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return template;
    }

    @RequestMapping("/hello")
    public String hello(@RequestParam MultiValueMap<String, String> params) {

        String name = params.getFirst("name");
        //使用 restTemplate访问远程rest服务
        String o = restTemplate.getForObject("http://spring-boot-service/hi?name={name}", String.class, name);

        System.out.println("result:" + o);
        return "result:" + o;
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloClient.class, args);
    }

}
