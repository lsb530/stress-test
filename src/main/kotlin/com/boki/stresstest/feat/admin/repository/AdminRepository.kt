package com.boki.stresstest.feat.admin.repository

import com.boki.stresstest.feat.admin.entity.AdminEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<AdminEntity, Long> {
    fun findAdminByEmail(email: String): AdminEntity?
}