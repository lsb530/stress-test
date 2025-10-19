package com.boki.stresstest.provision

import com.boki.stresstest.ext.logger
import com.boki.stresstest.feat.admin.entity.AdminEntity
import com.boki.stresstest.feat.admin.repository.AdminRepository
import com.boki.stresstest.feat.user.entity.UserEntity
import com.boki.stresstest.feat.user.repository.UserRepository
import com.boki.stresstest.security.role.UserRole
import com.boki.stresstest.util.MobileNumberGenerator
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Profile(value = ["local", "default", "dev"])
@Component
class DataInitializer(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        initAdmin()
        initUsers()
    }

    @Transactional
    fun initAdmin() {
        adminRepository.deleteAll()
        if (adminRepository.count() == 0L) {
            val admin = AdminEntity(
                name = "admin",
                email = "admin@machuda.kr",
                password = "admin1234!@",
                contact = "010-1234-5678"
            )
            adminRepository.save(admin)
        }
    }

    @Transactional
    fun initUsers() {
        if (userRepository.count() == 0L) {
            val users = listOf(
                UserEntity(
                    email = "guest@machudar.kr",
                    name = "guest",
                    password = "test",
                    contact = MobileNumberGenerator.generateRandomMobileNumber(),
                    role = UserRole.GUEST,
                ),
                UserEntity(
                    email = "test@machudar.kr",
                    name = "tester",
                    password = "test",
                    contact = MobileNumberGenerator.generateRandomMobileNumber(),
                ),
            )
            userRepository.saveAll(users)
        }
        logger.info { "유저 초기화 성공" }
    }
}