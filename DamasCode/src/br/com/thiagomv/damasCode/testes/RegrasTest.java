package br.com.thiagomv.damasCode.testes;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.controle.Regras;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.MovimentoSimples;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;

/**
 * Testa as regras do jogo. Algumas regras n�o s�o poss�eis de serem testadas
 * aqui e poder�o ser testadas em outras classes.
 * 
 * Regras do jogo:
 * 
 * 1 - O jogo de damas � praticado em um tabuleiro de 64 casas, claras e
 * escuras. A grande diagonal (escura), deve ficar sempre � esquerda de cada
 * jogador. O objetivo do jogo � imobilizar ou capturar todas as pe�as do
 * advers�rio.
 * 
 * 2 - O jogo de damas � praticado entre dois parceiros, com 12 pedras brancas
 * de um lado e com 12 pedras pretas de outro lado. O lance inicial cabe sempre
 * a quem estiver com as pe�as brancas. Tamb�m joga-se damas em um tabuleiro de
 * 100 casas, com 20 pedras para cada lado - Damas Internacional.
 * 
 * 3 - A pedra anda s� para frente, uma casa de cada vez. Quando a pedra atinge
 * a oitava linha do tabuleiro ela � promovida � dama.
 * 
 * 4 - A dama � uma pe�a de movimentos mais amplos. Ela anda para frente e para
 * tr�s, quantas casas quiser. A dama n�o pode saltar uma pe�a da mesma cor.
 * 
 * 5 - A captura � obrigat�ria. N�o existe sopro. Duas ou mais pe�as juntas, na
 * mesma diagonal, n�o podem ser capturadas.
 * 
 * 6 - A pedra captura a dama e a dama captura a pedra. Pedra e dama t�m o mesmo
 * valor para capturarem ou serem capturadas.
 * 
 * 7 - A pedra e a dama podem capturar tanto para frente como para tr�s, uma ou
 * mais pe�as.
 * 
 * 8 - Se no mesmo lance se apresentar mais de um modo de capturar, �
 * obrigat�rio executar o lance que capture o maior n�mero de pe�as (Lei da
 * Maioria).
 * 
 * 9 - A pedra que durante o lance de captura de v�rias pe�as, apenas passe por
 * qualquer casa de coroa��o, sem a� parar, n�o ser� promovida � dama.
 * 
 * 10 - Na execu��o do lance do lance de captura, � permitido passar mais de uma
 * vez pela mesma casa vazia, n�o � permitido capturar duas vezes a mesma pe�a.
 * 
 * 11 - Na execu��o do lance de captura, n�o � permitido capturar a mesma pe�a
 * mais de uma vez e as pe�as capturadas n�o podem ser retiradas do tabuleiro
 * antes de completar o lance de captura.
 * 
 * Empate: Ap�s 20 lances sucessivos de damas, sem captura ou deslocamento de
 * pedra, a partida � declarada empatada.
 * 
 * Finais de: 2 damas contra 2 damas; 2 damas contra uma; 2 damas contra uma
 * dama e uma pedra; uma dama contra uma dama e uma dama contra uma dama e uma
 * pedra, s�o declarados empatados ap�s 5 lances.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         21/09/2014
 */
public class RegrasTest {
	private Tabuleiro tabuleiro = null;
	private EstadoJogo estadoJogo = null;
	private Regras regras = null;
	private List<Jogada> jogadasPermitidas = null;

	/**
	 * Testa as configura��es de novo jogo e as jogadas permitidas para o
	 * primeiro lance do jogo.
	 * 
	 * Testa as regras 1 e 2.
	 */
	@Test
	public void novoJogo() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		tabuleiro.configurarParaNovoJogo();
		verificarPosicoesIniciais();

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();
		verificarJogadasPermitidasIniciais();
	}

	/**
	 * Verifica as posi��es iniciais de cada local do tabuleiro.
	 */
	private void verificarPosicoesIniciais() {
		IndicadorPedraJogador branca = IndicadorPedraJogador.NORMAL_J1;
		IndicadorPedraJogador preta = IndicadorPedraJogador.NORMAL_J2;

		assertTabuleiro(0, 0, branca);
		assertTabuleiro(0, 1, null);
		assertTabuleiro(0, 2, branca);
		assertTabuleiro(0, 3, null);
		assertTabuleiro(0, 4, branca);
		assertTabuleiro(0, 5, null);
		assertTabuleiro(0, 6, branca);
		assertTabuleiro(0, 7, null);

		assertTabuleiro(1, 0, null);
		assertTabuleiro(1, 1, branca);
		assertTabuleiro(1, 2, null);
		assertTabuleiro(1, 3, branca);
		assertTabuleiro(1, 4, null);
		assertTabuleiro(1, 5, branca);
		assertTabuleiro(1, 6, null);
		assertTabuleiro(1, 7, branca);

		assertTabuleiro(2, 0, branca);
		assertTabuleiro(2, 1, null);
		assertTabuleiro(2, 2, branca);
		assertTabuleiro(2, 3, null);
		assertTabuleiro(2, 4, branca);
		assertTabuleiro(2, 5, null);
		assertTabuleiro(2, 6, branca);
		assertTabuleiro(2, 7, null);

		assertTabuleiro(3, 0, null);
		assertTabuleiro(3, 1, null);
		assertTabuleiro(3, 2, null);
		assertTabuleiro(3, 3, null);
		assertTabuleiro(3, 4, null);
		assertTabuleiro(3, 5, null);
		assertTabuleiro(3, 6, null);
		assertTabuleiro(3, 7, null);

		assertTabuleiro(4, 0, null);
		assertTabuleiro(4, 1, null);
		assertTabuleiro(4, 2, null);
		assertTabuleiro(4, 3, null);
		assertTabuleiro(4, 4, null);
		assertTabuleiro(4, 5, null);
		assertTabuleiro(4, 6, null);
		assertTabuleiro(4, 7, null);

		assertTabuleiro(5, 0, null);
		assertTabuleiro(5, 1, preta);
		assertTabuleiro(5, 2, null);
		assertTabuleiro(5, 3, preta);
		assertTabuleiro(5, 4, null);
		assertTabuleiro(5, 5, preta);
		assertTabuleiro(5, 6, null);
		assertTabuleiro(5, 7, preta);

		assertTabuleiro(6, 0, preta);
		assertTabuleiro(6, 1, null);
		assertTabuleiro(6, 2, preta);
		assertTabuleiro(6, 3, null);
		assertTabuleiro(6, 4, preta);
		assertTabuleiro(6, 5, null);
		assertTabuleiro(6, 6, preta);
		assertTabuleiro(6, 7, null);

		assertTabuleiro(7, 0, null);
		assertTabuleiro(7, 1, preta);
		assertTabuleiro(7, 2, null);
		assertTabuleiro(7, 3, preta);
		assertTabuleiro(7, 4, null);
		assertTabuleiro(7, 5, preta);
		assertTabuleiro(7, 6, null);
		assertTabuleiro(7, 7, preta);
	}

	/**
	 * Verifica se determinada posi��o do tabuleiro est� com o valor esperado.
	 * 
	 * @param linha
	 *            Coordenada da linha da posi��o.
	 * @param coluna
	 *            Coordenada da coluna da posi��o.
	 * @param valorEsperado
	 *            Valor esperado para a posi��o definida.
	 */
	private void assertTabuleiro(int linha, int coluna,
			IndicadorPedraJogador valorEsperado) {
		Assert.assertThat(tabuleiro.getPedraJogador(linha, coluna),
				CoreMatchers.is(valorEsperado));
	}

	/**
	 * Verifica a lista de jogadas permitidas ao iniciar um novo jogo.
	 */
	private void verificarJogadasPermitidasIniciais() {
		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(7));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 0), new PosicaoTabuleiro(3, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 2), new PosicaoTabuleiro(3, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 2), new PosicaoTabuleiro(3, 3)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 4), new PosicaoTabuleiro(3, 3)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 4), new PosicaoTabuleiro(3, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 6), new PosicaoTabuleiro(3, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 6), new PosicaoTabuleiro(3, 7)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Simula a promo��o de uma pedra Normal a Dama.
	 * 
	 * Testa a regra 3.
	 */
	@Test
	public void promoverDama() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 4),
				IndicadorPedraJogador.NORMAL_J1);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(2));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(6, 4), new PosicaoTabuleiro(7, 3)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(6, 4), new PosicaoTabuleiro(7, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 4), null);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(7, 3),
				IndicadorPedraJogador.DAMA_J1);

		regras.atualizarRegras(estadoJogo);
		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(7));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(6, 2)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(5, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(4, 0)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(6, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(5, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(4, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(3, 7)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 4.
	 */
	@Test
	public void movimentoDama() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 3),
				IndicadorPedraJogador.DAMA_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(5, 5),
				IndicadorPedraJogador.NORMAL_J1);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(12));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(4, 2)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(5, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(6, 0)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(4, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(2, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(1, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(0, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(2, 2)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(1, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(0, 0)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 5.
	 */
	@Test
	public void capturarPedra() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 0),
				IndicadorPedraJogador.NORMAL_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(5, 1),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 2),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 2),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 6),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 7),
				IndicadorPedraJogador.NORMAL_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(1, 5),
				IndicadorPedraJogador.DAMA_J1);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);
		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(1));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 7), new PosicaoTabuleiro(4, 6),
				new PosicaoTabuleiro(5, 5)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 7), null);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 6), null);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(5, 5),
				IndicadorPedraJogador.NORMAL_J1);

		regras.atualizarRegras(estadoJogo);
		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(8));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(2, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(3, 3)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(2, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(3, 7)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(0, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(1, 5), new PosicaoTabuleiro(0, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 6.
	 */
	@Test
	public void pedraCapturaDama() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR2);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(2, 4),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(1, 5),
				IndicadorPedraJogador.DAMA_J1);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(1));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 4), new PosicaoTabuleiro(1, 5),
				new PosicaoTabuleiro(0, 6)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 7.
	 */
	@Test
	public void pedraCapturaPedraParaTras() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(2, 0),
				IndicadorPedraJogador.NORMAL_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(1, 1),
				IndicadorPedraJogador.NORMAL_J2);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(1));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 0), new PosicaoTabuleiro(1, 1),
				new PosicaoTabuleiro(0, 2)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 8.
	 */
	@Test
	public void leiDaMaioria() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 7),
				IndicadorPedraJogador.NORMAL_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 6),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 4),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 4),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(2, 2),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(2, 6),
				IndicadorPedraJogador.DAMA_J2);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(1));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 7), new PosicaoTabuleiro(4, 6),
				new PosicaoTabuleiro(5, 5)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(4, 4),
				new PosicaoTabuleiro(3, 3)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(2, 2),
				new PosicaoTabuleiro(1, 1)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 10.
	 */
	@Test
	public void capturaComCaminhoCruzado() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 7),
				IndicadorPedraJogador.NORMAL_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 6),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 4),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(4, 2),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 6),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 4),
				IndicadorPedraJogador.DAMA_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(6, 2),
				IndicadorPedraJogador.DAMA_J2);

		regras = new Regras();
		regras.atualizarRegras(estadoJogo);

		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(2));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 7), new PosicaoTabuleiro(4, 6),
				new PosicaoTabuleiro(5, 5)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(4, 4),
				new PosicaoTabuleiro(3, 3)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(4, 2),
				new PosicaoTabuleiro(5, 1)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 1), new PosicaoTabuleiro(6, 2),
				new PosicaoTabuleiro(7, 3)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(6, 4),
				new PosicaoTabuleiro(5, 5)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 6),
				new PosicaoTabuleiro(7, 7)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 7), new PosicaoTabuleiro(4, 6),
				new PosicaoTabuleiro(5, 5)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 4),
				new PosicaoTabuleiro(7, 3)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(6, 2),
				new PosicaoTabuleiro(5, 1)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 1), new PosicaoTabuleiro(4, 2),
				new PosicaoTabuleiro(3, 3)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(4, 4),
				new PosicaoTabuleiro(5, 5)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(6, 6),
				new PosicaoTabuleiro(7, 7)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Testa a regra 11.
	 */
	@Test
	public void capturaBloqueada() {
		tabuleiro = new Tabuleiro(8, 8);
		estadoJogo = new EstadoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		limparTabuleiro();
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(7, 5),
				IndicadorPedraJogador.DAMA_J1);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(5, 3),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(2, 2),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 3),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(3, 5),
				IndicadorPedraJogador.NORMAL_J2);
		tabuleiro.setPedraJogador(new PosicaoTabuleiro(1, 5),
				IndicadorPedraJogador.DAMA_J2);

		regras = new Regras();

		regras.atualizarRegras(estadoJogo);
		jogadasPermitidas = regras.getJogadasPermitidas();

		Assert.assertThat(jogadasPermitidas.size(), CoreMatchers.is(1));

		Jogada jogada;

		jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(7, 5), new PosicaoTabuleiro(5, 3),
				new PosicaoTabuleiro(3, 1)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(3, 1), new PosicaoTabuleiro(2, 2),
				new PosicaoTabuleiro(0, 4)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(0, 4), new PosicaoTabuleiro(1, 5),
				new PosicaoTabuleiro(2, 6)));
		jogada.adicionarMovimentoSimples(new MovimentoSimples(
				new PosicaoTabuleiro(2, 6), new PosicaoTabuleiro(3, 5),
				new PosicaoTabuleiro(4, 4)));
		Assert.assertTrue(jogadasPermitidas.contains(jogada));
	}

	/**
	 * Limpa o tabuleiro, estabelecendo valor "null" para todas as posi��es.
	 */
	private void limparTabuleiro() {
		for (int l = 0; l < 8; l++) {
			for (int c = 0; c < 8; c++) {
				tabuleiro.setPedraJogador(new PosicaoTabuleiro(l, c), null);
			}
		}
	}
}
