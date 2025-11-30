package com.maksymchernenko.bookingservice.client.dto.screening;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatsResponse {

    private String lockToken;
}
