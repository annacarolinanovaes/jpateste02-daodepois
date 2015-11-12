package dao.jpa;

import javax.persistence.EntityManager;

public class EM {
	//Objeto privado que instancia o EntityManager dentro da thread local
	private static ThreadLocal<EntityManager> localEm = new ThreadLocal<EntityManager>();

	//Ele verá se o EM está instanciado
	public static synchronized EntityManager getLocalEm(){
		EntityManager em = localEm.get();
		if(em == null){
			//Caso não tenha sido instanciado, ele instancia
			em = EMF.get().createEntityManager();
			localEm.set(em);
		}
		return em;
	}
	
	//Fecha o EntityManager que esteja instanciado na thread local
	public static void closeLocalEm(){
		EntityManager em = localEm.get();
		if(em != null){
			localEm.get().close();
			localEm.set(null);
		}
	}
}
