package sample.Main.Services;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lieroz on 25.05.17.
 */
@Service
@PropertySource("classpath:dropbox.properties")
public class ResourceService implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${dropbox.access-token}")
    private String ACCESS_TOKEN;

    @Value("${dropbox.resources-webp}")
    private String RESOURCES_PATH;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("dropbox", "en_US");
            DbxClientV2 dbxClientV2 = new DbxClientV2(dbxRequestConfig, ACCESS_TOKEN);
            ListFolderResult result = dbxClientV2.files().listFolder(RESOURCES_PATH);
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    List<SharedLinkMetadata> links = dbxClientV2.sharing().listSharedLinksBuilder()
                            .withPath(metadata.getPathLower()).withDirectOnly(true).start().getLinks();
                    if (links.size() == 0) {
                        links.add(dbxClientV2.sharing().createSharedLinkWithSettings(metadata.getPathLower()));
                    }
                    String url = links.get(0).getUrl();
                    url = url.substring(0, url.length() - 1) + "1";
                    jdbcTemplate.update("INSERT INTO cards (url) VALUES (?)", url);
                }
                if (!result.getHasMore()) {
                    break;
                }
                result = dbxClientV2.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException ex) {
            ex.printStackTrace();
        }
    }
}