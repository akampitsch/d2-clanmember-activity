package discord.errorhandling

import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDate
import java.util.stream.Collectors
import javax.naming.AuthenticationException
import javax.persistence.EntityNotFoundException
import javax.persistence.NoResultException


@ControllerAdvice
class ControllerExceptionsHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        ConstraintViolationException::class,
        HttpClientErrorException.BadRequest::class,
        IllegalArgumentException::class
    )
    fun constraintViolationException(e: Exception): ResponseEntity<ApiError> {
        return generateApiError(HttpStatus.BAD_REQUEST, "Bad request", e)
    }

//    @ExceptionHandler(AuthorizationException::class)
//    fun unauthorizedException(e: Exception): ResponseEntity<ApiError> {
//        return generateApiError(HttpStatus.FORBIDDEN, "You are not authorized to do this operation", e)
//    }

    @ExceptionHandler(AuthenticationException::class)
    fun forbiddenException(e: Exception): ResponseEntity<ApiError> {
        return generateApiError(HttpStatus.UNAUTHORIZED, "You are not allowed to do this operation", e)
    }

    @ExceptionHandler(
        EntityNotFoundException::class,
        NoSuchElementException::class,
        NoResultException::class,
        EmptyResultDataAccessException::class,
        IndexOutOfBoundsException::class,
    )
    fun notFoundException(e: Exception): ResponseEntity<ApiError> {
        return generateApiError(HttpStatus.NOT_FOUND, "Resource not found", e)
    }

    @ExceptionHandler(
        Exception::class
    )
    fun internalServerErrorException(e: Exception): ResponseEntity<ApiError> {
        return generateApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Generic internal error", e)
    }

    private fun generateApiError(
        status: HttpStatus,
        message: String,
        e: Exception
    ): ResponseEntity<ApiError> {
        // converting the exception stack trace to a string
        return ResponseEntity(ApiError(status, message, e.localizedMessage ?: ""), status)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body["timestamp"] = LocalDate.now()
        body["status"] = status.value()
        val errors = ex.bindingResult
            .fieldErrors
            .stream()
            .map { x: FieldError -> x.defaultMessage }
            .collect(Collectors.toList())
        body["errors"] = errors
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }
}