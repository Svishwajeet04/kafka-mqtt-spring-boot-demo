package com.example.demo.models;

import lombok.Builder;

@Builder
public record AlertViolationCheck(
        String alertViolationOf, Object requiredValue, Object actualValue
) {
}
