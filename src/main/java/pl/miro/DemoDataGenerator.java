package pl.miro;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import pl.miro.domain.Dokument;
import pl.miro.domain.JednostkaMiary;
import pl.miro.domain.Kontrahent;
import pl.miro.domain.PozycjaDokumentu;
import pl.miro.domain.StawkaVAT;
import pl.miro.domain.Typdokumentu;


public class DemoDataGenerator {

	

	public static void create() {

		EntityManager em = Persistence
				.createEntityManagerFactory("pl.miro")
				.createEntityManager();

		em.getTransaction().begin();
		
		StawkaVAT stawkaVAT = new StawkaVAT();
		stawkaVAT.setNazwa("23%");
		stawkaVAT.setPodstawowa(true);
		stawkaVAT.setWartosc(new BigDecimal(0.23d));
		em.persist(stawkaVAT);
		
		stawkaVAT = new StawkaVAT();
		stawkaVAT.setNazwa("7%");
		stawkaVAT.setPodstawowa(true);
		stawkaVAT.setWartosc(new BigDecimal(0.07d));
		em.persist(stawkaVAT);
		
		JednostkaMiary jednostkaMiary = new JednostkaMiary();
		jednostkaMiary.setNazwa("szt.");
		jednostkaMiary.setOpis("sztuka");
		em.persist(jednostkaMiary);
		
		jednostkaMiary = new JednostkaMiary();
		jednostkaMiary.setNazwa("kg");
		jednostkaMiary.setOpis("kilogram");
		em.persist(jednostkaMiary);
		
		Typdokumentu typDok = new Typdokumentu();
		typDok.setNazwa("Dokument Faktura VAT");
		typDok.setSkrot("Faktura VAT");
		em.persist(typDok);
		
		typDok = new Typdokumentu();
		typDok.setNazwa("Dokument Pro forma");
		typDok.setSkrot("Proforma");
		em.persist(typDok);
		
		Kontrahent kontrahent = new Kontrahent();
		kontrahent.setNazwa("Kontrahent SA");
		kontrahent.setMiejscowosc("USA");
		kontrahent.setTelefon("zastrzeżony");
		
		em.persist(kontrahent);
		
		em.getTransaction().commit();
		
		Dokument dokument;
		for (int i = 0; i < 10; i++) {
			
			em.getTransaction().begin();
			
			dokument = new Dokument();
			dokument.setTypDokumentu(typDok);
			dokument.setKontrahent(kontrahent);
			dokument.setNazwa("FV/"+ i +"/2012");
			dokument.setMiejcsceWystawienia("Pławniowice");
			em.persist(dokument);
			
			PozycjaDokumentu pozycjaDokumentu = new PozycjaDokumentu(1);
			pozycjaDokumentu.setDokument(dokument);
			pozycjaDokumentu.setLp(1);
			pozycjaDokumentu.setNazwa("Jabłka");
			pozycjaDokumentu.setCenaNetto(new BigDecimal(7.5d));
			pozycjaDokumentu.setIlosc(3.75);
			pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
			pozycjaDokumentu.setStawkaVAT(stawkaVAT);
			
			em.persist(pozycjaDokumentu);
			dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
			
			pozycjaDokumentu = new PozycjaDokumentu(2);
			pozycjaDokumentu.setDokument(dokument);
			pozycjaDokumentu.setLp(2);
			pozycjaDokumentu.setNazwa("Śliwki");
			pozycjaDokumentu.setCenaNetto(new BigDecimal(125d));
			pozycjaDokumentu.setIlosc(2.0);
			pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
			pozycjaDokumentu.setStawkaVAT(stawkaVAT);
			
			em.persist(pozycjaDokumentu);
			dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
			
			pozycjaDokumentu = new PozycjaDokumentu(3);
			pozycjaDokumentu.setDokument(dokument);
			pozycjaDokumentu.setLp(3);
			pozycjaDokumentu.setNazwa("Pomarańcze");
			pozycjaDokumentu.setCenaNetto(new BigDecimal(25d));
			pozycjaDokumentu.setIlosc(3.0);
			pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
			pozycjaDokumentu.setStawkaVAT(stawkaVAT);
			
			em.persist(pozycjaDokumentu);
			dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
			
			em.getTransaction().commit();
		}
		
		em.getTransaction().begin();
	    dokument = new Dokument();
		dokument.setTypDokumentu(typDok);
		dokument.setKontrahent(kontrahent);
		dokument.setNazwa("FV/01/2012");
		dokument.setMiejcsceWystawienia("Pławniowice");
		em.persist(dokument);
		
		PozycjaDokumentu pozycjaDokumentu = new PozycjaDokumentu(1);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(1);
		pozycjaDokumentu.setNazwa("Pomidor malina");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(7.5d));
		pozycjaDokumentu.setIlosc(3.75);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		pozycjaDokumentu = new PozycjaDokumentu(2);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(2);
		pozycjaDokumentu.setNazwa("Komu bije dzwon");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(125d));
		pozycjaDokumentu.setIlosc(2.0);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		pozycjaDokumentu = new PozycjaDokumentu(3);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(3);
		pozycjaDokumentu.setNazwa("Truskawki");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(25d));
		pozycjaDokumentu.setIlosc(3d);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		//=======================================
		dokument = new Dokument();
		dokument.setTypDokumentu(typDok);
		dokument.setKontrahent(kontrahent);
		dokument.setNazwa("FV/02/2012");
		dokument.setMiejcsceWystawienia("Gliwice");
		em.persist(dokument);
		
		pozycjaDokumentu = new PozycjaDokumentu(1);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(1);
		pozycjaDokumentu.setNazwa("Rower");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(8.5d));
		pozycjaDokumentu.setIlosc(1d);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		pozycjaDokumentu = new PozycjaDokumentu(2);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(2);
		pozycjaDokumentu.setNazwa("Kajak");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(15d));
		pozycjaDokumentu.setIlosc(2d);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		pozycjaDokumentu = new PozycjaDokumentu(3);
		pozycjaDokumentu.setDokument(dokument);
		pozycjaDokumentu.setLp(3);
		pozycjaDokumentu.setNazwa("Tramwaj");
		pozycjaDokumentu.setCenaNetto(new BigDecimal(25d));
		pozycjaDokumentu.setIlosc(3d);
		pozycjaDokumentu.setJednostkaMiary(jednostkaMiary);
		pozycjaDokumentu.setStawkaVAT(stawkaVAT);
		
		em.persist(pozycjaDokumentu);
		dokument.getPozycjaDokumentus().add(pozycjaDokumentu);
		
		em.getTransaction().commit();
	}

}
