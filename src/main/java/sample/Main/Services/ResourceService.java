package sample.Main.Services;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sample.Main.DAO.DropboxDAO;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by lieroz on 25.05.17.
 */
@Service
@PropertySource("classpath:dropbox.properties")
public class ResourceService implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${dropbox.access-token}")
    private String ACCESS_TOKEN;

    @Value("${dropbox.resources-jpg}")
    private String RESOURCES_PATH_JPG;

    @Value("${dropbox.resources-webp}")
    private String RESOURCES_PATH_WEBP;

    private DropboxDAO dropboxDAO;
    private DbxClientV2 dbxClientV2;

    public ResourceService(final JdbcTemplate jdbcTemplate) {
        dropboxDAO = new DropboxDAO(jdbcTemplate);
    }

    @PostConstruct
    public void setup() {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("dropbox", "en_US");
        dbxClientV2 = new DbxClientV2(dbxRequestConfig, ACCESS_TOKEN);
    }

    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            ListFolderResult jpgResources = dbxClientV2.files().listFolder(RESOURCES_PATH_JPG);
            ListFolderResult webpResources = dbxClientV2.files().listFolder(RESOURCES_PATH_WEBP);
            Integer count = jpgResources.getEntries().size() < webpResources.getEntries().size() ?
                    jpgResources.getEntries().size() : webpResources.getEntries().size();
            for (int i = 0; i < count; ++i) {
                List<SharedLinkMetadata> jpgMetadata = dbxClientV2.sharing().listSharedLinksBuilder()
                        .withPath(jpgResources.getEntries().get(i).getPathLower()).withDirectOnly(true).start().getLinks();
                List<SharedLinkMetadata> webpMetadata = dbxClientV2.sharing().listSharedLinksBuilder()
                        .withPath(webpResources.getEntries().get(i).getPathLower()).withDirectOnly(true).start().getLinks();
                if (jpgMetadata.size() == 0) {
                    jpgMetadata.add(dbxClientV2.sharing().createSharedLinkWithSettings(jpgResources.getEntries().get(i).getPathLower()));
                }
                if (webpMetadata.size() == 0) {
                    webpMetadata.add(dbxClientV2.sharing().createSharedLinkWithSettings(webpResources.getEntries().get(i).getPathLower()));
                }
                String jpgUrl = jpgMetadata.get(0).getUrl();
                String webpUrl = webpMetadata.get(0).getUrl();
                jpgUrl = jpgUrl.substring(0, jpgUrl.length() - 1) + "1";
                webpUrl = webpUrl.substring(0, webpUrl.length() - 1) + "1";
                dropboxDAO.addCard(jpgUrl, webpUrl);
            }
        } catch (DbxException ex) {
            ex.printStackTrace();
        }
    }
}
