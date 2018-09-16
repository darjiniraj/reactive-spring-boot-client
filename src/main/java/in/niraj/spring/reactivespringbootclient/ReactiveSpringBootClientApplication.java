package in.niraj.spring.reactivespringbootclient;

import in.niraj.spring.reactivespringbootclient.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ReactiveSpringBootClientApplication {

    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8080/api");
    }


    @Bean
    CommandLineRunner commandLineRunner(WebClient webClient) {
        return args -> {

            webClient
                    .get()
                    .uri("/product")
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .filter(product -> product.getName().equals("Dell Laptop"))
                    .flatMap(product -> webClient.get()
                            .uri("/{id}/event", product.getId())
                            .retrieve()
                            .bodyToFlux(Product.class)).subscribe(System.out::println);

            for (int i = 0; i < 100; i++) {

                System.out.println("non blocking " + i);
                Thread.sleep(1000);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveSpringBootClientApplication.class, args);


    }
}
