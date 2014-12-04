package br.com.thiagomv.damasCode.controle;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

public class MemoriaController {
	private static enum MemoriaInfo {
		CRIACAO_JOGADOR, JOGADA_JOGADOR;
	}

	private static class EstatisticaMemoria {
		long maxMem;
		long acumulateTime;
		double acumulateMem;
		double mediaMem;

		public EstatisticaMemoria() {
			clear();
		}

		public void clear() {
			maxMem = 0;
			acumulateMem = 0;
			acumulateTime = 0;
			mediaMem = 0;
		}

		public void calcularMedia() {
			mediaMem = acumulateMem / (double) (acumulateTime);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\nMaxmem: ").append(maxMem / 1024.0).append("\n");
			sb.append("MediaMem: ").append(mediaMem / 1024.0).append("\n");
			return sb.toString();
		}
	}

	private static final Runtime s_runtime = Runtime.getRuntime();

	private static long initMem;
	private static long lastTime;
	private static long lastMem;
	private static long acumulateTime;
	private static double acumulateMem;
	private static long maxMem;

	private static IndicadorJogador currentJogador;
	private static MemoriaInfo currentInfo;
	private static EstatisticaMemoria[][] state;

	private static boolean memoryAnalysisEnabled = false;

	public static void setMemoryAnalysisEnabled(boolean enabled) {
		MemoriaController.memoryAnalysisEnabled = enabled;
	}

	public static boolean isMemoryAnalysisEnabled() {
		return MemoriaController.memoryAnalysisEnabled;
	}

	static {
		state = new EstatisticaMemoria[IndicadorJogador.values().length][MemoriaInfo
				.values().length];
		for (IndicadorJogador j : IndicadorJogador.values()) {
			for (MemoriaInfo m : MemoriaInfo.values()) {
				state[j.ordinal()][m.ordinal()] = new EstatisticaMemoria();
			}
		}
	}

	protected static long antesCriarJogador1() {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men antes de criar " + IndicadorJogador.JOGADOR1.toString() + ": "
				+ String.valueOf(mem));
		setCurrentInfo(IndicadorJogador.JOGADOR1, MemoriaInfo.CRIACAO_JOGADOR);
		iniciarCalculoMem(mem);
		return mem;
	}

	protected static long aposCriarJogador1() {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men após de criar " + IndicadorJogador.JOGADOR1.toString() + ": "
				+ String.valueOf(mem));
		finalizarCalculoMem(mem);
		return mem;
	}

	protected static long antesCriarJogador2() {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men antes de criar " + IndicadorJogador.JOGADOR2.toString() + ": "
				+ String.valueOf(mem));
		setCurrentInfo(IndicadorJogador.JOGADOR2, MemoriaInfo.CRIACAO_JOGADOR);
		iniciarCalculoMem(mem);
		return mem;
	}

	protected static long aposCriarJogador2() {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men após de criar " + IndicadorJogador.JOGADOR2.toString() + ": "
				+ String.valueOf(mem));
		finalizarCalculoMem(mem);
		return mem;
	}

	protected static long antesJogar(IndicadorJogador jogadorAtual) {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men antes da jogada de " + jogadorAtual.toString() + ": "
				+ String.valueOf(mem));
		setCurrentInfo(jogadorAtual, MemoriaInfo.JOGADA_JOGADOR);
		iniciarCalculoMem(mem);
		return mem;
	}

	protected static long aposJogar(IndicadorJogador jogadorAtual) {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		long mem = usedMemoryByte();
		log("Men após a jogada de " + jogadorAtual.toString() + ": "
				+ String.valueOf(mem));
		finalizarCalculoMem(mem);
		return mem;
	}

	public static void contabilizarMemoria() {
		if (!memoryAnalysisEnabled) {
			return;
		}

		long mem = usedMemory();
		acumularMem(mem);
	}

	protected static long usedMemoryByte() {
		if (!memoryAnalysisEnabled) {
			return 0;
		}

		try {
			runGC();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		}
		return usedMemory();
	}

	protected static long usedMemoryKByte() {
		return usedMemoryByte() / 1024L;
	}

	protected static long usedMemoryMByte() {
		return usedMemoryByte() / 1048576L;
	}

	protected static long usedMemoryGByte() {
		return usedMemoryByte() / 1073741824L;
	}

	private static void runGC() throws Exception {
		/* It helps to call Runtime.gc() using several method calls: */
		for (int r = 0; r < 4; ++r)
			_runGC();
	}

	private static void _runGC() throws Exception {
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 10000); ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();

			Thread.yield();

			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

	private static long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static void setCurrentInfo(IndicadorJogador jogador,
			MemoriaInfo info) {
		currentJogador = jogador;
		currentInfo = info;
	}

	private static void iniciarCalculoMem(long mem) {
		initMem = mem;
		lastTime = System.nanoTime();
		lastMem = mem;

		EstatisticaMemoria em = state[currentJogador.ordinal()][currentInfo
				.ordinal()];
		acumulateTime = em.acumulateTime;
		acumulateMem = em.acumulateMem;
		maxMem = em.maxMem;
	}

	private static void acumularMem(long mem) {
		long nowTime = System.nanoTime();
		double difTime = nowTime - lastTime;
		double difMem = mem - lastMem;
		double acum = (lastMem - initMem) * difTime
				+ ((difMem * difTime) / 2.0);

		lastTime = nowTime;
		lastMem = mem;
		acumulateTime += difTime;
		acumulateMem += acum;
		maxMem = Math.max(maxMem, mem - initMem);
	}

	private static void finalizarCalculoMem(long mem) {
		acumularMem(mem);

		EstatisticaMemoria em = state[currentJogador.ordinal()][currentInfo
				.ordinal()];
		em.maxMem = maxMem;
		em.acumulateTime = acumulateTime;
		em.acumulateMem = acumulateMem;
		em.calcularMedia();
	}

	protected static void print() {
		for (IndicadorJogador j : IndicadorJogador.values()) {
			for (MemoriaInfo m : MemoriaInfo.values()) {
				ArchitectureSettings.logLine(
						j.toString() + " - " + m.toString(),
						state[j.ordinal()][m.ordinal()].toString());
			}
		}
	}

	protected static void clear() {
		for (IndicadorJogador j : IndicadorJogador.values()) {
			for (MemoriaInfo m : MemoriaInfo.values()) {
				state[j.ordinal()][m.ordinal()].clear();
			}
		}
	}

	private static void log(String msg) {
		if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
			ArchitectureSettings.logLine(msg);
		}
	}

	public static double getMediaUso(IndicadorJogador jogador) {
		return state[jogador.ordinal()][MemoriaInfo.JOGADA_JOGADOR.ordinal()].mediaMem;
	}

	public static double getMaiorUso(IndicadorJogador jogador) {
		return state[jogador.ordinal()][MemoriaInfo.JOGADA_JOGADOR.ordinal()].maxMem;
	}
}