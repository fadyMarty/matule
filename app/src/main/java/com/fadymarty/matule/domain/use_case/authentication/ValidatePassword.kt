package com.fadymarty.matule.domain.use_case.authentication

import com.fadymarty.matule.common.util.ValidationResult

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}