package org.metricminer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.metricminer.scm.CommitData;
import org.metricminer.scm.DiffData;


public class PersistedCommitConverter {

    private HashMap<String, Author> savedAuthors;

	public PersistedCommitConverter() {
    	savedAuthors = new HashMap<String, Author>();
    }

    public Commit toDomain(CommitData data, StatelessSession session, Project project) throws ParseException {

        Author author = convertAuthor(data, session);
        Commit commit = convertCommit(data, session, author, project);

        for (DiffData diff : data.getDiffs()) {
            Artifact artifact = convertArtifact(session, project, diff);
            createModification(session, commit, diff, artifact);

            if (artifact.isSourceCode()) {
                SourceCode sourceCode = new SourceCode(artifact, commit, diff.getFullSourceCode());
                session.insert(sourceCode);
                convertBlameInformation(session, diff, sourceCode);
                
                artifact.addSource(sourceCode);
                commit.addSource(sourceCode);
                session.insert(sourceCode);
            }

        }

        return commit;
    }

	private void convertBlameInformation(StatelessSession session, DiffData diff, SourceCode sourceCode) {
		for(Map.Entry<Integer, String> entry :  diff.getBlameLines().entrySet()) {
			Author blamedAuthor = searchForPreviouslySavedAuthor(entry.getValue(), session);
			BlamedLine blamedLine = sourceCode.blame(entry.getKey(), blamedAuthor);
			
			session.insert(blamedLine);
		}
	}

	private void createModification(StatelessSession session, Commit commit, DiffData diff,
			Artifact artifact) {
		Modification modification = new Modification(diff.getDiff(), commit, artifact, diff
		        .getModificationKind());
		artifact.addModification(modification);
		commit.addModification(modification);
		commit.addArtifact(artifact);
		session.insert(modification);
	}

	private Artifact convertArtifact(StatelessSession session, Project project, DiffData diff) {
		Artifact artifact = searchForPreviouslySavedArtifact(diff.getName(), project, session);

		if (artifact == null) {
		    artifact = new Artifact(diff.getName(), diff.getArtifactKind(), project);
		    session.insert(artifact);
		}
		return artifact;
	}

	private Commit convertCommit(CommitData data, StatelessSession session, Author author, Project project)
			throws ParseException {
		Commit commit = new Commit(data.getCommitId(), author, convertDate(data),
                data.getMessage(), data.getDiff(), data.getPriorCommit(), project);
        session.insert(commit);
		return commit;
	}

	private Author convertAuthor(CommitData data, StatelessSession session) {
		Author author = searchForPreviouslySavedAuthor(data.getAuthor(), session);
        if (author == null) {
            author = new Author(data.getAuthor(), data.getEmail());
            savedAuthors.put(data.getAuthor(), author);
            session.insert(author);
        }
		return author;
	}

    private Author searchForPreviouslySavedAuthor(String name, StatelessSession session) {
    	if (savedAuthors.containsKey(name))
    		return savedAuthors.get(name);
        Author author = (Author) session.createCriteria(Author.class).add(
                Restrictions.eq("name", name)).uniqueResult();
        savedAuthors.put(name, author);
        return author;
    }

    private Artifact searchForPreviouslySavedArtifact(String name, Project project, StatelessSession session) {
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
