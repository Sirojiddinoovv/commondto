package uz.nodir.common.model.dto.core.response


/**
@author: Nodir
@date: 18.04.2025
@group: Meloman

 **/
sealed interface HasResult {
    fun isSuccess(): Boolean

}