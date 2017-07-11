/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2017-02-15 17:18:02 UTC)
 * on 2017-07-11 at 21:11:19 UTC 
 * Modify at your own risk.
 */

package ie.ncirl.syncDeviceAPI;

/**
 * Service definition for SyncDeviceAPI (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link SyncDeviceAPIRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class SyncDeviceAPI extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.22.0 of the syncDeviceAPI library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "syncDeviceAPI/v1/sync_device/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public SyncDeviceAPI(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  SyncDeviceAPI(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "sync_device".
   *
   * This request holds the parameters needed by the syncDeviceAPI server.  After setting any optional
   * parameters, call the {@link SyncDevice#execute()} method to invoke the remote operation.
   *
   * @param appId
   * @param device
   * @return the request
   */
  public SyncDevice syncDevice(java.lang.String appId, java.lang.String device) throws java.io.IOException {
    SyncDevice result = new SyncDevice(appId, device);
    initialize(result);
    return result;
  }

  public class SyncDevice extends SyncDeviceAPIRequest<ie.ncirl.syncDeviceAPI.model.MyBean> {

    private static final String REST_PATH = "{appId}/{device}";

    /**
     * Create a request for the method "sync_device".
     *
     * This request holds the parameters needed by the the syncDeviceAPI server.  After setting any
     * optional parameters, call the {@link SyncDevice#execute()} method to invoke the remote
     * operation. <p> {@link
     * SyncDevice#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param appId
     * @param device
     * @since 1.13
     */
    protected SyncDevice(java.lang.String appId, java.lang.String device) {
      super(SyncDeviceAPI.this, "POST", REST_PATH, null, ie.ncirl.syncDeviceAPI.model.MyBean.class);
      this.appId = com.google.api.client.util.Preconditions.checkNotNull(appId, "Required parameter appId must be specified.");
      this.device = com.google.api.client.util.Preconditions.checkNotNull(device, "Required parameter device must be specified.");
    }

    @Override
    public SyncDevice setAlt(java.lang.String alt) {
      return (SyncDevice) super.setAlt(alt);
    }

    @Override
    public SyncDevice setFields(java.lang.String fields) {
      return (SyncDevice) super.setFields(fields);
    }

    @Override
    public SyncDevice setKey(java.lang.String key) {
      return (SyncDevice) super.setKey(key);
    }

    @Override
    public SyncDevice setOauthToken(java.lang.String oauthToken) {
      return (SyncDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public SyncDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (SyncDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public SyncDevice setQuotaUser(java.lang.String quotaUser) {
      return (SyncDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public SyncDevice setUserIp(java.lang.String userIp) {
      return (SyncDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String appId;

    /**

     */
    public java.lang.String getAppId() {
      return appId;
    }

    public SyncDevice setAppId(java.lang.String appId) {
      this.appId = appId;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String device;

    /**

     */
    public java.lang.String getDevice() {
      return device;
    }

    public SyncDevice setDevice(java.lang.String device) {
      this.device = device;
      return this;
    }

    @Override
    public SyncDevice set(String parameterName, Object value) {
      return (SyncDevice) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link SyncDeviceAPI}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link SyncDeviceAPI}. */
    @Override
    public SyncDeviceAPI build() {
      return new SyncDeviceAPI(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link SyncDeviceAPIRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setSyncDeviceAPIRequestInitializer(
        SyncDeviceAPIRequestInitializer syncdeviceapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(syncdeviceapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}