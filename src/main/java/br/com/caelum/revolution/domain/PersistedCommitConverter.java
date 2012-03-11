package br.com.caelum.revolution.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.DiffData;

public class PersistedCommitConverter {

	public Commit toDomain(CommitData data, Session session) throws ParseException {
		
		Author author = searchForPreviouslySavedAuthor(data.getAuthor(), session);
		if(author == null) {
			author = new Author(data.getAuthor(), data.getEmail());
			session.save(author);
		}
		
		Commit commit = new Commit(data.getCommitId(), author, convertDate(data), data.getMessage(), data.getDiff(), data.getPriorCommit());
		session.save(commit);
		
		for (DiffData diff : data.getDiffs()) {
			Artifact artifact = searchForPreviouslySavedArtifact(diff.getName(), session);

			if (artifact == null) {
				artifact = new Artifact(diff.getName(), diff.getArtifactKind());
				session.save(artifact);
			}
			
			Modification modification = new Modification(diff.getDiff(), commit, artifact, diff.getModificationKind());
			artifact.addModification(modification);
			commit.addModification(modification);
			commit.addArtifact(artifact);
			session.save(modification);
		}

		return commit;
	}

	private Author searchForPreviouslySavedAuthor(String name, Session session) {
		Author author = (Author)session.createCriteria(Author.class).add(Restrictions.eq("name", name)).uniqueResult();
		return author;
	}

	private Artifact searchForPreviouslySavedArtifact(String name, Session session) {
		Artifact artifact = (Artifact) session
				.createCriteria(Artifact.class)
				.add(Restrictions.eq("name", name))
				.uniqueResult();
		return artifact;
	}

	private Calendar convertDate(CommitData data) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(data
				.getDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
}
