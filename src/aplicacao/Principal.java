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
				//Conex�oImpl
				System.out.println("Digite o c�digo do �lbum: ");
				cod = Integer.parseInt(sc.nextLine());
				albumDao = DaoFactory.criarAlbumDao(conn);
				alb = albumDao.buscar(cod);
				if(alb == null){
					System.out.println("�lbum inexistente!");
				}else{
					System.out.println("Dura��o do �lbum " + alb.getNome() + ": " + alb.duracao());
				}
				conn.fecharConexao();
				break;
				
			//2 - Listar todos os �lbuns com seus artistas	
			case 2:
				conn = DaoFactory.criarConexao();
				albumDao = DaoFactory.criarAlbumDao(conn);	
				List<Album> listAlbuns = albumDao.buscarTodos();//Lista de resultados
				
				System.out.println("Listagem de �lbuns: ");
				for(Album x : listAlbuns){
					System.out.println(x.getNome() + ", " + x.getAno() + ", " + x.getArtista().getNome());
				}
				conn.fecharConexao();
				
				break;
			
			//3 - Cadastrar um novo �lbum	
			case 3:
				conn = DaoFactory.criarConexao();
				artistaDao = DaoFactory.criarArtistaDao(conn);
				List<Artista> listArtistas = artistaDao.buscarTodos();
				System.out.println("Artistas existentes: ");
				for(Artista x : listArtistas){
					System.out.println(x);
				}
				System.out.println("Digite o c�digo do artista do novo �lbum a ser inserido: ");
				int codArtista = Integer.parseInt(sc.nextLine());
				art = artistaDao.buscar(codArtista);

				if(art == null){
					System.out.println("C�digo de artista inexistente!");			
				}
				else{
					System.out.println("\nDigite o nome do novo �lbum: ");
					String nome = sc.nextLine();
					System.out.println("Digite o ano do novo �lbum: ");
					int ano = Integer.parseInt(sc.nextLine());
					alb = new Album(null, nome, ano, art);//Instancia��o do novo �lbum
					conn.iniciarTransacao();//Inicia a transa��o
					albumDao = DaoFactory.criarAlbumDao(conn);
					try{
						albumDao.inserirAtualizar(alb);//Comando do JPA para inser��o					
						conn.commit();//Confirmar
						System.out.println("O �lbum foi inserido. C�digo: " + alb.getCodAlbum());
					}
					catch(Exception e){//Se der algum problema
						if(conn.transacaoAtiva()){
							conn.rollback();
						}
						System.out.println("Falha ao inserir �lbum!");
					}
				}
				conn.fecharConexao();
				break;
			
			//Sair do programa
			case 4:
				System.out.println("Fim do programa!");
				break;
			
			default:
				System.out.println("Op��o inv�lida!");
			}
		} while(op != 4);
		

		sc.close();
	}

}
