package com.pitang.desafiopitangapi.domain.model;

import lombok.Builder;

@Builder
public record ApiError(String message, Integer errorCode) {}