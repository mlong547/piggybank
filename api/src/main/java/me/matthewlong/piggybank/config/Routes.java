package me.matthewlong.piggybank.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import me.matthewlong.piggybank.controller.TransactionController;

@Configuration
public class Routes {
    @Bean
    RouterFunction<ServerResponse> transactionsRouter(TransactionController controller) {
        return RouterFunctions.route(GET("transactions/{id}"), controller::get)
                .andRoute(POST("/transactions"), controller::create)
                .andRoute(GET("/transactions"), controller::list);
    }
}
