package com.nobita.microservices.transactionservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTransactionDTO {
    @NotBlank(message = "Account number cannot be blank")
    private String accountNum;
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotBlank(message = "Initial balance cannot be empty")
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private double amount;

}
