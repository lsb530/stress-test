package com.boki.stresstest.aop

import com.boki.stresstest.common.annotation.ActiveUserFilter
import jakarta.persistence.EntityManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.hibernate.Session
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
class HibernateActiveUserFilterAspect(private val em: EntityManager) {

    @Around("@annotation(transactional)")
    fun applyDefaultFilter(pjp: ProceedingJoinPoint, transactional: Transactional): Any? {
        val method = (pjp.signature as MethodSignature).method

        val override =
            AnnotationUtils.findAnnotation(method, ActiveUserFilter::class.java)
                ?: AnnotationUtils.findAnnotation(pjp.target.javaClass, ActiveUserFilter::class.java)

        val isActive = override?.value ?: true

        val session = em.unwrap(Session::class.java)
        session.enableFilter("activeUserFilter").setParameter("isActive", isActive)
        try {
            return pjp.proceed()
        } finally {
            session.disableFilter("activeUserFilter")
        }
    }

}
