package ir.neobank.ariapay.core.common.result

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AriaResultTest {

    @Test
    fun mapConvertsSuccessDataAndKeepsErrorUnchanged() {
        val success = AriaResult.Success(2).map { it * 10 }
        val error = AriaResult.Error(IllegalStateException("boom")).map { 1 }

        assertThat(success).isEqualTo(AriaResult.Success(20))
        assertThat(error).isInstanceOf(AriaResult.Error::class.java)
    }
}
