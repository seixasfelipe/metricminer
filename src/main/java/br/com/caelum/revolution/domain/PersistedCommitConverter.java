package br.com.caelum.revolution.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import model.BlamedLine;
import model.Project;
import model.SourceCode;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.DiffData;

public class PersistedCommitConverter {

    public PersistedCommitConverter() {
    }

    public Commit toDomain(CommitData data, Session session, Project project) throws ParseException {

        Author author = convertAuthor(data, session);
        Commit commit = convertCommit(data, session, author);

        for (DiffData diff : data.getDiffs()) {
            Artifact artifact = convertArtifact(session, project, diff);
            createModification(session, commit, diff, artifact);

            if (artifact.isSourceCode()) {
                SourceCode sourceCode = new SourceCode(artifact, commit, diff.getFullSourceCode());
                session.save(sourceCode);
                //convertBlameInformation(session, diff, sourceCode);
                
                artifact.addSource(sourceCode);
                commit.addSource(sourceCode);
                session.save(sourceCode);
            }

        }

        return commit;
    }

	private void convertBlameInformation(Session session, DiffData diff, SourceCode sourceCode) {
		for(Map.Entry<Integer, String> entry :  diff.getBlameLines().entrySet()) {
			Author blamedAuthor = searchForPreviouslySavedAuthor(entry.getValue(), session);
			System.out.println(entry.getValue());
			BlamedLine blamedLine = sourceCode.blame(entry.getKey(), blamedAuthor);
			
			session.save(blamedLine);
		}
	}

	private void createModification(Session session, Commit commit, DiffData diff,
			Artifact artifact) {
		Modification modification = new Modification(diff.getDiff(), commit, artifact, diff
		        .getModificationKind());
		artifact.addModification(modification);
		commit.addModification(modification);
		commit.addArtifact(artifact);
		session.save(modification);
	}

	private Artifact convertArtifact(Session session, Project project, DiffData diff) {
		Artifact artifact = searchForPreviouslySavedArtifact(diff.getName(), project, session);

		if (artifact == null) {
		    artifact = new Artifact(diff.getName(), diff.getArtifactKind(), project);
		    session.save(artifact);
		}
		return artifact;
	}

	private Commit convertCommit(CommitData data, Session session, Author author)
			throws ParseException {
		Commit commit = new Commit(data.getCommitId(), author, convertDate(data),
                data.getMessage(), data.getDiff(), data.getPriorCommit());
        session.save(commit);
		return commit;
	}

	private Author convertAuthor(CommitData data, Session session) {
		Author author = searchForPreviouslySavedAuthor(data.getAuthor(), session);
        if (author == null) {
            author = new Author(data.getAuthor(), data.getEmail());
            session.save(author);
        }
		return author;
	}

    private Author searchForPreviouslySavedAuthor(String name, Session session) {
        Author author = (Author) session.createCriteria(Author.class).add(
                Restrictions.eq("name", name)).uniqueResult();
        return author;
    }

    private Artifact searchForPreviouslySavedArtifact(String name, Project project, Session session) {
        Artifact artifact = (Artifact) session.createCriteria(Artifact.class).add(
                Restrictions.eq("name", name)).add(Restrictions.eq("project", project))
                .uniqueResult();
        return artifact;
    }

    private Calendar convertDate(CommitData data) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(data.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
