package andrew.volostnykh.usefulutils;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@Component
@NoArgsConstructor(access = PRIVATE)
public class PaginationUtils {

    public static <T> int iterateThroughPages(Function<Pageable, Page<T>> query, Pageable firstPage, Consumer<T> toDo) {
        AtomicInteger numberOfProcessed = new AtomicInteger(0);
        Page<T> entity;
        do {
            entity = query.apply(firstPage);
            entity.forEach(contract -> {
                numberOfProcessed.incrementAndGet();
                toDo.accept(contract);
            });
            firstPage = entity.nextPageable();
        } while (firstPage.isPaged());

        return numberOfProcessed.get();
    }

    public static <T> int iterateThroughPages(Function<Pageable, Page<T>> query, int batchSize, Consumer<T> toDo) {
        return iterateThroughPages(query, PageRequest.of(0, batchSize, Sort.by("id")), toDo);
    }
}
