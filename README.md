<br/>
<div align="center">
  <h3 align="center">Klinik - Java GUI</h3>

  <p align="center">
    Program Java GUI untuk Klinik menggunakan JavaFX
  </p>
</div>

<br/>

## Cara men-compile program

- Pastikan JDK terinstall di sistem dan clone repository

- Lakukan import database menggunakan file `db/Final Clinic.sql`, setelah sukes import database;

- Lakukan penyesuaian URL dan kredensial database pada file `Database.java` pada line ke-10 hingga line ke-12

- Buka terminal di direktori hasil clone

- Compile dan jalankan program menggunakan perintah dibawah ini

```zsh
javac --module-path ./deps --add-modules javafx.controls Clinic.java && java --module-path ./deps --add-modules javafx.controls Clinic.java
```

## Notes

- Repo ini telah ter pre-compiled oleh OpenJDK 21.0.1 pada sistem operasi macOS Sonoma 14.4.1

- Untuk menggunakan pre-compiled binary, pasikan repo terclone dan terminal terbuka pada direktori root projek

- Pastikan Java terinstall dan jalankan perintah berikut

```zsh
java --module-path ./deps --add-modules javafx.controls Clinic.java
```

## Credit

- 2702349666 - Yusril Dhiyaul Haq Muttaqien
