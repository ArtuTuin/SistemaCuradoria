package com.curadoria.model;

public class Resource {

    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private String pensamento;
    private int usuarioId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPensamento() { return pensamento; }
    public void setPensamento(String pensamento) { this.pensamento = pensamento; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
}
