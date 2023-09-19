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
    fun enrichFilmData(keyword: String) : Long?{
        val filmInfoList = kinopoiskFeignClient.searchFilm(keyword)

        if (filmInfoList.isNotEmpty()){
            val filmInfo = filmInfoList[0]
            val existingFilm = filmRepository.findByTitle(filmInfo.title)

            return existingFilm?.id
            } else {
            return null
        }
    }
}