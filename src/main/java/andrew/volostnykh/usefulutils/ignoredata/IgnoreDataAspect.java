package andrew.volostnykh.usefulutils.ignoredata;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
class IgnoreDataAspect {

    private final EntityManager em;

    @Around("@annotation(IgnoreData)")
    public Object ignoreTestDataAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Session session = getSession();
        boolean filterIgnoreTestDataEnabled = false;
        boolean filterIgnoreDeletedDataEnabled = false;

        if(isFilterIgnoreTestDataDisabled(session)) {
            session.enableFilter(IgnoreData.IGNORE_TEST_DATA_FILTER);
            filterIgnoreTestDataEnabled = true;
        }

        if(isFilterIgnoreDeletedDataDisabled(session)) {
            session.enableFilter(IgnoreData.IGNORE_DELETED_DATA_FILTER);
            filterIgnoreDeletedDataEnabled = true;
        }

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            if (filterIgnoreTestDataEnabled) {
                session.disableFilter(IgnoreData.IGNORE_TEST_DATA_FILTER);
            }

            if (filterIgnoreDeletedDataEnabled) {
                session.disableFilter(IgnoreData.IGNORE_DELETED_DATA_FILTER);
            }
        }
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

    private boolean isFilterIgnoreTestDataDisabled(Session session) {
        return session.getEnabledFilter(IgnoreData.IGNORE_TEST_DATA_FILTER) == null;
    }

    private boolean isFilterIgnoreDeletedDataDisabled(Session session) {
        return session.getEnabledFilter(IgnoreData.IGNORE_DELETED_DATA_FILTER) == null;
    }
}
