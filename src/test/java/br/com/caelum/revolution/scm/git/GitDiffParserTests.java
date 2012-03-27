package br.com.caelum.revolution.scm.git;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.revolution.domain.ModificationKind;
import br.com.caelum.revolution.scm.DiffData;


public class GitDiffParserTests {

	@Test
	public void shouldParseASimpleDiff() {
		String log = "diff --git a/FJ-ON-28/todo.txt b/FJ-ON-28/todo.txt\r\n"
				+ "index 6288664..007a49e 100644\r\n"
				+ "--- a/FJ-ON-28/todo.txt\r\n"
				+ "+++ b/FJ-ON-28/todo.txt\r\n"
				+ "@@ -1,11 +1,16 @@\r\n"
				+ " CAPITULO DI\r\n"
				+ " \r\n"
				+ " 1. usar algo ao inves de return String\r\n"
				+ "+   gui: vc edita? coloquei 2 videos lá, um colocando o Result e outro da correc\r\n"
				+ " 2. slide de 5 secs para mostrar dando new ProdutoDAO para receber ProdutoDAO (3\r\n"
				+ "+   gui: feito (injecao-dependencia.key)\r\n"
				+ " 3. slide de 5 secs de dependencia para interface ao inves de classe (3 trans, 1\r\n"
				+ "+   gui: feito (dao-interface)\r\n"
				+ " 4. slide de 5 secs que o VRaptor usa a anotacao @Component para conectar a inte\r\n"
				+ " 5. slidede 20 secs falando de requestscoped e produtodao a cada requisicao (2 a\r\n"
				+ "+   gui: feito (request-scoped)\r\n"
				+ " 6. slide de 10 secs explicando sessionscoped (2 a 3 slides)\r\n"
				+ "+   gui: feito (session-scoped)\r\n";

		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(1, diffs.size());
		assertEquals("FJ-ON-28/todo.txt", diffs.get(0).getName());
		assertEquals(
				"@@ -1,11 +1,16 @@\r\n"
						+ " CAPITULO DI\r\n"
						+ " \r\n"
						+ " 1. usar algo ao inves de return String\r\n"
						+ "+   gui: vc edita? coloquei 2 videos lá, um colocando o Result e outro da correc\r\n"
						+ " 2. slide de 5 secs para mostrar dando new ProdutoDAO para receber ProdutoDAO (3\r\n"
						+ "+   gui: feito (injecao-dependencia.key)\r\n"
						+ " 3. slide de 5 secs de dependencia para interface ao inves de classe (3 trans, 1\r\n"
						+ "+   gui: feito (dao-interface)\r\n"
						+ " 4. slide de 5 secs que o VRaptor usa a anotacao @Component para conectar a inte\r\n"
						+ " 5. slidede 20 secs falando de requestscoped e produtodao a cada requisicao (2 a\r\n"
						+ "+   gui: feito (request-scoped)\r\n"
						+ " 6. slide de 10 secs explicando sessionscoped (2 a 3 slides)\r\n"
						+ "+   gui: feito (session-scoped)\r\n", diffs.get(0)
						.getDiff());
	}

	@Test
	public void shouldParseManyDiffsInOne() {
		String log = "diff --git a/arquivo1 b/arquivo1\r\n" + "index xx\r\n"
				+ "--- a/FJ-ON-28/todo.txt\r\n" + "+++ b/FJ-ON-28/todo.txt\r\n"
				+ "bla bla\r\nble ble\r\n"
				+ "diff --git a/arquivo2 b/arquivo2\r\n" + "index xx\r\n"
				+ "--- a/FJ-ON-28/todo.txt\r\n" + "+++ b/FJ-ON-28/todo.txt\r\n"
				+ "bli bli\r\nblo blo\r\n";

		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(2, diffs.size());

		assertEquals("arquivo1", diffs.get(0).getName());
		assertEquals("arquivo2", diffs.get(1).getName());

		assertEquals("bla bla\r\nble ble\r\n", diffs.get(0).getDiff());
		assertEquals("bli bli\r\nblo blo\r\n", diffs.get(1).getDiff());
	}

	@Test
	public void shouldIgnoreModeInDiff() {
		String log = "diff --git a/arquivo1 b/arquivo1\r\n" + "mode bla\r\n"
				+ "index xx\r\n" + "--- a/FJ-ON-28/todo.txt\r\n"
				+ "+++ b/FJ-ON-28/todo.txt\r\n" + "bla bla\r\nble ble\r\n";

		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals("arquivo1", diffs.get(0).getName());
		assertEquals("bla bla\r\nble ble\r\n", diffs.get(0).getDiff());
		assertEquals(ModificationKind.DEFAULT, diffs.get(0).getModificationKind());
	}

	@Test
	public void shouldIdentifyDeletedFileMode() {
		String log = "diff --git a/arquivo1 b/arquivo1\r\n"
				+ "deleted file mode 2121\r\n" + "index xx\r\n"
				+ "--- a/FJ-ON-28/todo.txt\r\n" + "+++ b/FJ-ON-28/todo.txt\r\n"
				+ "bla bla\r\nble ble\r\n";

		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(ModificationKind.DELETED, diffs.get(0).getModificationKind());
	}

	@Test
	public void shouldIdentifyNewFileMode() {
		String log = "diff --git a/arquivo1 b/arquivo1\r\n"
				+ "new file mode 2121\r\n" + "index xx\r\n"
				+ "--- a/FJ-ON-28/todo.txt\r\n" + "+++ b/FJ-ON-28/todo.txt\r\n"
				+ "bla bla\r\nble ble\r\n";

		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(ModificationKind.NEW, diffs.get(0).getModificationKind());
	}

	@Test
	public void shouldTreatBinaryFiles() {
		String log = "diff --git a/common/sqlitejdbc-v056.jar b/common/sqlitejdbc-v056.jar\r\n"
				+ "new file mode 100644\r\n"
				+ "index 0000000..f95d90e\r\n"
				+ "Binary files /dev/null and b/common/sqlitejdbc-v056.jar differ\r\n";
		
		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(ModificationKind.NEW, diffs.get(0).getModificationKind());
		assertEquals("", diffs.get(0).getDiff());
	}
	
	@Test
	public void shouldIgnoreBlankDiffs() {
		List<DiffData> diffs = new GitDiffParser().parse("");
		
		assertEquals(0, diffs.size());
	}
	
	@Test
	public void shouldTreatNewFilesWithNoDiff() {
		String log = "diff --git a/src/main/webapp/WEB-INF/jsp/comercial/turma/atualiza.invalid.jsp "+
					"b/src/main/webapp/WEB-INF/jsp/comercial/turma/atuaa.invalid.jsp\r\n"+
					"new file mode 100644" +
					"index 0000000..e69de29";
		
		List<DiffData> diffs = new GitDiffParser().parse(log);

		assertEquals(ModificationKind.NEW, diffs.get(0).getModificationKind());
		assertEquals("", diffs.get(0).getDiff());

		
	}
}
