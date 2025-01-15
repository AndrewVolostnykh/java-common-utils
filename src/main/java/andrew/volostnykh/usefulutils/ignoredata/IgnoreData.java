package andrew.volostnykh.usefulutils.ignoredata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnoreData {

    String IGNORE_TEST_DATA_FILTER = "ignoreTestData";
    String IGNORE_DELETED_DATA_FILTER = "ignoreDeletedData";

    boolean test() default false;

    boolean deleted() default true;

}
