package com.fadymarty.matule.domain.use_case.authentication

import com.fadymarty.matule.common.util.ValidationResult

class ValidateName {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The name can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}