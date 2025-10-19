package com.boki.stresstest.feat.user.entity

import com.boki.stresstest.common.converter.PasswordConverter
import com.boki.stresstest.common.entity.Auditable
import com.boki.stresstest.security.role.UserRole
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.ParamDef

@FilterDef(
    name = "activeUserFilter",
    parameters = [ParamDef(name = "isActive", type = Boolean::class)]
)
@Filter(name = "activeUserFilter", condition = "is_active = :isActive")
@Entity
@Table(name = "users")
@DynamicUpdate
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = false, nullable = false, updatable = false)
    var email: String,

    @JsonIgnore
    @Column(nullable = false)
    @Convert(converter = PasswordConverter::class)
    var password: String,

    @Column(length = 100, nullable = false)
    var name: String,

    @Column(unique = true, length = 20)
    var contact: String,

    var isActive: Boolean = true,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val role: UserRole = UserRole.USER
) : Auditable()