package com.gmail.andersoninfonet.gpc.models.exceptions;

import java.time.Instant;
import java.util.List;

public record ExceptionDetails(
        String title,
        int status,
        String details,
        String className,
        Instant timestamp,
        List<GpcValidationExceptionDetails> validations
) {
}
