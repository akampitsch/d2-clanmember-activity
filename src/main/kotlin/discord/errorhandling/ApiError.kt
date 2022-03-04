package discord.errorhandling

import org.springframework.http.HttpStatus

class ApiError {
    //
    private var status: HttpStatus? = null
    var message: String? = null
    private var errors: List<String>? = null

    //
    constructor(status: HttpStatus?, message: String?, errors: List<String>?) : super() {
        this.status = status
        this.message = message
        this.errors = errors
    }

    constructor(status: HttpStatus?, message: String?, error: String) : super() {
        this.status = status
        this.message = message
        errors = listOf(error)
    }

    fun setError(error: String) {
        errors = listOf(error)
    }
}