package com.udesc.controller;

import java.util.List;
import java.util.Scanner;

import com.udesc.model.Livro;
import com.udesc.service.BibliotecaService;

public class BibliotecaController {

    public static void main(String[] args) {
        
        BibliotecaService bibliotecaService = new BibliotecaService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSistema de Gerenciamento de Biblioteca - LibraSys");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Remover Livro");
            System.out.println("3. Buscar Livro");
            System.out.println("4. Listar Todos os Livros");
            System.out.println("5. Ver Detalhes de um Livro");
            System.out.println("6. Contar Total de Livros");
            System.out.println("7. Listar Livros por Ano");
            System.out.println("8. Atualizar Ano de Publicação de um Livro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine();

            try {
                int opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1:
                        System.out.print("Título do Livro: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Autor do Livro: ");
                        String autor = scanner.nextLine();
                        System.out.print("Ano de Publicação: ");
                        int ano = scanner.nextInt();
                        scanner.nextLine(); 
                        
                        bibliotecaService.adicionarLivro(titulo, autor, ano);
                        System.out.println("Livro adicionado com sucesso!");
                        break;

                    case 2:
                        System.out.print("ID do Livro a ser removido: ");
                        int idRemover = scanner.nextInt();
                        scanner.nextLine();
                        if (bibliotecaService.removerLivro(idRemover)) {
                            System.out.println("Livro removido com sucesso!");
                        } else {
                            System.out.println("Livro não encontrado!");
                        }
                        break;

                    case 3:
                        System.out.print("Digite o título ou autor para buscar: ");
                        String query = scanner.nextLine();
                        List<Livro> resultados = bibliotecaService.buscarLivros(query);
                        if (resultados.isEmpty()) {
                            System.out.println("Nenhum livro encontrado.");
                        } else {
                            resultados.forEach(System.out::println);
                        }
                        break;

                    case 4:
                        List<Livro> livros = bibliotecaService.listarLivros();
                        if (livros.isEmpty()) {
                            System.out.println("Nenhum livro cadastrado.");
                        } else {
                            livros.forEach(System.out::println);
                        }
                        break;

                    case 5:
                        System.out.print("Digite o ID do livro: ");
                        int idDetalhes = scanner.nextInt();
                        scanner.nextLine(); 
                        bibliotecaService.verDetalhesDoLivro(idDetalhes).ifPresentOrElse(
                            tituloLivro -> System.out.println("Título do livro: " + tituloLivro),
                            () -> System.out.println("Livro não encontrado.")
                        );
                        break;

                    case 6:
                        System.out.println("Total de livros na biblioteca: " + bibliotecaService.contarLivros());
                        break;

                    case 7:
                        System.out.print("Ano de Publicação: ");
                        int anoBusca = scanner.nextInt();
                        scanner.nextLine();
                        List<Livro> livrosAno = bibliotecaService.listarLivrosPorAno(anoBusca);
                        if (livrosAno.isEmpty()) {
                            System.out.println("Nenhum livro encontrado para o ano " + anoBusca);
                        } else {
                            livrosAno.forEach(System.out::println);
                        }
                        break;

                    case 8:
                        System.out.print("ID do Livro a ser atualizado: ");
                        int idAtualizar = scanner.nextInt();
                        System.out.print("Novo Ano de Publicação: ");
                        int novoAno = scanner.nextInt();
                        scanner.nextLine();
                        if (bibliotecaService.atualizarAnoPublicacao(idAtualizar, novoAno)) {
                            System.out.println("Ano de publicação atualizado com sucesso!");
                        } else {
                            System.out.println("Livro não encontrado.");
                        }
                        break;

                    case 0:
                        System.out.println("Saindo do sistema. Até logo!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número para escolher uma opção.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
