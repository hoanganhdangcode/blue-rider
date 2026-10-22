package com.rideplatform.blueride.core.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AppResultTest {

    @Test
    fun error_isAssignableToAnyResultType() {
        // Compile được nghĩa là `out T` + `AppResult<Nothing>` hoạt động:
        // cùng một Error dùng cho AppResult<String> và AppResult<List<Int>>.
        val error = AppResult.Error(AppError.SessionExpired)
        val asString: AppResult<String> = error
        val asList: AppResult<List<Int>> = error

        assertEquals<AppResult<Any>>(asString, asList)
    }

    @Test
    fun success_isSubtypeOfWiderResultType() {
        // AppResult<String> gán được vào AppResult<Any> nhờ `out T`.
        val narrow: AppResult<String> = AppResult.Success("ride")
        val wide: AppResult<Any> = narrow

        assertEquals(AppResult.Success("ride"), wide)
    }

    @Test
    fun when_coversAllCasesWithoutElse() {
        val results: List<AppResult<Int>> = listOf(
            AppResult.Success(1),
            AppResult.Error(AppError.Server(httpStatus = 500, message = null)),
        )

        // Không có nhánh `else`: thêm case mới vào AppResult sẽ làm dòng này lỗi compile.
        val labels = results.map { result ->
            when (result) {
                is AppResult.Success -> "ok:${result.data}"
                is AppResult.Error -> "error:${result.error::class.simpleName}"
            }
        }

        assertEquals(listOf("ok:1", "error:Server"), labels)
    }

    @Test
    fun dataClass_comparesByValue() {
        assertEquals(AppResult.Success(listOf(1, 2)), AppResult.Success(listOf(1, 2)))
        assertNotEquals<AppResult<Int>>(AppResult.Success(1), AppResult.Success(2))
    }
}
