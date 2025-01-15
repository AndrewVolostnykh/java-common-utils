package andrew.volostnykh.usefulutils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class Futures {

    public <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.<T>toList())
        );
    }

}
