package morbrian.test;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.impl.api.ClientApiKey;
import morbrian.test.client.SimpleClient;
import morbrian.test.provisioning.ContainerConfigurationProvider;
import morbrian.test.provisioning.VendorSpecificProvisioner;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class) public class AuthRestApiTest {

  private static final ContainerConfigurationProvider configProvider =
      new ContainerConfigurationProvider();
  private static final String APP_BASE_PATH = "";
  @ArquillianResource private URL webappUrl;
  private SimpleClient client;

  @Deployment public static Archive<?> createDeployment() {
    return configProvider.createDeployment();
  }

  @BeforeClass public static void setupClass() throws Throwable {
    VendorSpecificProvisioner provisioner = configProvider.getVendorSpecificProvisioner();
    provisioner.setup();
    Client stormpathClient = Clients.builder().setApiKey(
        new ClientApiKey(System.getProperty("stormpath.apikey.id"),
            System.getProperty("stormpath.apikey.secret"))).build();

    Application application = stormpathClient
        .getResource(System.getProperty("stormpath.application.href"), Application.class);
    Account account = stormpathClient.instantiate(Account.class);

    account.setGivenName(UUID.randomUUID().toString()).setSurname(UUID.randomUUID().toString())
        .setUsername(configProvider.getUsername())
        .setEmail(UUID.randomUUID().toString() + "@morbrian.com")
        .setPassword(configProvider.getPassword()).getCustomData().put("favoriteColor", "purple");

    application.createAccount(account);
  }

  @AfterClass public static void teardownClass() throws Throwable {
    Client stormpathClient = Clients.builder().setApiKey(
        new ClientApiKey(System.getProperty("stormpath.apikey.id"),
            System.getProperty("stormpath.apikey.secret"))).build();

    Application application = stormpathClient
        .getResource(System.getProperty("stormpath.application.href"), Application.class);

    for (Account acct : application.getAccounts()) {
      if (acct.getUsername().equals(configProvider.getUsername())) {
        acct.delete();
        break;
      }
    }
  }

  @Before public void setup() {
    client = new SimpleClient(webappUrl.toString(), configProvider.getPasswordAuthentication());
  }

  @After public void teardown() {
    client.delete(Arrays.asList(APP_BASE_PATH, "logout"), null).close();
    client = null;
  }

  @Test public void validCredentialShouldRespondWithFound() throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("username", configProvider.getUsername());
    params.put("password", configProvider.getPassword());
    Response response = client.post(null, Arrays.asList(APP_BASE_PATH, "login.jsp"), params);
    assertEquals("response.status", Status.FOUND.getStatusCode(), response.getStatus());
    response.close();
  }

  // there seems to be some kind of runtime issue when WebClient loads,
  // perhaps a class loader conflict or something like that
  //  @Test public void test() throws Exception {
  //    WebClient webClient = new WebClient();
  //    HtmlPage page = webClient.getPage(webappUrl.toString() + "login.jsp");
  //    HtmlForm form = page.getFormByName("loginform");
  //    form.getInputByName("username").setValueAttribute("tk421");
  //    form.getInputByName("password").setValueAttribute("Changeme1");
  //    page = form.getInputByName("submit").click();
  //    // This'll throw an expection if not logged in
  //    page.getAnchorByHref("/logout");
  //  }

}
