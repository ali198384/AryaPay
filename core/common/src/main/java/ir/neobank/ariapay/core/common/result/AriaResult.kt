package ir.neobank.ariapay.core.common.result

sealed interface AriaResult<out T> {
    data class Success<T>(val data: T) : AriaResult<T>
    data class Error(val exception: Throwable) : AriaResult<Nothing>
    data object Loading : AriaResult<Nothing>
}

inline fun <T, R> AriaResult<T>.map(transform: (T) -> R): AriaResult<R> = when (this) {
    is AriaResult.Success -> AriaResult.Success(transform(data))
    is AriaResult.Error -> this
    AriaResult.Loading -> AriaResult.Loading
}
