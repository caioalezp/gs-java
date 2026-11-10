package br.com.fiap.citizentech.exception;

/** Lançada quando há conflito com dados já existentes, como e-mail duplicado (HTTP 409). */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
