package com.akamsi.dto;

public class UbahPasswordDTO {

    private String passwordLama;
    private String passwordBaru;
    private String konfirmasiPassword;

    public UbahPasswordDTO() {
    }

    public UbahPasswordDTO(String passwordLama, String passwordBaru, String konfirmasiPassword) {
        this.passwordLama = passwordLama;
        this.passwordBaru = passwordBaru;
        this.konfirmasiPassword = konfirmasiPassword;
    }

    public String getPasswordLama() {
        return passwordLama;
    }

    public void setPasswordLama(String passwordLama) {
        this.passwordLama = passwordLama;
    }

    public String getPasswordBaru() {
        return passwordBaru;
    }

    public void setPasswordBaru(String passwordBaru) {
        this.passwordBaru = passwordBaru;
    }

    public String getKonfirmasiPassword() {
        return konfirmasiPassword;
    }

    public void setKonfirmasiPassword(String konfirmasiPassword) {
        this.konfirmasiPassword = konfirmasiPassword;
    }
}
