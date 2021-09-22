package com.zup.propostaservice.carteira;

public class CarteiraDto {

    private Long id;

    private String email;

    private CarteiraEnum carteira;

    public CarteiraDto(Long id, String email, CarteiraEnum carteira) {
        this.id = id;
        this.email = email;
        this.carteira = carteira;
    }

    public CarteiraDto(Carteira carteira) {
        this.id = carteira.getId();
        this.email = carteira.getEmail();
        this.carteira = carteira.getCarteira();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public CarteiraEnum getCarteira() {
        return carteira;
    }
}
