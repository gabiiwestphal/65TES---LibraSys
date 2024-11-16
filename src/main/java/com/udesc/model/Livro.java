package com.udesc.model;

//Classe Livro
public class Livro {
	private static int contador = 1;
	private int id;
	private String titulo;
	private String autor;
	private int anoPublicacao;

	public Livro(String titulo, String autor, int anoPublicacao) {
		this.id = contador++;
		this.titulo = titulo;
		this.autor = autor;
		this.anoPublicacao = anoPublicacao;
	}

	public int getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public int getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(int anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	
	public void setId(int id) {
	    this.id = id;
	}

	@Override
	public String toString() {
		return "Livro{" + "id=" + id + ", titulo='" + titulo + '\'' + ", autor='" + autor + '\'' + ", anoPublicacao="
				+ anoPublicacao + '}';
	}
}