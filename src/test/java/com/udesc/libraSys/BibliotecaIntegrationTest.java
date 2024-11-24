package com.udesc.libraSys;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.udesc.service.BibliotecaService;
import com.udesc.database.DatabaseConnection;
import com.udesc.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BibliotecaIntegrationTest {

    private static BibliotecaService bibliotecaService;

    @BeforeAll
    public static void setUp() {
        bibliotecaService = new BibliotecaService();
    }

    @BeforeEach
    public void limparBanco() throws SQLException {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM livros")) {
            stmt.executeUpdate();
        }
    }

    @Test
    @Order(1)
    public void testAdicionarLivroComDadosValidos() {
        bibliotecaService.adicionarLivro("O Senhor dos Anéis", "J.R.R. Tolkien", 1954);
        List<Livro> livros = bibliotecaService.listarLivros();
        assertEquals(1, livros.size(), "O livro deveria ser salvo no banco de dados.");
        assertEquals("O Senhor dos Anéis", livros.get(0).getTitulo());
    }

    @Test
    @Order(2)
    public void testAdicionarLivroComTituloVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("", "Autor Teste", 2020);
        });
        assertEquals("Título inválido - não pode ser vazio.", exception.getMessage());
    }

    @Test
    @Order(3)
    public void testAdicionarLivroComAutorVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("Livro Teste", "", 2020);
        });
        assertEquals("Autor inválido - não pode ser vazio.", exception.getMessage());
    }

    @Test
    @Order(4)
    public void testRemoverLivroExistente() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2020);
        Livro livro = bibliotecaService.listarLivros().get(0);
        assertTrue(bibliotecaService.removerLivro(livro.getId()), "O livro deveria ser removido.");
        List<Livro> livros = bibliotecaService.listarLivros();
        assertTrue(livros.isEmpty(), "O banco deveria estar vazio após a remoção.");
    }

    @Test
    @Order(5)
    public void testRemoverLivroInexistente() {
        boolean resultado = bibliotecaService.removerLivro(999);
        assertFalse(resultado, "Não deveria ser possível remover um livro inexistente.");
    }

    @Test
    @Order(6)
    public void testContarLivrosBibliotecaVazia() {
        long contagem = bibliotecaService.contarLivros();
        assertEquals(0, contagem, "A contagem deveria ser 0 para uma biblioteca vazia.");
    }

    @Test
    @Order(7)
    public void testContarLivrosComLivrosAdicionados() {
        bibliotecaService.adicionarLivro("Livro 1", "Autor 1", 2020);
        bibliotecaService.adicionarLivro("Livro 2", "Autor 2", 2021);
        bibliotecaService.adicionarLivro("Livro 3", "Autor 3", 2022);

        long contagem = bibliotecaService.contarLivros();
        assertEquals(3, contagem, "A contagem deveria refletir o número de livros adicionados.");
    }

    @Test
    @Order(8)
    public void testAtualizarAnoDePublicacaoExistente() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2020);
        Livro livro = bibliotecaService.listarLivros().get(0);
        boolean atualizado = bibliotecaService.atualizarAnoPublicacao(livro.getId(), 2021);

        assertTrue(atualizado, "O ano de publicação deveria ser atualizado com sucesso.");
        Livro livroAtualizado = bibliotecaService.listarLivros().get(0);
        assertEquals(2021, livroAtualizado.getAnoPublicacao(), "O banco deveria refletir o novo ano de publicação.");
    }

    @Test
    @Order(9)
    public void testAtualizarAnoDePublicacaoInexistente() {
        boolean atualizado = bibliotecaService.atualizarAnoPublicacao(999, 2021);
        assertFalse(atualizado, "Não deveria ser possível atualizar o ano de um livro inexistente.");
    }

    @Test
    @Order(10)
    public void testListarLivrosPorAnoExistente() {
        bibliotecaService.adicionarLivro("Livro 1", "Autor 1", 2020);
        bibliotecaService.adicionarLivro("Livro 2", "Autor 2", 2020);
        bibliotecaService.adicionarLivro("Livro 3", "Autor 3", 2021);

        List<Livro> livros = bibliotecaService.listarLivrosPorAno(2020);
        assertEquals(2, livros.size(), "Deveria retornar exatamente 2 livros publicados em 2020.");
    }

    @Test
    @Order(11)
    public void testListarLivrosPorAnoInexistente() {
        List<Livro> livros = bibliotecaService.listarLivrosPorAno(1990);
        assertTrue(livros.isEmpty(), "Não deveria retornar nenhum livro para um ano inexistente.");
    }
}
