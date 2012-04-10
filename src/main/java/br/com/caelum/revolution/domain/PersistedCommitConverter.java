package br.com.caelum.revolution.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.Project;
import model.SourceCode;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.DiffData;

public class PersistedCommitConverter {
    private Map<String, Artifact> previouslySavedArtifactsByName;

    public PersistedCommitConverter() {
        this.previouslySavedArtifactsByName = new HashMap<String, Artifact>();
    }

    public Commit toDomain(CommitData data, Session session, Project project) throws ParseException {

        Author author = searchForPreviouslySavedAuthor(data.getAuthor(), session);
        if (author == null) {
            author = new Author(data.getAuthor(), data.getEmail());
            session.save(author);
        }

        Commit commit = new Commit(data.getCommitId(), author, convertDate(data),
                data.getMessage(), data.getDiff(), data.getPriorCommit());
        session.save(commit);

        for (DiffData diff : data.getDiffs()) {
            Artifact artifact = new Artifact(diff.getName(), diff.getArtifactKind(), project);

            if (!previouslySavedArtifactsByName.containsKey(artifact.getName())) {
                session.save(artifact);
                previouslySavedArtifactsByName.put(artifact.getName(), artifact);
            } else {
                artifact = previouslySavedArtifactsByName.get(artifact.getName());
            }

            Modification modification = new Modification(diff.getDiff(), commit, artifact, diff
                    .getModificationKind());
            artifact.addModification(modification);
            commit.addModification(modification);
            commit.addArtifact(artifact);
            session.save(modification);

            if (artifact.getKind() != ArtifactKind.BINARY) {
                SourceCode sourceCode = new SourceCode(artifact, commit, diff.getModifedSource());
                artifact.addSource(sourceCode);
                commit.addSource(sourceCode);
                session.save(sourceCode);
            }

        }

        return commit;
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

    private Artifact searchForPreviouslySavedArtifact(Artifact artifact) {
        if (previouslySavedArtifactsByName.containsKey(artifact.getName())) {
            return artifact;
        }
        return null;
    }

    private Calendar convertDate(CommitData data) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(data.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
