package com.humingamelab.mc.api;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Mutation;
import com.apollographql.apollo3.api.Query;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.humingamelab.mc.Client.type.LogInput;
import com.humingamelab.mc.Client.type.LogType;
import com.humingamelab.mc.Clientoperation.SubmitLogMutation;
import io.reactivex.rxjava3.core.Single;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class Api {
    ApolloClient client;

    public Api() {
        this.client = ApolloClient.builder()
                .serverUrl("https://lapis.drewh.net/query")
                .build();
    }

    public void log(LogType type, String playerName, @Nullable String message, @Nullable HashMap<String, Object> attributes) {
        LogInput input = LogInput.builder()
                .message(message)
                .playerName(playerName)
                .type(type)
                .attributes(attributes)
                .build();

        Runnable r = () -> {
            SubmitLogMutation m = SubmitLogMutation.builder().input(input).build();
            try {
                SubmitLogMutation.Data add = this.executeMutation(m).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(r).start();
    }

    public <T extends Query.Data> Future<T> executeQuery(Query<T> query) {
        ApolloCall<T> queryCall = client.query(query);
        Single<ApolloResponse<T>> queryResponse = Rx3Apollo.single(queryCall);
        CompletableFuture<T> future = new CompletableFuture<>();
        queryResponse.subscribe(response -> {
            if (!response.hasErrors()) {
                future.complete(response.data);
            } else {
                future.completeExceptionally(new RuntimeException("GraphQL query returned errors: " + response.errors));
            }
        }, throwable -> {
            future.completeExceptionally(throwable);
        });

        return future;
    }

    public <T extends Mutation.Data> Future<T> executeMutation(Mutation<T> mut) {
        ApolloCall<T> mutationCall = client.mutation(mut);
        Single<ApolloResponse<T>> queryResponse = Rx3Apollo.single(mutationCall);
        CompletableFuture<T> future = new CompletableFuture<>();
        queryResponse.subscribe(response -> {
            if (!response.hasErrors()) {
                future.complete(response.data);
            } else {
                future.completeExceptionally(new RuntimeException("GraphQL query returned errors: " + response.errors));
            }
        }, throwable -> {
            future.completeExceptionally(throwable);
        });
        return future;
    }
}
