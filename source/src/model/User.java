package model;
public class User {
    private int id;
    private String nome;
    private int idade;
    private String tipo;
    private String email;
    private boolean ativo;
    private String senhaPlain; // used on creation only
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNome(){return nome;} public void setNome(String n){nome=n;}
    public int getIdade(){return idade;} public void setIdade(int i){idade=i;}
    public String getTipo(){return tipo;} public void setTipo(String t){tipo=t;}
    public String getEmail(){return email;} public void setEmail(String e){email=e;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean a){ativo=a;}
    public String getSenhaPlain(){return senhaPlain;} public void setSenhaPlain(String s){senhaPlain=s;}
}
