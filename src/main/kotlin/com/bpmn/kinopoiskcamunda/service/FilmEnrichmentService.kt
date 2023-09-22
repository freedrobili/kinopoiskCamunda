package com.bpmn.kinopoiskcamunda.service

import com.bpmn.kinopoiskcamunda.feignClient.KinopoiskFeignClient
import com.bpmn.kinopoiskcamunda.repository.FilmRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FilmEnrichmentService(
    @Autowired
    private val kinopoiskFeignClient: KinopoiskFeignClient,

    @Autowired
    private val filmRepository: FilmRepository
){
//    fun enrichFilmData(keyword: String) : Long?{
//        val filmSearchResponse = kinopoiskFeignClient.searchFilm(keyword)
//
//        if (filmSearchResponse?.films?.isNotEmpty() == true){
//            val filmInfo = filmSearchResponse.films[0]
//            val existingFilm = filmRepository.findByTitle(filmInfo.nameRu ?: "")
//
//            return existingFilm?.id
//        } else {
//            return null
//        }
//    }

    fun enrichFilmData(keyword: String): List<Long>? {
        val filmSearchResponse = kinopoiskFeignClient.searchFilm(keyword)
        val filmIds = mutableListOf<Long>()

        if (filmSearchResponse?.films?.isNotEmpty() == true) {
            for (filmInfo in filmSearchResponse.films!!) {
                val existingFilm = filmRepository.findByTitle(filmInfo.nameRu ?: "")
                existingFilm?.id?.let { filmIds.add(it) }
            }
        }

        return if (filmIds.isNotEmpty()) filmIds else null
    }

}