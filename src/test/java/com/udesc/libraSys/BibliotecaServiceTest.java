package com.udesc.libraSys;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.udesc.service.BibliotecaService;
import com.udesc.model.Livro;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BibliotecaServiceTest {

    private static BibliotecaService bibliotecaService;

    @BeforeAll
    public static void setUp() {
        bibliotecaService = new BibliotecaService();
    }

    @BeforeEach
    public void limparBanco() {
        List<Livro> livros = bibliotecaService.listarLivros();
        for (Livro livro : livros) {
            bibliotecaService.removerLivro(livro.getId());
        }
    }
    
    @Test
    @Order(1)
    public void testAdicionarLivroComTituloVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("", "Autor Teste", 2023);
        });
        assertEquals("Título inválido - não pode ser vazio.", exception.getMessage());
    }
    
    @Test
    @Order(2)
    public void testAdicionarLivroComAutorVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("Livro Teste", "", 2023);
        });
        assertEquals("Autor inválido - não pode ser vazio.", exception.getMessage());
    }

    @Test
    @Order(3)
    public void testAdicionarLivroComAnoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", -2023);
        });
        assertEquals("Ano inválido - deve ser um número positivo.", exception.getMessage());
    }


    @Test
    @Order(4)
    public void testAdicionarLivroComDadosValidos() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
        List<Livro> livros = bibliotecaService.listarLivros();
        assertEquals(1, livros.size(), "O livro não foi adicionado corretamente.");
        assertEquals("Livro Teste", livros.get(0).getTitulo());
    }


    @Test
    @Order(5)
    public void testAdicionarLivroDuplicado() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
        });
        assertEquals("Erro: Livro duplicado - já existe um livro com o mesmo título e autor", exception.getMessage());
    }


    @Test
    @Order(6)
    public void testBuscarLivroPorTitulo() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
        List<Livro> livros = bibliotecaService.buscarLivros("Livro Teste");
        assertEquals(1, livros.size(), "O livro deveria ser encontrado.");
        assertEquals("Livro Teste", livros.get(0).getTitulo());
    }

    @Test
    @Order(7)
    public void testBuscarLivroInexistente() {
        List<Livro> livros = bibliotecaService.buscarLivros("Inexistente");
        assertTrue(livros.isEmpty(), "Nenhum livro deveria ser encontrado.");
    }

    @Test
    @Order(8)
    public void testRemoverLivroExistente() {
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
        Livro livro = bibliotecaService.listarLivros().get(0);
        assertTrue(bibliotecaService.removerLivro(livro.getId()), "O livro deveria ser removido.");
    }

    @Test
    @Order(9)
    public void testRemoverLivroInexistente() {
        assertFalse(bibliotecaService.removerLivro(999), "O livro inexistente não deveria ser removido.");
    }

    @Test
    @Order(10)
    public void testVerDetalhesDoLivro() {
        // Adicionar um livro ao banco
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);

        // Buscar o livro adicionado
        Optional<String> detalhesOpt = bibliotecaService.verDetalhesDoLivro(1); // ID do primeiro livro inserido

        // Verificar que o método retorna algo
        assertTrue(detalhesOpt.isPresent(), "Os detalhes do livro deveriam ser retornados.");

        // Validar que o método está incorreto porque retorna apenas o título
        String tituloRetornado = detalhesOpt.get();
        assertEquals("Livro Teste", tituloRetornado, "O título retornado está correto, mas o restante dos detalhes está ausente.");

        // Forçar falha porque deveria retornar um objeto completo
        fail("O método está retornando apenas o título, mas deveria retornar todos os detalhes do livro (título, autor e ano).");
    }

    @Test
    @Order(11)
    public void testAtualizarAnoDePublicacaoExistente() {
        // Adicionar um livro ao banco
        bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2020);

        // Obter o livro adicionado
        Livro livro = bibliotecaService.listarLivros().get(0);

        // Atualizar o ano de publicação
        boolean atualizado = bibliotecaService.atualizarAnoPublicacao(livro.getId(), 2021);
        
        // Verificar se o método retornou sucesso
        assertTrue(atualizado, "O método indicou sucesso na atualização.");

        // Validar se o banco de dados realmente foi atualizado
        Livro livroAtualizado = bibliotecaService.listarLivros().get(0);
        assertEquals(2021, livroAtualizado.getAnoPublicacao(), 
            "O banco de dados não foi atualizado com o novo ano de publicação.");
    }

    @Test
    @Order(12)
    public void testAtualizarAnoDePublicacaoParaLivroInexistente() {
        boolean atualizado = bibliotecaService.atualizarAnoPublicacao(999, 2024);
        assertFalse(atualizado, "O ano de publicação não deveria ser atualizado para um livro inexistente.");
    }
    
  @Test
  @Order(13)
  public void testBuscarLivroPorAutor() {
      bibliotecaService.adicionarLivro("Livro Teste", "Autor Teste", 2023);
      List<Livro> livros = bibliotecaService.buscarLivros("Autor Teste");
      assertEquals(1, livros.size(), "O livro deveria ser encontrado.");
      assertEquals("Autor Teste", livros.get(0).getAutor());
  }
    
    @Test
    @Order(14)
    public void testVerDetalhesLivroIdInvalido() {
        int idInvalido = 9999; // ID inexistente ou inválido no banco de dados
        Optional<String> titulo = bibliotecaService.verDetalhesDoLivro(idInvalido);
        assertTrue(titulo.isEmpty(), "Nenhum título deveria ser retornado para um ID inválido.");
    }

    
    @Test
    @Order(15)
    public void testListarLivrosBibliotecaVazia() {
        // Garantir que o banco esteja vazio antes do teste
        List<Livro> livros = bibliotecaService.listarLivros();
        assertTrue(livros.isEmpty(), "A lista de livros deveria estar vazia.");
    }

    
    @Test
    @Order(16)
    public void testListarLivrosComLivros() {
        // Adicionar 5 livros
        bibliotecaService.adicionarLivro("Livro 1", "Autor 1", 2020);
        bibliotecaService.adicionarLivro("Livro 2", "Autor 2", 2021);
        bibliotecaService.adicionarLivro("Livro 3", "Autor 3", 2022);
        bibliotecaService.adicionarLivro("Livro 4", "Autor 4", 2023);
        bibliotecaService.adicionarLivro("Livro 5", "Autor 5", 2024);
        
        // Listar os livros
        List<Livro> livros = bibliotecaService.listarLivros();

        // Verificar se a lista contém 5 livros
        assertEquals(5, livros.size(), "A lista de livros deveria conter 5 livros.");

        // Verificar os detalhes dos livros
        assertEquals("Livro 1", livros.get(0).getTitulo());
        assertEquals("Livro 2", livros.get(1).getTitulo());
        assertEquals("Livro 3", livros.get(2).getTitulo());
        assertEquals("Livro 4", livros.get(3).getTitulo());
        assertEquals("Livro 5", livros.get(4).getTitulo());
    }
    
    @Test
    @Order(17)
    public void testContarLivrosBibliotecaVazia() {
        // Garantir que o banco esteja vazio antes do teste
        long contagem = bibliotecaService.contarLivros();

        // Verificar se a contagem é 0
        assertEquals(0, contagem, "A contagem de livros deveria ser 0 para uma biblioteca vazia.");
    }

    @Test
    @Order(18)
    public void testContarLivrosComLivros() {
        // Adicionar 5 livros
        bibliotecaService.adicionarLivro("Livro 1", "Autor 1", 2020);
        bibliotecaService.adicionarLivro("Livro 2", "Autor 2", 2021);
        bibliotecaService.adicionarLivro("Livro 3", "Autor 3", 2022);
        bibliotecaService.adicionarLivro("Livro 4", "Autor 4", 2023);
        bibliotecaService.adicionarLivro("Livro 5", "Autor 5", 2024);
        
        // Contar os livros
        long contagem = bibliotecaService.contarLivros();

        // Verificar se a contagem é 5
        assertEquals(5, contagem, "A contagem de livros deveria ser 5 após adicionar 5 livros.");
    }

    @Test
    @Order(19)
    public void testListarLivrosAnoInexistente() {
        // Garantir que não existem livros do ano 1990 no banco
        List<Livro> livrosAno = bibliotecaService.listarLivrosPorAno(1990);

        // Verificar se a lista está vazia
        assertTrue(livrosAno.isEmpty(), "A lista de livros deveria estar vazia para um ano inexistente.");
    }

    @Test
    @Order(20)
    public void testListarLivrosAnoExistente() {
        // Adicionar livros de diferentes anos, incluindo o ano 2020
        bibliotecaService.adicionarLivro("Livro 1", "Autor 1", 2020);
        bibliotecaService.adicionarLivro("Livro 2", "Autor 2", 2020);
        bibliotecaService.adicionarLivro("Livro 3", "Autor 3", 2021);

        // Buscar livros do ano 2020
        List<Livro> livrosAno = bibliotecaService.listarLivrosPorAno(2020);

        // Verificar se a lista contém exatamente 2 livros
        assertEquals(2, livrosAno.size(), "A lista de livros para o ano 2020 deveria conter 2 livros.");

        // Verificar se os títulos dos livros correspondem aos adicionados
        assertTrue(livrosAno.stream().anyMatch(livro -> livro.getTitulo().equals("Livro 1")), "Livro 1 deveria estar na lista.");
        assertTrue(livrosAno.stream().anyMatch(livro -> livro.getTitulo().equals("Livro 2")), "Livro 2 deveria estar na lista.");
    }
    


}
