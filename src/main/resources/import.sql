INSERT INTO `Project` (`id`, `name`, `scmUrl`) VALUES
(1, 'Tubaina', 'git://github.com/caelum/tubaina.git');

INSERT INTO `ConfigurationEntry` (`id`, `entry_key`, `entry_value`, `project_id`) VALUES
(1, 'changesets', 'br.com.caelum.revolution.changesets.AllChangeSetsFactory', 1),
(2, 'build', 'br.com.caelum.revolution.builds.nobuild.NoBuildFactory', 1),
(3, 'scm.repository', '/home/csokol/ime/tcc/MetricMinerHome/1/Tubaina', 1),
(4, 'scm', 'br.com.caelum.revolution.scm.git.GitFactory', 1);

