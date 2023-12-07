package com.nobita.microservices.accountservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAccountDTO {
    @NotBlank(message = "Account Type cannot be empty")
    private String accountType;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private double balance;
    private double interestRate;
    @Positive(message = "Please assign valid customer Id to account")
    private int customerId;
}
