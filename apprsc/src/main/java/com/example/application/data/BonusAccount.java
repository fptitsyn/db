package com.example.application.data;
import jakarta.persistence.*;

@Entity
@Table(name = "bonus_account")
public class BonusAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bonus_account_id;

    private Long bonus_account_number;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Clients clients;

    // Геттеры
    public Long getId() {
        return bonus_account_id;
    }
    public Long getBonusAccountNumber() {
        return bonus_account_number;
    }
    public Clients getClients() {
        return clients;
    }
    // Сеттеры
    public void setId(Long id) {
        this.bonus_account_id = id;
    }
    public void setBonusAccountNumber(Long bonus_account_number) {
        this.bonus_account_number = bonus_account_number;
    }
    public void setClients(Clients clients) {
        this.clients = clients;
    }
}
