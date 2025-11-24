package model;

public class InteresseUsuario {
    private int id;
    private int usuarioId;
    private String categoria; // IA / CIBER / PRIVACIDADE

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
