package com.boki.stresstest.feat.admin.entity

import com.boki.stresstest.common.converter.DataConverter
import com.boki.stresstest.common.converter.PasswordConverter
import com.boki.stresstest.common.entity.Auditable
import com.boki.stresstest.security.role.UserRole
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "admin")
class AdminEntity(
    @Column(name = "name", length = 80)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @JsonIgnore
    @Column(nullable = false)
    @Convert(converter = PasswordConverter::class)
    var password: String,

    @Convert(converter = DataConverter::class)
    var contact: String,

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val role: UserRole = UserRole.ADMIN,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : Auditable() {

    override fun toString(): String {
        return "AdminEntity(id=$id, name='$name', email='$email', contact='$contact', role=${role.name})"
    }

}