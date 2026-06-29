package ui;

import compare.JsonComparator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TablePosition;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.ApiRequest;
import model.ApiRequestBodyPart;
import model.ApiResponse;
import model.DbConnectionConfig;
import model.DbValidationReport;
import model.DbValidationResult;
import model.DbValidationRule;
import model.PerformanceTestResult;
import model.ResponseFieldCandidate;
import model.WebTestCase;
import model.WebTestExecutionResult;
import model.WebTestRunReport;
import model.WebTestStep;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.kordamp.ikonli.javafx.FontIcon;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;
import service.ApiService;
import service.DbValidationService;
import service.DashboardExecutionService;
import service.DashboardExecutionService.StorageMode;
import service.PerformanceTestService;
import service.PlaywrightRecorderController;
import service.ResponseVariableService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ApiValidatorFxApp extends Application {

    private static final String APP_NAME = "TestWeave";
    private static final String APP_LOGO_RESOURCE = "/testweave-logo.png";
    private static final String APP_STYLESHEET_RESOURCE = "/css/testweave.css";
    private static final String PRIMARY = "#2f7cff";
    private static final String APP_BACKGROUND = "#05081a";
    private static final String PANEL_BACKGROUND = "#0d1430";
    private static final String PANEL_SURFACE = "#111b3d";
    private static final String BORDER_ACCENT = "#243b78";
    private static final String TEXT_PRIMARY = "#f8fbff";
    private static final String TEXT_MUTED = "#b7c7f7";
    private static final String CYAN = "#18d8e8";
    private static final String VIOLET = "#8b4df6";
    private static final List<String> RUNTIME_VARIABLES = List.of("randomString", "randomInt", "randomDate");
    private static final String CODEX_DOCKER_IMAGE_ENV = "CODEX_DOCKER_IMAGE";
    private static final String CODEX_DOCKER_IMAGE_DEFAULT = "docker/sandbox-templates:codex";
    private static final String CODEX_CONTAINER_NAME_ENV = "CODEX_CONTAINER_NAME";
    private static final String CODEX_CONTAINER_NAME_DEFAULT = "codex-cli";
    private static final String HERMES_DOCKER_IMAGE_ENV = "HERMES_DOCKER_IMAGE";
    private static final String HERMES_DOCKER_IMAGE_DEFAULT = "nousresearch/hermes-agent:latest";
    private static final String HERMES_CONTAINER_NAME_ENV = "HERMES_CONTAINER_NAME";
    private static final String HERMES_CONTAINER_NAME_DEFAULT = "hermes";
    private static final String HERMES_DASHBOARD_URL = "http://127.0.0.1:9119";
    private static final String CONFIG_CACHE_DB_NAME = "testweave-config-cache.db";
    private static final String CONFIG_CACHE_TABLE = "testweave_config_paths";
    private static final String CONFIG_STORAGE_CLOUD = "Cloud";
    private static final String CONFIG_STORAGE_LOCAL = "Local";
    private static final String HERMES_SESSION_TABLE = "hermes_cli_sessions";
    private static final String API_AI_MEMORY_TABLE = "api_ai_agent_memory";
    private static final String API_AI_FIREBASE_PATH = "/testweave-api-ai-agent/memory";
    private static final String API_AI_HERMES_FIREBASE_PATH = "/testweave-api-ai-agent/hermes-sessions";
    private static final String HERMES_NEW_SESSION = "New Session";
    private static final List<String> WEB_TEST_ACTIONS = List.of(
            "Navigate",
            "Type",
            "Click",
            "Select Option",
            "Wait For Visible",
            "Wait For Text",
            "Wait For URL",
            "Wait For Network Idle",
            "Assert Element Visible",
            "Assert URL Contains",
            "Fill By Label",
            "Click By Text",
            "Click By Role",
            "Validate Text",
            "Get Text",
            "Flow Variable",
            "Screenshot",
            "Visual Baseline",
            "Visual Compare");
    private static final List<String> TESTWEAVE_CONFIG_FOLDERS = List.of(
            "API",
            "API/ExpectedResponse",
            "API/SavedResponse",
            "API/SavedRequest",
            "DB",
            "DB/Connection",
            "DB/SQLQuery",
            "DB/DBRules",
            "WebUI",
            "WebUI/Recording",
            "TestSuite",
            "Variables",
            "AIAgent",
            "AIAgent/Sessions");

    private final ApiService apiService = new ApiService();
    private final JsonComparator comparator = new JsonComparator();
    private final PerformanceTestService performanceTestService = new PerformanceTestService();
    private final DashboardExecutionService dashboardExecutionService = new DashboardExecutionService();
    private final DbValidationService dbValidationService = new DbValidationService();
    private final PlaywrightRecorderController playwrightRecorderController = new PlaywrightRecorderController();
    private final ResponseVariableService responseVariableService = new ResponseVariableService();
    private final Map<String, String> savedVariables = new ConcurrentHashMap<>();
    private final Map<String, String> savedVariablePaths = new ConcurrentHashMap<>();
    private final Map<String, String> savedVariableTypes = new ConcurrentHashMap<>();
    private final Map<String, String> postmanCollectionVariables = new ConcurrentHashMap<>();
    private final Map<String, String> postmanEnvironmentVariables = new ConcurrentHashMap<>();
    private final List<ComboBox<String>> variableDropdowns = new ArrayList<>();

    private Stage stage;
    private ProgressBar globalLoadingBar;
    private TabPane mainNavigationTabs;
    private boolean sliderMenuOpen;
    private Timeline sliderMenuTimeline;
    private Timeline sliderLeafletIdleTimeline;
    private final AtomicInteger activeTaskCount = new AtomicInteger(0);
    private double windowDragOffsetX;
    private double windowDragOffsetY;
    private double splashDragOffsetX;
    private double splashDragOffsetY;
    private TextField endpointField;
    private ComboBox<String> apiUrlVariableBox;
    private ComboBox<String> methodBox;
    private ComboBox<String> authTypeBox;
    private ComboBox<String> requestFormatBox;
    private PasswordField tokenField;
    private TextField visibleTokenField;
    private ComboBox<String> oauthGrantTypeBox;
    private TextField oauthTokenUrlField;
    private TextField oauthClientIdField;
    private PasswordField oauthClientSecretField;
    private TextField oauthScopeField;
    private TextField oauthUsernameField;
    private PasswordField oauthPasswordField;
    private TextField oauthAuthCodeField;
    private TextField oauthRedirectUriField;
    private TextField oauthRefreshTokenField;
    private CheckBox oauthBasicAuthCheck;
    private Label oauthStatusLabel;
    private CheckBox sslVerificationDisabledCheck;
    private TextField trustStorePathField;
    private PasswordField trustStorePasswordField;
    private TextField keyStorePathField;
    private PasswordField keyStorePasswordField;
    private CheckBox proxyEnabledCheck;
    private ComboBox<String> proxySchemeBox;
    private TextField proxyHostField;
    private TextField proxyPortField;
    private TextField proxyUsernameField;
    private PasswordField proxyPasswordField;
    private TextArea headersArea;
    private TextArea bodyArea;
    private TextArea preRequestScriptArea;
    private TextArea testScriptArea;
    private TextArea prettyResponseArea;
    private TextArea rawResponseArea;
    private TextArea responseHeadersArea;
    private TextArea responseCookiesArea;
    private TabPane apiTesterTabs;
    private TreeView<PostmanCollectionNode> postmanCollectionTree;
    private TextField postmanCollectionPathField;
    private TextField postmanEnvironmentPathField;
    private Label postmanCollectionStatusLabel;
    private TextArea postmanCollectionDetailsArea;
    private PostmanCollectionNode currentPostmanRequestNode;
    private String currentPostmanBodyMode;
    private List<ApiRequestBodyPart> currentPostmanMultipartParts = new ArrayList<>();
    private String currentPostmanBinaryFilePath;
    private TabPane apiResponseTabs;
    private Label statusValueLabel;
    private Label timeValueLabel;
    private Label sizeValueLabel;
    private Label apiStatusLabel;
    private ApiResponse lastResponse;
    private String lastExpectedJson;
    private String lastActualJson;

    private TableView<Map<String, String>> responseFieldsTable;
    private ObservableList<Map<String, String>> responseFieldRows;
    private TableView<Map<String, String>> fieldValidationsTable;
    private ObservableList<Map<String, String>> fieldValidationRows;
    private TextField expectedJsonPathField;
    private ComboBox<String> compareModeBox;
    private TableView<Map<String, String>> compareTable;
    private ObservableList<Map<String, String>> compareRows;

    private Spinner<Integer> perfThreadsSpinner;
    private Spinner<Integer> perfIterationsSpinner;
    private TextArea perfBodyArea;
    private TextArea perfLogArea;
    private Label perfSamplesLabel;
    private Label perfErrorsLabel;
    private Label perfThroughputLabel;
    private Label perfDurationLabel;
    private Label perfReportLabel;
    private BarChart<String, Number> perfChart;
    private Path lastPerformanceReportPath;
    private ObservableList<Map<String, String>> dashboardRows = FXCollections.observableArrayList();
    private TableView<Map<String, String>> dashboardTable;
    private ComboBox<String> dashboardPeriodBox;
    private DatePicker dashboardFromDate;
    private DatePicker dashboardToDate;
    private Label dashboardLastRunCasesLabel;
    private Label dashboardExecutionsLabel;
    private Label dashboardPassRateLabel;
    private Label dashboardPerformanceLabel;
    private Label dashboardHealthLabel;
    private Label dashboardSyncLabel;
    private Button dashboardRefreshButton;
    private PieChart dashboardStatusChart;
    private BarChart<String, Number> dashboardPerformanceChart;
    private BarChart<String, Number> dashboardHistoryChart;
    private JSONArray dashboardExecutions = new JSONArray();
    private ObservableList<Map<String, String>> dashboardLastResultRows = FXCollections.observableArrayList();
    private TableView<Map<String, String>> dashboardLastResultsTable;

    private ComboBox<String> dbTypeBox;
    private TextField jdbcUrlField;
    private TextField dbUsernameField;
    private PasswordField dbPasswordField;
    private TextField visibleDbPasswordField;
    private TextField driverClassField;
    private TextArea dbQueryArea;
    private ComboBox<String> dbVariableDropdown;
    private Label dbConnectionStatusLabel;
    private TableView<Map<String, String>> dbRulesTable;
    private ObservableList<Map<String, String>> dbRuleRows;
    private TableView<Map<String, String>> dbQueryResultsTable;
    private ObservableList<Map<String, String>> dbQueryResultRows;
    private TableView<Map<String, String>> dbResultsTable;
    private ObservableList<Map<String, String>> dbResultRows;
    private TableView<Map<String, String>> dbColumnValidationsTable;
    private ObservableList<Map<String, String>> dbColumnValidationRows;
    private Label dbSummaryLabel;
    private TextField dbValidationTestSuiteField;
    private TextField dbValidationTestCaseField;
    private TextField dbValidationTestStepField;
    private Path dbConnectionFilePath;

    private TextField webTestNameField;
    private TextField webStartUrlField;
    private TextField webCdpEndpointField;
    private CheckBox webHeadlessCheck;
    private CheckBox webSlowMoCheck;
    private Label webRecorderStatusLabel;
    private Label webBrowserUrlLabel;
    private Label webRunSummaryLabel;
    private TableView<Map<String, String>> webStepsTable;
    private ObservableList<Map<String, String>> webStepRows;
    private TableView<Map<String, String>> webResultsTable;
    private ObservableList<Map<String, String>> webResultRows;

    private TextArea codexLogArea;
    private Stage codexCliStage;
    private TextArea codexChatArea;
    private TextArea codexPromptArea;
    private Button codexSendButton;
    private Button codexCancelButton;
    private volatile Process codexChatProcess;
    private boolean codexExecSessionStarted;
    private TextArea hermesLogArea;
    private TextArea configHermesLogArea;
    private TextField hermesAiAgentPathField;
    private ComboBox<String> hermesSessionBox;
    private TextField hermesManualSessionIdField;
    private final Map<String, HermesSessionRecord> hermesSessionRecords = new HashMap<>();

    private TableView<Map<String, String>> variablesTable;
    private ObservableList<Map<String, String>> variableRows;
    private TextField variablesPathField;
    private TableView<Map<String, String>> testSuiteStepsTable;
    private ObservableList<Map<String, String>> testSuiteRows = FXCollections.observableArrayList();
    private ObservableList<Map<String, String>> suiteBuilderTreeRows = FXCollections.observableArrayList();
    private ObservableList<Map<String, String>> suiteBuilderCanvasRows = FXCollections.observableArrayList();
    private ObservableList<Map<String, String>> suiteBuilderCanvasConnections = FXCollections.observableArrayList();
    private Map<String, BuilderTreeNode> suiteBuilderDragItems = new HashMap<>();
    private TreeView<BuilderTreeNode> suiteBuilderTreeView;
    private Pane suiteBuilderCanvas;
    private Stage suiteBuilderStage;
    private boolean suiteBuilderReverseFlow;
    private int suiteBuilderSelectedCanvasIndex = -1;
    private int suiteBuilderConnectionSourceIndex = -1;
    private int suiteBuilderSelectedConnectionIndex = -1;
    private Label testSuiteRunnerStatusLabel;
    private TextField testSuiteNameField;
    private ComboBox<String> testCaseNameField;
    private TextField testSuiteWorkbookPathField;
    private CheckBox testSuiteParallelExecutionCheck;
    private TextField testSuiteThreadCountField;
    private ComboBox<String> testSuiteBuilderAddTypeBox;
    private TextField githubOwnerField;
    private TextField githubRepoField;
    private TextField githubBranchField;
    private Label githubStatusLabel;
    private String githubAccessToken;
    private Path lastTestSuiteReportPath;
    private ExecutorService testSuiteRunnerExecutor;
    private final AtomicBoolean testSuiteStopRequested = new AtomicBoolean(false);
    private TextField fieldValidationTestSuiteField;
    private TextField fieldValidationTestCaseField;
    private TextField fieldValidationTestStepField;
    private TextField jsonCompareTestSuiteField;
    private TextField jsonCompareTestCaseField;
    private TextField jsonCompareTestStepField;
    private TextField performanceTestSuiteField;
    private TextField performanceTestCaseField;
    private TextField performanceTestStepField;
    private TextField webTestingTestSuiteField;
    private TextField webTestingTestCaseField;
    private TextField webTestingTestStepField;
    private TextField configBasePathField;
    private Label configStatusLabel;
    private Label configCacheKeyLabel;
    private Label configCacheDbLabel;
    private ToggleButton configExecutionStorageToggle;
    private Label configExecutionStorageLabel;
    private ToggleButton apiAiAgentStorageToggle;
    private Label apiAiAgentStorageLabel;
    private Label apiAiTesterConnectionLabel;
    private Label apiAiValidationConnectionLabel;
    private ComboBox<String> apiAiHermesSessionBox;
    private Label apiAiHermesConnectionLabel;
    private final Map<String, HermesSessionRecord> apiAiHermesSessionRecords = new HashMap<>();
    private volatile HermesSessionRecord activeApiAiHermesSession;
    private volatile String activeApiAiHermesDashboardUrl = "";
    private volatile String apiAiConnectedModel = "Hermes Agent";

    private record NavigationOption(String title, Supplier<javafx.scene.Node> contentFactory) {
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(
                new atlantafx.base.theme.Dracula().getUserAgentStylesheet()
        );
        this.stage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double maxSceneWidth = Math.max(640, visualBounds.getWidth() - 32);
        double maxSceneHeight = Math.max(520, visualBounds.getHeight() - 32);
        double sceneWidth = clamp(visualBounds.getWidth() * 0.92, Math.min(900, maxSceneWidth), Math.min(1540, maxSceneWidth));
        double sceneHeight = clamp(visualBounds.getHeight() * 0.90, Math.min(620, maxSceneHeight), Math.min(1040, maxSceneHeight));

        TabPane tabs = createTabs();
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(tabs);
        root.setStyle("-fx-background-color: " + APP_BACKGROUND + ";");

        Scene scene = new Scene(createSliderMenuShell(root, tabs), sceneWidth, sceneHeight);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        primaryStage.setTitle(APP_NAME);
        primaryStage.setMinWidth(Math.min(900, visualBounds.getWidth() * 0.80));
        primaryStage.setMinHeight(Math.min(620, visualBounds.getHeight() * 0.80));
        primaryStage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - sceneWidth) / 2);
        primaryStage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - sceneHeight) / 2);
        primaryStage.setScene(scene);
        loadApplicationIcon(primaryStage);
        primaryStage.setOnCloseRequest(event -> cleanupBeforeClose());
        showStartupScreenThenMain(primaryStage, visualBounds);
    }

    private void showStartupScreenThenMain(Stage primaryStage, Rectangle2D visualBounds) {
        Stage splashStage = createStartupStage(visualBounds);
        ProgressBar progressBar = (ProgressBar) splashStage.getScene().lookup("#startupProgressBar");
        Label progressLabel = (Label) splashStage.getScene().lookup("#startupProgressLabel");
        Label statusLabel = (Label) splashStage.getScene().lookup("#startupStatusLabel");
        Timeline launchTimeline = new Timeline(
                new KeyFrame(javafx.util.Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 0),
                        new KeyValue(progressLabel.textProperty(), "0%"),
                        new KeyValue(statusLabel.textProperty(), "Preparing TestWeave workspace...")),
                new KeyFrame(javafx.util.Duration.seconds(0.8),
                        new KeyValue(progressBar.progressProperty(), 0.28),
                        new KeyValue(progressLabel.textProperty(), "28%"),
                        new KeyValue(statusLabel.textProperty(), "Loading validators and workflow tools...")),
                new KeyFrame(javafx.util.Duration.seconds(1.7),
                        new KeyValue(progressBar.progressProperty(), 0.67),
                        new KeyValue(progressLabel.textProperty(), "67%"),
                        new KeyValue(statusLabel.textProperty(), "Connecting AI planning surfaces...")),
                new KeyFrame(javafx.util.Duration.seconds(3),
                        new KeyValue(progressBar.progressProperty(), 1),
                        new KeyValue(progressLabel.textProperty(), "100%"),
                        new KeyValue(statusLabel.textProperty(), "Weaving quality into every execution..."))
        );
        launchTimeline.setOnFinished(event -> {
            splashStage.close();
            primaryStage.show();
            primaryStage.toFront();
        });
        splashStage.show();
        launchTimeline.play();
    }

    private Stage createStartupStage(Rectangle2D visualBounds) {
        Stage splashStage = new Stage(StageStyle.UNDECORATED);
        splashStage.setTitle(APP_NAME + " Startup");
        loadApplicationIcon(splashStage);

        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("startup-content");

        FontIcon logo = new FontIcon("fth-box");
        logo.setIconSize(92);
        logo.getStyleClass().add("startup-logo");
        Label title = new Label(APP_NAME);
        title.getStyleClass().add("startup-title");
        Label subtitle = new Label("AI-Powered Validation. Seamlessly Connected.");
        subtitle.getStyleClass().add("startup-subtitle");

        HBox capabilities = new HBox(28,
                startupCapability("fth-code", "API Testing"),
                startupCapability("fth-database", "DB Validation"),
                startupCapability("fth-monitor", "Web UI Testing"),
                startupCapability("fth-git-branch", "Workflow Builder"),
                startupCapability("fth-bar-chart-2", "Reports & Analytics"),
                startupCapability("fth-cpu", "AI Agents"));
        capabilities.setAlignment(Pos.CENTER);
        capabilities.getStyleClass().add("startup-capabilities");

        HBox signal = new HBox(34,
                startupSignal("fth-code"),
                startupSignal("fth-database"),
                startupSignal("fth-globe"),
                startupSignal("fth-git-branch"),
                startupSignal("fth-bar-chart-2"),
                startupSignal("fth-cpu"));
        signal.setAlignment(Pos.CENTER);
        signal.getStyleClass().add("startup-signal-row");

        Label status = new Label("Preparing TestWeave workspace...");
        status.setId("startupStatusLabel");
        status.getStyleClass().add("startup-status");
        ProgressBar progress = new ProgressBar(0);
        progress.setId("startupProgressBar");
        progress.getStyleClass().add("startup-progress");
        progress.setPrefWidth(560);
        Label percent = new Label("0%");
        percent.setId("startupProgressLabel");
        percent.getStyleClass().add("startup-percent");

        HBox trustRow = new HBox(40,
                startupTrust("fth-shield", "Reliable", "Enterprise Grade Validation"),
                startupTrust("fth-zap", "Intelligent", "AI-driven Suggestions"),
                startupTrust("fth-target", "Integrated", "API, DB, UI & Performance"),
                startupTrust("fth-lock", "Secure", "Your Data, Your Control"));
        trustRow.setAlignment(Pos.CENTER);
        trustRow.getStyleClass().add("startup-trust-row");

        Label footer = new Label("Powered by Vision AI");
        footer.getStyleClass().add("startup-footer");
        content.getChildren().addAll(logo, title, subtitle, capabilities, signal, status, progress, percent, trustRow, footer);

        StackPane root = new StackPane(content);
        root.getStyleClass().add("startup-root");
        Scene scene = new Scene(root, 980, 680);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        splashStage.setScene(scene);
        splashStage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - 980) / 2);
        splashStage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - 680) / 2);
        root.setOnMousePressed(event -> {
            splashDragOffsetX = event.getSceneX();
            splashDragOffsetY = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            splashStage.setX(event.getScreenX() - splashDragOffsetX);
            splashStage.setY(event.getScreenY() - splashDragOffsetY);
        });
        return splashStage;
    }

    private VBox startupCapability(String iconLiteral, String labelText) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(34);
        icon.getStyleClass().add("startup-capability-icon");
        Label label = new Label(labelText);
        label.getStyleClass().add("startup-capability-label");
        VBox box = new VBox(8, icon, label);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private StackPane startupSignal(String iconLiteral) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(30);
        icon.getStyleClass().add("startup-signal-icon");
        StackPane tile = new StackPane(icon);
        tile.getStyleClass().add("startup-signal-tile");
        return tile;
    }

    private HBox startupTrust(String iconLiteral, String titleText, String detailText) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(28);
        icon.getStyleClass().add("startup-trust-icon");
        Label title = new Label(titleText);
        title.getStyleClass().add("startup-trust-title");
        Label detail = new Label(detailText);
        detail.getStyleClass().add("startup-trust-detail");
        VBox text = new VBox(3, title, detail);
        HBox item = new HBox(12, icon, text);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private HBox createHeader() {
        Label title = new Label(APP_NAME);
        title.getStyleClass().add("app-title");
        HBox workLoop = createWorkLoopAnimation();
        Label poweredBy = new Label("Powered by Vision AI");
        poweredBy.getStyleClass().add("powered-by");
        globalLoadingBar = new ProgressBar();
        globalLoadingBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        globalLoadingBar.setVisible(false);
        globalLoadingBar.setManaged(false);
        globalLoadingBar.setPrefWidth(150);
        globalLoadingBar.getStyleClass().add("global-loading");
        Button minimize = windowControl("fth-minus", "Minimize");
        minimize.setOnAction(e -> stage.setIconified(true));
        Button maximize = windowControl("fth-square", "Maximize or restore");
        maximize.setOnAction(e -> stage.setMaximized(!stage.isMaximized()));
        Button close = windowControl("fth-x", "Close");
        close.getStyleClass().add("window-close-button");
        close.setOnAction(e -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox windowControls = new HBox(8, minimize, maximize, close);
        windowControls.getStyleClass().add("window-controls");
        HBox header = new HBox(12, title, workLoop, spacer, globalLoadingBar, poweredBy, windowControls);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 12, 24));
        header.getStyleClass().add("top-bar");
        header.setOnMousePressed(event -> {
            windowDragOffsetX = event.getSceneX();
            windowDragOffsetY = event.getSceneY();
        });
        header.setOnMouseDragged(event -> {
            if (stage != null && !stage.isMaximized()) {
                stage.setX(event.getScreenX() - windowDragOffsetX);
                stage.setY(event.getScreenY() - windowDragOffsetY);
            }
        });
        header.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && stage != null) {
                stage.setMaximized(!stage.isMaximized());
            }
        });
        return header;
    }

    private HBox createWorkLoopAnimation() {
        Label plan = flowStep("fth-users", "Plan");
        Label execute = flowStep("fth-play", "Execute");
        Label analyze = flowStep("fth-cpu", "Analyze");
        Label decide = flowStep("fth-check-circle", "Decide");
        Label arrow1 = new Label(">");
        Label arrow2 = new Label(">");
        Label arrow3 = new Label(">");
        HBox flow = new HBox(7, plan, arrow1, execute, arrow2, analyze, arrow3, decide);
        flow.setAlignment(Pos.CENTER_LEFT);
        flow.getStyleClass().add("work-loop");
        List<Label> steps = List.of(plan, execute, analyze, decide);
        AtomicInteger active = new AtomicInteger(0);
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(1100), event -> {
            int index = active.getAndUpdate(value -> (value + 1) % steps.size());
            for (int i = 0; i < steps.size(); i++) {
                steps.get(i).getStyleClass().remove("work-loop-active");
                if (i == index) {
                    steps.get(i).getStyleClass().add("work-loop-active");
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return flow;
    }

    private Label flowStep(String iconLiteral, String text) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(13);
        Label label = new Label(text, icon);
        label.getStyleClass().add("work-loop-step");
        label.setGraphicTextGap(5);
        return label;
    }

    private StackPane createSliderMenuShell(javafx.scene.Node content, TabPane tabs) {
        double menuWidth = 250;
        double leafletWidth = 48;
        double hiddenOffset = -(menuWidth + leafletWidth - 8);
        double leafletOffset = -menuWidth;
        VBox menu = new VBox(10);
        menu.getStyleClass().add("slider-menu");
        menu.setPrefWidth(menuWidth);
        menu.setMinWidth(menuWidth);
        menu.setMaxWidth(menuWidth);

        Label title = new Label("Navigate");
        title.getStyleClass().add("slider-menu-title");
        Label subtitle = new Label("Select a workspace tab");
        subtitle.getStyleClass().add("slider-menu-subtitle");
        VBox header = new VBox(3, title, subtitle);
        header.getStyleClass().add("slider-menu-header");
        menu.getChildren().add(header);

        VBox optionList = new VBox(6);
        optionList.getStyleClass().add("slider-menu-options");
        menu.getChildren().add(optionList);

        Button leaflet = new Button("Menu");
        leaflet.setGraphic(new FontIcon("fth-menu"));
        leaflet.getStyleClass().add("slider-menu-leaflet");
        leaflet.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        leaflet.setOnAction(event -> setSliderMenuOpen(!sliderMenuOpen, menu.getParent()));
        leaflet.setOnMouseEntered(event -> revealSliderLeaflet(menu.getParent()));

        HBox slider = new HBox(menu, leaflet);
        slider.getStyleClass().add("slider-menu-shell");
        slider.getProperties().put("menuWidth", menuWidth);
        slider.getProperties().put("leafletOffset", leafletOffset);
        slider.getProperties().put("hiddenOffset", hiddenOffset);
        slider.setTranslateX(hiddenOffset);
        slider.setMaxWidth(menuWidth + leafletWidth);
        slider.setPickOnBounds(false);
        slider.setOnMouseEntered(event -> revealSliderLeaflet(slider));
        slider.setOnMouseExited(event -> scheduleSliderLeafletHide(slider));
        StackPane.setAlignment(slider, Pos.CENTER_LEFT);

        List<Button> optionButtons = new ArrayList<>();
        for (NavigationOption navigationOption : mainNavigationOptions()) {
            Button option = new Button(navigationOption.title());
            option.setGraphic(iconFor(navigationOption.title()));
            option.setMaxWidth(Double.MAX_VALUE);
            option.setUserData(navigationOption.title());
            option.getStyleClass().add("slider-menu-option");
            option.setOnAction(event -> {
                openNavigationTab(tabs, navigationOption);
                setSliderMenuOpen(false, slider);
            });
            optionButtons.add(option);
            optionList.getChildren().add(option);
        }
        tabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
                updateSliderMenuSelection(optionButtons, tabs));
        updateSliderMenuSelection(optionButtons, tabs);

        StackPane shell = new StackPane(content, slider);
        shell.getStyleClass().add("app-shell");
        shell.setOnMouseMoved(event -> {
            if (!sliderMenuOpen && event.getX() <= 18) {
                revealSliderLeaflet(slider);
            }
        });
        scheduleSliderLeafletHide(slider);
        return shell;
    }

    private void updateSliderMenuSelection(List<Button> optionButtons, TabPane tabs) {
        Tab selectedTab = tabs.getSelectionModel().getSelectedItem();
        String selectedTitle = selectedTab == null ? "" : selectedTab.getText();
        for (Button option : optionButtons) {
            option.getStyleClass().remove("slider-menu-option-selected");
            if (Objects.equals(option.getUserData(), selectedTitle)) {
                option.getStyleClass().add("slider-menu-option-selected");
            }
        }
    }

    private void setSliderMenuOpen(boolean open, javafx.scene.Node slider) {
        if (slider == null || sliderMenuOpen == open) {
            return;
        }
        sliderMenuOpen = open;
        if (sliderLeafletIdleTimeline != null) {
            sliderLeafletIdleTimeline.stop();
        }
        if (sliderMenuTimeline != null) {
            sliderMenuTimeline.stop();
        }
        double menuWidth = sliderNumericProperty(slider, "menuWidth", 250);
        double leafletOffset = sliderNumericProperty(slider, "leafletOffset", -menuWidth);
        sliderMenuTimeline = new Timeline(
                new KeyFrame(javafx.util.Duration.millis(260),
                        new KeyValue(slider.translateXProperty(), open ? 0 : leafletOffset))
        );
        sliderMenuTimeline.play();
        if (!open) {
            scheduleSliderLeafletHide(slider);
        }
    }

    private void revealSliderLeaflet(javafx.scene.Node slider) {
        if (slider == null || sliderMenuOpen) {
            return;
        }
        if (sliderLeafletIdleTimeline != null) {
            sliderLeafletIdleTimeline.stop();
        }
        if (sliderMenuTimeline != null) {
            sliderMenuTimeline.stop();
        }
        double leafletOffset = sliderNumericProperty(slider, "leafletOffset", -250);
        sliderMenuTimeline = new Timeline(
                new KeyFrame(javafx.util.Duration.millis(180),
                        new KeyValue(slider.translateXProperty(), leafletOffset))
        );
        sliderMenuTimeline.play();
        scheduleSliderLeafletHide(slider);
    }

    private void scheduleSliderLeafletHide(javafx.scene.Node slider) {
        if (slider == null || sliderMenuOpen) {
            return;
        }
        if (sliderLeafletIdleTimeline != null) {
            sliderLeafletIdleTimeline.stop();
        }
        sliderLeafletIdleTimeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(3), event -> hideSliderLeaflet(slider)));
        sliderLeafletIdleTimeline.play();
    }

    private void hideSliderLeaflet(javafx.scene.Node slider) {
        if (slider == null || sliderMenuOpen) {
            return;
        }
        if (sliderMenuTimeline != null) {
            sliderMenuTimeline.stop();
        }
        double hiddenOffset = sliderNumericProperty(slider, "hiddenOffset", -290);
        sliderMenuTimeline = new Timeline(
                new KeyFrame(javafx.util.Duration.millis(220),
                        new KeyValue(slider.translateXProperty(), hiddenOffset))
        );
        sliderMenuTimeline.play();
    }

    private double sliderNumericProperty(javafx.scene.Node slider, String key, double fallback) {
        Object value = slider == null ? null : slider.getProperties().get(key);
        return value instanceof Number number ? number.doubleValue() : fallback;
    }

    private TabPane createTabs() {
        TabPane tabs = new TabPane();
        mainNavigationTabs = tabs;
        Tab dashboardTab = tab("Dashboard", createDashboardPanel());
        dashboardTab.setClosable(false);
        dashboardTab.setOnSelectionChanged(e -> {
            if (dashboardTab.isSelected()) refreshDashboard();
        });
        tabs.getTabs().add(dashboardTab);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        return tabs;
    }

    private List<NavigationOption> mainNavigationOptions() {
        return List.of(
                new NavigationOption("Dashboard", this::createDashboardPanel),
                new NavigationOption("API Tester", this::createApiPanel),
                new NavigationOption("API Validation", this::createApiValidationPanel),
                new NavigationOption("Performance Test", this::createPerformancePanel),
                new NavigationOption("DB Validator", this::createDbValidatorPanel),
                new NavigationOption("Web Testing", this::createWebTestingPanel),
                new NavigationOption("AI Planning", this::createAiPlanningPanel),
                new NavigationOption("Test Suite Runner", this::createTestSuitePanel),
                new NavigationOption("Variables", this::createVariablesPanel),
                new NavigationOption("Config", this::createConfigPanel));
    }

    private void openNavigationTab(TabPane tabs, NavigationOption navigationOption) {
        for (Tab tab : tabs.getTabs()) {
            if (Objects.equals(tab.getText(), navigationOption.title())) {
                tabs.getSelectionModel().select(tab);
                if ("Variables".equals(navigationOption.title())) {
                    refreshVariablesView();
                }
                return;
            }
        }
        Tab tab = tab(navigationOption.title(), navigationOption.contentFactory().get());
        tab.setClosable(true);
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
    }

    private Tab tab(String title, javafx.scene.Node content) {
        Tab tab = new Tab(title, content);
        tab.setGraphic(iconFor(title));
        return tab;
    }

    private javafx.scene.Node createAiPlanningPanel() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(tab("Hermes Agent", createHermesAgentPanel()));
        tabs.getTabs().add(tab("Codex CLI", createCodexCliPanel()));
        return padded(card("AI Planning", tabs));
    }

    private javafx.scene.Node createDashboardPanel() {
        dashboardLastRunCasesLabel = metric("0");
        dashboardExecutionsLabel = metric("0");
        dashboardPassRateLabel = metric("0%");
        dashboardPerformanceLabel = metric("--");
        dashboardHealthLabel = metric("No data");
        HBox metrics = new HBox(12,
                dashboardMetricCard("TEST CASES · LAST RUN", dashboardLastRunCasesLabel),
                dashboardMetricCard("TOTAL EXECUTIONS", dashboardExecutionsLabel),
                dashboardMetricCard("PASS PERCENTAGE", dashboardPassRateLabel),
                dashboardMetricCard("AVG LOAD PERFORMANCE", dashboardPerformanceLabel),
                dashboardMetricCard("EXECUTION HEALTH", dashboardHealthLabel));
        for (javafx.scene.Node node : metrics.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);

        dashboardStatusChart = new PieChart();
        dashboardStatusChart.setTitle("Unified Pass / Fail");
        dashboardStatusChart.setLabelsVisible(true);
        dashboardPerformanceChart = dashboardBarChart("Average Performance Metrics", "Metric", "Value");
        dashboardHistoryChart = dashboardBarChart("Execution Activity", "Day", "Executions");
        dashboardStatusChart.setPrefHeight(310);
        dashboardPerformanceChart.setPrefHeight(310);
        dashboardHistoryChart.setPrefHeight(310);
        HBox charts = new HBox(12, card("Quality Split", dashboardStatusChart),
                card("Load Test Profile", dashboardPerformanceChart), card("Run Histogram", dashboardHistoryChart));
        for (javafx.scene.Node node : charts.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
            ((Region) node).setMaxWidth(Double.MAX_VALUE);
        }

        dashboardPeriodBox = combo("Past 3 days", "Past 5 days", "1 month", "3 months", "Custom dates");
        dashboardFromDate = new DatePicker(LocalDate.now().minusDays(3));
        dashboardToDate = new DatePicker(LocalDate.now());
        updateDashboardDateControls();
        dashboardSyncLabel = new Label("Dashboard data is mirrored locally and synchronized with Firebase.");
        dashboardSyncLabel.getStyleClass().add("muted");
        dashboardRefreshButton = primary("Refresh Dashboard");
        dashboardRefreshButton.setOnAction(e -> refreshDashboard());
        dashboardPeriodBox.setOnAction(e -> {
            updateDashboardDateControls();
            applyDashboardFilter();
        });
        dashboardFromDate.setOnAction(e -> applyDashboardFilter());
        dashboardToDate.setOnAction(e -> applyDashboardFilter());
        FlowPane filters = actionRow(labeled("Period", dashboardPeriodBox), labeled("From", dashboardFromDate),
                labeled("To", dashboardToDate));

        dashboardLastResultsTable = mapTable(dashboardLastResultRows, "Test Suite", "suite", "Test Case", "case",
                "Test Step", "step", "Type", "type", "Status", "status", "Details", "message");
        dashboardLastResultsTable.setMinHeight(220);

        dashboardTable = mapTable(dashboardRows, "Executed", "executed", "Execution", "name", "Type", "type",
                "Test Cases", "testCases", "Passed", "passed", "Failed", "failed", "Health", "health");
        dashboardTable.setRowFactory(table -> {
            javafx.scene.control.TableRow<Map<String, String>> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) showDashboardExecutionDetails(row.getItem());
            });
            return row;
        });
        dashboardTable.setMinHeight(280);
        VBox overview = new VBox(14, metrics, charts,
                card("Last Execution Results", dashboardLastResultsTable),
                card("Executions So Far", new VBox(10, filters, dashboardTable)));
        VBox.setVgrow(overview.getChildren().get(3), Priority.ALWAYS);

        Label weaving = new Label("Weaving");
        weaving.getStyleClass().add("weaving-title");
        BorderPane jira = new BorderPane(weaving);
        jira.setPadding(new Insets(80));
        TabPane dashboardTabs = new TabPane(tab("Overview", padded(new ScrollPane(overview))), tab("JIRA", jira));
        dashboardTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox dashboard = new VBox(12, actionRow(dashboardRefreshButton, dashboardSyncLabel), dashboardTabs);
        VBox.setVgrow(dashboardTabs, Priority.ALWAYS);
        return padded(dashboard);
    }

    private VBox dashboardMetricCard(String title, Label value) {
        Label heading = new Label(title);
        heading.getStyleClass().add("dashboard-metric-title");
        VBox box = new VBox(8, heading, value);
        box.getStyleClass().addAll("card", "dashboard-metric-card");
        box.setMinWidth(170);
        return box;
    }

    private BarChart<String, Number> dashboardBarChart(String title, String xLabel, String yLabel) {
        CategoryAxis x = new CategoryAxis();
        x.setLabel(xLabel);
        NumberAxis y = new NumberAxis();
        y.setLabel(yLabel);
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle(title);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        return chart;
    }

    private void refreshDashboard() {
        if (dashboardSyncLabel == null) return;
        if (dashboardRefreshButton != null) dashboardRefreshButton.setDisable(true);
        dashboardSyncLabel.setText("Refreshing local and Firebase execution history...");
        Path reportsRoot = configuredBaseReportsDirectory();
        Task<JSONArray> task = new Task<>() {
            @Override protected JSONArray call() throws Exception {
                StorageMode storageMode = selectedDashboardStorageMode();
                Path sqliteDbPath = dashboardExecutionSqlitePath(storageMode);
                dashboardExecutionService.importExistingReports(reportsRoot, storageMode, sqliteDbPath);
                return dashboardExecutionService.load(reportsRoot, storageMode, sqliteDbPath);
            }
        };
        task.setOnSucceeded(e -> {
            dashboardExecutions = task.getValue();
            dashboardSyncLabel.setText("Loaded " + dashboardExecutions.length()
                    + " execution(s) from local reports and Google Firebase.");
            if (dashboardRefreshButton != null) dashboardRefreshButton.setDisable(false);
            updateDashboardDateControls();
            applyDashboardFilter();
        });
        task.setOnFailed(e -> {
            dashboardSyncLabel.setText("Dashboard refresh failed: " + exceptionMessage(task.getException()));
            if (dashboardRefreshButton != null) dashboardRefreshButton.setDisable(false);
            applyDashboardFilter();
        });
        start(task);
    }

    private void applyDashboardFilter() {
        if (dashboardTable == null) return;
        LocalDate[] range = dashboardFilterRange();
        LocalDate from = range[0];
        LocalDate to = range[1];
        if (from.isAfter(to)) {
            LocalDate swap = from;
            from = to;
            to = swap;
        }
        if (!"Custom dates".equals(dashboardPeriodBox == null ? "" : dashboardPeriodBox.getValue())) {
            dashboardFromDate.setValue(from);
            dashboardToDate.setValue(to);
        }
        if (dashboardSyncLabel != null) {
            dashboardSyncLabel.setText("Showing executions from " + from + " to " + to
                    + " (" + dashboardExecutions.length() + " loaded).");
        }
        long start = from.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = to.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
        List<JSONObject> filtered = new ArrayList<>();
        for (JSONObject execution : sortedDashboardExecutions()) {
            long executedAt = execution.optLong("executedAt");
            if (executedAt >= start && executedAt <= end) {
                filtered.add(execution);
            }
        }
        renderDashboard(filtered);
    }

    private void updateDashboardDateControls() {
        if (dashboardPeriodBox == null || dashboardFromDate == null || dashboardToDate == null) return;
        boolean custom = "Custom dates".equals(dashboardPeriodBox.getValue());
        dashboardFromDate.setDisable(!custom);
        dashboardToDate.setDisable(!custom);
        dashboardFromDate.setManaged(true);
        dashboardToDate.setManaged(true);
        if (!custom) {
            LocalDate[] range = dashboardFilterRange();
            dashboardFromDate.setValue(range[0]);
            dashboardToDate.setValue(range[1]);
        }
    }

    private LocalDate[] dashboardFilterRange() {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(2);
        String period = dashboardPeriodBox == null ? "Past 3 days" : dashboardPeriodBox.getValue();
        if ("Past 5 days".equals(period)) from = to.minusDays(4);
        if ("1 month".equals(period)) from = to.minusMonths(1);
        if ("3 months".equals(period)) from = to.minusMonths(3);
        if ("Custom dates".equals(period)) {
            from = dashboardFromDate.getValue() == null ? LocalDate.MIN : dashboardFromDate.getValue();
            to = dashboardToDate.getValue() == null ? LocalDate.now() : dashboardToDate.getValue();
        }
        return new LocalDate[] {from, to};
    }

    private List<JSONObject> sortedDashboardExecutions() {
        List<JSONObject> executions = new ArrayList<>();
        for (int i = 0; i < dashboardExecutions.length(); i++) {
            JSONObject execution = dashboardExecutions.optJSONObject(i);
            if (execution != null) executions.add(execution);
        }
        executions.sort((left, right) -> Long.compare(right.optLong("executedAt"), left.optLong("executedAt")));
        return executions;
    }

    private void renderDashboard(List<JSONObject> executions) {
        dashboardRows.clear();
        dashboardLastResultRows.clear();
        long passed = 0, failed = 0;
        double avgMs = 0, p90Ms = 0, p95Ms = 0, throughput = 0;
        int performanceRuns = 0;
        JSONObject latestSuite = null;
        JSONObject latestExecution = executions.isEmpty() ? null : executions.get(0);
        Map<LocalDate, Integer> daily = new LinkedHashMap<>();
        for (JSONObject execution : executions) {
            passed += execution.optLong("passed");
            failed += execution.optLong("failed");
            if ("PERFORMANCE".equals(execution.optString("type"))) {
                JSONObject performance = execution.optJSONObject("performance");
                if (performance != null) {
                    avgMs += performance.optDouble("averageMs");
                    p90Ms += performance.optDouble("p90Ms");
                    p95Ms += performance.optDouble("p95Ms");
                    throughput += performance.optDouble("throughputPerSecond");
                    performanceRuns++;
                }
            } else if (latestSuite == null) latestSuite = execution;
            LocalDate day = Instant.ofEpochMilli(execution.optLong("executedAt"))
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            daily.merge(day, 1, Integer::sum);
            dashboardRows.add(row("executed", Instant.ofEpochMilli(execution.optLong("executedAt")).toString(),
                    "name", execution.optString("name", "Execution"), "type", execution.optString("type"),
                    "testCases", String.valueOf(execution.optInt("totalTestCases")),
                    "passed", String.valueOf(execution.optLong("passed")), "failed", String.valueOf(execution.optLong("failed")),
                    "health", execution.optString("health"), "json", execution.toString()));
        }
        renderLastExecutionResults(latestExecution);
        long total = passed + failed;
        double passRate = total == 0 ? 0 : passed * 100.0 / total;
        dashboardLastRunCasesLabel.setText(latestSuite == null ? "0" : String.valueOf(latestSuite.optInt("totalTestCases")));
        dashboardExecutionsLabel.setText(String.valueOf(executions.size()));
        dashboardPassRateLabel.setText(String.format("%.1f%%", passRate));
        dashboardPerformanceLabel.setText(performanceRuns == 0 ? "No load runs"
                : String.format("%.0f ms · %.1f/s", avgMs / performanceRuns, throughput / performanceRuns));
        dashboardHealthLabel.setText(total == 0 ? "No data" : passRate >= 95 ? "Excellent" : passRate >= 80 ? "Watch" : "At risk");
        dashboardStatusChart.setData(FXCollections.observableArrayList(new PieChart.Data("Passed", passed), new PieChart.Data("Failed", failed)));
        dashboardPerformanceChart.getData().clear();
        XYChart.Series<String, Number> performanceSeries = new XYChart.Series<>();
        performanceSeries.getData().add(new XYChart.Data<>("Avg ms", performanceRuns == 0 ? 0 : avgMs / performanceRuns));
        performanceSeries.getData().add(new XYChart.Data<>("P90 ms", performanceRuns == 0 ? 0 : p90Ms / performanceRuns));
        performanceSeries.getData().add(new XYChart.Data<>("P95 ms", performanceRuns == 0 ? 0 : p95Ms / performanceRuns));
        performanceSeries.getData().add(new XYChart.Data<>("Throughput/s", performanceRuns == 0 ? 0 : throughput / performanceRuns));
        dashboardPerformanceChart.getData().add(performanceSeries);
        dashboardHistoryChart.getData().clear();
        XYChart.Series<String, Number> history = new XYChart.Series<>();
        daily.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> history.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue())));
        dashboardHistoryChart.getData().add(history);
    }

    private void renderLastExecutionResults(JSONObject execution) {
        if (execution == null) return;
        JSONArray details = execution.optJSONArray("details");
        if (details == null || details.isEmpty()) {
            dashboardLastResultRows.add(row("suite", execution.optString("name", "Execution"), "case", "",
                    "step", "", "type", execution.optString("type"), "status", execution.optString("health"),
                    "message", "No detailed results were stored for the latest execution."));
            return;
        }
        for (int i = 0; i < details.length(); i++) {
            JSONObject detail = details.optJSONObject(i);
            if (detail != null) dashboardLastResultRows.add(row("suite", detail.optString("suite"),
                    "case", detail.optString("testCase"), "step", detail.optString("testStep"),
                    "type", detail.optString("type"), "status", detail.optString("status"),
                    "message", detail.optString("message")));
        }
    }

    private void showDashboardExecutionDetails(Map<String, String> selected) {
        if (selected == null || selected.getOrDefault("json", "").isBlank()) return;
        JSONObject execution = new JSONObject(selected.get("json"));
        ObservableList<Map<String, String>> rows = FXCollections.observableArrayList();
        JSONArray details = execution.optJSONArray("details");
        if (details != null) for (int i = 0; i < details.length(); i++) {
            JSONObject detail = details.optJSONObject(i);
            if (detail != null) rows.add(row("suite", detail.optString("suite"), "case", detail.optString("testCase"),
                    "step", detail.optString("testStep"), "type", detail.optString("type"),
                    "status", detail.optString("status"), "message", detail.optString("message")));
        }
        TableView<Map<String, String>> table = mapTable(rows, "Test Suite", "suite", "Test Case", "case",
                "Test Step", "step", "Type", "type", "Status", "status", "Details", "message");
        Stage detailStage = new Stage();
        detailStage.setTitle("Execution Details - " + execution.optString("name"));
        VBox content = new VBox(12, sectionTitle(execution.optString("name", "Execution Details")), table);
        content.setPadding(new Insets(16));
        VBox.setVgrow(table, Priority.ALWAYS);
        Scene scene = new Scene(content, 1100, 620);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private javafx.scene.Node createApiPanel() {
        apiTesterTabs = new TabPane();
        apiTesterTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        apiTesterTabs.getTabs().add(tab("API Executor", createApiExecutorPanel()));
        apiTesterTabs.getTabs().add(tab("Import Collection", createPostmanCollectionPanel()));
        VBox panel = new VBox(apiTesterTabs);
        panel.setPadding(new Insets(14, 0, 0, 0));
        VBox.setVgrow(apiTesterTabs, Priority.ALWAYS);
        return panel;
    }

    private javafx.scene.Node createApiExecutorPanel() {
        endpointField = new TextField();
        endpointField.setText("https://jsonplaceholder.typicode.com/posts/1");
        methodBox = combo("GET", "POST", "PUT", "PATCH", "DELETE");
        apiUrlVariableBox = createVariableDropdown();
        authTypeBox = combo("No Auth", "Bearer Token", "OAuth2");
        requestFormatBox = combo("JSON");
        tokenField = new PasswordField();
        tokenField.setDisable(true);
        visibleTokenField = new TextField();
        visibleTokenField.textProperty().bindBidirectional(tokenField.textProperty());
        visibleTokenField.setDisable(true);
        visibleTokenField.setManaged(false);
        visibleTokenField.setVisible(false);
        oauthGrantTypeBox = combo("client_credentials", "password", "authorization_code", "refresh_token");
        oauthTokenUrlField = new TextField();
        oauthClientIdField = new TextField();
        oauthClientSecretField = new PasswordField();
        oauthScopeField = new TextField();
        oauthUsernameField = new TextField();
        oauthPasswordField = new PasswordField();
        oauthAuthCodeField = new TextField();
        oauthRedirectUriField = new TextField();
        oauthRefreshTokenField = new TextField();
        oauthBasicAuthCheck = new CheckBox("Use Basic client auth");
        oauthBasicAuthCheck.setSelected(true);
        oauthStatusLabel = new Label("OAuth2 ready");
        oauthStatusLabel.getStyleClass().add("muted");
        sslVerificationDisabledCheck = new CheckBox("Disable SSL certificate verification");
        trustStorePathField = new TextField();
        trustStorePasswordField = new PasswordField();
        keyStorePathField = new TextField();
        keyStorePasswordField = new PasswordField();
        proxyEnabledCheck = new CheckBox("Use proxy");
        proxySchemeBox = combo("http", "https");
        proxyHostField = new TextField();
        proxyPortField = new TextField();
        proxyUsernameField = new TextField();
        proxyPasswordField = new PasswordField();
        authTypeBox.setOnAction(e -> updateAuthControls());
        methodBox.setOnAction(e -> updateRequestBodyState());
        headersArea = editor("Accept: application/json\nContent-Type: application/json\nUser-Agent: API-Validator-Tool/1.0");
        bodyArea = requestEditor("");
        preRequestScriptArea = requestEditor("");
        testScriptArea = requestEditor("");
        prettyResponseArea = responseEditor("");
        rawResponseArea = responseEditor("");
        responseHeadersArea = responseEditor("");
        responseCookiesArea = responseEditor("");
        statusValueLabel = metric("--");
        timeValueLabel = metric("--");
        sizeValueLabel = metric("--");
        apiStatusLabel = new Label("Ready");
        apiStatusLabel.getStyleClass().add("muted");
        apiAiTesterConnectionLabel = new Label();
        apiAiTesterConnectionLabel.getStyleClass().add("muted");
        updateApiAiConnectionLabels();

        ComboBox<String> apiVariableBox = createVariableDropdown();
        Button insertVariable = secondary("Insert Variable");
        insertVariable.setOnAction(e -> insertVariable(bodyArea, apiVariableBox));
        Button insertUrlVariable = secondary("Insert");
        insertUrlVariable.setOnAction(e -> insertVariable(endpointField, apiUrlVariableBox));
        Button beautify = secondary("Beautify");
        beautify.setOnAction(e -> beautifyBody());
        Button send = primary("Send Request");
        send.setOnAction(e -> sendRequest());
        Button aiAnalysis = secondary("AI Analysis");
        aiAnalysis.setOnAction(e -> runApiAiAnalysisForLastResponse());
        Button clear = secondary("Clear");
        clear.setOnAction(e -> clearApiForm());
        Button saveRequest = secondary("Save Request");
        saveRequest.setOnAction(e -> saveRequest());
        Button saveResponse = secondary("Save Response");
        saveResponse.setOnAction(e -> saveResponse());
        Button toggleToken = secondary("Show");
        toggleToken.setOnAction(e -> toggleTokenVisibility(toggleToken));
        toggleToken.setMinWidth(88);
        toggleToken.setPrefWidth(88);
        Button copyResponse = secondary("Copy");
        copyResponse.setOnAction(e -> copySelectedResponse());

        GridPane form = grid();
        form.add(labeled("Method", methodBox), 0, 0);
        form.add(labeled("Endpoint", endpointField), 1, 0, 5, 1);
        form.add(labeled("Variables", apiUrlVariableBox), 1, 1);
        form.add(labeled(" ", insertUrlVariable), 2, 1);
        form.add(labeled("Auth Type", authTypeBox), 3, 1);
        form.add(labeled("Token", wrapTokenField(toggleToken)), 4, 1, 2, 1);
        methodBox.setMinWidth(132);
        endpointField.setMinWidth(720);
        apiUrlVariableBox.setMinWidth(210);
        authTypeBox.setMinWidth(190);
        tokenField.setMinWidth(280);
        visibleTokenField.setMinWidth(280);
        GridPane.setHgrow(endpointField, Priority.ALWAYS);

        FlowPane bodyTools = actionRow(new Label("Format:"), requestFormatBox, apiVariableBox, insertVariable, beautify);

        TabPane requestSectionTabs = new TabPane();
        requestSectionTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        requestSectionTabs.getTabs().add(tab("Headers", headersArea));
        requestSectionTabs.getTabs().add(tab("Body", withFooter(bodyArea, bodyTools)));
        requestSectionTabs.getTabs().add(tab("OAuth2", createOAuth2Panel()));
        requestSectionTabs.getTabs().add(tab("Settings", createApiTransportSettingsPanel()));
        requestSectionTabs.getTabs().add(tab("Pre-request Script", preRequestScriptArea));
        requestSectionTabs.getTabs().add(tab("Tests", testScriptArea));
        requestSectionTabs.setMinHeight(360);
        VBox requestSectionsCard = card("Request Sections", requestSectionTabs);
        requestSectionsCard.setMinWidth(0);
        headersArea.setPrefHeight(230);
        bodyArea.setPrefHeight(230);
        preRequestScriptArea.setPrefHeight(230);
        testScriptArea.setPrefHeight(230);
        requestSectionsCard.setMinHeight(430);
        VBox.setVgrow(requestSectionTabs, Priority.ALWAYS);

        FlowPane actions = actionRow(send, aiAnalysis, clear, saveResponse, saveRequest, apiStatusLabel, apiAiTesterConnectionLabel);

        apiResponseTabs = new TabPane();
        apiResponseTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        apiResponseTabs.setMinHeight(360);
        apiResponseTabs.getTabs().add(tab("Pretty", prettyResponseArea));
        apiResponseTabs.getTabs().add(tab("Raw", rawResponseArea));
        apiResponseTabs.getTabs().add(tab("Headers", responseHeadersArea));
        apiResponseTabs.getTabs().add(tab("Cookies", responseCookiesArea));
        apiResponseTabs.getTabs().add(tab("Capture Variables", createResponseVariableCapturePanel()));

        FlowPane metrics = actionRow(
                new Label("Status:"), statusValueLabel,
                new Label("Time:"), timeValueLabel,
                new Label("Size:"), sizeValueLabel,
                copyResponse);

        VBox request = new VBox(16, form, actions);
        VBox response = new VBox(16, metrics, apiResponseTabs);
        VBox.setVgrow(apiResponseTabs, Priority.ALWAYS);
        request.setMinHeight(Region.USE_PREF_SIZE);
        response.setMinHeight(Region.USE_PREF_SIZE);
        apiResponseTabs.setPrefHeight(480);

        VBox requestCard = card("API Request Card", request);
        requestCard.setMinHeight(245);
        VBox responseCard = card("Response Card", response);
        responseCard.setMinHeight(620);
        VBox panel = new VBox(24, requestCard, requestSectionsCard, responseCard);
        panel.setMinWidth(0);
        panel.setFillWidth(true);
        updateRequestBodyState();
        ScrollPane scrollPane = padded(panel);
        scrollPane.setFitToHeight(false);
        return scrollPane;
    }

    private javafx.scene.Node createOAuth2Panel() {
        Button fetchToken = primary("Fetch Token");
        fetchToken.setOnAction(e -> fetchOAuth2Token());
        Button useAccessTokenVariable = secondary("Use ${access_token}");
        useAccessTokenVariable.setOnAction(e -> {
            authTypeBox.setValue("Bearer Token");
            tokenField.setText("${access_token}");
            updateAuthControls();
        });

        GridPane grid = grid();
        grid.add(labeled("Grant Type", oauthGrantTypeBox), 0, 0);
        grid.add(labeled("Token URL", oauthTokenUrlField), 1, 0, 2, 1);
        grid.add(labeled("Client ID", oauthClientIdField), 0, 1);
        grid.add(labeled("Client Secret", oauthClientSecretField), 1, 1);
        grid.add(labeled("Scope", oauthScopeField), 2, 1);
        grid.add(labeled("Username", oauthUsernameField), 0, 2);
        grid.add(labeled("Password", oauthPasswordField), 1, 2);
        grid.add(labeled("Authorization Code", oauthAuthCodeField), 0, 3);
        grid.add(labeled("Redirect URI", oauthRedirectUriField), 1, 3);
        grid.add(labeled("Refresh Token", oauthRefreshTokenField), 2, 3);
        grid.add(labeled("Client Auth", oauthBasicAuthCheck), 0, 4);
        grid.add(actionRow(fetchToken, useAccessTokenVariable, oauthStatusLabel), 1, 4, 2, 1);
        oauthTokenUrlField.setMinWidth(420);
        GridPane.setHgrow(oauthTokenUrlField, Priority.ALWAYS);
        return new VBox(16, grid);
    }

    private javafx.scene.Node createApiTransportSettingsPanel() {
        Button browseTrustStore = secondary("Browse");
        browseTrustStore.setOnAction(e -> chooseIntoField(trustStorePathField, "Trust Store", "*.*"));
        Button browseKeyStore = secondary("Browse");
        browseKeyStore.setOnAction(e -> chooseIntoField(keyStorePathField, "Key Store", "*.*"));

        GridPane sslGrid = grid();
        sslGrid.add(labeled("Verification", sslVerificationDisabledCheck), 0, 0);
        sslGrid.add(labeled("Trust Store", wrapTextFieldWithActions(trustStorePathField, browseTrustStore)), 0, 1, 2, 1);
        sslGrid.add(labeled("Trust Store Password", trustStorePasswordField), 2, 1);
        sslGrid.add(labeled("Client Certificate / Key Store", wrapTextFieldWithActions(keyStorePathField, browseKeyStore)), 0, 2, 2, 1);
        sslGrid.add(labeled("Key Store Password", keyStorePasswordField), 2, 2);

        GridPane proxyGrid = grid();
        proxyGrid.add(labeled("Proxy", proxyEnabledCheck), 0, 0);
        proxyGrid.add(labeled("Scheme", proxySchemeBox), 1, 0);
        proxyGrid.add(labeled("Host", proxyHostField), 0, 1);
        proxyGrid.add(labeled("Port", proxyPortField), 1, 1);
        proxyGrid.add(labeled("Username", proxyUsernameField), 0, 2);
        proxyGrid.add(labeled("Password", proxyPasswordField), 1, 2);
        proxyHostField.setMinWidth(280);
        trustStorePathField.setMinWidth(420);
        keyStorePathField.setMinWidth(420);

        return new VBox(18, sectionTitle("SSL / Certificates"), sslGrid, sectionTitle("Proxy"), proxyGrid);
    }

    private void chooseIntoField(TextField field, String description, String extension) {
        File file = chooseOpenFile(description, extension);
        if (file != null) {
            field.setText(file.toPath().toAbsolutePath().normalize().toString());
        }
    }

    private javafx.scene.Node createPostmanCollectionPanel() {
        postmanCollectionPathField = new TextField();
        postmanCollectionPathField.setEditable(false);
        postmanCollectionPathField.setPrefColumnCount(90);
        postmanEnvironmentPathField = new TextField();
        postmanEnvironmentPathField.setEditable(false);
        postmanEnvironmentPathField.setPrefColumnCount(90);
        postmanCollectionStatusLabel = new Label("No Postman collection imported.");
        postmanCollectionStatusLabel.getStyleClass().add("muted");
        postmanCollectionTree = new TreeView<>();
        postmanCollectionTree.setShowRoot(true);
        postmanCollectionTree.setRoot(new TreeItem<>(PostmanCollectionNode.placeholder("Import a Postman collection JSON file")));
        postmanCollectionDetailsArea = editor("");
        postmanCollectionDetailsArea.setEditable(false);
        postmanCollectionDetailsArea.setWrapText(true);
        postmanCollectionDetailsArea.setPrefHeight(220);

        Button browse = secondary("Import Collection");
        browse.setOnAction(e -> importPostmanCollection());
        Button importEnvironment = secondary("Import Environment");
        importEnvironment.setOnAction(e -> importPostmanEnvironment());
        Button load = primary("Load Selected Request");
        load.setOnAction(e -> loadSelectedPostmanRequest());
        Button runSelected = primary("Run Selected");
        runSelected.setOnAction(e -> runSelectedPostmanCollectionNode());
        FlowPane tools = actionRow(browse, importEnvironment, load, runSelected, postmanCollectionStatusLabel);

        postmanCollectionTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            PostmanCollectionNode node = newValue == null ? null : newValue.getValue();
            renderPostmanCollectionDetails(node);
            if (node != null && node.isRequest()) {
                loadPostmanRequest(node);
            }
        });
        postmanCollectionTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                loadSelectedPostmanRequest();
            }
        });

        SplitPane split = new SplitPane(postmanCollectionTree, postmanCollectionDetailsArea);
        split.setDividerPositions(0.42);
        VBox panel = new VBox(14,
                labeled("Collection Path", wrapTextFieldWithActions(postmanCollectionPathField, browse)),
                labeled("Environment Path", wrapTextFieldWithActions(postmanEnvironmentPathField, importEnvironment)),
                tools,
                split);
        VBox.setVgrow(split, Priority.ALWAYS);
        postmanCollectionTree.setMinHeight(520);
        postmanCollectionDetailsArea.setMinHeight(520);
        return padded(panel);
    }

    private void importPostmanCollection() {
        File file = chooseOpenFile("Postman Collection", "*.json");
        if (file == null) {
            return;
        }
        try {
            JSONObject collection = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            postmanCollectionPathField.setText(file.getAbsolutePath());
            postmanCollectionVariables.clear();
            TreeItem<PostmanCollectionNode> root = buildPostmanCollectionTree(collection);
            postmanCollectionTree.setRoot(root);
            root.setExpanded(true);
            int variables = importPostmanVariables(collection.optJSONArray("variable"), postmanCollectionVariables, "Postman Collection")
                    + importPostmanItemVariables(collection.optJSONArray("item"));
            postmanCollectionStatusLabel.setText("Imported " + root.getValue().name + " with "
                    + countPostmanRequests(root) + " request(s), " + variables + " variable(s).");
            refreshVariablesView();
        } catch (Exception e) {
            showError("Import Postman Collection Failed", e);
        }
    }

    private void importPostmanEnvironment() {
        File file = chooseOpenFile("Postman Environment", "*.json");
        if (file == null) {
            return;
        }
        try {
            JSONObject environment = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            postmanEnvironmentPathField.setText(file.getAbsolutePath());
            postmanEnvironmentVariables.clear();
            int variables = importPostmanVariables(environment.optJSONArray("values"), postmanEnvironmentVariables, "Postman Environment");
            postmanCollectionStatusLabel.setText("Imported environment "
                    + firstNonBlank(environment.optString("name"), file.getName()) + " with " + variables + " variable(s).");
            refreshVariablesView();
        } catch (Exception e) {
            showError("Import Postman Environment Failed", e);
        }
    }

    private TreeItem<PostmanCollectionNode> buildPostmanCollectionTree(JSONObject collection) {
        JSONObject info = collection.optJSONObject("info");
        String name = info == null ? "Postman Collection" : firstNonBlank(info.optString("name"), "Postman Collection");
        TreeItem<PostmanCollectionNode> root = new TreeItem<>(PostmanCollectionNode.collection(name, collection));
        JSONArray items = collection.optJSONArray("item");
        if (items != null) {
            appendPostmanItems(root, items, collection.optJSONObject("auth"));
        }
        return root;
    }

    private void appendPostmanItems(TreeItem<PostmanCollectionNode> parent, JSONArray items, JSONObject inheritedAuth) {
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);
            if (item == null) {
                continue;
            }
            String name = firstNonBlank(item.optString("name"), "Untitled");
            JSONObject effectiveAuth = item.has("auth") ? item.optJSONObject("auth") : inheritedAuth;
            if (item.has("request")) {
                JSONObject request = item.optJSONObject("request");
                request = request == null ? new JSONObject() : new JSONObject(request.toString());
                if (!request.has("auth") && effectiveAuth != null) {
                    request.put("auth", effectiveAuth);
                }
                parent.getChildren().add(new TreeItem<>(PostmanCollectionNode.request(name, request == null ? new JSONObject() : request, item)));
            } else {
                TreeItem<PostmanCollectionNode> folder = new TreeItem<>(PostmanCollectionNode.folder(name, item));
                JSONArray children = item.optJSONArray("item");
                if (children != null) {
                    appendPostmanItems(folder, children, effectiveAuth);
                }
                folder.setExpanded(false);
                parent.getChildren().add(folder);
            }
        }
    }

    private int importPostmanVariables(JSONArray variables, Map<String, String> targetScope, String type) {
        if (variables == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < variables.length(); i++) {
            JSONObject variable = variables.optJSONObject(i);
            if (variable == null || variable.optBoolean("disabled", false)
                    || (variable.has("enabled") && !variable.optBoolean("enabled", true))) {
                continue;
            }
            String key = normalizeVariableName(variable.optString("key"));
            if (key.isBlank()) {
                continue;
            }
            String value = postmanVariableRawValue(variable);
            targetScope.put(key, value);
            savedVariables.putIfAbsent(key, value);
            savedVariableTypes.putIfAbsent(key, type);
            savedVariablePaths.putIfAbsent(key, "Postman Environment".equals(type)
                    ? (postmanEnvironmentPathField == null ? "" : postmanEnvironmentPathField.getText())
                    : (postmanCollectionPathField == null ? "" : postmanCollectionPathField.getText()));
            count++;
        }
        return count;
    }

    private String postmanVariableRawValue(JSONObject variable) {
        if (variable == null) {
            return "";
        }
        if (variable.has("currentValue")) {
            return String.valueOf(variable.opt("currentValue") == null ? "" : variable.opt("currentValue"));
        }
        if (variable.has("value")) {
            return String.valueOf(variable.opt("value") == null ? "" : variable.opt("value"));
        }
        if (variable.has("initialValue")) {
            return String.valueOf(variable.opt("initialValue") == null ? "" : variable.opt("initialValue"));
        }
        return "";
    }

    private int importPostmanItemVariables(JSONArray items) {
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);
            if (item == null) {
                continue;
            }
            count += importPostmanVariables(item.optJSONArray("variable"), postmanCollectionVariables, "Postman Collection");
            count += importPostmanItemVariables(item.optJSONArray("item"));
            JSONObject request = item.optJSONObject("request");
            JSONObject url = request == null ? null : request.optJSONObject("url");
            if (url != null) {
                count += importPostmanVariables(url.optJSONArray("variable"), postmanCollectionVariables, "Postman Collection");
            }
        }
        return count;
    }

    private int countPostmanRequests(TreeItem<PostmanCollectionNode> item) {
        if (item == null) {
            return 0;
        }
        int count = item.getValue() != null && item.getValue().isRequest() ? 1 : 0;
        for (TreeItem<PostmanCollectionNode> child : item.getChildren()) {
            count += countPostmanRequests(child);
        }
        return count;
    }

    private void runSelectedPostmanCollectionNode() {
        TreeItem<PostmanCollectionNode> selected = postmanCollectionTree == null
                ? null : postmanCollectionTree.getSelectionModel().getSelectedItem();
        if (selected == null || selected.getValue() == null || "placeholder".equals(selected.getValue().kind)) {
            showWarning("Postman Collection", "Import a collection and select a request, folder, or collection to run.");
            return;
        }
        List<TreeItem<PostmanCollectionNode>> requestItems = new ArrayList<>();
        collectPostmanRequestItems(selected, requestItems);
        if (requestItems.isEmpty()) {
            showWarning("Postman Collection", "The selected item does not contain any runnable requests.");
            return;
        }
        AtomicInteger completed = new AtomicInteger();
        postmanCollectionStatusLabel.setText("Running " + requestItems.size() + " request(s) from " + selected.getValue().name + "...");
        Task<PostmanCollectionRunResult> runner = new Task<>() {
            @Override
            protected PostmanCollectionRunResult call() {
                PostmanCollectionRunResult result = new PostmanCollectionRunResult(selected.getValue().name, requestItems.size());
                for (TreeItem<PostmanCollectionNode> item : requestItems) {
                    if (isCancelled()) {
                        break;
                    }
                    PostmanCollectionNode node = item.getValue();
                    try {
                        runPostmanPreRequestScripts(item);
                        ApiRequest request = buildPostmanApiRequest(node);
                        List<String> unresolvedVariables = unresolvedRequestVariables(request);
                        if (!unresolvedVariables.isEmpty()) {
                            throw new IllegalStateException("Unresolved variables: " + String.join(", ", unresolvedVariables));
                        }
                        boolean bearerAuth = request.token != null && !request.token.isBlank();
                        hydratePostmanBearerTokenIfNeeded(request, bearerAuth, request.token);
                        ApiResponse response = apiService.sendRequest(request);
                        lastResponse = response;
                        captureAccessTokenFromResponse(response);
                        Platform.runLater(() -> renderResponse(response));
                        boolean passed = response.statusCode >= 200 && response.statusCode < 400;
                        String scriptMessage = "";
                        try {
                            capturePostmanTestVariables(item, response);
                        } catch (Exception scriptError) {
                            passed = false;
                            scriptMessage = "Postman test script failed: " + exceptionMessage(scriptError);
                        }
                        result.add(node.name, response.statusCode, passed ? "PASS" : "FAIL",
                                passed ? "" : firstNonBlank(scriptMessage, response.statusLine));
                    } catch (Exception ex) {
                        result.add(node == null ? "Request" : node.name, 0, "ERROR", exceptionMessage(ex));
                    }
                    int done = completed.incrementAndGet();
                    Platform.runLater(() -> postmanCollectionStatusLabel.setText("Ran " + done + " of "
                            + requestItems.size() + " request(s) from " + selected.getValue().name + "..."));
                }
                return result;
            }
        };
        runner.setOnSucceeded(e -> {
            PostmanCollectionRunResult result = runner.getValue();
            postmanCollectionStatusLabel.setText(result.summary());
            postmanCollectionDetailsArea.setText(result.details());
            refreshVariablesView();
            showInfo("Postman Collection Run", result.summary());
        });
        runner.setOnFailed(e -> {
            postmanCollectionStatusLabel.setText("Collection run failed");
            showError("Postman Collection Run Failed", runner.getException());
        });
        start(runner);
    }

    private void collectPostmanRequestItems(TreeItem<PostmanCollectionNode> item, List<TreeItem<PostmanCollectionNode>> requests) {
        if (item == null || item.getValue() == null) {
            return;
        }
        if (item.getValue().isRequest()) {
            requests.add(item);
            return;
        }
        for (TreeItem<PostmanCollectionNode> child : item.getChildren()) {
            collectPostmanRequestItems(child, requests);
        }
    }

    private void runPostmanPreRequestScripts(TreeItem<PostmanCollectionNode> requestItem) {
        for (PostmanCollectionNode node : postmanExecutionPath(requestItem)) {
            executeBasicPostmanPreRequestVariables(node);
        }
    }

    private void capturePostmanTestVariables(TreeItem<PostmanCollectionNode> requestItem, ApiResponse response) {
        for (PostmanCollectionNode node : postmanExecutionPath(requestItem)) {
            capturePostmanTestVariables(node, response);
        }
    }

    private List<PostmanCollectionNode> postmanExecutionPath(TreeItem<PostmanCollectionNode> requestItem) {
        List<PostmanCollectionNode> path = new ArrayList<>();
        TreeItem<PostmanCollectionNode> current = requestItem;
        while (current != null) {
            PostmanCollectionNode node = current.getValue();
            if (node != null && !"placeholder".equals(node.kind)) {
                path.add(node);
            }
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    private void loadSelectedPostmanRequest() {
        TreeItem<PostmanCollectionNode> selected = postmanCollectionTree == null
                ? null : postmanCollectionTree.getSelectionModel().getSelectedItem();
        PostmanCollectionNode node = selected == null ? null : selected.getValue();
        if (node == null || !node.isRequest()) {
            showWarning("Postman Collection", "Select a request from the imported collection.");
            return;
        }
        loadPostmanRequest(node);
    }

    private void loadPostmanRequest(PostmanCollectionNode node) {
        currentPostmanRequestNode = node;
        JSONObject request = node.request;
        String method = firstNonBlank(request.optString("method"), "GET").toUpperCase();
        if (methodBox.getItems().contains(method)) {
            methodBox.setValue(method);
        }
        endpointField.setText(postmanRequestUrl(node));
        headersArea.setText(postmanHeadersText(request));
        JSONObject postmanBody = request.optJSONObject("body");
        currentPostmanBodyMode = postmanBody == null ? "" : postmanBody.optString("mode");
        currentPostmanMultipartParts = postmanMultipartParts(postmanBody);
        currentPostmanBinaryFilePath = postmanBinaryFilePath(postmanBody);
        String body = postmanBodyText(postmanBody);
        bodyArea.setText(body);
        preRequestScriptArea.setText(postmanEventScript(node.source, "prerequest"));
        testScriptArea.setText(postmanEventScript(node.source, "test"));
        applyPostmanAuth(request);
        updateRequestBodyState();
        apiStatusLabel.setText("Loaded collection request: " + node.name);
        if (apiTesterTabs != null && !apiTesterTabs.getTabs().isEmpty()) {
            apiTesterTabs.getSelectionModel().select(0);
        }
    }

    private String postmanEventScript(JSONObject source, String listen) {
        JSONArray events = source == null ? null : source.optJSONArray("event");
        if (events == null || events.isEmpty()) {
            return "";
        }
        List<String> scripts = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.optJSONObject(i);
            if (event != null && listen.equalsIgnoreCase(event.optString("listen"))) {
                String script = postmanScriptText(event.optJSONObject("script"));
                if (!script.isBlank()) {
                    scripts.add(script);
                }
            }
        }
        return String.join(System.lineSeparator() + System.lineSeparator(), scripts);
    }

    private void renderPostmanCollectionDetails(PostmanCollectionNode node) {
        if (postmanCollectionDetailsArea == null) {
            return;
        }
        if (node == null) {
            postmanCollectionDetailsArea.clear();
            return;
        }
        if (!node.isRequest()) {
            postmanCollectionDetailsArea.setText(node.name + "\n\n" + firstNonBlank(node.source.optString("description"), "Folder"));
            return;
        }
        JSONObject request = node.request;
        postmanCollectionDetailsArea.setText("Request: " + node.name
                + "\nMethod: " + firstNonBlank(request.optString("method"), "GET")
                + "\nURL: " + postmanRequestUrl(node)
                + "\nAuth: " + postmanAuthType(request.optJSONObject("auth"))
                + "\n\nHeaders:\n" + postmanHeadersText(request)
                + "\n\nBody:\n" + postmanBodyText(request.optJSONObject("body"))
                + "\n\nDescription:\n" + firstNonBlank(request.optString("description"), ""));
    }

    private String postmanUrl(Object urlValue) {
        if (urlValue == null || urlValue == JSONObject.NULL) {
            return "";
        }
        if (urlValue instanceof String text) {
            return postmanVariableToTestWeave(text);
        }
        if (urlValue instanceof JSONObject url) {
            String raw = url.optString("raw");
            if (!raw.isBlank()) {
                return postmanVariableToTestWeave(raw);
            }
            String protocol = url.optString("protocol");
            String host = postmanStringOrArray(url.opt("host"), ".");
            String path = postmanStringOrArray(url.opt("path"), "/");
            StringBuilder builder = new StringBuilder();
            if (!protocol.isBlank()) {
                builder.append(protocol).append("://");
            }
            builder.append(host);
            if (!path.isBlank()) {
                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '/') {
                    builder.append('/');
                }
                builder.append(path);
            }
            String query = postmanQueryString(url.optJSONArray("query"));
            if (!query.isBlank()) {
                builder.append('?').append(query);
            }
            return postmanVariableToTestWeave(builder.toString());
        }
        return postmanVariableToTestWeave(String.valueOf(urlValue));
    }

    private String postmanRequestUrl(PostmanCollectionNode node) {
        JSONObject request = node == null ? null : node.request;
        if (request == null) {
            return "";
        }
        String url = postmanUrl(request.opt("url"));
        JSONObject urlObject = request.optJSONObject("url");
        if (urlObject != null) {
            url = applyPostmanPathVariables(url, urlObject.optJSONArray("variable"));
        }
        return applyPostmanPathVariables(url, node.source.optJSONArray("variable"));
    }

    private String applyPostmanPathVariables(String url, JSONArray variables) {
        if (url == null || url.isBlank() || variables == null) {
            return url == null ? "" : url;
        }
        String resolved = url;
        for (int i = 0; i < variables.length(); i++) {
            JSONObject variable = variables.optJSONObject(i);
            if (variable == null || variable.optBoolean("disabled", false)) {
                continue;
            }
            String key = variable.optString("key");
            if (!key.isBlank()) {
                resolved = resolved.replace(":" + key, "${" + normalizeVariableName(key) + "}");
            }
        }
        return resolved;
    }

    private String postmanHeadersText(JSONObject request) {
        List<String> lines = new ArrayList<>();
        JSONArray headers = request.optJSONArray("header");
        if (headers != null) {
            for (int i = 0; i < headers.length(); i++) {
                JSONObject header = headers.optJSONObject(i);
                if (header == null || header.optBoolean("disabled", false)) {
                    continue;
                }
                String key = header.optString("key");
                if (!key.isBlank()) {
                    lines.add(key + ": " + postmanVariableToTestWeave(header.optString("value")));
                }
            }
        }
        JSONObject body = request.optJSONObject("body");
        if (body != null && "urlencoded".equalsIgnoreCase(body.optString("mode"))
                && lines.stream().noneMatch(line -> line.toLowerCase().startsWith("content-type:"))) {
            lines.add("Content-Type: application/x-www-form-urlencoded");
        } else if (body != null && "raw".equalsIgnoreCase(body.optString("mode"))
                && postmanRawBodyLooksJson(body)
                && lines.stream().noneMatch(line -> line.toLowerCase().startsWith("content-type:"))) {
            lines.add("Content-Type: application/json");
        }
        if (lines.stream().noneMatch(line -> line.toLowerCase().startsWith("accept:"))) {
            lines.add(0, "Accept: application/json");
        }
        return String.join(System.lineSeparator(), lines);
    }

    private boolean postmanRawBodyLooksJson(JSONObject body) {
        JSONObject options = body.optJSONObject("options");
        JSONObject rawOptions = options == null ? null : options.optJSONObject("raw");
        String language = rawOptions == null ? "" : rawOptions.optString("language");
        String raw = body.optString("raw").trim();
        return "json".equalsIgnoreCase(language) || raw.startsWith("{") || raw.startsWith("[");
    }

    private String postmanBodyText(JSONObject body) {
        if (body == null) {
            return "";
        }
        String mode = body.optString("mode");
        if ("raw".equalsIgnoreCase(mode)) {
            return postmanVariableToTestWeave(body.optString("raw"));
        }
        if ("urlencoded".equalsIgnoreCase(mode)) {
            return postmanKeyValueBody(body.optJSONArray("urlencoded"), true);
        }
        if ("formdata".equalsIgnoreCase(mode)) {
            return postmanKeyValueBody(body.optJSONArray("formdata"), false);
        }
        if ("graphql".equalsIgnoreCase(mode)) {
            JSONObject graphql = body.optJSONObject("graphql");
            return graphql == null ? "" : graphql.toString(2);
        }
        if ("file".equalsIgnoreCase(mode)) {
            return postmanBinaryFilePath(body);
        }
        return "";
    }

    private List<ApiRequestBodyPart> postmanMultipartParts(JSONObject body) {
        List<ApiRequestBodyPart> parts = new ArrayList<>();
        if (body == null || !"formdata".equalsIgnoreCase(body.optString("mode"))) {
            return parts;
        }
        JSONArray values = body.optJSONArray("formdata");
        if (values == null) {
            return parts;
        }
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item == null || item.optBoolean("disabled", false)) {
                continue;
            }
            String key = postmanVariableToTestWeave(item.optString("key"));
            String type = item.optString("type");
            if ("file".equalsIgnoreCase(type)) {
                Object src = item.opt("src");
                if (src instanceof JSONArray array) {
                    for (int j = 0; j < array.length(); j++) {
                        String path = postmanVariableToTestWeave(array.optString(j));
                        if (!path.isBlank()) {
                            parts.add(ApiRequestBodyPart.file(key, path, item.optString("contentType")));
                        }
                    }
                } else {
                    String path = src == null || src == JSONObject.NULL ? "" : postmanVariableToTestWeave(String.valueOf(src));
                    if (!path.isBlank()) {
                        parts.add(ApiRequestBodyPart.file(key, path, item.optString("contentType")));
                    }
                }
            } else {
                parts.add(ApiRequestBodyPart.text(key, postmanVariableToTestWeave(item.optString("value"))));
            }
        }
        return parts;
    }

    private String postmanBinaryFilePath(JSONObject body) {
        if (body == null || !"file".equalsIgnoreCase(body.optString("mode"))) {
            return "";
        }
        JSONObject file = body.optJSONObject("file");
        return file == null ? "" : postmanVariableToTestWeave(file.optString("src"));
    }

    private List<ApiRequestBodyPart> resolveRequestBodyParts(List<ApiRequestBodyPart> parts) {
        List<ApiRequestBodyPart> resolved = new ArrayList<>();
        if (parts == null) {
            return resolved;
        }
        for (ApiRequestBodyPart part : parts) {
            if (part == null) {
                continue;
            }
            if (part.file) {
                resolved.add(ApiRequestBodyPart.file(resolveVariables(part.name), resolveVariables(part.filePath),
                        resolveVariables(part.contentType)));
            } else {
                resolved.add(ApiRequestBodyPart.text(resolveVariables(part.name), resolveVariables(part.value)));
            }
        }
        return resolved;
    }

    private String postmanKeyValueBody(JSONArray values, boolean encode) {
        if (values == null) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item == null || item.optBoolean("disabled", false)) {
                continue;
            }
            String key = postmanVariableToTestWeave(item.optString("key"));
            String value = postmanVariableToTestWeave(item.optString("value"));
            if (!key.isBlank()) {
                parts.add(encode ? urlEncode(key) + "=" + urlEncode(value) : key + "=" + value);
            }
        }
        return String.join("&", parts);
    }

    private String postmanQueryString(JSONArray query) {
        if (query == null) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < query.length(); i++) {
            JSONObject item = query.optJSONObject(i);
            if (item == null || item.optBoolean("disabled", false)) {
                continue;
            }
            String key = postmanVariableToTestWeave(item.optString("key"));
            String value = postmanVariableToTestWeave(item.optString("value"));
            if (!key.isBlank()) {
                parts.add(urlEncode(key) + "=" + urlEncode(value));
            }
        }
        return String.join("&", parts);
    }

    private String postmanStringOrArray(Object value, String delimiter) {
        if (value instanceof JSONArray array) {
            List<String> parts = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                parts.add(array.optString(i));
            }
            return String.join(delimiter, parts);
        }
        return value == null || value == JSONObject.NULL ? "" : String.valueOf(value);
    }

    private String postmanVariableToTestWeave(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\{\\{\\s*([$A-Za-z0-9_.-]+)\\s*}}", "\\${$1}");
    }

    private void applyPostmanAuth(JSONObject request) {
        JSONObject auth = request.optJSONObject("auth");
        String type = postmanAuthType(auth);
        if ("bearer".equalsIgnoreCase(type)) {
            authTypeBox.setValue("Bearer Token");
            tokenField.setText(postmanAuthValue(auth, "bearer", "token"));
            updateAuthControls();
            return;
        }
        authTypeBox.setValue("No Auth");
        tokenField.clear();
        updateAuthControls();
        if ("basic".equalsIgnoreCase(type)) {
            String username = postmanAuthValue(auth, "basic", "username");
            String password = postmanAuthValue(auth, "basic", "password");
            appendHeaderIfMissing("Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)));
        } else if ("apikey".equalsIgnoreCase(type)) {
            String key = postmanAuthValue(auth, "apikey", "key");
            String value = postmanAuthValue(auth, "apikey", "value");
            String in = postmanAuthValue(auth, "apikey", "in");
            if ("header".equalsIgnoreCase(in) && !key.isBlank()) {
                appendHeaderIfMissing(key, value);
            } else if ("query".equalsIgnoreCase(in) && !key.isBlank()) {
                endpointField.setText(appendQueryParameter(endpointField.getText(), key, value));
            }
        }
    }

    private String appendQueryParameter(String url, String key, String value) {
        if (url == null || url.isBlank()) {
            return url == null ? "" : url;
        }
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + urlEncode(key) + "=" + urlEncode(value == null ? "" : value);
    }

    private String postmanAuthType(JSONObject auth) {
        return auth == null ? "No Auth" : firstNonBlank(auth.optString("type"), "No Auth");
    }

    private String postmanAuthValue(JSONObject auth, String arrayName, String keyName) {
        Object rawValues = auth == null ? null : auth.opt(arrayName);
        if (rawValues instanceof JSONObject object) {
            return postmanVariableToTestWeave(object.optString(keyName));
        }
        JSONArray values = rawValues instanceof JSONArray array ? array : null;
        if (values == null) return "";
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item != null && keyName.equalsIgnoreCase(item.optString("key"))) {
                return postmanVariableToTestWeave(item.optString("value"));
            }
        }
        return "";
    }

    private void appendHeaderIfMissing(String key, String value) {
        if (key == null || key.isBlank()) {
            return;
        }
        String current = headersArea.getText() == null ? "" : headersArea.getText();
        boolean exists = parseHeaders(current).keySet().stream().anyMatch(existing -> existing.equalsIgnoreCase(key));
        if (!exists) {
            headersArea.appendText((current.isBlank() ? "" : System.lineSeparator()) + key + ": " + value);
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private javafx.scene.Node createResponseVariableCapturePanel() {
        responseFieldRows = FXCollections.observableArrayList();
        responseFieldsTable = mapTable(responseFieldRows,
                "Save", "selected", "JSON Path", "jsonPath", "Preview Value", "preview",
                "Variable Name", "variableName", "Type", "type", "Value", "value");
        responseFieldsTable.getSelectionModel().setCellSelectionEnabled(false);

        Button toggleAll = secondary("Toggle All");
        toggleAll.setOnAction(e -> toggleAllSelected(responseFieldRows, responseFieldsTable));
        Button selectAll = secondary("Select All");
        selectAll.setOnAction(e -> {
            responseFieldRows.forEach(row -> row.put("selected", "true"));
            responseFieldsTable.refresh();
        });
        Button clear = secondary("Clear");
        clear.setOnAction(e -> {
            responseFieldRows.forEach(row -> row.put("selected", "false"));
            responseFieldsTable.refresh();
        });
        Button selectTopLevel = secondary("Select Top Level");
        selectTopLevel.setOnAction(e -> selectTopLevelResponseFields());
        Button save = primary("Save Selected Variables");
        save.setOnAction(e -> saveSelectedResponseVariables());
        FlowPane tools = actionRow(toggleAll, selectAll, selectTopLevel, clear, save);
        tools.getStyleClass().add("capture-toolbar");

        BorderPane panel = new BorderPane(responseFieldsTable);
        panel.setTop(tools);
        BorderPane.setMargin(tools, new Insets(0, 0, 12, 0));
        panel.getStyleClass().add("capture-panel");
        if (lastResponse != null && lastResponse.rawBody != null && !lastResponse.rawBody.isBlank()) {
            parseResponseFields(lastResponse.rawBody);
        }
        return panel;
    }

    private javafx.scene.Node createApiValidationPanel() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(tab("Field Validation", createFieldValidationPanel()));
        tabs.getTabs().add(tab("JSON Compare", createComparePanel()));
        return padded(card("Validation Card", tabs));
    }

    private javafx.scene.Node createFieldValidationPanel() {
        fieldValidationTestSuiteField = new TextField();
        fieldValidationTestCaseField = new TextField();
        fieldValidationTestStepField = new TextField();
        applySharedTestSuiteContext(fieldValidationTestSuiteField, fieldValidationTestCaseField);

        fieldValidationRows = FXCollections.observableArrayList();
        fieldValidationsTable = mapTable(fieldValidationRows,
                "Add", "selected", "JSON Path", "field", "Preview Value", "preview",
                "Null Validation", "nullValidation", "Type Validation", "typeValidation",
                "Expected Value / Variable", "expected", "Result", "result",
                "Actual Value", "actual", "Actual Type", "actualType", "Message", "message");
        configureFieldValidationTableEditors();

        Button reset = secondary("Reset Defaults");
        reset.setOnAction(e -> resetFieldValidationDefaults());
        Button toggleAll = secondary("Toggle All");
        toggleAll.setOnAction(e -> toggleAllSelected(fieldValidationRows, fieldValidationsTable));
        Button validate = primary("Validate Fields");
        validate.setOnAction(e -> runFieldValidations());
        apiAiValidationConnectionLabel = new Label();
        apiAiValidationConnectionLabel.getStyleClass().add("muted");
        updateApiAiConnectionLabels();
        FlowPane tools = actionRow(toggleAll, reset, validate, apiAiValidationConnectionLabel);

        VBox top = new VBox(16,
                createTestRunnerContextPanel(fieldValidationTestSuiteField, fieldValidationTestCaseField,
                        fieldValidationTestStepField, () -> addFieldValidationToTestRunner()),
                tools);
        top.getStyleClass().add("validation-toolbar");

        BorderPane panel = new BorderPane(fieldValidationsTable);
        panel.setTop(top);
        BorderPane.setMargin(top, new Insets(0, 0, 16, 0));
        if (lastResponse != null && lastResponse.rawBody != null && !lastResponse.rawBody.isBlank()) {
            parseResponseFields(lastResponse.rawBody);
        }
        return panel;
    }

    private void configureFieldValidationTableEditors() {
        configureComboColumn(fieldValidationsTable, "nullValidation", () -> List.of("Not Null", "Null", "Skip"));
        configureComboColumn(fieldValidationsTable, "typeValidation", this::jsonTypeValidationOptions);
        configureEditableComboColumn(fieldValidationsTable, "expected", this::savedVariableReferences);
    }

    private List<String> jsonTypeValidationOptions() {
        return List.of("Skip", "string", "number", "integer", "boolean", "object", "array", "null");
    }

    private List<String> savedVariableReferences() {
        return savedVariables.keySet().stream().sorted().map(name -> "${" + name + "}").toList();
    }

    private void configureComboColumn(TableView<Map<String, String>> table, String key, Supplier<List<String>> values) {
        TableColumn<Map<String, String>, String> column = findStringColumn(table, key);
        if (column == null) {
            return;
        }
        column.setEditable(true);
        column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(values.get())));
        column.setOnEditCommit(event -> event.getRowValue().put(key, nullToBlank(event.getNewValue())));
    }

    private void configureEditableComboColumn(TableView<Map<String, String>> table, String key,
                                              Supplier<List<String>> values) {
        TableColumn<Map<String, String>, String> column = findStringColumn(table, key);
        if (column == null) {
            return;
        }
        column.setEditable(true);
        column.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<String> combo = new ComboBox<>();

            {
                combo.setEditable(true);
                combo.setMaxWidth(Double.MAX_VALUE);
                combo.setOnAction(e -> commitComboValue());
                combo.getEditor().setOnAction(e -> commitComboValue());
                combo.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (!isFocused && isEditing()) {
                        commitComboValue();
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                combo.setItems(FXCollections.observableArrayList(values.get()));
                String value = nullToBlank(getItem());
                combo.setValue(value);
                combo.getEditor().setText(value);
                setText(null);
                setGraphic(combo);
                Platform.runLater(() -> {
                    combo.requestFocus();
                    combo.getEditor().selectAll();
                });
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(nullToBlank(getItem()));
                setGraphic(null);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    combo.getEditor().setText(nullToBlank(item));
                    setText(null);
                    setGraphic(combo);
                } else {
                    setText(nullToBlank(item));
                    setGraphic(null);
                }
            }

            private void commitComboValue() {
                String value = combo.getEditor().getText();
                if (value == null) {
                    value = combo.getValue();
                }
                commitEdit(nullToBlank(value));
            }
        });
        column.setOnEditCommit(event -> event.getRowValue().put(key, nullToBlank(event.getNewValue())));
    }

    @SuppressWarnings("unchecked")
    private TableColumn<Map<String, String>, String> findStringColumn(TableView<Map<String, String>> table, String key) {
        if (table == null) {
            return null;
        }
        for (TableColumn<Map<String, String>, ?> column : table.getColumns()) {
            if (key.equals(column.getId())) {
                return (TableColumn<Map<String, String>, String>) column;
            }
        }
        return null;
    }

    private javafx.scene.Node createComparePanel() {
        jsonCompareTestSuiteField = new TextField();
        jsonCompareTestCaseField = new TextField();
        jsonCompareTestStepField = new TextField();
        applySharedTestSuiteContext(jsonCompareTestSuiteField, jsonCompareTestCaseField);

        expectedJsonPathField = new TextField();
        expectedJsonPathField.setPrefColumnCount(90);
        expectedJsonPathField.setPrefWidth(900);
        expectedJsonPathField.setMinWidth(520);
        compareModeBox = combo("Strict", "Lenient");
        compareRows = FXCollections.observableArrayList();
        compareTable = mapTable(compareRows,
                "Result", "status", "Path", "path", "Expected", "expected", "Actual", "actual", "Message", "message");

        Button browse = secondary("Browse");
        browse.setOnAction(e -> chooseExpectedJson());
        Button compare = primary("Compare");
        compare.setOnAction(e -> runCompare(false));
        Button matched = secondary("Show Matched");
        matched.setOnAction(e -> runCompare(true));
        FlowPane controls = actionRow(labeled("Expected JSON File", wrapTextFieldWithActions(expectedJsonPathField, browse)),
                labeled("Compare Mode", compareModeBox), compare, matched);

        VBox top = new VBox(16,
                createTestRunnerContextPanel(jsonCompareTestSuiteField, jsonCompareTestCaseField,
                        jsonCompareTestStepField, () -> addJsonCompareToTestRunner()),
                controls);
        top.getStyleClass().add("validation-toolbar");

        BorderPane panel = new BorderPane(compareTable);
        panel.setTop(top);
        BorderPane.setMargin(top, new Insets(0, 0, 16, 0));
        return panel;
    }

    private javafx.scene.Node createPerformancePanel() {
        performanceTestSuiteField = new TextField();
        performanceTestCaseField = new TextField();
        performanceTestStepField = new TextField();
        applySharedTestSuiteContext(performanceTestSuiteField, performanceTestCaseField);

        perfThreadsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 5));
        perfIterationsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 10));
        perfBodyArea = editor("");
        perfLogArea = editor("");
        perfSamplesLabel = metric("--");
        perfErrorsLabel = metric("--");
        perfThroughputLabel = metric("--");
        perfDurationLabel = metric("--");
        perfReportLabel = metric("No report yet");

        ComboBox<String> perfVariableBox = createVariableDropdown();
        Button insertPerfVariable = secondary("Insert Variable");
        insertPerfVariable.setOnAction(e -> insertVariable(perfBodyArea, perfVariableBox));
        Button copyBody = secondary("Copy From API Tester");
        copyBody.setOnAction(e -> perfBodyArea.setText(bodyArea == null ? "" : bodyArea.getText()));
        Button run = primary("Run Load Test");
        run.setOnAction(e -> runPerformanceTest());
        Button open = secondary("Open Report");
        open.setOnAction(e -> openPerformanceReport());

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        perfChart = new BarChart<>(xAxis, yAxis);
        perfChart.setLegendVisible(false);
        perfChart.setAnimated(false);
        perfChart.setTitle("Latency Snapshot (ms)");

        FlowPane config = actionRow(labeled("Threads", perfThreadsSpinner), labeled("Iterations / Thread", perfIterationsSpinner), run, open);
        VBox runnerContext = new VBox(12,
                createTestRunnerContextPanel(performanceTestSuiteField, performanceTestCaseField,
                        performanceTestStepField, () -> addPerformanceTestToTestRunner()),
                config);
        runnerContext.getStyleClass().add("validation-toolbar");

        FlowPane metrics = actionRow(
                labeled("Samples", perfSamplesLabel), labeled("Errors", perfErrorsLabel),
                labeled("Throughput", perfThroughputLabel), labeled("Duration", perfDurationLabel),
                labeled("Report", perfReportLabel));
        SplitPane center = new SplitPane(card("Latency Graph", perfChart), card("Execution Log", perfLogArea));
        center.setDividerPositions(0.5);
        FlowPane perfBodyTools = spacedActionRow(perfVariableBox, insertPerfVariable, copyBody);
        perfBodyArea.setPrefRowCount(12);
        perfBodyArea.setMinHeight(240);
        VBox panel = new VBox(14, sectionTitle("Performance Test"), runnerContext,
                card("Request Body Override", withFooter(perfBodyArea, perfBodyTools)), metrics, center);
        VBox.setVgrow(center, Priority.ALWAYS);
        return padded(panel);
    }

    private javafx.scene.Node createDbValidatorPanel() {
        dbTypeBox = combo("MySQL", "PostgreSQL", "Oracle", "SQL Server", "Custom");
        jdbcUrlField = new TextField();
        dbUsernameField = new TextField();
        dbPasswordField = new PasswordField();
        visibleDbPasswordField = new TextField();
        visibleDbPasswordField.textProperty().bindBidirectional(dbPasswordField.textProperty());
        visibleDbPasswordField.setManaged(false);
        visibleDbPasswordField.setVisible(false);
        driverClassField = new TextField("com.mysql.cj.jdbc.Driver");
        dbQueryArea = editor("");
        dbVariableDropdown = createVariableDropdown();
        dbConnectionStatusLabel = new Label("Not connected");
        dbConnectionStatusLabel.getStyleClass().add("muted");
        dbValidationTestSuiteField = new TextField();
        dbValidationTestCaseField = new TextField();
        dbValidationTestStepField = new TextField();
        applySharedTestSuiteContext(dbValidationTestSuiteField, dbValidationTestCaseField);

        Button defaults = secondary("Apply Defaults");
        defaults.setOnAction(e -> applyDbDefaults());
        Button testConnection = primary("Test Connection");
        testConnection.setOnAction(e -> testDbConnection());
        Button showPassword = secondary("Show");
        showPassword.setOnAction(e -> toggleDbPasswordVisibility(showPassword));
        Button saveConnection = secondary("Save Connection");
        saveConnection.setOnAction(e -> saveDbConnection());
        Button loadConnection = secondary("Load Connection");
        loadConnection.setOnAction(e -> loadDbConnection());
        Button insertDbVariable = secondary("Insert Variable");
        insertDbVariable.setOnAction(e -> insertVariable(dbQueryArea, dbVariableDropdown));
        Button saveQuery = secondary("Save Query");
        saveQuery.setOnAction(e -> saveTextFile(dbQueryArea.getText(), "dbquery.sql", configuredFolder("DB", "SQLQuery")));
        Button loadQuery = secondary("Load Query");
        loadQuery.setOnAction(e -> loadTextFile(dbQueryArea, configuredFolder("DB", "SQLQuery")));
        Button useApiVariables = secondary("Use API Response Variables");
        useApiVariables.setOnAction(e -> populateDefaultDbRules());
        Button runQuery = primary("Run Query");
        runQuery.setOnAction(e -> runDbQuery());
        Button dbAiAnalysis = secondary("AI Analysis");
        dbAiAnalysis.setOnAction(e -> runDbAiAnalysisForResultSet());
        Button saveSelectedCell = secondary("Save Selected Cell as Variable");
        saveSelectedCell.setOnAction(e -> saveSelectedDbResultCellAsVariable());

        dbTypeBox.setMinWidth(180);
        dbTypeBox.setPrefWidth(220);
        jdbcUrlField.setMinWidth(320);
        jdbcUrlField.setPrefWidth(560);
        dbUsernameField.setMinWidth(210);
        dbUsernameField.setPrefWidth(260);
        dbPasswordField.setMinWidth(230);
        visibleDbPasswordField.setMinWidth(230);
        driverClassField.setMinWidth(320);
        driverClassField.setPrefWidth(430);
        showPassword.setMinWidth(92);
        dbConnectionStatusLabel.setMinWidth(180);

        FlowPane connectionActions = spacedActionRow(defaults, testConnection, saveConnection, loadConnection);
        connectionActions.setHgap(20);
        connectionActions.setVgap(14);
        connectionActions.setPrefWrapLength(560);
        VBox dbForm = new VBox(18,
                labeled("DB Type", dbTypeBox),
                labeled("JDBC URL", jdbcUrlField),
                labeled("Username", dbUsernameField),
                labeled("Password", wrapDbPasswordField(showPassword)),
                labeled("Driver Class", driverClassField),
                labeled("Status", dbConnectionStatusLabel),
                connectionActions);
        dbForm.setMinWidth(0);
        dbForm.setFillWidth(true);

        dbQueryResultRows = FXCollections.observableArrayList();
        dbQueryResultsTable = mapTable(dbQueryResultRows, "Row", "row");
        dbQueryResultsTable.getSelectionModel().setCellSelectionEnabled(true);
        dbRuleRows = FXCollections.observableArrayList();
        dbRulesTable = mapTable(dbRuleRows,
                "Validate", "selected", "API Field", "apiField", "DB Column", "dbColumn", "Operator", "operator", "Description", "description");
        dbResultRows = FXCollections.observableArrayList();
        dbResultsTable = mapTable(dbResultRows,
                "Result", "result", "Field", "field", "Expected", "expected", "Actual", "actual", "Operator", "operator", "Message", "message");
        dbColumnValidationRows = FXCollections.observableArrayList();
        dbColumnValidationsTable = mapTable(dbColumnValidationRows,
                "Validate", "selected", "DB Column Name", "dbColumnName", "Value", "value",
                "Null Validation", "nullValidation", "Type Validation", "typeValidation",
                "Expected Value / Variable", "expectedValueOrVariable", "Result", "result");
        dbSummaryLabel = metric("--");

        Button addRule = secondary("Add Rule");
        addRule.setOnAction(e -> dbRuleRows.add(row("selected", "true", "apiField", "", "dbColumn", "", "operator", "=", "description", "")));
        Button removeRule = secondary("Remove Rule");
        removeRule.setOnAction(e -> dbRuleRows.removeAll(dbRulesTable.getSelectionModel().getSelectedItems()));
        Button checkAll = secondary("Check All");
        checkAll.setOnAction(e -> setAllRowsSelected(dbRuleRows, dbRulesTable, true));
        Button uncheckAll = secondary("Un-Check All");
        uncheckAll.setOnAction(e -> setAllRowsSelected(dbRuleRows, dbRulesTable, false));
        Button loadColumns = secondary("Load DB Columns");
        loadColumns.setOnAction(e -> loadDbColumnOptions());
        Button saveRules = secondary("Save Rules");
        saveRules.setOnAction(e -> saveDbRules());
        Button loadRules = secondary("Load Rules");
        loadRules.setOnAction(e -> loadDbRules());
        Button validate = primary("Run DB Validation");
        validate.setOnAction(e -> runDbValidation());

        FlowPane queryActions = spacedActionRow(dbVariableDropdown, insertDbVariable, saveQuery, loadQuery, useApiVariables, runQuery, dbAiAnalysis);
        queryActions.setPrefWrapLength(620);
        dbQueryArea.setMinHeight(260);
        dbQueryArea.setPrefRowCount(12);
        VBox queryBox = new VBox(16, dbQueryArea, queryActions);
        queryBox.setMinWidth(0);
        queryBox.setFillWidth(true);
        VBox.setVgrow(dbQueryArea, Priority.ALWAYS);
        VBox connectionCard = card("Connection", verticalSectionScroll(dbForm, 360));
        VBox queryCard = card("Query", verticalSectionScroll(queryBox, 360));
        connectionCard.setPrefWidth(760);
        queryCard.setPrefWidth(640);
        SplitPane top = new SplitPane(connectionCard, queryCard);
        top.setDividerPositions(0.56);
        TabPane validationTabs = new TabPane();
        validationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        validationTabs.getTabs().add(tab("API-DB Validation", withFooter(dbRulesTable,
                spacedActionRow(addRule, removeRule, checkAll, uncheckAll, loadColumns, saveRules, loadRules, validate))));
        validationTabs.getTabs().add(tab("DB Validation", withFooter(dbColumnValidationsTable,
                spacedActionRow(secondaryButton("Reset Defaults", e -> resetDbColumnValidationDefaults()),
                        secondaryButton("Check All", e -> setAllRowsSelected(dbColumnValidationRows, dbColumnValidationsTable, true)),
                        secondaryButton("Un-Check All", e -> setAllRowsSelected(dbColumnValidationRows, dbColumnValidationsTable, false)),
                        primaryButton("Validate DB Columns", e -> runDbColumnValidations())))));
        VBox rulesBox = new VBox(12,
                createTestRunnerContextPanel(dbValidationTestSuiteField, dbValidationTestCaseField,
                        dbValidationTestStepField, () -> addDbValidationsToTestRunner()),
                validationTabs);
        VBox.setVgrow(validationTabs, Priority.ALWAYS);

        VBox panel = new VBox(18,
                top,
                card("Query Resultset", withFooter(dbQueryResultsTable, spacedActionRow(saveSelectedCell))),
                card("Validation Rules", rulesBox),
                card("Validation Results", withFooter(dbResultsTable, dbSummaryLabel)));
        panel.getStyleClass().add("db-workflow");
        top.setMinHeight(460);
        dbQueryResultsTable.setMinHeight(260);
        validationTabs.setMinHeight(360);
        dbResultsTable.setMinHeight(300);
        return padded(panel);
    }

    private javafx.scene.Node createWebTestingPanel() {
        webTestNameField = new TextField("Web Test");
        webStartUrlField = new TextField();
        webCdpEndpointField = new TextField("http://127.0.0.1:9222");
        webHeadlessCheck = new CheckBox("Headless");
        webHeadlessCheck.setSelected(true);
        webSlowMoCheck = new CheckBox("Slow Mo");
        for (CheckBox option : new CheckBox[]{webHeadlessCheck, webSlowMoCheck}) {
            option.getStyleClass().add("web-option-check");
            option.setMinWidth(110);
        }
        webRecorderStatusLabel = new Label("Recorder idle");
        webBrowserUrlLabel = new Label("Browser URL: --");
        webRunSummaryLabel = metric("--");
        webTestingTestSuiteField = new TextField();
        webTestingTestCaseField = new TextField();
        webTestingTestStepField = new TextField();
        applySharedTestSuiteContext(webTestingTestSuiteField, webTestingTestCaseField);

        webStepRows = FXCollections.observableArrayList();
        webStepsTable = mapTable(webStepRows,
                "Step", "step", "Action", "action", "Selector / Variable Name", "selector", "Value", "value", "Note", "note");
        webResultRows = FXCollections.observableArrayList();
        webResultsTable = mapTable(webResultRows,
                "Result", "result", "Action", "action", "Selector", "selector", "Expected", "expected", "Message", "message", "Duration", "duration");

        Button record = primary("Record");
        record.setOnAction(e -> startWebRecording());
        Button attach = secondary("Attach");
        attach.setOnAction(e -> startAttachedWebRecording());
        Button stop = secondary("Stop");
        stop.setOnAction(e -> stopWebRecording());
        Button stopNoClose = secondary("Stop-No Browser Close");
        stopNoClose.setOnAction(e -> stopWebRecordingWithoutClosingBrowser());
        Button launchDebug = secondary("Launch Debug Chrome");
        launchDebug.setOnAction(e -> launchDebugChrome());
        Button clearSteps = secondary("Clear Steps");
        clearSteps.setOnAction(e -> clearWebSteps());
        Button add = secondary("Add Step");
        add.setOnAction(e -> addWebStepDialog());
        Button edit = secondary("Edit Step");
        edit.setOnAction(e -> editSelectedWebStep());
        Button delete = secondary("Delete Step");
        delete.setOnAction(e -> webStepRows.removeAll(webStepsTable.getSelectionModel().getSelectedItems()));
        Button moveUp = secondary("Move Up");
        moveUp.setOnAction(e -> moveSelectedWebStep(-1));
        Button moveDown = secondary("Move Down");
        moveDown.setOnAction(e -> moveSelectedWebStep(1));
        Button screenshot = secondary("Add Screenshot Step");
        screenshot.setOnAction(e -> addWebScreenshotStep());
        Button merge = secondary("Merge Recording");
        merge.setOnAction(e -> mergeWebRecording());
        Button save = secondary("Save Recording");
        save.setOnAction(e -> saveWebRecording());
        Button load = secondary("Load Recording");
        load.setOnAction(e -> loadWebRecording());
        Button run = primary("Run Web Test");
        run.setOnAction(e -> runWebTest());
        Button webAiAnalysis = secondary("AI Analysis");
        webAiAnalysis.setOnAction(e -> runWebAiAnalysisForFailures());
        Button stopRun = secondary("Stop Run");
        stopRun.setOnAction(e -> playwrightRecorderController.stopRunningWebTest());

        GridPane form = grid();
        form.add(labeled("Test Name", webTestNameField), 0, 0);
        form.add(labeled("Start URL", webStartUrlField), 1, 0);
        form.add(labeled("Active Browser CDP", webCdpEndpointField), 2, 0);
        GridPane.setHgrow(webStartUrlField, Priority.ALWAYS);
        FlowPane recorderTools = spacedActionRow(record, attach, launchDebug, stop, stopNoClose, clearSteps, save,
                load, screenshot, webRecorderStatusLabel);
        VBox header = new VBox(10, form, recorderTools, webBrowserUrlLabel);

        FlowPane runControls = spacedActionRow(run, webAiAnalysis, stopRun, webHeadlessCheck, webSlowMoCheck, labeled("Summary", webRunSummaryLabel));
        runControls.setPrefWrapLength(1000);
        VBox runnerContext = new VBox(12,
                createTestRunnerContextPanel(webTestingTestSuiteField, webTestingTestCaseField,
                        webTestingTestStepField, () -> addWebTestToTestRunner()),
                runControls);
        runnerContext.getStyleClass().add("validation-toolbar");
        FlowPane stepTools = spacedActionRow(add, edit, delete, moveUp, moveDown, merge);
        VBox capturedSection = card("Captured Steps", withFooter(webStepsTable, stepTools));
        webStepsTable.setMinHeight(260);

        VBox resultsSection = card("Step Results", webResultsTable);
        webResultsTable.setMinHeight(300);

        VBox panel = new VBox(16, card("UI Workflow Card", header), capturedSection, runnerContext, resultsSection);
        VBox.setVgrow(resultsSection, Priority.ALWAYS);
        return padded(card("Workflow Card", panel));
    }

    private javafx.scene.Node createCodexCliPanel() {
        codexLogArea = editor("");
        codexLogArea.setEditable(false);
        codexLogArea.setWrapText(true);
        codexLogArea.setMinHeight(420);

        Button setupCodex = primary("Setup Codex CLI");
        setupCodex.setOnAction(e -> setupCodexCliDocker());
        Button startCodex = secondary("Start Codex CLI");
        startCodex.setOnAction(e -> startCodexCliContainer());
        Button stopCodex = secondary("Stop Codex CLI");
        stopCodex.setOnAction(e -> stopCodexCliContainer());

        FlowPane codexControls = spacedActionRow(setupCodex, startCodex, stopCodex);
        VBox logs = card("Logs", codexLogArea);
        VBox panel = new VBox(14, codexControls, logs);
        VBox.setVgrow(logs, Priority.ALWAYS);
        return padded(card("Variable Card", panel));
    }

    private javafx.scene.Node createHermesAgentPanel() {
        hermesAiAgentPathField = new TextField();
        hermesAiAgentPathField.setEditable(false);
        hermesAiAgentPathField.setPrefColumnCount(90);
        hermesAiAgentPathField.setPrefWidth(900);
        hermesAiAgentPathField.setMinWidth(520);
        updateHermesAiAgentPathField();

        hermesSessionBox = new ComboBox<>();
        hermesSessionBox.setEditable(false);
        hermesSessionBox.setPrefWidth(320);
        refreshHermesSessionOptions();

        hermesManualSessionIdField = new TextField();
        hermesManualSessionIdField.setPromptText("Paste Hermes session id, for example 20260619_084413_a6ed26");
        hermesManualSessionIdField.setPrefWidth(420);
        Button saveHermesSession = secondary("Save Session");
        saveHermesSession.setOnAction(e -> saveManualHermesSessionId());

        hermesLogArea = editor("");
        hermesLogArea.setEditable(false);
        hermesLogArea.setWrapText(true);
        hermesLogArea.setMinHeight(420);

        Button startHermes = secondary("Start Hermes CLI");
        startHermes.setOnAction(e -> startHermesAgentContainer());
        Button browserHermes = secondary("Launch Browser");
        browserHermes.setOnAction(e -> launchHermesAgentBrowser());
        Button stopHermes = secondary("Stop Hermes");
        stopHermes.setOnAction(e -> stopHermesAgentContainer());

        FlowPane hermesControls = spacedActionRow(labeled("Session", hermesSessionBox), startHermes, browserHermes, stopHermes);
        FlowPane manualSessionControls = spacedActionRow(labeled("Session ID", hermesManualSessionIdField), saveHermesSession);
        VBox logs = card("Logs", hermesLogArea);
        VBox panel = new VBox(14, labeled("AI Agent Path", hermesAiAgentPathField), hermesControls, manualSessionControls, logs);
        VBox.setVgrow(logs, Priority.ALWAYS);
        return padded(panel);
    }

    private javafx.scene.Node createTestSuitePanel() {
        testSuiteNameField = new TextField();
        testCaseNameField = new ComboBox<>();
        testCaseNameField.setEditable(true);
        testCaseNameField.setMaxWidth(Double.MAX_VALUE);
        testSuiteWorkbookPathField = new TextField();
        testSuiteWorkbookPathField.setEditable(false);
        testSuiteParallelExecutionCheck = new CheckBox("Parallel Execution");
        testSuiteThreadCountField = new TextField("1");
        testSuiteThreadCountField.setMaxWidth(90);
        githubOwnerField = new TextField();
        githubRepoField = new TextField();
        githubBranchField = new TextField("main");
        githubStatusLabel = new Label("GitHub: not connected");
        githubStatusLabel.getStyleClass().add("muted");
        testSuiteStepsTable = createTestSuiteStepsTable();
        testSuiteRunnerStatusLabel = new Label("Import or create a Test Suite Runner workbook to view test steps.");
        testSuiteRunnerStatusLabel.getStyleClass().add("muted");

        Button create = primary("Create Workbook");
        create.setOnAction(e -> createTestSuiteWorkbook());
        Button importWorkbook = secondary("Import Workbook");
        importWorkbook.setOnAction(e -> importTestSuiteWorkbook());
        Button addManual = secondary("Add Manual Step");
        addManual.setOnAction(e -> addManualTestSuiteStep());
        Button run = primary("Run Selected");
        run.setOnAction(e -> runSelectedTestSuiteSteps());
        Button stop = secondary("Stop Execution");
        stop.getStyleClass().add("danger-button");
        stop.setOnAction(e -> stopTestSuiteRunnerExecution());
        Button suiteBuilder = secondary("Suite Builder");
        suiteBuilder.setOnAction(e -> openTestSuiteBuilderCanvas());
        testSuiteBuilderAddTypeBox = combo("Test Suite", "Test Case", "Test Step");
        testSuiteBuilderAddTypeBox.setPrefWidth(160);
        Button addBuilderItem = secondary("Add");
        addBuilderItem.setOnAction(e -> addSelectedTestSuiteBuilderItem());
        Button checkAll = secondary("Check All");
        checkAll.setOnAction(e -> setAllRowsSelected(testSuiteRows, testSuiteStepsTable, true));
        Button uncheckAll = secondary("Un-Check All");
        uncheckAll.setOnAction(e -> setAllRowsSelected(testSuiteRows, testSuiteStepsTable, false));
        Button openReport = secondary("Open Report");
        openReport.setOnAction(e -> openTestSuiteReport());
        Button updateWorkbook = secondary("Update");
        updateWorkbook.setOnAction(e -> updateTestSuiteWorkbook());
        Button openWorkbook = secondary("Open");
        openWorkbook.setOnAction(e -> openImportedTestSuiteWorkbook());
        Button connectGithub = primary("Connect GitHub");
        connectGithub.setOnAction(e -> connectGithub());
        Button deployGithub = secondary("Deploy to GitHub Actions");
        deployGithub.setOnAction(e -> deployTestSuiteToGithubActions());
        Button runGithub = primary("Run in GitHub Actions");
        runGithub.setOnAction(e -> runGithubActionsTestSuite());
        Button openWorkflow = secondary("Open Workflow");
        openWorkflow.setOnAction(e -> openGithubWorkflow());

        testSuiteNameField.textProperty().addListener((observable, oldValue, newValue) -> propagateTestSuiteContext());
        testCaseNameField.valueProperty().addListener((observable, oldValue, newValue) -> onTestSuiteCaseSelected(newValue));
        testCaseNameField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> propagateTestSuiteContext());

        FlowPane controls = spacedActionRow(labeled("Test Suite", testSuiteNameField), labeled("Test Case", testCaseNameField),
                create, importWorkbook, updateWorkbook, openWorkbook, addManual);
        FlowPane runnerActions = spacedActionRow(testSuiteRunnerStatusLabel, checkAll, uncheckAll, openReport,
                testSuiteParallelExecutionCheck, labeled("Threads", testSuiteThreadCountField), run, stop, suiteBuilder,
                testSuiteBuilderAddTypeBox, addBuilderItem);
        FlowPane githubActions = spacedActionRow(githubStatusLabel, labeled("Owner", githubOwnerField),
                labeled("Repository", githubRepoField), labeled("Branch", githubBranchField),
                connectGithub, deployGithub, runGithub, openWorkflow);
        VBox panel = new VBox(16, controls, labeled("Workbook Path", testSuiteWorkbookPathField),
                runnerActions, card("GitHub Actions", githubActions), testSuiteStepsTable);
        VBox.setVgrow(testSuiteStepsTable, Priority.ALWAYS);
        return padded(panel);
    }

    private javafx.scene.Node createVariablesPanel() {
        variableRows = FXCollections.observableArrayList();
        variablesTable = mapTable(variableRows,
                "Name", "name", "Value", "value", "Type", "type", "JSON Path", "path");
        variablesPathField = new TextField();
        variablesPathField.setEditable(false);
        variablesPathField.setPrefColumnCount(90);
        variablesPathField.setPrefWidth(900);
        variablesPathField.setMinWidth(520);
        Button create = primary("Create Variable");
        create.setOnAction(e -> createVariableDialog());
        Button remove = secondary("Remove Selected");
        remove.setOnAction(e -> removeSelectedVariables());
        Button save = secondary("Save Variables");
        save.setOnAction(e -> saveVariablesToFile());
        Button load = secondary("Import Variables");
        load.setOnAction(e -> importVariablesFromFile());
        FlowPane tools = actionRow(create, save, load, remove);
        BorderPane panel = new BorderPane(variablesTable);
        VBox top = new VBox(12, labeled("Variables Path", variablesPathField), tools);
        panel.setTop(top);
        BorderPane.setMargin(top, new Insets(0, 0, 12, 0));
        applyConfiguredPathsToFields();
        refreshVariablesView();
        return padded(panel);
    }

    private javafx.scene.Node createConfigPanel() {
        configBasePathField = new TextField();
        configBasePathField.setPrefColumnCount(90);
        configBasePathField.setPrefWidth(900);
        configBasePathField.setMinWidth(520);
        configStatusLabel = new Label("No config folder created yet.");
        configStatusLabel.getStyleClass().add("muted");
        configCacheKeyLabel = new Label("Cache key: " + configCacheKey());
        configCacheKeyLabel.getStyleClass().add("muted");
        configCacheDbLabel = new Label("SQLite Cache: enter a base path to see where the DB will be created.");
        configCacheDbLabel.getStyleClass().add("muted");
        configExecutionStorageToggle = new ToggleButton();
        configExecutionStorageToggle.getStyleClass().add("storage-toggle");
        installStorageToggleGraphic(configExecutionStorageToggle);
        configExecutionStorageToggle.setSelected(true);
        configExecutionStorageToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateExecutionStorageToggle();
            persistExecutionStorageMode();
            refreshDashboard();
        });
        configExecutionStorageLabel = new Label();
        configExecutionStorageLabel.getStyleClass().add("muted");
        updateExecutionStorageToggle();
        apiAiAgentStorageToggle = new ToggleButton();
        apiAiAgentStorageToggle.getStyleClass().add("storage-toggle");
        installStorageToggleGraphic(apiAiAgentStorageToggle);
        apiAiAgentStorageToggle.setSelected(false);
        apiAiAgentStorageToggle.selectedProperty().addListener((observable, oldValue, newValue) -> updateApiAiAgentToggleLabels());
        apiAiAgentStorageLabel = new Label();
        apiAiAgentStorageLabel.getStyleClass().add("muted");
        apiAiHermesSessionBox = new ComboBox<>();
        apiAiHermesSessionBox.setEditable(false);
        apiAiHermesSessionBox.setPrefWidth(420);
        apiAiHermesSessionBox.setPromptText("Hermes session");
        apiAiHermesConnectionLabel = new Label("Hermes: disconnected");
        apiAiHermesConnectionLabel.getStyleClass().add("muted");
        updateApiAiAgentToggleLabels();
        configBasePathField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateConfigCachePathLabel();
            applyConfiguredPathsToFields();
            refreshDashboard();
            refreshApiAiHermesSessionOptions();
        });

        Button browse = secondary("Browse");
        browse.setOnAction(e -> browseConfigBasePath());
        Button create = primary("Create Folder Structure");
        create.setOnAction(e -> createTestWeaveConfigFolders());
        Button loadConfig = secondary("Load Config Path");
        loadConfig.setOnAction(e -> loadConfigPathFromExistingFolder());
        Button truncate = secondary("Clear Config Cache");
        truncate.setOnAction(e -> truncateConfigCacheTable());
        Button clearCloudDb = secondary("Clear Firebase Cloud DB");
        clearCloudDb.getStyleClass().add("danger-button");
        clearCloudDb.setOnAction(e -> clearFirebaseCloudDb());
        Button hermesConnect = primary("Hermes Connect");
        hermesConnect.setOnAction(e -> connectApiAiHermesAgent());
        Button hermesDisconnect = secondary("Hermes Disconnect");
        hermesDisconnect.setOnAction(e -> disconnectApiAiHermesAgent());
        Button setupHermes = primary("Setup Hermes Agent");
        setupHermes.setOnAction(e -> setupHermesAgentDocker());

        configHermesLogArea = editor("");
        configHermesLogArea.setEditable(false);
        configHermesLogArea.setWrapText(true);
        configHermesLogArea.setMinHeight(280);

        autoLoadCachedConfigRootPath();

        GridPane form = grid();
        javafx.scene.Node basePathControl = labeled("Base Path", wrapTextFieldWithActions(configBasePathField, browse));
        form.add(basePathControl, 0, 0);
        GridPane.setHgrow(basePathControl, Priority.ALWAYS);
        form.setMaxWidth(1100);

        VBox panel = new VBox(16,
                sectionTitle("Config"),
                form,
                actionRow(new Label("Execution Logs:"), new Label(CONFIG_STORAGE_LOCAL), configExecutionStorageToggle,
                        new Label(CONFIG_STORAGE_CLOUD), configExecutionStorageLabel),
                card("Hermes Agent", new VBox(10,
                        actionRow(new Label("Memory:"), new Label(CONFIG_STORAGE_LOCAL), apiAiAgentStorageToggle,
                                new Label(CONFIG_STORAGE_CLOUD), apiAiAgentStorageLabel),
                        actionRow(hermesConnect, hermesDisconnect),
                        actionRow(new Label("Hermes Session:"), apiAiHermesSessionBox, apiAiHermesConnectionLabel))),
                actionRow(create, loadConfig, truncate, clearCloudDb),
                configStatusLabel,
                configCacheKeyLabel,
                configCacheDbLabel,
                actionRow(setupHermes),
                card("Hermes Setup Logs", configHermesLogArea));
        panel.setMinWidth(0);
        return padded(panel);
    }

    private void browseConfigBasePath() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose TestWeave Config Base Path");
        File selected = chooser.showDialog(stage);
        if (selected != null) {
            configBasePathField.setText(selected.getAbsolutePath());
        }
    }

    private void loadConfigPathFromExistingFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Existing TestWeave Config Base Path");
        File selected = chooser.showDialog(stage);
        if (selected == null) {
            return;
        }
        try {
            Path selectedPath = selected.toPath().toAbsolutePath().normalize();
            Path configRoot = resolveTestWeaveConfigRoot(selectedPath);
            Path sqliteDbPath = configRoot.resolve(CONFIG_CACHE_DB_NAME).toAbsolutePath().normalize();
            if (!Files.exists(sqliteDbPath)) {
                Files.createDirectories(configRoot);
                initializeConfigStorage(sqliteDbPath);
                saveConfigRootPath(configRoot, sqliteDbPath);
                saveConfigRootPath(configRoot, bootstrapConfigCacheDatabasePath());
            }
            initializeConfigStorage(sqliteDbPath);

            String cachedPath = cachedConfigRootPath(sqliteDbPath);
            Path pathToLoad = cachedPath.isBlank()
                    ? configRoot
                    : Path.of(cachedPath).toAbsolutePath().normalize();
            configBasePathField.setText(pathToLoad.toString());
            saveConfigRootPath(pathToLoad, sqliteDbPath);
            saveConfigRootPath(pathToLoad, bootstrapConfigCacheDatabasePath());
            loadExecutionStorageMode(sqliteDbPath);
            updateConfigCachePathLabel();
            applyConfiguredPathsToFields();
            refreshHermesSessionOptions();
            refreshDashboard();
            configStatusLabel.setText("Loaded cached config path: " + pathToLoad);
            showInfo("Config", "Loaded cached config path: " + pathToLoad);
        } catch (Exception e) {
            showError("Load Config Path Failed", e);
        }
    }

    private void createTestWeaveConfigFolders() {
        String basePathText = configBasePathField == null ? "" : configBasePathField.getText().trim();
        if (basePathText.isBlank()) {
            showWarning("Config", "Enter a base path before creating folders.");
            return;
        }
        try {
            Path basePath = Path.of(basePathText).toAbsolutePath().normalize();
            if (Files.exists(basePath) && !Files.isDirectory(basePath)) {
                showWarning("Config", "The selected base path is a file. Choose a folder path.");
                return;
            }
            Path configRoot = resolveTestWeaveConfigRoot(basePath);
            Files.createDirectories(configRoot);
            for (String folder : TESTWEAVE_CONFIG_FOLDERS) {
                Files.createDirectories(configRoot.resolve(folder));
            }
            Files.createDirectories(basePath.resolve("Reports").resolve("TestSuite_Reports"));
            Files.createDirectories(basePath.resolve("Reports").resolve("Perfomance_Reports"));
            Files.createDirectories(basePath.resolve("Reports").resolve("APIValidation_Reports"));
            Files.createDirectories(basePath.resolve("Reports").resolve("DBValidator_Reports"));
            Files.createDirectories(basePath.resolve("Reports").resolve("WebTesting_Reports"));
            Path sqliteDbPath = configCacheDatabasePath(basePath);
            boolean cacheAlreadyExists = Files.exists(sqliteDbPath);
            saveConfigRootPath(configRoot, sqliteDbPath);
            saveConfigRootPath(configRoot, bootstrapConfigCacheDatabasePath());
            initializeConfigStorage(sqliteDbPath);
            persistExecutionStorageMode(sqliteDbPath);
            updateConfigCachePathLabel();
            applyConfiguredPathsToFields();
            refreshHermesSessionOptions();
            String message = "Created TestWeaveConfig folders at " + configRoot
                    + ". SQLite cache " + (cacheAlreadyExists ? "reused" : "created") + " at " + sqliteDbPath;
            configStatusLabel.setText(message);
            showInfo("Config", message);
        } catch (Exception e) {
            showError("Create Config Folders Failed", e);
        }
    }

    private Path resolveTestWeaveConfigRoot(Path basePath) {
        Path fileName = basePath.getFileName();
        if (fileName != null && "TestWeaveConfig".equalsIgnoreCase(fileName.toString())) {
            return basePath;
        }
        return basePath.resolve("TestWeaveConfig");
    }

    private void autoLoadCachedConfigRootPath() {
        if (configBasePathField == null || !configBasePathField.getText().trim().isBlank()) {
            return;
        }
        try {
            Path bootstrapDbPath = bootstrapConfigCacheDatabasePath();
            if (!Files.exists(bootstrapDbPath)) {
                return;
            }
            initializeConfigCacheTable(bootstrapDbPath);
            String cachedPath = cachedConfigRootPath(bootstrapDbPath);
            if (cachedPath.isBlank()) {
                return;
            }
            Path configRoot = resolveTestWeaveConfigRoot(Path.of(cachedPath).toAbsolutePath().normalize());
            Path projectDbPath = configRoot.resolve(CONFIG_CACHE_DB_NAME).toAbsolutePath().normalize();
            if (Files.exists(projectDbPath)) {
                initializeConfigStorage(projectDbPath);
                String projectCachedPath = cachedConfigRootPath(projectDbPath);
                if (!projectCachedPath.isBlank()) {
                    configRoot = resolveTestWeaveConfigRoot(Path.of(projectCachedPath).toAbsolutePath().normalize());
                    projectDbPath = configRoot.resolve(CONFIG_CACHE_DB_NAME).toAbsolutePath().normalize();
                }
                loadExecutionStorageMode(projectDbPath);
            }
            configBasePathField.setText(configRoot.toString());
            updateConfigCachePathLabel();
            applyConfiguredPathsToFields();
            refreshApiAiHermesSessionOptions();
            configStatusLabel.setText("Auto-loaded cached TestWeaveConfig root path: " + configRoot);
        } catch (Exception e) {
            configStatusLabel.setText("Could not auto-load cached config path: " + exceptionMessage(e));
        }
    }

    private void loadCachedConfigRootPath(Path sqliteDbPath) {
        try {
            initializeConfigCacheTable(sqliteDbPath);
            String cachedPath = cachedConfigRootPath(sqliteDbPath);
            if (!cachedPath.isBlank()) {
                configBasePathField.setText(cachedPath);
                configStatusLabel.setText("Loaded cached TestWeaveConfig root path: " + cachedPath);
            }
        } catch (Exception e) {
            configStatusLabel.setText("Config cache unavailable: " + e.getMessage());
        }
    }

    private void saveConfigRootPath(Path configRoot, Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        String sql = "INSERT INTO " + CONFIG_CACHE_TABLE
                + " (system_user_key, root_path, updated_at, execution_storage_mode) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT(system_user_key) DO UPDATE SET root_path = excluded.root_path, updated_at = excluded.updated_at";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, configCacheKey());
            statement.setString(2, configRoot.toAbsolutePath().normalize().toString());
            statement.setString(3, Instant.now().toString());
            statement.setString(4, selectedDashboardStorageMode().name());
            statement.executeUpdate();
        }
    }

    private String cachedConfigRootPath(Path sqliteDbPath) throws Exception {
        String sql = "SELECT root_path FROM " + CONFIG_CACHE_TABLE + " WHERE system_user_key = ?";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, configCacheKey());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("root_path") : "";
            }
        }
    }

    private void truncateConfigCacheTable() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.initOwner(stage);
        confirm.setTitle("Clear Config Cache");
        confirm.setHeaderText("Clear config cache and local execution metrics?");
        confirm.setContentText("This clears cached config paths, saved execution storage mode, local SQLite execution metrics, "
                + "and API AI Agent memory. You will lose local dashboard metrics and API memory stored in this DB. "
                + "It does not delete folders from disk.");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            Path sqliteDbPath = configCacheDatabasePathFromField();
            if (!Files.exists(sqliteDbPath)) {
                showWarning("Config", "No SQLite cache DB exists at " + sqliteDbPath);
                return;
            }
            initializeConfigCacheTable(sqliteDbPath);
            try (Connection connection = openConfigCacheConnection(sqliteDbPath);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM " + CONFIG_CACHE_TABLE);
            }
            dashboardExecutionService.clearSqlite(sqliteDbPath);
            clearApiAiMemorySqlite(sqliteDbPath);
            refreshDashboard();
            configStatusLabel.setText("Config cache, local execution metrics, and API AI memory cleared.");
            showInfo("Config", "Config cache, local execution metrics, and API AI memory cleared.");
        } catch (Exception e) {
            showError("Clear Config Cache Failed", e);
        }
    }

    private void initializeConfigCacheTable(Path sqliteDbPath) throws Exception {
        Path parent = sqliteDbPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + CONFIG_CACHE_TABLE + " ("
                    + "system_user_key TEXT PRIMARY KEY,"
                    + "root_path TEXT NOT NULL,"
                    + "updated_at TEXT NOT NULL"
                    + ")");
            addColumnIfMissing(connection, CONFIG_CACHE_TABLE, "execution_storage_mode", "TEXT NOT NULL DEFAULT 'CLOUD'");
        }
    }

    private void initializeConfigStorage(Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        initializeHermesSessionTable(sqliteDbPath);
        initializeApiAiMemoryTable(sqliteDbPath);
        dashboardExecutionService.initializeSqlite(sqliteDbPath);
    }

    private void initializeApiAiMemoryTable(Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + API_AI_MEMORY_TABLE + " ("
                    + "id TEXT PRIMARY KEY,"
                    + "endpoint TEXT NOT NULL,"
                    + "method TEXT NOT NULL,"
                    + "response_json TEXT NOT NULL,"
                    + "variables_json TEXT NOT NULL,"
                    + "validations_json TEXT NOT NULL,"
                    + "db_mappings_json TEXT NOT NULL,"
                    + "created_at TEXT NOT NULL"
                    + ")");
            addColumnIfMissing(connection, API_AI_MEMORY_TABLE, "action_name", "TEXT");
            addColumnIfMissing(connection, API_AI_MEMORY_TABLE, "provider", "TEXT");
            addColumnIfMissing(connection, API_AI_MEMORY_TABLE, "hermes_session_id", "TEXT");
        }
    }

    private void clearApiAiMemorySqlite(Path sqliteDbPath) throws Exception {
        initializeApiAiMemoryTable(sqliteDbPath);
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + API_AI_MEMORY_TABLE);
        }
    }

    private void initializeHermesSessionTable(Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + HERMES_SESSION_TABLE + " ("
                    + "system_user_key TEXT NOT NULL,"
                    + "session_name TEXT NOT NULL,"
                    + "transcript TEXT NOT NULL,"
                    + "updated_at TEXT NOT NULL,"
                    + "PRIMARY KEY(system_user_key, session_name)"
                    + ")");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "session_id", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "title", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "resume_command", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "transcript_path", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "ai_agent_path", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "container_name", "TEXT");
            addColumnIfMissing(connection, HERMES_SESSION_TABLE, "created_at", "TEXT");
        }
    }

    private void addColumnIfMissing(Connection connection, String tableName, String columnName, String columnDefinition) throws Exception {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + tableName + ")")) {
            while (resultSet.next()) {
                if (columnName.equalsIgnoreCase(resultSet.getString("name"))) {
                    return;
                }
            }
        }
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition);
        }
    }

    private void refreshHermesSessionOptions() {
        if (hermesSessionBox == null) {
            return;
        }
        String selected = hermesSessionBox.getValue();
        List<String> sessions = new ArrayList<>();
        hermesSessionRecords.clear();
        sessions.add(HERMES_NEW_SESSION);
        try {
            Path sqliteDbPath = configCacheDatabasePathFromField();
            if (Files.exists(sqliteDbPath)) {
                initializeHermesSessionTable(sqliteDbPath);
                try (Connection connection = openConfigCacheConnection(sqliteDbPath);
                     PreparedStatement statement = connection.prepareStatement("SELECT session_name, session_id, title, resume_command, transcript_path, ai_agent_path, container_name FROM "
                             + HERMES_SESSION_TABLE + " WHERE system_user_key = ? ORDER BY updated_at DESC")) {
                    statement.setString(1, configCacheKey());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            HermesSessionRecord record = new HermesSessionRecord(
                                    resultSet.getString("session_name"),
                                    resultSet.getString("session_id"),
                                    resultSet.getString("title"),
                                    resultSet.getString("resume_command"),
                                    resultSet.getString("transcript_path"),
                                    resultSet.getString("ai_agent_path"),
                                    resultSet.getString("container_name"));
                            String display = hermesSessionDisplayName(record);
                            sessions.add(display);
                            hermesSessionRecords.put(display, record);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Config may not be loaded yet; keep only New Session.
        }
        hermesSessionBox.setItems(FXCollections.observableArrayList(sessions));
        hermesSessionBox.setValue(selected != null && sessions.contains(selected) ? selected : HERMES_NEW_SESSION);
    }

    private String hermesSessionDisplayName(HermesSessionRecord record) {
        String title = firstNonBlank(record.title(), record.sessionName(), "Hermes Session");
        String sessionId = nullToBlank(record.sessionId());
        if (!sessionId.isBlank() && (title.equals(sessionId) || "Hermes Session".equalsIgnoreCase(title))) {
            return sessionId;
        }
        return sessionId.isBlank() ? title : title + " (" + sessionId + ")";
    }

    private void saveManualHermesSessionId() {
        String sessionId = hermesManualSessionIdField == null ? "" : hermesManualSessionIdField.getText().trim();
        if (sessionId.isBlank()) {
            showWarning("Save Hermes Session", "Enter a Hermes session id first.");
            return;
        }
        if (sessionId.contains(" ")) {
            showWarning("Save Hermes Session", "Session id should not contain spaces.");
            return;
        }
        try {
            Path aiAgentPath = hermesDataDirectory();
            Path sqliteDbPath = configCacheDatabasePathFromField();
            initializeHermesSessionTable(sqliteDbPath);
            String now = Instant.now().toString();
            try (Connection connection = openConfigCacheConnection(sqliteDbPath);
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO " + HERMES_SESSION_TABLE
                         + " (system_user_key, session_name, transcript, updated_at, session_id, title, resume_command, transcript_path, ai_agent_path, container_name, created_at) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                         + "ON CONFLICT(system_user_key, session_name) DO UPDATE SET "
                         + "transcript = excluded.transcript, updated_at = excluded.updated_at, session_id = excluded.session_id, "
                         + "title = excluded.title, resume_command = excluded.resume_command, transcript_path = excluded.transcript_path, "
                         + "ai_agent_path = excluded.ai_agent_path, container_name = excluded.container_name")) {
                statement.setString(1, configCacheKey());
                statement.setString(2, sessionId);
                statement.setString(3, "");
                statement.setString(4, now);
                statement.setString(5, sessionId);
                statement.setString(6, sessionId);
                statement.setString(7, "hermes --resume " + sessionId);
                statement.setString(8, "");
                statement.setString(9, aiAgentPath.toString());
                statement.setString(10, hermesContainerName());
                statement.setString(11, now);
                statement.executeUpdate();
            }
            refreshHermesSessionOptions();
            if (hermesSessionBox != null) {
                hermesSessionBox.setValue(sessionId);
            }
            appendHermesLog("Saved Hermes session id to SQLite cache: " + sessionId);
            showInfo("Hermes Session Saved", "Saved session id: " + sessionId);
        } catch (Exception e) {
            showError("Save Hermes Session Failed", e);
        }
    }

    private String loadHermesSessionTranscript(String sessionName) {
        if (sessionName == null || sessionName.isBlank() || HERMES_NEW_SESSION.equals(sessionName)) {
            return "";
        }
        try {
            Path sqliteDbPath = configCacheDatabasePathFromField();
            initializeHermesSessionTable(sqliteDbPath);
            try (Connection connection = openConfigCacheConnection(sqliteDbPath);
                 PreparedStatement statement = connection.prepareStatement("SELECT transcript FROM "
                         + HERMES_SESSION_TABLE + " WHERE system_user_key = ? AND session_name = ?")) {
                statement.setString(1, configCacheKey());
                statement.setString(2, sessionName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? resultSet.getString("transcript") : "";
                }
            }
        } catch (Exception e) {
            return "[Could not load saved session: " + exceptionMessage(e) + "]" + System.lineSeparator();
        }
    }

    private Connection openConfigCacheConnection(Path sqliteDbPath) throws Exception {
        return DriverManager.getConnection("jdbc:sqlite:" + sqliteDbPath.toAbsolutePath().normalize());
    }

    private Path configCacheDatabasePathFromField() {
        String basePathText = configBasePathField == null ? "" : configBasePathField.getText().trim();
        if (basePathText.isBlank()) {
            throw new IllegalArgumentException("Enter a base path first.");
        }
        return configCacheDatabasePath(Path.of(basePathText).toAbsolutePath().normalize());
    }

    private Path configCacheDatabasePath(Path basePath) {
        return resolveTestWeaveConfigRoot(basePath).resolve(CONFIG_CACHE_DB_NAME).toAbsolutePath().normalize();
    }

    private Path bootstrapConfigCacheDatabasePath() {
        return Path.of(System.getProperty("user.home"), ".testweave", CONFIG_CACHE_DB_NAME)
                .toAbsolutePath()
                .normalize();
    }

    private void updateConfigCachePathLabel() {
        if (configCacheDbLabel == null || configBasePathField == null) {
            return;
        }
        String basePathText = configBasePathField.getText() == null ? "" : configBasePathField.getText().trim();
        if (basePathText.isBlank()) {
            configCacheDbLabel.setText("SQLite Cache: enter a base path to see where the DB will be created.");
            return;
        }
        Path sqliteDbPath = configCacheDatabasePath(Path.of(basePathText).toAbsolutePath().normalize());
        configCacheDbLabel.setText("SQLite Cache: " + sqliteDbPath);
    }

    private StorageMode selectedDashboardStorageMode() {
        return configExecutionStorageToggle != null && !configExecutionStorageToggle.isSelected() ? StorageMode.LOCAL : StorageMode.CLOUD;
    }

    private void updateExecutionStorageToggle() {
        if (configExecutionStorageToggle == null) {
            return;
        }
        boolean cloud = configExecutionStorageToggle.isSelected();
        configExecutionStorageToggle.setText("");
        updateStorageToggleGraphic(configExecutionStorageToggle, cloud);
        configExecutionStorageToggle.setAccessibleText("Execution logs storage mode: " + (cloud ? "Cloud Firebase" : "Local SQLite"));
        if (configExecutionStorageLabel != null) {
            configExecutionStorageLabel.setText(cloud
                    ? "Selected: Cloud - execution logs upload to Firebase."
                    : "Selected: Local - execution logs save to SQLite cache DB.");
        }
    }

    private void updateApiAiAgentToggleLabels() {
        if (apiAiAgentStorageToggle != null) {
            boolean cloud = apiAiAgentStorageToggle.isSelected();
            apiAiAgentStorageToggle.setText("");
            updateStorageToggleGraphic(apiAiAgentStorageToggle, cloud);
            if (apiAiAgentStorageLabel != null) {
                apiAiAgentStorageLabel.setText(cloud
                        ? "Selected: Cloud - Hermes sessions and AI memory save to Firebase."
                        : "Selected: Local - Hermes sessions and AI memory save to SQLite cache DB.");
            }
        }
        refreshApiAiHermesSessionOptions();
        updateApiAiConnectionLabels();
    }

    private void installStorageToggleGraphic(ToggleButton toggle) {
        StackPane track = new StackPane();
        track.getStyleClass().add("storage-switch-track");
        Region knob = new Region();
        knob.getStyleClass().add("storage-switch-knob");
        track.getChildren().add(knob);
        StackPane.setAlignment(knob, Pos.CENTER_LEFT);
        toggle.setGraphic(track);
        toggle.setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);
        toggle.setFocusTraversable(true);
        toggle.getProperties().put("storageSwitchTrack", track);
        toggle.getProperties().put("storageSwitchKnob", knob);
        updateStorageToggleGraphic(toggle, toggle.isSelected());
    }

    private void updateStorageToggleGraphic(ToggleButton toggle, boolean cloud) {
        Object trackObject = toggle.getProperties().get("storageSwitchTrack");
        Object knobObject = toggle.getProperties().get("storageSwitchKnob");
        if (trackObject instanceof StackPane track) {
            track.getStyleClass().remove("storage-switch-cloud");
            if (cloud) {
                track.getStyleClass().add("storage-switch-cloud");
            }
        }
        if (knobObject instanceof Region knob) {
            knob.setTranslateX(cloud ? 28 : 0);
        }
    }

    private void updateApiAiConnectionLabels() {
        String text = activeApiAiHermesSession == null
                ? "Hermes Agent: not connected"
                : "Hermes Agent connected: " + activeApiAiHermesSession.sessionId();
        updateApiAiConnectionLabels(text);
    }

    private void updateApiAiConnectionLabels(String text) {
        Platform.runLater(() -> {
            if (apiAiTesterConnectionLabel != null) {
                apiAiTesterConnectionLabel.setText(text);
            }
            if (apiAiValidationConnectionLabel != null) {
                apiAiValidationConnectionLabel.setText(text);
            }
        });
    }

    private void connectApiAiHermesAgent() {
        if (apiAiHermesConnectionLabel != null) {
            apiAiHermesConnectionLabel.setText("Hermes: connecting...");
        }
        Task<HermesSessionCapture> task = new Task<>() {
            @Override
            protected HermesSessionCapture call() throws Exception {
                HermesSessionRecord selectedRecord = selectedApiAiHermesSessionRecord();
                if (selectedRecord != null && !nullToBlank(selectedRecord.sessionId()).isBlank()) {
                    ensureSelectedHermesSessionReachable(selectedRecord);
                    return new HermesSessionCapture(selectedRecord.sessionId(), selectedRecord.title(), selectedRecord.resumeCommand(),
                            selectedRecord.transcriptPath(), selectedRecord.aiAgentPath(), selectedRecord.containerName(), "");
                }
                return establishApiAiHermesSession();
            }
        };
        task.setOnSucceeded(e -> {
            HermesSessionCapture capture = task.getValue();
            if (capture == null || nullToBlank(capture.sessionId()).isBlank()) {
                apiAiHermesConnectionLabel.setText("Hermes: connection failed - no session id returned");
                showWarning("Hermes Connect", "Hermes connected, but no session id was returned.");
                return;
            }
            activeApiAiHermesSession = new HermesSessionRecord(
                    firstNonBlank(capture.title(), "API AI Hermes Session") + " (" + capture.sessionId() + ")",
                    capture.sessionId(),
                    firstNonBlank(capture.title(), "API AI Hermes Session"),
                    capture.resumeCommand(),
                    capture.transcriptPath(),
                    capture.aiAgentPath(),
                    capture.containerName());
            apiAiConnectedModel = "Hermes Agent (" + capture.sessionId() + ")";
            updateApiAiConnectionLabels("Hermes Agent connected: " + capture.sessionId());
            refreshApiAiHermesSessionOptions();
            selectApiAiHermesSession(capture.sessionId());
            apiAiHermesConnectionLabel.setText("Hermes connected: " + capture.sessionId());
            showInfo("Hermes Connected", "Connected Hermes session: " + capture.sessionId());
        });
        task.setOnFailed(e -> {
            activeApiAiHermesSession = null;
            apiAiHermesConnectionLabel.setText("Hermes: disconnected");
            updateApiAiConnectionLabels();
            Throwable cause = rootCause(task.getException());
            if (isHermesAuthFailure(exceptionMessage(cause))) {
                showWarning("Hermes Re-Authentication Required", exceptionMessage(cause));
            } else {
                showError("Hermes Connect Failed", task.getException());
            }
        });
        start(task);
    }

    private HermesSessionRecord selectedApiAiHermesSessionRecord() {
        String selected = apiAiHermesSessionBox == null ? "" : nullToBlank(apiAiHermesSessionBox.getValue());
        if (selected.isBlank() || HERMES_NEW_SESSION.equals(selected)) {
            return null;
        }
        return apiAiHermesSessionRecords.get(selected);
    }

    private void ensureSelectedHermesSessionReachable(HermesSessionRecord record) throws Exception {
        String image = hermesDockerImage();
        String containerName = firstNonBlank(record.containerName(), hermesContainerName());
        requireDocker(ApiValidatorFxApp.this::appendHermesLog);
        ensureHermesContainerRunning(image, containerName, true);
        startHermesDashboard(containerName);
        waitForHermesDashboard();
        activeApiAiHermesDashboardUrl = discoverHermesDashboardUrl();
        verifyHermesAgent(containerName);
    }

    private HermesSessionCapture establishApiAiHermesSession() throws Exception {
        String dashboardUrl = discoverHermesDashboardUrl();
        String image = hermesDockerImage();
        String containerName = hermesContainerName();
        requireDocker(ApiValidatorFxApp.this::appendHermesLog);
        ensureHermesContainerRunning(image, containerName, true);
        startHermesDashboard(containerName);
        waitForHermesDashboard();
        activeApiAiHermesDashboardUrl = firstNonBlank(dashboardUrl, HERMES_DASHBOARD_URL);
        verifyHermesAgent(containerName);

        Path aiAgentPath = hermesDataDirectory();
        Files.createDirectories(aiAgentPath);
        Path sessionDirectory = aiAgentPath.resolve("Sessions");
        Files.createDirectories(sessionDirectory);
        String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(java.time.LocalDateTime.now());
        Path transcriptPath = sessionDirectory.resolve("api-ai-hermes-connect-" + timestamp + ".log");
        String prompt = """
                Start a new TestWeave API AI Agent session.
                Reply briefly and include the active Hermes session id if your CLI exposes one.
                """;
        List<String> command = List.of("docker", "exec",
                "-w", "/opt/data",
                "-e", "AI_AGENT_PATH=/opt/data",
                "-e", "TESTWEAVE_AI_AGENT_PATH=/opt/data",
                containerName, "hermes",
                "chat",
                "-q", prompt,
                "-Q",
                "--pass-session-id",
                "--source", "tool");
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        boolean completed = process.waitFor(120, java.util.concurrent.TimeUnit.SECONDS);
        if (!completed) {
            process.destroyForcibly();
            throw new IllegalStateException("Hermes session creation timed out after 120 seconds.");
        }
        String transcript = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        Files.writeString(transcriptPath, transcript, StandardCharsets.UTF_8);
        HermesSessionCapture capture = parseHermesSessionTranscript(transcriptPath, containerName, aiAgentPath);
        if (nullToBlank(capture.sessionId()).isBlank()) {
            throw new IllegalStateException("Hermes connected but did not return a session id. Transcript: "
                    + shorten(transcript, 1000));
        }
        if (process.exitValue() != 0) {
            if (isHermesAuthFailure(transcript)) {
                openHermesOpenAiCodexReauth(containerName);
                throw new IllegalStateException("Hermes created session " + capture.sessionId()
                        + ", but its OpenAI Codex authentication token is invalidated. "
                        + "A Hermes re-authentication terminal was opened. Complete sign-in there, then click Hermes Connect again.");
            }
            throw new IllegalStateException("Hermes created session " + capture.sessionId()
                    + " but the model/provider returned an error. " + shorten(transcript, 1000));
        }
        saveApiAiHermesSession(capture);
        appendHermesLog("API AI Hermes session connected at " + activeApiAiHermesDashboardUrl + ": " + capture.sessionId());
        return capture;
    }

    private boolean isHermesAuthFailure(String text) {
        String value = nullToBlank(text).toLowerCase();
        return value.contains("token_invalidated")
                || value.contains("authentication token has been invalidated")
                || value.contains("error code: 401")
                || value.contains("status': 401")
                || value.contains("\"status\":401");
    }

    private void openHermesOpenAiCodexReauth(String containerName) {
        appendHermesLog("Hermes OpenAI Codex token is invalidated. Opening re-authentication terminal.");
        try {
            try {
                runDockerCommand(List.of("exec", containerName, "hermes", "auth", "logout", "openai-codex"));
                appendHermesLog("Cleared invalid Hermes OpenAI Codex auth state.");
            } catch (Exception e) {
                appendHermesLog("Could not clear old Hermes auth state: " + exceptionMessage(e));
            }
            launchHermesCli(containerName, "auth add openai-codex --type oauth");
        } catch (Exception e) {
            appendHermesLog("Could not open Hermes re-authentication terminal: " + exceptionMessage(e));
        }
    }

    private void disconnectApiAiHermesAgent() {
        String sessionId = activeApiAiHermesSession == null ? "" : nullToBlank(activeApiAiHermesSession.sessionId());
        activeApiAiHermesSession = null;
        activeApiAiHermesDashboardUrl = "";
        apiAiConnectedModel = "Hermes Agent";
        if (apiAiHermesConnectionLabel != null) {
            apiAiHermesConnectionLabel.setText("Hermes: disconnected");
        }
        updateApiAiConnectionLabels();
        appendHermesLog("API AI Hermes session disconnected" + (sessionId.isBlank() ? "." : ": " + sessionId));
    }

    private void disconnectApiAiHermesAgentOnExit() {
        activeApiAiHermesSession = null;
        activeApiAiHermesDashboardUrl = "";
    }

    private String discoverHermesDashboardUrl() {
        if (isUrlReachable(HERMES_DASHBOARD_URL)) {
            return HERMES_DASHBOARD_URL;
        }
        try {
            ProcessResult port = runCommand(List.of("docker", "port", hermesContainerName(), "9119/tcp"));
            String output = firstOutputLine(port.output);
            Matcher matcher = Pattern.compile("(?:0\\.0\\.0\\.0|127\\.0\\.0\\.1|localhost|\\[::\\]|::):([0-9]+)").matcher(output);
            if (matcher.find()) {
                String discovered = "http://127.0.0.1:" + matcher.group(1);
                if (isUrlReachable(discovered)) {
                    return discovered;
                }
            }
        } catch (Exception ignored) {
            // Fall back to the default local Hermes dashboard URL.
        }
        return HERMES_DASHBOARD_URL;
    }

    private boolean isUrlReachable(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() > 0 && response.statusCode() < 500;
        } catch (Exception e) {
            return false;
        }
    }

    private Path dashboardExecutionSqlitePath(StorageMode storageMode) {
        if (storageMode != StorageMode.LOCAL) {
            return null;
        }
        return configCacheDatabasePathFromField();
    }

    private void persistExecutionStorageMode() {
        if (configBasePathField == null || configBasePathField.getText() == null || configBasePathField.getText().trim().isBlank()) {
            return;
        }
        try {
            persistExecutionStorageMode(configCacheDatabasePathFromField());
        } catch (Exception e) {
            configStatusLabel.setText("Could not save execution log storage mode: " + exceptionMessage(e));
        }
    }

    private void persistExecutionStorageMode(Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        String sql = "UPDATE " + CONFIG_CACHE_TABLE + " SET execution_storage_mode = ?, updated_at = ? WHERE system_user_key = ?";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedDashboardStorageMode().name());
            statement.setString(2, Instant.now().toString());
            statement.setString(3, configCacheKey());
            statement.executeUpdate();
        }
    }

    private void loadExecutionStorageMode(Path sqliteDbPath) throws Exception {
        initializeConfigCacheTable(sqliteDbPath);
        String sql = "SELECT execution_storage_mode FROM " + CONFIG_CACHE_TABLE + " WHERE system_user_key = ?";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, configCacheKey());
            try (ResultSet resultSet = statement.executeQuery()) {
                String mode = resultSet.next() ? resultSet.getString("execution_storage_mode") : StorageMode.CLOUD.name();
                boolean cloud = !StorageMode.LOCAL.name().equalsIgnoreCase(mode);
                if (configExecutionStorageToggle != null) {
                    configExecutionStorageToggle.setSelected(cloud);
                    updateExecutionStorageToggle();
                }
            }
        }
    }

    private void clearFirebaseCloudDb() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.initOwner(stage);
        confirm.setTitle("Clear Firebase Cloud DB");
        confirm.setHeaderText("Clear all cloud execution metrics?");
        confirm.setContentText("This permanently deletes all dashboard execution logs from Firebase under testweave-dashboard/executions.");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                dashboardExecutionService.clearFirebase();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            configStatusLabel.setText("Firebase cloud dashboard execution logs cleared.");
            if (selectedDashboardStorageMode() == StorageMode.CLOUD) {
                refreshDashboard();
            }
            showInfo("Firebase Cloud DB", "Firebase cloud dashboard execution logs cleared.");
        });
        task.setOnFailed(e -> showError("Clear Firebase Cloud DB Failed", task.getException()));
        start(task);
    }

    private void applyConfiguredPathsToFields() {
        Path expectedResponseDir = configuredFolder("API", "ExpectedResponse");
        if (expectedResponseDir != null && expectedJsonPathField != null && isBlankOrDirectory(expectedJsonPathField.getText())) {
            expectedJsonPathField.setText(expectedResponseDir.toString());
        }
        Path variablesDir = configuredFolder("Variables");
        if (variablesDir != null && variablesPathField != null && isBlankOrDirectory(variablesPathField.getText())) {
            variablesPathField.setText(variablesDir.toString());
        }
        updateHermesAiAgentPathField();
    }

    private void updateHermesAiAgentPathField() {
        if (hermesAiAgentPathField == null) {
            return;
        }
        Path aiAgentDir = configuredAiAgentDirectory();
        hermesAiAgentPathField.setText(aiAgentDir == null ? "" : aiAgentDir.toString());
    }

    private Path configuredAiAgentDirectory() {
        return configuredFolder("AIAgent");
    }

    private boolean isBlankOrDirectory(String text) {
        if (text == null || text.isBlank()) {
            return true;
        }
        try {
            return Files.isDirectory(Path.of(text));
        } catch (Exception ignored) {
            return false;
        }
    }

    private Path configuredFolder(String first, String... more) {
        Path root = configuredRootPath();
        if (root == null) {
            return null;
        }
        Path path = root.resolve(first);
        for (String part : more) {
            path = path.resolve(part);
        }
        return path.toAbsolutePath().normalize();
    }

    private Path configuredRootPath() {
        if (configBasePathField == null || configBasePathField.getText() == null || configBasePathField.getText().trim().isBlank()) {
            return null;
        }
        return resolveTestWeaveConfigRoot(Path.of(configBasePathField.getText().trim()).toAbsolutePath().normalize())
                .toAbsolutePath()
                .normalize();
    }

    private String configCacheKey() {
        return systemName() + "_" + System.getProperty("user.name", "unknown");
    }

    private String systemName() {
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null || computerName.isBlank()) {
            computerName = System.getenv("HOSTNAME");
        }
        return computerName == null || computerName.isBlank() ? "unknown-system" : computerName.trim();
    }

    private TableView<Map<String, String>> createTestSuiteStepsTable() {
        TableView<Map<String, String>> table = new TableView<>(testSuiteRows);
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Map<String, String>, Boolean> runColumn = new TableColumn<>("Run");
        runColumn.setCellValueFactory(data -> {
            SimpleBooleanProperty selected = new SimpleBooleanProperty(isSelected(data.getValue()));
            selected.addListener((observable, oldValue, newValue) ->
                    data.getValue().put("selected", Boolean.TRUE.equals(newValue) ? "true" : "false"));
            return selected;
        });
        runColumn.setCellFactory(CheckBoxTableCell.forTableColumn(runColumn));
        runColumn.setEditable(true);
        runColumn.setMinWidth(70);
        table.getColumns().add(runColumn);

        table.getColumns().add(stringColumn("Suite", "suite"));
        table.getColumns().add(stringColumn("Case", "case"));
        table.getColumns().add(stringColumn("Step", "step"));

        TableColumn<Map<String, String>, String> executionModeColumn = stringColumn("Execution Mode", "executionMode");
        executionModeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Sequential", "Parallel"));
        executionModeColumn.setOnEditCommit(event -> {
            String mode = event.getNewValue() == null ? "Sequential" : event.getNewValue();
            event.getRowValue().put("executionMode", mode);
            table.refresh();
        });
        executionModeColumn.setEditable(true);
        executionModeColumn.setMinWidth(140);
        table.getColumns().add(executionModeColumn);

        table.getColumns().add(stringColumn("Type", "type"));
        table.getColumns().add(stringColumn("Details", "details"));
        table.getColumns().add(stringColumn("Status", "status"));
        return table;
    }

    private void addSelectedTestSuiteBuilderItem() {
        String selection = testSuiteBuilderAddTypeBox == null ? "Test Suite" : testSuiteBuilderAddTypeBox.getValue();
        if ("Test Suite".equals(selection)) {
            addCurrentSuiteToBuilderTree();
        } else if ("Test Case".equals(selection)) {
            addCurrentCaseToBuilderTree();
        } else {
            addSelectedStepsToBuilderTree();
        }
    }

    private void addCurrentSuiteToBuilderTree() {
        String suite = testSuiteNameField == null ? "" : testSuiteNameField.getText().trim();
        if (suite.isBlank()) {
            promptForRunnerField("Add Test Suite", "Test Suite name", testSuiteNameField);
            suite = testSuiteNameField == null ? "" : testSuiteNameField.getText().trim();
        }
        if (suite.isBlank()) {
            return;
        }
        List<Map<String, String>> rows = readBuilderRowsForSuite(suite);
        if (rows.isEmpty()) {
            rows = List.of(row("suite", suite, "case", "", "step", "", "type", "Suite", "details", "", "status", "Ready"));
        }
        addRowsToBuilderTree(rows);
        testSuiteRunnerStatusLabel.setText("Added test suite to Suite Builder tree.");
    }

    private void addCurrentCaseToBuilderTree() {
        String suite = testSuiteNameField == null ? "" : testSuiteNameField.getText().trim();
        String testCase = currentTestCaseName();
        if (testCase.isBlank()) {
            promptForTestCaseName();
            testCase = currentTestCaseName();
        }
        if (testCase.isBlank()) {
            return;
        }
        List<Map<String, String>> rows = readBuilderRowsForCase(suite, testCase);
        if (rows.isEmpty()) {
            rows = List.of(row("suite", suite, "case", testCase, "step", "", "type", "Test Case", "details", "", "status", "Ready"));
        }
        addRowsToBuilderTree(rows);
        testSuiteRunnerStatusLabel.setText("Added test case to Suite Builder tree.");
    }

    private void addSelectedStepsToBuilderTree() {
        List<Map<String, String>> selectedSteps = new ArrayList<>();
        for (Map<String, String> row : testSuiteRows) {
            if (isSelected(row)) {
                selectedSteps.add(new LinkedHashMap<>(row));
            }
        }
        if (selectedSteps.isEmpty()) {
            showWarning("Suite Builder", "Select at least one test step before adding Test Step to the tree.");
            return;
        }
        addRowsToBuilderTree(selectedSteps);
        testSuiteRunnerStatusLabel.setText("Added " + selectedSteps.size() + " test step(s) to Suite Builder tree.");
    }

    private List<Map<String, String>> readBuilderRowsForSuite(String suite) {
        Path workbookPath = selectedWorkbookPath();
        if (workbookPath == null) {
            return copyRowsForSuite(testSuiteRows, suite);
        }
        try {
            return readAllWorkbookRowsForBuilder(workbookPath, suite, "");
        } catch (Exception e) {
            testSuiteRunnerStatusLabel.setText("Could not read suite tree rows: " + e.getMessage());
            return copyRowsForSuite(testSuiteRows, suite);
        }
    }

    private List<Map<String, String>> readBuilderRowsForCase(String suite, String testCase) {
        return copyRowsForCase(testSuiteRows, suite, testCase);
    }

    private List<Map<String, String>> readAllWorkbookRowsForBuilder(Path workbookPath, String suiteFilter,
                                                                     String caseFilter) throws Exception {
        Map<String, byte[]> entries = readWorkbookEntries(workbookPath);
        List<String> sharedStrings = readSharedStrings(entries);
        List<Map<String, String>> rows = new ArrayList<>();
        String workbookSuite = workbookNameWithoutExtension(workbookPath.getFileName().toString());
        for (WorkbookSheet sheet : readWorkbookSheets(entries)) {
            if (!caseFilter.isBlank() && !Objects.equals(sheet.name, caseFilter)) {
                continue;
            }
            byte[] sheetBytes = entries.get(sheet.path);
            if (sheetBytes == null) {
                continue;
            }
            for (Map<String, String> step : readTestSuiteRunnerSteps(sheetBytes, sharedStrings)) {
                Map<String, String> tableRow = workbookStepToTableRow(step);
                String rowSuite = tableRow.getOrDefault("suite", "").isBlank() ? workbookSuite : tableRow.getOrDefault("suite", "");
                tableRow.put("suite", suiteFilter.isBlank() ? rowSuite : suiteFilter);
                tableRow.put("case", sheet.name);
                boolean suiteMatches = suiteFilter.isBlank() || Objects.equals(tableRow.getOrDefault("suite", ""), suiteFilter)
                        || Objects.equals(workbookSuite, suiteFilter);
                if (suiteMatches) {
                    rows.add(tableRow);
                }
            }
            if (rows.stream().noneMatch(row -> Objects.equals(row.getOrDefault("case", ""), sheet.name))
                    && !suiteFilter.isBlank()) {
                rows.add(row("suite", suiteFilter, "case", sheet.name, "step", "", "type", "Test Case",
                        "details", "", "status", "Ready"));
            }
        }
        return rows;
    }

    private List<Map<String, String>> copyRowsForSuite(List<Map<String, String>> sourceRows, String suite) {
        List<Map<String, String>> rows = new ArrayList<>();
        for (Map<String, String> row : sourceRows) {
            if (suite.isBlank() || Objects.equals(row.getOrDefault("suite", ""), suite)) {
                rows.add(new LinkedHashMap<>(row));
            }
        }
        return rows;
    }

    private List<Map<String, String>> copyRowsForCase(List<Map<String, String>> sourceRows, String suite, String testCase) {
        List<Map<String, String>> rows = new ArrayList<>();
        for (Map<String, String> row : sourceRows) {
            boolean suiteMatches = suite.isBlank() || Objects.equals(row.getOrDefault("suite", ""), suite);
            boolean caseMatches = Objects.equals(row.getOrDefault("case", ""), testCase);
            if (suiteMatches && caseMatches) {
                rows.add(new LinkedHashMap<>(row));
            }
        }
        return rows;
    }

    private void addRowsToBuilderTree(List<Map<String, String>> rows) {
        for (Map<String, String> row : rows) {
            addBuilderTreeRow(row);
        }
        refreshSuiteBuilderTreeView();
    }

    private void addBuilderTreeRow(Map<String, String> row) {
        String suite = row.getOrDefault("suite", "");
        String testCase = row.getOrDefault("case", "");
        String step = row.getOrDefault("step", "");
        for (Map<String, String> existing : suiteBuilderTreeRows) {
            if (Objects.equals(existing.getOrDefault("suite", ""), suite)
                    && Objects.equals(existing.getOrDefault("case", ""), testCase)
                    && Objects.equals(existing.getOrDefault("step", ""), step)) {
                return;
            }
        }
        suiteBuilderTreeRows.add(new LinkedHashMap<>(row));
    }

    private void promptForRunnerField(String title, String prompt, TextField target) {
        TextInputDialog dialog = new TextInputDialog(target == null ? "" : target.getText());
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(prompt + ":");
        dialog.showAndWait().ifPresent(value -> {
            String trimmed = value == null ? "" : value.trim();
            if (trimmed.isBlank()) {
                showWarning(title, "Enter a value before adding.");
                return;
            }
            target.setText(trimmed);
            propagateTestSuiteContext();
        });
    }

    private void promptForTestCaseName() {
        TextInputDialog dialog = new TextInputDialog(currentTestCaseName());
        dialog.setTitle("Add Test Case");
        dialog.setHeaderText(null);
        dialog.setContentText("Test Case name:");
        dialog.showAndWait().ifPresent(value -> {
            String trimmed = value == null ? "" : value.trim();
            if (trimmed.isBlank()) {
                showWarning("Add Test Case", "Enter a value before adding.");
                return;
            }
            setCurrentTestCaseName(trimmed);
            if (!testCaseNameField.getItems().contains(trimmed)) {
                testCaseNameField.getItems().add(trimmed);
            }
            propagateTestSuiteContext();
        });
    }

    private void addManualTestSuiteStep() {
        String suite = testSuiteNameField == null ? "" : testSuiteNameField.getText().trim();
        String testCase = currentTestCaseName();
        testSuiteRows.add(row("selected", "true", "suite", suite, "case", testCase,
                "step", String.valueOf(testSuiteRows.size() + 1), "executionMode", "Sequential",
                "type", "Manual", "details", "Describe this step", "status", "Ready"));
        testSuiteStepsTable.refresh();
        testSuiteRunnerStatusLabel.setText("Manual test step added.");
    }

    private String currentTestCaseName() {
        if (testCaseNameField == null) {
            return "";
        }
        String editorText = testCaseNameField.isEditable() && testCaseNameField.getEditor() != null
                ? testCaseNameField.getEditor().getText()
                : "";
        String value = editorText == null || editorText.isBlank() ? testCaseNameField.getValue() : editorText;
        return value == null ? "" : value.trim();
    }

    private void setCurrentTestCaseName(String testCase) {
        if (testCaseNameField == null) {
            return;
        }
        String value = testCase == null ? "" : testCase.trim();
        testCaseNameField.setValue(value);
        if (testCaseNameField.getEditor() != null) {
            testCaseNameField.getEditor().setText(value);
        }
    }

    private void setTestCaseOptions(List<String> testCases, String selectedTestCase) {
        if (testCaseNameField == null) {
            return;
        }
        List<String> cleanCases = testCases.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
        testCaseNameField.getItems().setAll(cleanCases);
        String selected = selectedTestCase == null || selectedTestCase.isBlank()
                ? (cleanCases.isEmpty() ? "" : cleanCases.get(0))
                : selectedTestCase.trim();
        setCurrentTestCaseName(selected);
    }

    private void onTestSuiteCaseSelected(String selectedTestCase) {
        if (testCaseNameField != null && testCaseNameField.getEditor() != null
                && selectedTestCase != null
                && !Objects.equals(testCaseNameField.getEditor().getText(), selectedTestCase)) {
            testCaseNameField.getEditor().setText(selectedTestCase);
        }
        propagateTestSuiteContext();
        Path workbookPath = selectedWorkbookPath();
        if (workbookPath != null && selectedTestCase != null && !selectedTestCase.isBlank()) {
            refreshTestSuiteRunnerSteps(workbookPath);
        }
    }

    private void openTestSuiteBuilderCanvas() {
        try {
            if (suiteBuilderStage != null && suiteBuilderStage.isShowing()) {
                refreshSuiteBuilderTreeView();
                renderSuiteBuilderCanvas();
                suiteBuilderStage.toFront();
                suiteBuilderStage.requestFocus();
                return;
            }
            openTestSuiteBuilderCanvasWindow();
        } catch (Throwable e) {
            showError("Suite Builder Failed", e);
        }
    }

    private void openTestSuiteBuilderCanvasWindow() {
        Pane canvas = new Pane();
        suiteBuilderCanvas = canvas;
        canvas.getStyleClass().add("builder-canvas");
        int columns = 4;
        int rows = Math.max(1, (int) Math.ceil(suiteBuilderCanvasRows.size() / (double) columns));
        canvas.setPrefSize(Math.max(1040, columns * 270 + 60), Math.max(620, rows * 180 + 80));
        enableSuiteBuilderCanvasDrop(canvas);

        Label title = sectionTitle("Test Suite Builder Canvas");
        Label context = new Label(builderCanvasContext());
        context.getStyleClass().add("muted");
        Button refresh = secondary("Refresh Canvas");
        Button deleteNode = secondary("Delete");
        Button connect = secondary("Connect");
        Button reverse = secondary("Reverse Arrows");
        Button save = secondary("Save");
        Button importFlow = secondary("Import");
        Button deployFlow = secondary("Deploy GitHub");
        Button runFlow = primary("Run");
        Button stopFlow = secondary("Stop");
        stopFlow.getStyleClass().add("danger-button");
        Button openReport = secondary("Open Report");
        Button clear = secondary("Clear");
        Button close = secondary("Close");
        FlowPane toolbar = spacedActionRow(title, context, refresh, deleteNode, connect, reverse, save, importFlow, deployFlow,
                runFlow, stopFlow, openReport, clear, close);

        ScrollPane canvasScroll = new ScrollPane(canvas);
        canvasScroll.setFitToWidth(false);
        canvasScroll.setFitToHeight(false);
        canvasScroll.setPannable(true);
        canvasScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        canvasScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        canvasScroll.getStyleClass().add("workspace-scroll");

        suiteBuilderTreeView = createSuiteBuilderTreeView();
        VBox treePane = new VBox(10, sectionTitle("Builder Tree"), suiteBuilderTreeView);
        treePane.getStyleClass().add("builder-tree-pane");
        treePane.setPrefWidth(330);
        treePane.setMinWidth(260);
        VBox.setVgrow(suiteBuilderTreeView, Priority.ALWAYS);

        SplitPane builderContent = new SplitPane(treePane, canvasScroll);
        builderContent.setDividerPositions(0.28);

        BorderPane root = new BorderPane(builderContent);
        root.setTop(toolbar);
        BorderPane.setMargin(toolbar, new Insets(12));
        root.setStyle("-fx-background-color: " + APP_BACKGROUND + ";");

        suiteBuilderStage = new Stage();
        suiteBuilderStage.setTitle(APP_NAME + " - Test Suite Builder");
        suiteBuilderStage.initOwner(stage);
        Scene scene = new Scene(root, 1180, 720);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        suiteBuilderStage.setScene(scene);
        loadApplicationIcon(suiteBuilderStage);
        deleteNode.setOnAction(e -> deleteSelectedSuiteBuilderCanvasNode());
        connect.setOnAction(e -> connectSelectedSuiteBuilderCanvasNodes());
        reverse.setOnAction(e -> reverseSelectedSuiteBuilderConnection());
        save.setOnAction(e -> saveSuiteBuilderFlowTemplate());
        importFlow.setOnAction(e -> importSuiteBuilderFlowTemplate());
        deployFlow.setOnAction(e -> deploySuiteBuilderFlowToGithubActions());
        runFlow.setOnAction(e -> runSuiteBuilderFlow());
        stopFlow.setOnAction(e -> stopTestSuiteRunnerExecution());
        openReport.setOnAction(e -> openTestSuiteReport());
        clear.setOnAction(e -> clearSuiteBuilderTree());
        close.setOnAction(e -> suiteBuilderStage.close());
        refresh.setOnAction(e -> {
            renderSuiteBuilderCanvas();
            refreshSuiteBuilderTreeView();
        });
        suiteBuilderStage.setOnHidden(e -> {
            suiteBuilderStage = null;
            suiteBuilderTreeView = null;
            suiteBuilderCanvas = null;
        });

        renderSuiteBuilderCanvas();
        suiteBuilderStage.show();
    }

    private TreeView<BuilderTreeNode> createSuiteBuilderTreeView() {
        TreeView<BuilderTreeNode> treeView = new TreeView<>();
        treeView.setShowRoot(true);
        treeView.setRoot(buildSuiteBuilderTreeRoot());
        treeView.setCellFactory(view -> createSuiteBuilderTreeCell());
        return treeView;
    }

    private void refreshSuiteBuilderTreeView() {
        if (suiteBuilderTreeView != null) {
            suiteBuilderTreeView.setRoot(buildSuiteBuilderTreeRoot());
        }
    }

    private void clearSuiteBuilderTree() {
        suiteBuilderTreeRows.clear();
        suiteBuilderCanvasRows.clear();
        suiteBuilderCanvasConnections.clear();
        suiteBuilderDragItems.clear();
        suiteBuilderReverseFlow = false;
        suiteBuilderSelectedCanvasIndex = -1;
        suiteBuilderConnectionSourceIndex = -1;
        suiteBuilderSelectedConnectionIndex = -1;
        refreshSuiteBuilderTreeView();
        renderSuiteBuilderCanvas();
        if (testSuiteRunnerStatusLabel != null) {
            testSuiteRunnerStatusLabel.setText("Suite Builder tree cleared.");
        }
    }

    private TreeItem<BuilderTreeNode> buildSuiteBuilderTreeRoot() {
        TreeItem<BuilderTreeNode> root = new TreeItem<>(BuilderTreeNode.root());
        root.setExpanded(true);
        Map<String, Map<String, List<Map<String, String>>>> suites = new LinkedHashMap<>();
        for (Map<String, String> row : suiteBuilderTreeRows) {
            String suite = row.getOrDefault("suite", "").isBlank() ? "Untitled Suite" : row.getOrDefault("suite", "");
            String testCase = row.getOrDefault("case", "").isBlank() ? "Unassigned Test Case" : row.getOrDefault("case", "");
            suites.computeIfAbsent(suite, key -> new LinkedHashMap<>())
                    .computeIfAbsent(testCase, key -> new ArrayList<>())
                    .add(row);
        }

        for (Map.Entry<String, Map<String, List<Map<String, String>>>> suiteEntry : suites.entrySet()) {
            List<Map<String, String>> suiteRows = new ArrayList<>();
            for (List<Map<String, String>> caseRowsForSuite : suiteEntry.getValue().values()) {
                for (Map<String, String> row : caseRowsForSuite) {
                    suiteRows.add(new LinkedHashMap<>(row));
                }
            }
            TreeItem<BuilderTreeNode> suiteItem = new TreeItem<>(BuilderTreeNode.suite(suiteEntry.getKey(), suiteRows));
            suiteItem.setExpanded(true);
            for (Map.Entry<String, List<Map<String, String>>> caseEntry : suiteEntry.getValue().entrySet()) {
                List<Map<String, String>> caseRows = new ArrayList<>();
                for (Map<String, String> row : caseEntry.getValue()) {
                    caseRows.add(new LinkedHashMap<>(row));
                }
                TreeItem<BuilderTreeNode> caseItem = new TreeItem<>(BuilderTreeNode.testCase(caseEntry.getKey(), caseRows));
                caseItem.setExpanded(true);
                for (Map<String, String> row : caseEntry.getValue()) {
                    String step = row.getOrDefault("step", "");
                    if (step.isBlank()) {
                        continue;
                    }
                    caseItem.getChildren().add(new TreeItem<>(BuilderTreeNode.step(new LinkedHashMap<>(row))));
                }
                suiteItem.getChildren().add(caseItem);
            }
            root.getChildren().add(suiteItem);
        }
        if (root.getChildren().isEmpty()) {
            root.getChildren().add(new TreeItem<>(BuilderTreeNode.placeholder("Use Add to place suites, cases, or steps here")));
        }
        return root;
    }

    private TreeCell<BuilderTreeNode> createSuiteBuilderTreeCell() {
        TreeCell<BuilderTreeNode> cell = new TreeCell<>() {
            @Override
            protected void updateItem(BuilderTreeNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName);
            }
        };
        cell.setOnDragDetected(event -> {
            BuilderTreeNode node = cell.getItem();
            if (node == null || !node.draggable()) {
                return;
            }
            Dragboard dragboard = cell.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(node.id);
            dragboard.setContent(content);
            suiteBuilderDragItems.put(node.id, node);
            event.consume();
        });
        return cell;
    }

    private void enableSuiteBuilderCanvasDrop(Pane canvas) {
        canvas.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString() && suiteBuilderDragItems.containsKey(dragboard.getString())) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        canvas.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                BuilderTreeNode node = suiteBuilderDragItems.get(dragboard.getString());
                if (node != null && node.draggable()) {
                    addBuilderCanvasFlowNode(node, event.getX(), event.getY());
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void addBuilderCanvasFlowNode(BuilderTreeNode node, double x, double y) {
        Map<String, String> flowRow = new LinkedHashMap<>();
        if ("STEP".equals(node.kind) && !node.rows.isEmpty()) {
            flowRow.putAll(node.rows.get(0));
            flowRow.put("flowLabel", flowRow.getOrDefault("step", node.displayName));
            flowRow.put("flowKind", "STEP");
            flowRow.put("flowRows", new JSONArray(List.of(new JSONObject(flowRow))).toString());
        } else {
            flowRow.put("suite", node.suite);
            flowRow.put("case", node.testCase);
            flowRow.put("step", node.displayName);
            flowRow.put("flowLabel", node.displayName);
            flowRow.put("flowKind", node.kind);
            flowRow.put("type", "SUITE".equals(node.kind) ? "Test Suite" : "Test Case");
            flowRow.put("executionMode", "Sequential");
            long stepCount = node.rows.stream().filter(row -> !row.getOrDefault("step", "").isBlank()).count();
            flowRow.put("details", stepCount + " test step(s)");
            flowRow.put("status", "Ready");
            flowRow.put("flowRows", new JSONArray(node.rows).toString());
        }
        flowRow.put("flowStatus", "Ready");
        flowRow.put("flowProgress", "0");
        flowRow.put("canvasX", String.valueOf(Math.max(20, x)));
        flowRow.put("canvasY", String.valueOf(Math.max(20, y)));
        suiteBuilderCanvasRows.add(flowRow);
        int newIndex = suiteBuilderCanvasRows.size() - 1;
        if (newIndex > 0) {
            addSuiteBuilderConnection(newIndex - 1, newIndex);
        }
        renderSuiteBuilderCanvas();
    }

    private void renderSuiteBuilderCanvas() {
        if (suiteBuilderCanvas == null) {
            return;
        }
        suiteBuilderCanvas.getChildren().clear();
        ensureSuiteBuilderCanvasConnections();
        double maxX = 0;
        for (Map<String, String> row : suiteBuilderCanvasRows) {
            maxX = Math.max(maxX, canvasCoordinate(row.get("canvasX"), 0));
        }
        double width = Math.max(2400, Math.max(suiteBuilderCanvasRows.size() * 320 + 240, maxX + 520));
        suiteBuilderCanvas.setMinSize(width, 900);
        suiteBuilderCanvas.setPrefSize(width, 900);
        populateBuilderCanvas(suiteBuilderCanvas, suiteBuilderCanvasRows, 4);
    }

    private void ensureSuiteBuilderCanvasConnections() {
        if (!suiteBuilderCanvasConnections.isEmpty() || suiteBuilderCanvasRows.size() < 2) {
            return;
        }
        suiteBuilderCanvasConnections.setAll(defaultSuiteBuilderConnections(suiteBuilderCanvasRows.size()));
    }

    private void saveSuiteBuilderFlowTemplate() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("testweave-suite-builder-flow.json");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TestWeave Flow Template", "*.json"));
        File file = chooser.showSaveDialog(suiteBuilderStage == null ? stage : suiteBuilderStage);
        if (file == null) {
            return;
        }
        JSONObject template = new JSONObject()
                .put("format", "TestWeave Suite Builder Flow")
                .put("version", 1)
                .put("testSuite", testSuiteNameField == null ? "" : testSuiteNameField.getText())
                .put("testCase", currentTestCaseName())
                .put("reverseFlow", suiteBuilderReverseFlow)
                .put("treeRows", new JSONArray(suiteBuilderTreeRows))
                .put("canvasRows", new JSONArray(suiteBuilderCanvasRows))
                .put("connections", new JSONArray(suiteBuilderCanvasConnections));
        try {
            Files.writeString(file.toPath(), template.toString(2), StandardCharsets.UTF_8);
            testSuiteRunnerStatusLabel.setText("Suite Builder flow saved: " + file.getName());
        } catch (Exception e) {
            showError("Save Suite Builder Flow Failed", e);
        }
    }

    private void importSuiteBuilderFlowTemplate() {
        File file = chooseOpenFile("TestWeave Flow Template", "*.json");
        if (file == null) {
            return;
        }
        try {
            JSONObject template = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            suiteBuilderReverseFlow = false;
            suiteBuilderTreeRows.setAll(jsonArrayToRows(template.optJSONArray("treeRows")));
            suiteBuilderCanvasRows.setAll(jsonArrayToRows(template.optJSONArray("canvasRows")));
            suiteBuilderCanvasConnections.setAll(jsonArrayToRows(template.optJSONArray("connections")));
            refreshSuiteBuilderTreeView();
            renderSuiteBuilderCanvas();
            testSuiteRunnerStatusLabel.setText("Suite Builder flow imported: " + file.getName());
        } catch (Exception e) {
            showError("Import Suite Builder Flow Failed", e);
        }
    }

    private List<Map<String, String>> jsonArrayToRows(JSONArray rowsJson) {
        List<Map<String, String>> rows = new ArrayList<>();
        if (rowsJson == null) {
            return rows;
        }
        for (int i = 0; i < rowsJson.length(); i++) {
            JSONObject object = rowsJson.optJSONObject(i);
            if (object == null) {
                continue;
            }
            Map<String, String> row = new LinkedHashMap<>();
            for (String key : object.keySet()) {
                row.put(key, String.valueOf(object.opt(key)));
            }
            rows.add(row);
        }
        return rows;
    }

    private void runSuiteBuilderFlow() {
        if (suiteBuilderCanvasRows.isEmpty()) {
            showWarning("Suite Builder", "Drag items from the Builder Tree onto the canvas before running.");
            return;
        }
        if (testSuiteRunnerExecutor != null) {
            showWarning("Suite Builder", "A suite execution is already in progress.");
            return;
        }
        List<Map<String, String>> executionRows = expandedSuiteBuilderExecutionRows();
        if (executionRows.isEmpty()) {
            showWarning("Suite Builder", "The canvas flow does not contain executable test steps.");
            return;
        }
        testSuiteStopRequested.set(false);
        testSuiteRunnerExecutor = Executors.newSingleThreadExecutor();
        Task<Void> runner = new Task<>() {
            @Override
            protected Void call() throws Exception {
                runSuiteBuilderRows(selectedWorkbookPath(), executionRows);
                return null;
            }
        };
        runner.setOnSucceeded(e -> {
            shutdownTestSuiteExecutor();
            testSuiteRunnerStatusLabel.setText(testSuiteStopRequested.get()
                    ? "Suite Builder execution stopped."
                    : "Suite Builder execution completed. Report: "
                    + (lastTestSuiteReportPath == null ? "not generated" : lastTestSuiteReportPath.getFileName()));
            renderSuiteBuilderCanvas();
        });
        runner.setOnFailed(e -> {
            shutdownTestSuiteExecutor();
            showError("Suite Builder Run Failed", runner.getException());
            renderSuiteBuilderCanvas();
        });
        start(runner);
        testSuiteRunnerStatusLabel.setText("Running Suite Builder flow with " + executionRows.size() + " step(s).");
    }

    private void deploySuiteBuilderFlowToGithubActions() {
        List<Map<String, String>> executionRows = expandedSuiteBuilderExecutionRows();
        if (executionRows.isEmpty()) {
            showWarning("Suite Builder", "Build a canvas flow with executable test steps before deploying.");
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                requireGithubConnection();
                Path workbookPath = selectedWorkbookPath();
                if (workbookPath == null) {
                    throw new IllegalStateException("Import or create a Test Suite Runner workbook before deploying the canvas flow.");
                }
                ensureGithubRepositoryReady();
                writeRowsToWorkbook(workbookPath, executionRows);
                syncTestWeaveRunnerSourceToGithub();
                githubPutFile(".github/workflows/testweave-runner.yml", githubActionsWorkflowYaml(),
                        "Deploy TestWeave canvas flow workflow");
                githubPutFile("testweave/test-suite.xlsx", Files.readAllBytes(workbookPath),
                        "Update TestWeave canvas flow workbook");
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            githubStatusLabel.setText("GitHub: canvas flow deployed");
            showInfo("GitHub Actions", "Suite Builder canvas flow deployed to GitHub Actions.");
        });
        task.setOnFailed(e -> showError("Deploy Canvas Flow Failed", task.getException()));
        start(task);
    }

    private List<Map<String, String>> expandedSuiteBuilderExecutionRows() {
        List<Map<String, String>> rows = new ArrayList<>();
        List<Map<String, String>> canvasRows = new ArrayList<>(suiteBuilderCanvasRows);
        if (!suiteBuilderCanvasConnections.isEmpty()) {
            canvasRows = orderedCanvasRowsByConnections();
        }
        for (Map<String, String> canvasRow : canvasRows) {
            JSONArray flowRows = parseOptionalJsonArray(canvasRow.get("flowRows"));
            for (int i = 0; i < flowRows.length(); i++) {
                JSONObject object = flowRows.optJSONObject(i);
                if (object == null || object.optString("step").isBlank()) {
                    continue;
                }
                Map<String, String> row = new LinkedHashMap<>();
                for (String key : object.keySet()) {
                    row.put(key, String.valueOf(object.opt(key)));
                }
                row.put("builderCanvasIndex", String.valueOf(suiteBuilderCanvasRows.indexOf(canvasRow)));
                rows.add(row);
            }
        }
        return rows;
    }

    private List<Map<String, String>> orderedCanvasRowsByConnections() {
        List<Map<String, String>> ordered = new ArrayList<>();
        Set<Integer> used = new java.util.HashSet<>();
        for (Map<String, String> connection : suiteBuilderCanvasConnections) {
            boolean reversed = "true".equalsIgnoreCase(connection.getOrDefault("reversed", "false"));
            int from = parseIndex(connection.get(reversed ? "to" : "from"));
            int to = parseIndex(connection.get(reversed ? "from" : "to"));
            if (from >= 0 && from < suiteBuilderCanvasRows.size() && used.add(from)) {
                ordered.add(suiteBuilderCanvasRows.get(from));
            }
            if (to >= 0 && to < suiteBuilderCanvasRows.size() && used.add(to)) {
                ordered.add(suiteBuilderCanvasRows.get(to));
            }
        }
        for (int i = 0; i < suiteBuilderCanvasRows.size(); i++) {
            if (used.add(i)) {
                ordered.add(suiteBuilderCanvasRows.get(i));
            }
        }
        return ordered;
    }

    private void deleteSelectedSuiteBuilderCanvasNode() {
        if (suiteBuilderSelectedConnectionIndex >= 0
                && suiteBuilderSelectedConnectionIndex < suiteBuilderCanvasConnections.size()) {
            suiteBuilderCanvasConnections.remove(suiteBuilderSelectedConnectionIndex);
            suiteBuilderSelectedConnectionIndex = -1;
            renderSuiteBuilderCanvas();
            return;
        }
        int index = suiteBuilderSelectedCanvasIndex;
        if (index < 0 || index >= suiteBuilderCanvasRows.size()) {
            showWarning("Suite Builder", "Select a canvas activity or arrow before deleting.");
            return;
        }
        suiteBuilderCanvasRows.remove(index);
        List<Map<String, String>> adjusted = new ArrayList<>();
        for (Map<String, String> connection : suiteBuilderCanvasConnections) {
            int from = parseIndex(connection.get("from"));
            int to = parseIndex(connection.get("to"));
            if (from == index || to == index) {
                continue;
            }
            Map<String, String> copy = new LinkedHashMap<>(connection);
            if (from > index) {
                copy.put("from", String.valueOf(from - 1));
            }
            if (to > index) {
                copy.put("to", String.valueOf(to - 1));
            }
            adjusted.add(copy);
        }
        suiteBuilderCanvasConnections.setAll(adjusted);
        suiteBuilderSelectedCanvasIndex = -1;
        suiteBuilderConnectionSourceIndex = -1;
        suiteBuilderSelectedConnectionIndex = -1;
        renderSuiteBuilderCanvas();
    }

    private void connectSelectedSuiteBuilderCanvasNodes() {
        if (suiteBuilderConnectionSourceIndex < 0 || suiteBuilderSelectedCanvasIndex < 0
                || suiteBuilderConnectionSourceIndex == suiteBuilderSelectedCanvasIndex) {
            showWarning("Suite Builder", "Click two different canvas activities, then click Connect.");
            return;
        }
        addSuiteBuilderConnection(suiteBuilderConnectionSourceIndex, suiteBuilderSelectedCanvasIndex);
        renderSuiteBuilderCanvas();
    }

    private void reverseSelectedSuiteBuilderConnection() {
        if (suiteBuilderSelectedConnectionIndex < 0
                || suiteBuilderSelectedConnectionIndex >= suiteBuilderCanvasConnections.size()) {
            showWarning("Suite Builder", "Select an arrow before reversing it.");
            return;
        }
        Map<String, String> connection = suiteBuilderCanvasConnections.get(suiteBuilderSelectedConnectionIndex);
        boolean reversed = "true".equalsIgnoreCase(connection.getOrDefault("reversed", "false"));
        connection.put("reversed", reversed ? "false" : "true");
        renderSuiteBuilderCanvas();
    }

    private void addSuiteBuilderConnection(int from, int to) {
        if (from < 0 || to < 0 || from == to) {
            return;
        }
        for (Map<String, String> connection : suiteBuilderCanvasConnections) {
            if (parseIndex(connection.get("from")) == from && parseIndex(connection.get("to")) == to) {
                return;
            }
        }
        suiteBuilderCanvasConnections.add(row("from", String.valueOf(from), "to", String.valueOf(to)));
    }

    private int parseIndex(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return -1;
        }
    }

    private void runSuiteBuilderRows(Path workbookPath, List<Map<String, String>> executionRows) throws Exception {
        List<TestSuiteStepResult> results = new ArrayList<>();
        for (Map<String, String> row : executionRows) {
            if (testSuiteStopRequested.get()) {
                break;
            }
            updateSuiteBuilderCanvasStatus(row, "Running", 0.5);
            TestSuiteStepResult result = executeTestSuiteRow(row);
            results.add(result);
            updateSuiteBuilderCanvasStatus(row, result.passed ? "Passed" : result.status, 1);
        }
        lastTestSuiteReportPath = writeTestSuiteReport(workbookPath, results);
    }

    private void updateSuiteBuilderCanvasStatus(Map<String, String> executionRow, String status, double progress) {
        String index = executionRow.get("builderCanvasIndex");
        if (index == null || index.isBlank()) {
            return;
        }
        Platform.runLater(() -> {
            try {
                int canvasIndex = Integer.parseInt(index);
                if (canvasIndex >= 0 && canvasIndex < suiteBuilderCanvasRows.size()) {
                    Map<String, String> canvasRow = suiteBuilderCanvasRows.get(canvasIndex);
                    canvasRow.put("flowStatus", status);
                    canvasRow.put("status", status);
                    canvasRow.put("flowProgress", String.valueOf(progress));
                    renderSuiteBuilderCanvas();
                }
            } catch (Exception ignored) {
            }
        });
    }

    private String builderCanvasContext() {
        String suite = testSuiteNameField == null ? "" : testSuiteNameField.getText().trim();
        String testCase = currentTestCaseName();
        if (suite.isBlank() && testCase.isBlank()) {
            return testSuiteRows.size() + " step(s)";
        }
        if (testCase.isBlank()) {
            return suite + " - " + testSuiteRows.size() + " step(s)";
        }
        if (suite.isBlank()) {
            return testCase + " - " + testSuiteRows.size() + " step(s)";
        }
        return suite + " / " + testCase + " - " + testSuiteRows.size() + " step(s)";
    }

    private void populateBuilderCanvas(Pane canvas, List<Map<String, String>> steps, int columns) {
        if (steps.isEmpty()) {
            Label empty = new Label("Import or create a workbook, then add steps to build a suite canvas.");
            empty.getStyleClass().add("muted");
            empty.setLayoutX(32);
            empty.setLayoutY(32);
            canvas.getChildren().add(empty);
            return;
        }

        List<VBox> nodes = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Map<String, String> step = steps.get(i);
            VBox node = createBuilderNode(step, i + 1, i);
            node.setLayoutX(canvasCoordinate(step.get("canvasX"), 32 + (i % columns) * 270));
            node.setLayoutY(canvasCoordinate(step.get("canvasY"), 40 + (i / columns) * 180));
            enableBuilderNodeDrag(node, step, i);
            canvas.getChildren().add(node);
            nodes.add(node);
        }
        List<Map<String, String>> connections = new ArrayList<>(suiteBuilderCanvasConnections);
        for (int i = 0; i < connections.size(); i++) {
            Map<String, String> connection = connections.get(i);
            int from = parseIndex(connection.get("from"));
            int to = parseIndex(connection.get("to"));
            if (from >= 0 && to >= 0 && from < nodes.size() && to < nodes.size()) {
                addBuilderConnector(canvas, nodes.get(from), nodes.get(to), connection, i);
            }
        }
    }

    private List<Map<String, String>> defaultSuiteBuilderConnections(int count) {
        List<Map<String, String>> connections = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            connections.add(row("from", String.valueOf(i - 1), "to", String.valueOf(i)));
        }
        return connections;
    }

    private void addBuilderConnector(Pane canvas, VBox fromNode, VBox toNode, Map<String, String> connection, int connectionIndex) {
        boolean reversed = "true".equalsIgnoreCase(connection.getOrDefault("reversed", "false"));
        Line connector = new Line();
        connector.getStyleClass().add("builder-connector");
        if (connectionIndex == suiteBuilderSelectedConnectionIndex) {
            connector.getStyleClass().add("builder-connector-selected");
        }
        double forwardStartX = canvasCoordinate(connection.get("startX"), fromNode.getLayoutX() + 240);
        double forwardStartY = canvasCoordinate(connection.get("startY"), fromNode.getLayoutY() + 70);
        double forwardEndX = canvasCoordinate(connection.get("endX"), toNode.getLayoutX());
        double forwardEndY = canvasCoordinate(connection.get("endY"), toNode.getLayoutY() + 70);
        connector.setStartX(reversed ? forwardEndX : forwardStartX);
        connector.setStartY(reversed ? forwardEndY : forwardStartY);
        connector.setEndX(reversed ? forwardStartX : forwardEndX);
        connector.setEndY(reversed ? forwardStartY : forwardEndY);

        Polygon arrow = new Polygon(0, 0, -12, -6, -12, 6);
        arrow.getStyleClass().add("builder-arrow");
        arrow.layoutXProperty().bind(connector.endXProperty());
        arrow.layoutYProperty().bind(connector.endYProperty());
        arrow.rotateProperty().bind(javafx.beans.binding.Bindings.createDoubleBinding(() -> {
            double angle = Math.toDegrees(Math.atan2(
                    connector.getEndY() - connector.getStartY(),
                    connector.getEndX() - connector.getStartX()));
            return angle;
        }, connector.startXProperty(), connector.startYProperty(), connector.endXProperty(), connector.endYProperty()));
        Circle startHandle = connectorHandle(connector.getStartX(), connector.getStartY());
        Circle endHandle = connectorHandle(connector.getEndX(), connector.getEndY());
        startHandle.centerXProperty().bindBidirectional(connector.startXProperty());
        startHandle.centerYProperty().bindBidirectional(connector.startYProperty());
        endHandle.centerXProperty().bindBidirectional(connector.endXProperty());
        endHandle.centerYProperty().bindBidirectional(connector.endYProperty());
        enableConnectorSelection(connector, arrow, startHandle, endHandle, connectionIndex);
        enableConnectorHandleDrag(startHandle, connection, !reversed);
        enableConnectorHandleDrag(endHandle, connection, reversed);
        canvas.getChildren().addAll(connector, arrow, startHandle, endHandle);
    }

    private void enableConnectorSelection(Line connector, Polygon arrow, Circle startHandle, Circle endHandle, int connectionIndex) {
        EventHandler<javafx.scene.input.MouseEvent> selector = event -> {
            suiteBuilderSelectedConnectionIndex = connectionIndex;
            suiteBuilderSelectedCanvasIndex = -1;
            suiteBuilderConnectionSourceIndex = -1;
            renderSuiteBuilderCanvas();
            event.consume();
        };
        connector.setOnMouseClicked(selector);
        arrow.setOnMouseClicked(selector);
        startHandle.setOnMouseClicked(selector);
        endHandle.setOnMouseClicked(selector);
    }

    private Circle connectorHandle(double x, double y) {
        Circle handle = new Circle(x, y, 5);
        handle.getStyleClass().add("builder-connector-handle");
        return handle;
    }

    private void enableConnectorHandleDrag(Circle handle, Map<String, String> connection, boolean start) {
        handle.setOnMouseDragged(event -> {
            javafx.geometry.Point2D point = suiteBuilderCanvas.sceneToLocal(event.getSceneX(), event.getSceneY());
            handle.setCenterX(Math.max(0, point.getX()));
            handle.setCenterY(Math.max(0, point.getY()));
            connection.put(start ? "startX" : "endX", String.valueOf(handle.getCenterX()));
            connection.put(start ? "startY" : "endY", String.valueOf(handle.getCenterY()));
            event.consume();
        });
    }

    private VBox createBuilderNode(Map<String, String> step, int index, int canvasIndex) {
        Label title = new Label(index + ". " + step.getOrDefault("flowLabel", step.getOrDefault("step", "Step")));
        title.getStyleClass().add("builder-node-title");
        title.setWrapText(true);

        Label meta = new Label(step.getOrDefault("type", "Step") + " | " + step.getOrDefault("executionMode", "Sequential"));
        meta.getStyleClass().add("muted");
        meta.setWrapText(true);

        Label details = new Label(shorten(step.getOrDefault("details", ""), 88));
        details.setWrapText(true);

        Label status = new Label("Status: " + step.getOrDefault("status", "Ready"));
        status.getStyleClass().add("metric");
        ProgressBar progress = new ProgressBar(canvasProgress(step));
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.getStyleClass().add("builder-progress");

        VBox node = new VBox(7, title, meta, details, progress, status);
        node.getStyleClass().add("builder-node");
        if (canvasIndex == suiteBuilderSelectedCanvasIndex) {
            node.getStyleClass().add("builder-node-selected");
        }
        node.setStyle("-fx-border-color: " + builderNodeColor(step.getOrDefault("type", ""))
                + "; -fx-background-color: " + builderStatusColor(step) + ";");
        node.setPrefSize(240, 142);
        node.setMinSize(240, 142);
        node.setMaxSize(240, 142);
        return node;
    }

    private double canvasProgress(Map<String, String> row) {
        try {
            return clamp(Double.parseDouble(row.getOrDefault("flowProgress", "0")), 0, 1);
        } catch (Exception ignored) {
            return 0;
        }
    }

    private String builderStatusColor(Map<String, String> row) {
        String status = row.getOrDefault("flowStatus", row.getOrDefault("status", "Ready"));
        if (status.startsWith("Running")) {
            return "#fff7d1";
        }
        if (status.startsWith("Passed")) {
            return "#d9fff6";
        }
        if (status.startsWith("Failed")) {
            return "#ffe3eb";
        }
        if (status.startsWith("Stopped")) {
            return "#e9efff";
        }
        return "#f8fbff";
    }

    private double canvasCoordinate(String value, double fallback) {
        try {
            return value == null || value.isBlank() ? fallback : Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private void enableBuilderNodeDrag(VBox node, Map<String, String> row, int index) {
        double[] pointerOffset = new double[2];
        node.setOnMousePressed(event -> {
            suiteBuilderConnectionSourceIndex = suiteBuilderSelectedCanvasIndex;
            suiteBuilderSelectedCanvasIndex = index;
            suiteBuilderSelectedConnectionIndex = -1;
            pointerOffset[0] = event.getSceneX() - node.getLayoutX();
            pointerOffset[1] = event.getSceneY() - node.getLayoutY();
            node.toFront();
        });
        node.setOnMouseDragged(event -> {
            double x = Math.max(12, event.getSceneX() - pointerOffset[0]);
            double y = Math.max(12, event.getSceneY() - pointerOffset[1]);
            node.setLayoutX(x);
            node.setLayoutY(y);
            row.put("canvasX", String.valueOf(x));
            row.put("canvasY", String.valueOf(y));
        });
        node.setOnMouseReleased(event -> renderSuiteBuilderCanvas());
    }

    private String builderNodeColor(String type) {
        return switch (type) {
            case "Web Test" -> CYAN;
            case "Performance Test" -> "#ff9f43";
            case "JSON Compare" -> VIOLET;
            case "DB Validation" -> PRIMARY;
            case "Field Validation" -> "#ff4fc3";
            default -> PRIMARY;
        };
    }

    private String shorten(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value == null ? "" : value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private javafx.scene.Node createTestRunnerContextPanel(TextField suiteField, TextField caseField,
                                                           TextField stepField, Runnable addAction) {
        Button addToRunner = secondary("Add to Test Runner");
        addToRunner.setOnAction(e -> addAction.run());
        addToRunner.getStyleClass().add("context-button");

        GridPane context = grid();
        context.getStyleClass().add("context-panel");
        context.add(labeled("Test Suite", suiteField), 0, 0);
        context.add(labeled("Test Case", caseField), 1, 0);
        context.add(labeled("Test Step", stepField), 2, 0);
        context.add(addToRunner, 3, 0);
        GridPane.setHgrow(suiteField, Priority.ALWAYS);
        GridPane.setHgrow(caseField, Priority.ALWAYS);
        GridPane.setHgrow(stepField, Priority.ALWAYS);
        return context;
    }

    private void applySharedTestSuiteContext(TextField suiteField, TextField caseField) {
        if (testSuiteNameField != null && suiteField.getText().isBlank()) {
            suiteField.setText(testSuiteNameField.getText());
        }
        if (testCaseNameField != null && caseField.getText().isBlank()) {
            caseField.setText(currentTestCaseName());
        }
    }

    private void propagateTestSuiteContext() {
        populateImportedTestSuiteDetails(
                testSuiteNameField == null ? "" : testSuiteNameField.getText(),
                currentTestCaseName());
    }

    private void copyIfBlank(TextField target, TextField source) {
        if (target != null && source != null && target.getText().isBlank()) {
            target.setText(source.getText());
        }
    }

    private void addFieldValidationToTestRunner() {
        addValidationStepToTestRunner(
                fieldValidationTestSuiteField,
                fieldValidationTestCaseField,
                fieldValidationTestStepField,
                "Field Validation",
                selectedFieldValidationSummary());
    }

    private void addJsonCompareToTestRunner() {
        addValidationStepToTestRunner(
                jsonCompareTestSuiteField,
                jsonCompareTestCaseField,
                jsonCompareTestStepField,
                "JSON Compare",
                "Expected file: " + expectedJsonPathField.getText() + ", mode: " + compareModeBox.getValue());
    }

    private void addPerformanceTestToTestRunner() {
        addValidationStepToTestRunner(
                performanceTestSuiteField,
                performanceTestCaseField,
                performanceTestStepField,
                "Performance Test",
                "Threads: " + perfThreadsSpinner.getValue()
                        + ", iterations/thread: " + perfIterationsSpinner.getValue()
                        + ", endpoint: " + (endpointField == null ? "" : endpointField.getText()));
    }

    private void addDbValidationsToTestRunner() {
        long apiDbCount = dbRuleRows.stream().filter(this::isSelected).count();
        long dbColumnCount = dbColumnValidationRows.stream().filter(this::isSelected).count();
        addValidationStepToTestRunner(
                dbValidationTestSuiteField,
                dbValidationTestCaseField,
                dbValidationTestStepField,
                "DB Validation",
                apiDbCount + " API-DB rule(s), " + dbColumnCount + " DB column validation(s)");
    }

    private void addWebTestToTestRunner() {
        addValidationStepToTestRunner(
                webTestingTestSuiteField,
                webTestingTestCaseField,
                webTestingTestStepField,
                "Web Test",
                webStepRows.size() + " web step(s), start URL: " + webStartUrlField.getText());
    }

    private void addValidationStepToTestRunner(TextField suiteField, TextField caseField, TextField stepField,
                                               String type, String details) {
        String suite = suiteField.getText().trim();
        String testCase = caseField.getText().trim();
        String step = stepField.getText().trim();
        if (suite.isBlank() || testCase.isBlank() || step.isBlank()) {
            showWarning("Test Runner", "Enter Test Suite, Test Case, and Test Step before adding to Test Runner.");
            return;
        }
        Map<String, String> tableRow = row("selected", "true", "suite", suite, "case", testCase, "step", step,
                "executionMode", "Sequential",
                "type", type, "details", details, "status", "Ready");
        try {
            Path workbookPath = selectedWorkbookPath();
            if (workbookPath != null) {
                appendRowsToWorkbook(workbookPath, List.of(buildWorkbookRow(suite, testCase, step, type, details)));
                refreshTestSuiteRunnerSteps(workbookPath);
                testSuiteRunnerStatusLabel.setText(type + " step added to workbook.");
            } else {
                testSuiteRows.add(tableRow);
            }
            showInfo("Test Runner", type + " step added to Test Suite Runner.");
        } catch (Exception e) {
            showError("Add to Test Runner Failed", e);
        }
    }

    private void createTestSuiteWorkbook() {
        String testCase = currentTestCaseName();
        if (testSuiteNameField.getText().isBlank() || testCase.isBlank()) {
            showWarning("Test Suite Runner", "Enter both Test Suite and Test Case before creating the workbook.");
            return;
        }
        File file = chooseSaveFile("Excel Workbook", "*.xlsx",
                testSuiteNameField.getText().trim() + ".xlsx", configuredFolder("TestSuite"));
        if (file == null) {
            return;
        }
        try {
            writeSingleSheetWorkbook(file.toPath(), createSafeExcelSheetName(testCase), testCase);
            testSuiteWorkbookPathField.setText(file.getAbsolutePath());
            setTestCaseOptions(List.of(createSafeExcelSheetName(testCase)), createSafeExcelSheetName(testCase));
            refreshTestSuiteRunnerSteps(file.toPath());
            testSuiteRunnerStatusLabel.setText("Workbook created: " + file.getName());
            propagateTestSuiteContext();
        } catch (Exception e) {
            showError("Create Workbook Failed", e);
        }
    }

    private void importTestSuiteWorkbook() {
        File file = chooseOpenFile("Excel Workbook", "*.xlsx", configuredFolder("TestSuite"));
        if (file == null) {
            return;
        }
        try {
            String workbookName = workbookNameWithoutExtension(file.getName());
            List<String> sheetNames = readWorkbookSheets(file.toPath()).stream().map(sheet -> sheet.name).toList();
            String sheetName = sheetNames.isEmpty() ? "" : sheetNames.get(0);
            testSuiteWorkbookPathField.setText(file.getAbsolutePath());
            setTestCaseOptions(sheetNames, sheetName);
            populateImportedTestSuiteDetails(workbookName, sheetName);
            refreshTestSuiteRunnerSteps(file.toPath());
            showInfo("Test Suite Imported", "Test suite runner imported successfully.");
        } catch (Exception e) {
            showError("Import Workbook Failed", e);
        }
    }

    private void updateTestSuiteWorkbook() {
        Path workbookPath = selectedWorkbookPath();
        if (workbookPath == null) {
            showWarning("Update Test Suite", "Import or create a Test Suite Runner workbook before updating.");
            return;
        }
        try {
            writeTestSuiteRowsToWorkbook(workbookPath);
            testSuiteRunnerStatusLabel.setText("Workbook updated: " + workbookPath.getFileName());
            showInfo("Update Test Suite", "Imported workbook and test case sheet updated successfully.");
        } catch (Exception e) {
            showError("Update Workbook Failed", e);
        }
    }

    private void openImportedTestSuiteWorkbook() {
        Path workbookPath = selectedWorkbookPath();
        openPath(workbookPath, "Import or create a Test Suite Runner workbook before opening.");
        if (workbookPath != null) {
            testSuiteRunnerStatusLabel.setText("Opening workbook: " + workbookPath.getFileName()
                    + ", sheet: " + currentTestCaseName());
        }
    }

    private void connectGithub() {
        String clientId = System.getenv("TESTWEAVE_GITHUB_CLIENT_ID");
        if (clientId == null || clientId.isBlank()) {
            getHostServices().showDocument("https://github.com/login");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Connect GitHub");
            dialog.setHeaderText("Paste a GitHub token with repo and workflow access.");
            dialog.showAndWait().ifPresent(token -> {
                githubAccessToken = token.trim();
                githubStatusLabel.setText(githubAccessToken.isBlank() ? "GitHub: not connected" : "GitHub: connected");
            });
            return;
        }
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return githubDeviceLogin(clientId);
            }
        };
        task.setOnSucceeded(e -> {
            githubAccessToken = task.getValue();
            githubStatusLabel.setText("GitHub: connected");
        });
        task.setOnFailed(e -> showError("GitHub Connect Failed", task.getException()));
        start(task);
    }

    private String githubDeviceLogin(String clientId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String body = "client_id=" + encode(clientId) + "&scope=" + encode("repo workflow");
        JSONObject device = githubPostForm(client, "https://github.com/login/device/code", body);
        String verificationUri = device.optString("verification_uri", "https://github.com/login/device");
        String userCode = device.getString("user_code");
        Platform.runLater(() -> {
            githubStatusLabel.setText("GitHub: enter code " + userCode);
            getHostServices().showDocument(verificationUri);
            showInfo("GitHub Login", "Enter this code on GitHub: " + userCode);
        });
        String deviceCode = device.getString("device_code");
        int interval = Math.max(5, device.optInt("interval", 5));
        for (int attempt = 0; attempt < 90; attempt++) {
            Thread.sleep(interval * 1000L);
            JSONObject token = githubPostForm(client, "https://github.com/login/oauth/access_token",
                    "client_id=" + encode(clientId)
                            + "&device_code=" + encode(deviceCode)
                            + "&grant_type=urn:ietf:params:oauth:grant-type:device_code");
            if (token.has("access_token")) {
                return token.getString("access_token");
            }
            String error = token.optString("error");
            if (!error.isBlank() && !"authorization_pending".equals(error) && !"slow_down".equals(error)) {
                throw new IllegalStateException(token.optString("error_description", error));
            }
            if ("slow_down".equals(error)) {
                interval += 5;
            }
        }
        throw new IllegalStateException("GitHub login timed out.");
    }

    private JSONObject githubPostForm(HttpClient client, String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    private void deployTestSuiteToGithubActions() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                requireGithubConnection();
                Path workbookPath = selectedWorkbookPath();
                if (workbookPath == null) {
                    throw new IllegalStateException("Import or create a Test Suite Runner workbook before deploying.");
                }
                ensureGithubRepositoryReady();
                writeTestSuiteRowsToWorkbook(workbookPath);
                syncTestWeaveRunnerSourceToGithub();
                githubPutFile(".github/workflows/testweave-runner.yml", githubActionsWorkflowYaml(),
                        "Deploy TestWeave GitHub Actions workflow");
                githubPutFile("testweave/test-suite.xlsx", Files.readAllBytes(workbookPath),
                        "Update TestWeave test suite workbook");
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            githubStatusLabel.setText("GitHub: workflow deployed");
            showInfo("GitHub Actions", "TestWeave runner deployed to GitHub Actions.");
        });
        task.setOnFailed(e -> showError("Deploy to GitHub Actions Failed", task.getException()));
        start(task);
    }

    private void runGithubActionsTestSuite() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                requireGithubConnection();
                Path workbookPath = selectedWorkbookPath();
                if (workbookPath == null) {
                    throw new IllegalStateException("Import or create a Test Suite Runner workbook before running in GitHub Actions.");
                }
                ensureGithubRepositoryReady();
                writeTestSuiteRowsToWorkbook(workbookPath);
                syncTestWeaveRunnerSourceToGithub();
                githubPutFile(".github/workflows/testweave-runner.yml", githubActionsWorkflowYaml(),
                        "Update TestWeave GitHub Actions workflow before run");
                githubPutFile("testweave/test-suite.xlsx", Files.readAllBytes(workbookPath),
                        "Update TestWeave test suite workbook before run");
                JSONObject inputs = new JSONObject()
                        .put("suite_file", "testweave/test-suite.xlsx")
                        .put("parallel", String.valueOf(testSuiteParallelExecutionCheck.isSelected()))
                        .put("threads", String.valueOf(parseThreadCount()));
                JSONObject payload = new JSONObject()
                        .put("ref", githubBranch())
                        .put("inputs", inputs);
                githubRequest("POST", githubApiBase() + "/actions/workflows/testweave-runner.yml/dispatches",
                        payload.toString(), true);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            githubStatusLabel.setText("GitHub: workflow triggered");
            openGithubActions();
        });
        task.setOnFailed(e -> showError("Run GitHub Actions Failed", task.getException()));
        start(task);
    }

    private void openGithubWorkflow() {
        openGithubActions();
    }

    private void openGithubActions() {
        if (githubOwnerField.getText().isBlank() || githubRepoField.getText().isBlank()) {
            showWarning("GitHub Actions", "Enter GitHub owner and repository first.");
            return;
        }
        getHostServices().showDocument("https://github.com/" + githubOwner() + "/" + githubRepo() + "/actions/workflows/testweave-runner.yml");
    }

    private void requireGithubConnection() {
        normalizeGithubRepositoryFields();
        if (githubAccessToken == null || githubAccessToken.isBlank()) {
            throw new IllegalStateException("Connect GitHub before deploying or running the workflow.");
        }
        if (githubOwner().isBlank() || githubRepo().isBlank()) {
            throw new IllegalStateException("Enter GitHub owner and repository.");
        }
    }

    private void normalizeGithubRepositoryFields() {
        String repoText = githubRepoField.getText().trim();
        if (repoText.startsWith("https://github.com/")) {
            repoText = repoText.substring("https://github.com/".length());
        }
        repoText = repoText.replaceAll("\\.git$", "").replaceAll("^/+", "").replaceAll("/+$", "");
        if (repoText.contains("/")) {
            String[] parts = repoText.split("/", 3);
            if (githubOwnerField.getText().isBlank()) {
                githubOwnerField.setText(parts[0]);
            }
            githubRepoField.setText(parts[1]);
        }
    }

    private void ensureGithubRepositoryReady() throws Exception {
        HttpResponse<String> repoResponse = githubRequest("GET", githubApiBase(), null, false);
        if (repoResponse.statusCode() == 404) {
            throw new IllegalStateException("GitHub repository was not found or this token cannot access it. "
                    + "Check Owner/Repository, private repo permissions, and that the token has repo access.");
        }
        if (repoResponse.statusCode() >= 300) {
            throw new IllegalStateException("GitHub repository check failed (" + repoResponse.statusCode() + "): " + repoResponse.body());
        }
        JSONObject repo = new JSONObject(repoResponse.body());
        if (githubBranchField.getText().isBlank()) {
            githubBranchField.setText(repo.optString("default_branch", "main"));
        }
        HttpResponse<String> branchResponse = githubRequest("GET", githubApiBase() + "/branches/" + encode(githubBranch()), null, false);
        if (branchResponse.statusCode() == 404) {
            String defaultBranch = repo.optString("default_branch", "main");
            githubBranchField.setText(defaultBranch);
            branchResponse = githubRequest("GET", githubApiBase() + "/branches/" + encode(defaultBranch), null, false);
        }
        if (branchResponse.statusCode() >= 300) {
            throw new IllegalStateException("GitHub branch check failed (" + branchResponse.statusCode() + "): " + branchResponse.body());
        }
        HttpResponse<String> pomResponse = githubRequest("GET", githubApiBase() + "/contents/pom.xml?ref=" + encode(githubBranch()), null, false);
        if (pomResponse.statusCode() == 404) {
            throw new IllegalStateException("This deployment mode expects the selected repository to contain the TestWeave Maven source code with pom.xml. "
                    + "Select the TestWeave repository, or add the source before deploying the workflow.");
        }
    }

    private String githubActionsWorkflowYaml() {
        return """
                name: TestWeave Test Suite Runner

                on:
                  workflow_dispatch:
                    inputs:
                      suite_file:
                        description: Test suite workbook path
                        required: true
                        default: testweave/test-suite.xlsx
                      parallel:
                        description: Enable TestWeave parallel execution
                        required: true
                        default: '%s'
                      threads:
                        description: TestWeave thread count
                        required: true
                        default: '%s'

                jobs:
                  testweave:
                    runs-on: ubuntu-latest
                    steps:
                      - uses: actions/checkout@v4
                      - name: Check TestWeave suite file
                        run: |
                          test -f "${{ inputs.suite_file }}"
                          ls -l "${{ inputs.suite_file }}"
                      - name: Run TestWeave suite in Docker Compose
                        env:
                          SUITE_FILE: ${{ inputs.suite_file }}
                          TESTWEAVE_PARALLEL: ${{ inputs.parallel }}
                          TESTWEAVE_THREADS: ${{ inputs.threads }}
                          TESTWEAVE_HTTP_TIMEOUT_MS: '60000'
                        run: |
                          trap 'docker compose -f docker-compose.testweave.yml down -v --remove-orphans' EXIT
                          docker compose -f docker-compose.testweave.yml up --build --abort-on-container-exit --exit-code-from testweave
                      - name: Upload TestWeave report
                        if: always()
                        uses: actions/upload-artifact@v4
                        with:
                          name: testweave-report
                          path: target/testweave-report/**
                """.formatted(testSuiteParallelExecutionCheck.isSelected(), parseThreadCount());
    }

    private void syncTestWeaveRunnerSourceToGithub() throws Exception {
        Path projectRoot = Path.of("").toAbsolutePath().normalize();
        Path pomPath = projectRoot.resolve("pom.xml");
        Path srcPath = projectRoot.resolve("src");
        if (!Files.exists(pomPath) || !Files.isDirectory(srcPath)) {
            throw new IllegalStateException("Cannot sync TestWeave runner source to GitHub because pom.xml or src folder was not found at "
                    + projectRoot + ". Run the app from the TestWeave project folder or push the latest source manually.");
        }

        githubPutFile("pom.xml", Files.readAllBytes(pomPath), "Sync TestWeave runner pom");
        syncGithubFileIfPresent(projectRoot.resolve("Dockerfile.testweave"), "Dockerfile.testweave");
        syncGithubFileIfPresent(projectRoot.resolve("docker-compose.testweave.yml"), "docker-compose.testweave.yml");
        syncGithubFileIfPresent(projectRoot.resolve(".dockerignore"), ".dockerignore");
        syncTestWeaveSupportFolders(projectRoot);
        try (Stream<Path> paths = Files.walk(srcPath)) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .sorted()
                    .toList();
            for (Path file : files) {
                String githubPath = projectRoot.relativize(file).toString().replace(File.separatorChar, '/');
                githubPutFile(githubPath, Files.readAllBytes(file), "Sync TestWeave runner source");
            }
        }
    }

    private void syncGithubFileIfPresent(Path file, String githubPath) throws Exception {
        if (Files.exists(file) && Files.isRegularFile(file)) {
            githubPutFile(githubPath, Files.readAllBytes(file), "Sync TestWeave container setup");
        }
    }

    private void syncTestWeaveSupportFolders(Path projectRoot) throws Exception {
        List<String> supportFolders = List.of("SavedResponse", "Baseline", "DBConnection", "APIVariables", "WebRecordings");
        Path documentsProjectRoot = Path.of(System.getProperty("user.home"), "Documents", "api-validator");
        for (String folder : supportFolders) {
            syncGithubFolderIfPresent(projectRoot.resolve(folder), folder);
            if (!documentsProjectRoot.equals(projectRoot)) {
                syncGithubFolderIfPresent(documentsProjectRoot.resolve(folder), folder);
            }
        }
    }

    private void syncGithubFolderIfPresent(Path folder, String githubFolder) throws Exception {
        if (!Files.isDirectory(folder)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(folder)) {
            List<Path> files = paths.filter(Files::isRegularFile).sorted().toList();
            for (Path file : files) {
                String relative = folder.relativize(file).toString().replace(File.separatorChar, '/');
                githubPutFile(githubFolder + "/" + relative, Files.readAllBytes(file), "Sync TestWeave support files");
            }
        }
    }

    private void githubPutFile(String path, String content, String message) throws Exception {
        githubPutFile(path, content.getBytes(StandardCharsets.UTF_8), message);
    }

    private void githubPutFile(String path, byte[] content, String message) throws Exception {
        JSONObject payload = new JSONObject()
                .put("message", message)
                .put("branch", githubBranch())
                .put("content", Base64.getEncoder().encodeToString(content));
        String sha = githubContentSha(path);
        if (sha != null) {
            payload.put("sha", sha);
        }
        HttpResponse<String> response = githubRequest("PUT", githubApiBase() + "/contents/" + encodePath(path),
                payload.toString(), false);
        if (response.statusCode() >= 300 && !isUnchangedGithubContent(response)) {
            throw new IllegalStateException(githubErrorMessage(response, githubApiBase() + "/contents/" + encodePath(path)));
        }
    }

    private boolean isUnchangedGithubContent(HttpResponse<String> response) {
        if (response.statusCode() != 422) {
            return false;
        }
        String body = response.body() == null ? "" : response.body().toLowerCase();
        return body.contains("content is unchanged") || body.contains("same as current");
    }

    private String githubContentSha(String path) throws Exception {
        HttpResponse<String> response = githubRequest("GET", githubApiBase() + "/contents/" + encodePath(path)
                + "?ref=" + encode(githubBranch()), null, false);
        if (response.statusCode() == 404) {
            return null;
        }
        if (response.statusCode() >= 300) {
            throw new IllegalStateException("GitHub content lookup failed: " + response.body());
        }
        return new JSONObject(response.body()).optString("sha", null);
    }

    private HttpResponse<String> githubRequest(String method, String url, String body, boolean failOnError) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + githubAccessToken)
                .header("X-GitHub-Api-Version", "2022-11-28");
        if ("GET".equals(method)) {
            builder.GET();
        } else if ("POST".equals(method)) {
            builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        } else if ("PUT".equals(method)) {
            builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        }
        HttpResponse<String> response = HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (failOnError && response.statusCode() >= 300) {
            throw new IllegalStateException(githubErrorMessage(response, url));
        }
        return response;
    }

    private String githubErrorMessage(HttpResponse<String> response, String url) {
        if (response.statusCode() == 404) {
            return "GitHub API returned 404 Not Found. Check that the repository exists, the branch is correct, "
                    + "and the connected token has access to the repository. If deploying the workflow file, "
                    + "the token also needs workflow permission/scope. URL: " + url + " Response: " + response.body();
        }
        if (response.statusCode() == 401 || response.statusCode() == 403) {
            return "GitHub authorization failed (" + response.statusCode() + "). Reconnect GitHub with repo and workflow permissions. "
                    + "Response: " + response.body();
        }
        return "GitHub API failed (" + response.statusCode() + "): " + response.body();
    }

    private String githubApiBase() {
        return "https://api.github.com/repos/" + githubOwner() + "/" + githubRepo();
    }

    private String githubOwner() {
        return githubOwnerField.getText().trim();
    }

    private String githubRepo() {
        return githubRepoField.getText().trim();
    }

    private String githubBranch() {
        String branch = githubBranchField.getText().trim();
        return branch.isBlank() ? "main" : branch;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String encodePath(String path) {
        return String.join("/", List.of(path.split("/")).stream().map(this::encode).toList());
    }

    private void populateImportedTestSuiteDetails(String testSuite, String testCase) {
        if (testSuite == null || testCase == null) {
            return;
        }
        if (testSuiteNameField != null && !Objects.equals(testSuiteNameField.getText(), testSuite)) {
            testSuiteNameField.setText(testSuite);
        }
        if (testCaseNameField != null && !Objects.equals(currentTestCaseName(), testCase)) {
            setCurrentTestCaseName(testCase);
        }
        setText(fieldValidationTestSuiteField, testSuite);
        setText(fieldValidationTestCaseField, testCase);
        setDefaultStep(fieldValidationTestStepField, testCase + " API Field Validation");
        setText(jsonCompareTestSuiteField, testSuite);
        setText(jsonCompareTestCaseField, testCase);
        setDefaultStep(jsonCompareTestStepField, testCase + " JSON Compare");
        setText(dbValidationTestSuiteField, testSuite);
        setText(dbValidationTestCaseField, testCase);
        setDefaultStep(dbValidationTestStepField, testCase + " DB Validation");
        setText(performanceTestSuiteField, testSuite);
        setText(performanceTestCaseField, testCase);
        setDefaultStep(performanceTestStepField, testCase + " Performance Test");
        setText(webTestingTestSuiteField, testSuite);
        setText(webTestingTestCaseField, testCase);
        setDefaultStep(webTestingTestStepField, testCase + " Web Test");
    }

    private void setText(TextField field, String value) {
        if (field != null) {
            field.setText(value);
        }
    }

    private void setDefaultStep(TextField field, String value) {
        if (field != null && field.getText().trim().isBlank()) {
            field.setText(value);
        }
    }

    private void runSelectedTestSuiteSteps() {
        Path workbookPath = selectedWorkbookPath();
        List<Map<String, String>> selectedRows = new ArrayList<>();
        for (Map<String, String> row : testSuiteRows) {
            if (isSelected(row)) {
                selectedRows.add(new LinkedHashMap<>(row));
            }
        }
        if (selectedRows.isEmpty()) {
            showWarning("Test Suite Runner", "Select at least one test step to run.");
            return;
        }
        if (testSuiteRunnerExecutor != null) {
            showWarning("Test Suite Runner", "A test suite run is already in progress.");
            return;
        }
        testSuiteStopRequested.set(false);
        for (Map<String, String> row : testSuiteRows) {
            if (isSelected(row)) {
                row.put("status", "Queued");
            }
        }
        testSuiteStepsTable.refresh();

        int threads = testSuiteParallelExecutionCheck.isSelected() ? parseThreadCount() : 1;
        testSuiteRunnerExecutor = Executors.newFixedThreadPool(threads);
        Task<Void> runner = new Task<>() {
            @Override
            protected Void call() throws Exception {
                runTestSuiteRows(workbookPath, selectedRows, threads);
                return null;
            }
        };
        runner.setOnSucceeded(e -> {
            shutdownTestSuiteExecutor();
            testSuiteRunnerStatusLabel.setText(testSuiteStopRequested.get()
                    ? "Test suite execution stopped."
                    : "Test suite execution completed for " + selectedRows.size() + " step(s). Report: "
                            + (lastTestSuiteReportPath == null ? "not generated" : lastTestSuiteReportPath.getFileName()));
        });
        runner.setOnFailed(e -> {
            shutdownTestSuiteExecutor();
            showError("Test Suite Runner Failed", runner.getException());
        });
        start(runner);
        testSuiteRunnerStatusLabel.setText("Running " + selectedRows.size() + " selected step(s) with " + threads + " thread(s).");
    }

    private int parseThreadCount() {
        try {
            return Math.max(1, Integer.parseInt(testSuiteThreadCountField.getText().trim()));
        } catch (Exception ignored) {
            testSuiteThreadCountField.setText("1");
            return 1;
        }
    }

    private void runTestSuiteRows(Path workbookPath, List<Map<String, String>> selectedRows, int threads) throws Exception {
        Object sequentialLock = new Object();
        List<Future<TestSuiteStepResult>> futures = new ArrayList<>();
        for (Map<String, String> row : selectedRows) {
            futures.add(testSuiteRunnerExecutor.submit(() -> {
                if (testSuiteStopRequested.get()) {
                    updateTestSuiteRowStatus(row, "Stopped");
                    return TestSuiteStepResult.stopped(row);
                }
                boolean sequential = !"Parallel".equalsIgnoreCase(row.getOrDefault("executionMode", "Sequential"));
                if (threads <= 1 || sequential) {
                    synchronized (sequentialLock) {
                        return executeTestSuiteRow(row);
                    }
                }
                return executeTestSuiteRow(row);
            }));
        }
        List<TestSuiteStepResult> results = Collections.synchronizedList(new ArrayList<>());
        for (Future<TestSuiteStepResult> future : futures) {
            if (testSuiteStopRequested.get()) {
                break;
            }
            try {
                results.add(future.get());
            } catch (Exception e) {
                if (testSuiteStopRequested.get()) {
                    break;
                }
                throw e;
            }
        }
        lastTestSuiteReportPath = writeTestSuiteReport(workbookPath, results);
    }

    private TestSuiteStepResult executeTestSuiteRow(Map<String, String> row) {
        if (testSuiteStopRequested.get()) {
            updateTestSuiteRowStatus(row, "Stopped");
            return TestSuiteStepResult.stopped(row);
        }
        updateTestSuiteRowStatus(row, "Running");
        try {
            TestSuiteStepResult result = executeRunnerStep(row);
            updateTestSuiteRowStatus(row, testSuiteStopRequested.get() ? "Stopped" : result.status);
            return testSuiteStopRequested.get() ? TestSuiteStepResult.stopped(row) : result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            updateTestSuiteRowStatus(row, "Stopped");
            return TestSuiteStepResult.stopped(row);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            updateTestSuiteRowStatus(row, "Failed: " + message);
            return TestSuiteStepResult.failed(row, message);
        }
    }

    private TestSuiteStepResult executeRunnerStep(Map<String, String> row) throws Exception {
        String type = row.getOrDefault("type", "");
        if ("Web Test".equals(type) && !row.getOrDefault("workbook:WEB_TEST", "").isBlank()) {
            WebTestRunReport webReport = executeRunnerWebTest(row);
            String status = webReport.failed == 0 && webReport.total > 0 ? "Passed" : "Failed (" + webReport.failed + " failed)";
            TestSuiteStepResult result = new TestSuiteStepResult(row, status, webReport.failed == 0 && webReport.total > 0);
            result.details.add("Web steps executed: " + webReport.total + ", passed: " + webReport.passed + ", failed: " + webReport.failed);
            for (WebTestExecutionResult stepResult : webReport.results) {
                result.addValidation(stepResult.action, nullToBlank(stepResult.selector),
                        nullToBlank(stepResult.expectedValue), stepResult.passed ? "PASS" : "FAIL",
                        stepResult.passed, nullToBlank(stepResult.message));
                if (stepResult.capturedVariableName != null && !stepResult.capturedVariableName.isBlank()) {
                    savedVariables.put(stepResult.capturedVariableName, nullToBlank(stepResult.capturedVariableValue));
                }
            }
            Platform.runLater(this::refreshVariablesView);
            return result;
        }
        if ("Performance Test".equals(type) && !row.getOrDefault("workbook:PERFORMANCE_TEST", "").isBlank()) {
            JSONObject performance = new JSONObject(row.get("workbook:PERFORMANCE_TEST"));
            ApiRequest request = buildRunnerApiRequest(row, performance.optString("body", row.getOrDefault("workbook:Request Payload", "")));
            PerformanceTestResult result = performanceTestService.runLoadTest(request,
                    Math.max(1, performance.optInt("threads", 1)),
                    Math.max(1, performance.optInt("iterationsPerThread", 1)),
                    localPerformanceReportsDirectory());
            String status = result.errors == 0 ? "Passed (" + result.samples + " samples)" : "Failed (" + result.errors + " errors)";
            TestSuiteStepResult stepResult = new TestSuiteStepResult(row, status, result.errors == 0);
            stepResult.addValidation("Performance Test",
                    Math.max(1, performance.optInt("threads", 1)) + " threads x "
                            + Math.max(1, performance.optInt("iterationsPerThread", 1)) + " iterations",
                    "0 errors", result.errors + " errors / " + result.samples + " samples",
                    result.errors == 0,
                    "HTML report generated: " + (result.reportIndexPath == null ? "" : result.reportIndexPath.toAbsolutePath()));
            if (result.reportIndexPath != null) {
                stepResult.details.add("Performance report: " + result.reportIndexPath);
            }
            recordPerformanceExecution(result, row.getOrDefault("suite", "Performance Test Suite"));
            return stepResult;
        }
        if (!row.getOrDefault("workbook:Hit Request", "").isBlank()) {
            ApiResponse response = apiService.sendRequest(buildRunnerApiRequest(row, row.getOrDefault("workbook:Request Payload", "")));
            TestSuiteStepResult result = new TestSuiteStepResult(row, "Running validations", response.statusCode < 400);
            boolean hasRunnerValidations = hasRunnerValidationColumns(row);
            if (!hasRunnerValidations) {
                result.details.add("HTTP " + response.statusCode + ", duration: " + response.timeMs + " ms");
            }
            Map<String, String> variables = new HashMap<>(savedVariables);
            captureRunnerVariables(row, response.rawBody, variables);
            runRunnerApiFieldValidation(row, response.rawBody, variables, result);
            runRunnerJsonCompare(row, response.rawBody, result);
            runRunnerDbValidation(row, response.rawBody, variables, result);
            if (!hasRunnerValidations) {
                result.status = response.statusCode < 400 ? "Passed (" + response.statusCode + ")" : "Failed HTTP " + response.statusCode;
            } else {
                result.status = result.passed ? "Passed" : "Failed";
            }
            return result;
        }
        if (hasRunnerValidationColumns(row)) {
            TestSuiteStepResult result = new TestSuiteStepResult(row, "Running validations", true);
            Map<String, String> variables = new HashMap<>(savedVariables);
            runRunnerDbValidation(row, "", variables, result);
            if (result.validations.isEmpty()) {
                result.passed = false;
                result.status = "Failed";
                result.addValidation("DB Validation", "Execution failed", "", "", false,
                        "Validation columns were present, but no executable DB validation was configured.");
            } else {
                result.status = result.passed ? "Passed" : "Failed";
            }
            return result;
        }
        Thread.sleep(100);
        TestSuiteStepResult result = new TestSuiteStepResult(row, "Passed", true);
        result.addValidation("Manual Step", row.getOrDefault("type", "Manual"), "", "Completed", true, "Manual step marked as passed.");
        return result;
    }

    private boolean hasRunnerValidationColumns(Map<String, String> row) {
        return !row.getOrDefault("workbook:API_FIELD_VALIDATION", "").isBlank()
                || !row.getOrDefault("workbook:JSON_COMPARE", "").isBlank()
                || !row.getOrDefault("workbook:DB_VALIDATION", "").isBlank()
                || !row.getOrDefault("workbook:DB_CONNECTION", "").isBlank()
                || !row.getOrDefault("workbook:DB_QUERY", "").isBlank()
                || !row.getOrDefault("workbook:API_DB_VALIDATION", "").isBlank()
                || !row.getOrDefault("workbook:DB_COLUMN_VALIDATION", "").isBlank();
    }

    private void captureRunnerVariables(Map<String, String> row, String responseBody, Map<String, String> variables) {
        String captureText = row.getOrDefault("workbook:Captured Variables", "");
        if (captureText.isBlank() || responseBody == null || responseBody.isBlank()) {
            return;
        }
        Object responseJson = new JSONTokener(responseBody).nextValue();
        for (String capture : captureText.split(";")) {
            int equals = capture.indexOf('=');
            if (equals <= 0 || equals == capture.length() - 1) {
                continue;
            }
            String name = normalizeVariableName(capture.substring(0, equals));
            String path = capture.substring(equals + 1).trim();
            if (name.isBlank() || path.isBlank()) {
                continue;
            }
            try {
                Object actual = extractJsonPathValue(responseJson, path);
                String value = actual == null || actual == JSONObject.NULL ? "" : String.valueOf(actual);
                variables.put(name, value);
                savedVariables.put(name, value);
            } catch (Exception ignored) {
                // Optional captures should not hide the actual step validation result.
            }
        }
        Platform.runLater(this::refreshVariablesView);
    }

    private WebTestRunReport executeRunnerWebTest(Map<String, String> row) throws Exception {
        JSONObject config = new JSONObject(row.get("workbook:WEB_TEST"));
        WebTestCase testCase = new WebTestCase();
        testCase.testName = resolveVariables(config.optString("testName", row.getOrDefault("step", "Web Test")));
        testCase.startUrl = resolveVariables(config.optString("startUrl"));
        JSONArray steps = config.optJSONArray("steps");
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("WEB_TEST step does not contain recorded web steps.");
        }
        for (int i = 0; i < steps.length(); i++) {
            JSONObject item = steps.optJSONObject(i);
            if (item == null) {
                continue;
            }
            WebTestStep step = new WebTestStep();
            step.action = item.optString("action");
            if ("Flow Variable".equalsIgnoreCase(step.action)) {
                String variableName = normalizeVariableName(firstNonBlank(item.optString("flowVariableName"), item.optString("note"), item.optString("value")));
                String value = resolveVariables(item.optString("value"));
                if (!variableName.isBlank()) {
                    savedVariables.put(variableName, value);
                    savedVariablePaths.put(variableName, "web-runner-flow:" + (i + 1));
                    savedVariableTypes.put(variableName, "Web Flow Variable");
                }
                step.selector = "";
                step.value = value;
                step.note = variableName;
            } else {
                step.selector = resolveVariables(item.optString("selector"));
                step.value = "Get Text".equalsIgnoreCase(step.action)
                        ? item.optString("value")
                        : resolveVariables(item.optString("value"));
                step.note = item.optString("note");
            }
            step.suggested = item.optBoolean("suggested");
            testCase.steps.add(step);
        }
        boolean headless = config.optBoolean("headless", false);
        int slowMoMillis = Math.max(0, config.optInt("slowMoMillis", 0));
        return playwrightRecorderController.runTest(testCase, headless, slowMoMillis);
    }

    private void runRunnerApiFieldValidation(Map<String, String> row, String responseBody,
                                             Map<String, String> variables, TestSuiteStepResult result) {
        String validationJson = row.getOrDefault("workbook:API_FIELD_VALIDATION", "");
        if (validationJson.isBlank()) {
            return;
        }
        JSONArray validations = parseOptionalJsonObject(validationJson).optJSONArray("validations");
        if (validations == null) {
            validations = parseOptionalJsonArray(validationJson);
        }
        Object responseJson = responseBody == null || responseBody.isBlank()
                ? new JSONObject()
                : new JSONTokener(responseBody).nextValue();
        for (int i = 0; i < validations.length(); i++) {
            JSONObject validation = validations.optJSONObject(i);
            if (validation == null) {
                continue;
            }
            String path = validation.optString("jsonPath");
            Object actual = extractJsonPathValue(responseJson, path);
            String actualValue = actual == null || actual == JSONObject.NULL ? "" : String.valueOf(actual);
            String actualType = jsonValueType(actual);
            String nullRule = validation.optString("nullValidation");
            String typeRule = validation.optString("typeValidation");
            String expected = resolveRunnerVariables(validation.optString("expectedValueOrVariable"), variables);
            List<String> errors = fieldValidationErrors(actualType, actualValue, nullRule, typeRule, expected);
            boolean passed = errors.isEmpty();
            result.addValidation(path, "Null: " + nullRule + ", Type: " + typeRule,
                    expected, actualValue, passed, String.join(", ", errors));
            result.passed = result.passed && passed;
        }
    }

    private void runRunnerJsonCompare(Map<String, String> row, String responseBody, TestSuiteStepResult result) throws Exception {
        String compareJson = row.getOrDefault("workbook:JSON_COMPARE", "");
        if (compareJson.isBlank()) {
            return;
        }
        JSONObject config = new JSONObject(compareJson);
        JSONObject expectedResponse = config.optJSONObject("expectedResponse");
        if (expectedResponse == null) {
            result.passed = false;
            result.addValidation("JSON_COMPARE", "JSON Compare", "", "",
                    false, "JSON_COMPARE step does not contain expectedResponse details.");
            return;
        }
        Path expectedPath = resolveWorkbookRelativePath(selectedWorkbookPath(),
                expectedResponse.optString("path"), expectedResponse.optString("relativePath"));
        String expected = Files.readString(expectedPath, StandardCharsets.UTF_8);
        boolean strict = "STRICT".equalsIgnoreCase(config.optString("compareMode"))
                || "Strict".equalsIgnoreCase(config.optString("compareMode"));
        List<Object[]> compareResults = comparator.compare(expected, responseBody, strict, true);
        boolean mismatch = false;
        for (Object[] compareResult : compareResults) {
            String type = valueAt(compareResult, 0);
            boolean passed = "Match".equals(type) || "Message".equals(type);
            if (!passed) {
                mismatch = true;
            }
            result.addValidation(valueAt(compareResult, 1), "JSON " + type,
                    valueAt(compareResult, 2), valueAt(compareResult, 3),
                    passed, passed ? "" : "JSON comparison mismatch");
        }
        if (mismatch) {
            result.passed = false;
            result.status = "Failed";
        }
    }

    private void runRunnerDbValidation(Map<String, String> row, String responseBody,
                                       Map<String, String> variables, TestSuiteStepResult result) throws Exception {
        boolean hasDbValidation = !row.getOrDefault("workbook:DB_VALIDATION", "").isBlank()
                || !row.getOrDefault("workbook:DB_CONNECTION", "").isBlank()
                || !row.getOrDefault("workbook:DB_QUERY", "").isBlank()
                || !row.getOrDefault("workbook:API_DB_VALIDATION", "").isBlank()
                || !row.getOrDefault("workbook:DB_COLUMN_VALIDATION", "").isBlank();
        if (!hasDbValidation) {
            return;
        }

        JSONObject dbValidation = parseOptionalJsonObject(row.get("workbook:DB_VALIDATION"));
        String sqlTemplate = row.getOrDefault("workbook:DB_QUERY", "");
        if (sqlTemplate.isBlank()) {
            sqlTemplate = dbValidation.optString("sqlQuery");
        }
        String sqlQuery = resolveRunnerVariables(sqlTemplate, variables);
        DbConnectionConfig config = runnerDbConnectionConfig(selectedWorkbookPath(), row.get("workbook:DB_CONNECTION"));

        JSONArray apiDbValidations = parseOptionalJsonArray(row.get("workbook:API_DB_VALIDATION"));
        if (!apiDbValidations.isEmpty()) {
            List<DbValidationRule> rules = new ArrayList<>();
            for (int i = 0; i < apiDbValidations.length(); i++) {
                JSONObject json = apiDbValidations.optJSONObject(i);
                if (json == null) {
                    continue;
                }
                DbValidationRule rule = new DbValidationRule();
                rule.apiField = json.optString("apiField");
                rule.dbColumn = json.optString("dbColumn");
                rule.operator = json.optString("operator", "=");
                rule.description = json.optString("description");
                rules.add(rule);
            }
            if (!rules.isEmpty()) {
                DbValidationReport dbReport = dbValidationService.validate(config, sqlQuery, rules, responseBody, variables);
                for (DbValidationResult dbResult : dbReport.results) {
                    result.addValidation(dbResult.field, "API-DB " + dbResult.operator,
                            dbResult.expectedValue, dbResult.actualValue,
                            dbResult.passed, dbResult.message);
                    result.passed = result.passed && dbResult.passed;
                }
            }
        }

        JSONArray dbColumnValidations = parseOptionalJsonArray(row.get("workbook:DB_COLUMN_VALIDATION"));
        JSONArray legacyColumnValidations = dbValidation.optJSONArray("dbColumnValidations");
        if (dbColumnValidations.isEmpty() && legacyColumnValidations != null) {
            dbColumnValidations = legacyColumnValidations;
        }
        if (!dbColumnValidations.isEmpty()) {
            List<Map<String, Object>> rows = dbValidationService.executeQuery(config, sqlQuery, responseBody, variables);
            for (int i = 0; i < dbColumnValidations.length(); i++) {
                JSONObject validation = dbColumnValidations.optJSONObject(i);
                if (validation == null) {
                    continue;
                }
                Object actual = dbColumnActualValue(rows, validation.optString("dbColumnName"));
                String actualType = dbValueType(actual);
                String actualValue = actual == null ? "" : String.valueOf(actual);
                String expected = resolveRunnerVariables(validation.optString("expectedValueOrVariable"), variables);
                List<String> errors = dbColumnValidationErrors(actualType, actualValue,
                        validation.optString("nullValidation"), validation.optString("typeValidation"), expected);
                boolean passed = errors.isEmpty();
                result.addValidation(validation.optString("dbColumnName"),
                        "DB Column Null: " + validation.optString("nullValidation")
                                + ", Type: " + validation.optString("typeValidation"),
                        expected, actualValue, passed, String.join(", ", errors));
                result.passed = result.passed && passed;
            }
        }
        if (!result.passed) {
            result.status = "Failed";
        }
    }

    private ApiRequest buildRunnerApiRequest(Map<String, String> row, String body) {
        JSONObject hitRequest = row.getOrDefault("workbook:Hit Request", "").isBlank()
                ? new JSONObject()
                : new JSONObject(row.get("workbook:Hit Request"));
        ApiRequest request = new ApiRequest();
        request.method = hitRequest.optString("method", methodBox == null ? "GET" : methodBox.getValue());
        request.url = resolveVariables(hitRequest.optString("endpoint", endpointField == null ? "" : endpointField.getText()));
        request.headers = resolveHeaderVariables(parseHeaders(hitRequest.optString("headersText", "")));
        request.body = resolveVariables(body == null ? "" : body);
        request.token = tokenField == null ? "" : resolveVariables(tokenField.getText());
        applyApiTransportSettings(request);
        return request;
    }

    private void updateTestSuiteRowStatus(Map<String, String> rowSnapshot, String status) {
        Platform.runLater(() -> {
            for (Map<String, String> row : testSuiteRows) {
                if (Objects.equals(row.get("suite"), rowSnapshot.get("suite"))
                        && Objects.equals(row.get("case"), rowSnapshot.get("case"))
                        && Objects.equals(row.get("step"), rowSnapshot.get("step"))) {
                    row.put("status", status);
                    break;
                }
            }
            testSuiteStepsTable.refresh();
        });
    }

    private void stopTestSuiteRunnerExecution() {
        testSuiteStopRequested.set(true);
        shutdownTestSuiteExecutor();
        for (Map<String, String> row : testSuiteRows) {
            if ("Queued".equals(row.get("status")) || "Running".equals(row.get("status"))) {
                row.put("status", "Stopped");
            }
        }
        testSuiteStepsTable.refresh();
        testSuiteRunnerStatusLabel.setText("Stop requested for Test Suite Runner execution.");
    }

    private void shutdownTestSuiteExecutor() {
        if (testSuiteRunnerExecutor != null) {
            testSuiteRunnerExecutor.shutdownNow();
            testSuiteRunnerExecutor = null;
        }
    }

    private String workbookNameWithoutExtension(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".xlsx") ? fileName.substring(0, fileName.length() - 5) : fileName;
    }

    private String readFirstWorkbookSheetName(Path workbookPath) throws Exception {
        List<WorkbookSheet> sheets = readWorkbookSheets(workbookPath);
        if (sheets.isEmpty()) {
            throw new IllegalArgumentException("No sheets were found in the selected workbook.");
        }
        return sheets.get(0).name;
    }

    private List<WorkbookSheet> readWorkbookSheets(Path workbookPath) throws Exception {
        return readWorkbookSheets(readWorkbookEntries(workbookPath));
    }

    private List<WorkbookSheet> readWorkbookSheets(Map<String, byte[]> entries) throws Exception {
        byte[] workbookBytes = entries.get("xl/workbook.xml");
        if (workbookBytes == null) {
            throw new IllegalArgumentException("Selected file does not contain an Excel workbook definition.");
        }

        Map<String, String> relationships = readWorkbookRelationships(entries);
        Document workbook = parseXml(workbookBytes);
        NodeList sheetNodes = workbook.getElementsByTagNameNS("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheet");
        if (sheetNodes.getLength() == 0) {
            sheetNodes = workbook.getElementsByTagName("sheet");
        }

        List<WorkbookSheet> sheets = new ArrayList<>();
        for (int i = 0; i < sheetNodes.getLength(); i++) {
            Element sheet = (Element) sheetNodes.item(i);
            String name = sheet.getAttribute("name");
            String relationshipId = sheet.getAttributeNS("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id");
            if (relationshipId == null || relationshipId.isBlank()) {
                relationshipId = sheet.getAttribute("r:id");
            }
            String path = relationships.get(relationshipId);
            if ((path == null || path.isBlank()) && entries.containsKey("xl/worksheets/sheet" + (i + 1) + ".xml")) {
                path = "xl/worksheets/sheet" + (i + 1) + ".xml";
            }
            if (name != null && !name.isBlank() && path != null && !path.isBlank()) {
                sheets.add(new WorkbookSheet(name, path));
            }
        }
        return sheets;
    }

    private Map<String, String> readWorkbookRelationships(Map<String, byte[]> entries) throws Exception {
        byte[] relationshipBytes = entries.get("xl/_rels/workbook.xml.rels");
        if (relationshipBytes == null) {
            return Map.of();
        }
        Document relationshipsDocument = parseXml(relationshipBytes);
        NodeList relationships = relationshipsDocument.getElementsByTagName("Relationship");
        Map<String, String> targets = new LinkedHashMap<>();
        for (int i = 0; i < relationships.getLength(); i++) {
            Element relationship = (Element) relationships.item(i);
            String id = relationship.getAttribute("Id");
            String target = normalizeWorkbookRelationshipTarget(relationship.getAttribute("Target"));
            if (!id.isBlank() && !target.isBlank()) {
                targets.put(id, target);
            }
        }
        return targets;
    }

    private String normalizeWorkbookRelationshipTarget(String target) {
        if (target == null || target.isBlank()) {
            return "";
        }
        String normalized = target.replace('\\', '/');
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        } else if (!normalized.startsWith("xl/")) {
            normalized = "xl/" + normalized;
        }
        return normalized;
    }

    private String selectedWorksheetPath(Map<String, byte[]> entries) throws Exception {
        List<WorkbookSheet> sheets = readWorkbookSheets(entries);
        if (sheets.isEmpty()) {
            if (entries.containsKey("xl/worksheets/sheet1.xml")) {
                return "xl/worksheets/sheet1.xml";
            }
            throw new IllegalArgumentException("No worksheets were found in the selected workbook.");
        }
        String selected = currentTestCaseName();
        for (WorkbookSheet sheet : sheets) {
            if (sheet.name.equals(selected)) {
                return sheet.path;
            }
        }
        return sheets.get(0).path;
    }

    private Document parseXml(byte[] bytes) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            return factory.newDocumentBuilder().parse(input);
        }
    }

    private void appendRowsToWorkbook(Path workbookPath, List<List<String>> rows) throws Exception {
        if (rows.isEmpty()) {
            return;
        }
        Map<String, byte[]> entries = readWorkbookEntries(workbookPath);
        String worksheetPath = selectedWorksheetPath(entries);
        byte[] sheetBytes = entries.get(worksheetPath);
        if (sheetBytes == null) {
            throw new IllegalArgumentException("The selected workbook does not contain " + worksheetPath + ".");
        }
        String sheetXml = new String(sheetBytes, StandardCharsets.UTF_8);
        List<String> sharedStrings = readSharedStrings(entries);
        List<List<String>> rowsToAppend = new ArrayList<>();
        boolean needsRunnerHeader = !hasRunnerHeaderRow(sheetXml, sharedStrings);
        if (needsRunnerHeader) {
            rowsToAppend.add(runnerWorkbookHeaderColumns());
        }
        rowsToAppend.addAll(rows);
        entries.put(worksheetPath,
                appendInlineStringRows(sheetXml, rowsToAppend, needsRunnerHeader).getBytes(StandardCharsets.UTF_8));
        ensureRunnerWorkbookStyles(entries);

        Path tempWorkbook = Files.createTempFile(workbookPath.getParent(), "testweave-runner-", ".xlsx");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(tempWorkbook))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                zip.write(entry.getValue());
                zip.closeEntry();
            }
        }
        Files.move(tempWorkbook, workbookPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private Map<String, byte[]> readWorkbookEntries(Path workbookPath) throws Exception {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        try (ZipFile workbookZip = new ZipFile(workbookPath.toFile())) {
            var zipEntries = workbookZip.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                if (!entry.isDirectory()) {
                    try (var input = workbookZip.getInputStream(entry)) {
                        entries.put(entry.getName(), input.readAllBytes());
                    }
                }
            }
        }
        return entries;
    }

    private String appendInlineStringRows(String sheetXml, List<List<String>> rows, boolean firstRowIsHeader) {
        int nextRow = findMaxSheetRow(sheetXml) + 1;
        StringBuilder rowXml = new StringBuilder();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);
            boolean headerRow = firstRowIsHeader && rowIndex == 0;
            rowXml.append("                        <row r=\"").append(nextRow).append("\">\n");
            for (int column = 0; column < row.size(); column++) {
                rowXml.append("                          <c r=\"")
                        .append(excelColumnName(column + 1)).append(nextRow)
                        .append("\"").append(headerRow ? " s=\"1\"" : "")
                        .append(" t=\"inlineStr\"><is><t>")
                        .append(escapeXml(row.get(column) == null ? "" : row.get(column)))
                        .append("</t></is></c>\n");
            }
            rowXml.append("                        </row>\n");
            nextRow++;
        }
        if (sheetXml.contains("</sheetData>")) {
            return sheetXml.replace("</sheetData>", rowXml + "                      </sheetData>");
        }
        if (sheetXml.contains("<sheetData/>")) {
            return sheetXml.replace("<sheetData/>", "<sheetData>\n" + rowXml + "                      </sheetData>");
        }
        return sheetXml.replace("</worksheet>", "                      <sheetData>\n" + rowXml + "                      </sheetData>\n</worksheet>");
    }

    private List<String> runnerWorkbookHeaderColumns() {
        return List.of("Test Suite", "Test Case", "Test Step", "Hit Request", "Request Payload",
                "Captured Variables", "API_FIELD_VALIDATION", "Variable Dependencies", "JSON_COMPARE",
                "DB_VALIDATION", "DB_CONNECTION", "DB_QUERY", "API_DB_VALIDATION", "DB_COLUMN_VALIDATION",
                "WEB_TEST", "PERFORMANCE_TEST", "Run", "Execution Mode", "Status");
    }

    private void writeTestSuiteRowsToWorkbook(Path workbookPath) throws Exception {
        writeRowsToWorkbook(workbookPath, testSuiteRows);
    }

    private void writeRowsToWorkbook(Path workbookPath, List<Map<String, String>> rowsToWrite) throws Exception {
        Map<String, byte[]> entries = readWorkbookEntries(workbookPath);
        String worksheetPath = selectedWorksheetPath(entries);
        byte[] sheetBytes = entries.get(worksheetPath);
        if (sheetBytes == null) {
            throw new IllegalArgumentException("The selected workbook does not contain " + worksheetPath + ".");
        }
        String sheetXml = new String(sheetBytes, StandardCharsets.UTF_8);
        List<List<String>> rows = new ArrayList<>();
        rows.add(runnerWorkbookHeaderColumns());
        for (Map<String, String> tableRow : rowsToWrite) {
            rows.add(buildWorkbookRowFromTableRow(tableRow));
        }
        entries.put(worksheetPath, replaceSheetData(sheetXml, rows).getBytes(StandardCharsets.UTF_8));
        ensureRunnerWorkbookStyles(entries);

        Path tempWorkbook = Files.createTempFile(workbookPath.getParent(), "testweave-runner-update-", ".xlsx");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(tempWorkbook))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                zip.write(entry.getValue());
                zip.closeEntry();
            }
        }
        Files.move(tempWorkbook, workbookPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private List<String> buildWorkbookRowFromTableRow(Map<String, String> tableRow) {
        List<String> rowValues = new ArrayList<>();
        for (String header : runnerWorkbookHeaderColumns()) {
            rowValues.add(switch (header) {
                case "Test Suite" -> tableRow.getOrDefault("suite", "");
                case "Test Case" -> tableRow.getOrDefault("case", "");
                case "Test Step" -> tableRow.getOrDefault("step", "");
                case "Run" -> tableRow.getOrDefault("selected", "true");
                case "Execution Mode" -> tableRow.getOrDefault("executionMode", "Sequential");
                case "Status" -> tableRow.getOrDefault("status", "Ready");
                case "API_FIELD_VALIDATION" -> "Field Validation".equals(tableRow.get("type"))
                        ? tableRow.getOrDefault("workbook:" + header, tableRow.getOrDefault("details", "")) : tableRow.getOrDefault("workbook:" + header, "");
                case "JSON_COMPARE" -> "JSON Compare".equals(tableRow.get("type"))
                        ? tableRow.getOrDefault("workbook:" + header, tableRow.getOrDefault("details", "")) : tableRow.getOrDefault("workbook:" + header, "");
                case "DB_QUERY" -> "DB Validation".equals(tableRow.get("type"))
                        ? tableRow.getOrDefault("workbook:" + header, tableRow.getOrDefault("details", "")) : tableRow.getOrDefault("workbook:" + header, "");
                case "WEB_TEST" -> "Web Test".equals(tableRow.get("type"))
                        ? tableRow.getOrDefault("workbook:" + header, tableRow.getOrDefault("details", "")) : tableRow.getOrDefault("workbook:" + header, "");
                case "PERFORMANCE_TEST" -> "Performance Test".equals(tableRow.get("type"))
                        ? tableRow.getOrDefault("workbook:" + header, tableRow.getOrDefault("details", "")) : tableRow.getOrDefault("workbook:" + header, "");
                default -> tableRow.getOrDefault("workbook:" + header, "");
            });
        }
        return rowValues;
    }

    private String replaceSheetData(String sheetXml, List<List<String>> rows) {
        String sheetData = buildSheetData(rows);
        if (sheetXml.matches("(?s).*<sheetData>.*?</sheetData>.*")) {
            return sheetXml.replaceFirst("(?s)<sheetData>.*?</sheetData>", Matcher.quoteReplacement(sheetData));
        }
        if (sheetXml.contains("<sheetData/>")) {
            return sheetXml.replace("<sheetData/>", sheetData);
        }
        return sheetXml.replace("</worksheet>", sheetData + "\n</worksheet>");
    }

    private String buildSheetData(List<List<String>> rows) {
        StringBuilder xml = new StringBuilder("                      <sheetData>\n");
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);
            int excelRow = rowIndex + 1;
            boolean headerRow = rowIndex == 0;
            xml.append("                        <row r=\"").append(excelRow).append("\">\n");
            for (int column = 0; column < row.size(); column++) {
                xml.append("                          <c r=\"")
                        .append(excelColumnName(column + 1)).append(excelRow)
                        .append("\"").append(headerRow ? " s=\"1\"" : "")
                        .append(" t=\"inlineStr\"><is><t>")
                        .append(escapeXml(row.get(column) == null ? "" : row.get(column)))
                        .append("</t></is></c>\n");
            }
            xml.append("                        </row>\n");
        }
        xml.append("                      </sheetData>");
        return xml.toString();
    }

    private List<List<String>> readSheetRows(String sheetXml, List<String> sharedStrings) {
        List<List<String>> rows = new ArrayList<>();
        java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*>(.*?)</row>", java.util.regex.Pattern.DOTALL)
                .matcher(sheetXml);
        while (rowMatcher.find()) {
            rows.add(rowValues(rowMatcher.group(1), sharedStrings));
        }
        return rows;
    }

    private List<String> readSharedStrings(Map<String, byte[]> entries) {
        byte[] sharedStringsBytes = entries.get("xl/sharedStrings.xml");
        if (sharedStringsBytes == null) {
            return List.of();
        }
        String sharedStringsXml = new String(sharedStringsBytes, StandardCharsets.UTF_8);
        List<String> values = new ArrayList<>();
        java.util.regex.Matcher stringMatcher = java.util.regex.Pattern
                .compile("<si\\b[^>]*>(.*?)</si>", java.util.regex.Pattern.DOTALL)
                .matcher(sharedStringsXml);
        while (stringMatcher.find()) {
            String itemXml = stringMatcher.group(1);
            StringBuilder value = new StringBuilder();
            java.util.regex.Matcher textMatcher = java.util.regex.Pattern
                    .compile("<t[^>]*>(.*?)</t>", java.util.regex.Pattern.DOTALL)
                    .matcher(itemXml);
            while (textMatcher.find()) {
                value.append(unescapeXml(textMatcher.group(1)));
            }
            values.add(value.toString());
        }
        return values;
    }

    private List<String> rowValues(String rowXml, List<String> sharedStrings) {
        List<String> values = new ArrayList<>();
        java.util.regex.Matcher cellMatcher = java.util.regex.Pattern
                .compile("<c\\b([^>]*)>(.*?)</c>", java.util.regex.Pattern.DOTALL)
                .matcher(rowXml);
        int nextColumnIndex = 0;
        while (cellMatcher.find()) {
            String attributes = cellMatcher.group(1);
            int columnIndex = cellColumnIndex(attributes);
            if (columnIndex < 0) {
                columnIndex = nextColumnIndex;
            }
            while (values.size() < columnIndex) {
                values.add("");
            }
            String value = cellText(attributes, cellMatcher.group(2), sharedStrings);
            if (values.size() == columnIndex) {
                values.add(value);
            } else {
                values.set(columnIndex, value);
            }
            nextColumnIndex = columnIndex + 1;
        }
        return values;
    }

    private int cellColumnIndex(String cellAttributes) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\\br=\"([A-Z]+)\\d+\"")
                .matcher(cellAttributes);
        if (!matcher.find()) {
            return -1;
        }
        int column = 0;
        String letters = matcher.group(1);
        for (int i = 0; i < letters.length(); i++) {
            column = column * 26 + (letters.charAt(i) - 'A' + 1);
        }
        return column - 1;
    }

    private String cellText(String attributes, String cellXml, List<String> sharedStrings) {
        java.util.regex.Matcher inlineMatcher = java.util.regex.Pattern
                .compile("<is>\\s*<t[^>]*>(.*?)</t>\\s*</is>", java.util.regex.Pattern.DOTALL)
                .matcher(cellXml);
        if (inlineMatcher.find()) {
            return unescapeXml(inlineMatcher.group(1));
        }
        java.util.regex.Matcher valueMatcher = java.util.regex.Pattern
                .compile("<v>(.*?)</v>", java.util.regex.Pattern.DOTALL)
                .matcher(cellXml);
        if (!valueMatcher.find()) {
            return "";
        }
        String value = valueMatcher.group(1).trim();
        if (attributes.contains("t=\"s\"")) {
            try {
                int sharedStringIndex = Integer.parseInt(value);
                if (sharedStringIndex >= 0 && sharedStringIndex < sharedStrings.size()) {
                    return sharedStrings.get(sharedStringIndex);
                }
            } catch (NumberFormatException ignored) {
                return "";
            }
        }
        return unescapeXml(value);
    }

    private boolean hasRunnerHeaderRow(String sheetXml, List<String> sharedStrings) {
        java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*>(.*?)</row>", java.util.regex.Pattern.DOTALL)
                .matcher(sheetXml);
        while (rowMatcher.find()) {
            if (isRunnerHeader(rowValues(rowMatcher.group(1), sharedStrings))) {
                return true;
            }
        }
        return false;
    }

    private boolean isRunnerHeader(List<String> values) {
        return values.size() >= 6
                && "Test Suite".equals(values.get(0))
                && "Test Case".equals(values.get(1))
                && "Test Step".equals(values.get(2));
    }

    private boolean isBlankRow(List<String> row) {
        return row.stream().allMatch(value -> value == null || value.isBlank());
    }

    private String runnerStepType(Map<String, String> step) {
        if (!step.getOrDefault("WEB_TEST", "").isBlank()) {
            return "Web Test";
        }
        if (!step.getOrDefault("PERFORMANCE_TEST", "").isBlank()) {
            return "Performance Test";
        }
        if (!step.getOrDefault("JSON_COMPARE", "").isBlank()) {
            return "JSON Compare";
        }
        if (!step.getOrDefault("DB_VALIDATION", "").isBlank()
                || !step.getOrDefault("API_DB_VALIDATION", "").isBlank()
                || !step.getOrDefault("DB_COLUMN_VALIDATION", "").isBlank()) {
            return "DB Validation";
        }
        if (!step.getOrDefault("API_FIELD_VALIDATION", "").isBlank()) {
            return "Field Validation";
        }
        return "API Request";
    }

    private String runnerStepDetails(Map<String, String> step) {
        String type = runnerStepType(step);
        return switch (type) {
            case "Web Test" -> step.getOrDefault("WEB_TEST", "");
            case "Performance Test" -> step.getOrDefault("PERFORMANCE_TEST", "");
            case "JSON Compare" -> step.getOrDefault("JSON_COMPARE", "");
            case "DB Validation" -> step.getOrDefault("DB_QUERY", "");
            case "Field Validation" -> step.getOrDefault("API_FIELD_VALIDATION", "");
            default -> step.getOrDefault("Hit Request", "");
        };
    }

    private int findMaxSheetRow(String sheetXml) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("<row\\b[^>]*\\sr=\"(\\d+)\"")
                .matcher(sheetXml);
        int max = 0;
        while (matcher.find()) {
            max = Math.max(max, Integer.parseInt(matcher.group(1)));
        }
        return max;
    }

    private String excelColumnName(int columnNumber) {
        StringBuilder name = new StringBuilder();
        int current = columnNumber;
        while (current > 0) {
            current--;
            name.insert(0, (char) ('A' + current % 26));
            current /= 26;
        }
        return name.toString();
    }

    private void writeSingleSheetWorkbook(Path workbookPath, String sheetName, String testCase) throws Exception {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(workbookPath))) {
            writeZipEntry(zip, "[Content_Types].xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                      <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
                      <Default Extension="xml" ContentType="application/xml"/>
                      <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
                      <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
                      <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
                    </Types>
                    """);
            writeZipEntry(zip, "_rels/.rels", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
                    </Relationships>
                    """);
            writeZipEntry(zip, "xl/_rels/workbook.xml.rels", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                      <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
                      <Relationship Id="rIdStyles" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
                    </Relationships>
                    """);
            writeZipEntry(zip, "xl/styles.xml", runnerWorkbookStylesXml());
            writeZipEntry(zip, "xl/workbook.xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
                      <sheets><sheet name="%s" sheetId="1" r:id="rId1"/></sheets>
                    </workbook>
                    """.formatted(escapeXml(sheetName)));
            writeZipEntry(zip, "xl/worksheets/sheet1.xml", """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                      <sheetData>
                        <row r="1"><c r="A1" t="inlineStr"><is><t>%s</t></is></c></row>
                      </sheetData>
                    </worksheet>
                    """.formatted(escapeXml(testCase)));
        }
    }

    private void writeZipEntry(ZipOutputStream zip, String name, String content) throws Exception {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private String createSafeExcelSheetName(String value) {
        String sheetName = value.trim().replaceAll("[\\\\/?*\\[\\]:\\p{Cntrl}]", "_").replaceAll("^'+|'+$", "");
        if (sheetName.isBlank()) {
            sheetName = "TestCase";
        }
        return sheetName.length() > 31 ? sheetName.substring(0, 31) : sheetName;
    }

    private void ensureRunnerWorkbookStyles(Map<String, byte[]> entries) {
        entries.put("xl/styles.xml", runnerWorkbookStylesXml().getBytes(StandardCharsets.UTF_8));
    }

    private String runnerWorkbookStylesXml() {
        return """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                  <fonts count="2"><font><sz val="11"/><name val="Calibri"/></font><font><b/><sz val="11"/><name val="Calibri"/></font></fonts>
                  <fills count="1"><fill><patternFill patternType="none"/></fill></fills>
                  <borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders>
                  <cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
                  <cellXfs count="2"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/><xf numFmtId="0" fontId="1" fillId="0" borderId="0" xfId="0"/></cellXfs>
                  <cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles>
                </styleSheet>
                """;
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }

    private String unescapeXml(String value) {
        return value.replace("&apos;", "'").replace("&quot;", "\"").replace("&gt;", ">")
                .replace("&lt;", "<").replace("&amp;", "&");
    }

    private Path selectedWorkbookPath() {
        if (testSuiteWorkbookPathField == null || testSuiteWorkbookPathField.getText().isBlank()) {
            return null;
        }
        Path path = Path.of(testSuiteWorkbookPathField.getText());
        return Files.exists(path) ? path : null;
    }

    private JSONObject parseOptionalJsonObject(String value) {
        if (value == null || value.isBlank()) {
            return new JSONObject();
        }
        try {
            return new JSONObject(value);
        } catch (Exception ignored) {
            return new JSONObject();
        }
    }

    private JSONArray parseOptionalJsonArray(String value) {
        if (value == null || value.isBlank()) {
            return new JSONArray();
        }
        try {
            return new JSONArray(value);
        } catch (Exception ignored) {
            return new JSONArray();
        }
    }

    private String resolveRunnerVariables(String text, Map<String, String> variables) {
        if (text == null) {
            return "";
        }
        String resolved = text;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            resolved = resolved.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return resolveVariables(resolved);
    }

    private Path resolveWorkbookRelativePath(Path workbookPath, String absolutePath, String relativePath) {
        if (absolutePath != null && !absolutePath.isBlank() && Files.exists(Path.of(absolutePath))) {
            return Path.of(absolutePath);
        }
        Path workbookDirectory = workbookPath == null ? null : workbookPath.toAbsolutePath().getParent();
        if (workbookDirectory != null && relativePath != null && !relativePath.isBlank()) {
            Path resolved = workbookDirectory.resolve(relativePath).normalize();
            if (Files.exists(resolved)) {
                return resolved;
            }
        }
        return Path.of(absolutePath == null || absolutePath.isBlank() ? relativePath : absolutePath);
    }

    private DbConnectionConfig runnerDbConnectionConfig(Path workbookPath, String connectionJson) throws Exception {
        JSONObject connection = parseOptionalJsonObject(connectionJson);
        JSONObject json = connection;
        String path = connection.optString("path");
        String relativePath = connection.optString("relativePath");
        if (!path.isBlank() || !relativePath.isBlank()) {
            Path connectionPath = resolveWorkbookRelativePath(workbookPath, path, relativePath);
            if (Files.exists(connectionPath)) {
                json = new JSONObject(Files.readString(connectionPath, StandardCharsets.UTF_8));
            }
        }
        DbConnectionConfig config = new DbConnectionConfig();
        config.databaseType = json.optString("databaseType", connection.optString("databaseType", "MySQL"));
        config.jdbcUrl = json.optString("jdbcUrl", connection.optString("jdbcUrl"));
        config.username = json.optString("username", connection.optString("username"));
        config.password = json.optString("password", connection.optString("password"));
        config.driverClass = json.optString("driverClass", connection.optString("driverClass"));
        return config;
    }

    private List<String> buildWorkbookRow(String suite, String testCase, String step, String type, String details) {
        String hitRequest = "";
        String requestPayload = bodyArea == null ? "" : bodyArea.getText();
        String jsonCompare = "";
        String dbValidation = "";
        String dbConnection = "";
        String dbQuery = "";
        String apiDbValidation = "";
        String dbColumnValidation = "";
        String webTest = "";
        String performanceTest = "";
        if ("JSON Compare".equals(type)) {
            jsonCompare = new JSONObject().put("expectedJsonFile", expectedJsonPathField.getText())
                    .put("mode", compareModeBox.getValue()).toString();
        } else if ("DB Validation".equals(type)) {
            dbValidation = details;
            dbConnection = dbConnectionFilePath == null ? "" : dbConnectionFilePath.toString();
            dbQuery = dbQueryArea == null ? "" : dbQueryArea.getText();
            apiDbValidation = new JSONArray(dbRuleRows).toString();
            dbColumnValidation = new JSONArray(dbColumnValidationRows).toString();
        } else if ("Web Test".equals(type)) {
            webTest = new JSONObject().put("testName", webTestNameField.getText())
                    .put("startUrl", webStartUrlField.getText())
                    .put("headless", webHeadlessCheck != null && webHeadlessCheck.isSelected())
                    .put("slowMoMillis", webSlowMoCheck != null && webSlowMoCheck.isSelected() ? 250 : 0)
                    .put("stepType", "WEB_TEST")
                    .put("steps", new JSONArray(webStepRows)).toString();
        } else if ("Performance Test".equals(type)) {
            hitRequest = new JSONObject().put("method", methodBox == null ? "GET" : methodBox.getValue())
                    .put("endpoint", endpointField == null ? "" : endpointField.getText())
                    .put("headersText", headersArea == null ? "" : headersArea.getText()).toString();
            performanceTest = new JSONObject().put("threads", perfThreadsSpinner.getValue())
                    .put("iterationsPerThread", perfIterationsSpinner.getValue())
                    .put("body", perfBodyArea.getText()).toString();
        } else if ("Field Validation".equals(type)) {
            hitRequest = new JSONObject().put("method", methodBox == null ? "GET" : methodBox.getValue())
                    .put("endpoint", endpointField == null ? "" : endpointField.getText())
                    .put("headersText", headersArea == null ? "" : headersArea.getText()).toString();
        }
        return List.of(suite, testCase, step, hitRequest, requestPayload, "",
                "Field Validation".equals(type) ? new JSONArray(fieldValidationRows).toString() : "",
                "", jsonCompare, dbValidation, dbConnection, dbQuery, apiDbValidation,
                dbColumnValidation, webTest, performanceTest, "true", "Sequential", "Ready");
    }

    private void refreshTestSuiteRunnerSteps(Path workbookPath) {
        testSuiteRows.clear();
        try {
            List<Map<String, String>> steps = readTestSuiteRunnerSteps(workbookPath);
            for (Map<String, String> step : steps) {
                testSuiteRows.add(workbookStepToTableRow(step));
            }
            testSuiteRunnerStatusLabel.setText(steps.isEmpty()
                    ? "No test steps found in the imported workbook."
                    : "Loaded " + steps.size() + " test step(s).");
        } catch (Exception e) {
            testSuiteRunnerStatusLabel.setText("Could not load test steps: " + e.getMessage());
        }
    }

    private List<Map<String, String>> readTestSuiteRunnerSteps(Path workbookPath) throws Exception {
        Map<String, byte[]> entries = readWorkbookEntries(workbookPath);
        String worksheetPath = selectedWorksheetPath(entries);
        byte[] sheetBytes = entries.get(worksheetPath);
        if (sheetBytes == null) {
            return List.of();
        }
        String sheetXml = new String(sheetBytes, StandardCharsets.UTF_8);
        List<String> sharedStrings = readSharedStrings(entries);
        return readTestSuiteRunnerSteps(sheetBytes, sharedStrings);
    }

    private List<Map<String, String>> readTestSuiteRunnerSteps(byte[] sheetBytes, List<String> sharedStrings) {
        String sheetXml = new String(sheetBytes, StandardCharsets.UTF_8);
        List<List<String>> rows = readSheetRows(sheetXml, sharedStrings);
        List<String> header = null;
        List<Map<String, String>> steps = new ArrayList<>();
        for (List<String> row : rows) {
            if (header == null) {
                if (isRunnerHeader(row)) {
                    header = row;
                }
                continue;
            }
            if (isBlankRow(row) || isRunnerHeader(row)) {
                continue;
            }
            Map<String, String> step = new LinkedHashMap<>();
            for (int column = 0; column < header.size(); column++) {
                step.put(header.get(column), column < row.size() ? row.get(column) : "");
            }
            if (!step.getOrDefault("Test Step", "").isBlank()) {
                steps.add(step);
            }
        }
        return steps;
    }

    private Map<String, String> workbookStepToTableRow(Map<String, String> step) {
        Map<String, String> tableRow = row("selected", step.getOrDefault("Run", "true"),
                "suite", step.getOrDefault("Test Suite", ""),
                "case", step.getOrDefault("Test Case", ""),
                "step", step.getOrDefault("Test Step", ""),
                "executionMode", step.getOrDefault("Execution Mode", "Sequential"),
                "type", runnerStepType(step),
                "details", runnerStepDetails(step),
                "status", step.getOrDefault("Status", "Ready"));
        for (Map.Entry<String, String> entry : step.entrySet()) {
            tableRow.put("workbook:" + entry.getKey(), entry.getValue());
        }
        return tableRow;
    }

    private String selectedFieldValidationSummary() {
        long selected = fieldValidationRows.stream().filter(this::isSelected).count();
        if (selected == 0) {
            return "No field rows selected yet";
        }
        return selected + " selected field validation(s)";
    }

    private void sendRequest() {
        executeCurrentPostmanPreRequestScript();
        boolean bearerAuthSelected = authTypeBox != null && "Bearer Token".equals(authTypeBox.getValue());
        String rawBearerToken = bearerAuthSelected && tokenField != null
                ? tokenField.getText().trim() : "";
        ApiRequest request = buildApiRequest(bodyArea.getText());
        if (request.url == null || request.url.isBlank()) {
            showWarning("API Tester", "Enter an endpoint before sending the request.");
            return;
        }
        List<String> unresolvedVariables = unresolvedRequestVariables(request);
        if (!unresolvedVariables.isEmpty()) {
            showWarning("Unresolved Variables", "Set these variables before sending: "
                    + String.join(", ", unresolvedVariables));
            return;
        }
        apiStatusLabel.setText("Sending...");
        Task<ApiResponse> task = new Task<>() {
            @Override
            protected ApiResponse call() throws Exception {
                hydrateOAuth2TokenIfNeeded(request);
                hydratePostmanBearerTokenIfNeeded(request, bearerAuthSelected, rawBearerToken);
                return apiService.sendRequest(request);
            }
        };
        task.setOnSucceeded(e -> {
            lastResponse = task.getValue();
            captureAccessTokenFromResponse(lastResponse);
            renderResponse(lastResponse);
            try {
                captureCurrentPostmanTestScript(lastResponse);
                apiStatusLabel.setText("Response received");
            } catch (Exception scriptError) {
                apiStatusLabel.setText("Response received; Postman test script failed");
                showError("Postman Test Script Failed", scriptError);
            }
        });
        task.setOnFailed(e -> {
            apiStatusLabel.setText("Request failed");
            showError("Request Failed", task.getException());
        });
        start(task);
    }

    private void executeCurrentPostmanPreRequestScript() {
        if (currentPostmanRequestNode == null || preRequestScriptArea == null) {
            return;
        }
        String script = preRequestScriptArea.getText();
        int updated = executePostmanJavaScript(currentPostmanRequestNode, "prerequest", script, null, null);
        if (updated > 0) {
            refreshVariablesViewSafely();
        }
    }

    private void captureCurrentPostmanTestScript(ApiResponse response) {
        if (currentPostmanRequestNode == null || testScriptArea == null || response == null
                || response.rawBody == null || response.rawBody.isBlank()) {
            return;
        }
        int captured = executePostmanJavaScript(currentPostmanRequestNode, "test", testScriptArea.getText(), response, null);
        if (captured > 0) {
            refreshVariablesViewSafely();
        }
    }

    private void runApiAiAnalysisForLastResponse() {
        if (activeApiAiHermesSession == null || nullToBlank(activeApiAiHermesSession.sessionId()).isBlank()) {
            showWarning("Hermes Agent", "Connect a Hermes session in Config before running AI Analysis.");
            updateApiAiConnectionLabels();
            return;
        }
        if (lastResponse == null || lastResponse.rawBody == null || lastResponse.rawBody.isBlank()) {
            showWarning("API AI Agent", "Send an API request first, then click AI Analysis.");
            return;
        }
        ApiRequest request = buildApiRequest(bodyArea == null ? "" : bodyArea.getText());
        maybeShowApiAiSuggestions(request, lastResponse);
    }

    private ApiRequest buildApiRequest(String body) {
        ApiRequest request = new ApiRequest();
        request.url = endpointField == null ? "" : resolveVariables(endpointField.getText().trim());
        request.method = methodBox == null ? "GET" : methodBox.getValue();
        request.headers = resolveHeaderVariables(parseHeaders(headersArea == null ? "" : headersArea.getText()));
        request.body = body == null ? "" : resolveVariables(body);
        request.token = authTypeBox != null && "Bearer Token".equals(authTypeBox.getValue()) && tokenField != null
                ? resolveVariables(tokenField.getText().trim()) : "";
        if (authTypeBox != null && "OAuth2".equals(authTypeBox.getValue()) && tokenField != null) {
            request.token = resolveVariables(tokenField.getText().trim());
        }
        applyApiTransportSettings(request);
        if (currentPostmanRequestNode != null) {
            request.bodyMode = currentPostmanBodyMode;
            request.multipartParts = resolveRequestBodyParts(currentPostmanMultipartParts);
            request.binaryFilePath = resolveVariables(currentPostmanBinaryFilePath);
        }
        return request;
    }

    private ApiRequest buildPostmanApiRequest(PostmanCollectionNode node) {
        if (node == null || !node.isRequest()) {
            throw new IllegalArgumentException("Postman request node is required.");
        }
        JSONObject postmanRequest = node.request;
        JSONObject postmanBody = postmanRequest.optJSONObject("body");
        ApiRequest request = new ApiRequest();
        request.url = resolveVariables(postmanRequestUrl(node));
        request.method = firstNonBlank(postmanRequest.optString("method"), "GET").toUpperCase();
        request.headers = resolveHeaderVariables(parseHeaders(postmanHeadersText(postmanRequest)));
        request.body = resolveVariables(postmanBodyText(postmanBody));
        request.bodyMode = postmanBody == null ? "" : postmanBody.optString("mode");
        request.multipartParts = resolveRequestBodyParts(postmanMultipartParts(postmanBody));
        request.binaryFilePath = resolveVariables(postmanBinaryFilePath(postmanBody));
        applyApiTransportSettings(request);
        applyPostmanAuth(request, postmanRequest);
        return request;
    }

    private void applyPostmanAuth(ApiRequest request, JSONObject postmanRequest) {
        JSONObject auth = postmanRequest == null ? null : postmanRequest.optJSONObject("auth");
        String type = postmanAuthType(auth);
        if ("bearer".equalsIgnoreCase(type)) {
            request.token = resolveVariables(postmanAuthValue(auth, "bearer", "token"));
            return;
        }
        if ("basic".equalsIgnoreCase(type)) {
            String username = resolveVariables(postmanAuthValue(auth, "basic", "username"));
            String password = resolveVariables(postmanAuthValue(auth, "basic", "password"));
            putHeaderIfMissing(request.headers, "Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)));
            return;
        }
        if ("apikey".equalsIgnoreCase(type)) {
            String key = resolveVariables(postmanAuthValue(auth, "apikey", "key"));
            String value = resolveVariables(postmanAuthValue(auth, "apikey", "value"));
            String in = postmanAuthValue(auth, "apikey", "in");
            if ("header".equalsIgnoreCase(in)) {
                putHeaderIfMissing(request.headers, key, value);
            } else if ("query".equalsIgnoreCase(in) && !key.isBlank()) {
            request.url = appendQueryParameter(request.url, key, value);
            }
        }
    }

    private void applyApiTransportSettings(ApiRequest request) {
        if (request == null) {
            return;
        }
        request.sslVerificationDisabled = sslVerificationDisabledCheck != null && sslVerificationDisabledCheck.isSelected();
        request.trustStorePath = trustStorePathField == null ? "" : resolveVariables(trustStorePathField.getText().trim());
        request.trustStorePassword = trustStorePasswordField == null ? "" : resolveVariables(trustStorePasswordField.getText());
        request.keyStorePath = keyStorePathField == null ? "" : resolveVariables(keyStorePathField.getText().trim());
        request.keyStorePassword = keyStorePasswordField == null ? "" : resolveVariables(keyStorePasswordField.getText());
        request.proxyEnabled = proxyEnabledCheck != null && proxyEnabledCheck.isSelected();
        request.proxyScheme = proxySchemeBox == null ? "http" : proxySchemeBox.getValue();
        request.proxyHost = proxyHostField == null ? "" : resolveVariables(proxyHostField.getText().trim());
        request.proxyPort = parsePositiveInt(proxyPortField == null ? "" : resolveVariables(proxyPortField.getText().trim()), 0);
        request.proxyUsername = proxyUsernameField == null ? "" : resolveVariables(proxyUsernameField.getText().trim());
        request.proxyPassword = proxyPasswordField == null ? "" : resolveVariables(proxyPasswordField.getText());
    }

    private int parsePositiveInt(String value, int fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return Math.max(0, Integer.parseInt(value.trim()));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private void hydrateOAuth2TokenIfNeeded(ApiRequest request) throws Exception {
        if (authTypeBox == null || !"OAuth2".equals(authTypeBox.getValue()) || request == null) {
            return;
        }
        if (request.token != null && !request.token.isBlank()) {
            return;
        }
        JSONObject token = requestOAuth2Token();
        request.token = token.optString("access_token");
    }

    private void fetchOAuth2Token() {
        oauthStatusLabel.setText("Fetching token...");
        Task<JSONObject> task = new Task<>() {
            @Override
            protected JSONObject call() throws Exception {
                return requestOAuth2Token();
            }
        };
        task.setOnSucceeded(e -> {
            JSONObject token = task.getValue();
            String accessToken = token.optString("access_token");
            tokenField.setText(accessToken);
            authTypeBox.setValue("OAuth2");
            updateAuthControls();
            oauthStatusLabel.setText(accessToken.isBlank() ? "Token response received" : "Access token saved");
        });
        task.setOnFailed(e -> {
            oauthStatusLabel.setText("Token request failed");
            showError("OAuth2 Token Failed", task.getException());
        });
        start(task);
    }

    private JSONObject requestOAuth2Token() {
        String tokenUrl = resolveVariables(oauthTokenUrlField == null ? "" : oauthTokenUrlField.getText().trim());
        if (tokenUrl.isBlank()) {
            throw new IllegalArgumentException("Enter an OAuth2 token URL.");
        }
        Map<String, String> params = new LinkedHashMap<>();
        String grantType = oauthGrantTypeBox == null ? "client_credentials" : oauthGrantTypeBox.getValue();
        params.put("grant_type", grantType);
        addFormParam(params, "client_id", oauthClientIdField == null ? "" : oauthClientIdField.getText());
        if (oauthBasicAuthCheck == null || !oauthBasicAuthCheck.isSelected()) {
            addFormParam(params, "client_secret", oauthClientSecretField == null ? "" : oauthClientSecretField.getText());
        }
        addFormParam(params, "scope", oauthScopeField == null ? "" : oauthScopeField.getText());
        if ("password".equals(grantType)) {
            addFormParam(params, "username", oauthUsernameField == null ? "" : oauthUsernameField.getText());
            addFormParam(params, "password", oauthPasswordField == null ? "" : oauthPasswordField.getText());
        } else if ("authorization_code".equals(grantType)) {
            addFormParam(params, "code", oauthAuthCodeField == null ? "" : oauthAuthCodeField.getText());
            addFormParam(params, "redirect_uri", oauthRedirectUriField == null ? "" : oauthRedirectUriField.getText());
        } else if ("refresh_token".equals(grantType)) {
            addFormParam(params, "refresh_token", oauthRefreshTokenField == null ? "" : oauthRefreshTokenField.getText());
        }

        ApiRequest tokenRequest = new ApiRequest();
        tokenRequest.method = "POST";
        tokenRequest.url = tokenUrl;
        tokenRequest.headers = new LinkedHashMap<>();
        tokenRequest.headers.put("Accept", "application/json");
        tokenRequest.headers.put("Content-Type", "application/x-www-form-urlencoded");
        String clientId = resolveVariables(oauthClientIdField == null ? "" : oauthClientIdField.getText());
        String clientSecret = resolveVariables(oauthClientSecretField == null ? "" : oauthClientSecretField.getText());
        if (oauthBasicAuthCheck != null && oauthBasicAuthCheck.isSelected() && !clientId.isBlank()) {
            tokenRequest.headers.put("Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
        }
        tokenRequest.body = formBody(params);
        applyApiTransportSettings(tokenRequest);

        ApiResponse response = apiService.sendRequest(tokenRequest);
        if (response.statusCode < 200 || response.statusCode >= 300) {
            throw new IllegalStateException("OAuth2 token request failed: HTTP "
                    + response.statusCode + " " + shorten(response.rawBody, 500));
        }
        JSONObject json = new JSONObject(response.rawBody);
        saveOAuth2TokenVariables(json);
        return json;
    }

    private void addFormParam(Map<String, String> params, String key, String value) {
        String resolved = resolveVariables(value == null ? "" : value.trim());
        if (!resolved.isBlank()) {
            params.put(key, resolved);
        }
    }

    private String formBody(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> urlEncode(entry.getKey()) + "=" + urlEncode(entry.getValue()))
                .collect(java.util.stream.Collectors.joining("&"));
    }

    private void saveOAuth2TokenVariables(JSONObject json) {
        String accessToken = json.optString("access_token");
        if (!accessToken.isBlank()) {
            savedVariables.put("access_token", accessToken);
            savedVariableTypes.put("access_token", "OAuth2");
            savedVariablePaths.put("access_token", "oauth2:access_token");
        }
        String refreshToken = json.optString("refresh_token");
        if (!refreshToken.isBlank()) {
            savedVariables.put("refresh_token", refreshToken);
            savedVariableTypes.put("refresh_token", "OAuth2");
            savedVariablePaths.put("refresh_token", "oauth2:refresh_token");
        }
        long expiresIn = json.optLong("expires_in", 0);
        if (expiresIn > 0) {
            savedVariables.put("access_token_expiry", String.valueOf(System.currentTimeMillis() + expiresIn * 1000));
            savedVariableTypes.put("access_token_expiry", "OAuth2");
            savedVariablePaths.put("access_token_expiry", "oauth2:expires_in");
        }
        refreshVariablesViewSafely();
    }

    private void putHeaderIfMissing(Map<String, String> headers, String key, String value) {
        if (headers == null || key == null || key.isBlank()) {
            return;
        }
        boolean exists = headers.keySet().stream().anyMatch(existing -> existing.equalsIgnoreCase(key));
        if (!exists) {
            headers.put(key, value == null ? "" : value);
        }
    }

    private void hydratePostmanBearerTokenIfNeeded(ApiRequest request, boolean bearerAuthSelected, String rawBearerToken) throws Exception {
        if (request == null || !bearerAuthSelected) {
            return;
        }
        if (request.token != null && !request.token.isBlank()) {
            return;
        }
        if (rawBearerToken == null || !rawBearerToken.contains("access_token")) {
            return;
        }
        String token = validSavedAccessToken();
        if (token.isBlank()) {
            token = requestPayPalAccessToken();
        }
        request.token = token;
    }

    private List<String> unresolvedRequestVariables(ApiRequest request) {
        Set<String> names = new HashSet<>();
        collectUnresolvedVariables(request.url, names);
        collectUnresolvedVariables(request.body, names);
        collectUnresolvedVariables(request.token, names);
        if (request.headers != null) {
            request.headers.forEach((key, value) -> {
                collectUnresolvedVariables(key, names);
                collectUnresolvedVariables(value, names);
            });
        }
        return names.stream().sorted().toList();
    }

    private void collectUnresolvedVariables(String text, Set<String> names) {
        if (text == null || text.isBlank()) {
            return;
        }
        Matcher matcher = Pattern.compile("\\$\\{([^}]+)}").matcher(text);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
    }

    private String validSavedAccessToken() {
        String token = savedVariables.getOrDefault("access_token", "");
        if (token.isBlank()) {
            return "";
        }
        String expiryText = savedVariables.getOrDefault("access_token_expiry", "");
        if (expiryText.isBlank()) {
            return token;
        }
        try {
            long expiry = Long.parseLong(expiryText.trim());
            return expiry > System.currentTimeMillis() + 30_000 ? token : "";
        } catch (NumberFormatException ignored) {
            return token;
        }
    }

    private String requestPayPalAccessToken() throws Exception {
        String baseUrl = resolveVariables("${base_url}");
        String clientId = resolveVariables("${client_id}");
        String clientSecret = resolveVariables("${client_secret}");
        if (baseUrl.isBlank() || clientId.isBlank() || clientSecret.isBlank()
                || baseUrl.contains("${") || clientId.contains("${") || clientSecret.contains("${")) {
            throw new IllegalStateException("Postman bearer token is empty. Import or set base_url, client_id, and client_secret variables first.");
        }
        String tokenUrl = baseUrl.replaceAll("/+$", "") + "/v1/oauth2/token";
        String basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder(URI.create(tokenUrl))
                .timeout(Duration.ofSeconds(60))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + basic)
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials", StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("PayPal access token request failed: HTTP "
                    + response.statusCode() + " " + shorten(response.body(), 500));
        }
        JSONObject json = new JSONObject(response.body());
        String token = json.optString("access_token");
        if (token.isBlank()) {
            throw new IllegalStateException("PayPal access token response did not contain access_token.");
        }
        saveAccessTokenVariables(json, clientId);
        return token;
    }

    private void captureAccessTokenFromResponse(ApiResponse response) {
        if (response == null || response.rawBody == null || response.rawBody.isBlank()) {
            return;
        }
        try {
            JSONObject json = new JSONObject(response.rawBody);
            if (json.has("access_token")) {
                saveAccessTokenVariables(json, resolveVariables("${client_id}"));
                Platform.runLater(this::refreshVariablesView);
            }
        } catch (Exception ignored) {
            // Non-token responses are ignored.
        }
    }

    private void saveAccessTokenVariables(JSONObject tokenJson, String clientId) {
        String token = tokenJson.optString("access_token");
        if (token.isBlank()) {
            return;
        }
        long expiresInSeconds = Math.max(0, tokenJson.optLong("expires_in", 0));
        long expiry = expiresInSeconds == 0 ? 0 : System.currentTimeMillis() + expiresInSeconds * 1000;
        savedVariables.put("access_token", token);
        savedVariableTypes.put("access_token", "Postman OAuth");
        savedVariablePaths.put("access_token", "postman-oauth:access_token");
        postmanCollectionVariables.put("access_token", token);
        if (expiry > 0) {
            savedVariables.put("access_token_expiry", String.valueOf(expiry));
            savedVariableTypes.put("access_token_expiry", "Postman OAuth");
            savedVariablePaths.put("access_token_expiry", "postman-oauth:expires_in");
            postmanCollectionVariables.put("access_token_expiry", String.valueOf(expiry));
        }
        if (clientId != null && !clientId.isBlank() && !clientId.contains("${")) {
            savedVariables.put("access_token_for", clientId);
            savedVariableTypes.put("access_token_for", "Postman OAuth");
            savedVariablePaths.put("access_token_for", "postman-oauth:client_id");
            postmanCollectionVariables.put("access_token_for", clientId);
        }
    }

    private void capturePostmanTestVariables(PostmanCollectionNode node, ApiResponse response) {
        if (node == null || response == null || response.rawBody == null || response.rawBody.isBlank()) {
            return;
        }
        JSONArray events = node.source.optJSONArray("event");
        if (events == null || events.isEmpty()) {
            return;
        }
        int captured = 0;
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.optJSONObject(i);
            if (event == null || !"test".equalsIgnoreCase(event.optString("listen"))) {
                continue;
            }
            String script = postmanScriptText(event.optJSONObject("script"));
            captured += executePostmanJavaScript(node, event.optString("listen"), script, response, null);
        }
        if (captured > 0) {
            refreshVariablesViewSafely();
        }
    }

    private void executeBasicPostmanPreRequestVariables(PostmanCollectionNode node) {
        if (node == null) {
            return;
        }
        JSONArray events = node.source.optJSONArray("event");
        if (events == null || events.isEmpty()) {
            return;
        }
        int updated = 0;
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.optJSONObject(i);
            if (event == null || !"prerequest".equalsIgnoreCase(event.optString("listen"))) {
                continue;
            }
            String script = postmanScriptText(event.optJSONObject("script"));
            updated += executePostmanJavaScript(node, event.optString("listen"), script, null, null);
        }
        if (updated > 0) {
            refreshVariablesViewSafely();
        }
    }

    private int executePostmanJavaScript(PostmanCollectionNode node, String eventType, String script,
                                         ApiResponse response, ApiRequest request) {
        if (script == null || script.isBlank()) {
            return 0;
        }
        AtomicInteger updates = new AtomicInteger();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicReference<Context> activeContext = new AtomicReference<>();
        Future<?> future = executor.submit(() -> {
            try (Context context = Context.newBuilder("js")
                    .allowAllAccess(false)
                    .allowHostAccess(HostAccess.NONE)
                    .allowHostClassLookup(className -> false)
                    .allowCreateThread(false)
                    .allowCreateProcess(false)
                    .allowIO(false)
                    .option("js.ecmascript-version", "2022")
                    .option("engine.WarnInterpreterOnly", "false")
                    .build()) {
                activeContext.set(context);
                context.getBindings("js").putMember("__responseText", response == null ? "" : nullToBlank(response.rawBody));
                context.getBindings("js").putMember("pm", postmanPmApi(context, node, eventType, response, request, updates));
                context.eval("js", script);
            } catch (PolyglotException e) {
                throw new IllegalStateException("Postman " + eventType + " script failed for " + node.name + ": "
                        + firstNonBlank(e.getMessage(), e.toString()), e);
            }
        });
        try {
            future.get(3000, TimeUnit.MILLISECONDS);
            return updates.get();
        } catch (TimeoutException e) {
            Context context = activeContext.get();
            if (context != null) {
                context.close(true);
            }
            future.cancel(true);
            throw new IllegalStateException("Postman " + eventType + " script timed out after 3 seconds for " + node.name, e);
        } catch (Exception e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Postman " + eventType + " script failed for " + node.name, cause);
        } finally {
            executor.shutdownNow();
        }
    }

    private ProxyObject postmanPmApi(Context context, PostmanCollectionNode node, String eventType,
                                     ApiResponse response, ApiRequest request, AtomicInteger updates) {
        Map<String, Object> api = new LinkedHashMap<>();
        api.put("variables", postmanVariableScope(node, "variables", updates));
        api.put("collectionVariables", postmanVariableScope(node, "collectionVariables", updates));
        api.put("environment", postmanVariableScope(node, "environment", updates));
        api.put("response", postmanResponseApi(context, response));
        api.put("request", postmanRequestApi(request));
        api.put("info", ProxyObject.fromMap(Map.of(
                "eventName", eventType == null ? "" : eventType,
                "requestName", node == null ? "" : node.name
        )));
        api.put("test", (ProxyExecutable) args -> {
            if (args.length > 1 && args[1].canExecute()) {
                args[1].execute();
            }
            return true;
        });
        api.put("expect", (ProxyExecutable) args -> postmanExpectation(args.length == 0 ? null : args[0]));
        return ProxyObject.fromMap(api);
    }

    private ProxyObject postmanVariableScope(PostmanCollectionNode node, String scope, AtomicInteger updates) {
        Map<String, Object> api = new LinkedHashMap<>();
        api.put("get", (ProxyExecutable) args -> postmanVariableValue(args.length == 0 ? "" : valueToString(args[0])));
        api.put("has", (ProxyExecutable) args -> postmanVariableValue(args.length == 0 ? "" : valueToString(args[0])) != null);
        api.put("set", (ProxyExecutable) args -> {
            if (args.length >= 2) {
                savePostmanScriptVariable(node, scope, valueToString(args[0]), valueToString(args[1]));
                updates.incrementAndGet();
            }
            return null;
        });
        api.put("unset", (ProxyExecutable) args -> {
            if (args.length >= 1) {
                unsetPostmanScriptVariable(scope, valueToString(args[0]));
                updates.incrementAndGet();
            }
            return null;
        });
        api.put("replaceIn", (ProxyExecutable) args -> resolveVariables(postmanVariableToTestWeave(args.length == 0 ? "" : valueToString(args[0]))));
        return ProxyObject.fromMap(api);
    }

    private ProxyObject postmanResponseApi(Context context, ApiResponse response) {
        String body = response == null ? "" : nullToBlank(response.rawBody);
        Map<String, Object> api = new LinkedHashMap<>();
        api.put("code", response == null ? 0 : response.statusCode);
        api.put("status", response == null ? "" : nullToBlank(response.statusLine));
        api.put("responseTime", response == null ? 0L : response.timeMs);
        api.put("text", (ProxyExecutable) args -> body);
        api.put("json", (ProxyExecutable) args -> {
            if (body.isBlank()) {
                return null;
            }
            return context.eval("js", "JSON.parse(__responseText)");
        });
        api.put("headers", postmanHeadersApi(response == null ? "" : response.headersText));
        return ProxyObject.fromMap(api);
    }

    private ProxyObject postmanHeadersApi(String headersText) {
        Map<String, String> headers = parseHeaders(headersText);
        Map<String, Object> api = new LinkedHashMap<>();
        api.put("get", (ProxyExecutable) args -> {
            String key = args.length == 0 ? "" : valueToString(args[0]);
            return headers.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(key))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
        });
        api.put("has", (ProxyExecutable) args -> {
            String key = args.length == 0 ? "" : valueToString(args[0]);
            return headers.keySet().stream().anyMatch(existing -> existing.equalsIgnoreCase(key));
        });
        return ProxyObject.fromMap(api);
    }

    private ProxyObject postmanRequestApi(ApiRequest request) {
        Map<String, Object> api = new LinkedHashMap<>();
        api.put("url", request == null ? "" : nullToBlank(request.url));
        api.put("method", request == null ? "" : nullToBlank(request.method));
        api.put("headers", ProxyObject.fromMap(request == null || request.headers == null ? Map.of() : new LinkedHashMap<>(request.headers)));
        api.put("body", request == null ? "" : nullToBlank(request.body));
        return ProxyObject.fromMap(api);
    }

    private ProxyObject postmanExpectation(Value actual) {
        Map<String, Object> api = new LinkedHashMap<>();
        ProxyExecutable equal = args -> {
            Object expected = args.length == 0 ? null : valueToObject(args[0]);
            Object actualObject = valueToObject(actual);
            if (!Objects.equals(String.valueOf(expected), String.valueOf(actualObject))) {
                throw new IllegalStateException("Expected " + actualObject + " to equal " + expected);
            }
            return true;
        };
        api.put("to", null);
        api.put("be", null);
        api.put("have", null);
        api.put("that", null);
        api.put("is", null);
        api.put("and", null);
        api.put("which", null);
        api.put("with", null);
        api.put("equal", equal);
        api.put("equals", equal);
        api.put("eql", equal);
        api.put("property", (ProxyExecutable) args -> assertPropertyExpectation(actual, args));
        api.put("include", (ProxyExecutable) args -> {
            String expected = args.length == 0 ? "" : valueToString(args[0]);
            String actualText = valueToString(actual);
            if (!actualText.contains(expected)) {
                throw new IllegalStateException("Expected " + actualText + " to include " + expected);
            }
            return true;
        });
        api.put("above", (ProxyExecutable) args -> assertNumericExpectation(actual, args, "above"));
        api.put("below", (ProxyExecutable) args -> assertNumericExpectation(actual, args, "below"));
        ProxyObject proxy = ProxyObject.fromMap(api);
        api.put("to", proxy);
        api.put("be", proxy);
        api.put("have", proxy);
        api.put("that", proxy);
        api.put("is", proxy);
        api.put("and", proxy);
        api.put("which", proxy);
        api.put("with", proxy);
        return proxy;
    }

    private ProxyObject assertPropertyExpectation(Value actual, Value[] args) {
        if (args.length == 0) {
            throw new IllegalStateException("Expected property name.");
        }
        String propertyName = valueToString(args[0]);
        if (!hasPostmanProperty(actual, propertyName)) {
            throw new IllegalStateException("Expected " + valueToString(actual) + " to have property " + propertyName);
        }
        Value propertyValue = getPostmanProperty(actual, propertyName);
        if (args.length > 1) {
            Object expected = valueToObject(args[1]);
            Object actualObject = valueToObject(propertyValue);
            if (!Objects.equals(String.valueOf(expected), String.valueOf(actualObject))) {
                throw new IllegalStateException("Expected property " + propertyName + " to equal " + expected
                        + " but found " + actualObject);
            }
        }
        return postmanExpectation(propertyValue);
    }

    private boolean hasPostmanProperty(Value actual, String propertyName) {
        if (actual == null || actual.isNull() || propertyName == null) {
            return false;
        }
        if (actual.hasMembers() && actual.hasMember(propertyName)) {
            return true;
        }
        if (actual.hasArrayElements()) {
            try {
                long index = Long.parseLong(propertyName);
                return index >= 0 && index < actual.getArraySize();
            } catch (NumberFormatException ignored) {
                return "length".equals(propertyName);
            }
        }
        return false;
    }

    private Value getPostmanProperty(Value actual, String propertyName) {
        if (actual.hasMembers() && actual.hasMember(propertyName)) {
            return actual.getMember(propertyName);
        }
        if (actual.hasArrayElements()) {
            if ("length".equals(propertyName)) {
                return Context.getCurrent().asValue(actual.getArraySize());
            }
            return actual.getArrayElement(Long.parseLong(propertyName));
        }
        return Context.getCurrent().asValue(null);
    }

    private boolean assertNumericExpectation(Value actual, Value[] args, String operator) {
        double left = valueToDouble(actual);
        double right = args.length == 0 ? 0 : valueToDouble(args[0]);
        boolean ok = "above".equals(operator) ? left > right : left < right;
        if (!ok) {
            throw new IllegalStateException("Expected " + left + " to be " + operator + " " + right);
        }
        return true;
    }

    private void savePostmanScriptVariable(PostmanCollectionNode node, String scope, String rawName, String rawValue) {
        String variableName = normalizeVariableName(rawName);
        if (variableName.isBlank()) {
            return;
        }
        String value = resolveVariables(postmanVariableToTestWeave(rawValue));
        savedVariables.put(variableName, value);
        savedVariableTypes.put(variableName, "environment".equals(scope) ? "Postman Environment" : "Postman Script");
        savedVariablePaths.put(variableName, "postman-script:" + (node == null ? "" : node.name));
        if ("environment".equals(scope)) {
            postmanEnvironmentVariables.put(variableName, value);
        } else {
            postmanCollectionVariables.put(variableName, value);
        }
    }

    private void unsetPostmanScriptVariable(String scope, String rawName) {
        String variableName = normalizeVariableName(rawName);
        savedVariables.remove(variableName);
        savedVariableTypes.remove(variableName);
        savedVariablePaths.remove(variableName);
        if ("environment".equals(scope)) {
            postmanEnvironmentVariables.remove(variableName);
        } else {
            postmanCollectionVariables.remove(variableName);
        }
    }

    private String valueToString(Value value) {
        Object object = valueToObject(value);
        return object == null ? "" : String.valueOf(object);
    }

    private Object valueToObject(Value value) {
        if (value == null || value.isNull()) {
            return null;
        }
        if (value.isString()) {
            return value.asString();
        }
        if (value.isBoolean()) {
            return value.asBoolean();
        }
        if (value.fitsInLong()) {
            return value.asLong();
        }
        if (value.fitsInDouble()) {
            return value.asDouble();
        }
        return value.toString();
    }

    private double valueToDouble(Value value) {
        Object object = valueToObject(value);
        if (object instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(object));
    }

    private void refreshVariablesViewSafely() {
        if (Platform.isFxApplicationThread()) {
            refreshVariablesView();
        } else {
            Platform.runLater(this::refreshVariablesView);
        }
    }

    private String postmanScriptText(JSONObject script) {
        if (script == null) {
            return "";
        }
        Object exec = script.opt("exec");
        if (exec instanceof JSONArray lines) {
            List<String> text = new ArrayList<>();
            for (int i = 0; i < lines.length(); i++) {
                text.add(lines.optString(i));
            }
            return String.join("\n", text);
        }
        return exec == null || exec == JSONObject.NULL ? "" : String.valueOf(exec);
    }

    private Object extractPostmanJsonDataPath(Object root, String path) {
        Object current = root;
        Matcher matcher = Pattern.compile("\\.([A-Za-z0-9_]+)|\\[(\\d+)]").matcher(path == null ? "" : path);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                if (!(current instanceof JSONObject object)) {
                    return null;
                }
                current = object.opt(matcher.group(1));
            } else {
                if (!(current instanceof JSONArray array)) {
                    return null;
                }
                current = array.opt(Integer.parseInt(matcher.group(2)));
            }
            if (current == null || current == JSONObject.NULL) {
                return current;
            }
        }
        return current;
    }

    private void toggleTokenVisibility(Button toggleButton) {
        boolean show = !visibleTokenField.isVisible();
        visibleTokenField.setVisible(show);
        visibleTokenField.setManaged(show);
        tokenField.setVisible(!show);
        tokenField.setManaged(!show);
        toggleButton.setText(show ? "Hide" : "Show");
    }

    private void copySelectedResponse() {
        String text = selectedResponseText();
        if (text == null || text.isBlank()) {
            showWarning("Copy Response", "No response content is available to copy.");
            return;
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
        apiStatusLabel.setText("Response copied");
    }

    private String selectedResponseText() {
        if (apiResponseTabs == null || apiResponseTabs.getSelectionModel().getSelectedItem() == null) {
            return lastResponse == null ? "" : lastResponse.prettyBody;
        }
        String selected = apiResponseTabs.getSelectionModel().getSelectedItem().getText();
        return switch (selected) {
            case "Raw" -> rawResponseArea.getText();
            case "Headers" -> responseHeadersArea.getText();
            case "Cookies" -> responseCookiesArea.getText();
            default -> prettyResponseArea.getText();
        };
    }

    private void renderResponse(ApiResponse response) {
        if (response == null) {
            return;
        }
        if (statusValueLabel == null || prettyResponseArea == null || rawResponseArea == null
                || responseHeadersArea == null || responseCookiesArea == null) {
            lastActualJson = response.rawBody;
            return;
        }
        statusValueLabel.setText(response.statusLine);
        timeValueLabel.setText(response.timeMs + " ms");
        sizeValueLabel.setText(response.sizeBytes + " bytes");
        prettyResponseArea.setText(response.prettyBody);
        rawResponseArea.setText(response.rawBody);
        responseHeadersArea.setText(response.headersText);
        responseCookiesArea.setText(response.cookiesText);
        lastActualJson = response.rawBody;
        if (responseFieldRows != null || fieldValidationRows != null) {
            parseResponseFields(response.rawBody);
        }
    }

    private void maybeShowApiAiSuggestions(ApiRequest request, ApiResponse response) {
        if (activeApiAiHermesSession == null || response == null || response.rawBody == null || response.rawBody.isBlank()) {
            updateApiAiConnectionLabels();
            return;
        }
        apiStatusLabel.setText("Hermes Agent analyzing response...");
        updateApiAiConnectionLabels("Hermes Agent analyzing with " + apiAiConnectedModel);
        Task<ApiAiSuggestion> task = new Task<>() {
            @Override protected ApiAiSuggestion call() {
                return requestApiAiModelSuggestion(request, response);
            }
        };
        task.setOnSucceeded(e -> {
            ApiAiSuggestion suggestion = task.getValue();
            apiStatusLabel.setText("Hermes Agent suggestions ready");
            updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel);
            if (!suggestion.variables.isEmpty() || !suggestion.validations.isEmpty()) {
                showApiAiSuggestionWindow(suggestion);
            } else {
                showWarning("API AI Agent", "The connected model did not return variable or validation suggestions.");
            }
        });
        task.setOnFailed(e -> {
            logApiAiConsole("AI Analysis task failed", task.getException());
            apiStatusLabel.setText("API AI Agent suggestion failed");
            if (activeApiAiHermesSession != null) {
                updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel
                        + " (last analysis failed: " + exceptionMessage(rootCause(task.getException())) + ")");
            } else {
                updateApiAiConnectionLabels("API AI Agent disconnected: " + exceptionMessage(task.getException()));
            }
            showError("API AI Agent Suggestion Failed", task.getException());
        });
        start(task);
    }

    private ApiAiSuggestion requestApiAiModelSuggestion(ApiRequest request, ApiResponse response) {
        try {
            logApiAiConsole("AI Analysis started for " + nullToBlank(request.method) + " " + nullToBlank(request.url)
                    + ", status=" + nullToBlank(response.statusLine)
                    + ", responseBytes=" + nullToBlank(response.rawBody).getBytes(StandardCharsets.UTF_8).length);
            String prompt = apiAiSuggestionPrompt(request, response);
            logApiAiConsole("AI Analysis prompt built, characters=" + prompt.length());
            String output = runHermesApiAiPrompt(prompt);
            logApiAiConsole("AI Analysis model output received, characters=" + (output == null ? 0 : output.length()));
            JSONObject root = extractJsonObject(output);
            logApiAiConsole("AI Analysis model output parsed as JSON");
            return apiAiSuggestionFromModelJson(request, response, root);
        } catch (Exception e) {
            logApiAiConsole("AI Analysis failed while preparing, running, or parsing model suggestion", e);
            throw new RuntimeException(e);
        }
    }

    private ApiAiSuggestion buildApiAiSuggestion(ApiRequest request, ApiResponse response) {
        List<ResponseFieldCandidate> fields = responseVariableService.parseAllFields(response.rawBody);
        String actionName = inferApiActionName(request);
        String tableName = inferDbTableName(request);
        ApiAiSuggestion suggestion = new ApiAiSuggestion(request, response, actionName);
        Set<String> importantNames = Set.of("orderid", "status", "amount", "token", "userid", "createdat");
        for (ResponseFieldCandidate field : fields) {
            String fieldName = nullToBlank(field.fieldName);
            String compact = fieldName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            boolean scalar = !List.of("object", "array", "null").contains(field.type);
            if (!scalar || (!importantNames.contains(compact) && !field.jsonPath.matches(".*\\.(id|token|status|amount|createdAt|userId)$"))) {
                continue;
            }
            String alias = actionName + "." + fieldName;
            String variableName = normalizeVariableName(alias);
            suggestion.variables.add(row("selected", "true", "variable", "${" + variableName + "}",
                    "alias", "${" + alias + "}", "jsonPath", field.jsonPath, "value", field.value, "type", field.type));
            String validation = suggestedValidation(field);
            suggestion.validations.add(row("selected", "true", "field", field.jsonPath,
                    "validation", validation, "expected", suggestedExpected(field, validation),
                    "type", field.type, "preview", field.previewValue));
            suggestion.dbMappings.add(row("selected", "true", "apiField", fieldName, "jsonPath", field.jsonPath,
                    "dbMapping", tableName + "." + toSnakeCase(fieldName)));
        }
        return suggestion;
    }

    private String apiAiSuggestionPrompt(ApiRequest request, ApiResponse response) {
        List<ResponseFieldCandidate> fields = responseVariableService.parseAllFields(response.rawBody);
        JSONArray candidates = new JSONArray();
        Set<String> seenCandidateKeys = new HashSet<>();
        for (ResponseFieldCandidate field : fields) {
            if (List.of("object", "array", "null").contains(field.type)) continue;
            String candidateKey = field.fieldName + "\u0000" + field.type;
            if (!seenCandidateKeys.add(candidateKey) && candidates.length() >= 20) continue;
            candidates.put(new JSONObject()
                    .put("jsonPath", field.jsonPath)
                    .put("fieldName", field.fieldName)
                    .put("value", field.value)
                    .put("type", field.type));
            if (candidates.length() >= 80) break;
        }
        JSONObject input = new JSONObject()
                .put("endpoint", request.url)
                .put("method", request.method)
                .put("status", response.statusLine)
                .put("responseSample", apiAiResponseSample(response.rawBody))
                .put("candidateFields", candidates);
        return """
                You are TestWeave API Quality Brain.
                Analyze this API response and return ONLY strict JSON. No markdown, no prose.

                Required JSON shape:
                {
                  "variables": [
                    {"name":"createOrder_orderId","alias":"createOrder.orderId","jsonPath":"$.orderId","value":"123","type":"string"}
                  ],
                  "validations": [
                    {"jsonPath":"$.orderId","validation":"notNull","expected":"","type":"string","preview":"123"}
                  ],
                  "dbMappings": [
                    {"apiField":"orderId","jsonPath":"$.orderId","dbMapping":"orders.order_id"}
                  ]
                }

                Prefer important reusable fields such as orderId, status, amount, token, userId, createdAt.
                Suggest validations like notNull, equals, type number, type string, type boolean.
                Suggest likely DB mappings using snake_case column names.

                API input:
                """ + input.toString(2);
    }

    private Object apiAiResponseSample(String rawBody) {
        try {
            Object parsed = new JSONTokener(rawBody).nextValue();
            if (parsed instanceof JSONArray array) {
                JSONArray sample = new JSONArray();
                for (int i = 0; i < Math.min(5, array.length()); i++) {
                    sample.put(array.opt(i));
                }
                return sample;
            }
            return parsed;
        } catch (Exception e) {
            logApiAiConsole("AI Analysis response sample could not be parsed as JSON. Raw response preview: "
                    + shorten(rawBody, 600), e);
            throw e;
        }
    }

    private String runHermesApiAiPrompt(String prompt) throws Exception {
        HermesSessionRecord session = activeApiAiHermesSession;
        if (session == null || nullToBlank(session.sessionId()).isBlank()) {
            throw new IllegalStateException("No active Hermes session is connected. Click Hermes Connect in Config first.");
        }
        String containerName = firstNonBlank(session.containerName(), hermesContainerName());
        requireDocker(ApiValidatorFxApp.this::appendHermesLog);
        if (!dockerContainerRunning(containerName)) {
            ensureHermesContainerRunning(hermesDockerImage(), containerName, true);
        }
        String instruction = "Analyze the API payload from stdin and return ONLY the strict JSON object requested. No markdown or prose.";
        String query = instruction + System.lineSeparator() + System.lineSeparator() + prompt;
        Path promptPath = hermesDataDirectory().resolve("Sessions")
                .resolve("api-ai-analysis-prompt-" + System.currentTimeMillis() + ".txt")
                .toAbsolutePath().normalize();
        Files.createDirectories(promptPath.getParent());
        Files.writeString(promptPath, query, StandardCharsets.UTF_8);
        Path scriptPath = promptPath.getParent().resolve("api-ai-analysis-run-" + System.currentTimeMillis() + ".sh")
                .toAbsolutePath().normalize();
        String containerPromptPath = "/opt/data/" + hermesDataDirectory().relativize(promptPath)
                .toString().replace('\\', '/');
        String containerScriptPath = "/opt/data/" + hermesDataDirectory().relativize(scriptPath)
                .toString().replace('\\', '/');
        Files.writeString(scriptPath, "#!/bin/sh\n"
                + "set -eu\n"
                + "prompt=$(cat " + shellSingleQuote(containerPromptPath) + ")\n"
                + "exec hermes chat -q \"$prompt\" -Q --resume " + shellSingleQuote(session.sessionId()) + " --source tool\n",
                StandardCharsets.UTF_8);
        List<String> command = List.of("docker", "exec",
                "-w", "/opt/data",
                "-e", "AI_AGENT_PATH=/opt/data",
                "-e", "TESTWEAVE_AI_AGENT_PATH=/opt/data",
                containerName, "sh", containerScriptPath);
        logApiAiConsole("AI Analysis Hermes prompt file: " + promptPath);
        logApiAiConsole("AI Analysis Hermes script file: " + scriptPath);
        logApiAiConsole("AI Analysis Hermes command: docker exec ... sh " + containerScriptPath);
        Process process = new ProcessBuilder(command).redirectErrorStream(false).start();
        ExecutorService stderrExecutor = Executors.newSingleThreadExecutor();
        ExecutorService stdoutExecutor = Executors.newSingleThreadExecutor();
        Future<String> stderr = stderrExecutor.submit(() -> new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
        Future<String> stdoutFuture = stdoutExecutor.submit(() -> new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        boolean completed = process.waitFor(120, java.util.concurrent.TimeUnit.SECONDS);
        if (!completed) {
            process.destroyForcibly();
            stderrExecutor.shutdownNow();
            stdoutExecutor.shutdownNow();
            logApiAiConsole("AI Analysis Hermes process timed out after 120 seconds");
            throw new IllegalStateException("Hermes model analysis timed out after 120 seconds. Confirm Hermes is running and try again.");
        }
        int exit = process.exitValue();
        String errors = stderr.get();
        String stdout = stdoutFuture.get();
        stderrExecutor.shutdownNow();
        stdoutExecutor.shutdownNow();
        logApiAiConsole("AI Analysis Hermes exit code: " + exit);
        if (!errors.isBlank()) {
            logApiAiConsole("AI Analysis Hermes stderr:\n" + errors.trim());
        }
        if (!stdout.isBlank()) {
            logApiAiConsole("AI Analysis Hermes stdout:\n" + stdout.trim());
        }
        if (exit != 0) {
            String details = firstNonBlank(errors, stdout);
            if (isHermesAuthFailure(details)) {
                openHermesOpenAiCodexReauth(containerName);
            }
            throw new IllegalStateException("Hermes model analysis failed with exit code " + exit + ". "
                    + shorten(details, 1200));
        }
        apiAiConnectedModel = "Hermes Agent (" + session.sessionId() + ")";
        return stdout;
    }

    private String shellSingleQuote(String value) {
        return "'" + nullToBlank(value).replace("'", "'\"'\"'") + "'";
    }

    private JSONObject extractJsonObject(String text) {
        String value = text == null ? "" : text.trim();
        if (value.startsWith("```")) {
            value = value.replaceFirst("^```(?:json)?\\s*", "").replaceFirst("\\s*```$", "").trim();
        }
        int start = value.indexOf('{');
        int end = value.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("Model response did not contain a JSON object: " + shorten(value, 600));
        }
        return new JSONObject(value.substring(start, end + 1));
    }

    private ApiAiSuggestion apiAiSuggestionFromModelJson(ApiRequest request, ApiResponse response, JSONObject root) {
        ApiAiSuggestion suggestion = new ApiAiSuggestion(request, response, inferApiActionName(request));
        JSONArray variables = root.optJSONArray("variables");
        if (variables != null) for (int i = 0; i < variables.length(); i++) {
            JSONObject item = variables.optJSONObject(i);
            if (item == null) continue;
            String name = normalizeVariableName(item.optString("name", item.optString("alias", "apiField")));
            suggestion.variables.add(row("selected", "true", "variable", "${" + name + "}",
                    "alias", item.optString("alias"), "jsonPath", item.optString("jsonPath"),
                    "value", item.optString("value"), "type", item.optString("type")));
        }
        JSONArray validations = root.optJSONArray("validations");
        if (validations != null) for (int i = 0; i < validations.length(); i++) {
            JSONObject item = validations.optJSONObject(i);
            if (item == null) continue;
            suggestion.validations.add(row("selected", "true", "field", item.optString("jsonPath"),
                    "validation", item.optString("validation"), "expected", item.optString("expected"),
                    "type", item.optString("type"), "preview", item.optString("preview")));
        }
        JSONArray mappings = root.optJSONArray("dbMappings");
        if (mappings != null) for (int i = 0; i < mappings.length(); i++) {
            JSONObject item = mappings.optJSONObject(i);
            if (item == null) continue;
            suggestion.dbMappings.add(row("selected", "true", "apiField", item.optString("apiField"), "jsonPath", item.optString("jsonPath"),
                    "dbMapping", item.optString("dbMapping",
                            item.optString("table") + "." + item.optString("column"))));
        }
        return suggestion;
    }

    private String suggestedValidation(ResponseFieldCandidate field) {
        if ("status".equalsIgnoreCase(field.fieldName) && !field.value.isBlank()) {
            return "equals";
        }
        if (!field.type.isBlank() && !"string".equals(field.type)) {
            return "type " + field.type;
        }
        return "notNull";
    }

    private String suggestedExpected(ResponseFieldCandidate field, String validation) {
        if ("equals".equals(validation)) {
            return field.value;
        }
        if (validation.startsWith("type ")) {
            return validation.substring("type ".length());
        }
        return "";
    }

    private void showApiAiSuggestionWindow(ApiAiSuggestion suggestion) {
        ObservableList<Map<String, String>> variableRows = FXCollections.observableArrayList(suggestion.variables);
        ObservableList<Map<String, String>> validationRows = FXCollections.observableArrayList(suggestion.validations);
        ObservableList<Map<String, String>> dbRows = FXCollections.observableArrayList(suggestion.dbMappings);
        TableView<Map<String, String>> variablesTable = mapTable(variableRows, "Save", "selected",
                "Variable", "variable", "Alias", "alias", "JSON Path", "jsonPath", "Value", "value", "Type", "type");
        TableView<Map<String, String>> validationsTable = mapTable(validationRows, "Add", "selected",
                "JSON Path", "field", "Validation", "validation", "Expected", "expected", "Type", "type", "Preview", "preview");
        TableView<Map<String, String>> mappingsTable = mapTable(dbRows, "Add", "selected",
                "API Field", "apiField", "JSON Path", "jsonPath", "Possible DB Mapping", "dbMapping");
        variablesTable.setPrefHeight(210);
        variablesTable.setMinHeight(140);
        validationsTable.setPrefHeight(240);
        validationsTable.setMinHeight(150);
        mappingsTable.setPrefHeight(210);
        mappingsTable.setMinHeight(130);
        Button approve = primary("Approve & Save Memory");
        Button importSuggestion = primary("Import");
        Button toggleAll = secondary("Check All");
        Button close = secondary("Close");
        Stage suggestionStage = new Stage();
        if (stage != null) {
            suggestionStage.initOwner(stage);
        }
        final boolean[] allChecked = {true};
        toggleAll.setOnAction(e -> {
            boolean selected = !"Uncheck All".equals(toggleAll.getText());
            setAllRowsSelected(variableRows, variablesTable, selected);
            setAllRowsSelected(validationRows, validationsTable, selected);
            setAllRowsSelected(dbRows, mappingsTable, selected);
            allChecked[0] = selected;
            toggleAll.setText(selected ? "Uncheck All" : "Check All");
        });
        importSuggestion.setOnAction(e -> importApiAiSuggestionToTabs(variableRows, validationRows));
        approve.setOnAction(e -> {
            saveApprovedApiAiSuggestionMemory(suggestion, variableRows, validationRows, dbRows);
            suggestionStage.close();
        });
        close.setOnAction(e -> suggestionStage.close());
        VBox content = new VBox(14,
                sectionTitle("AI Suggestion"),
                new Label("API AI Agent converted this response into reusable variables, validations, and DB mapping hints."),
                card("Variables", variablesTable),
                card("Validation", validationsTable),
                card("DB Validation Opportunities", mappingsTable));
        content.setPadding(new Insets(16));
        VBox.setVgrow(variablesTable, Priority.ALWAYS);
        VBox.setVgrow(validationsTable, Priority.ALWAYS);
        VBox.setVgrow(mappingsTable, Priority.ALWAYS);
        ScrollPane scroller = new ScrollPane(content);
        scroller.setFitToWidth(true);
        scroller.setPannable(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        FlowPane footer = actionRow(toggleAll, importSuggestion, approve, close);
        footer.setPadding(new Insets(12, 16, 16, 16));
        footer.getStyleClass().add("card");
        BorderPane shell = new BorderPane();
        shell.setCenter(scroller);
        shell.setBottom(footer);
        double[] suggestionSize = apiAiSuggestionWindowSize();
        Scene scene = new Scene(shell, suggestionSize[0], suggestionSize[1]);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        suggestionStage.setTitle("AI Suggestion - API AI Agent");
        suggestionStage.setScene(scene);
        suggestionStage.setMinWidth(Math.min(760, suggestionSize[0]));
        suggestionStage.setMinHeight(Math.min(520, suggestionSize[1]));
        suggestionStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        suggestionStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        if (stage != null) {
            suggestionStage.setX(stage.getX() + Math.max(0, (stage.getWidth() - suggestionSize[0]) / 2));
            suggestionStage.setY(stage.getY() + Math.max(0, (stage.getHeight() - suggestionSize[1]) / 2));
        }
        suggestionStage.show();
    }

    private double[] apiAiSuggestionWindowSize() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double maxWidth = visualBounds.getWidth() - 32;
        double maxHeight = visualBounds.getHeight() - 48;
        double parentWidth = stage == null ? maxWidth : Math.max(760, stage.getWidth() - 48);
        double parentHeight = stage == null ? maxHeight : Math.max(560, stage.getHeight() - 48);
        double width = Math.min(maxWidth, Math.max(760, parentWidth));
        double height = Math.min(maxHeight, Math.max(560, parentHeight));
        return new double[]{width, height};
    }

    private void importApiAiSuggestionToTabs(List<Map<String, String>> variables, List<Map<String, String>> validations) {
        savedVariables.clear();
        savedVariablePaths.clear();
        savedVariableTypes.clear();
        if (responseFieldRows != null) {
            responseFieldRows.clear();
        }
        int variableCount = 0;
        for (Map<String, String> variable : variables) {
            if (!isSelected(variable)) continue;
            String name = variable.getOrDefault("variable", "").replace("${", "").replace("}", "");
            if (name.isBlank()) {
                name = normalizeVariableName(variable.getOrDefault("alias", variable.getOrDefault("jsonPath", "apiField")));
            }
            String jsonPath = variable.getOrDefault("jsonPath", "");
            String value = variable.getOrDefault("value", "");
            String type = firstNonBlank(variable.get("type"), "API AI Agent");
            if (responseFieldRows != null) {
                responseFieldRows.add(row("selected", "true", "jsonPath", jsonPath,
                        "preview", value, "variableName", name, "type", type, "value", value));
            }
            savedVariables.put(name, variable.getOrDefault("value", ""));
            savedVariablePaths.put(name, jsonPath);
            savedVariableTypes.put(name, type);
            variableCount++;
        }
        if (responseFieldsTable != null) {
            responseFieldsTable.refresh();
        }
        if (fieldValidationRows != null) {
            fieldValidationRows.clear();
            for (Map<String, String> validation : validations) {
                if (!isSelected(validation)) continue;
                String validationText = validation.getOrDefault("validation", "");
                String typeValidation = validationText.startsWith("type ")
                        ? validationText.substring("type ".length())
                        : validation.getOrDefault("type", "Skip");
                String nullValidation = "notNull".equalsIgnoreCase(validationText) ? "Not Null" : "Skip";
                fieldValidationRows.add(row("selected", "true", "field", validation.getOrDefault("field", ""),
                        "preview", validation.getOrDefault("preview", ""), "nullValidation", nullValidation,
                        "typeValidation", typeValidation, "operator", "equals",
                        "expected", validation.getOrDefault("expected", ""), "actual", "",
                        "actualType", validation.getOrDefault("type", ""), "result", "Imported", "message", "Imported from API AI Agent"));
            }
        }
        if (fieldValidationsTable != null) {
            fieldValidationsTable.refresh();
        }
        refreshVariablesView();
        showInfo("API AI Agent", "Imported " + variableCount + " variable(s) and "
                + (fieldValidationRows == null ? 0 : fieldValidationRows.size()) + " validation row(s).");
    }

    private void saveApprovedApiAiSuggestionMemory(ApiAiSuggestion suggestion, List<Map<String, String>> variables,
                                                   List<Map<String, String>> validations, List<Map<String, String>> dbMappings) {
        JSONArray selectedVariables = new JSONArray();
        JSONArray selectedValidations = new JSONArray();
        JSONArray mappings = new JSONArray();
        for (Map<String, String> variable : variables) {
            if (!isSelected(variable)) continue;
            selectedVariables.put(new JSONObject(variable));
        }
        for (Map<String, String> validation : validations) {
            if (!isSelected(validation)) continue;
            selectedValidations.put(new JSONObject(validation));
        }
        for (Map<String, String> mapping : dbMappings) {
            if (isSelected(mapping)) {
                mappings.put(new JSONObject(mapping));
            }
        }
        JSONObject memory = new JSONObject()
                .put("id", "api-ai-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                .put("endpoint", suggestion.request.url)
                .put("method", suggestion.request.method)
                .put("actionName", suggestion.actionName)
                .put("hermesSessionId", activeApiAiHermesSession == null ? "" : activeApiAiHermesSession.sessionId())
                .put("provider", activeApiAiHermesSession == null ? "Codex" : "Hermes")
                .put("response", new JSONObject().put("status", suggestion.response.statusLine)
                        .put("body", suggestion.response.rawBody))
                .put("variables", selectedVariables)
                .put("validations", selectedValidations)
                .put("dbMappings", mappings)
                .put("createdAt", Instant.now().toString());
        saveApiAiMemory(memory);
    }

    private void saveApiAiMemory(JSONObject memory) {
        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                if (apiAiAgentMemoryStorageMode() == StorageMode.LOCAL) {
                    saveApiAiMemoryToSqlite(memory);
                } else {
                    saveApiAiMemoryToFirebase(memory);
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> showInfo("API AI Agent", "Saved API AI memory for " + memory.optString("endpoint")));
        task.setOnFailed(e -> showError("Save API AI Memory Failed", task.getException()));
        start(task);
    }

    private void saveApiAiMemoryToSqlite(JSONObject memory) throws Exception {
        Path sqliteDbPath = configCacheDatabasePathFromField();
        initializeApiAiMemoryTable(sqliteDbPath);
        String sql = "INSERT INTO " + API_AI_MEMORY_TABLE
                + " (id, endpoint, method, response_json, variables_json, validations_json, db_mappings_json, created_at, action_name, provider, hermes_session_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(id) DO UPDATE SET endpoint = excluded.endpoint, method = excluded.method, "
                + "response_json = excluded.response_json, variables_json = excluded.variables_json, "
                + "validations_json = excluded.validations_json, db_mappings_json = excluded.db_mappings_json, "
                + "created_at = excluded.created_at, action_name = excluded.action_name, provider = excluded.provider, "
                + "hermes_session_id = excluded.hermes_session_id";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, memory.optString("id"));
            statement.setString(2, memory.optString("endpoint"));
            statement.setString(3, memory.optString("method"));
            statement.setString(4, memory.optJSONObject("response").toString());
            statement.setString(5, memory.optJSONArray("variables").toString());
            statement.setString(6, memory.optJSONArray("validations").toString());
            statement.setString(7, memory.optJSONArray("dbMappings").toString());
            statement.setString(8, memory.optString("createdAt"));
            statement.setString(9, memory.optString("actionName"));
            statement.setString(10, memory.optString("provider"));
            statement.setString(11, memory.optString("hermesSessionId"));
            statement.executeUpdate();
        }
    }

    private void saveApiAiMemoryToFirebase(JSONObject memory) throws Exception {
        String id = URLEncoder.encode(memory.optString("id"), StandardCharsets.UTF_8).replace("+", "%20");
        HttpResponse<String> response = HttpClient.newHttpClient().send(apiAiFirebaseRequest(API_AI_FIREBASE_PATH + "/" + id + ".json")
                        .PUT(HttpRequest.BodyPublishers.ofString(memory.toString(), StandardCharsets.UTF_8))
                        .header("Content-Type", "application/json")
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Firebase API AI memory save failed: HTTP " + response.statusCode() + " " + response.body());
        }
    }

    private HttpRequest.Builder apiAiFirebaseRequest(String path) {
        String auth = firstNonBlank(System.getProperty("testweave.firebase.authToken"),
                System.getenv("TESTWEAVE_FIREBASE_AUTH_TOKEN"));
        String uri = DashboardExecutionService.FIREBASE_URL + path + (auth.isBlank() ? "" : "?auth="
                + URLEncoder.encode(auth, StandardCharsets.UTF_8));
        return HttpRequest.newBuilder(URI.create(uri)).timeout(Duration.ofSeconds(10));
    }

    private StorageMode apiAiAgentMemoryStorageMode() {
        return apiAiAgentStorageToggle != null && apiAiAgentStorageToggle.isSelected() ? StorageMode.CLOUD : StorageMode.LOCAL;
    }

    private String inferApiActionName(ApiRequest request) {
        String segment = lastEndpointSegment(request == null ? "" : request.url);
        String noun = singularize(toCamelCase(segment.isBlank() ? "apiResponse" : segment));
        String method = request == null || request.method == null ? "GET" : request.method.toUpperCase();
        if ("POST".equals(method)) return "create" + capitalize(noun);
        if ("PUT".equals(method) || "PATCH".equals(method)) return "update" + capitalize(noun);
        if ("DELETE".equals(method)) return "delete" + capitalize(noun);
        if (noun.toLowerCase().contains("login")) return "login";
        return "get" + capitalize(noun);
    }

    private String inferDbTableName(ApiRequest request) {
        String segment = lastEndpointSegment(request == null ? "" : request.url);
        String snake = toSnakeCase(segment.isBlank() ? "api_response" : segment);
        return snake.endsWith("s") ? snake : snake + "s";
    }

    private String lastEndpointSegment(String url) {
        try {
            String path = URI.create(url).getPath();
            String[] parts = path == null ? new String[0] : path.split("/");
            for (int i = parts.length - 1; i >= 0; i--) {
                if (!parts[i].isBlank() && !parts[i].matches("\\d+")) return parts[i];
            }
        } catch (Exception ignored) {
            // Fall through to a generic action name.
        }
        return "apiResponse";
    }

    private String toCamelCase(String value) {
        String[] parts = value.replaceAll("[^A-Za-z0-9]+", " ").trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) continue;
            if (builder.isEmpty()) builder.append(part.substring(0, 1).toLowerCase()).append(part.substring(1));
            else builder.append(capitalize(part));
        }
        return builder.isEmpty() ? "apiResponse" : builder.toString();
    }

    private String toSnakeCase(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "")
                .toLowerCase();
    }

    private String singularize(String value) {
        return value != null && value.length() > 1 && value.endsWith("s") ? value.substring(0, value.length() - 1) : value;
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) return "";
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private void updateAuthControls() {
        boolean tokenAuth = "Bearer Token".equals(authTypeBox.getValue()) || "OAuth2".equals(authTypeBox.getValue());
        tokenField.setDisable(!tokenAuth);
        visibleTokenField.setDisable(!tokenAuth);
        if (!tokenAuth) {
            tokenField.clear();
        }
    }

    private void updateRequestBodyState() {
        if (bodyArea == null || methodBox == null) {
            return;
        }
        boolean bodyAllowed = !"GET".equals(methodBox.getValue()) && !"DELETE".equals(methodBox.getValue());
        bodyArea.setDisable(!bodyAllowed);
        requestFormatBox.setDisable(!bodyAllowed);
        if (!bodyAllowed) {
            bodyArea.clear();
        }
    }

    private void parseResponseFields(String body) {
        if (responseFieldRows != null) {
            responseFieldRows.clear();
        }
        if (fieldValidationRows != null) {
            fieldValidationRows.clear();
        }
        Task<List<ResponseFieldCandidate>> task = new Task<>() {
            @Override
            protected List<ResponseFieldCandidate> call() {
                return responseVariableService.parseAllFields(body);
            }
        };
        task.setOnSucceeded(e -> {
            for (ResponseFieldCandidate candidate : task.getValue()) {
                if (responseFieldRows != null) {
                    responseFieldRows.add(row("selected", "true", "jsonPath", candidate.jsonPath,
                            "field", candidate.fieldName, "preview", candidate.previewValue,
                            "variableName", normalizeVariableName(candidate.fieldName == null || candidate.fieldName.isBlank()
                                    ? candidate.jsonPath : candidate.fieldName),
                            "value", candidate.value, "type", candidate.type));
                }
                if (fieldValidationRows != null) {
                    fieldValidationRows.add(row("selected", "true", "field", candidate.jsonPath,
                            "preview", candidate.previewValue, "nullValidation", "Not Null",
                            "typeValidation", candidate.type, "operator", "equals",
                            "expected", "", "actual", candidate.value,
                            "actualType", candidate.type, "result", "Not run", "message", ""));
                }
            }
        });
        start(task);
    }

    private void resetFieldValidations() {
        if (lastResponse == null) {
            showWarning("Field Validations", "Send an API request first.");
            return;
        }
        parseResponseFields(lastResponse.rawBody);
    }

    private void resetFieldValidationDefaults() {
        if (lastResponse == null) {
            fieldValidationRows.clear();
            showWarning("Field Validations", "Send an API request first to list response fields for validation.");
            return;
        }
        for (Map<String, String> row : fieldValidationRows) {
            String actualType = row.getOrDefault("actualType", "");
            row.put("selected", "true");
            row.put("nullValidation", "Not Null");
            row.put("typeValidation", actualType.isBlank() ? "Skip" : actualType);
            row.put("expected", "");
            row.put("result", "Not run");
            row.put("message", "");
        }
        fieldValidationsTable.refresh();
        writeStandaloneValidationReport("API_VALIDATION", "API Validation", "Field Validation",
                localApiValidationReportsDirectory(), fieldValidationRows,
                "field", "expected", "actual", "nullValidation", "typeValidation", "result", "message");
    }

    private void runFieldValidations() {
        if (fieldValidationRows.isEmpty()) {
            showWarning("Field Validations", "No response fields are available to validate.");
            return;
        }
        int passed = 0;
        int failed = 0;
        for (Map<String, String> row : fieldValidationRows) {
            if (!isSelected(row)) {
                continue;
            }
            String actual = extractJsonValue(lastResponse == null ? null : lastResponse.rawBody, row.get("field"));
            if (actual == null && row.containsKey("actual")) {
                actual = row.get("actual");
            }
            String actualType = row.getOrDefault("actualType", "");
            String expected = resolveVariables(row.getOrDefault("expected", ""));
            List<String> errors = fieldValidationErrors(actualType, actual, row.getOrDefault("nullValidation", ""),
                    row.getOrDefault("typeValidation", ""), expected);
            if (errors.isEmpty()) {
                row.put("result", "Passed");
                row.put("message", "Expected checks matched");
                passed++;
            } else {
                row.put("result", "Failed: " + String.join(", ", errors));
                row.put("message", String.join(", ", errors));
                failed++;
            }
            row.put("actual", actual);
        }
        fieldValidationsTable.refresh();
    }

    private void selectTopLevelResponseFields() {
        for (Map<String, String> row : responseFieldRows) {
            String path = row.getOrDefault("jsonPath", "");
            String trimmed = path.startsWith("$.") ? path.substring(2) : path;
            row.put("selected", String.valueOf(!trimmed.contains(".") && !trimmed.contains("[")));
        }
        responseFieldsTable.refresh();
    }

    private void toggleAllSelected(ObservableList<Map<String, String>> rows, TableView<Map<String, String>> table) {
        boolean selectAll = rows.stream().anyMatch(row -> !isSelected(row));
        rows.forEach(row -> row.put("selected", String.valueOf(selectAll)));
        table.refresh();
    }

    private void chooseExpectedJson() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("API", "ExpectedResponse"));
        if (file != null) {
            expectedJsonPathField.setText(file.getAbsolutePath());
        }
    }

    private void runCompare(boolean includeMatches) {
        try {
            Path path = Path.of(expectedJsonPathField.getText());
            lastExpectedJson = Files.readString(path, StandardCharsets.UTF_8);
            lastActualJson = lastResponse == null ? rawResponseArea.getText() : lastResponse.rawBody;
            boolean strict = "Strict".equals(compareModeBox.getValue());
            List<Object[]> results = comparator.compare(lastExpectedJson, lastActualJson, strict, includeMatches);
            compareRows.clear();
            for (Object[] result : results) {
                compareRows.add(row("status", valueAt(result, 0), "path", valueAt(result, 1),
                        "expected", valueAt(result, 2), "actual", valueAt(result, 3), "message", valueAt(result, 4)));
            }
            writeStandaloneValidationReport("API_VALIDATION", "API Validation", "JSON Compare",
                    localApiValidationReportsDirectory(), compareRows,
                    "path", "expected", "actual", "status", "message");
        } catch (Exception e) {
            showError("Compare Failed", e);
        }
    }

    private void runPerformanceTest() {
        ApiRequest request = buildApiRequest(perfBodyArea.getText().isBlank() && bodyArea != null ? bodyArea.getText() : perfBodyArea.getText());
        if (request.url.isBlank()) {
            showWarning("Performance Test", "Enter an endpoint in API Tester first.");
            return;
        }
        Path reportsDirectory = localPerformanceReportsDirectory();
        perfLogArea.appendText("Starting load test...\n");
        Task<PerformanceTestResult> task = new Task<>() {
            @Override
            protected PerformanceTestResult call() throws Exception {
                return performanceTestService.runLoadTest(request, perfThreadsSpinner.getValue(),
                        perfIterationsSpinner.getValue(), reportsDirectory);
            }
        };
        task.setOnSucceeded(e -> renderPerformance(task.getValue()));
        task.setOnFailed(e -> showError("Performance Test Failed", task.getException()));
        start(task);
    }

    private void renderPerformance(PerformanceTestResult result) {
        perfSamplesLabel.setText(String.valueOf(result.samples));
        perfErrorsLabel.setText(result.errors + " (" + String.format("%.2f", result.errorPercent) + "%)");
        perfThroughputLabel.setText(String.format("%.2f / sec", result.throughputPerSecond));
        perfDurationLabel.setText(formatDuration(result.duration));
        lastPerformanceReportPath = result.reportIndexPath;
        perfReportLabel.setText(result.reportIndexPath == null ? "No report" : result.reportIndexPath.toString());
        perfLogArea.appendText("Completed " + result.samples + " samples. Report: " + perfReportLabel.getText() + "\n");
        recordPerformanceExecution(result, "Performance Load Test");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : result.chartValuesMs.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        perfChart.getData().setAll(series);
    }

    private void openPerformanceReport() {
        Path reportDirectory = localPerformanceReportsDirectory();
        if (!isReportUnder(lastPerformanceReportPath, reportDirectory)) {
            lastPerformanceReportPath = latestPerformanceReportPath();
        }
        openPath(lastPerformanceReportPath, "No HTML performance report is available yet.");
    }

    private void openTestSuiteReport() {
        Path reportDirectory = localTestSuiteReportsDirectory();
        if (!isReportUnder(lastTestSuiteReportPath, reportDirectory)) {
            lastTestSuiteReportPath = latestTestSuiteReportPath();
        }
        openPath(lastTestSuiteReportPath, "No HTML test suite report is available yet.");
    }

    private Path latestPerformanceReportPath() {
        Path reportDirectory = localPerformanceReportsDirectory();
        if (!Files.isDirectory(reportDirectory)) {
            return null;
        }
        try (var reports = Files.walk(reportDirectory)) {
            return reports
                    .filter(Files::isRegularFile)
                    .filter(path -> "index.html".equalsIgnoreCase(path.getFileName().toString()))
                    .max(this::compareLastModified)
                    .orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Path latestTestSuiteReportPath() {
        Path reportDirectory = localTestSuiteReportsDirectory();
        if (!Files.isDirectory(reportDirectory)) {
            return null;
        }
        try (var reports = Files.list(reportDirectory)) {
            return reports
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".html"))
                    .max(this::compareLastModified)
                    .orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Path writeTestSuiteReport(Path workbookPath, List<TestSuiteStepResult> results) throws Exception {
        Path reportDirectory = localTestSuiteReportsDirectory();
        Files.createDirectories(reportDirectory);
        String suiteName = workbookPath == null
                ? "test-suite"
                : workbookNameWithoutExtension(workbookPath.getFileName().toString()).replaceAll("[^A-Za-z0-9_.-]", "_");
        Path reportPath = reportDirectory.resolve(suiteName + "-report-" + System.currentTimeMillis() + ".html").toAbsolutePath();
        Files.writeString(reportPath, buildTestSuiteReportHtml(workbookPath, results), StandardCharsets.UTF_8);
        recordTestSuiteExecution(suiteName, reportPath, results);
        return reportPath;
    }

    private Path localTestSuiteReportsDirectory() {
        return localReportsDirectory("TestSuite_Reports");
    }

    private Path localPerformanceReportsDirectory() {
        return localReportsDirectory("Perfomance_Reports");
    }

    private Path localApiValidationReportsDirectory() {
        return localReportsDirectory("APIValidation_Reports");
    }

    private Path localDbValidatorReportsDirectory() {
        return localReportsDirectory("DBValidator_Reports");
    }

    private Path localWebTestingReportsDirectory() {
        return localReportsDirectory("WebTesting_Reports");
    }

    private Path configuredBaseReportsDirectory() {
        String basePathText = configBasePathField == null || configBasePathField.getText() == null
                ? "" : configBasePathField.getText().trim();
        return basePathText.isBlank()
                ? Path.of("target", "reports").toAbsolutePath().normalize()
                : Path.of(basePathText, "Reports").toAbsolutePath().normalize();
    }

    private Path localReportsDirectory(String reportTypeFolder) {
        String basePathText = configBasePathField == null || configBasePathField.getText() == null
                ? ""
                : configBasePathField.getText().trim();
        return basePathText.isBlank()
                ? Path.of("target", "reports", reportTypeFolder).toAbsolutePath().normalize()
                : Path.of(basePathText, "Reports", reportTypeFolder).toAbsolutePath().normalize();
    }

    private boolean isReportUnder(Path reportPath, Path reportDirectory) {
        return reportPath != null
                && Files.exists(reportPath)
                && reportPath.toAbsolutePath().normalize().startsWith(reportDirectory.toAbsolutePath().normalize());
    }

    private int compareLastModified(Path left, Path right) {
        try {
            return Files.getLastModifiedTime(left).compareTo(Files.getLastModifiedTime(right));
        } catch (Exception ignored) {
            return 0;
        }
    }

    private void recordTestSuiteExecution(String suiteName, Path reportPath, List<TestSuiteStepResult> results) {
        try {
            long passed = results.stream().filter(result -> result.passed).count();
            long failed = results.size() - passed;
            Set<String> cases = new java.util.HashSet<>();
            JSONArray details = new JSONArray();
            for (TestSuiteStepResult result : results) {
                cases.add(result.suite + "\u0000" + result.testCase);
                JSONArray validations = new JSONArray();
                for (TestSuiteValidationRow validation : result.validations) {
                    validations.put(new JSONObject().put("field", validation.field).put("validation", validation.validation)
                            .put("expected", validation.expected).put("actual", validation.actual)
                            .put("passed", validation.passed).put("message", validation.message));
                }
                details.put(new JSONObject().put("suite", result.suite).put("testCase", result.testCase)
                        .put("testStep", result.stepName).put("type", result.type).put("status", result.status)
                        .put("passed", result.passed).put("message", String.join(" | ", result.details))
                        .put("validations", validations));
            }
            double rate = results.isEmpty() ? 0 : passed * 100.0 / results.size();
            JSONObject execution = new JSONObject().put("id", "suite-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                    .put("type", "TEST_SUITE").put("name", suiteName).put("executedAt", System.currentTimeMillis())
                    .put("totalTestCases", cases.size()).put("totalSteps", results.size())
                    .put("passed", passed).put("failed", failed).put("passPercentage", rate)
                    .put("health", executionHealth(rate)).put("reportPath", reportPath.toString()).put("details", details);
            StorageMode storageMode = selectedDashboardStorageMode();
            dashboardExecutionService.save(execution, configuredBaseReportsDirectory(), storageMode, dashboardExecutionSqlitePath(storageMode));
        } catch (Exception ignored) {
            // Report generation must remain successful if dashboard persistence is temporarily unavailable.
        }
    }

    private void recordPerformanceExecution(PerformanceTestResult result, String name) {
        try {
            long passed = Math.max(0, result.samples - result.errors);
            double rate = result.samples == 0 ? 0 : passed * 100.0 / result.samples;
            JSONObject performance = new JSONObject().put("samples", result.samples).put("errors", result.errors)
                    .put("threads", result.threads).put("iterationsPerThread", result.iterationsPerThread)
                    .put("throughputPerSecond", result.throughputPerSecond)
                    .put("durationMs", durationMillis(result.duration)).put("minMs", durationMillis(result.min))
                    .put("averageMs", durationMillis(result.mean)).put("medianMs", durationMillis(result.median))
                    .put("p90Ms", durationMillis(result.perc90)).put("p95Ms", durationMillis(result.perc95))
                    .put("p99Ms", durationMillis(result.perc99)).put("maxMs", durationMillis(result.max))
                    .put("method", nullToBlank(result.method)).put("endpoint", nullToBlank(result.endpoint))
                    .put("requestCapturePath", result.requestCaptureJsonPath == null ? "" : result.requestCaptureJsonPath.toString());
            String report = result.reportIndexPath == null ? "" : result.reportIndexPath.toAbsolutePath().toString();
            JSONObject execution = new JSONObject().put("id", "performance-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                    .put("type", "PERFORMANCE").put("name", firstNonBlank(name, "Performance Load Test"))
                    .put("executedAt", System.currentTimeMillis()).put("totalTestCases", 1).put("totalSteps", result.samples)
                    .put("passed", passed).put("failed", result.errors).put("passPercentage", rate)
                    .put("health", executionHealth(rate)).put("reportPath", report).put("performance", performance)
                    .put("details", new JSONArray().put(new JSONObject().put("suite", name).put("testCase", "Load Test")
                            .put("testStep", result.method + " " + result.endpoint).put("type", "Performance Test")
                            .put("status", result.errors == 0 ? "Passed" : "Failed")
                            .put("message", result.samples + " samples; " + result.errors + " errors")));
            StorageMode storageMode = selectedDashboardStorageMode();
            dashboardExecutionService.save(execution, configuredBaseReportsDirectory(), storageMode, dashboardExecutionSqlitePath(storageMode));
        } catch (Exception ignored) {
            // Local execution is not failed by an unavailable dashboard backend.
        }
    }

    private void writeStandaloneValidationReport(String executionType, String suiteName, String validationName,
                                                 Path reportDirectory, List<Map<String, String>> sourceRows,
                                                 String... reportKeys) {
        try {
            List<Map<String, String>> performedRows = sourceRows.stream()
                    .filter(row -> !row.containsKey("selected") || isSelected(row))
                    .toList();
            if (performedRows.isEmpty()) {
                return;
            }
            Files.createDirectories(reportDirectory);
            String reportName = validationName.replaceAll("[^A-Za-z0-9_.-]", "_");
            Path reportPath = reportDirectory.resolve(reportName + "-report-" + System.currentTimeMillis() + ".html").toAbsolutePath();
            Files.writeString(reportPath, buildStandaloneValidationReportHtml(suiteName, validationName, performedRows, reportKeys),
                    StandardCharsets.UTF_8);
            recordStandaloneValidationExecution(executionType, suiteName, validationName, reportPath, performedRows);
        } catch (Exception ignored) {
            // Screen validation should remain successful if report/dashboard persistence is unavailable.
        }
    }

    private String buildStandaloneValidationReportHtml(String suiteName, String validationName,
                                                       List<Map<String, String>> rows, String... reportKeys) {
        long passed = rows.stream().filter(this::standaloneRowPassed).count();
        long failed = rows.size() - passed;
        int passPercent = rows.isEmpty() ? 0 : Math.round((passed * 100f) / rows.size());
        StringBuilder html = new StringBuilder("""
                <!doctype html>
                <html>
                <head>
                  <meta charset="utf-8">
                  <title>TestWeave Validation Report</title>
                  <style>
                    :root{--bg:#071018;--panel:#111827;--line:#29415f;--ink:#f8fafc;--muted:#a7b4c7;--pass:#19c37d;--fail:#ef476f;--accent:#38bdf8}
                    *{box-sizing:border-box}body{margin:0;font-family:Segoe UI,Arial,sans-serif;background:var(--bg);color:var(--ink)}
                    header{padding:24px 30px;background:#0b1625;border-bottom:1px solid var(--line)}h1{margin:0 0 6px;font-size:26px}header div{color:var(--muted)}
                    main{padding:20px 28px}.metrics{display:grid;grid-template-columns:repeat(4,minmax(130px,1fr));gap:12px;margin-bottom:18px}
                    .metric{background:var(--panel);border:1px solid var(--line);border-radius:8px;padding:14px}.metric b{display:block;font-size:28px}
                    table{width:100%;border-collapse:collapse;background:var(--panel);border:1px solid var(--line);border-radius:8px;overflow:hidden}
                    th,td{padding:10px 12px;border-bottom:1px solid var(--line);vertical-align:top;text-align:left;font-size:13px}th{color:var(--accent);background:#0f1d31}
                    pre{white-space:pre-wrap;word-break:break-word;margin:0;font:12px Consolas,monospace}.pass{color:var(--pass);font-weight:700}.fail{color:var(--fail);font-weight:700}
                  </style>
                </head>
                <body>
                """);
        html.append("<header><h1>").append(escapeXml(suiteName)).append(" - ").append(escapeXml(validationName))
                .append("</h1><div>Generated ").append(escapeXml(Instant.now().toString())).append("</div></header><main>");
        html.append("<section class=\"metrics\"><div class=\"metric\"><b>").append(rows.size()).append("</b>Total</div>")
                .append("<div class=\"metric\"><b>").append(passed).append("</b>Passed</div>")
                .append("<div class=\"metric\"><b>").append(failed).append("</b>Failed</div>")
                .append("<div class=\"metric\"><b>").append(passPercent).append("%</b>Pass Rate</div></section>");
        html.append("<table><thead><tr>");
        for (String key : reportKeys) {
            html.append("<th>").append(escapeXml(reportHeader(key))).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (Map<String, String> row : rows) {
            html.append("<tr>");
            for (String key : reportKeys) {
                String value = row.getOrDefault(key, "");
                String css = isStatusKey(key) ? (standaloneStatusPassed(value) ? " class=\"pass\"" : " class=\"fail\"") : "";
                html.append("<td").append(css).append("><pre>").append(escapeXml(value)).append("</pre></td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table></main></body></html>");
        return html.toString();
    }

    private void recordStandaloneValidationExecution(String executionType, String suiteName, String validationName,
                                                     Path reportPath, List<Map<String, String>> rows) {
        try {
            long passed = rows.stream().filter(this::standaloneRowPassed).count();
            long failed = rows.size() - passed;
            double rate = rows.isEmpty() ? 0 : passed * 100.0 / rows.size();
            JSONArray details = new JSONArray();
            for (Map<String, String> row : rows) {
                String status = standaloneRowPassed(row) ? "Passed" : "Failed";
                details.put(new JSONObject().put("suite", suiteName).put("testCase", validationName)
                        .put("testStep", standaloneStepName(row)).put("type", validationName).put("status", status)
                        .put("passed", standaloneRowPassed(row)).put("message", standaloneRowMessage(row))
                        .put("expected", firstNonBlank(row.get("expected"), row.get("expectedValueOrVariable")))
                        .put("actual", firstNonBlank(row.get("actual"), row.get("value"), row.get("selector"))));
            }
            JSONObject execution = new JSONObject().put("id", executionType.toLowerCase() + "-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                    .put("type", executionType).put("name", suiteName + " - " + validationName)
                    .put("executedAt", System.currentTimeMillis()).put("totalTestCases", 1).put("totalSteps", rows.size())
                    .put("passed", passed).put("failed", failed).put("passPercentage", rate)
                    .put("health", executionHealth(rate)).put("reportPath", reportPath.toString()).put("details", details);
            StorageMode storageMode = selectedDashboardStorageMode();
            dashboardExecutionService.save(execution, configuredBaseReportsDirectory(), storageMode, dashboardExecutionSqlitePath(storageMode));
        } catch (Exception ignored) {
            // Report generation must remain successful if dashboard persistence is temporarily unavailable.
        }
    }

    private boolean standaloneRowPassed(Map<String, String> row) {
        return standaloneStatusPassed(firstNonBlank(row.get("result"), row.get("status")));
    }

    private boolean standaloneStatusPassed(String status) {
        String normalized = nullToBlank(status).trim().toLowerCase();
        return normalized.equals("pass") || normalized.equals("passed") || normalized.equals("match")
                || normalized.startsWith("passed ");
    }

    private boolean isStatusKey(String key) {
        return "result".equals(key) || "status".equals(key);
    }

    private String standaloneStepName(Map<String, String> row) {
        return firstNonBlank(row.get("field"), row.get("path"), row.get("dbColumnName"), row.get("action"), "Validation");
    }

    private String standaloneRowMessage(Map<String, String> row) {
        return firstNonBlank(row.get("message"), row.get("result"), row.get("status"));
    }

    private String reportHeader(String key) {
        return switch (key) {
            case "apiField" -> "API Field";
            case "dbColumnName" -> "DB Column";
            case "expectedValueOrVariable" -> "Expected";
            case "nullValidation" -> "Null Validation";
            case "typeValidation" -> "Type Validation";
            default -> key == null || key.isBlank()
                    ? ""
                    : Character.toUpperCase(key.charAt(0)) + key.substring(1).replaceAll("([A-Z])", " $1");
        };
    }

    private long durationMillis(Duration value) {
        return value == null ? 0 : value.toMillis();
    }

    private String executionHealth(double passRate) {
        return passRate >= 95 ? "Excellent" : passRate >= 80 ? "Watch" : "At risk";
    }

    private String buildTestSuiteReportHtml(Path workbookPath, List<TestSuiteStepResult> results) {
        long passed = results.stream().filter(result -> result.passed).count();
        long failed = results.size() - passed;
        long total = results.size();
        int passPercent = total == 0 ? 0 : Math.round((passed * 100f) / total);
        int failPercent = total == 0 ? 0 : 100 - passPercent;
        Map<String, Map<String, List<Integer>>> tree = new LinkedHashMap<>();
        for (int i = 0; i < results.size(); i++) {
            TestSuiteStepResult result = results.get(i);
            String suite = result.suite == null || result.suite.isBlank() ? "Untitled Suite" : result.suite;
            String testCase = result.testCase == null || result.testCase.isBlank() ? "Untitled Test Case" : result.testCase;
            tree.computeIfAbsent(suite, key -> new LinkedHashMap<>())
                    .computeIfAbsent(testCase, key -> new ArrayList<>())
                    .add(i);
        }

        StringBuilder html = new StringBuilder("""
                <!doctype html>
                <html>
                <head>
                  <meta charset="utf-8">
                  <title>TestWeave Test Suite Report</title>
                  <style>
                    :root{--blue:#2f7cff;--cyan:#18d8e8;--violet:#8b4df6;--ink:#f8fbff;--muted:#b7c7f7;--line:#243b78;--bg:#05081a;--panel:#111b3d;--panel2:#0d1430;--paper:#f8fbff;--paper-ink:#111827;--pass:#20d38f;--fail:#ff4f72}
                    *{box-sizing:border-box}
                    body{font-family:Segoe UI,Arial,sans-serif;margin:0;background:var(--bg);color:var(--ink)}
                    header{background:linear-gradient(110deg,#06091f,#111b3d,#271052);color:white;padding:22px 30px;border-bottom:1px solid var(--line)}
                    header h1{margin:0 0 6px;font-size:26px}
                    header div{opacity:.9;font-size:13px;word-break:break-all}
                    main{display:grid;grid-template-columns:310px minmax(0,1fr);gap:18px;padding:18px 24px 28px}
                    aside{background:var(--panel);border:1px solid var(--line);border-radius:8px;min-height:calc(100vh - 130px);padding:14px;position:sticky;top:14px;align-self:start}
                    aside h2{margin:0 0 12px;color:var(--cyan);font-size:18px}
                    details{border-top:1px solid var(--line);padding:8px 0}
                    summary{cursor:pointer;font-weight:700;color:#ffffff}
                    .case summary{font-weight:600;color:var(--muted);margin-left:10px}
                    .step-link{display:flex;align-items:center;gap:8px;width:calc(100% - 22px);margin:6px 0 4px 22px;padding:8px 9px;border:1px solid #2a4080;border-radius:6px;background:#0d1430;color:#f8fbff;text-align:left;cursor:pointer;font:13px Segoe UI,Arial,sans-serif}
                    .step-link:hover,.step-link.active{border-color:var(--cyan);background:#17265a}
                    .dot{width:9px;height:9px;border-radius:50%;display:inline-block;flex:0 0 auto}.dot.pass{background:var(--pass)}.dot.fail{background:var(--fail)}
                    .summary-cards{display:grid;grid-template-columns:repeat(4,minmax(140px,1fr));gap:12px;margin-bottom:14px}
                    .metric{background:var(--panel);border:1px solid var(--line);padding:14px;border-radius:8px}
                    .metric b{display:block;font-size:28px;line-height:1.1}
                    .viz{display:grid;grid-template-columns:260px minmax(0,1fr);gap:14px;margin-bottom:14px}
                    .card{background:var(--panel);border:1px solid var(--line);border-radius:8px;padding:16px}
                    .pie{width:164px;height:164px;border-radius:50%;margin:8px auto;background:conic-gradient(var(--pass) 0 var(--pass-pct),var(--fail) var(--pass-pct) 100%)}
                    .bar{height:28px;display:flex;border-radius:5px;overflow:hidden;background:#18234b;margin:20px 0 10px}.passbar{background:var(--pass)}.failbar{background:var(--fail)}
                    .legend{color:var(--muted);font-size:14px}
                    .detail{display:none;background:var(--panel);border:1px solid var(--line);border-radius:8px;padding:18px}
                    .detail.active{display:block}
                    .detail-head{display:flex;justify-content:space-between;gap:16px;border-bottom:1px solid var(--line);padding-bottom:12px;margin-bottom:14px}
                    h2{margin:0;color:var(--cyan);font-size:22px}.meta{color:var(--muted);font-size:14px;margin-top:6px}
                    .pill{border-radius:999px;padding:6px 10px;font-weight:700;align-self:start}.pill.pass{background:#d9fff6;color:#07533d}.pill.fail{background:#ffe3eb;color:#8f1832}
                    .facts{display:grid;grid-template-columns:repeat(4,minmax(120px,1fr));gap:10px;margin-bottom:14px}
                    .fact{background:var(--paper);border:1px solid #b7cff6;border-radius:6px;padding:10px;color:var(--paper-ink)}.fact span{display:block;color:#526480;font-size:12px}.fact b{font-size:15px}
                    .error{background:#ffeef3;border:1px solid #ff9ab0;color:#8f1832;border-radius:6px;padding:12px;margin:12px 0}
                    table{width:100%;border-collapse:collapse;font-size:14px}
                    th,td{border:1px solid #d5e6ff;padding:8px;vertical-align:top;text-align:left;color:var(--paper-ink)}
                    th{background:#101936;color:#ffffff}.ok{color:#067a52;font-weight:700}.bad{color:#b11d3b;font-weight:700}
                    td{background:var(--paper)}
                    pre{white-space:pre-wrap;margin:0;font-family:Consolas,monospace;font-size:13px}
                    @media(max-width:960px){main{grid-template-columns:1fr}aside{position:static;min-height:auto}.summary-cards,.viz,.facts{grid-template-columns:1fr}}
                  </style>
                </head>
                <body>
                """);
        html.append("<header><h1>Test Suite Run Report</h1><div>")
                .append(escapeXml(workbookPath == null ? "" : workbookPath.toAbsolutePath().toString()))
                .append("</div></header><main>");

        html.append("<aside><h2>Execution Tree</h2>");
        for (Map.Entry<String, Map<String, List<Integer>>> suiteEntry : tree.entrySet()) {
            html.append("<details open><summary>").append(escapeXml(suiteEntry.getKey())).append("</summary>");
            for (Map.Entry<String, List<Integer>> caseEntry : suiteEntry.getValue().entrySet()) {
                html.append("<details class=\"case\" open><summary>").append(escapeXml(caseEntry.getKey())).append("</summary>");
                for (Integer reportIndex : caseEntry.getValue()) {
                    TestSuiteStepResult result = results.get(reportIndex);
                    html.append("<button class=\"step-link")
                            .append(reportIndex == 0 ? " active" : "")
                            .append("\" data-step=\"step-").append(reportIndex).append("\"><span class=\"dot ")
                            .append(result.passed ? "pass" : "fail")
                            .append("\"></span><span>")
                            .append(escapeXml(result.stepName))
                            .append("</span></button>");
                }
                html.append("</details>");
            }
            html.append("</details>");
        }
        html.append("</aside><section>");

        html.append("<div class=\"summary-cards\">")
                .append(metricHtml("Total Steps", String.valueOf(total)))
                .append(metricHtml("Passed", String.valueOf(passed)))
                .append(metricHtml("Failed", String.valueOf(failed)))
                .append(metricHtml("Pass Rate", passPercent + "%"))
                .append("</div>");
        html.append("<div class=\"viz\"><div class=\"card\"><h2>Status Split</h2><div class=\"pie\" style=\"--pass-pct:")
                .append(passPercent)
                .append("%\"></div><div class=\"legend\">Passed: ")
                .append(passed)
                .append(" | Failed: ")
                .append(failed)
                .append("</div></div><div class=\"card\"><h2>Run Health</h2><div class=\"bar\">")
                .append("<div class=\"passbar\" style=\"width:").append(passPercent).append("%\"></div>")
                .append("<div class=\"failbar\" style=\"width:").append(failPercent).append("%\"></div>")
                .append("</div><div class=\"legend\">Use the execution tree to inspect every test step, validations, expected values, actual values, and error messages.</div></div></div>");

        for (int i = 0; i < results.size(); i++) {
            TestSuiteStepResult result = results.get(i);
            String firstFailure = result.passed ? "" : result.validations.stream()
                    .filter(validation -> !validation.passed)
                    .map(validation -> validation.message)
                    .filter(message -> message != null && !message.isBlank())
                    .findFirst()
                    .orElse(result.status);
            html.append("<article id=\"step-").append(i).append("\" class=\"detail")
                    .append(i == 0 ? " active" : "")
                    .append("\"><div class=\"detail-head\"><div><h2>")
                    .append(escapeXml(result.stepName))
                    .append("</h2><div class=\"meta\">")
                    .append(escapeXml(result.suite)).append(" / ")
                    .append(escapeXml(result.testCase))
                    .append("</div></div><span class=\"pill ")
                    .append(result.passed ? "pass\">PASS" : "fail\">FAIL")
                    .append("</span></div>");
            html.append("<div class=\"facts\"><div class=\"fact\"><span>Step Type</span><b>")
                    .append(escapeXml(result.type))
                    .append("</b></div><div class=\"fact\"><span>Status</span><b>")
                    .append(escapeXml(result.status))
                    .append("</b></div><div class=\"fact\"><span>Validations</span><b>")
                    .append(result.validations.size())
                    .append("</b></div><div class=\"fact\"><span>Result</span><b>")
                    .append(result.passed ? "Passed" : "Failed")
                    .append("</b></div></div>");
            if (!firstFailure.isBlank()) {
                html.append("<div class=\"error\"><b>Failure Error Message</b><br>")
                        .append(escapeXml(firstFailure))
                        .append("</div>");
            }
            html.append("<table><thead><tr><th>Status</th><th>Field</th><th>Validation</th><th>Expected</th><th>Actual</th><th>Message</th></tr></thead><tbody>");
            if (result.validations.isEmpty()) {
                html.append("<tr><td>")
                        .append(result.passed ? "<span class=\"ok\">PASS</span>" : "<span class=\"bad\">FAIL</span>")
                        .append("</td><td>").append(escapeXml(result.stepName))
                        .append("</td><td>").append(escapeXml(result.type))
                        .append("</td><td><pre></pre></td><td><pre></pre></td><td>")
                        .append(escapeXml(result.status))
                        .append("</td></tr>");
            } else {
                for (TestSuiteValidationRow validation : result.validations) {
                    html.append("<tr><td>")
                            .append(validation.passed ? "<span class=\"ok\">PASS</span>" : "<span class=\"bad\">FAIL</span>")
                            .append("</td><td>").append(escapeXml(validation.field))
                            .append("</td><td>").append(escapeXml(validation.validation))
                            .append("</td><td><pre>").append(escapeXml(validation.expected))
                            .append("</pre></td><td><pre>").append(escapeXml(validation.actual))
                            .append("</pre></td><td>")
                            .append(escapeXml(validation.message))
                            .append("</td></tr>");
                }
            }
            html.append("</tbody></table></article>");
        }
        html.append("</section></main><script>")
                .append("document.querySelectorAll('.step-link').forEach(function(btn){btn.addEventListener('click',function(){")
                .append("document.querySelectorAll('.step-link').forEach(function(item){item.classList.remove('active')});")
                .append("document.querySelectorAll('.detail').forEach(function(item){item.classList.remove('active')});")
                .append("btn.classList.add('active');var target=document.getElementById(btn.dataset.step);")
                .append("if(target){target.classList.add('active');target.scrollIntoView({behavior:'smooth',block:'start'});}")
                .append("});});")
                .append("</script></body></html>");
        return html.toString();
    }

    private String metricHtml(String label, String value) {
        return "<div class=\"metric\"><b>" + escapeXml(value) + "</b>" + escapeXml(label) + "</div>";
    }

    private void testDbConnection() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                dbValidationService.testConnection(buildDbConfig());
                return null;
            }
        };
        dbConnectionStatusLabel.setText("Testing...");
        task.setOnSucceeded(e -> dbConnectionStatusLabel.setText("Connection OK"));
        task.setOnFailed(e -> {
            dbConnectionStatusLabel.setText("Connection failed");
            showError("DB Connection Failed", task.getException());
        });
        start(task);
    }

    private void toggleDbPasswordVisibility(Button toggleButton) {
        boolean show = !visibleDbPasswordField.isVisible();
        visibleDbPasswordField.setVisible(show);
        visibleDbPasswordField.setManaged(show);
        dbPasswordField.setVisible(!show);
        dbPasswordField.setManaged(!show);
        toggleButton.setText(show ? "Hide" : "Show");
    }

    private void saveDbConnection() {
        File file = chooseSaveFile("JSON Files", "*.json", "db-connection.json", configuredFolder("DB", "Connection"));
        if (file == null) {
            return;
        }
        try {
            DbConnectionConfig config = buildDbConfig();
            JSONObject json = new JSONObject();
            json.put("databaseType", config.databaseType);
            json.put("jdbcUrl", config.jdbcUrl);
            json.put("username", config.username);
            json.put("password", config.password);
            json.put("driverClass", config.driverClass);
            Files.writeString(file.toPath(), json.toString(2), StandardCharsets.UTF_8);
            dbConnectionFilePath = file.toPath();
            dbConnectionStatusLabel.setText("Connection saved");
        } catch (Exception e) {
            showError("Save Connection Failed", e);
        }
    }

    private void loadDbConnection() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("DB", "Connection"));
        if (file == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            dbTypeBox.setValue(json.optString("databaseType", "MySQL"));
            jdbcUrlField.setText(json.optString("jdbcUrl"));
            dbUsernameField.setText(json.optString("username"));
            dbPasswordField.setText(json.optString("password"));
            driverClassField.setText(json.optString("driverClass", "com.mysql.cj.jdbc.Driver"));
            dbConnectionFilePath = file.toPath();
            dbConnectionStatusLabel.setText("Connection loaded");
        } catch (Exception e) {
            showError("Load Connection Failed", e);
        }
    }

    private void runDbQuery() {
        Task<List<Map<String, Object>>> task = new Task<>() {
            @Override
            protected List<Map<String, Object>> call() throws Exception {
                return dbValidationService.executeQuery(buildDbConfig(), dbQueryArea.getText(),
                        lastResponse == null ? "" : lastResponse.rawBody, savedVariables);
            }
        };
        task.setOnSucceeded(e -> renderDbRows(task.getValue()));
        task.setOnFailed(e -> showError("Run Query Failed", task.getException()));
        start(task);
    }

    private void renderDbRows(List<Map<String, Object>> rows) {
        dbQueryResultRows.clear();
        rebuildDynamicTable(dbQueryResultsTable, dbQueryResultRows, rows);
        refreshDbColumnValidationRows(rows);
    }

    private void runDbAiAnalysisForResultSet() {
        if (activeApiAiHermesSession == null || nullToBlank(activeApiAiHermesSession.sessionId()).isBlank()) {
            showWarning("Hermes Agent", "Connect a Hermes session in Config before running AI Analysis.");
            updateApiAiConnectionLabels();
            return;
        }
        if (dbQueryResultRows == null || dbQueryResultRows.isEmpty()) {
            showWarning("DB AI Analysis", "Run a DB query first so AI Analysis can inspect the ResultSet.");
            return;
        }
        dbConnectionStatusLabel.setText("Hermes Agent analyzing DB resultset...");
        updateApiAiConnectionLabels("Hermes Agent analyzing DB resultset with " + apiAiConnectedModel);
        Task<DbAiSuggestion> task = new Task<>() {
            @Override protected DbAiSuggestion call() {
                return requestDbAiModelSuggestion();
            }
        };
        task.setOnSucceeded(e -> {
            dbConnectionStatusLabel.setText("DB AI suggestions ready");
            updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel);
            DbAiSuggestion suggestion = task.getValue();
            if (!suggestion.apiDbMappings.isEmpty() || !suggestion.dbValidations.isEmpty() || !suggestion.variables.isEmpty()) {
                showDbAiSuggestionWindow(suggestion);
            } else {
                showWarning("DB AI Analysis", "The connected model did not return DB mapping, validation, or variable suggestions.");
            }
        });
        task.setOnFailed(e -> {
            logApiAiConsole("DB AI Analysis task failed", task.getException());
            dbConnectionStatusLabel.setText("DB AI suggestion failed");
            updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel
                    + " (last DB analysis failed: " + exceptionMessage(rootCause(task.getException())) + ")");
            showError("DB AI Analysis Failed", task.getException());
        });
        start(task);
    }

    private DbAiSuggestion requestDbAiModelSuggestion() {
        try {
            String prompt = dbAiSuggestionPrompt();
            logApiAiConsole("DB AI Analysis prompt built, characters=" + prompt.length());
            String output = runHermesApiAiPrompt(prompt);
            logApiAiConsole("DB AI Analysis model output received, characters=" + (output == null ? 0 : output.length()));
            JSONObject root = extractJsonObject(output);
            return dbAiSuggestionFromModelJson(root);
        } catch (Exception e) {
            logApiAiConsole("DB AI Analysis failed while preparing, running, or parsing model suggestion", e);
            throw new RuntimeException(e);
        }
    }

    private String dbAiSuggestionPrompt() throws Exception {
        JSONObject input = new JSONObject()
                .put("dbType", dbTypeBox == null ? "" : dbTypeBox.getValue())
                .put("jdbcUrl", jdbcUrlField == null ? "" : scrubJdbcUrl(jdbcUrlField.getText()))
                .put("sqlQuery", dbQueryArea == null ? "" : dbQueryArea.getText())
                .put("resultSet", dbResultSetSample())
                .put("lastApiRequest", lastApiRequestSummary())
                .put("lastApiResponseFields", lastApiResponseFieldSample())
                .put("knowledgeRepositoryMode", apiAiAgentMemoryStorageMode().name())
                .put("knowledgeRepository", loadApiAiMemoryRepositorySample());
        return """
                You are TestWeave API-DB Quality Brain.
                Analyze this DB ResultSet with the API details and knowledge repository below.
                Return ONLY strict JSON. No markdown, no prose.

                Required JSON shape:
                {
                  "apiDbMappings": [
                    {"apiField":"$.orderId","jsonPath":"$.orderId","dbColumn":"order_id","operator":"=","description":"API orderId matches DB order_id"}
                  ],
                  "dbValidations": [
                    {"dbColumnName":"order_id[0]","nullValidation":"must not be null","typeValidation":"uuid","expectedValueOrVariable":"","description":"Validate persisted order id"}
                  ],
                  "variables": [
                    {"variable":"${dbOrderId}","dbColumnName":"order_id[0]","value":"123","type":"uuid","description":"Store order id for later validations"}
                  ]
                }

                Rules:
                - Use the ResultSet columns as DB Column values.
                - Prefer API-DB mappings when an API JSON path or stored memory field clearly corresponds to a DB column.
                - Suggest individual DB validations for identifiers, statuses, totals, amounts, dates, foreign keys, and non-null business fields.
                - Suggest variables only for reusable identifiers, correlation keys, tokens, status values, and generated references.
                - For dbColumnName include [rowIndex] when the validation or variable points to a concrete ResultSet cell.
                - Do not invent API fields absent from the API response sample or knowledge repository.

                Input:
                """ + input.toString(2);
    }

    private String scrubJdbcUrl(String jdbcUrl) {
        return nullToBlank(jdbcUrl).replaceAll("(?i)(password|pwd)=([^;&]+)", "$1=***");
    }

    private JSONArray dbResultSetSample() {
        JSONArray rows = new JSONArray();
        if (dbQueryResultRows == null) {
            return rows;
        }
        int rowIndex = 0;
        for (Map<String, String> row : dbQueryResultRows) {
            JSONObject item = new JSONObject();
            item.put("_rowIndex", rowIndex);
            for (Map.Entry<String, String> entry : row.entrySet()) {
                if (!"row".equals(entry.getKey())) {
                    item.put(entry.getKey(), entry.getValue());
                }
            }
            rows.put(item);
            if (++rowIndex >= 10) {
                break;
            }
        }
        return rows;
    }

    private JSONObject lastApiRequestSummary() {
        ApiRequest request = endpointField == null ? null : buildApiRequest(bodyArea == null ? "" : bodyArea.getText());
        return new JSONObject()
                .put("method", request == null ? "" : request.method)
                .put("endpoint", request == null ? "" : request.url)
                .put("hasResponse", lastResponse != null && !nullToBlank(lastResponse.rawBody).isBlank());
    }

    private JSONArray lastApiResponseFieldSample() {
        JSONArray fields = new JSONArray();
        if (lastResponse == null || nullToBlank(lastResponse.rawBody).isBlank()) {
            return fields;
        }
        for (ResponseFieldCandidate candidate : responseVariableService.parseAllFields(lastResponse.rawBody)) {
            if (List.of("object", "array", "null").contains(candidate.type)) {
                continue;
            }
            fields.put(new JSONObject()
                    .put("jsonPath", candidate.jsonPath)
                    .put("fieldName", candidate.fieldName)
                    .put("value", shorten(candidate.value, 180))
                    .put("type", candidate.type));
            if (fields.length() >= 60) {
                break;
            }
        }
        return fields;
    }

    private JSONArray loadApiAiMemoryRepositorySample() throws Exception {
        return apiAiAgentMemoryStorageMode() == StorageMode.LOCAL
                ? loadApiAiMemoryRepositoryFromSqlite()
                : loadApiAiMemoryRepositoryFromFirebase();
    }

    private JSONArray loadApiAiMemoryRepositoryFromSqlite() throws Exception {
        JSONArray memories = new JSONArray();
        Path sqliteDbPath = configCacheDatabasePathFromField();
        if (!Files.exists(sqliteDbPath)) {
            return memories;
        }
        initializeApiAiMemoryTable(sqliteDbPath);
        String sql = "SELECT id, endpoint, method, response_json, variables_json, validations_json, db_mappings_json, created_at, action_name "
                + "FROM " + API_AI_MEMORY_TABLE + " ORDER BY created_at DESC LIMIT 20";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                memories.put(apiAiMemorySummary(
                        resultSet.getString("id"),
                        resultSet.getString("endpoint"),
                        resultSet.getString("method"),
                        resultSet.getString("action_name"),
                        resultSet.getString("response_json"),
                        resultSet.getString("variables_json"),
                        resultSet.getString("validations_json"),
                        resultSet.getString("db_mappings_json"),
                        resultSet.getString("created_at")));
            }
        }
        return memories;
    }

    private JSONArray loadApiAiMemoryRepositoryFromFirebase() throws Exception {
        JSONArray memories = new JSONArray();
        HttpResponse<String> response = HttpClient.newHttpClient().send(apiAiFirebaseRequest(API_AI_FIREBASE_PATH + ".json")
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404 || response.body() == null || response.body().isBlank() || "null".equals(response.body())) {
            return memories;
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Firebase API AI memory load failed: HTTP " + response.statusCode() + " " + response.body());
        }
        JSONObject root = new JSONObject(response.body());
        List<JSONObject> items = new ArrayList<>();
        for (String id : root.keySet()) {
            JSONObject item = root.optJSONObject(id);
            if (item != null) {
                items.add(item);
            }
        }
        items.sort((left, right) -> right.optString("createdAt").compareTo(left.optString("createdAt")));
        for (JSONObject item : items) {
            memories.put(apiAiMemorySummary(
                    item.optString("id"),
                    item.optString("endpoint"),
                    item.optString("method"),
                    item.optString("actionName"),
                    item.optJSONObject("response") == null ? "{}" : item.optJSONObject("response").toString(),
                    item.optJSONArray("variables") == null ? "[]" : item.optJSONArray("variables").toString(),
                    item.optJSONArray("validations") == null ? "[]" : item.optJSONArray("validations").toString(),
                    item.optJSONArray("dbMappings") == null ? "[]" : item.optJSONArray("dbMappings").toString(),
                    item.optString("createdAt")));
            if (memories.length() >= 20) {
                break;
            }
        }
        return memories;
    }

    private JSONObject apiAiMemorySummary(String id, String endpoint, String method, String actionName,
                                          String responseJson, String variablesJson, String validationsJson,
                                          String dbMappingsJson, String createdAt) {
        JSONObject response = safeJsonObject(responseJson);
        return new JSONObject()
                .put("id", nullToBlank(id))
                .put("endpoint", nullToBlank(endpoint))
                .put("method", nullToBlank(method))
                .put("actionName", nullToBlank(actionName))
                .put("createdAt", nullToBlank(createdAt))
                .put("responseStatus", response.optString("status"))
                .put("responsePreview", shorten(response.optString("body"), 900))
                .put("variables", safeJsonArray(variablesJson))
                .put("validations", safeJsonArray(validationsJson))
                .put("dbMappings", safeJsonArray(dbMappingsJson));
    }

    private JSONObject safeJsonObject(String text) {
        try {
            return new JSONObject(nullToBlank(text).isBlank() ? "{}" : text);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private JSONArray safeJsonArray(String text) {
        try {
            return new JSONArray(nullToBlank(text).isBlank() ? "[]" : text);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private DbAiSuggestion dbAiSuggestionFromModelJson(JSONObject root) {
        DbAiSuggestion suggestion = new DbAiSuggestion();
        JSONArray mappings = root.optJSONArray("apiDbMappings");
        if (mappings != null) for (int i = 0; i < mappings.length(); i++) {
            JSONObject item = mappings.optJSONObject(i);
            if (item == null) continue;
            String apiField = firstNonBlank(item.optString("apiField"), item.optString("jsonPath"));
            String dbColumn = firstNonBlank(item.optString("dbColumn"), item.optString("column"));
            if (apiField.isBlank() || dbColumn.isBlank()) continue;
            suggestion.apiDbMappings.add(row("selected", "true",
                    "apiField", apiField,
                    "dbColumn", dbColumn,
                    "operator", firstNonBlank(item.optString("operator"), "="),
                    "description", item.optString("description", apiField + " matches " + dbColumn)));
        }
        JSONArray validations = root.optJSONArray("dbValidations");
        if (validations != null) for (int i = 0; i < validations.length(); i++) {
            JSONObject item = validations.optJSONObject(i);
            if (item == null) continue;
            String columnName = firstNonBlank(item.optString("dbColumnName"), item.optString("dbColumn"), item.optString("column"));
            if (columnName.isBlank()) continue;
            String value = firstNonBlank(item.optString("value"), dbResultValueForColumnReference(columnName));
            suggestion.dbValidations.add(row("selected", "true",
                    "dbColumnName", columnName,
                    "value", value,
                    "nullValidation", firstNonBlank(item.optString("nullValidation"), value.isBlank() ? "must be null" : "must not be null"),
                    "typeValidation", firstNonBlank(item.optString("typeValidation"), inferTypeValidation(value)),
                    "expectedValueOrVariable", item.optString("expectedValueOrVariable"),
                    "result", "AI Suggested",
                    "description", item.optString("description")));
        }
        JSONArray variables = root.optJSONArray("variables");
        if (variables != null) for (int i = 0; i < variables.length(); i++) {
            JSONObject item = variables.optJSONObject(i);
            if (item == null) continue;
            String dbColumnName = firstNonBlank(item.optString("dbColumnName"), item.optString("dbColumn"), item.optString("column"));
            String value = firstNonBlank(item.optString("value"), dbResultValueForColumnReference(dbColumnName));
            String variable = item.optString("variable");
            if (variable.isBlank()) {
                variable = "${" + normalizeVariableName(firstNonBlank(dbColumnName, "dbValue")) + "}";
            }
            suggestion.variables.add(row("selected", "true",
                    "variable", variable,
                    "dbColumnName", dbColumnName,
                    "value", value,
                    "type", firstNonBlank(item.optString("type"), inferTypeValidation(value)),
                    "description", item.optString("description")));
        }
        return suggestion;
    }

    private String dbResultValueForColumnReference(String reference) {
        if (dbQueryResultRows == null || dbQueryResultRows.isEmpty() || reference == null || reference.isBlank()) {
            return "";
        }
        String column = reference;
        int rowIndex = 0;
        Matcher matcher = Pattern.compile("^(.*)\\[(\\d+)]$").matcher(reference);
        if (matcher.matches()) {
            column = matcher.group(1);
            rowIndex = Integer.parseInt(matcher.group(2));
        }
        if (rowIndex < 0 || rowIndex >= dbQueryResultRows.size()) {
            rowIndex = 0;
        }
        return dbQueryResultRows.get(rowIndex).getOrDefault(column, "");
    }

    private void showDbAiSuggestionWindow(DbAiSuggestion suggestion) {
        ObservableList<Map<String, String>> mappingRows = FXCollections.observableArrayList(suggestion.apiDbMappings);
        ObservableList<Map<String, String>> validationRows = FXCollections.observableArrayList(suggestion.dbValidations);
        ObservableList<Map<String, String>> variableRows = FXCollections.observableArrayList(suggestion.variables);
        TableView<Map<String, String>> mappingsTable = mapTable(mappingRows,
                "Add", "selected", "API Field", "apiField", "DB Column", "dbColumn", "Operator", "operator", "Description", "description");
        TableView<Map<String, String>> validationsTable = mapTable(validationRows,
                "Add", "selected", "DB Column Name", "dbColumnName", "Value", "value",
                "Null Validation", "nullValidation", "Type Validation", "typeValidation",
                "Expected Value / Variable", "expectedValueOrVariable", "Description", "description");
        TableView<Map<String, String>> variablesTable = mapTable(variableRows,
                "Save", "selected", "Variable", "variable", "DB Column Name", "dbColumnName",
                "Value", "value", "Type", "type", "Description", "description");
        mappingsTable.setPrefHeight(230);
        validationsTable.setPrefHeight(260);
        variablesTable.setPrefHeight(210);
        Button toggleAll = secondary("Toggle");
        Button saveMemory = primary("Save in Memory");
        Button importSuggestion = primary("Import");
        Button close = secondary("Close");
        Stage suggestionStage = new Stage();
        if (stage != null) {
            suggestionStage.initOwner(stage);
        }
        toggleAll.setOnAction(e -> {
            boolean selected = mappingRows.stream().anyMatch(row -> !isSelected(row))
                    || validationRows.stream().anyMatch(row -> !isSelected(row))
                    || variableRows.stream().anyMatch(row -> !isSelected(row));
            setAllRowsSelected(mappingRows, mappingsTable, selected);
            setAllRowsSelected(validationRows, validationsTable, selected);
            setAllRowsSelected(variableRows, variablesTable, selected);
        });
        saveMemory.setOnAction(e -> saveDbAiSuggestionMemory(suggestion, mappingRows, validationRows, variableRows));
        importSuggestion.setOnAction(e -> importDbAiSuggestionToTabs(mappingRows, validationRows, variableRows));
        close.setOnAction(e -> suggestionStage.close());
        VBox content = new VBox(14,
                sectionTitle("DB AI Suggestion"),
                new Label("Hermes Agent used the DB ResultSet and API AI knowledge repository to suggest API-DB mappings, DB validations, and reusable variables."),
                card("API-DB Validator Suggestions", mappingsTable),
                card("DB Validation Suggestions", validationsTable),
                card("Variable Suggestions", variablesTable));
        content.setPadding(new Insets(16));
        VBox.setVgrow(mappingsTable, Priority.ALWAYS);
        VBox.setVgrow(validationsTable, Priority.ALWAYS);
        VBox.setVgrow(variablesTable, Priority.ALWAYS);
        ScrollPane scroller = new ScrollPane(content);
        scroller.setFitToWidth(true);
        scroller.setPannable(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FlowPane footer = actionRow(toggleAll, saveMemory, importSuggestion, close);
        footer.setPadding(new Insets(12, 16, 16, 16));
        footer.getStyleClass().add("card");
        BorderPane shell = new BorderPane(scroller);
        shell.setBottom(footer);
        double[] suggestionSize = apiAiSuggestionWindowSize();
        Scene scene = new Scene(shell, suggestionSize[0], suggestionSize[1]);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        suggestionStage.setTitle("AI Suggestion - DB Validator");
        suggestionStage.setScene(scene);
        suggestionStage.setMinWidth(Math.min(760, suggestionSize[0]));
        suggestionStage.setMinHeight(Math.min(520, suggestionSize[1]));
        suggestionStage.show();
    }

    private void importDbAiSuggestionToTabs(List<Map<String, String>> mappings, List<Map<String, String>> validations,
                                            List<Map<String, String>> variables) {
        dbRuleRows.clear();
        for (Map<String, String> mapping : mappings) {
            if (!isSelected(mapping)) continue;
            dbRuleRows.add(row("selected", "true",
                    "apiField", mapping.getOrDefault("apiField", ""),
                    "dbColumn", mapping.getOrDefault("dbColumn", ""),
                    "operator", firstNonBlank(mapping.get("operator"), "="),
                    "description", mapping.getOrDefault("description", "Imported from DB AI Analysis")));
        }
        dbColumnValidationRows.clear();
        for (Map<String, String> validation : validations) {
            if (!isSelected(validation)) continue;
            dbColumnValidationRows.add(row("selected", "true",
                    "dbColumnName", validation.getOrDefault("dbColumnName", ""),
                    "value", validation.getOrDefault("value", ""),
                    "nullValidation", validation.getOrDefault("nullValidation", ""),
                    "typeValidation", validation.getOrDefault("typeValidation", ""),
                    "expectedValueOrVariable", validation.getOrDefault("expectedValueOrVariable", ""),
                    "result", "Imported"));
        }
        int variableCount = saveSelectedDbAiVariablesInMemory(variables);
        dbRulesTable.refresh();
        dbColumnValidationsTable.refresh();
        refreshVariablesView();
        showInfo("DB AI Analysis", "Imported " + dbRuleRows.size() + " API-DB mapping(s), "
                + dbColumnValidationRows.size() + " DB validation(s), and saved "
                + variableCount + " variable(s) in memory.");
    }

    private void saveDbAiSuggestionMemory(DbAiSuggestion suggestion, List<Map<String, String>> mappings,
                                          List<Map<String, String>> validations, List<Map<String, String>> variables) {
        int variableCount = saveSelectedDbAiVariablesInMemory(variables);
        JSONObject memory = new JSONObject()
                .put("id", "db-ai-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                .put("endpoint", "DB ResultSet: " + shorten(dbQueryArea == null ? "" : dbQueryArea.getText(), 180))
                .put("method", "DB_QUERY")
                .put("actionName", "dbResultSetAnalysis")
                .put("hermesSessionId", activeApiAiHermesSession == null ? "" : activeApiAiHermesSession.sessionId())
                .put("provider", activeApiAiHermesSession == null ? "Codex" : "Hermes")
                .put("response", new JSONObject()
                        .put("status", "DB AI Analysis")
                        .put("body", new JSONObject()
                                .put("dbType", dbTypeBox == null ? "" : dbTypeBox.getValue())
                                .put("sqlQuery", dbQueryArea == null ? "" : dbQueryArea.getText())
                                .put("resultSet", dbResultSetSample()).toString()))
                .put("variables", selectedRowsJson(variables))
                .put("validations", selectedRowsJson(validations))
                .put("dbMappings", selectedRowsJson(mappings))
                .put("createdAt", Instant.now().toString());
        saveApiAiMemory(memory);
        refreshVariablesView();
        showInfo("DB AI Analysis", "Saved " + variableCount + " selected variable(s) in memory and queued repository persistence.");
    }

    private int saveSelectedDbAiVariablesInMemory(List<Map<String, String>> variables) {
        int count = 0;
        for (Map<String, String> variable : variables) {
            if (!isSelected(variable)) continue;
            String name = variable.getOrDefault("variable", "").replace("${", "").replace("}", "");
            if (name.isBlank()) {
                name = normalizeVariableName(variable.getOrDefault("dbColumnName", "dbValue"));
            }
            String value = variable.getOrDefault("value", "");
            String column = variable.getOrDefault("dbColumnName", "");
            savedVariables.put(name, value);
            savedVariablePaths.put(name, "db:" + column);
            savedVariableTypes.put(name, firstNonBlank(variable.get("type"), "DB Result"));
            count++;
        }
        return count;
    }

    private JSONArray selectedRowsJson(List<Map<String, String>> rows) {
        JSONArray selected = new JSONArray();
        for (Map<String, String> row : rows) {
            if (isSelected(row)) {
                selected.put(new JSONObject(row));
            }
        }
        return selected;
    }

    private void runDbValidation() {
        List<DbValidationRule> rules = new ArrayList<>();
        for (Map<String, String> row : dbRuleRows) {
            if (!isSelected(row)) {
                continue;
            }
            DbValidationRule rule = new DbValidationRule();
            rule.apiField = row.get("apiField");
            rule.dbColumn = row.get("dbColumn");
            rule.operator = row.getOrDefault("operator", "=");
            rule.description = row.get("description");
            rules.add(rule);
        }
        if (rules.isEmpty()) {
            showWarning("DB Validation", "Add at least one selected validation rule.");
            return;
        }
        String apiResponseBody = dbValidationApiResponseBody(rules);
        if (apiResponseBody == null) {
            return;
        }
        Task<DbValidationReport> task = new Task<>() {
            @Override
            protected DbValidationReport call() throws Exception {
                return dbValidationService.validate(buildDbConfig(), dbQueryArea.getText(),
                        rules, apiResponseBody, savedVariables);
            }
        };
        task.setOnSucceeded(e -> renderDbValidation(task.getValue()));
        task.setOnFailed(e -> showError("DB Validation Failed", task.getException()));
        start(task);
    }

    private String dbValidationApiResponseBody(List<DbValidationRule> rules) {
        if (lastResponse != null && !nullToBlank(lastResponse.rawBody).isBlank()) {
            return lastResponse.rawBody;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        if (stage != null) {
            confirm.initOwner(stage);
        }
        confirm.setTitle("Use Project Knowledge?");
        confirm.setHeaderText("I don't see any live API request being hit.");
        confirm.setContentText("Shall I use the project knowledge to look for the last saved response and run DB validation?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return null;
        }
        try {
            ApiResponse saved = loadSavedApiResponseForDbValidation(rules);
            if (saved == null || nullToBlank(saved.rawBody).isBlank()) {
                showWarning("DB Validation", "No saved API response memory was found in the selected project knowledge store.");
                return null;
            }
            lastResponse = saved;
            renderResponse(saved);
            showInfo("DB Validation", "Loaded saved API response from project knowledge for validation.");
            return saved.rawBody;
        } catch (Exception e) {
            showError("Load Saved Response Failed", e);
            return null;
        }
    }

    private ApiResponse loadSavedApiResponseForDbValidation(List<DbValidationRule> rules) throws Exception {
        JSONArray memories = apiAiAgentMemoryStorageMode() == StorageMode.LOCAL
                ? loadSavedApiResponseMemoriesFromSqlite()
                : loadSavedApiResponseMemoriesFromFirebase();
        JSONObject best = null;
        int bestScore = -1;
        for (int i = 0; i < memories.length(); i++) {
            JSONObject memory = memories.optJSONObject(i);
            if (memory == null) continue;
            JSONObject response = memory.optJSONObject("response");
            String body = savedResponseBody(response);
            int score = savedResponseScore(body, rules);
            if (score > bestScore) {
                best = memory;
                bestScore = score;
            }
        }
        if (best == null) {
            return null;
        }
        JSONObject response = best.optJSONObject("response");
        String body = savedResponseBody(response);
        if (body.isBlank()) {
            return null;
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.rawBody = body;
        apiResponse.prettyBody = body;
        apiResponse.statusLine = response == null ? "Saved Response" : firstNonBlank(response.optString("status"), "Saved Response");
        apiResponse.statusCode = response == null ? 0 : response.optInt("statusCode", 0);
        apiResponse.headersText = response == null ? "" : response.optString("headers");
        apiResponse.cookiesText = response == null ? "" : response.optString("cookies");
        apiResponse.sizeBytes = body.getBytes(StandardCharsets.UTF_8).length;
        apiResponse.timeMs = 0;
        return apiResponse;
    }

    private JSONArray loadSavedApiResponseMemoriesFromSqlite() throws Exception {
        JSONArray memories = new JSONArray();
        Path sqliteDbPath = configCacheDatabasePathFromField();
        if (!Files.exists(sqliteDbPath)) {
            return memories;
        }
        initializeApiAiMemoryTable(sqliteDbPath);
        String sql = "SELECT id, endpoint, method, response_json, variables_json, validations_json, db_mappings_json, created_at, action_name "
                + "FROM " + API_AI_MEMORY_TABLE + " WHERE action_name = ? ORDER BY created_at DESC LIMIT 50";
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "savedApiResponse");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    memories.put(new JSONObject()
                            .put("id", resultSet.getString("id"))
                            .put("endpoint", resultSet.getString("endpoint"))
                            .put("method", resultSet.getString("method"))
                            .put("actionName", resultSet.getString("action_name"))
                            .put("response", safeJsonObject(resultSet.getString("response_json")))
                            .put("variables", safeJsonArray(resultSet.getString("variables_json")))
                            .put("validations", safeJsonArray(resultSet.getString("validations_json")))
                            .put("dbMappings", safeJsonArray(resultSet.getString("db_mappings_json")))
                            .put("createdAt", resultSet.getString("created_at")));
                }
            }
        }
        return memories;
    }

    private JSONArray loadSavedApiResponseMemoriesFromFirebase() throws Exception {
        JSONArray memories = new JSONArray();
        HttpResponse<String> response = HttpClient.newHttpClient().send(apiAiFirebaseRequest(API_AI_FIREBASE_PATH + ".json")
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404 || response.body() == null || response.body().isBlank() || "null".equals(response.body())) {
            return memories;
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Firebase saved response memory load failed: HTTP " + response.statusCode() + " " + response.body());
        }
        JSONObject root = new JSONObject(response.body());
        List<JSONObject> items = new ArrayList<>();
        for (String id : root.keySet()) {
            JSONObject item = root.optJSONObject(id);
            if (item != null && "savedApiResponse".equals(item.optString("actionName"))) {
                items.add(item);
            }
        }
        items.sort((left, right) -> right.optString("createdAt").compareTo(left.optString("createdAt")));
        for (JSONObject item : items) {
            memories.put(item);
            if (memories.length() >= 50) {
                break;
            }
        }
        return memories;
    }

    private String savedResponseBody(JSONObject response) {
        if (response == null) {
            return "";
        }
        String pathText = response.optString("savedResponsePath");
        if (!pathText.isBlank()) {
            try {
                Path path = Path.of(pathText).toAbsolutePath().normalize();
                if (Files.isRegularFile(path)) {
                    return Files.readString(path, StandardCharsets.UTF_8);
                }
            } catch (Exception ignored) {
                // Fall back to response body captured in memory.
            }
        }
        return response.optString("body");
    }

    private int savedResponseScore(String body, List<DbValidationRule> rules) {
        if (body == null || body.isBlank()) {
            return 0;
        }
        int score = 0;
        for (DbValidationRule rule : rules) {
            String apiField = nullToBlank(rule.apiField);
            if (!apiField.isBlank() && extractJsonValue(body, apiField) != null) {
                score += 3;
            } else if (!apiField.isBlank() && body.toLowerCase().contains(apiField.replace("$.", "").toLowerCase())) {
                score++;
            }
        }
        return score;
    }

    private void renderDbValidation(DbValidationReport report) {
        dbResultRows.clear();
        for (DbValidationResult result : report.results) {
            dbResultRows.add(row("result", result.passed ? "Pass" : "Fail", "field", result.field,
                    "expected", result.expectedValue, "actual", result.actualValue, "operator", result.operator, "message", result.message));
        }
        dbSummaryLabel.setText(report.passed + " passed / " + report.failed + " failed");
        if (report.dbRows != null) {
            renderDbRows(report.dbRows);
        }
        writeStandaloneValidationReport("DB_VALIDATOR", "DB Validator", "API-DB Validation",
                localDbValidatorReportsDirectory(), dbResultRows,
                "field", "expected", "actual", "operator", "result", "message");
    }

    private DbConnectionConfig buildDbConfig() {
        DbConnectionConfig config = new DbConnectionConfig();
        config.databaseType = dbTypeBox.getValue();
        config.jdbcUrl = jdbcUrlField.getText();
        config.username = dbUsernameField.getText();
        config.password = dbPasswordField.getText();
        config.driverClass = driverClassField.getText();
        return config;
    }

    private void applyDbDefaults() {
        switch (dbTypeBox.getValue()) {
            case "PostgreSQL" -> {
                jdbcUrlField.setText("jdbc:postgresql://localhost:5432/your_database");
                driverClassField.setText("org.postgresql.Driver");
            }
            case "Oracle" -> {
                jdbcUrlField.setText("jdbc:oracle:thin:@localhost:1521:xe");
                driverClassField.setText("oracle.jdbc.OracleDriver");
            }
            case "SQL Server" -> {
                jdbcUrlField.setText("jdbc:sqlserver://localhost:1433;databaseName=your_database;encrypt=false");
                driverClassField.setText("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            }
            default -> {
                jdbcUrlField.setText("jdbc:mysql://localhost:3306/your_database");
                driverClassField.setText("com.mysql.cj.jdbc.Driver");
            }
        }
    }

    private void saveSelectedDbResultCellAsVariable() {
        TablePosition<Map<String, String>, ?> position = dbQueryResultsTable.getSelectionModel().getSelectedCells().stream()
                .findFirst()
                .orElse(null);
        if (position == null || position.getRow() < 0 || position.getTableColumn() == null) {
            showWarning("Save DB Variable", "Select a resultset cell to save as a variable.");
            return;
        }
        String key = position.getTableColumn().getId();
        if (key == null || key.isBlank() || "row".equals(key)) {
            showWarning("Save DB Variable", "Select a data column cell, not the row number.");
            return;
        }
        Map<String, String> selectedRow = dbQueryResultRows.get(position.getRow());
        String value = selectedRow.getOrDefault(key, "");
        TextInputDialog dialog = new TextInputDialog(normalizeVariableName(key));
        dialog.setTitle("Save DB Variable");
        dialog.setHeaderText("Save selected DB cell as variable");
        dialog.showAndWait().ifPresent(name -> {
            String variableName = normalizeVariableName(name);
            savedVariables.put(variableName, value);
            savedVariablePaths.put(variableName, "db:" + key + "[" + position.getRow() + "]");
            savedVariableTypes.put(variableName, "DB Result");
            refreshVariablesView();
            showInfo("Save DB Variable", "Saved ${" + variableName + "} from DB resultset.");
        });
    }

    private void populateDefaultDbRules() {
        dbRuleRows.clear();
        if (lastResponse == null || lastResponse.rawBody == null || lastResponse.rawBody.isBlank()) {
            dbRuleRows.add(row("selected", "true", "apiField", "$.id", "dbColumn", "id", "operator", "=", "description", "API id equals DB id"));
            return;
        }
        for (ResponseFieldCandidate candidate : responseVariableService.parseFields(lastResponse.rawBody)) {
            dbRuleRows.add(row("selected", "true", "apiField", candidate.jsonPath,
                    "dbColumn", candidate.fieldName, "operator", "=", "description", candidate.fieldName + " matches"));
        }
        dbRulesTable.refresh();
    }

    private void setAllRowsSelected(ObservableList<Map<String, String>> rows, TableView<Map<String, String>> table, boolean selected) {
        rows.forEach(row -> row.put("selected", String.valueOf(selected)));
        table.refresh();
    }

    private void loadDbColumnOptions() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return dbValidationService.fetchColumnLabels(buildDbConfig(), dbQueryArea.getText(),
                        lastResponse == null ? "" : lastResponse.rawBody, savedVariables);
            }
        };
        task.setOnSucceeded(e -> {
            dbRuleRows.clear();
            for (String column : task.getValue()) {
                dbRuleRows.add(row("selected", "true", "apiField", "$." + column,
                        "dbColumn", column, "operator", "=", "description", column + " matches"));
            }
            dbRulesTable.refresh();
        });
        task.setOnFailed(e -> showError("Load DB Columns Failed", task.getException()));
        start(task);
    }

    private void saveDbRules() {
        JSONArray rules = new JSONArray();
        for (Map<String, String> row : dbRuleRows) {
            rules.put(new JSONObject(row));
        }
        saveTextFile(rules.toString(2), "dbrules.json", configuredFolder("DB", "DBRules"));
    }

    private void loadDbRules() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("DB", "DBRules"));
        if (file == null) {
            return;
        }
        try {
            JSONArray rules = new JSONArray(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            dbRuleRows.clear();
            for (int i = 0; i < rules.length(); i++) {
                JSONObject item = rules.getJSONObject(i);
                dbRuleRows.add(row("selected", item.optString("selected", "true"),
                        "apiField", item.optString("apiField"),
                        "dbColumn", item.optString("dbColumn"),
                        "operator", item.optString("operator", "="),
                        "description", item.optString("description")));
            }
        } catch (Exception e) {
            showError("Load Rules Failed", e);
        }
    }

    private void refreshDbColumnValidationRows(List<Map<String, Object>> dbRows) {
        dbColumnValidationRows.clear();
        if (dbRows == null || dbRows.isEmpty()) {
            return;
        }
        int rowIndex = 0;
        for (Map<String, Object> dbRow : dbRows) {
            for (Map.Entry<String, Object> entry : dbRow.entrySet()) {
                String value = String.valueOf(entry.getValue());
                dbColumnValidationRows.add(row("selected", "true",
                        "dbColumnName", entry.getKey() + "[" + rowIndex + "]",
                        "value", value,
                        "nullValidation", value == null || "null".equals(value) ? "must be null" : "must not be null",
                        "typeValidation", inferTypeValidation(value),
                        "expectedValueOrVariable", value,
                        "result", "Ready"));
            }
            rowIndex++;
        }
    }

    private void resetDbColumnValidationDefaults() {
        for (Map<String, String> row : dbColumnValidationRows) {
            String value = row.getOrDefault("value", "");
            row.put("selected", "true");
            row.put("nullValidation", value.isBlank() || "null".equalsIgnoreCase(value) ? "must be null" : "must not be null");
            row.put("typeValidation", inferTypeValidation(value));
            row.put("expectedValueOrVariable", value);
            row.put("result", "Ready");
        }
        dbColumnValidationsTable.refresh();
    }

    private void runDbColumnValidations() {
        for (Map<String, String> row : dbColumnValidationRows) {
            if (!isSelected(row)) {
                continue;
            }
            String actual = row.getOrDefault("value", "");
            String expected = resolveVariables(row.getOrDefault("expectedValueOrVariable", ""));
            String nullRule = row.getOrDefault("nullValidation", "");
            String normalizedNullRule = "must not be null".equalsIgnoreCase(nullRule) ? "Not Null"
                    : "must be null".equalsIgnoreCase(nullRule) ? "Null" : nullRule;
            String typeRule = row.getOrDefault("typeValidation", "");
            String normalizedTypeRule = "any".equalsIgnoreCase(typeRule) ? "Skip" : typeRule;
            String actualType = "null".equalsIgnoreCase(actual) || actual == null || actual.isBlank() ? "null" : inferTypeValidation(actual);
            List<String> reasons = dbColumnValidationErrors(actualType, actual,
                    normalizedNullRule, normalizedTypeRule, expected);
            boolean passed = reasons.isEmpty();
            row.put("result", passed ? "Pass" : "Fail");
            row.put("message", passed ? "Expected checks matched" : String.join(", ", reasons));
        }
        dbColumnValidationsTable.refresh();
        writeStandaloneValidationReport("DB_VALIDATOR", "DB Validator", "DB Validation",
                localDbValidatorReportsDirectory(), dbColumnValidationRows,
                "dbColumnName", "expectedValueOrVariable", "value", "nullValidation", "typeValidation", "result", "message");
    }

    private String inferTypeValidation(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
            return "any";
        }
        if (value.matches("-?\\d+")) {
            return "integer";
        }
        if (value.matches("-?\\d+\\.\\d+")) {
            return "number";
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return "boolean";
        }
        return "string";
    }

    private void startWebRecording() {
        if (webStartUrlField.getText().isBlank()) {
            showWarning("Web Recording", "Start URL is required before recording.");
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                playwrightRecorderController.startRecording(webStartUrlField.getText(), recorderListener());
                return null;
            }
        };
        task.setOnFailed(e -> showError("Web Recording Failed", task.getException()));
        start(task);
    }

    private void startAttachedWebRecording() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                playwrightRecorderController.startAttachedRecording(webCdpEndpointField.getText(), recorderListener());
                return null;
            }
        };
        task.setOnFailed(e -> showError("Attach Browser Failed", task.getException()));
        start(task);
    }

    private PlaywrightRecorderController.RecorderListener recorderListener() {
        return new PlaywrightRecorderController.RecorderListener() {
            @Override
            public void onStatus(String message) {
                Platform.runLater(() -> webRecorderStatusLabel.setText(message));
            }

            @Override
            public void onStepCaptured(WebTestStep step) {
                Platform.runLater(() -> appendWebStep(step));
            }

            @Override
            public void onRecordingStopped() {
                Platform.runLater(() -> webRecorderStatusLabel.setText("Recorder stopped"));
            }

            @Override
            public void onUrlChanged(String url) {
                Platform.runLater(() -> webBrowserUrlLabel.setText("Browser URL: " + url));
            }

            @Override
            public void onError(String message) {
                Platform.runLater(() -> showError("Web Recorder Error", new RuntimeException(message)));
            }
        };
    }

    private void stopWebRecording() {
        playwrightRecorderController.stopRecording();
    }

    private void appendWebStep(WebTestStep step) {
        webStepRows.add(row("step", String.valueOf(webStepRows.size() + 1), "action", step.action,
                "selector", step.selector, "value", step.value, "note", step.note));
    }

    private void addWebStepDialog() {
        showWebStepDialog("Add Web Step", null).ifPresent(step -> {
                Map<String, String> row = row("step", String.valueOf(webStepRows.size() + 1),
                        "action", step.getOrDefault("action", ""),
                        "selector", step.getOrDefault("selector", ""),
                        "value", step.getOrDefault("value", ""),
                        "note", step.getOrDefault("note", ""),
                        "flowVariableName", step.getOrDefault("flowVariableName", ""));
                webStepRows.add(row);
                registerFlowVariableFromRow(row);
        });
    }

    private void editSelectedWebStep() {
        Map<String, String> selected = webStepsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Web Testing", "Select a web step to edit.");
            return;
        }
        showWebStepDialog("Edit Web Step", selected).ifPresent(step -> {
            String previousFlowVariableName = flowVariableName(selected);
            selected.put("action", step.getOrDefault("action", ""));
            selected.put("selector", step.getOrDefault("selector", ""));
            selected.put("value", step.getOrDefault("value", ""));
            selected.put("note", step.getOrDefault("note", ""));
            selected.put("flowVariableName", step.getOrDefault("flowVariableName", ""));
            String nextFlowVariableName = flowVariableName(selected);
            if (!previousFlowVariableName.isBlank() && !previousFlowVariableName.equals(nextFlowVariableName)) {
                savedVariables.remove(previousFlowVariableName);
                savedVariablePaths.remove(previousFlowVariableName);
                savedVariableTypes.remove(previousFlowVariableName);
            }
            registerFlowVariableFromRow(selected);
            webStepsTable.refresh();
        });
    }

    private java.util.Optional<Map<String, String>> showWebStepDialog(String title, Map<String, String> existing) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        if (stage != null) {
            dialog.initOwner(stage);
        }
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResizable(true);
        styleDialogPane(dialog.getDialogPane());

        ComboBox<String> actionField = new ComboBox<>(FXCollections.observableArrayList(WEB_TEST_ACTIONS));
        actionField.setEditable(true);
        actionField.setValue(existing == null ? "Navigate" : firstNonBlank(existing.getOrDefault("action", ""), "Navigate"));
        TextField selectorField = new TextField(existing == null ? "" : existing.getOrDefault("selector", ""));
        TextField valueField = new TextField(existing == null ? "" : existing.getOrDefault("value", ""));
        TextField flowVariableNameField = new TextField(existing == null ? "" : existing.getOrDefault("flowVariableName",
                flowVariableName(existing)));
        TextArea noteArea = editor(existing == null ? "" : existing.getOrDefault("note", ""));
        noteArea.setPrefRowCount(3);
        noteArea.setMinHeight(90);

        ComboBox<String> variableBox = createVariableDropdown();
        ComboBox<String> targetBox = combo("Selector", "Value", "Note", "Flow Variable Name");
        Button insertVariable = secondary("Insert Variable");
        insertVariable.setOnAction(e -> {
            if (variableBox.getValue() == null) {
                showWarning("Web Step", "Select a variable before inserting.");
                return;
            }
            String target = targetBox.getValue();
            if ("Selector".equals(target)) {
                insertVariable(selectorField, variableBox);
            } else if ("Value".equals(target)) {
                insertVariable(valueField, variableBox);
            } else {
                if ("Flow Variable Name".equals(target)) {
                    insertVariable(flowVariableNameField, variableBox);
                } else {
                    insertVariable(noteArea, variableBox);
                }
            }
        });
        Runnable updateFlowVariableMode = () -> {
            boolean flowVariable = "Flow Variable".equalsIgnoreCase(nullToBlank(actionField.getValue()));
            selectorField.setDisable(flowVariable);
            flowVariableNameField.setDisable(!flowVariable);
            selectorField.setPromptText(flowVariable ? "Disabled for Flow Variable" : "");
            valueField.setPromptText(flowVariable ? "Value or ${variable} to store" : "");
            flowVariableNameField.setPromptText(flowVariable ? "Variable name to create, for example customerName" : "");
            if (flowVariable) {
                selectorField.clear();
            }
        };
        actionField.valueProperty().addListener((observable, oldValue, newValue) -> updateFlowVariableMode.run());
        actionField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> updateFlowVariableMode.run());
        updateFlowVariableMode.run();

        GridPane form = grid();
        form.setPrefWidth(720);
        form.add(labeled("Action", actionField), 0, 0, 2, 1);
        form.add(labeled("Selector", selectorField), 0, 1, 2, 1);
        form.add(labeled("Value", valueField), 0, 2, 2, 1);
        form.add(labeled("Flow Variable Name", flowVariableNameField), 0, 3, 2, 1);
        form.add(labeled("Note", noteArea), 0, 4, 2, 1);
        form.add(labeled("Variables", variableBox), 0, 5);
        form.add(labeled("Apply To", targetBox), 1, 5);
        form.add(insertVariable, 0, 6, 2, 1);
        GridPane.setHgrow(actionField, Priority.ALWAYS);
        GridPane.setHgrow(selectorField, Priority.ALWAYS);
        GridPane.setHgrow(valueField, Priority.ALWAYS);
        GridPane.setHgrow(noteArea, Priority.ALWAYS);

        VBox shell = new VBox(14, themedSubwindowHeader(title, dialog), form);
        shell.setMinWidth(0);
        dialog.getDialogPane().setContent(shell);
        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            String action = firstNonBlank(actionField.getEditor().getText(), actionField.getValue());
            boolean flowVariable = "Flow Variable".equalsIgnoreCase(action);
            String flowName = flowVariable ? normalizeVariableName(flowVariableNameField.getText()) : "";
            return row("action", action,
                    "selector", flowVariable ? flowName : selectorField.getText(),
                    "value", valueField.getText(),
                    "note", noteArea.getText(),
                    "flowVariableName", flowName);
        });
        return dialog.showAndWait();
    }

    private void moveSelectedWebStep(int direction) {
        int index = webStepsTable.getSelectionModel().getSelectedIndex();
        int target = index + direction;
        if (index < 0 || target < 0 || target >= webStepRows.size()) {
            return;
        }
        Map<String, String> item = webStepRows.remove(index);
        webStepRows.add(target, item);
        renumberWebSteps();
        webStepsTable.getSelectionModel().select(target);
    }

    private void addWebScreenshotStep() {
        webStepRows.add(row("step", String.valueOf(webStepRows.size() + 1),
                "action", "screenshot", "selector", "", "value", "", "note", "Capture screenshot"));
    }

    private void clearWebSteps() {
        webStepRows.clear();
        webResultRows.clear();
        webRunSummaryLabel.setText("--");
    }

    private void mergeWebRecording() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("WebUI", "Recording"));
        if (file == null) {
            return;
        }
        int before = webStepRows.size();
        loadWebRecordingFile(file, true);
        showInfo("Merge Recording", (webStepRows.size() - before) + " step(s) merged.");
    }

    private void renumberWebSteps() {
        for (int i = 0; i < webStepRows.size(); i++) {
            webStepRows.get(i).put("step", String.valueOf(i + 1));
        }
    }

    private void launchDebugChrome() {
        String startUrl = webStartUrlField == null ? "" : webStartUrlField.getText().trim();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Path userDataDir = Path.of(System.getProperty("java.io.tmpdir"), "testweave-debug-chrome-profile");
                Files.createDirectories(userDataDir);

                List<String> command = new ArrayList<>();
                command.add(resolveChromeExecutable());
                command.add("--remote-debugging-port=9222");
                command.add("--user-data-dir=" + userDataDir.toAbsolutePath());
                command.add("--no-first-run");
                command.add("--no-default-browser-check");
                if (!startUrl.isBlank()) {
                    command.add(startUrl);
                }

                new ProcessBuilder(command).start();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            webCdpEndpointField.setText("http://127.0.0.1:9222");
            webRecorderStatusLabel.setText("Debug Chrome launched on port 9222. Click Attach to record.");
        });
        task.setOnFailed(e -> showError("Launch Debug Chrome Failed", task.getException()));
        start(task);
    }

    private String resolveChromeExecutable() {
        List<String> candidates = chromeExecutableCandidates();
        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }
            Path path = Path.of(candidate);
            if (Files.isRegularFile(path)) {
                return path.toString();
            }
        }

        String osName = System.getProperty("os.name", "").toLowerCase();
        if (osName.contains("win")) {
            return "chrome.exe";
        }
        if (osName.contains("mac")) {
            return "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
        }
        return "google-chrome";
    }

    private List<String> chromeExecutableCandidates() {
        String localAppData = System.getenv("LOCALAPPDATA");
        String programFiles = System.getenv("PROGRAMFILES");
        String programFilesX86 = System.getenv("PROGRAMFILES(X86)");
        String home = System.getProperty("user.home", "");
        return List.of(
                localAppData == null ? "" : localAppData + "\\Google\\Chrome\\Application\\chrome.exe",
                programFiles == null ? "" : programFiles + "\\Google\\Chrome\\Application\\chrome.exe",
                programFilesX86 == null ? "" : programFilesX86 + "\\Google\\Chrome\\Application\\chrome.exe",
                "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
                home + "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
                "/usr/bin/google-chrome",
                "/usr/local/bin/google-chrome",
                "/usr/bin/chromium-browser",
                "/usr/bin/chromium"
        );
    }

    private void stopWebRecordingWithoutClosingBrowser() {
        playwrightRecorderController.stopRecordingWithoutClosingBrowser();
    }

    private void setupCodexCliDocker() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String image = codexDockerImage();
                appendCodexLog("Checking Codex CLI Docker image availability: " + image);

                requireDocker();
                if (!dockerImageExists(image)) {
                    appendCodexLog("Codex CLI image not found locally. Pulling " + image + ".");
                    runDockerCommandWithLogs(List.of("pull", image));
                    appendCodexLog("Codex CLI image installed: " + image);
                } else {
                    appendCodexLog("Codex CLI image is already available locally: " + image);
                }
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendCodexLog("Codex CLI setup failed: " + exceptionMessage(task.getException()));
            showError("Codex CLI Setup Failed", task.getException());
        });
        start(task);
    }

    private void startCodexCliContainer() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String image = codexDockerImage();
                String containerName = codexContainerName();
                requireDocker();
                if (!dockerImageExists(image)) {
                    throw new IllegalStateException("Codex CLI image was not found locally. Run setup first to pull " + image + ".");
                }
                if (!dockerContainerExists(containerName)) {
                    appendCodexLog("Codex CLI container does not exist. Creating it from image: " + image);
                    runCodexCliContainer(image, containerName);
                    appendCodexLog("Codex CLI container started: " + containerName);
                    verifyCodexCli(containerName);
                    openCodexCliWindow(containerName);
                    return null;
                }
                if (dockerContainerRunning(containerName)) {
                    appendCodexLog("Codex CLI container is already running: " + containerName);
                    verifyCodexCli(containerName);
                    openCodexCliWindow(containerName);
                    return null;
                }
                runDockerCommand(List.of("start", containerName));
                appendCodexLog("Codex CLI container started: " + containerName);
                verifyCodexCli(containerName);
                openCodexCliWindow(containerName);
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendCodexLog("Codex CLI start failed: " + exceptionMessage(task.getException()));
            showError("Codex CLI Start Failed", task.getException());
        });
        start(task);
    }

    private void stopCodexCliContainer() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String containerName = codexContainerName();
                requireDocker();
                if (!dockerContainerExists(containerName)) {
                    appendCodexLog("Codex CLI container was not found: " + containerName);
                    return null;
                }
                if (!dockerContainerRunning(containerName)) {
                    appendCodexLog("Codex CLI container is already stopped: " + containerName);
                    return null;
                }
                appendCodexLog("Stopping Codex CLI container: " + containerName);
                cancelCodexChatProcess();
                runDockerCommand(List.of("stop", containerName));
                appendCodexLog("Codex CLI container stopped: " + containerName);
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendCodexLog("Codex CLI stop failed: " + exceptionMessage(task.getException()));
            showError("Codex CLI Stop Failed", task.getException());
        });
        start(task);
    }

    private void setupHermesAgentDocker() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String image = hermesDockerImage();
                String containerName = hermesContainerName();
                appendHermesLog("Starting Hermes Agent setup.");
                appendHermesLog("Checking Hermes Agent Docker image availability: " + image);

                requireDocker(ApiValidatorFxApp.this::appendHermesLog);
                if (!dockerImageExists(image)) {
                    appendHermesLog("Hermes Agent image not found locally. Pulling " + image + ".");
                    runDockerCommandWithLogs(List.of("pull", image), ApiValidatorFxApp.this::appendHermesProcessLine);
                    appendHermesLog("Hermes Agent image installed: " + image);
                } else {
                    appendHermesLog("Hermes Agent image is already available locally: " + image);
                }

                if (!dockerContainerExists(containerName)) {
                    appendHermesLog("Creating Hermes Agent container: " + containerName);
                    runHermesAgentContainer(image, containerName);
                    appendHermesLog("Hermes Agent container created and started: " + containerName);
                    appendHermesLog("Opening Hermes setup wizard in a terminal.");
                    launchHermesCli(containerName, "setup");
                } else if (!dockerContainerHasExpectedMount(containerName, "/opt/data", hermesDataDirectory())) {
                    appendHermesLog("Hermes Agent container is using an older AI Agent path. Recreating it.");
                    recreateHermesAgentContainer(image, containerName);
                    appendHermesLog("Opening Hermes setup wizard in a terminal.");
                    launchHermesCli(containerName, "setup");
                } else {
                    appendHermesLog("Hermes Agent container is already configured: " + containerName);
                }
                appendHermesLog("Hermes Agent setup complete.");
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendHermesLog("Hermes Agent setup failed: " + exceptionMessage(task.getException()));
            showError("Hermes Agent Setup Failed", task.getException());
        });
        start(task);
    }

    private void startHermesAgentContainer() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String image = hermesDockerImage();
                String containerName = hermesContainerName();
                requireDocker(ApiValidatorFxApp.this::appendHermesLog);
                ensureHermesContainerRunning(image, containerName, false);
                verifyHermesAgent(containerName);
                launchHermesCli(containerName, null);
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendHermesLog("Hermes Agent start failed: " + exceptionMessage(task.getException()));
            showError("Hermes Agent Start Failed", task.getException());
        });
        start(task);
    }

    private void launchHermesAgentBrowser() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String image = hermesDockerImage();
                String containerName = hermesContainerName();
                requireDocker(ApiValidatorFxApp.this::appendHermesLog);
                ensureHermesContainerRunning(image, containerName, true);
                startHermesDashboard(containerName);
                waitForHermesDashboard();
                Platform.runLater(() -> getHostServices().showDocument(HERMES_DASHBOARD_URL));
                appendHermesLog("Opened Hermes dashboard in browser: " + HERMES_DASHBOARD_URL);
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendHermesLog("Hermes browser launch failed: " + exceptionMessage(task.getException()));
            showError("Hermes Browser Launch Failed", task.getException());
        });
        start(task);
    }

    private void stopHermesAgentContainer() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String containerName = hermesContainerName();
                requireDocker(ApiValidatorFxApp.this::appendHermesLog);
                if (!dockerContainerExists(containerName)) {
                    appendHermesLog("Hermes Agent container was not found: " + containerName);
                    return null;
                }
                if (!dockerContainerRunning(containerName)) {
                    appendHermesLog("Hermes Agent container is already stopped: " + containerName);
                    return null;
                }
                appendHermesLog("Stopping Hermes Agent container: " + containerName);
                runDockerCommandWithLogs(List.of("stop", containerName), ApiValidatorFxApp.this::appendHermesProcessLine);
                appendHermesLog("Hermes Agent container stopped: " + containerName);
                return null;
            }
        };
        task.setOnFailed(e -> {
            appendHermesLog("Hermes Agent stop failed: " + exceptionMessage(task.getException()));
            showError("Hermes Agent Stop Failed", task.getException());
        });
        start(task);
    }

    private void appendCodexLog(String message) {
        if (codexLogArea == null) {
            return;
        }
        Platform.runLater(() -> {
            String timestamp = LocalTime.now().withNano(0).toString();
            codexLogArea.appendText("[" + timestamp + "] " + message + System.lineSeparator());
        });
    }

    private void appendHermesLog(String message) {
        if (hermesLogArea == null && configHermesLogArea == null) {
            return;
        }
        Platform.runLater(() -> {
            String timestamp = LocalTime.now().withNano(0).toString();
            String line = "[" + timestamp + "] " + message + System.lineSeparator();
            if (hermesLogArea != null) {
                hermesLogArea.appendText(line);
            }
            if (configHermesLogArea != null) {
                configHermesLogArea.appendText(line);
            }
        });
    }

    private String codexDockerImage() {
        String image = System.getenv(CODEX_DOCKER_IMAGE_ENV);
        return image == null || image.isBlank() ? CODEX_DOCKER_IMAGE_DEFAULT : image.trim();
    }

    private String codexContainerName() {
        String name = System.getenv(CODEX_CONTAINER_NAME_ENV);
        return name == null || name.isBlank() ? CODEX_CONTAINER_NAME_DEFAULT : name.trim();
    }

    private String hermesDockerImage() {
        String image = System.getenv(HERMES_DOCKER_IMAGE_ENV);
        return image == null || image.isBlank() ? HERMES_DOCKER_IMAGE_DEFAULT : image.trim();
    }

    private String hermesContainerName() {
        String name = System.getenv(HERMES_CONTAINER_NAME_ENV);
        return name == null || name.isBlank() ? HERMES_CONTAINER_NAME_DEFAULT : name.trim();
    }

    private void requireDocker() throws Exception {
        requireDocker(this::appendCodexLog);
    }

    private void requireDocker(java.util.function.Consumer<String> logConsumer) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "version", "--format", "{{.Server.Version}}"));
        if (!result.success()) {
            throw new IllegalStateException("Docker is not available or the Docker daemon is not running. "
                    + result.output.trim());
        }
        logConsumer.accept("Docker is available: " + firstOutputLine(result.output));
    }

    private boolean dockerContainerExists(String containerName) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "ps", "-a", "--filter", "name=^/" + containerName + "$",
                "--format", "{{.Names}}"));
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        return result.output.lines().anyMatch(line -> containerName.equals(line.trim()));
    }

    private boolean dockerContainerRunning(String containerName) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "ps", "--filter", "name=^/" + containerName + "$",
                "--filter", "status=running", "--format", "{{.Names}}"));
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        return result.output.lines().anyMatch(line -> containerName.equals(line.trim()));
    }

    private boolean dockerImageExists(String image) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "image", "inspect", image));
        return result.success();
    }

    private void ensureHermesContainerRunning(String image, String containerName, boolean requireDashboardPort) throws Exception {
        if (!dockerImageExists(image)) {
            throw new IllegalStateException("Hermes Agent image was not found locally. Run setup first to pull " + image + ".");
        }
        if (dockerContainerExists(containerName)
                && (!dockerContainerHasExpectedMount(containerName, "/opt/data", hermesDataDirectory())
                || (requireDashboardPort && (!dockerContainerPublishesPort(containerName, "9119/tcp")
                || dockerContainerHasEnv(containerName, "HERMES_DASHBOARD")
                || !dockerContainerHasMount(containerName, hermesContainerUserDirectory()))))) {
            appendHermesLog("Hermes container needs an AI Agent path, mount, or dashboard port update. Recreating it.");
            recreateHermesAgentContainer(image, containerName);
            return;
        }
        if (!dockerContainerExists(containerName)) {
            appendHermesLog("Hermes Agent container does not exist. Creating it from image: " + image);
            runHermesAgentContainer(image, containerName);
            appendHermesLog("Hermes Agent container started: " + containerName);
        } else if (!dockerContainerRunning(containerName)) {
            appendHermesLog("Starting Hermes Agent container: " + containerName);
            runDockerCommandWithLogs(List.of("start", containerName), this::appendHermesProcessLine);
            appendHermesLog("Hermes Agent container started: " + containerName);
        } else {
            appendHermesLog("Hermes Agent container is already running: " + containerName);
        }
    }

    private boolean dockerContainerPublishesPort(String containerName, String containerPort) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "port", containerName, containerPort));
        return result.success() && !result.output.trim().isBlank();
    }

    private boolean dockerContainerHasEnv(String containerName, String envName) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "inspect", "--format", "{{range .Config.Env}}{{println .}}{{end}}", containerName));
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        return result.output.lines().anyMatch(line -> line.startsWith(envName + "="));
    }

    private boolean dockerContainerHasMount(String containerName, String destination) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "inspect", "--format", "{{range .Mounts}}{{println .Destination}}{{end}}", containerName));
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        return result.output.lines().anyMatch(line -> destination.equals(line.trim()));
    }

    private boolean dockerContainerHasExpectedMount(String containerName, String destination, Path expectedSource) throws Exception {
        ProcessResult result = runCommand(List.of("docker", "inspect", containerName));
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        String expected = expectedSource.toAbsolutePath().normalize().toString();
        String expectedForward = expected.replace('\\', '/');
        String expectedLower = expectedForward.toLowerCase();
        String dockerDesktopPath = dockerDesktopHostPath(expectedForward).toLowerCase();
        JSONArray containers = new JSONArray(result.output);
        for (int i = 0; i < containers.length(); i++) {
            JSONArray mounts = containers.getJSONObject(i).optJSONArray("Mounts");
            if (mounts == null) {
                continue;
            }
            for (int j = 0; j < mounts.length(); j++) {
                JSONObject mount = mounts.getJSONObject(j);
                if (!destination.equals(mount.optString("Destination"))) {
                    continue;
                }
                String source = mount.optString("Source").replace('\\', '/').toLowerCase();
                if (source.equals(expectedLower) || source.equals(dockerDesktopPath) || source.endsWith("/" + dockerDesktopPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String dockerDesktopHostPath(String hostPath) {
        if (hostPath.length() > 2 && hostPath.charAt(1) == ':') {
            char drive = Character.toLowerCase(hostPath.charAt(0));
            String remainder = hostPath.substring(2);
            if (!remainder.startsWith("/")) {
                remainder = "/" + remainder;
            }
            return "/run/desktop/mnt/host/" + drive + remainder;
        }
        return hostPath;
    }

    private void recreateHermesAgentContainer(String image, String containerName) throws Exception {
        if (dockerContainerRunning(containerName)) {
            appendHermesLog("Stopping existing Hermes container before updating it: " + containerName);
            runDockerCommandWithLogs(List.of("stop", containerName), this::appendHermesProcessLine);
        }
        appendHermesLog("Removing existing Hermes container. Data remains in " + hermesDataDirectory() + ".");
        runDockerCommandWithLogs(List.of("rm", containerName), this::appendHermesProcessLine);
        runHermesAgentContainer(image, containerName);
        appendHermesLog("Hermes Agent container recreated with dashboard port and Windows home mount: " + containerName);
    }

    private void runCodexCliContainer(String image, String containerName) throws Exception {
        Path workspaceDirectory = Path.of("").toAbsolutePath().normalize();
        String workspaceVolume = workspaceDirectory + ":/workspace";
        String configVolume = codexConfigDirectory() + ":/root/.codex";
        appendCodexLog("Mounting project workspace into Codex CLI container: " + workspaceVolume);
        appendCodexLog("Mounting Codex config into Codex CLI container: " + configVolume);
        runDockerCommand(List.of("run", "-d",
                "--name", containerName,
                "-v", workspaceVolume,
                "-v", configVolume,
                "-w", "/workspace",
                "--entrypoint", "sleep",
                image,
                "infinity"));
    }

    private void runHermesAgentContainer(String image, String containerName) throws Exception {
        String dataVolume = hermesDataDirectory() + ":/opt/data";
        String userHomeVolume = hermesHostUserDirectory() + ":" + hermesContainerUserDirectory();
        appendHermesLog("Mounting Hermes data into container: " + dataVolume);
        appendHermesLog("Mounting Windows user home into container: " + userHomeVolume);
        runDockerCommandWithLogs(List.of("run", "-d",
                "--name", containerName,
                "--restart", "unless-stopped",
                "-v", dataVolume,
                "-v", userHomeVolume,
                "-p", "8642:8642",
                "-p", "127.0.0.1:9119:9119",
                image,
                "sleep",
                "infinity"), this::appendHermesProcessLine);
    }

    private Path codexConfigDirectory() throws Exception {
        Path directory = Path.of(System.getProperty("user.home"), ".codex").toAbsolutePath().normalize();
        Files.createDirectories(directory);
        return directory;
    }

    private Path hermesDataDirectory() throws Exception {
        Path directory = hermesAiAgentPathField != null && hermesAiAgentPathField.getText() != null
                && !hermesAiAgentPathField.getText().trim().isBlank()
                ? Path.of(hermesAiAgentPathField.getText().trim()).toAbsolutePath().normalize()
                : configuredAiAgentDirectory();
        if (directory == null) {
            directory = Path.of(System.getProperty("user.home"), ".hermes").toAbsolutePath().normalize();
        }
        Files.createDirectories(directory);
        updateHermesAiAgentPathField();
        return directory;
    }

    private Path hermesHostUserDirectory() {
        return Path.of(System.getProperty("user.home")).toAbsolutePath().normalize();
    }

    private String hermesContainerUserDirectory() {
        return "/opt/data/Users/" + System.getProperty("user.name");
    }

    private void verifyCodexCli(String containerName) {
        try {
            ProcessResult result = runDockerCommand(List.of("exec", containerName, "codex", "--version"));
            appendCodexLog("Codex CLI is available in container: " + firstOutputLine(result.output));
        } catch (Exception e) {
            appendCodexLog("Codex CLI container started, but CLI verification failed: " + exceptionMessage(e));
        }
    }

    private void verifyHermesAgent(String containerName) {
        try {
            ProcessResult result = runDockerCommand(List.of("exec", containerName, "hermes", "--version"));
            appendHermesLog("Hermes Agent CLI is available in container: " + firstOutputLine(result.output));
        } catch (Exception e) {
            appendHermesLog("Hermes Agent container started, but CLI verification failed: " + exceptionMessage(e));
        }
    }

    private void startHermesDashboard(String containerName) throws Exception {
        if (isHermesDashboardReachable()) {
            appendHermesLog("Hermes dashboard is already running: " + HERMES_DASHBOARD_URL);
            return;
        }
        appendHermesLog("Starting Hermes dashboard on " + HERMES_DASHBOARD_URL + ".");
        stopHermesDashboardProcess(containerName);
        runDockerCommandWithLogs(List.of("exec", "-d",
                containerName,
                "hermes",
                "dashboard",
                "--host", "0.0.0.0",
                "--port", "9119",
                "--no-open",
                "--insecure"), this::appendHermesProcessLine);
    }

    private void stopHermesDashboardProcess(String containerName) {
        try {
            runDockerCommand(List.of("exec", containerName, "pkill", "-f", "hermes dashboard"));
        } catch (Exception e) {
            appendHermesLog("No existing Hermes dashboard process needed cleanup.");
        }
    }

    private void waitForHermesDashboard() throws Exception {
        for (int attempt = 0; attempt < 30; attempt++) {
            if (isHermesDashboardReachable()) {
                return;
            }
            Thread.sleep(500);
        }
        throw new IllegalStateException("Hermes dashboard did not respond at " + HERMES_DASHBOARD_URL + ".");
    }

    private boolean isHermesDashboardReachable() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(HERMES_DASHBOARD_URL))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() > 0 && response.statusCode() < 500;
        } catch (Exception e) {
            return false;
        }
    }

    private void launchHermesCli(String containerName, String hermesCommand) throws Exception {
        Path aiAgentPath = hermesDataDirectory();
        Files.createDirectories(aiAgentPath);
        Path sessionDirectory = aiAgentPath.resolve("Sessions");
        Files.createDirectories(sessionDirectory);
        String selectedSession = hermesSessionBox == null ? HERMES_NEW_SESSION : hermesSessionBox.getValue();
        HermesSessionRecord selectedRecord = hermesSessionRecords.get(selectedSession);
        List<String> dockerArgs = new ArrayList<>(List.of("exec", "-it",
                "-w", "/opt/data",
                "-e", "TERM=xterm-256color",
                "-e", "AI_AGENT_PATH=/opt/data",
                "-e", "TESTWEAVE_AI_AGENT_PATH=/opt/data",
                containerName, "hermes"));
        if ((hermesCommand == null || hermesCommand.isBlank()) && selectedRecord != null && !nullToBlank(selectedRecord.sessionId()).isBlank()) {
            dockerArgs.add("--resume");
            dockerArgs.add(selectedRecord.sessionId());
        } else if (hermesCommand != null && !hermesCommand.isBlank()) {
            dockerArgs.addAll(splitShellWords(hermesCommand));
        }

        String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(java.time.LocalDateTime.now());
        Path transcriptPath = sessionDirectory.resolve("hermes-session-" + timestamp + ".log");
        Path runnerPath = sessionDirectory.resolve("launch-hermes-" + timestamp + ".ps1");
        Files.writeString(runnerPath, powerShellHermesScript(containerName, aiAgentPath, transcriptPath, dockerArgs), StandardCharsets.UTF_8);

        String parentCommand = "Start-Process -FilePath 'powershell.exe' -ArgumentList @('-NoExit','-NoProfile','-ExecutionPolicy','Bypass','-File',"
                + powershellQuote(runnerPath.toString()) + ") -Wait";
        Process watcherProcess = new ProcessBuilder("powershell.exe", "-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", parentCommand)
                .redirectErrorStream(true)
                .start();
        watcherProcess.onExit().thenRun(() -> appendHermesLog("Hermes PowerShell window closed. Transcript: " + transcriptPath));
        appendHermesLog("Opened Hermes CLI in PowerShell for container: " + containerName);
        appendHermesLog("AI Agent output path: " + aiAgentPath);
        appendHermesLog("Hermes transcript path: " + transcriptPath);
        appendHermesLog("Hermes CLI command: docker " + String.join(" ", dockerArgs));
    }

    private String powerShellHermesScript(String containerName, Path aiAgentPath, Path transcriptPath, List<String> dockerArgs) {
        StringBuilder command = new StringBuilder();
        command.append("$Host.UI.RawUI.WindowTitle = 'TestWeave - Powered by Vision AI'; ");
        command.append("$ErrorActionPreference = 'Continue'; ");
        command.append("Start-Transcript -Path '").append(psSingleQuoteContent(transcriptPath.toString())).append("' -Force | Out-Null; ");
        command.append("Write-Host 'Powered by Vision AI'; ");
        command.append("Write-Host 'AI Agent Path: ").append(psSingleQuoteContent(aiAgentPath.toString())).append("'; ");
        command.append("Write-Host 'Container: ").append(psSingleQuoteContent(containerName)).append("'; ");
        command.append("Write-Host 'Container working directory: /opt/data'; ");
        command.append("Write-Host ''; ");
        command.append("$dockerArgs = @(");
        for (int i = 0; i < dockerArgs.size(); i++) {
            if (i > 0) {
                command.append(",");
            }
            command.append("'").append(psSingleQuoteContent(dockerArgs.get(i))).append("'");
        }
        command.append("); ");
        command.append("try { & docker @dockerArgs } finally { ");
        command.append("Write-Host ''; ");
        command.append("Write-Host '[Vision AI process exited. Close this PowerShell window to return to TestWeave.]'; ");
        command.append("try { Stop-Transcript | Out-Null } catch {} }");
        return command.toString();
    }

    private List<String> splitShellWords(String command) {
        List<String> words = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|'([^']*)'|(\\S+)").matcher(nullToBlank(command));
        while (matcher.find()) {
            words.add(firstNonBlank(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
        return words;
    }

    private void watchHermesPowerShellSession(Process watcherProcess, String containerName, Path aiAgentPath, Path transcriptPath) {
        Task<HermesSessionCapture> task = new Task<>() {
            @Override
            protected HermesSessionCapture call() throws Exception {
                watcherProcess.waitFor();
                return parseHermesSessionTranscript(transcriptPath, containerName, aiAgentPath);
            }
        };
        task.setOnSucceeded(e -> promptToSaveHermesSession(task.getValue()));
        task.setOnFailed(e -> appendHermesLog("Could not capture Hermes session: " + exceptionMessage(rootCause(task.getException()))));
        start(task);
    }

    private HermesSessionCapture parseHermesSessionTranscript(Path transcriptPath, String containerName, Path aiAgentPath) {
        try {
            if (!Files.exists(transcriptPath)) {
                return HermesSessionCapture.empty(transcriptPath, containerName, aiAgentPath);
            }
            String transcript = Files.readString(transcriptPath, StandardCharsets.UTF_8);
            Matcher sessionMatcher = Pattern.compile("(?m)^\\s*Session:\\s*(\\S+)\\s*$").matcher(transcript);
            Matcher sessionIdMatcher = Pattern.compile("(?im)^\\s*session_id:\\s*(\\S+)\\s*$").matcher(transcript);
            Matcher titleMatcher = Pattern.compile("(?m)^\\s*Title:\\s*(.+?)\\s*$").matcher(transcript);
            Matcher resumeMatcher = Pattern.compile("(?m)^\\s*hermes\\s+--resume\\s+(\\S+)\\s*$").matcher(transcript);
            String sessionId = lastMatch(sessionMatcher, 1);
            String explicitSessionId = lastMatch(sessionIdMatcher, 1);
            String title = lastMatch(titleMatcher, 1);
            String resumeSessionId = lastMatch(resumeMatcher, 1);
            if (sessionId.isBlank()) {
                sessionId = explicitSessionId;
            }
            if (sessionId.isBlank()) {
                sessionId = resumeSessionId;
            }
            String resumeCommand = sessionId.isBlank() ? "" : "hermes --resume " + sessionId;
            return new HermesSessionCapture(sessionId, title, resumeCommand, transcriptPath.toString(), aiAgentPath.toString(),
                    containerName, transcript);
        } catch (Exception e) {
            appendHermesLog("Hermes transcript parse failed: " + exceptionMessage(e));
            return HermesSessionCapture.empty(transcriptPath, containerName, aiAgentPath);
        }
    }

    private String lastMatch(Matcher matcher, int group) {
        String value = "";
        while (matcher.find()) {
            value = nullToBlank(matcher.group(group)).trim();
        }
        return value;
    }

    private void promptToSaveHermesSession(HermesSessionCapture capture) {
        if (capture == null || nullToBlank(capture.sessionId()).isBlank()) {
            appendHermesLog("Hermes PowerShell session closed. No resume session id was found in the transcript.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.initOwner(stage);
        confirm.setTitle("Save Hermes Session");
        confirm.setHeaderText("Save this Hermes session to SQLite cache?");
        String title = firstNonBlank(capture.title(), "Hermes Session");
        confirm.setContentText("Session: " + capture.sessionId() + System.lineSeparator()
                + "Title: " + title + System.lineSeparator()
                + "Resume: " + capture.resumeCommand());
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            saveHermesSessionCapture(capture);
        } else {
            appendHermesLog("Hermes session was not saved: " + capture.sessionId());
        }
    }

    private void saveHermesSessionCapture(HermesSessionCapture capture) {
        try {
            String title = firstNonBlank(capture.title(), "Hermes Session");
            String sessionName = title + " (" + capture.sessionId() + ")";
            saveHermesSessionCaptureToSqlite(capture, sessionName, title);
            refreshHermesSessionOptions();
            if (hermesSessionBox != null) {
                String display = title + " (" + capture.sessionId() + ")";
                hermesSessionBox.setValue(display);
            }
            appendHermesLog("Saved Hermes session to SQLite cache: " + capture.sessionId());
            showInfo("Hermes Session Saved", "Saved session: " + title);
        } catch (Exception e) {
            showError("Save Hermes Session Failed", e);
        }
    }

    private void saveApiAiHermesSession(HermesSessionCapture capture) throws Exception {
        String title = firstNonBlank(capture.title(), "API AI Hermes Session");
        String sessionName = title + " (" + capture.sessionId() + ")";
        if (apiAiAgentMemoryStorageMode() == StorageMode.LOCAL) {
            saveHermesSessionCaptureToSqlite(capture, sessionName, title);
        } else {
            saveHermesSessionCaptureToFirebase(capture, sessionName, title);
        }
    }

    private void saveHermesSessionCaptureToSqlite(HermesSessionCapture capture, String sessionName, String title) throws Exception {
        Path sqliteDbPath = configCacheDatabasePathFromField();
        initializeHermesSessionTable(sqliteDbPath);
        String now = Instant.now().toString();
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + HERMES_SESSION_TABLE
                     + " (system_user_key, session_name, transcript, updated_at, session_id, title, resume_command, transcript_path, ai_agent_path, container_name, created_at) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                     + "ON CONFLICT(system_user_key, session_name) DO UPDATE SET "
                     + "transcript = excluded.transcript, updated_at = excluded.updated_at, session_id = excluded.session_id, "
                     + "title = excluded.title, resume_command = excluded.resume_command, transcript_path = excluded.transcript_path, "
                     + "ai_agent_path = excluded.ai_agent_path, container_name = excluded.container_name")) {
            statement.setString(1, configCacheKey());
            statement.setString(2, sessionName);
            statement.setString(3, capture.transcript());
            statement.setString(4, now);
            statement.setString(5, capture.sessionId());
            statement.setString(6, title);
            statement.setString(7, capture.resumeCommand());
            statement.setString(8, capture.transcriptPath());
            statement.setString(9, capture.aiAgentPath());
            statement.setString(10, capture.containerName());
            statement.setString(11, now);
            statement.executeUpdate();
        }
    }

    private void saveHermesSessionCaptureToFirebase(HermesSessionCapture capture, String sessionName, String title) throws Exception {
        JSONObject payload = hermesSessionJson(capture, sessionName, title);
        String id = URLEncoder.encode(capture.sessionId(), StandardCharsets.UTF_8).replace("+", "%20");
        HttpResponse<String> response = HttpClient.newHttpClient().send(apiAiFirebaseRequest(API_AI_HERMES_FIREBASE_PATH
                        + "/" + URLEncoder.encode(configCacheKey(), StandardCharsets.UTF_8).replace("+", "%20")
                        + "/" + id + ".json")
                        .PUT(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                        .header("Content-Type", "application/json")
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Firebase Hermes session save failed: HTTP " + response.statusCode() + " " + response.body());
        }
    }

    private JSONObject hermesSessionJson(HermesSessionCapture capture, String sessionName, String title) {
        return new JSONObject()
                .put("systemUserKey", configCacheKey())
                .put("sessionName", sessionName)
                .put("sessionId", capture.sessionId())
                .put("title", title)
                .put("resumeCommand", capture.resumeCommand())
                .put("transcriptPath", capture.transcriptPath())
                .put("aiAgentPath", capture.aiAgentPath())
                .put("containerName", capture.containerName())
                .put("dashboardUrl", activeApiAiHermesDashboardUrl)
                .put("transcript", capture.transcript())
                .put("updatedAt", Instant.now().toString());
    }

    private void refreshApiAiHermesSessionOptions() {
        if (apiAiHermesSessionBox == null) {
            return;
        }
        String selected = apiAiHermesSessionBox.getValue();
        List<String> sessions = new ArrayList<>();
        apiAiHermesSessionRecords.clear();
        sessions.add(HERMES_NEW_SESSION);
        if (activeApiAiHermesSession != null && !nullToBlank(activeApiAiHermesSession.sessionId()).isBlank()) {
            addApiAiHermesSessionOption(sessions, activeApiAiHermesSession);
        }
        try {
            if (apiAiAgentMemoryStorageMode() == StorageMode.LOCAL) {
                addLocalHermesSessionsToList(sessions);
            } else {
                addCloudHermesSessionsToList(sessions);
            }
        } catch (Exception e) {
            appendHermesLog("Could not refresh API AI Hermes sessions: " + exceptionMessage(e));
        }
        List<String> distinct = sessions.stream().filter(value -> value != null && !value.isBlank()).distinct().toList();
        apiAiHermesSessionBox.setItems(FXCollections.observableArrayList(distinct));
        apiAiHermesSessionBox.setValue(selected != null && distinct.contains(selected) ? selected
                : activeApiAiHermesSession == null ? HERMES_NEW_SESSION : hermesSessionDisplayName(activeApiAiHermesSession));
    }

    private void addLocalHermesSessionsToList(List<String> sessions) throws Exception {
        Path sqliteDbPath = configCacheDatabasePathFromField();
        if (!Files.exists(sqliteDbPath)) {
            return;
        }
        initializeHermesSessionTable(sqliteDbPath);
        try (Connection connection = openConfigCacheConnection(sqliteDbPath);
             PreparedStatement statement = connection.prepareStatement("SELECT session_name, session_id, title, resume_command, transcript_path, ai_agent_path, container_name FROM "
                     + HERMES_SESSION_TABLE + " WHERE system_user_key = ? ORDER BY updated_at DESC")) {
            statement.setString(1, configCacheKey());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    addApiAiHermesSessionOption(sessions, new HermesSessionRecord(
                            resultSet.getString("session_name"),
                            resultSet.getString("session_id"),
                            resultSet.getString("title"),
                            resultSet.getString("resume_command"),
                            resultSet.getString("transcript_path"),
                            resultSet.getString("ai_agent_path"),
                            resultSet.getString("container_name")));
                }
            }
        }
    }

    private void addCloudHermesSessionsToList(List<String> sessions) throws Exception {
        String key = URLEncoder.encode(configCacheKey(), StandardCharsets.UTF_8).replace("+", "%20");
        HttpResponse<String> response = HttpClient.newHttpClient().send(apiAiFirebaseRequest(API_AI_HERMES_FIREBASE_PATH + "/" + key + ".json")
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404 || response.body() == null || response.body().isBlank() || "null".equals(response.body())) {
            return;
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Firebase Hermes sessions load failed: HTTP " + response.statusCode() + " " + response.body());
        }
        JSONObject root = new JSONObject(response.body());
        for (String id : root.keySet()) {
            JSONObject item = root.optJSONObject(id);
            if (item == null) continue;
            addApiAiHermesSessionOption(sessions, new HermesSessionRecord(
                    item.optString("sessionName"),
                    item.optString("sessionId", id),
                    item.optString("title"),
                    item.optString("resumeCommand"),
                    item.optString("transcriptPath"),
                    item.optString("aiAgentPath"),
                    item.optString("containerName")));
        }
    }

    private void addApiAiHermesSessionOption(List<String> sessions, HermesSessionRecord record) {
        if (record == null || nullToBlank(record.sessionId()).isBlank()) {
            return;
        }
        String display = hermesSessionDisplayName(record);
        sessions.add(display);
        apiAiHermesSessionRecords.put(display, record);
    }

    private void selectApiAiHermesSession(String sessionId) {
        if (apiAiHermesSessionBox == null || sessionId == null || sessionId.isBlank()) {
            return;
        }
        for (String item : apiAiHermesSessionBox.getItems()) {
            if (item != null && item.contains(sessionId)) {
                apiAiHermesSessionBox.setValue(item);
                return;
            }
        }
    }

    private String psSingleQuoteContent(String value) {
        return value == null ? "" : value.replace("'", "''");
    }

    private String hermesDataDirectoryText() {
        try {
            return hermesDataDirectory().toString();
        } catch (Exception e) {
            return hermesAiAgentPathField == null ? "" : hermesAiAgentPathField.getText();
        }
    }

    private void openCodexCliWindow(String containerName) {
        Platform.runLater(() -> {
            if (codexCliStage == null) {
                codexCliStage = new Stage();
                codexCliStage.setTitle(APP_NAME + " - Codex CLI");
                loadApplicationIcon(codexCliStage);

                codexChatArea = editor("");
                codexChatArea.setEditable(false);
                codexChatArea.setWrapText(true);
                codexChatArea.setMinHeight(420);

                codexPromptArea = editor("");
                codexPromptArea.setPromptText("Ask Codex to generate test cases, heal Playwright scripts, map DB fields, review logs, or analyze this project.");
                codexPromptArea.setWrapText(true);
                codexPromptArea.setMinHeight(90);
                codexPromptArea.setPrefHeight(110);

                codexSendButton = primary("Send");
                codexSendButton.setOnAction(e -> sendCodexPrompt(containerName));
                codexCancelButton = secondary("Stop Response");
                codexCancelButton.setOnAction(e -> cancelCodexChatProcess());

                Label status = new Label("Connected to container: " + containerName);
                status.getStyleClass().add("muted");
                FlowPane actions = spacedActionRow(status, codexSendButton, codexCancelButton);
                VBox content = new VBox(12, card("Conversation", codexChatArea), card("Prompt", new VBox(10, codexPromptArea, actions)));
                content.setPadding(new Insets(14));
                VBox.setVgrow(content.getChildren().get(0), Priority.ALWAYS);

                Scene scene = new Scene(content, 960, 720);
                scene.getStylesheets().add(createInlineStylesheet());
                addApplicationStylesheet(scene);
                codexCliStage.setScene(scene);
                codexCliStage.setOnCloseRequest(e -> cancelCodexChatProcess());
            }
            codexCliStage.show();
            codexCliStage.toFront();
            if (codexChatArea != null && codexChatArea.getText().isBlank()) {
                appendCodexChat("Codex CLI is ready. Type a request below and click Send." + System.lineSeparator());
            }
        });
    }

    private void sendCodexPrompt(String containerName) {
        String prompt = codexPromptArea == null ? "" : codexPromptArea.getText().trim();
        if (prompt.isBlank()) {
            showWarning("Codex CLI", "Enter a prompt before sending.");
            return;
        }
        if (codexChatProcess != null && codexChatProcess.isAlive()) {
            showWarning("Codex CLI", "Codex is still responding. Stop the current response before sending another prompt.");
            return;
        }
        codexPromptArea.clear();
        appendCodexChat(System.lineSeparator() + "You:" + System.lineSeparator() + prompt + System.lineSeparator());
        appendCodexChat(System.lineSeparator() + "Codex:" + System.lineSeparator());
        setCodexChatRunning(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (!dockerContainerRunning(containerName)) {
                    throw new IllegalStateException("Codex CLI container is not running. Click Start Codex CLI first.");
                }
                List<String> command = codexExecCommand(containerName, translateHostWorkspacePathsForCodex(prompt));
                Process process = new ProcessBuilder(command)
                        .redirectErrorStream(true)
                        .start();
                codexChatProcess = process;
                try (java.io.Reader reader = new java.io.InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
                    char[] buffer = new char[1024];
                    int count;
                    while ((count = reader.read(buffer)) != -1) {
                        appendCodexChat(new String(buffer, 0, count));
                    }
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new IllegalStateException("Codex CLI exited with code " + exitCode + ".");
                }
                codexExecSessionStarted = true;
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            codexChatProcess = null;
            setCodexChatRunning(false);
            appendCodexChat(System.lineSeparator());
        });
        task.setOnFailed(e -> {
            codexChatProcess = null;
            setCodexChatRunning(false);
            appendCodexChat(System.lineSeparator() + "Codex CLI failed: " + exceptionMessage(task.getException()) + System.lineSeparator());
            appendCodexLog("Codex CLI chat failed: " + exceptionMessage(task.getException()));
        });
        start(task);
    }

    private List<String> codexExecCommand(String containerName, String prompt) {
        List<String> command = new ArrayList<>();
        command.add("docker");
        command.add("exec");
        command.add("-w");
        command.add("/workspace");
        command.add(containerName);
        command.add("codex");
        command.add("exec");
        command.add("--skip-git-repo-check");
        command.add("--sandbox");
        command.add("workspace-write");
        command.add("--color");
        command.add("never");
        if (codexExecSessionStarted) {
            command.add("resume");
            command.add("--last");
            command.add(prompt);
        } else {
            command.add(prompt);
        }
        return command;
    }

    private String translateHostWorkspacePathsForCodex(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "";
        }
        Path workspaceDirectory = Path.of("").toAbsolutePath().normalize();
        String workspacePath = workspaceDirectory.toString();
        String workspaceForwardPath = workspacePath.replace('\\', '/');
        String result = prompt.replace('\\', '/');
        for (String hostPath : List.of(workspaceForwardPath, workspacePath.replace('/', '\\'))) {
            if (hostPath == null || hostPath.isBlank()) {
                continue;
            }
            String normalizedHostPath = hostPath.replace('\\', '/');
            result = Pattern.compile(Pattern.quote(normalizedHostPath), Pattern.CASE_INSENSITIVE)
                    .matcher(result)
                    .replaceAll(Matcher.quoteReplacement("/workspace"));
        }
        return result;
    }

    private void cancelCodexChatProcess() {
        Process process = codexChatProcess;
        if (process != null && process.isAlive()) {
            process.destroy();
            appendCodexChat(System.lineSeparator() + "Codex response stopped." + System.lineSeparator());
            appendCodexLog("Codex CLI response stopped.");
        }
        codexChatProcess = null;
        setCodexChatRunning(false);
    }

    private void appendCodexChat(String text) {
        if (codexChatArea == null || text == null || text.isEmpty()) {
            return;
        }
        Platform.runLater(() -> codexChatArea.appendText(text));
    }

    private void setCodexChatRunning(boolean running) {
        Platform.runLater(() -> {
            if (codexSendButton != null) {
                codexSendButton.setDisable(running);
            }
            if (codexCancelButton != null) {
                codexCancelButton.setDisable(!running);
            }
        });
    }

    private ProcessResult runDockerCommand(List<String> dockerArgs) throws Exception {
        List<String> command = new ArrayList<>();
        command.add("docker");
        command.addAll(dockerArgs);
        ProcessResult result = runCommand(command);
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
        return result;
    }

    private void runDockerCommandWithLogs(List<String> dockerArgs) throws Exception {
        runDockerCommandWithLogs(dockerArgs, this::appendCodexProcessLine);
    }

    private void runDockerCommandWithLogs(List<String> dockerArgs, java.util.function.Consumer<String> lineConsumer) throws Exception {
        List<String> command = new ArrayList<>();
        command.add("docker");
        command.addAll(dockerArgs);
        ProcessResult result = runCommandWithLogStreaming(command, lineConsumer);
        if (!result.success()) {
            throw new IllegalStateException(result.output.trim());
        }
    }

    private ProcessResult runCommand(List<String> command) throws Exception {
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            return new ProcessResult(exitCode, output);
        } catch (java.io.IOException e) {
            return new ProcessResult(127, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        }
    }

    private ProcessResult runCommandWithLogStreaming(List<String> command) throws Exception {
        return runCommandWithLogStreaming(command, this::appendCodexProcessLine);
    }

    private ProcessResult runCommandWithLogStreaming(List<String> command, java.util.function.Consumer<String> lineConsumer) throws Exception {
        StringBuilder output = new StringBuilder();
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            StringBuilder chunk = new StringBuilder();
            try (java.io.Reader reader = new java.io.InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
                int value;
                while ((value = reader.read()) != -1) {
                    char character = (char) value;
                    output.append(character);
                    if (character == '\n' || character == '\r') {
                        lineConsumer.accept(chunk.toString());
                        chunk.setLength(0);
                    } else {
                        chunk.append(character);
                    }
                }
            }
            if (!chunk.isEmpty()) {
                lineConsumer.accept(chunk.toString());
            }
            int exitCode = process.waitFor();
            return new ProcessResult(exitCode, output.toString());
        } catch (java.io.IOException e) {
            return new ProcessResult(127, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        }
    }

    private void appendCodexProcessLine(String line) {
        String text = line == null ? "" : line.trim();
        if (!text.isBlank()) {
            appendCodexLog(text);
        }
    }

    private void appendHermesProcessLine(String line) {
        String text = line == null ? "" : line.trim();
        if (!text.isBlank()) {
            appendHermesLog(text);
        }
    }

    private String powershellQuote(String value) {
        return "'" + (value == null ? "" : value.replace("'", "''")) + "'";
    }

    private String firstOutputLine(String output) {
        return output == null ? "" : output.lines()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .findFirst()
                .orElse("");
    }

    private String exceptionMessage(Throwable throwable) {
        if (throwable == null) {
            return "Unknown error";
        }
        return throwable.getMessage() == null ? throwable.getClass().getSimpleName() : throwable.getMessage();
    }

    private void logApiAiConsole(String message) {
        System.err.println("[API AI Analysis] " + Instant.now() + " - " + message);
    }

    private void logApiAiConsole(String message, Throwable throwable) {
        logApiAiConsole(message);
        if (throwable != null) {
            throwable.printStackTrace(System.err);
        }
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current instanceof InvocationTargetException invocation && invocation.getTargetException() != null) {
            current = invocation.getTargetException();
        }
        while (current != null && current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current == null ? throwable : current;
    }

    private void runWebTest() {
        WebTestCase testCase = buildWebTestCase();
        if (testCase.steps.isEmpty()) {
            showWarning("Web Testing", "Record, load, or add at least one step before running.");
            return;
        }
        Task<WebTestRunReport> task = new Task<>() {
            @Override
            protected WebTestRunReport call() throws Exception {
                return playwrightRecorderController.runTest(testCase, webHeadlessCheck.isSelected(), webSlowMoCheck.isSelected() ? 250 : 0);
            }
        };
        task.setOnSucceeded(e -> renderWebReport(task.getValue()));
        task.setOnFailed(e -> showError("Web Test Failed", task.getException()));
        start(task);
    }

    private WebTestCase buildWebTestCase() {
        WebTestCase testCase = new WebTestCase();
        testCase.testName = webTestNameField.getText();
        testCase.startUrl = resolveVariables(webStartUrlField.getText());
        for (Map<String, String> row : webStepRows) {
            WebTestStep step = new WebTestStep();
            step.action = row.get("action");
            if ("Flow Variable".equalsIgnoreCase(step.action)) {
                String variableName = flowVariableName(row);
                String value = resolveVariables(row.get("value"));
                if (!variableName.isBlank()) {
                    savedVariables.put(variableName, value);
                    savedVariablePaths.put(variableName, "web-flow:" + row.getOrDefault("step", ""));
                    savedVariableTypes.put(variableName, "Web Flow Variable");
                }
                step.selector = "";
                step.value = value;
                step.note = variableName;
            } else {
                step.selector = resolveVariables(row.get("selector"));
                step.value = resolveVariables(row.get("value"));
                step.note = resolveVariables(row.get("note"));
            }
            testCase.steps.add(step);
        }
        return testCase;
    }

    private String flowVariableName(Map<String, String> row) {
        String explicit = row == null ? "" : row.getOrDefault("flowVariableName", "");
        if (!explicit.isBlank()) {
            return normalizeVariableName(explicit);
        }
        String selector = row == null ? "" : row.getOrDefault("selector", "");
        if ("Flow Variable".equalsIgnoreCase(row == null ? "" : row.getOrDefault("action", "")) && !selector.isBlank()) {
            return normalizeVariableName(selector);
        }
        String note = row == null ? "" : row.getOrDefault("note", "");
        String value = row == null ? "" : row.getOrDefault("value", "");
        String candidate = firstNonBlank(note, value);
        if (candidate.startsWith("${") && candidate.endsWith("}")) {
            candidate = candidate.substring(2, candidate.length() - 1);
        }
        return normalizeVariableName(candidate);
    }

    private void registerFlowVariableFromRow(Map<String, String> row) {
        if (row == null || !"Flow Variable".equalsIgnoreCase(row.getOrDefault("action", ""))) {
            return;
        }
        String name = flowVariableName(row);
        if (name.isBlank()) {
            return;
        }
        row.put("flowVariableName", name);
        row.put("selector", name);
        savedVariables.put(name, resolveVariables(row.getOrDefault("value", "")));
        savedVariablePaths.put(name, "web-flow:" + row.getOrDefault("step", ""));
        savedVariableTypes.put(name, "Web Flow Variable");
        refreshVariablesView();
    }

    private void renderWebReport(WebTestRunReport report) {
        webResultRows.clear();
        int index = 0;
        for (WebTestExecutionResult result : report.results) {
            webResultRows.add(row("stepIndex", String.valueOf(index++),
                    "result", result.passed ? "Pass" : "Fail", "action", result.action,
                    "selector", result.selector, "expected", result.expectedValue, "message", result.message,
                    "duration", result.durationMs + " ms"));
            if (result.capturedVariableName != null && !result.capturedVariableName.isBlank()) {
                savedVariables.put(result.capturedVariableName, nullToBlank(result.capturedVariableValue));
                savedVariablePaths.put(result.capturedVariableName, "web:" + result.action);
                savedVariableTypes.put(result.capturedVariableName, "Web Test");
            }
        }
        refreshVariablesView();
        webRunSummaryLabel.setText(report.passed + " passed / " + report.failed + " failed");
        writeStandaloneValidationReport("WEB_TESTING", "Web Testing", "Web Test",
                localWebTestingReportsDirectory(), webResultRows,
                "action", "expected", "selector", "duration", "result", "message");
    }

    private void runWebAiAnalysisForFailures() {
        if (activeApiAiHermesSession == null || nullToBlank(activeApiAiHermesSession.sessionId()).isBlank()) {
            showWarning("Hermes Agent", "Connect a Hermes session in Config before running AI Analysis.");
            updateApiAiConnectionLabels();
            return;
        }
        List<Map<String, String>> failures = webFailureRows();
        if (failures.isEmpty()) {
            showWarning("Web AI Analysis", "Run the web test first and make sure Step Results contains at least one failure.");
            return;
        }
        webRunSummaryLabel.setText("Hermes Agent healing failed steps...");
        updateApiAiConnectionLabels("Hermes Agent analyzing WebUI failures with " + apiAiConnectedModel);
        Task<WebAiSuggestion> task = new Task<>() {
            @Override protected WebAiSuggestion call() {
                return requestWebAiModelSuggestion(failures);
            }
        };
        task.setOnSucceeded(e -> {
            webRunSummaryLabel.setText("Web AI suggestions ready");
            updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel);
            WebAiSuggestion suggestion = task.getValue();
            if (suggestion.fixes.isEmpty()) {
                showWarning("Web AI Analysis", "The connected model did not return any recommended fixes.");
            } else {
                showWebAiSuggestionWindow(suggestion);
            }
        });
        task.setOnFailed(e -> {
            logApiAiConsole("Web AI Analysis task failed", task.getException());
            webRunSummaryLabel.setText("Web AI suggestion failed");
            updateApiAiConnectionLabels("Hermes Agent connected: " + apiAiConnectedModel
                    + " (last Web analysis failed: " + exceptionMessage(rootCause(task.getException())) + ")");
            showError("Web AI Analysis Failed", task.getException());
        });
        start(task);
    }

    private List<Map<String, String>> webFailureRows() {
        if (webResultRows == null) {
            return List.of();
        }
        return webResultRows.stream()
                .filter(row -> !isPassingStatus(row.getOrDefault("result", "")))
                .toList();
    }

    private WebAiSuggestion requestWebAiModelSuggestion(List<Map<String, String>> failures) {
        try {
            String prompt = webAiSuggestionPrompt(failures);
            logApiAiConsole("Web AI Analysis prompt built, characters=" + prompt.length());
            String output = runHermesApiAiPrompt(prompt);
            logApiAiConsole("Web AI Analysis model output received, characters=" + (output == null ? 0 : output.length()));
            JSONObject root = extractJsonObject(output);
            return webAiSuggestionFromModelJson(root, failures);
        } catch (Exception e) {
            logApiAiConsole("Web AI Analysis failed while preparing, running, or parsing model suggestion", e);
            throw new RuntimeException(e);
        }
    }

    private String webAiSuggestionPrompt(List<Map<String, String>> failures) throws Exception {
        JSONObject input = new JSONObject()
                .put("testName", webTestNameField == null ? "" : webTestNameField.getText())
                .put("startUrl", webStartUrlField == null ? "" : webStartUrlField.getText())
                .put("capturedSteps", webStepsJson())
                .put("failedResults", new JSONArray(failures))
                .put("availableSavedVariables", savedVariablesJson())
                .put("supportedActions", new JSONArray(WEB_TEST_ACTIONS))
                .put("supportedRuntimeVariables", new JSONArray(RUNTIME_VARIABLES))
                .put("knowledgeRepositoryMode", apiAiAgentMemoryStorageMode().name())
                .put("knowledgeRepository", loadApiAiMemoryRepositorySample());
        return """
                You are TestWeave WebUI Healer.
                Analyze failed WebUI Step Results and suggest concrete repairs for the Captured Steps.
                Return ONLY strict JSON. No markdown, no prose.

                Required JSON shape:
                {
                  "fixes": [
                    {
                      "stepIndex": 0,
                      "cause":"Selector no longer matches the login button.",
                      "recommendedFix":"Use a stable text locator.",
                      "action":"Click",
                      "selector":"button:has-text(\\"Login\\")",
                      "value":"",
                      "note":"Healed selector after failed click",
                      "flowVariableName":""
                    }
                  ]
                }

                Rules:
                - stepIndex is zero-based and must point to the Captured Steps row that should be changed.
                - Return the full replacement action, selector, value, and note for each fix.
                - Prefer stable selectors: data-testid, id, name, aria-label, role/text, then CSS.
                - Use supported runtime variables when helpful: ${randomString}, ${randomInt}, ${randomDate}.
                - Runtime variables may be used in selector, value, or note when the failure is caused by duplicate/static data.
                - Use saved variables as ${name} when a captured value should be reused.
                - Flow Variable stores Value into flowVariableName. Its selector must be blank.
                - For Get Text, Value is the variable name where actual page text is stored.
                - For Validate Text or any failure message showing actual text differs from expected, include a fix that updates Value/expected to the actual text or a variable such as ${capturedName}; explain that the webpage actual was updated and expected should be updated.
                - Only use actions from supportedActions.
                - Keep fixes minimal. Do not propose unrelated steps.

                Input:
                """ + input.toString(2);
    }

    private JSONArray webStepsJson() {
        JSONArray steps = new JSONArray();
        if (webStepRows == null) {
            return steps;
        }
        for (int i = 0; i < webStepRows.size(); i++) {
            steps.put(new JSONObject(webStepRows.get(i)).put("stepIndex", i));
        }
        return steps;
    }

    private JSONObject savedVariablesJson() {
        JSONObject variables = new JSONObject();
        for (Map.Entry<String, String> entry : savedVariables.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
        }
        return variables;
    }

    private WebAiSuggestion webAiSuggestionFromModelJson(JSONObject root, List<Map<String, String>> failures) {
        WebAiSuggestion suggestion = new WebAiSuggestion();
        suggestion.failures.addAll(failures);
        JSONArray fixes = root.optJSONArray("fixes");
        if (fixes != null) for (int i = 0; i < fixes.length(); i++) {
            JSONObject item = fixes.optJSONObject(i);
            if (item == null) continue;
            int stepIndex = item.optInt("stepIndex", -1);
            if (stepIndex < 0 || stepIndex >= webStepRows.size()) {
                continue;
            }
            Map<String, String> current = webStepRows.get(stepIndex);
            suggestion.fixes.add(row("selected", "true",
                    "stepIndex", String.valueOf(stepIndex),
                    "step", String.valueOf(stepIndex + 1),
                    "cause", item.optString("cause"),
                    "recommendedFix", item.optString("recommendedFix"),
                    "action", firstNonBlank(item.optString("action"), current.getOrDefault("action", "")),
                    "selector", firstNonBlank(item.optString("selector"), current.getOrDefault("selector", "")),
                    "value", item.has("value") ? item.optString("value") : current.getOrDefault("value", ""),
                    "note", firstNonBlank(item.optString("note"), current.getOrDefault("note", "")),
                    "flowVariableName", firstNonBlank(item.optString("flowVariableName"), current.getOrDefault("flowVariableName", ""))));
        }
        return suggestion;
    }

    private void showWebAiSuggestionWindow(WebAiSuggestion suggestion) {
        ObservableList<Map<String, String>> fixRows = FXCollections.observableArrayList(suggestion.fixes);
        TableView<Map<String, String>> fixesTable = mapTable(fixRows,
                "Apply", "selected", "Step", "step", "Cause", "cause", "Recommended Fix", "recommendedFix",
                "Action", "action", "Selector", "selector", "Value", "value",
                "Flow Variable Name", "flowVariableName", "Note", "note");
        fixesTable.setPrefHeight(430);
        fixesTable.setMinHeight(260);
        Button toggle = secondary("Toggle");
        Button saveKnowledge = primary("Save to Project Knowledge store");
        Button applyFix = primary("Apply Fix");
        Button close = secondary("Close");
        Stage suggestionStage = new Stage();
        if (stage != null) {
            suggestionStage.initOwner(stage);
        }
        toggle.setOnAction(e -> {
            boolean selected = fixRows.stream().anyMatch(row -> !isSelected(row));
            setAllRowsSelected(fixRows, fixesTable, selected);
        });
        saveKnowledge.setOnAction(e -> saveWebAiSuggestionMemory(suggestion, fixRows));
        applyFix.setOnAction(e -> applyWebAiFixes(fixRows));
        close.setOnAction(e -> suggestionStage.close());
        VBox content = new VBox(14,
                sectionTitle("WebUI Healer Suggestions"),
                new Label("Hermes Agent analyzed failed Step Results and suggested repairs for Captured Steps."),
                card("Recommended Fixes", fixesTable));
        content.setPadding(new Insets(16));
        VBox.setVgrow(fixesTable, Priority.ALWAYS);
        ScrollPane scroller = new ScrollPane(content);
        scroller.setFitToWidth(true);
        scroller.setPannable(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FlowPane footer = actionRow(toggle, saveKnowledge, applyFix, close);
        footer.setPadding(new Insets(12, 16, 16, 16));
        footer.getStyleClass().add("card");
        BorderPane shell = new BorderPane(scroller);
        shell.setBottom(footer);
        double[] suggestionSize = apiAiSuggestionWindowSize();
        Scene scene = new Scene(shell, suggestionSize[0], suggestionSize[1]);
        scene.getStylesheets().add(createInlineStylesheet());
        addApplicationStylesheet(scene);
        suggestionStage.setTitle("AI Suggestion - WebUI Healer");
        suggestionStage.setScene(scene);
        suggestionStage.setMinWidth(Math.min(820, suggestionSize[0]));
        suggestionStage.setMinHeight(Math.min(560, suggestionSize[1]));
        suggestionStage.show();
    }

    private void applyWebAiFixes(List<Map<String, String>> fixes) {
        int applied = 0;
        for (Map<String, String> fix : fixes) {
            if (!isSelected(fix)) continue;
            int index;
            try {
                index = Integer.parseInt(fix.getOrDefault("stepIndex", "-1"));
            } catch (NumberFormatException e) {
                continue;
            }
            if (index < 0 || index >= webStepRows.size()) {
                continue;
            }
            Map<String, String> target = webStepRows.get(index);
            target.put("action", fix.getOrDefault("action", target.getOrDefault("action", "")));
            target.put("selector", fix.getOrDefault("selector", target.getOrDefault("selector", "")));
            target.put("value", fix.getOrDefault("value", target.getOrDefault("value", "")));
            target.put("note", fix.getOrDefault("note", target.getOrDefault("note", "")));
            target.put("flowVariableName", fix.getOrDefault("flowVariableName", target.getOrDefault("flowVariableName", "")));
            registerFlowVariableFromRow(target);
            applied++;
        }
        renumberWebSteps();
        webStepsTable.refresh();
        showInfo("WebUI Healer", "Applied " + applied + " selected fix(es) to Captured Steps.");
    }

    private void saveWebAiSuggestionMemory(WebAiSuggestion suggestion, List<Map<String, String>> fixes) {
        JSONObject memory = new JSONObject()
                .put("id", "web-ai-healer-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                .put("endpoint", webStartUrlField == null ? "" : webStartUrlField.getText())
                .put("method", "WEB_TEST")
                .put("actionName", "webUiHealing")
                .put("hermesSessionId", activeApiAiHermesSession == null ? "" : activeApiAiHermesSession.sessionId())
                .put("provider", activeApiAiHermesSession == null ? "Codex" : "Hermes")
                .put("response", new JSONObject()
                        .put("status", "WebUI Healer Suggestions")
                        .put("body", new JSONObject()
                                .put("testName", webTestNameField == null ? "" : webTestNameField.getText())
                                .put("startUrl", webStartUrlField == null ? "" : webStartUrlField.getText())
                                .put("capturedSteps", webStepsJson())
                                .put("failedResults", new JSONArray(suggestion.failures))
                                .put("fixes", new JSONArray(fixes))
                                .put("runtimeVariables", new JSONArray(RUNTIME_VARIABLES))
                                .toString()))
                .put("variables", new JSONArray())
                .put("validations", new JSONArray(fixes))
                .put("dbMappings", new JSONArray())
                .put("createdAt", Instant.now().toString());
        saveApiAiMemory(memory);
        showInfo("WebUI Healer", "Saved WebUI healing suggestions to project knowledge.");
    }

    private void saveWebRecording() {
        JSONArray steps = new JSONArray();
        for (Map<String, String> row : webStepRows) {
            JSONObject item = new JSONObject(row);
            if ("Flow Variable".equalsIgnoreCase(row.getOrDefault("action", ""))) {
                item.put("flowVariableName", flowVariableName(row));
            }
            steps.put(item);
        }
        JSONObject root = new JSONObject();
        root.put("testName", webTestNameField.getText());
        root.put("startUrl", webStartUrlField.getText());
        root.put("steps", steps);
        saveTextFile(root.toString(2), "web-recording.json", configuredFolder("WebUI", "Recording"));
    }

    private void loadWebRecording() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("WebUI", "Recording"));
        if (file == null) {
            return;
        }
        loadWebRecordingFile(file, false);
    }

    private void loadWebRecordingFile(File file, boolean merge) {
        try {
            JSONObject root = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            webTestNameField.setText(root.optString("testName", "Web Test"));
            webStartUrlField.setText(root.optString("startUrl", ""));
            if (!merge) {
                webStepRows.clear();
            }
            JSONArray steps = root.optJSONArray("steps");
            if (steps != null) {
                for (int i = 0; i < steps.length(); i++) {
                    JSONObject item = steps.getJSONObject(i);
                    webStepRows.add(row("step", String.valueOf(webStepRows.size() + 1), "action", item.optString("action"),
                            "selector", item.optString("selector"), "value", item.optString("value"), "note", item.optString("note"),
                            "flowVariableName", item.optString("flowVariableName")));
                    registerFlowVariableFromRow(webStepRows.get(webStepRows.size() - 1));
                }
            }
        } catch (Exception e) {
            showError("Load Recording Failed", e);
        }
    }

    private void saveSelectedResponseVariables() {
        int count = 0;
        for (Map<String, String> row : responseFieldRows) {
            if (isSelected(row)) {
                String name = normalizeVariableName(row.getOrDefault("variableName", row.get("field")));
                savedVariables.put(name, row.get("value"));
                savedVariablePaths.put(name, row.get("jsonPath"));
                savedVariableTypes.put(name, row.get("type"));
                count++;
            }
        }
        refreshVariablesView();
        showInfo("Variables", count + " variable(s) saved.");
    }

    private void createVariableDialog() {
        TextInputDialog dialog = new TextInputDialog("name=value");
        dialog.setTitle("Create Variable");
        dialog.setHeaderText("Enter name=value");
        dialog.showAndWait().ifPresent(text -> {
            int equals = text.indexOf('=');
            if (equals <= 0) {
                showWarning("Create Variable", "Use the format name=value.");
                return;
            }
            String name = normalizeVariableName(text.substring(0, equals));
            savedVariables.put(name, text.substring(equals + 1));
            savedVariableTypes.put(name, "Manual");
            refreshVariablesView();
        });
    }

    private void removeSelectedVariables() {
        for (Map<String, String> row : new ArrayList<>(variablesTable.getSelectionModel().getSelectedItems())) {
            savedVariables.remove(row.get("name"));
            savedVariablePaths.remove(row.get("name"));
            savedVariableTypes.remove(row.get("name"));
        }
        refreshVariablesView();
    }

    private void refreshVariablesView() {
        if (variableRows != null) {
            variableRows.clear();
            savedVariables.keySet().stream().sorted().forEach(name ->
                    variableRows.add(row("name", name, "value", savedVariables.get(name),
                            "type", savedVariableTypes.getOrDefault(name, "Manual"), "path", savedVariablePaths.getOrDefault(name, ""))));
        }
        refreshVariableDropdowns();
    }

    private void saveVariablesToFile() {
        JSONArray array = new JSONArray();
        for (String name : savedVariables.keySet()) {
            JSONObject item = new JSONObject();
            item.put("name", name);
            item.put("value", savedVariables.get(name));
            item.put("type", savedVariableTypes.getOrDefault(name, "Manual"));
            item.put("path", savedVariablePaths.getOrDefault(name, ""));
            array.put(item);
        }
        saveTextFile(array.toString(2), "api-validator-variables.json", configuredFolder("Variables"));
    }

    private String importedVariableName(JSONObject item) {
        return item.optString("name").replace("${", "").replace("}", "").trim();
    }

    private void importVariablesFromFile() {
        File file = chooseOpenFile("JSON Files", "*.json", configuredFolder("Variables"));
        if (file == null) {
            return;
        }
        try {
            if (variablesPathField != null) {
                variablesPathField.setText(file.getAbsolutePath());
            }
            Object parsed = new JSONTokener(Files.readString(file.toPath(), StandardCharsets.UTF_8)).nextValue();
            JSONArray array;
            if (parsed instanceof JSONArray parsedArray) {
                array = parsedArray;
            } else if (parsed instanceof JSONObject parsedObject && parsedObject.has("variables")) {
                array = parsedObject.optJSONArray("variables");
            } else {
                array = new JSONArray().put(parsed);
            }
            if (array == null) {
                showWarning("Import Variables", "No variables array was found in the selected JSON file.");
                return;
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String name = importedVariableName(item);
                if (name.isBlank()) {
                    continue;
                }
                savedVariables.put(name, item.optString("value"));
                savedVariableTypes.put(name, item.optString("type", "Imported"));
                savedVariablePaths.put(name, item.optString("path", item.optString("jsonPath", "")));
            }
            refreshVariablesView();
        } catch (Exception e) {
            showError("Import Variables Failed", e);
        }
    }

    private ComboBox<String> createVariableDropdown() {
        ComboBox<String> box = new ComboBox<>();
        box.setPromptText("Variables");
        variableDropdowns.add(box);
        refreshVariableDropdowns();
        return box;
    }

    private void refreshVariableDropdowns() {
        List<String> values = new ArrayList<>();
        for (String runtimeVariable : RUNTIME_VARIABLES) {
            values.add("${" + runtimeVariable + "}");
        }
        values.addAll(savedVariables.keySet().stream().sorted().map(name -> "${" + name + "}").toList());
        for (ComboBox<String> box : variableDropdowns) {
            box.setItems(FXCollections.observableArrayList(values));
        }
    }

    private void insertVariable(TextArea target, ComboBox<String> dropdown) {
        if (dropdown.getValue() != null) {
            target.insertText(target.getCaretPosition(), dropdown.getValue());
        }
    }

    private void insertVariable(TextField target, ComboBox<String> dropdown) {
        if (dropdown.getValue() != null) {
            target.insertText(target.getCaretPosition(), dropdown.getValue());
        }
    }

    private void beautifyBody() {
        try {
            bodyArea.setText(apiService.prettyPrintJson(bodyArea.getText()));
        } catch (Exception e) {
            showWarning("Beautify Failed", "Request body is not valid JSON.");
        }
    }

    private void clearApiForm() {
        currentPostmanRequestNode = null;
        currentPostmanBodyMode = "";
        currentPostmanMultipartParts = new ArrayList<>();
        currentPostmanBinaryFilePath = "";
        endpointField.clear();
        tokenField.clear();
        visibleTokenField.clear();
        headersArea.clear();
        bodyArea.clear();
        if (preRequestScriptArea != null) {
            preRequestScriptArea.clear();
        }
        if (testScriptArea != null) {
            testScriptArea.clear();
        }
        prettyResponseArea.clear();
        rawResponseArea.clear();
        responseHeadersArea.clear();
        responseCookiesArea.clear();
        statusValueLabel.setText("--");
        timeValueLabel.setText("--");
        sizeValueLabel.setText("--");
        responseFieldRows.clear();
    }

    private void saveRequest() {
        JSONObject root = savedApiRequestContext();
        File file = chooseSaveFile("JSON Files", "*.json", "request.json", configuredFolder("API", "SavedRequest"));
        if (file == null) {
            return;
        }
        try {
            Files.writeString(file.toPath(), root.toString(2), StandardCharsets.UTF_8);
            showInfo("Save Request", "Saved API request to " + file.toPath().toAbsolutePath().normalize());
        } catch (Exception e) {
            showError("Save Request Failed", e);
        }
    }

    private void saveResponse() {
        if (lastResponse == null) {
            showWarning("Save Response", "No response is available to save.");
            return;
        }
        File file = chooseSaveFile("JSON Files", "*.json", "response.json", configuredFolder("API", "SavedResponse"));
        if (file == null) {
            return;
        }
        try {
            Files.writeString(file.toPath(), nullToBlank(lastResponse.rawBody), StandardCharsets.UTF_8);
            JSONObject memory = savedApiResponseMemory(file.toPath());
            saveApiAiMemory(memory);
            showInfo("Save Response", "Saved API response to " + file.toPath().toAbsolutePath().normalize()
                    + " and queued project knowledge memory.");
        } catch (Exception e) {
            showError("Save Response Failed", e);
        }
    }

    private JSONObject savedApiRequestContext() {
        String url = endpointField == null ? "" : endpointField.getText();
        return new JSONObject()
                .put("url", url)
                .put("method", methodBox == null ? "" : methodBox.getValue())
                .put("headers", new JSONObject(parseHeaders(headersArea == null ? "" : headersArea.getText())))
                .put("body", bodyArea == null ? "" : bodyArea.getText())
                .put("authType", authTypeBox == null ? "" : authTypeBox.getValue())
                .put("oauth2", new JSONObject()
                        .put("grantType", oauthGrantTypeBox == null ? "" : oauthGrantTypeBox.getValue())
                        .put("tokenUrl", oauthTokenUrlField == null ? "" : oauthTokenUrlField.getText())
                        .put("clientId", oauthClientIdField == null ? "" : oauthClientIdField.getText())
                        .put("scope", oauthScopeField == null ? "" : oauthScopeField.getText()))
                .put("transport", new JSONObject()
                        .put("sslVerificationDisabled", sslVerificationDisabledCheck != null && sslVerificationDisabledCheck.isSelected())
                        .put("trustStorePath", trustStorePathField == null ? "" : trustStorePathField.getText())
                        .put("keyStorePath", keyStorePathField == null ? "" : keyStorePathField.getText())
                        .put("proxyEnabled", proxyEnabledCheck != null && proxyEnabledCheck.isSelected())
                        .put("proxyScheme", proxySchemeBox == null ? "" : proxySchemeBox.getValue())
                        .put("proxyHost", proxyHostField == null ? "" : proxyHostField.getText())
                        .put("proxyPort", proxyPortField == null ? "" : proxyPortField.getText()))
                .put("resolvedUrl", endpointField == null ? "" : resolveVariables(endpointField.getText()))
                .put("queryParams", queryParamsJson(url))
                .put("savedAt", Instant.now().toString());
    }

    private JSONObject savedApiResponseMemory(Path savedResponsePath) {
        JSONObject request = savedApiRequestContext();
        return new JSONObject()
                .put("id", "saved-response-" + System.currentTimeMillis() + "-" + UUID.randomUUID())
                .put("endpoint", request.optString("url"))
                .put("method", request.optString("method"))
                .put("actionName", "savedApiResponse")
                .put("hermesSessionId", activeApiAiHermesSession == null ? "" : activeApiAiHermesSession.sessionId())
                .put("provider", "TestWeave")
                .put("response", new JSONObject()
                        .put("status", lastResponse == null ? "" : lastResponse.statusLine)
                        .put("statusCode", lastResponse == null ? 0 : lastResponse.statusCode)
                        .put("headers", lastResponse == null ? "" : nullToBlank(lastResponse.headersText))
                        .put("cookies", lastResponse == null ? "" : nullToBlank(lastResponse.cookiesText))
                        .put("body", lastResponse == null ? "" : nullToBlank(lastResponse.rawBody))
                        .put("prettyBody", lastResponse == null ? "" : nullToBlank(lastResponse.prettyBody))
                        .put("savedResponsePath", savedResponsePath.toAbsolutePath().normalize().toString())
                        .put("savedResponseName", savedResponsePath.getFileName() == null ? "" : savedResponsePath.getFileName().toString())
                        .put("request", request))
                .put("variables", new JSONArray())
                .put("validations", new JSONArray())
                .put("dbMappings", new JSONArray())
                .put("createdAt", Instant.now().toString());
    }

    private JSONObject queryParamsJson(String url) {
        JSONObject params = new JSONObject();
        try {
            String query = URI.create(resolveVariables(nullToBlank(url))).getRawQuery();
            if (query == null || query.isBlank()) {
                return params;
            }
            for (String part : query.split("&")) {
                if (part.isBlank()) continue;
                int equals = part.indexOf('=');
                String key = equals >= 0 ? part.substring(0, equals) : part;
                String value = equals >= 0 ? part.substring(equals + 1) : "";
                params.put(java.net.URLDecoder.decode(key, StandardCharsets.UTF_8),
                        java.net.URLDecoder.decode(value, StandardCharsets.UTF_8));
            }
        } catch (Exception ignored) {
            // Keep request context saving resilient even for templated URLs.
        }
        return params;
    }

    private Map<String, String> parseHeaders(String text) {
        Map<String, String> headers = new LinkedHashMap<>();
        if (text == null || text.isBlank()) {
            return headers;
        }
        for (String line : text.split("\\R")) {
            int colon = line.indexOf(':');
            if (colon > 0) {
                headers.put(line.substring(0, colon).trim(), line.substring(colon + 1).trim());
            }
        }
        return headers;
    }

    private Map<String, String> resolveHeaderVariables(Map<String, String> headers) {
        Map<String, String> resolvedHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            resolvedHeaders.put(resolveVariables(entry.getKey()), resolveVariables(entry.getValue()));
        }
        return resolvedHeaders;
    }

    private String resolveVariables(String text) {
        if (text == null) {
            return "";
        }
        String resolved = text;
        resolved = resolved.replace("${randomString}", randomString());
        resolved = resolved.replace("${randomInt}", String.valueOf(ThreadLocalRandom.current().nextInt(10000, 999999)));
        resolved = resolved.replace("${randomDate}", LocalDate.now().toString());
        resolved = resolved.replace("${$guid}", UUID.randomUUID().toString());
        resolved = resolved.replace("${$timestamp}", String.valueOf(Instant.now().getEpochSecond()));
        resolved = resolved.replace("${$randomInt}", String.valueOf(ThreadLocalRandom.current().nextInt(0, 100000)));
        Matcher matcher = Pattern.compile("\\$\\{([^}]+)}").matcher(resolved);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            String value = postmanVariableValue(variableName);
            matcher.appendReplacement(buffer, value == null ? Matcher.quoteReplacement(matcher.group(0)) : Matcher.quoteReplacement(value));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private String randomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private String postmanVariableValue(String variableName) {
        if (variableName == null || variableName.isBlank()) {
            return null;
        }
        String savedType = savedVariableTypes.getOrDefault(variableName, "");
        String savedValue = savedVariables.get(variableName);
        if (savedValue != null && !"Postman Collection".equals(savedType) && !"Postman Environment".equals(savedType)) {
            return savedValue;
        }
        if (postmanEnvironmentVariables.containsKey(variableName)) {
            return postmanEnvironmentVariables.get(variableName);
        }
        if (postmanCollectionVariables.containsKey(variableName)) {
            return postmanCollectionVariables.get(variableName);
        }
        return savedValue;
    }

    private String extractJsonValue(String json, String path) {
        try {
            if (json == null || json.isBlank() || path == null || path.isBlank()) {
                return "";
            }
            Object current = new JSONTokener(json).nextValue();
            String normalized = path.startsWith("$.") ? path.substring(2) : path;
            for (String part : normalized.split("\\.")) {
                if (part.isBlank()) {
                    continue;
                }
                if (current instanceof JSONObject object) {
                    current = object.opt(part);
                } else if (current instanceof JSONArray array) {
                    int index = Integer.parseInt(part.replace("[", "").replace("]", ""));
                    current = array.opt(index);
                }
            }
            return current == null ? "" : String.valueOf(current);
        } catch (Exception e) {
            return "";
        }
    }

    private Object extractJsonPathValue(Object root, String path) {
        String normalized = path == null ? "" : path.trim();
        if (normalized.isEmpty() || "$".equals(normalized)) {
            return root;
        }
        if (normalized.startsWith("$.")) {
            normalized = normalized.substring(2);
        } else if (normalized.startsWith("$")) {
            normalized = normalized.substring(1);
        }
        Object current = root;
        for (String part : normalized.split("\\.")) {
            if (part.isBlank()) {
                continue;
            }
            current = stepIntoJsonPath(current, part);
        }
        return current;
    }

    private Object stepIntoJsonPath(Object current, String part) {
        String remaining = part;
        int bracketIndex = remaining.indexOf('[');
        Object value = bracketIndex <= 0 ? current : objectJsonField(current, remaining.substring(0, bracketIndex));
        if (bracketIndex < 0) {
            return objectJsonField(current, remaining);
        }
        while (bracketIndex >= 0) {
            int closeIndex = remaining.indexOf(']', bracketIndex);
            int index = Integer.parseInt(remaining.substring(bracketIndex + 1, closeIndex).trim());
            if (!(value instanceof JSONArray array)) {
                throw new IllegalArgumentException("Path segment is not an array: " + part);
            }
            value = array.get(index);
            bracketIndex = remaining.indexOf('[', closeIndex);
        }
        return value;
    }

    private Object objectJsonField(Object current, String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return current;
        }
        if (current instanceof JSONObject object) {
            if (!object.has(fieldName)) {
                throw new IllegalArgumentException("Response field not found: " + fieldName);
            }
            return object.get(fieldName);
        }
        if (current instanceof JSONArray array) {
            if (array.isEmpty()) {
                throw new IllegalArgumentException("Response array is empty for field: " + fieldName);
            }
            return objectJsonField(array.get(0), fieldName);
        }
        throw new IllegalArgumentException("Path segment is not an object: " + fieldName);
    }

    private String jsonValueType(Object value) {
        if (value == null || value == JSONObject.NULL) {
            return "null";
        }
        if (value instanceof JSONObject) {
            return "object";
        }
        if (value instanceof JSONArray) {
            return "array";
        }
        if (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte) {
            return "integer";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        return "string";
    }

    private List<String> fieldValidationErrors(String actualType, String actualValue,
                                               String nullRule, String typeRule, String expectedValue) {
        List<String> errors = new ArrayList<>();
        if ("Not Null".equals(nullRule) && "null".equals(actualType)) {
            errors.add("expected not null");
        } else if ("Null".equals(nullRule) && !"null".equals(actualType)) {
            errors.add("expected null");
        }
        if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !typeRule.equals(actualType)) {
            String expectedType = typeRule.toLowerCase();
            String normalizedActualType = actualType == null ? "" : actualType.toLowerCase();
            if (!expectedType.equals(normalizedActualType)
                    && !("number".equals(expectedType) && "integer".equals(normalizedActualType))) {
                errors.add("expected " + typeRule);
            }
        }
        if (!expectedValue.isBlank() && !expectedValue.equals(actualValue)) {
            errors.add("expected value mismatch");
        }
        return errors;
    }

    private Object dbColumnActualValue(List<Map<String, Object>> rows, String columnReference) {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("DB query returned no rows.");
        }
        String columnName = columnReference == null ? "" : columnReference.trim();
        int rowIndex = 0;
        Matcher matcher = java.util.regex.Pattern.compile("^(.+)\\[(\\d+)]$").matcher(columnName);
        if (matcher.matches()) {
            columnName = matcher.group(1).trim();
            rowIndex = Integer.parseInt(matcher.group(2));
        }
        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("DB row index " + rowIndex + " is not available for " + columnName);
        }
        Map<String, Object> row = rows.get(rowIndex);
        if (!row.containsKey(columnName)) {
            throw new IllegalArgumentException("DB column not found: " + columnName);
        }
        return row.get(columnName);
    }

    private List<String> dbColumnValidationErrors(String actualType, String actualValue,
                                                  String nullRule, String typeRule, String expectedValue) {
        List<String> errors = new ArrayList<>();
        boolean isNull = "null".equals(actualType);
        boolean isEmpty = actualValue == null || actualValue.isEmpty();
        boolean isBlank = actualValue == null || actualValue.isBlank();
        if ("Not Null".equals(nullRule) && isNull) {
            errors.add("expected not null");
        } else if ("Null".equals(nullRule) && !isNull) {
            errors.add("expected null");
        } else if ("Not Empty".equals(nullRule) && (isNull || isEmpty)) {
            errors.add("expected not empty");
        } else if ("Empty".equals(nullRule) && !isEmpty) {
            errors.add("expected empty");
        } else if ("Not Blank".equals(nullRule) && (isNull || isBlank)) {
            errors.add("expected not blank");
        } else if ("Blank".equals(nullRule) && !isBlank) {
            errors.add("expected blank");
        }
        if (!typeRule.isBlank() && !"Skip".equals(typeRule) && !dbTypeMatches(typeRule, actualType, actualValue)) {
            errors.add("expected " + typeRule);
        }
        if (!expectedValue.isBlank() && !expectedValue.equals(actualValue)) {
            errors.add("expected value mismatch");
        }
        return errors;
    }

    private boolean dbTypeMatches(String expectedType, String actualType, String actualValue) {
        String expected = expectedType == null ? "" : expectedType.toLowerCase();
        String actual = actualType == null ? "" : actualType.toLowerCase();
        if (expected.equals(actual)) {
            return true;
        }
        if ("number".equals(expected) && ("integer".equals(actual) || "decimal".equals(actual))) {
            return true;
        }
        if ("datetime".equals(expected) && "timestamp".equals(actual)) {
            return true;
        }
        if ("timestamp".equals(expected) && "datetime".equals(actual)) {
            return true;
        }
        if ("uuid".equals(expected)) {
            return actualValue.matches("(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
        }
        if ("json".equals(expected)) {
            String trimmed = actualValue.trim();
            try {
                if (trimmed.startsWith("{")) {
                    new JSONObject(trimmed);
                    return true;
                }
                if (trimmed.startsWith("[")) {
                    new JSONArray(trimmed);
                    return true;
                }
            } catch (Exception ignored) {
                return false;
            }
        }
        return false;
    }

    private String dbValueType(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return "integer";
        }
        if (value instanceof java.math.BigDecimal || value instanceof Float || value instanceof Double) {
            return "decimal";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        if (value instanceof java.sql.Date || value instanceof java.time.LocalDate) {
            return "date";
        }
        if (value instanceof java.sql.Time || value instanceof java.time.LocalTime) {
            return "time";
        }
        if (value instanceof java.sql.Timestamp || value instanceof java.time.Instant
                || value instanceof java.time.LocalDateTime || value instanceof java.time.OffsetDateTime) {
            return "timestamp";
        }
        return "string";
    }

    private boolean compareValues(String expected, String actual, String operator) {
        String op = operator == null ? "equals" : operator.toLowerCase();
        return switch (op) {
            case "not equals", "!=" -> !Objects.equals(expected, actual);
            case "contains" -> actual != null && actual.contains(expected);
            case "not empty" -> actual != null && !actual.isBlank();
            default -> Objects.equals(expected, actual);
        };
    }

    private void rebuildDynamicTable(TableView<Map<String, String>> table, ObservableList<Map<String, String>> target,
                                     List<Map<String, Object>> rows) {
        table.getColumns().clear();
        target.clear();
        if (rows == null || rows.isEmpty()) {
            table.getColumns().add(stringColumn("Result", "result"));
            return;
        }
        Set<String> columns = rows.get(0).keySet();
        for (String column : columns) {
            table.getColumns().add(stringColumn(column, column));
        }
        int index = 1;
        for (Map<String, Object> source : rows) {
            Map<String, String> mapped = new LinkedHashMap<>();
            mapped.put("row", String.valueOf(index++));
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                mapped.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            target.add(mapped);
        }
    }

    private TableView<Map<String, String>> mapTable(ObservableList<Map<String, String>> rows, String... columnPairs) {
        TableView<Map<String, String>> table = new TableView<>(rows);
        table.setEditable(true);
        table.setFixedCellSize(38);
        table.setMinHeight(220);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        for (int i = 0; i + 1 < columnPairs.length; i += 2) {
            String title = columnPairs[i];
            String key = columnPairs[i + 1];
            if ("selected".equals(key) || "jsonPath".equals(key) && "Add".equals(title)) {
                TableColumn<Map<String, String>, Boolean> column = new TableColumn<>(title);
                column.setEditable(true);
                column.setCellValueFactory(data -> {
                    Map<String, String> row = data.getValue();
                    SimpleBooleanProperty selected = new SimpleBooleanProperty(isSelected(row));
                    selected.addListener((observable, oldValue, newValue) ->
                            row.put("selected", String.valueOf(Boolean.TRUE.equals(newValue))));
                    return selected;
                });
                column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                table.getColumns().add(column);
            } else {
                table.getColumns().add(stringColumn(title, key));
            }
        }
        return table;
    }

    private TableColumn<Map<String, String>, String> stringColumn(String title, String key) {
        TableColumn<Map<String, String>, String> column = new TableColumn<>(title);
        column.setId(key);
        column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrDefault(key, "")));
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("status-badge-pass", "status-badge-fail");
                setText(null);
                setGraphic(null);
                if (empty || item == null) {
                    return;
                }
                if (isStatusColumn(key, title) && isPassFail(item)) {
                    Label badge = new Label(item.toUpperCase());
                    badge.getStyleClass().add(isPassingStatus(item) ? "status-badge-pass" : "status-badge-fail");
                    setGraphic(badge);
                } else {
                    setText(item);
                }
            }
        });
        return column;
    }

    private boolean isStatusColumn(String key, String title) {
        String normalized = (key + " " + title).toLowerCase();
        return normalized.contains("result") || normalized.contains("status");
    }

    private boolean isPassFail(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase();
        return normalized.equals("pass") || normalized.equals("passed")
                || normalized.equals("fail") || normalized.equals("failed");
    }

    private boolean isPassingStatus(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase();
        return normalized.equals("pass") || normalized.equals("passed");
    }

    private boolean isSelected(Map<String, String> row) {
        return "true".equalsIgnoreCase(row.getOrDefault("selected", row.getOrDefault("jsonPath", "false")));
    }

    private Map<String, String> row(String... values) {
        Map<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i + 1 < values.length; i += 2) {
            row.put(values[i], values[i + 1] == null ? "" : values[i + 1]);
        }
        return row;
    }

    private static class TestSuiteStepResult {
        final String suite;
        final String testCase;
        final String stepName;
        final String type;
        String status;
        boolean passed;
        final List<String> details = new ArrayList<>();
        final List<TestSuiteValidationRow> validations = new ArrayList<>();

        TestSuiteStepResult(Map<String, String> row, String status, boolean passed) {
            this.suite = row.getOrDefault("suite", "");
            this.testCase = row.getOrDefault("case", "");
            this.stepName = row.getOrDefault("step", "");
            this.type = row.getOrDefault("type", "");
            this.status = status;
            this.passed = passed;
        }

        static TestSuiteStepResult failed(Map<String, String> row, String message) {
            TestSuiteStepResult result = new TestSuiteStepResult(row, "Failed: " + message, false);
            result.addValidation("Step Error", "Execution failed", "", "", false, message);
            return result;
        }

        static TestSuiteStepResult stopped(Map<String, String> row) {
            TestSuiteStepResult result = new TestSuiteStepResult(row, "Stopped", false);
            result.addValidation("Step", "Execution stopped", "", "", false, "Execution stopped before this step completed.");
            return result;
        }

        void addValidation(String field, String validation, String expected, String actual, boolean passed, String message) {
            TestSuiteValidationRow row = new TestSuiteValidationRow();
            row.field = field == null ? "" : field;
            row.validation = validation == null ? "" : validation;
            row.expected = expected == null ? "" : expected;
            row.actual = actual == null ? "" : actual;
            row.passed = passed;
            row.message = message == null ? "" : message;
            validations.add(row);
        }
    }

    private static class TestSuiteValidationRow {
        String field;
        String validation;
        String expected;
        String actual;
        boolean passed;
        String message;
    }

    private static class ApiAiSuggestion {
        final ApiRequest request;
        final ApiResponse response;
        final String actionName;
        final List<Map<String, String>> variables = new ArrayList<>();
        final List<Map<String, String>> validations = new ArrayList<>();
        final List<Map<String, String>> dbMappings = new ArrayList<>();

        ApiAiSuggestion(ApiRequest request, ApiResponse response, String actionName) {
            this.request = request;
            this.response = response;
            this.actionName = actionName;
        }
    }

    private static class DbAiSuggestion {
        final List<Map<String, String>> apiDbMappings = new ArrayList<>();
        final List<Map<String, String>> dbValidations = new ArrayList<>();
        final List<Map<String, String>> variables = new ArrayList<>();
    }

    private static class WebAiSuggestion {
        final List<Map<String, String>> failures = new ArrayList<>();
        final List<Map<String, String>> fixes = new ArrayList<>();
    }

    private static class PostmanCollectionRunResult {
        final String name;
        final int total;
        int passed;
        int failed;
        int errors;
        final List<String> lines = new ArrayList<>();

        PostmanCollectionRunResult(String name, int total) {
            this.name = name;
            this.total = total;
        }

        void add(String requestName, int statusCode, String status, String message) {
            if ("PASS".equals(status)) {
                passed++;
            } else if ("FAIL".equals(status)) {
                failed++;
            } else {
                errors++;
            }
            String httpStatus = statusCode > 0 ? " HTTP " + statusCode : "";
            lines.add(status + httpStatus + " - " + requestName
                    + (message == null || message.isBlank() ? "" : " - " + message));
        }

        String summary() {
            return "Ran " + total + " request(s) from " + name + ": "
                    + passed + " passed, " + failed + " failed, " + errors + " error(s).";
        }

        String details() {
            return summary() + System.lineSeparator() + System.lineSeparator() + String.join(System.lineSeparator(), lines);
        }
    }

    private static class PostmanCollectionNode {
        final String name;
        final JSONObject request;
        final JSONObject source;
        final String kind;

        private PostmanCollectionNode(String name, JSONObject request, JSONObject source, String kind) {
            this.name = name;
            this.request = request;
            this.source = source == null ? new JSONObject() : source;
            this.kind = kind;
        }

        static PostmanCollectionNode collection(String name, JSONObject source) {
            return new PostmanCollectionNode(name, null, source, "collection");
        }

        static PostmanCollectionNode folder(String name, JSONObject source) {
            return new PostmanCollectionNode(name, null, source, "folder");
        }

        static PostmanCollectionNode request(String name, JSONObject request, JSONObject source) {
            return new PostmanCollectionNode(name, request, source, "request");
        }

        static PostmanCollectionNode placeholder(String name) {
            return new PostmanCollectionNode(name, null, new JSONObject(), "placeholder");
        }

        boolean isRequest() {
            return request != null;
        }

        @Override
        public String toString() {
            if (isRequest()) {
                return request.optString("method", "GET") + "  " + name;
            }
            return name;
        }
    }

    private static class ProcessResult {
        final int exitCode;
        final String output;

        ProcessResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output == null ? "" : output;
        }

        boolean success() {
            return exitCode == 0;
        }
    }

    private record HermesSessionRecord(
            String sessionName,
            String sessionId,
            String title,
            String resumeCommand,
            String transcriptPath,
            String aiAgentPath,
            String containerName) {
    }

    private record HermesSessionCapture(
            String sessionId,
            String title,
            String resumeCommand,
            String transcriptPath,
            String aiAgentPath,
            String containerName,
            String transcript) {

        static HermesSessionCapture empty(Path transcriptPath, String containerName, Path aiAgentPath) {
            return new HermesSessionCapture("", "", "", transcriptPath == null ? "" : transcriptPath.toString(),
                    aiAgentPath == null ? "" : aiAgentPath.toString(), containerName == null ? "" : containerName, "");
        }
    }

    private static class WorkbookSheet {
        final String name;
        final String path;

        WorkbookSheet(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    private static class BuilderTreeNode {
        final String id = UUID.randomUUID().toString();
        final String kind;
        final String displayName;
        final String suite;
        final String testCase;
        final List<Map<String, String>> rows;

        BuilderTreeNode(String kind, String displayName, String suite, String testCase, List<Map<String, String>> rows) {
            this.kind = kind;
            this.displayName = displayName;
            this.suite = suite == null ? "" : suite;
            this.testCase = testCase == null ? "" : testCase;
            this.rows = rows == null ? List.of() : rows;
        }

        static BuilderTreeNode root() {
            return new BuilderTreeNode("ROOT", "Suite Builder", "", "", List.of());
        }

        static BuilderTreeNode placeholder(String text) {
            return new BuilderTreeNode("PLACEHOLDER", text, "", "", List.of());
        }

        static BuilderTreeNode suite(String suite, List<Map<String, String>> rows) {
            return new BuilderTreeNode("SUITE", suite, suite, "", rows);
        }

        static BuilderTreeNode testCase(String testCase, List<Map<String, String>> rows) {
            String suite = rows.isEmpty() ? "" : rows.get(0).getOrDefault("suite", "");
            return new BuilderTreeNode("CASE", testCase, suite, testCase, rows);
        }

        static BuilderTreeNode step(Map<String, String> row) {
            String step = row.getOrDefault("step", "Test Step");
            String type = row.getOrDefault("type", "Step");
            return new BuilderTreeNode("STEP", step + " [" + type + "]",
                    row.getOrDefault("suite", ""), row.getOrDefault("case", ""), List.of(row));
        }

        boolean draggable() {
            return "SUITE".equals(kind) || "CASE".equals(kind) || "STEP".equals(kind);
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private String normalizeVariableName(String value) {
        if (value == null || value.isBlank()) {
            return "variable";
        }
        return value.replace("${", "").replace("}", "").replaceAll("[^A-Za-z0-9_]", "_");
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    private String valueAt(Object[] values, int index) {
        return values != null && index < values.length && values[index] != null ? String.valueOf(values[index]) : "";
    }

    private String valueAt(String[] values, int index) {
        return values != null && index < values.length && values[index] != null ? values[index] : "";
    }

    private void start(Task<?> task) {
        showGlobalLoading(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> showGlobalLoading(false));
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> showGlobalLoading(false));
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, event -> showGlobalLoading(false));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showGlobalLoading(boolean active) {
        Platform.runLater(() -> {
            int count = active ? activeTaskCount.incrementAndGet() : Math.max(0, activeTaskCount.decrementAndGet());
            if (globalLoadingBar != null) {
                globalLoadingBar.setVisible(count > 0);
                globalLoadingBar.setManaged(count > 0);
            }
        });
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private ComboBox<String> combo(String first, String... rest) {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().add(first);
        box.getItems().addAll(rest);
        box.setValue(first);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private TextArea editor(String text) {
        TextArea area = new TextArea(text);
        area.setWrapText(false);
        area.getStyleClass().add("editor");
        area.setMinWidth(240);
        area.setMinHeight(120);
        return area;
    }

    private TextArea requestEditor(String text) {
        TextArea area = editor(text);
        area.getStyleClass().add("request-editor");
        return area;
    }

    private TextArea responseEditor(String text) {
        TextArea area = editor(text);
        area.getStyleClass().add("response-editor");
        area.setMinHeight(320);
        area.setWrapText(true);
        area.setScrollTop(0);
        return area;
    }

    private Label metric(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("metric");
        return label;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private Button primary(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        decorateButton(button, text);
        return button;
    }

    private Button secondary(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        decorateButton(button, text);
        if (isDangerAction(text)) {
            button.getStyleClass().add("danger-button");
        }
        return button;
    }

    private Button windowControl(String iconLiteral, String accessibleText) {
        Button button = new Button();
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(15);
        button.setGraphic(icon);
        button.setAccessibleText(accessibleText);
        button.getStyleClass().add("window-control-button");
        button.setFocusTraversable(false);
        return button;
    }

    private void decorateButton(Button button, String text) {
        button.setGraphic(iconFor(text));
        button.setGraphicTextGap(8);
        button.setMinHeight(36);
    }

    private boolean isDangerAction(String text) {
        String normalized = text == null ? "" : text.toLowerCase();
        return normalized.contains("delete")
                || normalized.contains("remove")
                || normalized.contains("clear")
                || normalized.contains("stop");
    }

    private Button primaryButton(String text, EventHandler<ActionEvent> handler) {
        Button button = primary(text);
        button.setOnAction(handler);
        return button;
    }

    private Button secondaryButton(String text, EventHandler<ActionEvent> handler) {
        Button button = secondary(text);
        button.setOnAction(handler);
        return button;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        return grid;
    }

    private VBox labeled(String label, javafx.scene.Node node) {
        Label title = new Label(label);
        title.getStyleClass().add("field-label");
        VBox box = new VBox(8, title, node);
        box.setMinWidth(0);
        if (node instanceof Region region) {
            region.setMaxWidth(Double.MAX_VALUE);
        }
        VBox.setVgrow(node, Priority.ALWAYS);
        return box;
    }

    private HBox wrapTokenField(Button toggleButton) {
        HBox wrapper = new HBox(8, tokenField, visibleTokenField, toggleButton);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setMinWidth(0);
        HBox.setHgrow(tokenField, Priority.ALWAYS);
        HBox.setHgrow(visibleTokenField, Priority.ALWAYS);
        tokenField.setMaxWidth(Double.MAX_VALUE);
        visibleTokenField.setMaxWidth(Double.MAX_VALUE);
        return wrapper;
    }

    private HBox wrapTextFieldWithActions(TextField field, javafx.scene.Node... actions) {
        HBox wrapper = new HBox(8);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setMinWidth(0);
        wrapper.getChildren().add(field);
        wrapper.getChildren().addAll(actions);
        HBox.setHgrow(field, Priority.ALWAYS);
        field.setMaxWidth(Double.MAX_VALUE);
        return wrapper;
    }

    private HBox wrapDbPasswordField(Button toggleButton) {
        HBox wrapper = new HBox(8, dbPasswordField, visibleDbPasswordField, toggleButton);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setMinWidth(0);
        HBox.setHgrow(dbPasswordField, Priority.ALWAYS);
        HBox.setHgrow(visibleDbPasswordField, Priority.ALWAYS);
        dbPasswordField.setMaxWidth(Double.MAX_VALUE);
        visibleDbPasswordField.setMaxWidth(Double.MAX_VALUE);
        return wrapper;
    }

    private FlowPane actionRow(javafx.scene.Node... children) {
        FlowPane row = new FlowPane(16, 8, children);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWrapLength(900);
        row.setMinWidth(0);
        return row;
    }

    private FlowPane spacedActionRow(javafx.scene.Node... children) {
        FlowPane row = new FlowPane(16, 12, children);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWrapLength(1200);
        row.setMinWidth(0);
        for (javafx.scene.Node child : children) {
            if (child instanceof Button button) {
                button.setMinWidth(130);
            }
        }
        return row;
    }

    private VBox card(String title, javafx.scene.Node content) {
        VBox card = new VBox(16, sectionTitle(title), content);
        card.getStyleClass().add("card");
        card.setMinWidth(0);
        VBox.setVgrow(content, Priority.ALWAYS);
        return card;
    }

    private BorderPane withFooter(javafx.scene.Node content, javafx.scene.Node footer) {
        BorderPane pane = new BorderPane(content);
        pane.setBottom(footer);
        pane.setMinWidth(0);
        BorderPane.setMargin(footer, new Insets(16, 0, 0, 0));
        return pane;
    }

    private BorderPane wrap(javafx.scene.Node content) {
        BorderPane pane = new BorderPane(content);
        pane.getStyleClass().add("card");
        pane.setPadding(new Insets(16));
        pane.setMinWidth(0);
        return pane;
    }

    private ScrollPane verticalSectionScroll(javafx.scene.Node content, double minHeight) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);
        scrollPane.setMinHeight(minHeight);
        scrollPane.setPrefHeight(minHeight);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("workspace-scroll");
        return scrollPane;
    }

    private ScrollPane padded(javafx.scene.Node content) {
        BorderPane pane = new BorderPane(content);
        pane.setPadding(new Insets(24));
        pane.setMinWidth(0);
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("workspace-scroll");
        return scrollPane;
    }

    private File chooseOpenFile(String description, String extension) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        return chooser.showOpenDialog(stage);
    }

    private File chooseOpenFile(String description, String extension, Path initialDirectory) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        applyInitialDirectory(chooser, initialDirectory);
        return chooser.showOpenDialog(stage);
    }

    private File chooseSaveFile(String description, String extension, String initialName, Path initialDirectory) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(initialName);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        applyInitialDirectory(chooser, initialDirectory);
        return chooser.showSaveDialog(stage);
    }

    private void applyInitialDirectory(FileChooser chooser, Path initialDirectory) {
        if (chooser == null || initialDirectory == null) {
            return;
        }
        try {
            Files.createDirectories(initialDirectory);
            File directory = initialDirectory.toFile();
            if (directory.isDirectory()) {
                chooser.setInitialDirectory(directory);
            }
        } catch (Exception ignored) {
            // Fall back to the platform default file chooser location.
        }
    }

    private void saveTextFile(String text, String initialName) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(initialName);
        File file = chooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        try {
            Files.writeString(file.toPath(), text == null ? "" : text, StandardCharsets.UTF_8);
        } catch (Exception e) {
            showError("Save Failed", e);
        }
    }

    private void saveTextFile(String text, String initialName, Path initialDirectory) {
        File file = chooseSaveFile("Text Files", "*.*", initialName, initialDirectory);
        if (file == null) {
            return;
        }
        try {
            Files.writeString(file.toPath(), text == null ? "" : text, StandardCharsets.UTF_8);
        } catch (Exception e) {
            showError("Save Failed", e);
        }
    }

    private void loadTextFile(TextArea target) {
        File file = chooseOpenFile("Text Files", "*.*");
        if (file == null) {
            return;
        }
        try {
            target.setText(Files.readString(file.toPath(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            showError("Load Failed", e);
        }
    }

    private void loadTextFile(TextArea target, Path initialDirectory) {
        File file = chooseOpenFile("Text Files", "*.*", initialDirectory);
        if (file == null) {
            return;
        }
        try {
            target.setText(Files.readString(file.toPath(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            showError("Load Failed", e);
        }
    }

    private void openPath(Path path, String missingMessage) {
        if (path == null || !Files.exists(path)) {
            showWarning("Open File", missingMessage);
            return;
        }
        getHostServices().showDocument(path.toUri().toString());
    }

    private String formatDuration(Duration duration) {
        if (duration == null) {
            return "--";
        }
        long millis = duration.toMillis();
        return millis < 1000 ? millis + " ms" : String.format("%.2f s", millis / 1000.0);
    }

    private void showInfo(String title, String message) {
        showToast("OK " + message, "toast-success");
    }

    private void showWarning(String title, String message) {
        showToast("Warning " + message, "toast-error");
        alert(Alert.AlertType.WARNING, title, message, null);
    }

    private void showError(String title, Throwable throwable) {
        showToast("Failed " + (throwable == null ? title : exceptionMessage(throwable)), "toast-error");
        alert(Alert.AlertType.ERROR, title, throwable == null ? "Unknown error" : throwable.getMessage(), throwable);
    }

    private void alert(Alert.AlertType type, String title, String message, Throwable throwable) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(title);
            if (stage != null) {
                alert.initOwner(stage);
            }
            styleDialogPane(alert.getDialogPane());
            if (throwable != null) {
                TextArea details = new TextArea(String.valueOf(throwable));
                details.setEditable(false);
                alert.getDialogPane().setExpandableContent(details);
            }
            alert.showAndWait();
        });
    }

    private void styleDialogPane(javafx.scene.control.DialogPane dialogPane) {
        if (dialogPane == null) {
            return;
        }
        dialogPane.getStylesheets().add(createInlineStylesheet());
        URL stylesheetUrl = ApiValidatorFxApp.class.getResource(APP_STYLESHEET_RESOURCE);
        if (stylesheetUrl != null) {
            dialogPane.getStylesheets().add(stylesheetUrl.toExternalForm());
        }
        dialogPane.getStyleClass().add("testweave-dialog");
    }

    private HBox themedSubwindowHeader(String titleText, Dialog<?> dialog) {
        Label title = new Label(titleText);
        title.getStyleClass().add("app-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button minimize = windowControl("fth-minus", "Minimize");
        Button maximize = windowControl("fth-square", "Maximize or restore");
        Button close = windowControl("fth-x", "Close");
        close.getStyleClass().add("window-close-button");
        minimize.setOnAction(e -> {
            if (dialog.getDialogPane().getScene() != null
                    && dialog.getDialogPane().getScene().getWindow() instanceof Stage dialogStage) {
                dialogStage.setIconified(true);
            }
        });
        maximize.setOnAction(e -> {
            if (dialog.getDialogPane().getScene() != null
                    && dialog.getDialogPane().getScene().getWindow() instanceof Stage dialogStage) {
                dialogStage.setMaximized(!dialogStage.isMaximized());
            }
        });
        close.setOnAction(e -> {
            javafx.scene.Node cancel = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            if (cancel instanceof Button button) {
                button.fire();
            } else {
                dialog.close();
            }
        });
        HBox controls = new HBox(8, minimize, maximize, close);
        controls.getStyleClass().add("window-controls");
        HBox header = new HBox(12, title, spacer, controls);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 14, 10, 14));
        header.getStyleClass().add("top-bar");
        final double[] offsets = new double[2];
        header.setOnMousePressed(event -> {
            offsets[0] = event.getSceneX();
            offsets[1] = event.getSceneY();
        });
        header.setOnMouseDragged(event -> {
            if (dialog.getDialogPane().getScene() != null
                    && dialog.getDialogPane().getScene().getWindow() instanceof Stage dialogStage
                    && !dialogStage.isMaximized()) {
                dialogStage.setX(event.getScreenX() - offsets[0]);
                dialogStage.setY(event.getScreenY() - offsets[1]);
            }
        });
        return header;
    }

    private void loadApplicationIcon(Stage stage) {
        URL logoUrl = ApiValidatorFxApp.class.getResource(APP_LOGO_RESOURCE);
        if (logoUrl != null) {
            stage.getIcons().add(new Image(logoUrl.toExternalForm()));
        }
    }

    private void cleanupBeforeClose() {
        disconnectApiAiHermesAgentOnExit();
        playwrightRecorderController.stopRecording();
        playwrightRecorderController.stopRunningWebTest();
    }

    private void addApplicationStylesheet(Scene scene) {
        URL stylesheetUrl = ApiValidatorFxApp.class.getResource(APP_STYLESHEET_RESOURCE);
        if (stylesheetUrl != null) {
            scene.getStylesheets().add(stylesheetUrl.toExternalForm());
        }
    }

    private FontIcon iconFor(String text) {
        FontIcon icon = new FontIcon(iconLiteralFor(text));
        icon.setIconSize(16);
        return icon;
    }

    private String iconLiteralFor(String text) {
        String normalized = text == null ? "" : text.toLowerCase();
        if (normalized.contains("api") || normalized.contains("request") || normalized.contains("endpoint")) return "fth-globe";
        if (normalized.contains("db") || normalized.contains("database") || normalized.contains("query")) return "fth-database";
        if (normalized.contains("web") || normalized.contains("ui") || normalized.contains("browser")) return "fth-monitor";
        if (normalized.contains("workflow") || normalized.contains("suite") || normalized.contains("builder")) return "fth-git-branch";
        if (normalized.contains("run") || normalized.contains("send") || normalized.contains("record") || normalized.contains("start")) return "fth-play";
        if (normalized.contains("save") || normalized.contains("create") || normalized.contains("import")) return "fth-save";
        if (normalized.contains("report") || normalized.contains("dashboard") || normalized.contains("result")) return "fth-file-text";
        if (normalized.contains("delete") || normalized.contains("remove") || normalized.contains("clear")) return "fth-trash-2";
        if (normalized.contains("copy")) return "fth-copy";
        if (normalized.contains("load") || normalized.contains("open") || normalized.contains("browse")) return "fth-folder";
        if (normalized.contains("ai") || normalized.contains("hermes") || normalized.contains("codex")) return "fth-cpu";
        if (normalized.contains("validation") || normalized.contains("validate") || normalized.contains("compare")) return "fth-check-circle";
        if (normalized.contains("variables") || normalized.contains("variable")) return "fth-sliders";
        if (normalized.contains("config")) return "fth-settings";
        return "fth-circle";
    }

    private void showToast(String message, String styleClass) {
        if (stage == null || !stage.isShowing()) {
            return;
        }
        Platform.runLater(() -> {
            Label toast = new Label(message == null ? "" : message);
            toast.getStyleClass().add("toast");
            if (styleClass != null && !styleClass.isBlank()) {
                toast.getStyleClass().add(styleClass);
            }
            Popup popup = new Popup();
            popup.setAutoFix(true);
            popup.setAutoHide(true);
            popup.getContent().add(toast);
            popup.show(stage);
            double x = stage.getX() + stage.getWidth() - toast.prefWidth(-1) - 32;
            double y = stage.getY() + 72;
            popup.setX(Math.max(stage.getX() + 24, x));
            popup.setY(y);
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2.6));
            delay.setOnFinished(event -> popup.hide());
            delay.play();
        });
    }

    private String createInlineStylesheet() {
        return "data:text/css," + """
                .root { -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-background-color: %s; }
                .label { -fx-text-fill: %s; }
                .top-bar { -fx-background-color: linear-gradient(to right, #06091f, #111b3d, #271052); -fx-border-color: transparent transparent #243b78 transparent; -fx-effect: dropshadow(gaussian, rgba(24,216,232,0.22), 16, 0.22, 0, 2); }
                .app-title { -fx-font-size: 21px; -fx-font-weight: 800; -fx-text-fill: linear-gradient(to right, #ffffff, #2f7cff, #8b4df6, #18d8e8); }
                .muted { -fx-text-fill: %s; }
                .section-title { -fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #ffffff; }
                .field-label { -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: #74eaff; }
                .metric { -fx-font-weight: 800; -fx-text-fill: #ffffff; }
                .dashboard-metric-card { -fx-background-color: linear-gradient(to bottom right, #17265a, #241454); -fx-border-color: #3c62bc; -fx-min-height: 104px; }
                .dashboard-metric-title { -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: #74eaff; }
                .dashboard-metric-card .metric { -fx-font-size: 22px; }
                .weaving-title { -fx-font-size: 52px; -fx-font-weight: 900; -fx-text-fill: linear-gradient(to right, #2f7cff, #8b4df6, #18d8e8); }
                .chart-title, .axis-label, .chart-legend-item { -fx-text-fill: #f8fbff; }
                .chart-legend { -fx-background-color: rgba(248,251,255,0.12); -fx-background-radius: 6px; -fx-padding: 8px; }
                .chart-legend-item .label, .chart-legend-item { -fx-text-fill: #f8fbff; }
                .chart-pie-label { -fx-fill: #f8fbff; -fx-font-weight: 700; }
                .chart-pie-label-line { -fx-stroke: #dbe8ff; -fx-stroke-width: 1.2px; }
                .axis { -fx-tick-label-fill: #c8d7ff; }
                .chart-plot-background { -fx-background-color: #0d1430; }
                .chart-vertical-grid-lines, .chart-horizontal-grid-lines { -fx-stroke: #243b78; }
                .card { -fx-background-color: %s; -fx-border-color: #243b78; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.24), 14, 0.18, 0, 3); }
                .editor { -fx-font-family: 'Consolas'; -fx-font-size: 13px; }
                .vision-terminal { -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: #f8fbff; -fx-highlight-fill: #2f7cff; -fx-highlight-text-fill: #ffffff; -fx-prompt-text-fill: #a9bbdf; }
                .vision-terminal .content { -fx-background-color: #012456; }
                .vision-terminal:focused .content { -fx-background-color: #012456; }
                .vision-terminal .text { -fx-fill: #f8fbff; }
                .vision-terminal .scroll-pane, .vision-terminal .viewport { -fx-background-color: #012456; }
                .request-editor .scroll-pane { -fx-hbar-policy: as-needed; -fx-vbar-policy: always; }
                .response-editor { -fx-font-size: 14px; }
                .response-editor .scroll-pane { -fx-hbar-policy: never; -fx-vbar-policy: always; }
                .capture-panel { -fx-background-color: %s; -fx-padding: 10px; }
                .capture-toolbar { -fx-background-color: %s; -fx-padding: 4px 0 8px 0; }
                .validation-toolbar { -fx-background-color: %s; -fx-padding: 12px; -fx-border-color: #243b78; -fx-border-radius: 6px; -fx-background-radius: 6px; }
                .context-panel { -fx-hgap: 20px; -fx-vgap: 10px; }
                .context-button { -fx-padding: 9px 18px; }
                .db-workflow { -fx-padding: 0 0 18px 0; }
                .workspace-scroll, .scroll-pane { -fx-background-color: %s; -fx-border-color: #243b78; }
                .workspace-scroll > .viewport, .scroll-pane > .viewport { -fx-background-color: %s; }
                .builder-canvas { -fx-background-color: #080d24; }
                .builder-tree-pane { -fx-background-color: %s; -fx-border-color: #243b78; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 12px; }
                .builder-node { -fx-background-color: #f8fbff; -fx-border-width: 0 0 0 5px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 10px; -fx-effect: dropshadow(gaussian, rgba(24,216,232,0.22), 12, 0.2, 0, 2); }
                .builder-node .label { -fx-text-fill: #172033; }
                .builder-node .muted { -fx-text-fill: #4b5d86; }
                .builder-node .metric { -fx-text-fill: #0f1730; }
                .builder-node-selected { -fx-border-width: 2px 2px 2px 6px; -fx-border-color: #18d8e8; }
                .builder-node-title { -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #0f1730; }
                .builder-connector { -fx-stroke: #18d8e8; -fx-stroke-width: 2px; -fx-opacity: 0.75; }
                .builder-connector-selected { -fx-stroke: #ff4fc3; -fx-stroke-width: 4px; -fx-opacity: 1; }
                .builder-connector-handle { -fx-fill: #ffffff; -fx-stroke: #8b4df6; -fx-stroke-width: 2px; }
                .builder-arrow { -fx-fill: #18d8e8; -fx-opacity: 0.85; }
                .builder-progress { -fx-pref-height: 7px; }
                .builder-progress .track { -fx-background-color: #d7def4; }
                .builder-progress .bar { -fx-background-color: linear-gradient(to right, #2f7cff, #8b4df6, #18d8e8); }
                .primary-button { -fx-background-color: linear-gradient(to right, #2f7cff, #7048ff); -fx-text-fill: #ffffff; -fx-font-weight: 800; -fx-background-radius: 5px; -fx-padding: 8px 16px; }
                .primary-button:hover { -fx-background-color: linear-gradient(to right, #4b91ff, #8b4df6); }
                .secondary-button { -fx-background-color: #151f45; -fx-text-fill: #f8fbff; -fx-border-color: #3454a4; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 8px 14px; }
                .secondary-button:hover { -fx-background-color: #1b2b61; -fx-border-color: #18d8e8; }
                .danger-button { -fx-background-color: #d8385e; -fx-text-fill: #ffffff; -fx-border-color: #ff7aa0; -fx-font-weight: 800; }
                .storage-toggle { -fx-min-width: 126px; -fx-pref-width: 126px; -fx-background-radius: 999px; -fx-border-radius: 999px; -fx-padding: 9px 22px; -fx-background-color: linear-gradient(to bottom, #2d3347, #111827); -fx-border-color: #d7deea; -fx-border-width: 2px; -fx-text-fill: #ffffff; -fx-font-weight: 900; -fx-effect: innershadow(gaussian, rgba(0,0,0,0.55), 9, 0.35, 0, 2); }
                .storage-toggle:selected { -fx-background-color: linear-gradient(to right, #ff8a00, #f04e23); -fx-border-color: #ffe1ba; -fx-text-fill: #ffffff; }
                .storage-toggle:hover { -fx-border-color: #18d8e8; }
                .web-option-check { -fx-text-fill: #ffffff; -fx-padding: 8px 12px; }
                .tab-pane { -fx-background-color: %s; }
                .tab-pane .tab-header-area .tab-header-background { -fx-background-color: #06091f; }
                .tab { -fx-background-color: #101936; -fx-border-color: #243b78; -fx-border-radius: 5px 5px 0 0; -fx-background-radius: 5px 5px 0 0; }
                .tab:selected { -fx-background-color: linear-gradient(to right, #1d3579, #40248c); -fx-border-color: #18d8e8; }
                .tab .tab-label { -fx-text-fill: #c8d7ff; -fx-font-weight: 700; }
                .tab:selected .tab-label { -fx-text-fill: #ffffff; }
                .text-field, .password-field, .text-area, .combo-box-base, .spinner, .table-view, .tree-view { -fx-background-color: #f8fbff; -fx-text-fill: #111827; -fx-control-inner-background: #f8fbff; -fx-border-color: #57b7ff; -fx-border-radius: 5px; -fx-background-radius: 5px; }
                .text-area .content { -fx-background-color: #f8fbff; }
                .text-input { -fx-text-fill: #111827; -fx-prompt-text-fill: #60708f; }
                .combo-box .list-cell, .choice-box .label { -fx-text-fill: #111827; }
                .combo-box-popup .list-view { -fx-background-color: #ffffff; -fx-border-color: #57b7ff; }
                .combo-box-popup .list-cell { -fx-text-fill: #111827; -fx-background-color: #ffffff; }
                .combo-box-popup .list-cell:hover, .combo-box-popup .list-cell:selected { -fx-background-color: #e8f4ff; -fx-text-fill: #101936; }
                .table-view .column-header-background, .table-view .column-header { -fx-background-color: #101936; }
                .table-view .column-header .label { -fx-text-fill: #ffffff; -fx-font-weight: 800; }
                .table-row-cell { -fx-background-color: #ffffff; -fx-text-fill: #111827; }
                .table-row-cell:odd { -fx-background-color: #eef5ff; }
                .table-row-cell:selected { -fx-background-color: #c9e8ff; }
                .table-cell { -fx-text-fill: #111827; -fx-border-color: #d5e6ff; }
                .tree-cell { -fx-background-color: #f8fbff; -fx-text-fill: #111827; }
                .tree-cell:selected { -fx-background-color: #c9e8ff; -fx-text-fill: #101936; }
                .progress-bar .track { -fx-background-color: #18234b; }
                .progress-bar .bar { -fx-background-color: linear-gradient(to right, #2f7cff, #8b4df6, #18d8e8); }
                """.formatted(APP_BACKGROUND, TEXT_PRIMARY, TEXT_MUTED, PANEL_SURFACE, PANEL_BACKGROUND,
                PANEL_BACKGROUND, PANEL_SURFACE, APP_BACKGROUND, APP_BACKGROUND, PANEL_SURFACE,
                APP_BACKGROUND).replace("\n", "%0A").replace(" ", "%20").replace("#", "%23");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
