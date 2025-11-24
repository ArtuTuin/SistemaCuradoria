package model;
public class Resource {
    private int id;
    private int usuarioId;
    private String titulo;
    private String autor;
    private String categoria;
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getUsuarioId(){return usuarioId;} public void setUsuarioId(int v){usuarioId=v;}
    public String getTitulo(){return titulo;} public void setTitulo(String t){titulo=t;}
    public String getAutor(){return autor;} public void setAutor(String a){autor=a;}
    public String getCategoria(){return categoria;} public void setCategoria(String c){categoria=c;}
}
