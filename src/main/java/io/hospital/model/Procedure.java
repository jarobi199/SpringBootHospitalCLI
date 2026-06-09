package io.hospital.model;

import java.time.LocalDateTime;

public record Procedure (String name, LocalDateTime performanceDate, String doctorId, String outcome, String notes){}