package teste_tgid_DevJava;

import java.util.HashMap;
import java.util.Map;

// Interface para os callbacks
interface TransactionCallback {
	void notifyTransaction(Transaction transaction);
}

// Classe base para usuários
abstract class User {
	String name;
	String cpfCnpj;

	public User(String name, String cpfCnpj) {
		this.name = name;
		this.cpfCnpj = cpfCnpj;
	}

	// Métodos getters e setters
}

// Classe Cliente
class Cliente extends User {
	private EmailSender emailSender;

	public Cliente(String name, String cpf, EmailSender emailSender) {
		super(name, cpf);
		this.emailSender = emailSender;
	}

	// Lógica para notificar o cliente por e-mail
	public void notifyClient(Transaction transaction) {
		String assunto = "Notificação de Transação";
		String corpo = "Olá " + name + ",\n\nVocê realizou uma transação:\n" + transaction.toString();
		emailSender.sendEmail(cpfCnpj, assunto, corpo);
	}
}

// Classe Empresa
class Empresa extends User implements TransactionCallback {
	private double saldo;
	private Map<String, Double> taxas;

	public Empresa(String name, String cnpj) {
		super(name, cnpj);
		this.taxas = new HashMap<>();
	}

	public void addTaxa(String tipoTaxa, double valor) {
		taxas.put(tipoTaxa, valor);
	}

	public void notifyTransaction(Transaction transaction) {
		// Implemente a lógica para notificar a Empresa sobre a transação
		System.out.println("Empresa notificada sobre a transação: " + transaction.toString());
	}

	public double getSaldo() {
		return saldo;
	}

	public void processarTransacao(double valor, String tipo, Cliente cliente) {
		if (tipo.equals("Depósito")) {
			saldo += valor;
			cliente.notifyClient(new Transaction(this, valor, tipo));
		} else if (tipo.equals("Saque")) {
			double taxa = taxas.getOrDefault("Taxa de Saque", 0.0);
			double valorComTaxa = valor + taxa;
			if (saldo >= valorComTaxa) {
				saldo -= valorComTaxa;
				cliente.notifyClient(new Transaction(this, valor, tipo));
			} else {
				System.out.println("Saldo insuficiente para saque.");
			}
		}
	}
}

// Classe para enviar e-mails
class EmailSender {
	public void sendEmail(String destinatario, String assunto, String corpo) {
		// Simulação de envio de e-mail
		System.out.println("Enviando e-mail para: " + destinatario);
		System.out.println("Assunto: " + assunto);
		System.out.println("Corpo:\n" + corpo);
		System.out.println("E-mail enviado com sucesso!");
	}
}

// Classe para representar uma transação
class Transaction {
	private User usuario;
	double valor;
	String tipo; // Depósito ou Saque

	public Transaction(User usuario, double valor, String tipo) {
		this.usuario = usuario;
		this.valor = valor;
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Transação{" +
				"usuário=" + usuario.name +
				", valor=" + valor +
				", tipo='" + tipo + '\'' +
				'}';
	}
}

// Classe principal para teste
public class Teste_Java_Developer {
	public static void main(String[] args) {
		EmailSender emailSender = new EmailSender();

		Cliente cliente1 = new Cliente("Cliente 1", "cliente1@example.com", emailSender);
		Empresa empresa1 = new Empresa("Empresa 1", "00.000.000/0001-00");
		empresa1.addTaxa("Taxa de Saque", 0.5); // Exemplo de taxa

		double valorDeposito = 100.0;
		Transaction deposito = new Transaction(cliente1, valorDeposito, "Depósito");
		empresa1.processarTransacao(deposito.valor, deposito.tipo, cliente1);
		cliente1.notifyClient(deposito); // Notifica o cliente por e-mail

		double valorSaque = 50.0;
		Transaction saque = new Transaction(cliente1, valorSaque, "Saque");
		empresa1.processarTransacao(saque.valor, saque.tipo, cliente1);
		cliente1.notifyClient(saque); // Notifica o cliente por e-mail

		System.out.println("Saldo atual da empresa: " + empresa1.getSaldo());
	}
}
