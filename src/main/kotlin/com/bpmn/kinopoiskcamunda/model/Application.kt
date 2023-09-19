package com.bpmn.kinopoiskcamunda.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "application")
data class Application(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Enumerated(EnumType.STRING)
        val status: ApplicationStatus,

        @Column(name = "creation_data")
        val creationData: LocalDateTime,

        @Column(name = "update_date")
        val updateDate: LocalDateTime,

        val keyword: String,

        @ManyToOne
        @JoinColumn(name = "film_id")
        val film: Film? = null
)
