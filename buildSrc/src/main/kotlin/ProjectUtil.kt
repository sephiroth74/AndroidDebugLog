import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.provideDelegate
import java.net.URI

object ProjectUtil {

    fun isGitlabJob(): Boolean = !System.getenv("CI_JOB_ID").isNullOrBlank()

    fun artifactory(project: Project): Action<in MavenArtifactRepository> {
        val isSnapshot = project.version.toString().endsWith("SNAPSHOT")
        val swisscomStbuiArtifactoryPublishingReleaseUrl: String by project
        val swisscomStbuiArtifactoryPublishingSnapshotUrl: String by project
        val publishingUrl = if (!isSnapshot) swisscomStbuiArtifactoryPublishingReleaseUrl else swisscomStbuiArtifactoryPublishingSnapshotUrl

        val repoUsername = project.findProperty("artifactoryUser") as String
        val repoPassword = project.findProperty("artifactoryPassword") as String

        return Action {
            project.logger.info("publishing URL for ${project.name} = $publishingUrl")
            name = "artifactory"
            url = URI.create(publishingUrl)
            credentials {
                username = repoUsername
                password = repoPassword
            }
        }
    }
}
