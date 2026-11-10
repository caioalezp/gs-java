package br.com.fiap.citizentech;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CitizenTechApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        diagnosticarVariaveisDoBanco();
        SpringApplication.run(CitizenTechApplication.class, args);
    }

    /** Mostra nos logs o que chegou nas variáveis do banco, sem revelar a senha. */
    private static void diagnosticarVariaveisDoBanco() {
        String usuario = System.getenv("DB_USER");
        String senha = System.getenv("DB_PASSWORD");
        String url = System.getenv("DB_URL");
        System.out.println("[DIAGNOSTICO] DB_USER=" + (usuario == null ? "NAO DEFINIDA"
                : "'" + usuario + "' (" + usuario.length() + " caracteres)"));
        System.out.println("[DIAGNOSTICO] DB_PASSWORD=" + (senha == null ? "NAO DEFINIDA"
                : senha.length() + " caracteres"));
        System.out.println("[DIAGNOSTICO] DB_URL=" + (url == null ? "padrao (oracle.fiap.com.br:1521:ORCL)" : url));
    }
}
