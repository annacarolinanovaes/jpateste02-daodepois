package aplicacao;

import java.util.List;
import java.util.Scanner;

import dao.AlbumDao;
import dao.ArtistaDao;
import dao.Conexao;
import dao.DaoFactory;
//import dao.MusicaDao;
import dominio.Album;
import dominio.Artista;

public class Principal {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Conexao conn = null;
		int op, cod;
		Album alb = null;
		Artista art = null;
		AlbumDao albumDao = null;
		ArtistaDao artistaDao = null;
		//MusicaDao musicaDao = null;
		
		do{
			op = Tela.menu(sc);
			
			switch(op){
			//
			case 1:
				conn = DaoFactory.criarConexao();
				//ConexãoImpl
				System.out.println("Digite o código do álbum: ");
				cod = Integer.parseInt(sc.nextLine());
				albumDao = DaoFactory.criarAlbumDao(conn);
				alb = albumDao.buscar(cod);
				if(alb == null){
					System.out.println("Álbum inexistente!");
				}else{
					System.out.println("Duração do álbum " + alb.getNome() + ": " + alb.duracao());
				}
				conn.fecharConexao();
				break;
				
			//2 - Listar todos os álbuns com seus artistas	
			case 2:
				conn = DaoFactory.criarConexao();
				albumDao = DaoFactory.criarAlbumDao(conn);	
				List<Album> listAlbuns = albumDao.buscarTodos();//Lista de resultados
				
				System.out.println("Listagem de álbuns: ");
				for(Album x : listAlbuns){
					System.out.println(x.getNome() + ", " + x.getAno() + ", " + x.getArtista().getNome());
				}
				conn.fecharConexao();
				
				break;
			
			//3 - Cadastrar um novo álbum	
			case 3:
				conn = DaoFactory.criarConexao();
				artistaDao = DaoFactory.criarArtistaDao(conn);
				List<Artista> listArtistas = artistaDao.buscarTodos();
				System.out.println("Artistas existentes: ");
				for(Artista x : listArtistas){
					System.out.println(x);
				}
				System.out.println("Digite o código do artista do novo álbum a ser inserido: ");
				int codArtista = Integer.parseInt(sc.nextLine());
				art = artistaDao.buscar(codArtista);

				if(art == null){
					System.out.println("Código de artista inexistente!");			
				}
				else{
					System.out.println("\nDigite o nome do novo álbum: ");
					String nome = sc.nextLine();
					System.out.println("Digite o ano do novo álbum: ");
					int ano = Integer.parseInt(sc.nextLine());
					alb = new Album(null, nome, ano, art);//Instanciação do novo álbum
					conn.iniciarTransacao();//Inicia a transação
					albumDao = DaoFactory.criarAlbumDao(conn);
					try{
						albumDao.inserirAtualizar(alb);//Comando do JPA para inserção					
						conn.commit();//Confirmar
						System.out.println("O álbum foi inserido. Código: " + alb.getCodAlbum());
					}
					catch(Exception e){//Se der algum problema
						if(conn.transacaoAtiva()){
							conn.rollback();
						}
						System.out.println("Falha ao inserir álbum!");
					}
				}
				conn.fecharConexao();
				break;
			
			//Sair do programa
			case 4:
				System.out.println("Fim do programa!");
				break;
			
			default:
				System.out.println("Opção inválida!");
			}
		} while(op != 4);
		

		sc.close();
	}

}
